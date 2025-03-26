<%@ tag language="java" pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ tag isELIgnored="false" %>
<%@ attribute name="title" type="java.lang.String" required="false" %>

<div class="card mb-4">
    <div class="card-header">
        <h3 class="card-title">
            <c:choose>
                <c:when test="${not empty title}">
                    ${title}
                </c:when>
                <c:when test="${not empty sessionScope.userName}">
                    ${sessionScope.userName}&#39;s TODO
                </c:when>
                <c:otherwise>Guest&#39;s TODO</c:otherwise>
            </c:choose>
        </h3>
    </div>
    <div class="card-body">
        <jsp:doBody/>
    </div>
    <div class="card-footer clearfix">
        <c:if test="${totalPages > 1}">
            <ul class="pagination pagination-sm m-0 float-end">
                <c:if test="${currentPage > 1}">
                    <li class="page-item">
                        <a class="page-link" href="?page=${currentPage - 1}&size=${pageSize}">&laquo;</a>
                    </li>
                </c:if>

                <c:forEach var="i" begin="1" end="${totalPages}">
                    <li class="page-item ${i == currentPage ? 'active' : ''}">
                        <a class="page-link" href="?page=${i}&size=${pageSize}">${i}</a>
                    </li>
                </c:forEach>

                <c:if test="${currentPage < totalPages}">
                    <li class="page-item">
                        <a class="page-link" href="?page=${currentPage + 1}&size=${pageSize}">&raquo;</a>
                    </li>
                </c:if>
            </ul>
        </c:if>
    </div>
</div>