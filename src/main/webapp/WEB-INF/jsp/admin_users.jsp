<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>

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
    <script src="js/jquery-3.6.0.js" type="text/javascript"></script>
</head>
<body>

<div class="container-fluid">
    <div class="row">

        <%@include file="/WEB-INF/jsp_fragment/admin_nav.jsp" %>

        <main class="main-side col-9 ms-sm-auto col-lg-10 px-md-4">

            <div class="container-lg">

                <h2 style="border-bottom: 1px solid green; margin-top: 30px">Фильтры</h2>
                <form id="filters-form">
                    <input type="hidden" name="command" value="find_users_and_pages_count_by_admin">
                    <input type="hidden" name="requestIsAjax" value="true">
                    <div class="row mt-4">
                        <div class="col-lg-3 col-sm-6 mb-3">
                            <label class="mb-2" for="ban-status-filter">Статус бана</label>
                            <select id="ban-status-filter" name="ban-status" class="form-select"
                                    aria-label="Default select example">
                                <option selected value="">Любой</option>
                                <option value="true">Забанен</option>
                                <option value="false">Не забанен</option>
                            </select>
                        </div>
                        <div class="col-lg-3 col-sm-6 mb-3">
                            <label class="mb-2" for="user-id-filter">
                                User id
                            </label>
                            <div id="user-id-filter">
                                <input type="text" name="user-id" aria-label="user-id"
                                       class="form-control"
                                       id="user-id">
                            </div>
                        </div>
                        <div class="col-lg-3 col-sm-6 mb-3">
                            <label class="mb-2" for="email-filter">
                                Email
                            </label>
                            <div id="email-filter">
                                <input type="text" name="email" aria-label="email"
                                       class="form-control"
                                       id="email">
                            </div>
                        </div>
                        <div class="col-lg-3 col-sm-6 mb-3" style="display: flex; align-items: flex-end">
                            <button type="submit" class="btn btn-success w-100">
                                Применить
                            </button>
                        </div>
                        <div class="col-lg-3 col-sm-6 mb-3" style="display: flex; align-items: flex-end">
                            <button type="button" id="reset-button" class="btn btn-danger w-100">
                                Сбросить
                            </button>
                        </div>
                        <div class="col-12 mb-3" id="error-place">
                        </div>
                    </div>
                </form>

                <h2 style="border-bottom: 1px solid green; margin-top: 30px">Users</h2>
                <div class="row col-12">
                    <table class="table table-hover">
                        <thead>
                        <tr>
                            <th scope="col">User id</th>
                            <th scope="col">Email</th>
                            <th scope="col">Ban status</th>
                            <th scope="col">Cash account</th>
                            <th scope="col">Action</th>
                        </tr>
                        </thead>
                        <tbody id="users-table-body">
                        </tbody>
                    </table>
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

        </main>
    </div>
</div>

<script src="js/escape-text.js" type="text/javascript"></script>
<script src="js/nav-link.js" type="text/javascript"></script>
<script src="js/jquery.validate.js"></script>
<script type="text/javascript">
    $(document).ready(function () {
        selectActiveNavPage('users');
    });
</script>

<script src="js/pagination-range.js" type="text/javascript"></script>

<script type="text/javascript">
    $(document).ready(function () {

        $('#reset-button').click(function (){
            window.location.replace(window.location.href)
        });

        let form = $('#filters-form');
        form.submit(e=>applyFilters(e));

        form.validate({
            rules:{
                'user-id': {
                    digits: true,
                    min: 1
                },
                email:{
                    maxlength: 254
                }
            },
            messages: {
                'user-id': {
                    digits: "ID должен содержать только числа (целые)",
                    min: "Минимальный id - 1"
                },
                email:{
                    maxlength: "Максимальная длинна поля 254 символа"
                }
            },
            errorPlacement: function(error, element) {
                let errorPlace = $("#error-place");
                errorPlace.empty();
                errorPlace.append(error);
            }
        });

        requestUsers(1);
    });

    function applyFilters(e) {
        e.preventDefault();
        requestUsers(1);
    }

    function requestUsers(page) {
        if(!$("#filters-form").valid()){
            return false;
        }
        $.ajax({
            type: 'GET',
            url: 'ControllerServlet?page=' + page,
            processData: false,
            cache: false,
            dataType: "json",
            data: $('#filters-form').serialize(),
            success: function (answer) {
                printUsers(answer[0]);
                printPagination(page, answer[1], 'requestUsers', 'pagination');
            },
            error: function (xhr, textStatus, thrownError) {
                alert("Code error: " + xhr.status + "\nMessage: " + xhr.responseText);
            }
        });
    }

    function printUsers(lots){
        let divForUsers = $('#users-table-body');
        divForUsers.empty();
        lots.forEach(function (user){
            let bannedStatus;
            let rowColorClass;
            let button;
            if(user.banned){
                bannedStatus = 'banned';
                rowColorClass = 'table-danger';
                button = `<button type="button" class="btn btn-success w-100" onclick="actionWithUser(`+
                    user.userId+`, 'unban')">Unban</button>`;
            }else{
                bannedStatus = 'not banned';
                rowColorClass = 'table-success';
                button = `<button type="button" class="btn btn-danger w-100" onclick="actionWithUser(`+
                    user.userId+`, 'ban')">Ban</button>`;
            }
            divForUsers.append(`<tr class="`+rowColorClass+`">
                            <td>`+user.userId+`</td>
                            <td>`+escapeText(user.email)+`</td>
                            <td>`+bannedStatus+`</td>
                            <td>`+user.cashAccount+`</td>
                            <td>
                                `+button+`
                            </td>
                        </tr>`);
        });
    }

    function actionWithUser(id, action){
        $.ajax({
            type: 'POST',
            url: 'ControllerServlet',
            dataType: "text",
            data: {
                command: 'change_user_banned_status_by_admin',
                requestIsAjax: true,
                id: id,
                action: action,
            },
            success: function () {
                let currentPage = $('.pagination li.active').children(":first")[0].innerHTML;
                requestUsers(currentPage);
            },
            error: function (xhr, textStatus, thrownError) {
                alert("Code error: " + xhr.status + "\nMessage: " + xhr.responseText);
            }
        });
    }
</script>
</body>
</html>
