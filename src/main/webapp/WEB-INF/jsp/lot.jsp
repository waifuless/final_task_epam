<jsp:useBean id="lot" scope="request" type="by.epam.finaltask.model.LotWithImages"/>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>

<fmt:setBundle basename="path-to-images-folder" var="imgFolderPath"/>

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
    <link rel="stylesheet" href="css/page-wrapper.css">
    <link rel="stylesheet" href="css/lot.css">
</head>
<body>
<div class="page-wrapper">

    <%@include file="/WEB-INF/jsp_fragment/header.jsp"%>

    <div class="container-lg">

        <div class="row">
            <h1>${lot.title}</h1>
        </div>

        <div id="row">
            <h5>Фотографии</h5>
            <div class="gallery">
                <div class="lot-image lot-main-image">
                    <img src="${lot.images.mainImage.path}" alt=""/>
                </div>
                <c:forEach var="image" items="${lot.images.otherImages}">
                    <div class="lot-image">
                        <img src="${image.path}" alt=""/>
                    </div>
                </c:forEach>
            </div>
        </div>

        <div class="row">
            <h5>Категория: ${lot.category}</h5>
        </div>

        <div class="row">
            <h5>Тип аукциона: ${lot.auctionType}</h5>
        </div>

        <div class="row">
            <h5>Состояние: ${lot.productCondition}</h5>
        </div>

        <div class="row">
            <h5>Начальная цена: ${lot.initialPrice}</h5>
        </div>

        <div class="row">
            <label for="description">Описание</label><br>
            <textarea style="resize: none" disabled id="description">${lot.description}</textarea>
        </div>

        <div class="row">
            <h5>Начало аукциона: ${lot.startDatetime}</h5>
        </div>

        <div class="row">
            <h5>Окончание аукциона: ${lot.endDatetime}</h5>
        </div>

        <div class="row">
            <h5>Регион: ${lot.region}</h5>
        </div>

        <div class="row">
            <h5>Город(район): ${lot.cityOrDistrict}</h5>
        </div>

        <div class="page-buffer"></div>
    </div>
</div>

<%@include file="/WEB-INF/jsp_fragment/footer.jsp"%>

</body>
</html>
