package by.epam.finaltask.command;

import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

public class CommandFactory {

    private static volatile CommandFactory instance;
    private final ConcurrentMap<String, Command> commands = new ConcurrentHashMap<>();

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
        Optional<Command> optionalCommand = Optional.ofNullable(commands.get(commandName));
        if (!optionalCommand.isPresent()) {
            optionalCommand = Optional.ofNullable(createCommand(commandName));
            if (optionalCommand.isPresent()) {
                Optional<Command> oldCommandValue = Optional
                        .ofNullable(commands.putIfAbsent(commandName, optionalCommand.get()));
                if (oldCommandValue.isPresent()) {//check in case that other thread put command earlier
                    optionalCommand = oldCommandValue;
                }
            }
        }
        return optionalCommand;
    }

    private Command createCommand(String commandName) {
        switch (commandName) {
            case "show_sign_in":
                return new ShowSingInPageCommand();
            case "register":
                return new RegisterCommand();
            case "show_main_page":
                return new ShowMainPageCommand();
            case "sign_in":
                return new SignInCommand();
            case "sign_out":
                return new SignOutCommand();
            case "show_registration":
                return new ShowRegistrationPageCommand();
            case "show_restore_password":
                return new ShowRestorePasswordPageCommand();
            default:
                return null;
        }
    }
}
