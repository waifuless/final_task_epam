package by.epam.finaltask.command;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

public class CommandFactory {

    private final static Logger LOG = LogManager.getLogger(CommandFactory.class);

    private static volatile CommandFactory instance;
    private final ConcurrentMap<CommandVariant, Command> commands = new ConcurrentHashMap<>();

    private CommandFactory() {
    }

    public static CommandFactory getInstance() {
        if (instance == null) {
            synchronized (CommandFactory.class) {
                if (instance == null) {
                    instance = new CommandFactory();
                }
            }
        }
        return instance;
    }

    public Optional<Command> findCommand(String commandName) {
        if (commandName == null) {
            return Optional.empty();
        }
        Optional<CommandVariant> optionalCommandClassByName = CommandVariant.findByName(commandName);
        if(optionalCommandClassByName.isPresent()){
            return findCommand(optionalCommandClassByName.get());
        }else{
            return Optional.empty();
        }
    }

    public Optional<Command> findCommand(CommandVariant commandVariant) {
        if (commandVariant == null) {
            return Optional.empty();
        }
        return Optional.ofNullable(commands.computeIfAbsent(commandVariant, this::createCommand));
    }

    public enum CommandVariant {
        SHOW_SIGN_IN("show_sign_in", ShowSingInPageCommand.class),
        REGISTER("register", RegisterCommand.class),
        SHOW_MAIN("show_main_page", ShowMainPageCommand.class),
        SIGN_IN("sign_in", SignInCommand.class),
        SIGN_OUT("sign_out", SignOutCommand.class),
        SHOW_REGISTRATION("show_registration", ShowRegistrationPageCommand.class),
        SHOW_LOT("show_lot_page", ShowLotPageCommand.class),
        SHOW_RESTORE_PASSWORD("show_restore_password", ShowRestorePasswordPageCommand.class);

        private final String commandName;
        private final Class<? extends Command> commandClass;

        CommandVariant(String commandName, Class<? extends Command> commandClass) {
            this.commandName = commandName;
            this.commandClass = commandClass;
        }

        public String getCommandName() {
            return commandName;
        }

        public static Optional<CommandVariant> findByName(String commandName) {
            for (CommandVariant value : values()) {
                if (value.commandName.equals(commandName)) {
                    return Optional.of(value);
                }
            }
            return Optional.empty();
        }
    }

    private Command createCommand(CommandVariant commandVariant) {
        try {
            return commandVariant.commandClass.newInstance();
        } catch (Exception e) {
            LOG.error(e.getMessage(), e);
            return null;
        }
    }
}
