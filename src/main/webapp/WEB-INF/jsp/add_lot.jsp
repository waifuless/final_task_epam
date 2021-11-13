<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>

<fmt:setBundle basename="path-to-images-folder" var="imgFolderPath"/>

<fmt:message bundle="${imgFolderPath}" key="folder.context.path" var="imagesFolderContextPath"/>

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
    <link rel="stylesheet" href="css/add-lot.css">
    <script src="js/add-lot.js"></script>
</head>
<body>
<div class="page-wrapper">

    <%@include file="/WEB-INF/jsp_fragment/header.jsp"%>

    <div class="container">

        <div class="row col-lg-8 col-sm-9 col-11 mb-3 mx-auto mx-lg-0" id="div-input-and-images">
            <p class="big-text">Фотографии</p>
            <div class="mb-3 input-image">
                <div>
                    <form id="imageInputForm" enctype="multipart/form-data">
                        <label for="gallery-photo-add-input" id="gallery-photo-add-label" class="m-3 btn btn-primary">Добавить
                            фотографии</label>
                        <input type="file" id="gallery-photo-add-input"
                               accept="image/png, image/gif, image/jpeg, image/jpg" name="imageInput">
                    </form>
                    <p class="px-3 mb-2">Или перетащите сюда <small>(Маскимум 3 фотографии, по 5 мб)</small></p>
                </div>
            </div>
            <div class="gallery" id="gallery">
                <!--            <div class="div__added-image">-->
                <!--                <img src="images/dollar-symbol-large.png" alt=""/>-->
                <!--            </div>-->
            </div>
        </div>

        <div class="row col-lg-5 col-sm-8 col-10 mb-3 mx-auto mx-lg-0">
            <label for="title">Название товара</label><br>
            <input type="text" required name="title" id="title" class="form-control">
        </div>

        <div class="row col-lg-5 col-sm-8 col-10 mb-3 mx-auto mx-lg-0">
            <label class="mb-2" for="category-filter">Категория</label>
            <select id="category-filter" class="form-select" aria-label="Default select example">
                <option selected>Не выбрана</option>
                <option value="1">One</option>
                <option value="2">Two</option>
                <option value="3">Three</option>
            </select>
        </div>

        <div class="row col-lg-5 col-sm-8 col-10 mb-3 mx-auto mx-lg-0">
            <label class="mb-2" for="auction-type-filter">Тип аукциона</label>
            <select id="auction-type-filter" class="form-select" aria-label="Default select example">
                <option selected>Не выбран</option>
                <option value="1">Прямой</option>
                <option value="2">Обратный</option>
            </select>
        </div>

        <div class="row col-lg-5 col-sm-8 col-10 mb-3 mx-auto mx-lg-0">
            <label class="mb-2" for="condition-filter">Состояние</label>
            <select id="condition-filter" class="form-select" aria-label="Default select example">
                <option selected>Не выбрано</option>
                <option value="1">One</option>
                <option value="2">Two</option>
                <option value="3">Three</option>
            </select>
        </div>

        <div class="row col-lg-5 col-sm-8 col-10 mb-3 mx-auto mx-lg-0">
            <label for="description">Описание</label><br>
            <textarea style="resize: none" required name="description" id="description" class="form-control"></textarea>
        </div>

        <div class="row col-lg-5 col-sm-8 col-10 mb-3 mx-auto mx-lg-0">
            <label class="big-text" for="init-price">Начальная цена</label><br>
            <input type="text" required name="init-price" id="init-price" class="form-control">
        </div>

        <div class="row col-lg-5 col-sm-8 col-10 mb-3 mx-auto mx-lg-0">
            <label for="auction-start">Начало аукциона</label><br>
            <input type="datetime-local" required name="auction-start" id="auction-start" class="form-control"/>
        </div>

        <div class="row col-lg-5 col-sm-8 col-10 mb-3 mx-auto mx-lg-0">
            <label for="duration">Длительность аукциона(в часах)</label><br>
            <input type="number" required name="duration" id="duration" class="form-control"/>
        </div>

        <div class="row col-lg-5 col-sm-8 col-10 mb-3 mx-auto mx-lg-0">
            <p class="big-text">Метоположение</p>
            <label class="mb-2" for="region-filter">Регион</label>
            <select id="region-filter" class="form-select" aria-label="Default select example">
                <option selected>Не выбран</option>
                <option value="1">One</option>
                <option value="2">Two</option>
                <option value="3">Three</option>
            </select>
        </div>
        <div class="row col-lg-5 col-sm-8 col-10 mb-3 mx-auto mx-lg-0">
            <label class="mb-2" for="city-filter">Город</label>
            <select id="city-filter" class="form-select" aria-label="Default select example">
                <option selected>Не выбран</option>
                <option value="1">One</option>
                <option value="2">Two</option>
                <option value="3">Three</option>
            </select>
        </div>

        <div class="row">
            <a class="btn btn-success mx-auto col-lg-5 col-sm-8 col-10 my-5">Добавить товар</a>
        </div>

        <div class="page-buffer"></div>
    </div>
</div>

<%@include file="/WEB-INF/jsp_fragment/footer.jsp"%>

</body>
</html>
