package by.epam.finaltask.controller;

public enum PagePath {
    MAIN("WEB-INF/jsp/main.jsp"),
    SIGN_IN("WEB-INF/jsp/sign_in.jsp"),
    REGISTRATION("WEB-INF/jsp/registration.jsp"),
    LOT("WEB-INF/jsp/lot.jsp"),
    RESTORE_PASSWORD("WEB-INF/jsp/restore_password.jsp"),
    ERROR("WEB-INF/jsp/exception.jsp"),
    START_PAGE("index.jsp"),
    LOT_CREATION("WEB-INF/jsp/create_lot.jsp");

    private final String path;

    PagePath(String path) {
        this.path = path;
    }

    public String getPath() {
        return path;
    }
}
