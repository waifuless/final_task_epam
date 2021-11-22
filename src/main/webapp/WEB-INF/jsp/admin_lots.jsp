<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>

<fmt:setLocale value="${cookie.get('lang').value}"/>
<fmt:setBundle basename="l10n.page.main" var="loc"/>

<!doctype html>
<html lang="en">
<head>
    <meta charset="utf-8">
    <title>Admin tools</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.1.1/dist/css/bootstrap.min.css" rel="stylesheet"
          integrity="sha384-F3w7mX95PdgyTmZZMECAngseQB83DfGTowi0iMjiWaeVhAn4FJkqJByhZMI3AhiU" crossorigin="anonymous">
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.1.1/dist/js/bootstrap.bundle.min.js"
            integrity="sha384-/bQdsTh/da6pkI1MST/rWKFNjaCP5gBSY4sEBT38Q/9RBh9AH40zEOg7Hlq2THRZ"
            crossorigin="anonymous"></script>
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.5.0/font/bootstrap-icons.css">
    <link rel="stylesheet" href="css/main-page.css">
    <script src="js/jquery-3.6.0.js" type="text/javascript"></script>
    <script src="js/region-cities-select.js" type="text/javascript"></script>
</head>
<body>

<svg xmlns="http://www.w3.org/2000/svg" style="display: none;">
    <symbol id="bootstrap" viewBox="0 0 118 94">
        <title>Bootstrap</title>
        <path fill-rule="evenodd" clip-rule="evenodd"
              d="M24.509 0c-6.733 0-11.715 5.893-11.492 12.284.214 6.14-.064 14.092-2.066 20.577C8.943 39.365 5.547 43.485 0 44.014v5.972c5.547.529 8.943 4.649 10.951 11.153 2.002 6.485 2.28 14.437 2.066 20.577C12.794 88.106 17.776 94 24.51 94H93.5c6.733 0 11.714-5.893 11.491-12.284-.214-6.14.064-14.092 2.066-20.577 2.009-6.504 5.396-10.624 10.943-11.153v-5.972c-5.547-.529-8.934-4.649-10.943-11.153-2.002-6.484-2.28-14.437-2.066-20.577C105.214 5.894 100.233 0 93.5 0H24.508zM80 57.863C80 66.663 73.436 72 62.543 72H44a2 2 0 01-2-2V24a2 2 0 012-2h18.437c9.083 0 15.044 4.92 15.044 12.474 0 5.302-4.01 10.049-9.119 10.88v.277C75.317 46.394 80 51.21 80 57.863zM60.521 28.34H49.948v14.934h8.905c6.884 0 10.68-2.772 10.68-7.727 0-4.643-3.264-7.207-9.012-7.207zM49.948 49.2v16.458H60.91c7.167 0 10.964-2.876 10.964-8.281 0-5.406-3.903-8.178-11.425-8.178H49.948z"></path>
    </symbol>
</svg>

