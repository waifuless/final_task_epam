<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>

<fmt:setLocale value="${cookie.get('lang').value}"/>
<fmt:setBundle basename="l10n.page.main" var="loc"/>
<fmt:setBundle basename="l10n.page.lots_filters" var="filters"/>

<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <link rel="shortcut icon" href="images/dollar-symbol.png" type="image/x-icon">
    <title><fmt:message bundle="${loc}" key="head.title"/></title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.1.1/dist/css/bootstrap.min.css" rel="stylesheet"
          integrity="sha384-F3w7mX95PdgyTmZZMECAngseQB83DfGTowi0iMjiWaeVhAn4FJkqJByhZMI3AhiU" crossorigin="anonymous">
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.1.1/dist/js/bootstrap.bundle.min.js"
            integrity="sha384-/bQdsTh/da6pkI1MST/rWKFNjaCP5gBSY4sEBT38Q/9RBh9AH40zEOg7Hlq2THRZ"
            crossorigin="anonymous"></script>
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.5.0/font/bootstrap-icons.css">
    <link rel="stylesheet" href="css/main-page.css">
    <link rel="stylesheet" href="css/page-wrapper.css">
</head>
<body>
<div class="page-wrapper">

    <%@include file="/WEB-INF/jsp_fragment/header.jsp" %>

    <div class="container-lg">

        <ul class="nav nav-tabs">
            <li class="nav-item">
                <button class="nav-link active user-lots-nav-item" id="owned-lots-button">Мои лоты</button>
            </li>
            <li class="nav-item">
                <button class="nav-link user-lots-nav-item" id="ended-owned-lots-button">Мои завершенные лоты</button>
            </li>
            <li class="nav-item">
                <button class="nav-link user-lots-nav-item" id="participate-lots-button">Записан на участие</button>
            </li>
            <li class="nav-item">
                <button class="nav-link user-lots-nav-item" id="running-participate-lots-button">Идут ставки</button>
            </li>
            <li class="nav-item">
                <button class="nav-link user-lots-nav-item" id="ended-participate-lots-button">Завершенные лоты</button>
            </li>
        </ul>

        <div class="row mt-4" id="div-lots">
        </div>

        <div class="row">
            <ul class="pagination mx-auto" style="justify-content: center" id="pagination">
                <li class="page-item" id="leftArrow">
                    <button class="page-link">&laquo;</button>
                </li>
                <li class="page-item" id="rightArrow">
                    <button class="page-link">&raquo;</button>
                </li>
            </ul>
        </div>
    </div>

    <div class="page-buffer"></div>
</div>

<%@include file="/WEB-INF/jsp_fragment/footer.jsp" %>

<script src="js/jquery-3.6.0.js" type="text/javascript"></script>
<script src="js/escape-text.js" type="text/javascript"></script>
<script src="js/region-cities-select.js" type="text/javascript"></script>

<script src="js/nav-link.js" type="text/javascript"></script>
<script src="js/pagination-range.js" type="text/javascript"></script>
<script type="text/javascript">
    $(document).ready(function () {
        selectActiveNavPage('users-lots-link');
    });
</script>

