<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>

<fmt:setLocale value="${cookie.get('lang').value}"/>
<fmt:setBundle basename="l10n.page.create_lot" var="loc"/>

<html>
<head>
    <meta charset="UTF-8">
    <link rel="shortcut icon" href="images/dollar-symbol.png" type="image/x-icon">
    <title><fmt:message bundle="${loc}" key="head.title"/></title>
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
    <script src="js/region-cities-select.js"></script>
</head>
<body>
<div class="page-wrapper">

    <%@include file="/WEB-INF/jsp_fragment/header.jsp" %>

    <div class="container">

        <div class="row col-lg-8 col-sm-9 col-11 mb-3 mx-auto mx-lg-0" id="div-input-and-images">
            <p class="big-text"><fmt:message bundle="${loc}" key="photos.title"/></p>
            <div class="mb-3 input-image">
                <div>
                    <form id="imageInputForm" enctype="multipart/form-data">
                        <label for="gallery-photo-add-input" id="gallery-photo-add-label"
                               class="m-3 btn btn-primary">
                            <fmt:message bundle="${loc}" key="photos.input.label"/>
                        </label>
                        <input type="file" id="gallery-photo-add-input"
                               accept="image/png, image/gif, image/jpeg, image/jpg" name="imageInput">
                    </form>
                    <p class="px-3 mb-2">
                        <fmt:message bundle="${loc}" key="photos.prompt"/>
                        <small><fmt:message bundle="${loc}" key="photos.prompt.small"/></small>
                    </p>
                </div>
            </div>
        </div>

        <form action="${pageContext.request.contextPath}/ControllerServlet?command=create_lot" method="post">

            <div class="row col-lg-8 col-sm-9 col-11 mb-3 mx-auto mx-lg-0">
                <div class="gallery" id="gallery"></div>
            </div>

            <div class="row col-lg-5 col-sm-8 col-10 mb-3 mx-auto mx-lg-0">
                <label for="title"><fmt:message bundle="${loc}" key="lot.label.title"/></label><br>
                <input type="text" required name="title" id="title" class="form-control">
            </div>

            <div class="row col-lg-5 col-sm-8 col-10 mb-3 mx-auto mx-lg-0">
                <label class="mb-2" for="category-filter"><fmt:message bundle="${loc}"
                                                                       key="lot.label.category"/></label>
                <select id="category-filter" name="category" class="form-select" aria-label="Default select example">
                    <option value="" disabled selected><fmt:message bundle="${loc}" key="lot.category.not_chosen"/></option>
                    <jsp:useBean id="categories" scope="request"
                                 type="java.util.List<by.epam.finaltask.model.Category>"/>
                    <c:forEach items="${categories}" var="category">
                        <option value="${category.categoryName}">${category.categoryName}</option>
                    </c:forEach>
                </select>
            </div>

            <div class="row col-lg-5 col-sm-8 col-10 mb-3 mx-auto mx-lg-0">
                <label class="mb-2" for="auction-type-filter"><fmt:message bundle="${loc}"
                                                                           key="lot.label.auction_type"/></label>
                <select id="auction-type-filter" name="auction-type" class="form-select"
                        aria-label="Default select example">
                    <option value="" disabled selected><fmt:message bundle="${loc}" key="lot.auction_type.not_chosen"/></option>
                    <option value="FORWARD"><fmt:message bundle="${loc}" key="lot.auction_type.forward"/></option>
                    <option value="REVERSE"><fmt:message bundle="${loc}" key="lot.auction_type.reverse"/></option>
                </select>
            </div>

            <div class="row col-lg-5 col-sm-8 col-10 mb-3 mx-auto mx-lg-0">
                <label class="mb-2" for="condition-filter">
                    <fmt:message bundle="${loc}" key="lot.label.condition"/>
                </label>
                <select id="condition-filter" name="condition" class="form-select" aria-label="Default select example">
                    <option value="NOT_SPECIFIED" selected>
                        <fmt:message bundle="${loc}" key="lot.condition.not_specified"/>
                    </option>
                    <option value="NEW"><fmt:message bundle="${loc}" key="lot.condition.new"/></option>
                    <option value="USED"><fmt:message bundle="${loc}" key="lot.condition.used"/></option>
                </select>
            </div>

            <div class="row col-lg-5 col-sm-8 col-10 mb-3 mx-auto mx-lg-0">
                <label for="description"><fmt:message bundle="${loc}" key="lot.label.description"/></label><br>
                <textarea style="resize: none" required name="description" id="description"
                          class="form-control"></textarea>
            </div>

            <div class="row col-lg-5 col-sm-8 col-10 mb-3 mx-auto mx-lg-0">
                <label class="big-text" for="init-price">
                    <fmt:message bundle="${loc}" key="lot.label.initial_price"/>
                </label><br>
                <input type="text" required name="init-price" id="init-price" class="form-control">
            </div>

            <div class="row col-lg-5 col-sm-8 col-10 mb-3 mx-auto mx-lg-0">
                <label for="auction-start"><fmt:message bundle="${loc}" key="lot.label.auction_start"/></label><br>
                <input type="datetime-local" required name="auction-start" id="auction-start" class="form-control"/>
            </div>

            <div class="row col-lg-5 col-sm-8 col-10 mb-3 mx-auto mx-lg-0">
                <label for="duration"><fmt:message bundle="${loc}" key="lot.label.auction_duration"/></label><br>
                <input type="number" required name="duration" id="duration" class="form-control"/>
            </div>

            <div class="row col-lg-5 col-sm-8 col-10 mb-3 mx-auto mx-lg-0">
                <p class="big-text"><fmt:message bundle="${loc}" key="lot.label.location"/></p>
                <label class="mb-2" for="region-filter"><fmt:message bundle="${loc}" key="lot.label.region"/></label>
                <select id="region-filter" name="region" onchange="placeCitiesOrDistricts('region-filter','city-filter')"
                        class="form-select" aria-label="Default select example">
                    <option value="" disabled selected>
                        <fmt:message bundle="${loc}" key="lot.region.not_chosen"/>
                    </option>
                    <jsp:useBean id="regions" scope="request" type="java.util.List<by.epam.finaltask.model.Region>"/>
                    <c:forEach items="${regions}" var="region">
                        <option value="${region.regionName}">${region.regionName}</option>
                    </c:forEach>
                </select>
            </div>

            <div class="row col-lg-5 col-sm-8 col-10 mb-3 mx-auto mx-lg-0">
                <label class="mb-2" for="city-filter"><fmt:message bundle="${loc}"
                                                                   key="lot.label.city_or_district"/></label>
                <select disabled id="city-filter" name="city-or-district" class="form-select"
                        aria-label="Default select example">
                    <option value="" disabled selected>
                        <fmt:message bundle="${loc}" key="lot.city_or_district.not_chosen"/>
                    </option>
                </select>
            </div>

            <div class="row">
                <button type="submit" class="btn btn-success mx-auto col-lg-5 col-sm-8 col-10 my-5">
                    <fmt:message bundle="${loc}" key="add_lot_form.apply"/>
                </button>
            </div>

        </form>

        <div class="page-buffer"></div>
    </div>
</div>

<%@include file="/WEB-INF/jsp_fragment/footer.jsp" %>

</body>
</html>
