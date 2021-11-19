<jsp:useBean id="lots" scope="request" type="java.util.List<by.epam.finaltask.model.LotWithImages>"/>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>

<fmt:setLocale value="${cookie.get('lang').value}"/>
<fmt:setBundle basename="l10n.page.main" var="loc"/>

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

        <h2 style="border-bottom: 1px solid green; margin-top: 30px">
            <fmt:message bundle="${loc}" key="container.filters.header"/>
        </h2>
        <form method="post"
              action="${pageContext.request.contextPath}/ControllerServlet?command=set_main_page_lot_context">
            <div class="row mt-4">
                <div class="col-lg-3 col-sm-6 mb-3">
                    <label class="mb-2" for="auction-type-filter">
                        <fmt:message bundle="${loc}" key="container.filter.label.auction_type"/>
                    </label>
                    <select id="auction-type-filter" name="auction-type" class="form-select"
                            aria-label="Default select example">
                        <option value="" selected><fmt:message bundle="${loc}"
                                                               key="container.filter.type.any"/></option>
                        <option value="FORWARD"><fmt:message bundle="${loc}"
                                                             key="filter.auction_type.forward"/></option>
                        <option value="REVERSE"><fmt:message bundle="${loc}"
                                                             key="filter.auction_type.reverse"/></option>
                    </select>
                </div>
                <div class="col-lg-3 col-sm-6 mb-3">
                    <label class="mb-2" for="category-filter">
                        <fmt:message bundle="${loc}" key="container.filter.label.category"/>
                    </label>
                    <select id="category-filter" name="category" class="form-select"
                            aria-label="Default select example">
                        <option value="" selected><fmt:message bundle="${loc}" key="filter.category.any"/></option>
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
                    <select id="region-filter" name="region" class="form-select" aria-label="Default select example"
                            onchange="placeCitiesOrDistricts('region-filter','city-filter')">
                        <option value="" selected><fmt:message bundle="${loc}" key="filter.region.any"/></option>
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
                    <label class="mb-2" for="price-filter"><fmt:message bundle="${loc}"
                                                                        key="container.filter.label.price"/></label>
                    <div id="price-filter" class="input-group">
                        <input type="text" name="price-from" aria-label="from"
                               class="form-control"
                               placeholder="<fmt:message bundle="${loc}" key="filter.price.from"/>" id="price-from">
                        <input type="text" name="price-to" aria-label="to" class="form-control"
                               placeholder="<fmt:message bundle="${loc}" key="filter.price.to"/>" id="price-to">
                    </div>
                </div>
                <div class="col-lg-3 col-sm-6 mb-3" style="display: flex; align-items: flex-end">
                    <button type="submit" class="btn btn-success w-100">
                        <fmt:message bundle="${loc}" key="container.filters.apply"/>
                    </button>
                </div>
                <div class="col-lg-3 col-sm-6 mb-3" style="display: flex; align-items: flex-end">
                    <a class="btn btn-danger w-100"
                       href="${pageContext.request.contextPath}/ControllerServlet?command=remove_main_page_lot_context">
                        <fmt:message bundle="${loc}" key="container.filters.reset"/>
                    </a>
                </div>
            </div>
        </form>


        <h2 style="border-bottom: 1px solid green; margin-top: 30px">
            <fmt:message bundle="${loc}" key="container.lots.header"/>
        </h2>

        <div class="row mt-4">
            <c:forEach var="lot" items="${lots}">
                <a href="${pageContext.request.contextPath}/ControllerServlet?command=show_lot_page&lot_id=${lot.lotId}"
                   class="container__row__a col-12 col-lg-6 mb-3 mb-3">
                    <div class="card border-dark h-100" style="max-width: 540px; max-height: 213px">
                        <div class="row g-0">
                            <div class="col-4 div-image">
                                <img src="${lot.images.mainImage.path}" class="img-fluid rounded-start"
                                     alt="...">
                            </div>
                            <div class="col-8">
                                <div class="card-body">
                                    <h5 class="card-title">${lot.title}</h5>
                                    <p class="card-text category">${lot.category}</p>
                                    <p class="card-text auction-type">
                                        <fmt:message bundle="${loc}"
                                                     key="container.lot.auction_type"/> ${lot.auctionType}
                                    </p>
                                    <p class="card-text region">
                                        <fmt:message bundle="${loc}" key="container.lot.region"/> ${lot.region}
                                    </p>
                                    <p class="card-text address">
                                        <fmt:message bundle="${loc}" key="container.lot.city"/> ${lot.cityOrDistrict}
                                    </p>
                                    <p class="card-text initial-price">
                                        <fmt:message bundle="${loc}" key="container.lot.price"/> ${lot.initialPrice}
                                    </p>
                                    <p class="card-text auction-start"><small class="text-muted">
                                        <fmt:message bundle="${loc}"
                                                     key="container.lot.auction_start_datetime"/>${lot.startDatetime}</small>
                                    </p>
                                </div>
                            </div>
                        </div>
                    </div>
                </a>
            </c:forEach>
        </div>
    </div>


    <div class="page-buffer"></div>
</div>

<%@include file="/WEB-INF/jsp_fragment/footer.jsp" %>

<script src="js/jquery-3.6.0.js" type="text/javascript"></script>
<script src="js/region-cities-select.js" type="text/javascript"></script>

<script src="js/nav-link.js" type="text/javascript"></script>
<script type="text/javascript">
    $(document).ready(function () {
        selectActiveNavPage('main-link');
    });
</script>

<script type="text/javascript">
    $(document).ready(function () {

        let filtersContext = ${requestScope.get("lotContextJson")};

        function setOption(selectId, contextFiledValue) {
            if (contextFiledValue != null) {
                $('#' + selectId + ' option[value="' + contextFiledValue + '"]').attr('selected', 'selected');
            }
        }

        function setInput(inputId, contextInputValue) {
            if (contextInputValue != null) {
                $('#' + inputId).val(contextInputValue);
            }
        }

        setOption('auction-type-filter', filtersContext.auctionType);
        setOption('category-filter', filtersContext.category);
        setOption('region-filter', filtersContext.region);
        placeCitiesOrDistricts('region-filter', 'city-filter', function (){
            setOption('city-filter', filtersContext.cityOrDistrict);
        });
        setOption('condition-filter', filtersContext.productCondition);
        setInput('price-from', filtersContext.minInitialPrice);
        setInput('price-to', filtersContext.maxInitialPrice);

    });
</script>
</body>
</html>