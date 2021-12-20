<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" import="by.epam.finaltask.model.Role"%>

<script src="js/language-switch.js" type="text/javascript"></script>
<script src="js/print-cash-account-in-header.js" type="text/javascript"></script>
<link rel="stylesheet" href="css/account-drop-down-menu.css">

<fmt:setBundle basename="l10n.page.header" var="headerLoc"/>

<header style="background-color: #e3f2fd;">
    <nav class="container-lg navbar navbar-expand-lg navbar-light">
        <div class="container-fluid">
            <a class="navbar-brand" href="${pageContext.request.contextPath}/ControllerServlet?command=show_main_page">
                <img src="images/dollar-symbol-large.png" alt="" width="30" height="24"
                     class="d-inline-block align-text-top">
                <fmt:message bundle="${headerLoc}" key="title"/>
            </a>
            <button class="navbar-toggler" type="button" data-bs-toggle="collapse"
                    data-bs-target="#navbarSupportedContent" aria-controls="navbarSupportedContent"
                    aria-expanded="false" aria-label="Toggle navigation">
                <span class="navbar-toggler-icon"></span>
            </button>
            <div class="collapse navbar-collapse" id="navbarSupportedContent">
                <ul class="navbar-nav me-auto mb-2 mb-lg-0">
                    <li class="nav-item">
                        <a class="nav-link" id="main-link" aria-current="page"
                           href="${pageContext.request.contextPath}/ControllerServlet?command=show_main_page">
                            <fmt:message bundle="${headerLoc}" key="link.main"/>
                        </a>
                    </li>
                    <li class="nav-item">
                        <a class="nav-link" id="lot-creation-link"
                           href="${pageContext.request.contextPath}/ControllerServlet?command=show_lot_creation">
                            <fmt:message bundle="${headerLoc}" key="link.add_lot"/>
                        </a>
                    </li>
                    <li class="nav-item">
                        <a class="nav-link" id="users-lots-link"
                           href="${pageContext.request.contextPath}/ControllerServlet?command=show_user_lots">
                            <fmt:message bundle="${headerLoc}" key="link.cart"/>
                        </a>
                    </li>
                    <li class="nav-item dropdown">
                        <a class="nav-link dropdown-toggle" href="#" id="navbarDropdown" role="button"
                           data-bs-toggle="dropdown" aria-expanded="false">
                            <fmt:message bundle="${headerLoc}" key="link.other"/>
                        </a>
                        <ul class="dropdown-menu" aria-labelledby="navbarDropdown">
                            <li><a class="dropdown-item" href="#">
                                <fmt:message bundle="${headerLoc}" key="link.other.help"/>
                            </a></li>
                            <li><a class="dropdown-item" href="#">
                                <fmt:message bundle="${headerLoc}" key="link.other.info"/>
                            </a></li>
                        </ul>
                    </li>
                </ul>

                <div class="dropdown my-3 my-lg-0 mx-lg-3">
                    <div class="dropdown-toggle" id="langDropdown" role="button"
                         data-bs-toggle="dropdown" aria-expanded="false">
                        <svg xmlns="http://www.w3.org/2000/svg" width="30" height="30" fill="currentColor"
                             class="bi bi-translate" viewBox="0 0 16 16">
                            <path d="M4.545 6.714 4.11 8H3l1.862-5h1.284L8 8H6.833l-.435-1.286H4.545zm1.634-.736L5.5 3.956h-.049l-.679 2.022H6.18z"></path>
                            <path d="M0 2a2 2 0 0 1 2-2h7a2 2 0 0 1 2 2v3h3a2 2 0 0 1 2 2v7a2 2 0 0 1-2 2H7a2 2 0 0 1-2-2v-3H2a2 2 0 0 1-2-2V2zm2-1a1 1 0 0 0-1 1v7a1 1 0 0 0 1 1h7a1 1 0 0 0 1-1V2a1 1 0 0 0-1-1H2zm7.138 9.995c.193.301.402.583.63.846-.748.575-1.673 1.001-2.768 1.292.178.217.451.635.555.867 1.125-.359 2.08-.844 2.886-1.494.777.665 1.739 1.165 2.93 1.472.133-.254.414-.673.629-.89-1.125-.253-2.057-.694-2.82-1.284.681-.747 1.222-1.651 1.621-2.757H14V8h-3v1.047h.765c-.318.844-.74 1.546-1.272 2.13a6.066 6.066 0 0 1-.415-.492 1.988 1.988 0 0 1-.94.31z"></path>
                        </svg>
                        <fmt:message bundle="${headerLoc}" key="lang.dropdown.title"/>
                    </div>
                    <ul class="dropdown-menu dropdown-menu-lg-end" aria-labelledby="langDropdown">
                        <li><a class="dropdown-item" onclick="setLocaleAndReloadPage('ru_RU')" href="#">
                            <fmt:message bundle="${headerLoc}" key="lang.dropdown.ru"/>
                        </a></li>
                        <li><a class="dropdown-item" onclick="setLocaleAndReloadPage('en_US')" href="#">
                            <fmt:message bundle="${headerLoc}" key="lang.dropdown.en"/>
                        </a></li>
                    </ul>
                </div>

                <form class="d-flex" method="get" action="${pageContext.request.contextPath}/ControllerServlet">
                    <input id="title" name="title" class="form-control me-2" type="search" placeholder="Search" aria-label="Search">
                    <input type="hidden" name="command" value="show_main_page">
                    <button class="btn btn-outline-success" type="submit">
                        <fmt:message bundle="${headerLoc}" key="search.button"/>
                    </button>
                </form>

                <c:choose>
                    <c:when test="${sessionScope.containsKey('USER_EMAIL')}">
                        <div class="dropdown my-3 my-lg-0 mx-lg-3">
                            <div class="dropdown-toggle" id="accountDropDown" role="button"
                                 data-bs-toggle="dropdown" aria-expanded="false">
                                <svg style="color: darkslategrey;" xmlns="http://www.w3.org/2000/svg" width="30"
                                     height="30" fill="currentColor" class="bi bi-person-circle" viewBox="0 0 16 16">
                                    <path d="M11 6a3 3 0 1 1-6 0 3 3 0 0 1 6 0z"></path>
                                    <path fill-rule="evenodd"
                                          d="M0 8a8 8 0 1 1 16 0A8 8 0 0 1 0 8zm8-7a7 7 0 0 0-5.468 11.37C3.242 11.226 4.805 10 8 10s4.757 1.225 5.468 2.37A7 7 0 0 0 8 1z"></path>
                                </svg>
                            </div>
                            <ul class="dropdown-menu dropdown-menu-lg-end accountDropDownMenu" id="accountDropDownMenu"
                                aria-labelledby="accountDropDown">
                                <li><a style="font-size: 0.75em" class="dropdown-item disabled" href="#">
                                        <c:out value="${sessionScope.get('USER_EMAIL')}"/>
                                </a></li>
                                <li><button class="dropdown-item" data-bs-toggle="modal" data-bs-target="#cashAccountModal"
                                            onclick="printCashAccountInHeader()">
                                        Мой счет
                                </button></li>
                                <li><a class="dropdown-item" href="#">
                                    <fmt:message bundle="${headerLoc}" key="account.personal_link"/>
                                </a></li>
                                <c:if test="${sessionScope.get('USER_ROLE') eq Role.ADMIN}">
                                    <li><a class="dropdown-item" href="${pageContext.request.contextPath}/ControllerServlet?command=show_admin_tools">
                                        <fmt:message bundle="${headerLoc}" key="account.admin_link"/>
                                    </a></li>
                                </c:if>
                                <li>
                                    <hr class="dropdown-divider">
                                </li>
                                <li><a class="dropdown-item"
                                       href="${pageContext.request.contextPath}/ControllerServlet?command=sign_out"
                                       style="color: darkred;">
                                    <fmt:message bundle="${headerLoc}" key="account.sign_out"/>
                                </a></li>
                            </ul>
                        </div>
                    </c:when>
                    <c:otherwise>
                        <div class="my-3 my-lg-0 mx-lg-3">
                            <a class="btn btn-primary" role="button"
                               href="${pageContext.request.contextPath}/ControllerServlet?command=show_sign_in"><fmt:message
                                    bundle="${headerLoc}" key="account.sign_in"/></a>
                        </div>
                    </c:otherwise>
                </c:choose>
            </div>


            <div class="modal fade" id="cashAccountModal" tabindex="-1" aria-labelledby="cashAccountModalLabel" aria-hidden="true">
                <div class="modal-dialog">
                    <div class="modal-content">
                        <div class="modal-header">
                            <h5 class="modal-title" id="cashAccountModalLabel">Сейчас на счету, р.</h5>
                            <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
                        </div>
                        <div class="modal-body" id="cashAccountModalBody">
                        </div>
                        <div class="modal-footer">
                            <button type="button" class="btn btn-primary" data-bs-dismiss="modal">Ok</button>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </nav>
</header>