<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix='c' uri='http://java.sun.com/jsp/jstl/core' %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<fmt:setLocale value="${cookie.get('lang').value}"/>
<fmt:setBundle basename="l10n.page.registration" var="loc"/>

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

    <div class="container text-center w-100 my-5">
        <div class="mb-2">
            <h1><fmt:message bundle="${loc}" key="header"/></h1>
        </div>
        <div class="col-xl-4 col-md-6 col-9 mx-auto my-3">
            <form class="needs-validation"
                  action="${pageContext.request.contextPath}/ControllerServlet?command=register"
                  method="post">
                <div class="form-floating my-2">
                    <input type="email" required class="form-control <c:if test="${requestScope.containsKey('EMAIL_INVALID')
                || requestScope.containsKey('USER_WITH_EMAIL_ALREADY_EXISTS')}">is-invalid</c:if>"
                           id="emailInput" name="email" placeholder="name@example.com"
                           <c:if test="${not empty requestScope.get('email')}">value="${requestScope.get('email')}"</c:if>>
                    <label for="emailInput"><fmt:message bundle="${loc}" key="email.label"/></label>
                    <div class="invalid-feedback">
                        <c:if test="${requestScope.containsKey('EMAIL_INVALID')}">
                            <fmt:message bundle="${loc}" key="email.invalid"/>
                        </c:if>
                        <c:if test="${requestScope.containsKey('USER_WITH_EMAIL_ALREADY_EXISTS')}">
                            <fmt:message bundle="${loc}" key="email.exists"/>
                        </c:if>
                    </div>
                </div>
                <div class="form-floating my-2">
                    <input type="password" required
                           class="form-control <c:if test="${requestScope.containsKey('PASSWORD_INVALID')}">
                is-invalid</c:if>" id="PasswordInput" name="password" placeholder="Password">
                    <label for="PasswordInput"><fmt:message bundle="${loc}" key="password.label"/></label>
                    <div class="invalid-feedback">
                        <fmt:message bundle="${loc}" key="password.invalid"/>
                    </div>
                </div>
                <div class="form-floating my-2">
                    <input type="password" required
                           class="form-control <c:if test="${requestScope.containsKey('PASSWORDS_NOT_MATCH')}">
                is-invalid</c:if>" id="PasswordRepeatInput" name="passwordRepeat"
                           placeholder="Repeat password">
                    <label for="PasswordRepeatInput"><fmt:message bundle="${loc}" key="password.repeat"/></label>
                    <div class="invalid-feedback">
                        <fmt:message bundle="${loc}" key="password.repeat.not_equals"/>
                    </div>
                </div>
                <button type="submit" class="btn btn-primary w-100">
                    <fmt:message bundle="${loc}" key="registration.button"/>
                </button>
            </form>
        </div>
        <div>
            <div><fmt:message bundle="${loc}" key="have_account"/>
                <a class="link-primary"
                   href="${pageContext.request.contextPath}/ControllerServlet?command=show_sign_in">
                    <fmt:message bundle="${loc}" key="sign_in.link"/>
                </a>
            </div>
        </div>
    </div>

    <div class="page-buffer"></div>
</div>

<%@include file="/WEB-INF/jsp_fragment/footer.jsp" %>

</body>
</html>