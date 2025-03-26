<%@ tag language="java" pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ tag isELIgnored="false" %>
<%@ attribute name="label" required="true" %>
<%@ attribute name="name" required="true" %>
<%@ attribute name="options" required="true" type="java.util.List"%>

<div class="mb-3">
    <label for="${name}" class="form-label">${label}</label>
    <select class="form-control" id="${name}" name="${name}">
        <c:forEach var="option" items="${options}">
            <option value="${option}">${option}</option>
        </c:forEach>
    </select>
</div>