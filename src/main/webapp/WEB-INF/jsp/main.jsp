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

    <%@include file="/WEB-INF/jsp_fragment/header.jsp"%>

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

<%@include file="/WEB-INF/jsp_fragment/footer.jsp"%>

</body>
</html>