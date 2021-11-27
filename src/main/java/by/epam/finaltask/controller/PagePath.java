package by.epam.finaltask.controller;

public enum PagePath {
    MAIN("WEB-INF/jsp/main.jsp"),
    SIGN_IN("WEB-INF/jsp/sign_in.jsp"),
    REGISTRATION("WEB-INF/jsp/registration.jsp"),
    LOT("WEB-INF/jsp/lot.jsp"),
    RESTORE_PASSWORD("WEB-INF/jsp/restore_password.jsp"),
    ERROR("WEB-INF/jsp/exception.jsp"),
    START_PAGE("index.jsp"),
    ADMIN_ALL_LOTS("WEB-INF/jsp/admin_all_lots.jsp"),
    LOT_CREATION("WEB-INF/jsp/create_lot.jsp"),
    ADMIN_CATEGORIES("WEB-INF/jsp/admin_categories.jsp"),
    ADMIN_NEW_LOTS("WEB-INF/jsp/admin_new_lots.jsp");

    private final String path;

    PagePath(String path) {
        this.path = path;
    }

    public String getPath() {
        return path;
    }
}