<div class="container-fluid">
    <div class="row">

        <%@include file="/WEB-INF/jsp_fragment/admin_nav.jsp" %>

        <main class="main-side col-9 ms-sm-auto col-lg-10 px-md-4">
            <div class="container-lg">

                <h2 style="border-bottom: 1px solid green; margin-top: 30px">
                    <fmt:message bundle="${loc}" key="container.filters.header"/>
                </h2>
                <form id="filters-form">
                    <input type="hidden" name="command" value="find_lots_by_admin">
                    <input type="hidden" name="requestIsAjax" value="true">

                    <div class="row mt-4">
                        <div class="col-lg-3 col-sm-6 mb-3">
                            <label class="mb-2" for="auction-status-filter">
                                <fmt:message bundle="${loc}" key="container.filter.label.auction_type"/>
                            </label>
                            <select id="auction-status-filter" name="auction-status" class="form-select"
                                    aria-label="Default select example">
                                <option value="" selected>
                                    Any
                                </option>
                                <option value="NOT_VERIFIED">
                                    NOT_VERIFIED
                                </option>
                                <option value="APPROVED_BY_ADMIN">
                                    APPROVED_BY_ADMIN
                                </option>
                                <option value="RUNNING">
                                    RUNNING
                                </option>
                                <option value="SUSPENDED">
                                    SUSPENDED
                                </option>
                                <option value="ENDED">
                                    ENDED
                                </option>
                            </select>
                        </div>
                        <div class="col-lg-3 col-sm-6 mb-3">
                            <label class="mb-2" for="auction-type-filter">
                                <fmt:message bundle="${loc}" key="container.filter.label.auction_type"/>
                            </label>
                            <select id="auction-type-filter" name="auction-type" class="form-select"
                                    aria-label="Default select example">
                                <option value="" selected>
                                    <fmt:message bundle="${loc}" key="container.filter.type.any"/>
                                </option>
                                <option value="FORWARD">
                                    <fmt:message bundle="${loc}" key="filter.auction_type.forward"/>
                                </option>
                                <option value="REVERSE">
                                    <fmt:message bundle="${loc}" key="filter.auction_type.reverse"/>
                                </option>
                            </select>
                        </div>
                        <div class="col-lg-3 col-sm-6 mb-3">
                            <label class="mb-2" for="category-filter">
                                <fmt:message bundle="${loc}" key="container.filter.label.category"/>
                            </label>
                            <select id="category-filter" name="category" class="form-select"
                                    aria-label="Default select example">
                                <option value="" selected>
                                    <fmt:message bundle="${loc}" key="filter.category.any"/>
                                </option>
                                <jsp:useBean id="categories" scope="request"
                                             type="java.util.List<by.epam.finaltask.model.Category>"/>
                                <c:forEach items="${categories}" var="category">
                                    <option value="${category.categoryName}">${category.categoryName}</option>
                                </c:forEach>
                            </select>
                        </div>
                        <div class="col-lg-3 col-sm-6 mb-3">
                            <label class="mb-2" for="region-filter"><fmt:message bundle="${loc}"
                                                                                 key="container.filter.label.region"/></label>
                            <select id="region-filter" name="region" class="form-select"
                                    aria-label="Default select example"
                                    onchange="placeCitiesOrDistricts('region-filter','city-filter')">
                                <option value="" selected><fmt:message bundle="${loc}"
                                                                       key="filter.region.any"/></option>
                                <jsp:useBean id="regions" scope="request"
                                             type="java.util.List<by.epam.finaltask.model.Region>"/>
                                <c:forEach items="${regions}" var="region">
                                    <option value="${region.regionName}">${region.regionName}</option>
                                </c:forEach>
                            </select>
                        </div>
                        <div class="col-lg-3 col-sm-6 mb-3">
                            <label class="mb-2" for="city-filter"><fmt:message bundle="${loc}"
                                                                               key="container.filter.label.city"/></label>
                            <select disabled id="city-filter" name="city-or-district" class="form-select"
                                    aria-label="Default select example">
                                <option value="" selected>
                                    <fmt:message bundle="${loc}" key="filter.city_or_district.any"/>
                                </option>
                            </select>
                        </div>
                        <div class="col-lg-3 col-sm-6 mb-3">
                            <label class="mb-2" for="condition-filter"><fmt:message bundle="${loc}"
                                                                                    key="container.filter.label.condition"/></label>
                            <select id="condition-filter" name="product-condition" class="form-select"
                                    aria-label="Default select example">
                                <option value="" selected>
                                    <fmt:message bundle="${loc}" key="filter.condition.any"/>
                                </option>
                                <option value="NEW"><fmt:message bundle="${loc}" key="filter.condition.new"/></option>
                                <option value="USED"><fmt:message bundle="${loc}" key="filter.condition.used"/></option>
                                <option value="NOT_SPECIFIED">
                                    <fmt:message bundle="${loc}" key="filter.condition.not_specified"/>
                                </option>
                            </select>
                        </div>
                        <div class="col-lg-3 col-sm-6 mb-3">
                            <label class="mb-2" for="price-filter">
                                <fmt:message bundle="${loc}" key="container.filter.label.price"/>
                            </label>
                            <div id="price-filter" class="input-group">
                                <input type="text" name="price-from" aria-label="from"
                                       class="form-control"
                                       placeholder="<fmt:message bundle="${loc}" key="filter.price.from"/>"
                                       id="price-from">
                                <input type="text" name="price-to" aria-label="to" class="form-control"
                                       placeholder="<fmt:message bundle="${loc}" key="filter.price.to"/>" id="price-to">
                            </div>
                        </div>
                        <div class="col-lg-3 col-sm-6 mb-3">
                            <label class="mb-2" for="owner-id-filter">
                                Owner id
                            </label>
                            <div id="owner-id-filter">
                                <input type="text" name="owner-id" aria-label="owner-id"
                                       class="form-control"
                                       id="owner-id">
                            </div>
                        </div>
                        <div class="col-lg-3 col-sm-6 mb-3">
                            <label class="mb-2" for="title-filter">
                                Title
                            </label>
                            <div id="title-filter">
                                <input type="text" name="title" aria-label="title"
                                       class="form-control"
                                       id="title">
                            </div>
                        </div>
                        <div class="col-lg-3 col-sm-6 mb-3" style="display: flex; align-items: flex-end">
                            <button type="submit" class="btn btn-success w-100">
                                <fmt:message bundle="${loc}" key="container.filters.apply"/>
                            </button>
                        </div>
                        <div class="col-lg-3 col-sm-6 mb-3" style="display: flex; align-items: flex-end">
                            <button type="button" id="reset-button" class="btn btn-danger w-100">
                                <fmt:message bundle="${loc}" key="container.filters.reset"/>
                            </button>
                        </div>
                    </div>
                </form>


                <h2 style="border-bottom: 1px solid green; margin-top: 30px">
                    <fmt:message bundle="${loc}" key="container.lots.header"/>
                </h2>

                <div class="row mt-4" id="div-lots">

                </div>
            </div>
        </main>
    </div>
