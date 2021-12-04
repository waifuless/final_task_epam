package by.epam.finaltask.command.async_command;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

public class AjaxCommandFactory {

    private final static Logger LOG = LogManager.getLogger(AjaxCommandFactory.class);

    private static volatile AjaxCommandFactory instance;
    private final ConcurrentMap<AjaxCommandVariant, AjaxCommand> commands = new ConcurrentHashMap<>();

    private AjaxCommandFactory() {
    }

    public static AjaxCommandFactory getInstance() {
        if (instance == null) {
            synchronized (AjaxCommandFactory.class) {
                if (instance == null) {
                    instance = new AjaxCommandFactory();
                }
            }
        }
        return instance;
    }

    public Optional<AjaxCommand> findCommand(String commandName) {
        if (commandName == null) {
            return Optional.empty();
        }
        Optional<AjaxCommandVariant> optionalCommandClassByName = AjaxCommandVariant.findByName(commandName);
        if (optionalCommandClassByName.isPresent()) {
            return findCommand(optionalCommandClassByName.get());
        } else {
            return Optional.empty();
        }
    }

    public Optional<AjaxCommand> findCommand(AjaxCommandVariant ajaxCommandVariant) {
        if (ajaxCommandVariant == null) {
            return Optional.empty();
        }
        return Optional.ofNullable(commands.computeIfAbsent(ajaxCommandVariant, this::createCommand));
    }

    private AjaxCommand createCommand(AjaxCommandVariant ajaxCommandVariant) {
        try {
            return ajaxCommandVariant.commandClass.newInstance();
        } catch (Exception e) {
            LOG.error(e.getMessage(), e);
            return null;
        }
    }

    public enum AjaxCommandVariant {
        SHOW_SIGN_IN("upload_image", UploadImageCommand.class),
        FIND_CITIES_OR_DISTRICTS_BY_REGION("find_cities_or_districts_by_region",
                FindCitiesOrDistrictsByRegionCommand.class),
        FIND_LOTS_BY_ADMIN("find_lots_by_admin", FindLotsByAdminCommand.class),
        ADD_CATEGORY("add_category", AddCategoryCommand.class),
        DELETE_CATEGORIES("delete_categories", DeleteCategoriesCommand.class),
        UPDATE_CATEGORY("update_category", UpdateCategoryCommand.class),
        UPDATE_AUCTION_STATUS("update_auction_status", UpdateAuctionStatusCommand.class),
        FIND_LOTS_BY_USER("find_lots_by_user", FindLotsByUserCommand.class),
        VALIDATE_AUCTION_START_DATE("validateAuctionStartDate", ValidateDateInLotCreationCommand.class);

        private final String commandName;
        private final Class<? extends AjaxCommand> commandClass;

        AjaxCommandVariant(String commandName, Class<? extends AjaxCommand> commandClass) {
            this.commandName = commandName;
            this.commandClass = commandClass;
        }

        public static Optional<AjaxCommandVariant> findByName(String commandName) {
            for (AjaxCommandVariant value : values()) {
                if (value.commandName.equals(commandName)) {
                    return Optional.of(value);
                }
            }
            return Optional.empty();
        }

        public String getCommandName() {
            return commandName;
        }
    }
}
