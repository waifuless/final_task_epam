package by.epam.finaltask.command.sync_command;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

public class SyncCommandFactory {

    private final static Logger LOG = LogManager.getLogger(SyncCommandFactory.class);

    private static volatile SyncCommandFactory instance;
    private final ConcurrentMap<SyncCommandVariant, SyncCommand> commands = new ConcurrentHashMap<>();

    private SyncCommandFactory() {
    }

    public static SyncCommandFactory getInstance() {
        if (instance == null) {
            synchronized (SyncCommandFactory.class) {
                if (instance == null) {
                    instance = new SyncCommandFactory();
                }
            }
        }
        return instance;
    }

    public Optional<SyncCommand> findCommand(String commandName) {
        if (commandName == null) {
            return Optional.empty();
        }
        Optional<SyncCommandVariant> optionalCommandClassByName = SyncCommandVariant.findByName(commandName);
        if (optionalCommandClassByName.isPresent()) {
            return findCommand(optionalCommandClassByName.get());
        } else {
            return Optional.empty();
        }
    }

    public Optional<SyncCommand> findCommand(SyncCommandVariant syncCommandVariant) {
        if (syncCommandVariant == null) {
            return Optional.empty();
        }
        return Optional.ofNullable(commands.computeIfAbsent(syncCommandVariant, this::createCommand));
    }

    private SyncCommand createCommand(SyncCommandVariant syncCommandVariant) {
        try {
            return syncCommandVariant.commandClass.newInstance();
        } catch (Exception e) {
            LOG.error(e.getMessage(), e);
            return null;
        }
    }

    public enum SyncCommandVariant {
        SHOW_SIGN_IN("show_sign_in", ShowSingInPageCommand.class),
        REGISTER("register", RegisterCommand.class),
        SHOW_MAIN("show_main_page", ShowMainPageCommand.class),
        SIGN_IN("sign_in", SignInCommand.class),
        SIGN_OUT("sign_out", SignOutCommand.class),
        SHOW_REGISTRATION("show_registration", ShowRegistrationPageCommand.class),
        SHOW_LOT("show_lot_page", ShowLotPageCommand.class),
        SHOW_RESTORE_PASSWORD("show_restore_password", ShowRestorePasswordPageCommand.class),
        SHOW_LOT_CREATION("show_lot_creation", ShowLotCreationCommand.class),
        CREATE_LOT("create_lot", CreateLotCommand.class),
        SHOW_ADMIN_LOTS("show_admin_tools", ShowAdminLotsCommand.class),
        SHOW_ADMIN_CATEGORIES("show_admin_categories", ShowAdminCategoriesCommand.class),
        SHOW_ADMIN_NEW_LOTS("show_admin_new_lots", ShowAdminNewLotsCommand.class);

        private final String commandName;
        private final Class<? extends SyncCommand> commandClass;

        SyncCommandVariant(String commandName, Class<? extends SyncCommand> commandClass) {
            this.commandName = commandName;
            this.commandClass = commandClass;
        }

        public static Optional<SyncCommandVariant> findByName(String commandName) {
            for (SyncCommandVariant value : values()) {
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
