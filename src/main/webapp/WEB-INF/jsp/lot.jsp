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

        <c:if test="${lot.auctionStatus eq AuctionStatus.RUNNING && requestScope.get('user_is_participate')}">
            <div class="row col-9 my-5">
                <div class="card border-primary mb-3">
                    <div class="card-header">СТАВКИ</div>
                    <div class="card-body">
                        <h5 class="card-title">Действует ${lot.auctionType} аукцион</h5>
                        <p class="card-text">Лучшая ставка сейчас: <span id="best-bid"></span></p>
                        <p class="card-text">Минимальный шаг аукциона: <fmt:formatNumber
                                type="number" maxFractionDigits="2" value="${lot.initialPrice*0.05}"/>
                        </p>
                        <div class="row col-9" id="div-best-bid-status">
                        </div>
                    </div>
                </div>
            </div>
            <div class="row col-9">
                <div class="input-group mb-3">
                    <span class="input-group-text" style="color: green;">$</span>
                    <input type="text" class="form-control" id="new-bid-input"/>
                    <button class="btn btn-outline-success" type="button" onclick="addBid(${lot.lotId})">
                        Поставить
                    </button>
                </div>
            </div>
            <div class="row col-9 mb-3">
                <button type="button" class="btn btn-primary w-100" onclick="renewBestBid(${lot.lotId})">
                    Обновить
                </button>
            </div>
        </c:if>

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
                        <button type="button" class="btn btn-danger w-100"
                                onclick="deleteAuctionParticipation(${lot.lotId})">
                            Отменить участие (вам вернется <fmt:formatNumber type="number" maxFractionDigits="2"
                                                                             value="${lot.initialPrice*0.1}"/> рублей)
                        </button>
                    </c:when>
                    <c:when test="${sessionScope.get(UserSessionAttribute.USER_ID.name()) eq lot.ownerId}">
                        <button type="button" class="btn btn-success w-100 disabled">
                            Участие недоступно для владельца лота
                        </button>
                    </c:when>
                    <c:otherwise>
                        <button type="button" class="btn btn-success w-100" data-bs-toggle="modal"
                                data-bs-target="#auctionParticipationModal">
                            Записаться на участие
                        </button>
                    </c:otherwise>
                </c:choose>
            </div>
        </c:if>

        <div class="modal fade" id="auctionParticipationModal" tabindex="-1"
             aria-labelledby="auctionParticipationModalLabel" aria-hidden="true">
            <div class="modal-dialog">
                <div class="modal-content">
                    <div class="modal-header">
                        <h5 class="modal-title" id="auctionParticipationModalLabel">Сумма залога для участия составляет,
                            р.</h5>
                        <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
                    </div>
                    <div class="modal-body" id="auctionParticipationModalBody">

                    </div>
                    <div class="modal-footer">
                        <button type="button" class="btn btn-primary" data-bs-dismiss="modal"
                                onclick="saveAuctionParticipation(${lot.lotId})">
                            Заплатить
                        </button>
                        <button type="button" class="btn btn-primary" data-bs-dismiss="modal">
                            Отказаться
                        </button>
                    </div>
                </div>
            </div>
        </div>
        <div class="page-buffer"></div>
    </div>
</div>

<%@include file="/WEB-INF/jsp_fragment/footer.jsp" %>

<script type="text/javascript">

    $(document).ready(function () {
        let modalBody = $('#auctionParticipationModalBody');
        modalBody.empty();
        modalBody.append(0.1 * ${lot.initialPrice});
        <c:if test="${lot.auctionStatus eq AuctionStatus.RUNNING && requestScope.get('user_is_participate')}">
        renewBestBid(${lot.lotId});
        </c:if>
    });

    function saveAuctionParticipation(lotId) {
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

    function deleteAuctionParticipation(lotId) {
        $.ajax({
            type: 'POST',
            url: 'ControllerServlet',
            dataType: "text",
            data: {
                requestIsAjax: true,
                command: "delete_auction_participation",
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

    function addBid(lotId) {
        let newBid = $('#new-bid-input');
        $.ajax({
            type: 'POST',
            url: 'ControllerServlet',
            dataType: "text",
            data: {
                requestIsAjax: true,
                command: "add_bid",
                lotId: lotId,
                bidAmount: newBid.val()
            },
            success: function (answer) {
                window.location.replace(window.location.href);
                renewBestBid(lotId);
            },
            error: function (xhr, textStatus, thrownError) {
                alert("Code error: " + xhr.status + "\nMessage: " + xhr.responseText);
            }
        });
    }

    function renewBestBid(lotId) {
        let bestBid = $('#best-bid');
        let bestBidStatus = $('#div-best-bid-status');
        $.ajax({
            type: 'GET',
            url: 'ControllerServlet',
            dataType: "json",
            cache: false,
            data: {
                requestIsAjax: true,
                command: "find_best_bid_and_its_belonging_to_user",
                lotId: lotId
            },
            success: function (answer) {
                bestBid.empty();
                bestBid.append(answer[0]);
                bestBidStatus.empty();
                if (answer[1]) {
                    bestBidStatus.append("Вы выигрываете");
                    bestBidStatus.addClass('text-success');
                    bestBidStatus.remove('text-danger');
                } else {
                    bestBidStatus.append("Вы не выигрываете");
                    bestBidStatus.addClass('text-danger');
                    bestBidStatus.remove('text-success');
                }
            },
            error: function (xhr, textStatus, thrownError) {
                if (xhr.status == 404) {
                    bestBid.empty();
                    bestBid.append('не существует');
                    bestBidStatus.empty();
                    bestBidStatus.append('Нет ни одной ставки');
                    return;
                }
                alert("Code error: " + xhr.status + "\nMessage: " + xhr.responseText);
            }
        });
    }
</script>
</body>
</html>
