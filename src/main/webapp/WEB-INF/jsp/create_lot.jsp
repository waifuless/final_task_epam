<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>

<fmt:setLocale value="${cookie.get('lang').value}"/>
<fmt:setBundle basename="l10n.page.create_lot" var="loc"/>

<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <link rel="shortcut icon" href="images/dollar-symbol.png" type="image/x-icon">
    <title><fmt:message bundle="${loc}" key="head.title"/></title>
    <script src="js/jquery-3.6.0.js"></script>
    <script src="js/jquery.validate.js"></script>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.1.1/dist/css/bootstrap.min.css" rel="stylesheet"
          integrity="sha384-F3w7mX95PdgyTmZZMECAngseQB83DfGTowi0iMjiWaeVhAn4FJkqJByhZMI3AhiU" crossorigin="anonymous">
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.1.1/dist/js/bootstrap.bundle.min.js"
            integrity="sha384-/bQdsTh/da6pkI1MST/rWKFNjaCP5gBSY4sEBT38Q/9RBh9AH40zEOg7Hlq2THRZ"
            crossorigin="anonymous"></script>
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.5.0/font/bootstrap-icons.css">
    <link rel="stylesheet" href="css/page-wrapper.css">
    <link rel="stylesheet" href="css/add-lot.css">
    <script src="js/add-lot.js"></script>
    <script src="js/region-cities-select.js"></script>

    <script src="js/nav-link.js" type="text/javascript"></script>
    <script type="text/javascript">
        $(document).ready(function () {
            selectActiveNavPage('lot-creation-link');
        });
    </script>
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
                               accept="image/png, image/jpeg, image/jpg" name="imageInput">
                    </form>
                    <p class="px-3 mb-2">
                        <fmt:message bundle="${loc}" key="photos.prompt"/>
                        <small><fmt:message bundle="${loc}" key="photos.prompt.small"/></small>
                    </p>
                </div>
            </div>
        </div>

        <form action="${pageContext.request.contextPath}/ControllerServlet?command=create_lot" method="post"
              id="form-new-lot">

            <div class="row col-lg-8 col-sm-9 col-11 mb-3 mx-auto mx-lg-0">
                <input type="text" style="display: none" class="imageAlert">
                <div class="gallery" id="gallery"></div>
            </div>

            <div class="row col-lg-8 col-sm-9 col-11 mb-3 mx-auto mx-lg-0">
                <p class="big-text"><fmt:message bundle="${loc}" key="necessary.alert"/></p>
            </div>

            <div class="row col-lg-5 col-sm-8 col-10 mb-3 mx-auto mx-lg-0">
                <label for="title"><fmt:message bundle="${loc}" key="lot.label.title"/></label><br>
                <input type="text" name="title" id="title" class="form-control">
            </div>

            <div class="row col-lg-5 col-sm-8 col-10 mb-3 mx-auto mx-lg-0">
                <label class="mb-2" for="category-input"><fmt:message bundle="${loc}"
                                                                      key="lot.label.category"/></label>
                <select id="category-input" name="category" class="form-select" aria-label="Default select example">
                    <option value="" disabled selected><fmt:message bundle="${loc}"
                                                                    key="lot.category.not_chosen"/></option>
                    <jsp:useBean id="categories" scope="request"
                                 type="java.util.List<by.epam.finaltask.model.Category>"/>
                    <c:forEach items="${categories}" var="category">
                        <option value="<c:out value="${category.categoryName}"/>"><c:out value="${category.categoryName}"/></option>
                    </c:forEach>
                </select>
            </div>

            <div class="row col-lg-5 col-sm-8 col-10 mb-3 mx-auto mx-lg-0">
                <label class="mb-2" for="auction-type-input"><fmt:message bundle="${loc}"
                                                                          key="lot.label.auction_type"/></label>
                <select id="auction-type-input" name="auction-type" class="form-select"
                        aria-label="Default select example">
                    <option value="" disabled selected><fmt:message bundle="${loc}"
                                                                    key="lot.auction_type.not_chosen"/></option>
                    <option value="FORWARD"><fmt:message bundle="${loc}" key="lot.auction_type.forward"/></option>
                    <option value="REVERSE"><fmt:message bundle="${loc}" key="lot.auction_type.reverse"/></option>
                </select>
            </div>

            <div class="row col-lg-5 col-sm-8 col-10 mb-3 mx-auto mx-lg-0">
                <label class="mb-2" for="condition-input">
                    <fmt:message bundle="${loc}" key="lot.label.condition"/>
                </label>
                <select id="condition-input" name="condition" class="form-select" aria-label="Default select example">
                    <option value="NOT_SPECIFIED" selected>
                        <fmt:message bundle="${loc}" key="lot.condition.not_specified"/>
                    </option>
                    <option value="NEW"><fmt:message bundle="${loc}" key="lot.condition.new"/></option>
                    <option value="USED"><fmt:message bundle="${loc}" key="lot.condition.used"/></option>
                </select>
            </div>

            <div class="row col-lg-5 col-sm-8 col-10 mb-3 mx-auto mx-lg-0">
                <label for="description"><fmt:message bundle="${loc}" key="lot.label.description"/></label><br>
                <textarea style="resize: none" name="description" id="description"
                          class="form-control"></textarea>
            </div>

            <div class="row col-lg-5 col-sm-8 col-10 mb-3 mx-auto mx-lg-0">
                <label class="big-text" for="init-price">
                    <fmt:message bundle="${loc}" key="lot.label.initial_price"/>
                </label><br>
                <input type="text" pattern="\d+" name="init-price" id="init-price" class="form-control">
            </div>

            <div class="row col-lg-5 col-sm-8 col-10 mb-3 mx-auto mx-lg-0">
                <label for="auction-start"><fmt:message bundle="${loc}" key="lot.label.auction_start"/></label><br>
                <input type="datetime-local" name="auction-start" id="auction-start" class="form-control"/>
            </div>

            <div class="row col-lg-5 col-sm-8 col-10 mb-3 mx-auto mx-lg-0">
                <label for="duration"><fmt:message bundle="${loc}" key="lot.label.auction_duration"/></label><br>
                <input type="number" step="1" name="duration" id="duration"
                       class="form-control" pattern="\d+" value="2"/>
            </div>

            <div class="row col-lg-5 col-sm-8 col-10 mb-3 mx-auto mx-lg-0">
                <p class="big-text"><fmt:message bundle="${loc}" key="lot.label.location"/></p>
                <label class="mb-2" for="region-input"><fmt:message bundle="${loc}" key="lot.label.region"/></label>
                <select id="region-input" name="region" onchange="placeCitiesOrDistricts('region-input','city-input')"
                        class="form-select" aria-label="Default select example">
                    <option value="" disabled selected>
                        <fmt:message bundle="${loc}" key="lot.region.not_chosen"/>
                    </option>
                    <jsp:useBean id="regions" scope="request" type="java.util.List<by.epam.finaltask.model.Region>"/>
                    <c:forEach items="${regions}" var="region">
                        <option value="<c:out value="${region.regionName}"/>"><c:out value="${region.regionName}"/></option>
                    </c:forEach>
                </select>
            </div>

            <div class="row col-lg-5 col-sm-8 col-10 mb-3 mx-auto mx-lg-0">
                <label class="mb-2" for="city-input"><fmt:message bundle="${loc}"
                                                                  key="lot.label.city_or_district"/></label>
                <select disabled id="city-input" name="city-or-district" class="form-select"
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

