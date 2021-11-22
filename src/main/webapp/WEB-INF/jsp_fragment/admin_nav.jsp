<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>

<link rel="stylesheet" href="css/account-drop-down-menu.css">
<link rel="stylesheet" href="css/sidebar.css">
<script src="js/language-switch.js" type="text/javascript"></script>

<fmt:setBundle basename="l10n.page.admin_tools_header" var="adminHeaderLoc"/>

<nav class="sidebar col-3 col-lg-2 bg-light">
    <div class="d-flex flex-column flex-shrink-0 p-3 bg-light">
        <a href="${pageContext.request.contextPath}/ControllerServlet?command=show_main_page"
           class="d-flex align-items-center mb-3 mb-md-0 me-md-auto link-dark text-decoration-none">
            <img src="images/dollar-symbol-large.png" alt="" width="40" height="36"
                 class="d-inline-block align-text-top">
            <fmt:message bundle="${adminHeaderLoc}" key="title"/>
        </a>
        <hr>
        <ul class="nav nav-pills flex-column mb-auto">
            <li class="nav-item">
                <a href="#" class="nav-link link-dark" aria-current="page">
                    <svg xmlns="http://www.w3.org/2000/svg" width="16" height="16" fill="currentColor" class="bi me-2 bi-plus-square" viewBox="0 0 16 16">
                        <path d="M14 1a1 1 0 0 1 1 1v12a1 1 0 0 1-1 1H2a1 1 0 0 1-1-1V2a1 1 0 0 1 1-1h12zM2 0a2 2 0 0 0-2 2v12a2 2 0 0 0 2 2h12a2 2 0 0 0 2-2V2a2 2 0 0 0-2-2H2z"/>
                        <path d="M8 4a.5.5 0 0 1 .5.5v3h3a.5.5 0 0 1 0 1h-3v3a.5.5 0 0 1-1 0v-3h-3a.5.5 0 0 1 0-1h3v-3A.5.5 0 0 1 8 4z"/>
                    </svg>
                    <fmt:message bundle="${adminHeaderLoc}" key="new_lots"/>
                </a>
            </li>
            <li class="nav-item">
                <a href="#" class="nav-link link-dark active" aria-current="page">
                    <svg xmlns="http://www.w3.org/2000/svg" width="16" height="16" fill="currentColor"
                         class="bi me-2 bi-grid" viewBox="0 0 16 16">
                        <path d="M1 2.5A1.5 1.5 0 0 1 2.5 1h3A1.5 1.5 0 0 1 7 2.5v3A1.5 1.5 0 0 1 5.5 7h-3A1.5 1.5 0 0 1 1 5.5v-3zM2.5 2a.5.5 0 0 0-.5.5v3a.5.5 0 0 0 .5.5h3a.5.5 0 0 0 .5-.5v-3a.5.5 0 0 0-.5-.5h-3zm6.5.5A1.5 1.5 0 0 1 10.5 1h3A1.5 1.5 0 0 1 15 2.5v3A1.5 1.5 0 0 1 13.5 7h-3A1.5 1.5 0 0 1 9 5.5v-3zm1.5-.5a.5.5 0 0 0-.5.5v3a.5.5 0 0 0 .5.5h3a.5.5 0 0 0 .5-.5v-3a.5.5 0 0 0-.5-.5h-3zM1 10.5A1.5 1.5 0 0 1 2.5 9h3A1.5 1.5 0 0 1 7 10.5v3A1.5 1.5 0 0 1 5.5 15h-3A1.5 1.5 0 0 1 1 13.5v-3zm1.5-.5a.5.5 0 0 0-.5.5v3a.5.5 0 0 0 .5.5h3a.5.5 0 0 0 .5-.5v-3a.5.5 0 0 0-.5-.5h-3zm6.5.5A1.5 1.5 0 0 1 10.5 9h3a1.5 1.5 0 0 1 1.5 1.5v3a1.5 1.5 0 0 1-1.5 1.5h-3A1.5 1.5 0 0 1 9 13.5v-3zm1.5-.5a.5.5 0 0 0-.5.5v3a.5.5 0 0 0 .5.5h3a.5.5 0 0 0 .5-.5v-3a.5.5 0 0 0-.5-.5h-3z"/>
                    </svg>
                    <fmt:message bundle="${adminHeaderLoc}" key="all_lots"/>
                </a>
            </li>
            <li class="nav-item mx-3 my-2">
                <a href="#" class="btn btn-toggle align-items-center rounded collapsed"
                   data-bs-toggle="collapse"
                   data-bs-target="#params" aria-expanded="true">
                    <fmt:message bundle="${adminHeaderLoc}" key="parameters"/>
                </a>
                <div class="collapse show" id="params">
                    <ul class="btn-toggle-nav list-unstyled fw-normal pb-1 small">
                        <li><a href="#" class="rounded nav-link link-dark"><fmt:message bundle="${adminHeaderLoc}" key="parameters.regions"/></a></li>
                        <li><a href="#" class="rounded nav-link link-dark"><fmt:message bundle="${adminHeaderLoc}" key="parameters.cities"/></a></li>
                        <li><a href="#" class="rounded nav-link link-dark"><fmt:message bundle="${adminHeaderLoc}" key="parameters.categories"/></a></li>
                    </ul>
                </div>
            </li>
            <li class="nav-item">
                <a href="#" class="nav-link link-dark">
                    <svg xmlns="http://www.w3.org/2000/svg" width="16" height="16" fill="currentColor"
                         class="bi bi-person-circle me-2" viewBox="0 0 16 16">
                        <path d="M11 6a3 3 0 1 1-6 0 3 3 0 0 1 6 0z"/>
                        <path fill-rule="evenodd"
                              d="M0 8a8 8 0 1 1 16 0A8 8 0 0 1 0 8zm8-7a7 7 0 0 0-5.468 11.37C3.242 11.226 4.805 10 8 10s4.757 1.225 5.468 2.37A7 7 0 0 0 8 1z"/>
                    </svg>
                    <fmt:message bundle="${adminHeaderLoc}" key="users"/>
                </a>
            </li>
            <li class="nav-item">
                <a href="#" class="nav-link link-dark">
                    <svg xmlns="http://www.w3.org/2000/svg" width="16" height="16" fill="currentColor"
                         class="bi me-2 bi-lightning-charge" viewBox="0 0 16 16">
                        <path d="M11.251.068a.5.5 0 0 1 .227.58L9.677 6.5H13a.5.5 0 0 1 .364.843l-8 8.5a.5.5 0 0 1-.842-.49L6.323 9.5H3a.5.5 0 0 1-.364-.843l8-8.5a.5.5 0 0 1 .615-.09zM4.157 8.5H7a.5.5 0 0 1 .478.647L6.11 13.59l5.732-6.09H9a.5.5 0 0 1-.478-.647L9.89 2.41 4.157 8.5z"/>
                    </svg>
                    <fmt:message bundle="${adminHeaderLoc}" key="manage_admins"/>
                </a>
            </li>
        </ul>
        <div class="dropdown my-3 my-lg-0 mx-lg-3">
            <div class="dropdown-toggle" id="langDropdown" role="button"
                 data-bs-toggle="dropdown" aria-expanded="false">
                <svg xmlns="http://www.w3.org/2000/svg" width="30" height="30" fill="currentColor"
                     class="bi bi-translate" viewBox="0 0 16 16">
                    <path d="M4.545 6.714 4.11 8H3l1.862-5h1.284L8 8H6.833l-.435-1.286H4.545zm1.634-.736L5.5 3.956h-.049l-.679 2.022H6.18z"/>
                    <path d="M0 2a2 2 0 0 1 2-2h7a2 2 0 0 1 2 2v3h3a2 2 0 0 1 2 2v7a2 2 0 0 1-2 2H7a2 2 0 0 1-2-2v-3H2a2 2 0 0 1-2-2V2zm2-1a1 1 0 0 0-1 1v7a1 1 0 0 0 1 1h7a1 1 0 0 0 1-1V2a1 1 0 0 0-1-1H2zm7.138 9.995c.193.301.402.583.63.846-.748.575-1.673 1.001-2.768 1.292.178.217.451.635.555.867 1.125-.359 2.08-.844 2.886-1.494.777.665 1.739 1.165 2.93 1.472.133-.254.414-.673.629-.89-1.125-.253-2.057-.694-2.82-1.284.681-.747 1.222-1.651 1.621-2.757H14V8h-3v1.047h.765c-.318.844-.74 1.546-1.272 2.13a6.066 6.066 0 0 1-.415-.492 1.988 1.988 0 0 1-.94.31z"/>
                </svg>
                <fmt:message bundle="${adminHeaderLoc}" key="lang.dropdown.title"/>
            </div>
            <ul class="dropdown-menu dropdown-menu-lg-end" aria-labelledby="langDropdown">
                <li><a class="dropdown-item" onclick="setLocaleAndReloadPage('ru_RU')" href="#">
                    <fmt:message bundle="${adminHeaderLoc}" key="lang.dropdown.ru"/>
                </a></li>
                <li><a class="dropdown-item" onclick="setLocaleAndReloadPage('en_US')" href="#">
                    <fmt:message bundle="${adminHeaderLoc}" key="lang.dropdown.en"/>
                </a></li>
            </ul>
        </div>
        <hr>
        <div class="dropdown my-3 my-lg-0 mx-lg-3">
            <div class="dropdown-toggle" id="accountDropDown" role="button"
                 data-bs-toggle="dropdown" aria-expanded="false">
                <svg style="color: darkslategrey;" xmlns="http://www.w3.org/2000/svg" width="30"
                     height="30" fill="currentColor" class="bi bi-person-circle" viewBox="0 0 16 16">
                    <path d="M11 6a3 3 0 1 1-6 0 3 3 0 0 1 6 0z"/>
                    <path fill-rule="evenodd"
                          d="M0 8a8 8 0 1 1 16 0A8 8 0 0 1 0 8zm8-7a7 7 0 0 0-5.468 11.37C3.242 11.226 4.805 10 8 10s4.757 1.225 5.468 2.37A7 7 0 0 0 8 1z"/>
                </svg>
            </div>
            <ul class="dropdown-menu dropdown-menu-lg-end accountDropDownMenu" id="accountDropDownMenu"
                aria-labelledby="accountDropDown">
                <li><a style="font-size: 0.75em" class="dropdown-item disabled" href="#">
                    ${sessionScope.get('USER_EMAIL')}
                </a></li>
                <li><a class="dropdown-item" href="#">
                    <fmt:message bundle="${adminHeaderLoc}" key="account.personal_link"/>
                </a></li>
                <li><a class="dropdown-item"
                       href="${pageContext.request.contextPath}/ControllerServlet?command=show_main_page">
                    <fmt:message bundle="${adminHeaderLoc}" key="account.back_to_users_pages"/>
                </a></li>
                <li>
                    <hr class="dropdown-divider">
                </li>
                <li><a class="dropdown-item"
                       href="${pageContext.request.contextPath}/ControllerServlet?command=sign_out"
                       style="color: darkred;">
                    <fmt:message bundle="${adminHeaderLoc}" key="account.sign_out"/>
                </a></li>
            </ul>
        </div>
    </div>
</nav>
