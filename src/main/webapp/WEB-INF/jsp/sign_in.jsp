<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix='c' uri='http://java.sun.com/jsp/jstl/core' %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<fmt:setLocale value="${cookie.get('lang').value}"/>
<fmt:setBundle basename="l10n.page.signin" var="loc"/>

<!DOCTYPE html>
<html xmlns="http://www.w3.org/1999/html" xmlns="http://www.w3.org/1999/html">
<head>
    <meta charset="UTF-8">
    <link rel="shortcut icon" href="images/dollar-symbol.png" type="image/x-icon">
    <title><fmt:message bundle="${loc}" key="head.title"/></title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.1.1/dist/css/bootstrap.min.css" rel="stylesheet"
          integrity="sha384-F3w7mX95PdgyTmZZMECAngseQB83DfGTowi0iMjiWaeVhAn4FJkqJByhZMI3AhiU" crossorigin="anonymous">
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.1.1/dist/js/bootstrap.bundle.min.js"
            integrity="sha384-/bQdsTh/da6pkI1MST/rWKFNjaCP5gBSY4sEBT38Q/9RBh9AH40zEOg7Hlq2THRZ"
            crossorigin="anonymous"></script>
    <link rel="stylesheet" href="css/page-with-center-content.css">
    <link rel="stylesheet" href="css/page-wrapper.css">
    <link rel="stylesheet" href="https://fonts.googleapis.com/css?family=Comfortaa">
</head>
<body>
<div class="page-wrapper">

    <%@include file="/WEB-INF/jsp_fragment/header.jsp" %>

    <div class="container-xl text-center w-100 my-5">
        <div class="row mb-2">
            <h1><fmt:message bundle="${loc}" key="header"/></h1>
        </div>
        <div class="row">
            <div class="col-xl-4 col-md-6 col-9 mx-auto my-3">
                <form action="${pageContext.request.contextPath}/ControllerServlet?command=sign_in" method="post">
                    <div class="form-floating my-2">
                        <input type="email" class="form-control <c:if test="${requestScope.containsKey('EMAIL_OR_PASSWORD_INVALID')}">
                is-invalid</c:if>" id="floatingInput" name="email"
                               placeholder="name@example.com" required>
                        <label for="floatingInput"><fmt:message bundle="${loc}" key="email.label"/></label>
                    </div>
                    <div class="form-floating my-2">
                        <input type="password" class="form-control <c:if test="${requestScope.containsKey('EMAIL_OR_PASSWORD_INVALID')}">
                is-invalid</c:if>" id="floatingPassword" name="password"
                               placeholder="Password" required>
                        <label for="floatingPassword"><fmt:message bundle="${loc}" key="password.label"/></label>
                        <div class="invalid-feedback">
                            <fmt:message bundle="${loc}" key="email_or_password.invalid"/>
                        </div>
                    </div>
                    <button type="submit" class="btn btn-primary w-100">
                        <fmt:message bundle="${loc}" key="sign_in.button"/>
                    </button>
                </form>
            </div>
        </div>
        <div class="row">
            <div class="mb-1">
                <fmt:message bundle="${loc}" key="not_have_account"/>
                <a class="link-primary"
                   href="${pageContext.request.contextPath}/ControllerServlet?command=show_registration">
                    <fmt:message bundle="${loc}" key="registration.link"/>
                </a>
            </div>
        </div>
    </div>

    <div class="page-buffer"></div>
</div>

<%@include file="/WEB-INF/jsp_fragment/footer.jsp" %>

</body>
</html>
