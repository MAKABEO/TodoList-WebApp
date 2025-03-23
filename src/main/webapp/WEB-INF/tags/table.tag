<%@ tag language="java" pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ attribute name="items" required="true" type="java.util.List" %>
<%@ attribute name="tableHeaders" required="true" type="java.util.List" %>
<%@ attribute name="fields" required="true" type="java.util.List" %>
<%@ attribute name="showActions" required="false" type="java.lang.Boolean" %>
<%@ tag isELIgnored="false" %>

<table class="table table-bordered" id="taskTable"
       data-page-size="${pageSize}"
       data-total-pages="${totalPages}"
       data-current-page="${currentPage}">
    <thead>
        <tr>
            <c:forEach var="tableHeader" items="${tableHeaders}">
                <th>${tableHeader}</th>
            </c:forEach>
            <c:if test="${showActions}">
                <th class="col-3">Actions</th>
            </c:if>
        </tr>
    </thead>
    <tbody>
        <c:forEach var="item" items="${items}">
            <c:set var="rowId" value="${not empty rowIdField ? item[rowIdField] : item.id}" />
            <tr class="align-middle" data-task-id="${rowId}">
                <c:forEach var="field" items="${fields}">
                    <c:set var="fieldValue" value="${not empty item[field] ? item[field] : '-'}" />
                    <td>${fieldValue}</td>
                </c:forEach>
                <c:if test="${showActions}">
                    <td>
                        <jsp:doBody />

                        <button type="button" class="btn btn-primary mb-2 edit-task-btn"
                                data-bs-toggle="modal" data-bs-target="#taskModal"
                                data-id="${item.id}" data-title="${item.title}"
                                data-description="${item.description}" data-status="${item.status}"
                                data-startdate="${item.startDate}" data-targetdate="${item.targetDate}">
                            <i class="bi bi-pencil me-2"></i>Edit
                        </button>
                        <button type="button" class="btn btn-success mb-2" onclick="completeTask(${item.id})">
                            <i class="bi bi-check-lg me-2"></i>Complete
                        </button>
                        <button type="button" class="btn btn-danger mb-2" onclick="deleteTask(${item.id})">
                            <i class="bi bi-trash me-2"></i>Delete
                        </button>
                    </td>
                </c:if>
            </tr>
        </c:forEach>
    </tbody>
</table>