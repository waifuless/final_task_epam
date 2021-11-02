<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix='c' uri='http://java.sun.com/jsp/jstl/core' %>
<html lang="en" xmlns="http://www.w3.org/1999/html" xmlns="http://www.w3.org/1999/html">
<head>
    <meta charset="UTF-8">
    <link rel="shortcut icon" href="images/dollar-symbol.png" type="image/x-icon">
    <title>Sing in to MyDictionary</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.1.1/dist/css/bootstrap.min.css" rel="stylesheet"
          integrity="sha384-F3w7mX95PdgyTmZZMECAngseQB83DfGTowi0iMjiWaeVhAn4FJkqJByhZMI3AhiU" crossorigin="anonymous">
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.1.1/dist/js/bootstrap.bundle.min.js"
            integrity="sha384-/bQdsTh/da6pkI1MST/rWKFNjaCP5gBSY4sEBT38Q/9RBh9AH40zEOg7Hlq2THRZ"
            crossorigin="anonymous"></script>
    <link rel="stylesheet" href="css/page-with-center-content.css">
    <link rel="stylesheet" href="https://fonts.googleapis.com/css?family=Comfortaa">
</head>
<body>
<div class="container-xl text-center w-100">
    <div class="row mb-2">
        <h1>Sign in</h1>
    </div>
    <div class="row">
        <div class="col-xl-4 col-md-6 col-9 mx-auto my-3">
            <form action="${pageContext.request.contextPath}/ControllerServlet?command=sign_in" method="post">
                <div class="form-floating my-2">
                    <input type="email" class="form-control" id="floatingInput" name="email"
                           placeholder="name@example.com">
                    <label for="floatingInput">Email address</label>
                </div>
                <div class="form-floating my-2">
                    <input type="password" class="form-control" id="floatingPassword" name="password"
                           placeholder="Password">
                    <label for="floatingPassword">Password</label>
                </div>
                <button type="submit" class="btn btn-primary w-100">Sign in</button>
            </form>
        </div>
    </div>
    <div class="row">
        <div class="mb-1">First time? <a class="link-primary"
                                         href="${pageContext.request.contextPath}/ControllerServlet?command=show_registration">Create
            an account.</a></div>
        <div>Forgot password? <a class="link-primary"
                                 href="${pageContext.request.contextPath}/ControllerServlet?command=show_restore_password">Click
            here.</a></div>
    </div>
</div>
</body>
</html>