<script src="js/escape-text.js" type="text/javascript"></script>

<script type="text/javascript">
    $(document).ready(function () {

        // $('#form-new-lot').validate({
        //     rules: {
        //         email: {
        //             required: true,
        //             email: true
        //         }
        //     },
        //     messages: {
        //         email: {
        //             required: "We need your email address to contact you",
        //             email: "Your email address must be in the format of name@domain.com"
        //         }
        //     }
        // });

        $('#form-new-lot').validate({
            ignore: "",
            rules:{
                'init-price': {
                    required: true,
                    digits: true,
                    maxlength: 20
                },
                duration: {
                    digits: true,
                    range: [2, 24]
                },
                'auction-start':{
                    startDateValidation: true
                }
            }
            //todo: messages
        });

        $.validator.addMethod("startDateValidation", function (value, element) {
            let test = true;
            $.ajax({
                type: 'GET',
                url: 'ControllerServlet',
                async: false,
                cache: false,
                dataType: "text",
                data: {
                    command: "validate_auction_start_date",
                    requestIsAjax: true,
                    'auction-start': value
                },
                success: function (resp) {
                    test = true;
                },
                error: function (xhr, textStatus, thrownError) {
                    if(xhr.status>=400 && xhr.status<500){
                        test = false;
                    }
                }
            });
            return test;
        }, "Start date should be at least after 4 days");

        $.validator.addMethod("cRequired", $.validator.methods.required, "Fields should not be empty");

        $.validator.addMethod("imageRequired", function (value, element) {
            const test = $('.div__added-image').length > 0;
            if(!test){
                alert("Please, add an image.");
            }
            return test;
        }, "There should be at least one image");

        $.validator.addMethod("cSelectedNotNull", function (value, element) {
            return value !== null;
        }, "Please select some value");

        $.validator.addClassRules({
            "form-control": {
                cRequired: true
            },
            "form-select": {
                cSelectedNotNull: true
            },
            "imageAlert": {
                imageRequired: true
            }
        });

    });
</script>
</body>
</html>
