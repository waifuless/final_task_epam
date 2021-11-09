<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>

<header style="background-color: #e3f2fd;">
  <nav class="container-lg navbar navbar-expand-lg navbar-light">
    <div class="container-fluid">
      <a class="navbar-brand" href="${pageContext.request.contextPath}/ControllerServlet?command=show_main_page">
        <img src="images/dollar-symbol-large.png" alt="" width="30" height="24"
             class="d-inline-block align-text-top">
        Auction
      </a>
      <button class="navbar-toggler" type="button" data-bs-toggle="collapse"
              data-bs-target="#navbarSupportedContent" aria-controls="navbarSupportedContent"
              aria-expanded="false" aria-label="Toggle navigation">
        <span class="navbar-toggler-icon"></span>
      </button>
      <div class="collapse navbar-collapse" id="navbarSupportedContent">
        <ul class="navbar-nav me-auto mb-2 mb-lg-0">
          <li class="nav-item">
            <a class="nav-link active" aria-current="page" href="${pageContext.request.contextPath}/ControllerServlet?command=show_main_page">Главная</a>
          </li>
          <li class="nav-item">
            <a class="nav-link" href="#">Личный кабинет</a>
          </li>
          <li class="nav-item">
            <a class="nav-link" href="#">Корзина</a>
          </li>
          <li class="nav-item dropdown">
            <a class="nav-link dropdown-toggle" href="#" id="navbarDropdown" role="button"
               data-bs-toggle="dropdown" aria-expanded="false">
              Другое
            </a>
            <ul class="dropdown-menu" aria-labelledby="navbarDropdown">
              <li><a class="dropdown-item" href="#">Помощь</a></li>
              <li><a class="dropdown-item" href="#">Информация</a></li>
            </ul>
          </li>
        </ul>
        <form class="d-flex">
          <input class="form-control me-2" type="search" placeholder="Search" aria-label="Search">
          <button class="btn btn-outline-success" type="submit">Search</button>
        </form>
        <c:choose>
          <c:when test="${sessionScope.containsKey('USER_EMAIL')}">
            <div class="dropdown my-3 my-lg-0 mx-lg-3">
              <div class="dropdown-toggle" id="accountDropDown" role="button"
                   data-bs-toggle="dropdown" aria-expanded="false">
                <svg style="color: darkslategrey;" xmlns="http://www.w3.org/2000/svg" width="30" height="30" fill="currentColor" class="bi bi-person-circle" viewBox="0 0 16 16">
                  <path d="M11 6a3 3 0 1 1-6 0 3 3 0 0 1 6 0z"/>
                  <path fill-rule="evenodd" d="M0 8a8 8 0 1 1 16 0A8 8 0 0 1 0 8zm8-7a7 7 0 0 0-5.468 11.37C3.242 11.226 4.805 10 8 10s4.757 1.225 5.468 2.37A7 7 0 0 0 8 1z"/>
                </svg>
              </div>
              <ul class="dropdown-menu dropdown-menu-lg-end" id="accountDropDownMenu" aria-labelledby="accountDropDown">
                <li><a style="font-size: 0.75em" class="dropdown-item disabled" href="#">${sessionScope.get('USER_EMAIL')}</a></li>
                <li><a class="dropdown-item" href="#">Личный кабинет</a></li>
                <li><hr class="dropdown-divider"></li>
                <li><a class="dropdown-item" href="${pageContext.request.contextPath}/ControllerServlet?command=sign_out" style="color: darkred;">Выйти</a></li>
              </ul>
            </div>
          </c:when>
          <c:otherwise>
            <div class="my-3 my-lg-0 mx-lg-3">
              <a class="btn btn-primary" role="button" href="${pageContext.request.contextPath}/ControllerServlet?command=show_sign_in">Войти</a>
            </div>
          </c:otherwise>
        </c:choose>
      </div>
    </div>
  </nav>
</header>