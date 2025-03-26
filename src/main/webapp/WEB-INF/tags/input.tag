<%@ tag language="java" pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ tag isELIgnored="false" %>
<%@ attribute name="label" required="true" %>
<%@ attribute name="name" required="true" %>
<%@ attribute name="type" required="false" %>
<%@ attribute name="rows" required="false" %>

<c:choose>
    <c:when test="${type == 'textarea'}">
        <div class="mb-3">
            <label for="${name}" class="form-label">${label}</label>
            <textarea class="form-control" id="${name}" name="${name}" rows="${rows}"></textarea>
        </div>
    </c:when>
    <c:otherwise>
        <div class="mb-3">
            <label for="${name}" class="form-label">${label}</label>
            <input type="${type != null ? type : 'text'}" class="form-control" id="${name}" name="${name}">
        </div>
    </c:otherwise>
</c:choose>