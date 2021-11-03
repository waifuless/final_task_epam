<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <link rel="shortcut icon" href="images/dollar-symbol.png" type="image/x-icon">
    <title>Auction</title>
    <script src="js/jquery-3.6.0.js"></script>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.1.1/dist/css/bootstrap.min.css" rel="stylesheet"
          integrity="sha384-F3w7mX95PdgyTmZZMECAngseQB83DfGTowi0iMjiWaeVhAn4FJkqJByhZMI3AhiU" crossorigin="anonymous">
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.1.1/dist/js/bootstrap.bundle.min.js"
            integrity="sha384-/bQdsTh/da6pkI1MST/rWKFNjaCP5gBSY4sEBT38Q/9RBh9AH40zEOg7Hlq2THRZ"
            crossorigin="anonymous"></script>
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.5.0/font/bootstrap-icons.css">
    <link rel="stylesheet" href="css/main-page.css">
</head>
<body>
<div class="page-wrapper">
    <header style="background-color: #e3f2fd;">
        <nav class="container-lg navbar navbar-expand-lg navbar-light">
            <div class="container-fluid">
                <a class="navbar-brand" href="#">
                    <img src="images/dollar-symbol-large.png" alt="" width="30" height="24"
                         class="d-inline-block align-text-top">
                    Navbar
                </a>
                <button class="navbar-toggler" type="button" data-bs-toggle="collapse"
                        data-bs-target="#navbarSupportedContent" aria-controls="navbarSupportedContent"
                        aria-expanded="false" aria-label="Toggle navigation">
                    <span class="navbar-toggler-icon"></span>
                </button>
                <div class="collapse navbar-collapse" id="navbarSupportedContent">
                    <ul class="navbar-nav me-auto mb-2 mb-lg-0">
                        <li class="nav-item">
                            <a class="nav-link active" aria-current="page" href="#">Главная</a>
                        </li>
                        <li class="nav-item">
                            <a class="nav-link" href="#">Категории</a>
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
                                <li><a class="dropdown-item" href="#">Another action</a></li>
                                <li>
                                    <hr class="dropdown-divider">
                                </li>
                                <li><a class="dropdown-item" href="#">Выйти</a></li>
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


    <div class="container-lg">

        <h2 style="border-bottom: 1px solid green; margin-top: 30px">Фильтры</h2>
        <div class="row mt-4">
            <div class="col-lg-3 col-sm-6 mb-3">
                <label class="mb-2" for="auction-type-filter">Тип аукциона</label>
                <select id="auction-type-filter" class="form-select" aria-label="Default select example">
                    <option selected>Любой</option>
                    <option value="1">Прямой</option>
                    <option value="2">Обратный</option>
                </select>
            </div>
            <div class="col-lg-3 col-sm-6 mb-3">
                <label class="mb-2" for="category-filter">Категория</label>
                <select id="category-filter" class="form-select" aria-label="Default select example">
                    <option selected>Любая</option>
                    <option value="1">One</option>
                    <option value="2">Two</option>
                    <option value="3">Three</option>
                </select>
            </div>
            <div class="col-lg-3 col-sm-6 mb-3">
                <label class="mb-2" for="region-filter">Регион</label>
                <select id="region-filter" class="form-select" aria-label="Default select example">
                    <option selected>Любой</option>
                    <option value="1">One</option>
                    <option value="2">Two</option>
                    <option value="3">Three</option>
                </select>
            </div>
            <div class="col-lg-3 col-sm-6 mb-3">
                <label class="mb-2" for="city-filter">Город</label>
                <select id="city-filter" class="form-select" aria-label="Default select example">
                    <option selected>Любой</option>
                    <option value="1">One</option>
                    <option value="2">Two</option>
                    <option value="3">Three</option>
                </select>
            </div>
            <div class="col-lg-3 col-sm-6 mb-3">
                <label class="mb-2" for="condition-filter">Состояние</label>
                <select id="condition-filter" class="form-select" aria-label="Default select example">
                    <option selected>Любое</option>
                    <option value="1">One</option>
                    <option value="2">Two</option>
                    <option value="3">Three</option>
                </select>
            </div>
            <div class="col-lg-3 col-sm-6 mb-3">
                <label class="mb-2" for="price-filter">Цена</label>
                <div id="price-filter" class="input-group">
                    <input type="text" aria-label="from" class="form-control" placeholder="от" id="price-from">
                    <input type="text" aria-label="to" class="form-control" placeholder="до" id="price-to">
                </div>
            </div>
            <div class="col-lg-3 col-sm-6 mb-3" style="display: flex; align-items: flex-end">
                <button type="button" class="btn btn-success w-100">Применить</button>
            </div>
        </div>


        <h2 style="border-bottom: 1px solid green; margin-top: 30px">Все товары</h2>
        <div class="row mt-4">
            <a href="html/page2.html" class="container__row__a col-12 col-lg-6 mb-3 mb-3">
                <div class="card " style="max-width: 540px;">
                    <div class="row g-0">
                        <div class="col-md-4">
                            <img src="images/dollar-symbol-large.png" class="img-fluid rounded-start" alt="...">
                        </div>
                        <div class="col-md-8">
                            <div class="card-body">
                                <h5 class="card-title">Жопа новогодняя</h5>
                                <p class="card-text category">Одежда</p>
                                <p class="card-text auction-type">Тип аукциона: прямой</p>
                                <p class="card-text region">Регион: Минск</p>
                                <p class="card-text address">Адрес: ул.Зопы д.2 кв.2</p>
                                <p class="card-text initial-price">Начальная цена: 800000р</p>
                                <p class="card-text auction-start"><small class="text-muted">Начало аукциона:
                                    25.11.2021, 19:00</small></p>
                            </div>
                        </div>
                    </div>
                </div>
            </a>
            <a href="html/page2.html" class="container__row__a col-12 col-lg-6 mb-3 mb-3">
                <div class="card " style="max-width: 540px;">
                    <div class="row g-0">
                        <div class="col-md-4">
                            <img src="images/dollar-symbol-large.png" class="img-fluid rounded-start" alt="...">
                        </div>
                        <div class="col-md-8">
                            <div class="card-body">
                                <h5 class="card-title">Жопа новогодняя</h5>
                                <p class="card-text category">Одежда</p>
                                <p class="card-text auction-type">Тип аукциона: прямой</p>
                                <p class="card-text region">Регион: Минск</p>
                                <p class="card-text address">Адрес: ул.Зопы д.2 кв.2</p>
                                <p class="card-text initial-price">Начальная цена: 800000р</p>
                                <p class="card-text auction-start"><small class="text-muted">Начало аукциона:
                                    25.11.2021, 19:00</small></p>
                            </div>
                        </div>
                    </div>
                </div>
            </a>
            <a href="html/page2.html" class="container__row__a col-12 col-lg-6 mb-3 mb-3">
                <div class="card " style="max-width: 540px;">
                    <div class="row g-0">
                        <div class="col-md-4">
                            <img src="images/dollar-symbol-large.png" class="img-fluid rounded-start" alt="...">
                        </div>
                        <div class="col-md-8">
                            <div class="card-body">
                                <h5 class="card-title">Жопа новогодняя</h5>
                                <p class="card-text category">Одежда</p>
                                <p class="card-text auction-type">Тип аукциона: прямой</p>
                                <p class="card-text region">Регион: Минск</p>
                                <p class="card-text address">Адрес: ул.Зопы д.2 кв.2</p>
                                <p class="card-text initial-price">Начальная цена: 800000р</p>
                                <p class="card-text auction-start"><small class="text-muted">Начало аукциона:
                                    25.11.2021, 19:00</small></p>
                            </div>
                        </div>
                    </div>
                </div>
            </a>
            <a href="html/page2.html" class="container__row__a col-12 col-lg-6 mb-3 mb-3">
                <div class="card " style="max-width: 540px;">
                    <div class="row g-0">
                        <div class="col-md-4">
                            <img src="images/dollar-symbol-large.png" class="img-fluid rounded-start" alt="...">
                        </div>
                        <div class="col-md-8">
                            <div class="card-body">
                                <h5 class="card-title">Жопа новогодняя</h5>
                                <p class="card-text category">Одежда</p>
                                <p class="card-text auction-type">Тип аукциона: прямой</p>
                                <p class="card-text region">Регион: Минск</p>
                                <p class="card-text address">Адрес: ул.Зопы д.2 кв.2</p>
                                <p class="card-text initial-price">Начальная цена: 800000р</p>
                                <p class="card-text auction-start"><small class="text-muted">Начало аукциона:
                                    25.11.2021, 19:00</small></p>
                            </div>
                        </div>
                    </div>
                </div>
            </a>
        </div>
    </div>


    <div class="page-buffer"></div>
</div>
<div class="page-footer">
    <footer class="py-3 my-4" style="background-color: #e3f2fd;">
        <ul class="nav justify-content-center border-bottom pb-3 mb-3">
            <li class="nav-item"><a href="#" class="nav-link px-2 text-muted">Home</a></li>
            <li class="nav-item"><a href="#" class="nav-link px-2 text-muted">Features</a></li>
            <li class="nav-item"><a href="#" class="nav-link px-2 text-muted">Pricing</a></li>
            <li class="nav-item"><a href="#" class="nav-link px-2 text-muted">FAQs</a></li>
            <li class="nav-item"><a href="#" class="nav-link px-2 text-muted">About</a></li>
        </ul>
        <p class="text-center text-muted">&copy; All rights belong to Vova</p>
    </footer>
</div>
</body>
</html>