</div>

<script type="text/javascript">
    $(document).ready(function () {

        $('#reset-button').click(function (){
            window.location.replace(window.location.href)
        });

        let form = $('#filters-form');
        form.submit(e=>requestLots(e));

        function requestLots(e) {
            e.preventDefault();
            $.ajax({
                type: 'POST',
                url: 'ControllerServlet',
                processData: false,
                dataType: "json",
                data: form.serialize(),
                success: function (lots) {
                    printLots(lots);
                },
                error: function (xhr, textStatus, thrownError) {
                    alert("Code error: " + xhr.status + "\nMessage: " + xhr.responseText);
                }
            });
        }

        function printLots(lots){
            let divForLots = $('#div-lots');
            divForLots.empty();
            lots.forEach(function (lot){
                divForLots.append('<a href="${pageContext.request.contextPath}/ControllerServlet?command=show_lot_page&lot_id='+lot.lotId+'"'+
                   `class="container__row__a col-12 col-lg-6 mb-3 mb-3">
                    <div class="card border-dark h-100" style="max-width: 540px; max-height: 213px">
                        <div class="row g-0">
                            <div class="col-4 div-image">`+
                                `<img src="`+lot.images.mainImage.path+`" class="img-fluid rounded-start"
                                     alt="...">
                            </div>
                            <div class="col-8">
                                <div class="card-body">
                                    <h5 class="card-title">`+lot.title+`</h5>
                                    <p class="card-text category">`+lot.category+`</p>
                                    <p class="card-text auction-type">
                                        <fmt:message bundle="${loc}"
                                                     key="container.lot.auction_type"/>` +lot.auctionType+
                                    `</p>
                                    <p class="card-text region">
                                        <fmt:message bundle="${loc}" key="container.lot.region"/>` +lot.region+
                                    `</p>
                                    <p class="card-text address">
                                        <fmt:message bundle="${loc}" key="container.lot.city"/>` +lot.cityOrDistrict+
                                    `</p>
                                    <p class="card-text initial-price">
                                        <fmt:message bundle="${loc}" key="container.lot.price"/>` +lot.initialPrice+
                                    `</p>
                                    <p class="card-text auction-start"><small class="text-muted">
                                        <fmt:message bundle="${loc}"
                                                     key="container.lot.auction_start_datetime"/>`+lot.startDatetime+`</small>
                                    </p>
                                </div>
                            </div>
                        </div>
                    </div>
                </a>`);
            });
        }
    });
</script>
</body>
</html>
