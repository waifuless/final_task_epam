<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>

<fmt:setBundle basename="l10n.page.footer" var="footerLoc"/>

<div class="page-footer">
    <footer class="py-3 my-4" style="background-color: #e3f2fd;">
        <ul class="nav justify-content-center border-bottom pb-3 mb-3">
            <li class="nav-item"><a href="${pageContext.request.contextPath}/ControllerServlet?command=show_main_page" class="nav-link px-2 text-muted">
                <fmt:message bundle="${footerLoc}" key="home"/>
            </a></li>
            <li class="nav-item"><a href="${pageContext.request.contextPath}/ControllerServlet?command=show_user_lots" class="nav-link px-2 text-muted">
                <fmt:message bundle="${footerLoc}" key="auctions"/></a>
            </li>
        </ul>
        <p class="text-center text-muted">&copy; All rights belong to Vova (email: vova.prusenok@gmail.com)</p>
    </footer>
</div>