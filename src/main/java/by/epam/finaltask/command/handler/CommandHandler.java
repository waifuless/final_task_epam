package by.epam.finaltask.command.handler;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

public interface CommandHandler {

    void handle(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException;
}
