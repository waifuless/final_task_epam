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
    <link rel="stylesheet" href="css/main-page.css">
    <script src="js/jquery-3.6.0.js" type="text/javascript"></script>
</head>
<body>

<div class="container-fluid">
    <div class="row">

        <%@include file="/WEB-INF/jsp_fragment/admin_nav.jsp" %>

        <main class="main-side col-9 ms-sm-auto col-lg-10 px-md-4">

            <div class="container-lg">

                <h2 style="border-bottom: 1px solid green; margin-top: 30px">Новые лоты</h2>
                <button type="button" class="btn btn-success" id="approve-lots">Одобрить</button>
                <button type="button" class="btn btn-danger" id="deny-lots">Отклонить</button>
                <div class="row col-10">
                    <table class="table table-hover">
                        <thead>
                        <tr>
                            <th scope="col" class="text-center">
                                <input class="form-check-input" type="checkbox" id="main-checkbox">
                            </th>
                            <th scope="col">Lot id</th>
                            <th scope="col">Owner id</th>
                            <th scope="col">Title</th>
                            <th scope="col"></th>
                        </tr>
                        </thead>
                        <tbody id="lots-table-body">
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

<script src="js/nav-link.js" type="text/javascript"></script>
<script type="text/javascript">
    $(document).ready(function () {
        selectActiveNavPage('new-lots');
    });
</script>

<script src="js/pagination-range.js" type="text/javascript"></script>

<script type="text/javascript">
    $(document).ready(function () {

        requestLots(1);

        $('#main-checkbox').click(function () {
            if ($(this).is(':checked')) {
                $('#lots-table-body input:checkbox').prop('checked', true);
            } else {
                $('#lots-table-body input:checkbox').prop('checked', false);
            }
        });

        $('#deny-lots').click(function () {
            let checked = $('.ids:checked');
            if (!confirm("Отклонить " + checked.length + " лотов?")) {
                return;
            }
            updateStatus('DENIED', checked);
        });

        $('#approve-lots').click(function () {
            let checked = $('.ids:checked');
            if (!confirm("Одобрить " + checked.length + " лотов?")) {
                return;
            }
            updateStatus('APPROVED_BY_ADMIN', checked);
        });
    });

    function requestLots(page) {
        $.ajax({
            type: 'GET',
            url: 'ControllerServlet',
            dataType: "json",
            cache: false,
            data: {
                requestIsAjax: true,
                command: "find_lots_and_pages_count_by_admin",
                page: page,
                'auction-status': 'NOT_VERIFIED'
            },
            success: function (answer) {
                printLots(answer[0]);
                printPagination(page, answer[1], 'requestLots', 'pagination');
            },
            error: function (xhr, textStatus, thrownError) {
                alert("Code error: " + xhr.status + "\nMessage: " + xhr.responseText);
            }
        });
    }

    function printLots(lots){
        let divForLots = $('#lots-table-body');
        divForLots.empty();
        lots.forEach(function (lot){
            divForLots.append(
                `<tr>
                        <th scope="row" class="text-center">
                            <input class="form-check-input ids" type="checkbox" name="ids[]" value="`+lot.lotId+`">
                        </th>
                        <td>`+lot.lotId+`</td>
                        <td>`+lot.ownerId+`</td>
                        <td>`+lot.title+`</td>
                        <td>
                            <a class="link-dark" target="_blank" href="${pageContext.request.contextPath}/ControllerServlet?command=show_lot_page&lot_id=`+lot.lotId+`">
                                Перейти
                            </a>
                        </td>
                    </tr>`);
        });
    }

    function updateStatus(newStatus, checked){
        $.ajax({
            type: 'POST',
            url: 'ControllerServlet?command=update_auction_status&requestIsAjax=true&new_status='+newStatus,
            processData: false,
            dataType: "text",
            data: checked.serialize(),
            success: function () {
                window.location.replace(window.location.href);
            },
            error: function (xhr, textStatus, thrownError) {
                alert("Code error: " + xhr.status + "\nMessage: " + xhr.responseText);
            }
        });
    }
</script>
</body>
</html>
