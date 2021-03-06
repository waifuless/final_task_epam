<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>

<fmt:setLocale value="${cookie.get('lang').value}"/>
<fmt:setBundle basename="l10n.page.lots_filters" var="filters"/>
<fmt:setBundle basename="l10n.page.admin_categories" var="admin_categories"/>
<fmt:setBundle basename="l10n.page.validation" var="validation"/>

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

<div class="container-fluid">
    <div class="row">

        <%@include file="/WEB-INF/jsp_fragment/admin_nav.jsp" %>

        <main class="main-side col-9 ms-sm-auto col-lg-10 px-md-4">

            <div class="container-lg">

                <h2 style="border-bottom: 1px solid green; margin-top: 30px"><fmt:message bundle="${admin_categories}" key="title"/></h2>
                <div class="row">
                    <div class="col-5" style="border-right: 1px solid green">
                        <h4 style="margin-top: 30px"><fmt:message bundle="${admin_categories}" key="title.add.categories"/></h4>
                        <form id="add-category-form">
                            <input type="hidden" name="command" value="add_category">
                            <input type="hidden" name="requestIsAjax" value="true">

                            <input type="text" aria-label="category-name" class="form-control my-4"
                                   placeholder="category name" name="category-name" id="category-name">
                            <button type="submit" class="btn btn-success w-100"><fmt:message bundle="${admin_categories}" key="button.add"/></button>
                        </form>
                    </div>

                    <div class="col-7">
                        <h4 style="margin-top: 30px"><fmt:message bundle="${admin_categories}" key="title.exists.categories"/></h4>
                        <p><b><fmt:message bundle="${admin_categories}" key="delete.alert"/></b> <fmt:message bundle="${admin_categories}" key="delete.text"/></p>
                        <button type="button" class="btn btn-danger my-2" id="delete-categories"><fmt:message bundle="${admin_categories}" key="button.delete"/></button>
                        <div class="row col-10">

                            <table class="table table-hover">
                                <thead>
                                <tr>
                                    <th scope="col" class="text-center">
                                        <input class="form-check-input" type="checkbox" id="main-checkbox">
                                    </th>
                                    <th scope="col"><fmt:message bundle="${admin_categories}" key="table.col.name"/></th>
                                    <th scope="col"></th>
                                </tr>
                                </thead>
                                <tbody id="categories-table-body">
                                <jsp:useBean id="categories" scope="request"
                                             type="java.util.List<by.epam.finaltask.model.Category>"/>
                                <c:forEach items="${categories}" var="category">
                                    <tr>
                                        <th scope="row" class="text-center">
                                            <input class="form-check-input ids" type="checkbox" name="ids[]"
                                                   value="${category.categoryId}">
                                        </th>
                                        <td class="category-name"><c:out value="${category.categoryName}"/></td>
                                        <td><a class="link-dark rename-link"><fmt:message bundle="${admin_categories}" key="update.name"/></a></td>
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

<script src="js/escape-text.js" type="text/javascript"></script>
<script src="js/jquery.validate.js"></script>
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

        form.validate({
            rules:{
                'category-name':{
                    required: true,
                    maxlength: 64
                }
            },
            messages: {
                'category-name':{
                    required: "<fmt:message bundle="${validation}" key="required"/>",
                    maxlength: "<fmt:message bundle="${validation}" key="category-name.maxlength"/>"
                }
            }
        });

        function addCategory(e) {
            if(!form.valid()){
                return false;
            }
            e.preventDefault();
            $.ajax({
                type: 'POST',
                url: 'ControllerServlet',
                processData: false,
                dataType: "text",
                data: form.serialize(),
                success: function () {
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
            let checked = $('.ids:checked');
            if (!confirm("<fmt:message bundle="${admin_categories}" key="confirm.alert.start"/> " + checked.length + " <fmt:message bundle="${admin_categories}" key="confirm.alert.end"/>")) {
                return;
            }
            $.ajax({
                type: 'POST',
                url: 'ControllerServlet?command=delete_categories&requestIsAjax=true',
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
        });

        $('.rename-link').click(function () {
            let parentRow = $(this).parent().parent();
            let oldCategoryName = parentRow.children('.category-name').text();
            let categoryId = parentRow.find('th > .ids').val();
            let newName = prompt("<fmt:message bundle="${admin_categories}" key="prompt.update.name"/> " + oldCategoryName);
            if(newName==null || newName.length<=0 || oldCategoryName === newName){
                return;
            }
            $.ajax({
                type: 'POST',
                url: 'ControllerServlet',
                dataType: "text",
                data: {
                    requestIsAjax: true,
                    command: "update_category",
                    id: categoryId,
                    oldCategoryName: oldCategoryName,
                    newCategoryName: newName
                },
                success: function () {
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
