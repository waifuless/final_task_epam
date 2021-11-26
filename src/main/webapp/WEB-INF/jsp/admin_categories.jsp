<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>

<fmt:setLocale value="${cookie.get('lang').value}"/>
<fmt:setBundle basename="l10n.page.lots_filters" var="filters"/>

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

                <h2 style="border-bottom: 1px solid green; margin-top: 30px">Категории</h2>
                <div class="row">
                    <div class="col-5" style="border-right: 1px solid green">
                        <h4 style="margin-top: 30px">Добавление категорий</h4>
                        <form id="add-category-form">
                            <input type="hidden" name="command" value="add_category">
                            <input type="hidden" name="requestIsAjax" value="true">

                            <input type="text" aria-label="category-name" class="form-control my-4"
                                   placeholder="category name" name="category-name" id="category-name">
                            <button type="submit" class="btn btn-success w-100">Добавить</button>
                        </form>
                    </div>

                    <div class="col-7">
                        <h4 style="margin-top: 30px">Существующие категории</h4>
                        <p><b>Внимание!</b>Удаление категории приведет к удалению всех лотов в этой категории.</p>
                        <button type="button" class="btn btn-danger my-2" id="delete-categories">Удалить</button>
                        <div class="row col-10">

                            <table class="table table-hover">
                                <thead>
                                <tr>
                                    <th scope="col" class="text-center">
                                        <input class="form-check-input" type="checkbox" id="main-checkbox">
                                    </th>
                                    <th scope="col">Название</th>
                                </tr>
                                </thead>
                                <tbody id="categories-table-body">
                                <jsp:useBean id="categories" scope="request"
                                             type="java.util.List<by.epam.finaltask.model.Category>"/>
                                <c:forEach items="${categories}" var="category">
                                    <tr>
                                        <th scope="row" class="text-center">
                                            <input class="form-check-input ids" type="checkbox" name="ids[]" value="${category.categoryId}">
                                        </th>
                                        <td>${category.categoryName}</td>
                                    </tr>
                                </c:forEach>
                                </tbody>
                            </table>
                        </div>
                    </div>
                </div>
            </div>
        </main>
    </div>
</div>

<script src="js/nav-link.js" type="text/javascript"></script>
<script type="text/javascript">
    $(document).ready(function () {
        selectActiveNavPage('categories');
    });
</script>


<script type="text/javascript">
    $(document).ready(function () {

        let form = $('#add-category-form');
        form.submit(e => addCategory(e));

        function addCategory(e) {
            e.preventDefault();
            $.ajax({
                type: 'POST',
                url: 'ControllerServlet',
                processData: false,
                dataType: "text",
                data: form.serialize(),
                success: function (response) {
                    alert(response);
                    window.location.replace(window.location.href);
                },
                error: function (xhr, textStatus, thrownError) {
                    alert("Code error: " + xhr.status + "\nMessage: " + xhr.responseText);
                }
            });
        }


        $('#main-checkbox').click(function () {

            if ($(this).is(':checked')) {

                $('#categories-table-body input:checkbox').prop('checked', true);

            } else {

                $('#categories-table-body input:checkbox').prop('checked', false);

            }

        });

        $('#delete-categories').click(function () {
            $.ajax({
                type: 'POST',
                url: 'ControllerServlet?command=delete_categories&requestIsAjax=true',
                processData: false,
                dataType: "text",
                data: $('.ids:checked').serialize(),
                success: function (response) {
                    alert(response);
                    window.location.replace(window.location.href);
                },
                error: function (xhr, textStatus, thrownError) {
                    alert("Code error: " + xhr.status + "\nMessage: " + xhr.responseText);
                }
            });
        });
    });
</script>
</body>
</html>
