<jsp:useBean id="lot" scope="request" type="by.epam.finaltask.model.LotWithImages"/>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"
         import="by.epam.finaltask.model.AuctionStatus, by.epam.finaltask.command.UserSessionAttribute" %>

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
    <link rel="stylesheet" href="css/lot-page.css">
</head>
<body>
<div class="page-wrapper">

    <%@include file="/WEB-INF/jsp_fragment/header.jsp" %>

    <div class="container-lg">

        <div class="row my-3">
            <h1>${lot.title}</h1>
        </div>

        <div class="row">
            <div id="carouselExampleControls" class="carousel carousel-dark slide" data-bs-ride="carousel">
                <div class="carousel-inner wrapper-div-image">
                    <div class="carousel-item div-image active">
                        <img src="${lot.images.mainImage.path}" class="d-block w-100" alt=""/>
                    </div>
                    <c:forEach var="image" items="${lot.images.otherImages}">
                        <div class="carousel-item div-image">
                            <img src="${image.path}" class="d-block w-100" alt=""/>
                        </div>
                    </c:forEach>
                </div>
                <c:if test="${lot.images.otherImages.size() > 0}">
                    <button class="carousel-control-prev" type="button" data-bs-target="#carouselExampleControls"
                            data-bs-slide="prev">
                        <span class="carousel-control-prev-icon" aria-hidden="true"></span>
                        <span class="visually-hidden">Previous</span>
                    </button>
                    <button class="carousel-control-next" type="button" data-bs-target="#carouselExampleControls"
                            data-bs-slide="next">
                        <span class="carousel-control-next-icon" aria-hidden="true"></span>
                        <span class="visually-hidden">Next</span>
                    </button>
                </c:if>
            </div>
        </div>

        <div class="row col-9">
            <table class="table table-hover">
                <thead>
                <tr>
                    <th scope="col">Параметры</th>
                    <th scope="col"></th>
                </tr>
                </thead>
                <tbody>
                <tr>
                    <th scope="row">Категория</th>
                    <td>${lot.category}</td>
                </tr>
                <tr>
                    <th scope="row">Тип аукциона</th>
                    <td>${lot.auctionType}</td>
                </tr>
                <tr>
                    <th scope="row">Состояние</th>
                    <td>${lot.productCondition}</td>
                </tr>
                <tr>
                    <th scope="row">Начало аукциона</th>
                    <td>${lot.startDatetime}</td>
                </tr>
                <tr>
                    <th scope="row">Окончание аукциона</th>
                    <td>${lot.endDatetime}</td>
                </tr>
                <tr>
                    <th scope="row">Регион</th>
                    <td>${lot.region}</td>
                </tr>
                <tr>
                    <th scope="row">Город(район)</th>
                    <td>${lot.cityOrDistrict}</td>
                </tr>
                <tr>
                    <th scope="row">Статус аукциона</th>
                    <td>${lot.auctionStatus}</td>
                </tr>
                </tbody>
            </table>
        </div>

        <div class="row">
            <h2>Описание</h2>
        </div>
        <div class="row col-9">
            <p id="description">${lot.description}</p>
        </div>

        <div class="row col-9">
            <h2>Начальная цена: ${lot.initialPrice}р</h2>
        </div>

        <c:if test="${lot.auctionStatus eq AuctionStatus.APPROVED_BY_ADMIN}">
            <div class="row col-9 my-5 mx-1">
                Размер задатка составляет 10% от начальной стоимости лота.
            </div>
            <div class="row col-9 my-5">
                <c:choose>
                    <c:when test="${sessionScope.get(UserSessionAttribute.USER_ROLE.name()) eq Role.NOT_AUTHORIZED}">
                        <button type="button" class="btn btn-success w-100 disabled">
                            Для записи на участие вы должны авторизоваться
                        </button>
                    </c:when>
                    <c:when test="${requestScope.get('user_is_participate')}">
                        <button type="button" class="btn btn-success w-100 disabled">
                            Вы уже числитесь в участниках
                        </button>
                    </c:when>
                    <c:when test="${sessionScope.get(UserSessionAttribute.USER_ID.name()) eq lot.ownerId}">
                        <button type="button" class="btn btn-success w-100 disabled">
                            Участие недоступно для владельца лота
                        </button>
                    </c:when>
                    <c:otherwise>
                        <button type="button" class="btn btn-success w-100"
                                onclick="saveAuctionParticipation(${lot.lotId})">
                            Записаться на участие
                        </button>
                    </c:otherwise>
                </c:choose>
            </div>
        </c:if>

        <div class="page-buffer"></div>
    </div>
</div>

<%@include file="/WEB-INF/jsp_fragment/footer.jsp" %>

<script type="text/javascript">
    function saveAuctionParticipation(lotId){
        $.ajax({
            type: 'POST',
            url: 'ControllerServlet',
            dataType: "text",
            data: {
                requestIsAjax: true,
                command: "save_auction_participation",
                lotId: lotId
            },
            success: function (answer) {
                window.location.replace(window.location.href);
            },
            error: function (xhr, textStatus, thrownError) {
                alert("Code error: " + xhr.status + "\nMessage: " + xhr.responseText);
            }
        });
    }
</script>
</body>
</html>
