package by.epam.finaltask.command.handler;

import jakarta.servlet.http.HttpServletRequest;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

public class HandlerFactory {

    private static volatile HandlerFactory factoryInstance;
    private final ConcurrentMap<Boolean, CommandHandler> commandHandlers = new ConcurrentHashMap<>();

    private HandlerFactory() {
    }

    public static HandlerFactory getInstance() {
        if (factoryInstance == null) {
            synchronized (HandlerFactory.class) {
                if (factoryInstance == null) {
                    factoryInstance = new HandlerFactory();
                }
            }
        }
        return factoryInstance;
    }

    public CommandHandler find(HttpServletRequest request) {
        boolean requestIsAjax = isRequestAjax(request);
        return commandHandlers.computeIfAbsent(requestIsAjax, this::createHandler);
    }

    public boolean isRequestAjax(HttpServletRequest request) {
        try {
            return Boolean.parseBoolean(request.getParameter("requestIsAjax"));
        } catch (Exception ex) {
            return false;
        }
    }

    private CommandHandler createHandler(Boolean requestIsAjax) {
        if (requestIsAjax) {
            return new AjaxCommandHandler();
        } else {
            return new SyncCommandHandler();
        }
    }
}