<script type="text/javascript">
    $(document).ready(function () {
        requestOwnedLots(1);
        $('#owned-lots-button').click(function () { //+
            disactiveLinksAndActiveOnParent(this);
            requestOwnedLots(1);
        });
        $('#ended-owned-lots-button').click(function () { //+
            disactiveLinksAndActiveOnParent(this);
            requestOwnedEndedLots(1);
        });
        $('#participate-lots-button').click(function () { //+
            disactiveLinksAndActiveOnParent(this);
            requestParticipatedLots(1);
        });
        $('#running-participate-lots-button').click(function () {
            disactiveLinksAndActiveOnParent(this);
            requestParticipatedRunningLots(1);
        });
        $('#ended-participate-lots-button').click(function () {
            disactiveLinksAndActiveOnParent(this);
            requestParticipatedEndedLots(1);
        });
    });

    function disactiveLinksAndActiveOnParent(elem) {
        $('.user-lots-nav-item').removeClass('active');
        $(elem).addClass('active');
    }

    function requestOwnedLots(page) { //Просто посмотреть
        requestLots(page, 'find_lots_and_pages_count_owned_by_user', 'requestOwnedLots', printLotsWithStatus);
    }

    function requestOwnedEndedLots(page) { //Забрать задаток если кто-о выиграл и псомотреть email
        requestLots(page, 'find_lots_and_pages_count_owned_ended_with_results_by_user', 'requestOwnedEndedLots',
            printOwnedEndedLots);
    }

    function requestParticipatedLots(page) { //Просто посмотреть
        requestLots(page, 'find_lots_and_pages_count_participated_by_user', 'requestParticipatedLots',
            printLotsWithStatus);
    }

    function requestParticipatedRunningLots(page) { //Просто посмотреть
        requestLots(page, 'find_lots_and_pages_count_participated_running_by_user',
            'requestParticipatedRunningLots', printSimpleLots);
    }

    function requestParticipatedEndedLots(page) { //Посмотреть email владельца аука
        requestLots(page, 'find_lots_and_pages_count_participated_ended_with_results_by_user',
            'requestParticipatedEndedLots', printEndedParticipatedLots);
    }

    function requestLots(page, command, funcNameToRequestLots, funcToPrintLots) {
        $.ajax({
            type: 'GET',
            url: 'ControllerServlet',
            dataType: "json",
            cache: false,
            data: {
                page: page,
                command: command,
                requestIsAjax: true
            },
            success: function (answer) {
                funcToPrintLots(answer[0]);
                printPagination(page, answer[1], funcNameToRequestLots, 'pagination');
            },
            error: function (xhr, textStatus, thrownError) {
                alert("Code error: " + xhr.status + "\nMessage: " + xhr.responseText);
            }
        });
    }

    function printSimpleLots(lots) {
        printLots(lots, false);
    }

    function printLotsWithStatus(lots) {
        printLots(lots, true);
    }

    function printLots(lots, printWithStatus) {
        let divForLots = $('#div-lots');
        divForLots.empty();
        if (jQuery.isEmptyObject(lots)) {
            return;
        }
        lots.forEach(function (lot) {
            let additionalInfo = printWithStatus ? `<p class="card-text auction-status">
                                        Статус аукциона: ` + escapeText(lot.auctionStatus) +
                `</p>` : '';
            divForLots.append('<a href="${pageContext.request.contextPath}/ControllerServlet?command=show_lot_page&lot_id=' + lot.lotId + '"' +
                `class="container__row__a col-12 col-lg-6 mb-3 mb-3" target="_blank">
                    <div class="card border-dark h-100" style="max-width: 540px; max-height: 250px">
                        <div class="row g-0">
                            <div class="col-4 div-image">` +
                `<img src="` + escapeText(lot.images.mainImage.path) + `" class="img-fluid rounded-start"
                                     alt="...">
                            </div>
                            <div class="col-8">
                                <div class="card-body">
                                    <h5 class="card-title">` + escapeText(lot.title) + `</h5>
                                    <p class="card-text category">` + escapeText(lot.category) + `</p>
                                    <p class="card-text auction-type">
                                        <fmt:message bundle="${loc}"
                                                     key="container.lot.auction_type"/>` + escapeText(lot.auctionType) +
                `</p>
                                    <p class="card-text region">
                                        <fmt:message bundle="${loc}" key="container.lot.region"/>` + escapeText(lot.region) +
                `</p>
                                    <p class="card-text address">
                                        <fmt:message bundle="${loc}" key="container.lot.city"/>` + escapeText(lot.cityOrDistrict) +
                `</p>
                                    <p class="card-text initial-price">
                                        <fmt:message bundle="${loc}" key="container.lot.price"/>` + escapeText(lot.initialPrice) +
                `</p>
                                    ` + additionalInfo + `
                                    <p class="card-text auction-start"><small class="text-muted">
                                        <fmt:message bundle="${loc}"
                                                     key="container.lot.auction_start_datetime"/>` + escapeText(lot.startDatetime) + `</small>
                                    </p>
                                </div>
                            </div>
                        </div>
                    </div>
                </a>`);
        });
    }


    function printEndedParticipatedLots(lotsWithResults) {
        let divForLots = $('#div-lots');
        divForLots.empty();
        if (jQuery.isEmptyObject(lotsWithResults)) {
            return;
        }
        lotsWithResults.forEach(function (val) {
            let key = val[0];
            let value = val[1];
            let additionalInfo = value == null ? `<div class="col-6">
                <button type="button" class="btn btn-primary my-5" onclick="deleteAuctionParticipation(` + key.lotId + `)">
                    Забрать задаток
                </button></div>` :
                `<div class="text-primary col-6">
                <div class="card border-primary" style="width: 540px; height: 213px">
                    <div class="card-header">Результат</div>
                    <div class="card-body">
                        <h5 class="card-title">Вы выиграли аукцион</h5>
                        <p class="card-text">Со ставкой: ` + value.winnerBidAmount + `</p>
                        <p class="card-text">Владельцу лота будет выдан ваш задаток в: ` + value.deposit + `р</p>
                        <p class="card-text">Email владельца лота: ` + escapeText(value.emailToContact) + `р</p>
                    </div>
                </div>
                </div>`;
            divForLots.append(findSimpleLotCard(key) + additionalInfo);
        });
    }

    function printOwnedEndedLots(lotsWithResults) {
        let divForLots = $('#div-lots');
        divForLots.empty();
        if (jQuery.isEmptyObject(lotsWithResults)) {
            return;
        }
        lotsWithResults.forEach(function (val) {
            let key = val[0];
            let value = val[1];
            let additionalInfo;
            if(value == null){
                additionalInfo = `<div class="text-primary col-6 my-5">Никто не сделал ставок</div>`;
            } else{
                let takeDepositInfo = value.depositIsTakenByOwner? `Вы уже забрали задаток` :
                    `<button type="button" class="btn btn-primary" onclick="retrieveAuctionParticipationDepositFromWinner(` + key.lotId + `)">
                    Забрать задаток в: ` + value.deposit + `р
                </button>`;
                additionalInfo = `<div class="text-primary col-6">
                <div class="card border-primary" style="width: 540px; height: 213px">
                    <div class="card-header">Результат</div>
                    <div class="card-body">
                        <h5 class="card-title">Определен победитель аукциона</h5>
                        <p class="card-text">Со ставкой: ` + value.winnerBidAmount + `</p>
                        <p class="card-text">`+takeDepositInfo+`</p>
                        <p class="card-text">Email победителя: ` + escapeText(value.emailToContact) + `р</p>
                    </div>
                </div>
                </div>`;
            }
            divForLots.append(findSimpleLotCard(key) + additionalInfo);
        });
    }

    function findSimpleLotCard(key) {
        return '<a href="${pageContext.request.contextPath}/ControllerServlet?command=show_lot_page&lot_id=' + key.lotId + '"' +
            `class="container__row__a col-6 mb-3 mb-3" target="_blank">
                    <div class="card border-dark h-100" style="max-width: 540px; max-height: 213px">
                        <div class="row g-0">
                            <div class="col-4 div-image">` +
            `<img src="` + escapeText(key.images.mainImage.path) + `" class="img-fluid rounded-start"
                                     alt="...">
                            </div>
                            <div class="col-8">
                                <div class="card-body">
                                    <h5 class="card-title">` + escapeText(key.title) + `</h5>
                                    <p class="card-text category">` + escapeText(key.category) + `</p>
                                    <p class="card-text auction-type">
                                        <fmt:message bundle="${loc}"
                                                     key="container.lot.auction_type"/>` + escapeText(key.auctionType) +
            `</p>
                                    <p class="card-text region">
                                        <fmt:message bundle="${loc}" key="container.lot.region"/>` + escapeText(key.region) +
            `</p>
                                    <p class="card-text address">
                                        <fmt:message bundle="${loc}" key="container.lot.city"/>` + escapeText(key.cityOrDistrict) +
            `</p>
                                    <p class="card-text initial-price">
                                        <fmt:message bundle="${loc}" key="container.lot.price"/>` + key.initialPrice +
            `</p>
                                    <p class="card-text auction-start"><small class="text-muted">
                                        <fmt:message bundle="${loc}"
                                                     key="container.lot.auction_start_datetime"/>` + escapeText(key.startDatetime) + `</small>
                                    </p>
                                </div>
                            </div>
                        </div>
                    </div>
                </a>`;
    }

    function retrieveAuctionParticipationDepositFromWinner(lotId) {
        $.ajax({
            type: 'POST',
            url: 'ControllerServlet',
            dataType: "text",
            data: {
                requestIsAjax: true,
                command: "retrieve_auction_participation_deposit_from_winner",
                lotId: lotId
            },
            success: function (answer) {
                requestOwnedEndedLots(1);
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
                requestParticipatedEndedLots(1);
            },
            error: function (xhr, textStatus, thrownError) {
                alert("Code error: " + xhr.status + "\nMessage: " + xhr.responseText);
            }
        });
    }
</script>
</body>
</html>