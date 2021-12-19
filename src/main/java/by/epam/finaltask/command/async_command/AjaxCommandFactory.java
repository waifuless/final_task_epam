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
        FIND_LOTS_AND_PAGES_COUNT_BY_ADMIN("find_lots_and_pages_count_by_admin",
                FindLotsAndPagesCountByAdminCommand.class),
        ADD_CATEGORY("add_category", AddCategoryCommand.class),
        DELETE_CATEGORIES("delete_categories", DeleteCategoriesCommand.class),
        UPDATE_CATEGORY("update_category", UpdateCategoryCommand.class),
        UPDATE_AUCTION_STATUS("update_auction_status", UpdateAuctionStatusCommand.class),
        FIND_LOTS_AND_PAGES_COUNT_BY_USER("find_lots_and_pages_count_by_user",
                FindLotsAndPagesCountByUserCommand.class),
        VALIDATE_AUCTION_START_DATE("validate_auction_start_date",
                ValidateDateInLotCreationCommand.class),
        FIND_USERS_AND_PAGES_COUNT_BY_ADMIN("find_users_and_pages_count_by_admin",
                FindUsersAndPagesCountByAdminCommand.class),
        CHANGE_USER_BANNED_STATUS_BY_ADMIN("change_user_banned_status_by_admin",
                ChangeUserBannedStatusByAdminCommand.class),
        SAVE_AUCTION_PARTICIPATION("save_auction_participation", SaveAuctionParticipationCommand.class),
        DELETE_AUCTION_PARTICIPATION("delete_auction_participation",
                DeleteAuctionParticipationCommand.class),
        FIND_USER_CASH_ACCOUNT("find_user_cash_account", FindUserCashAccountCommand.class),
        ADD_BID("add_bid", AddBidCommand.class),
        FIND_BEST_BID_AND_ITS_BELONGING_TO_USER("find_best_bid_and_its_belonging_to_user",
                FindBestBidAndItsBelongingToUserCommand.class),
        FIND_LOTS_AND_PAGES_COUNT_OWNED_BY_USER("find_lots_and_pages_count_owned_by_user",
                FindLotsAndPagesCountOwnedByUserCommand.class),
        FIND_LOTS_AND_PAGES_COUNT_OWNED_ENDED_WITH_RESULTS_BY_USER
                ("find_lots_and_pages_count_owned_ended_with_results_by_user",
                        FindLotsAndPagesCountOwnedEndedWithResultsByUserCommand.class),
        FIND_LOTS_AND_PAGES_COUNT_PARTICIPATED_BY_USER("find_lots_and_pages_count_participated_by_user",
                FindLotsAndPagesCountParticipatedByUserCommand.class),
        FIND_LOTS_AND_PAGES_COUNT_PARTICIPATED_RUNNING_BY_USER
                ("find_lots_and_pages_count_participated_running_by_user",
                        FindLotsAndPagesCountParticipatedRunningByUserCommand.class),
        FIND_LOTS_AND_PAGES_COUNT_PARTICIPATED_ENDED_WITH_RESULTS_BY_USER
                ("find_lots_and_pages_count_participated_ended_with_results_by_user",
                        FindLotsAndPagesCountParticipatedEndedWithResultsByUserCommand.class);

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
