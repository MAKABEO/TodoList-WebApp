<%@ tag language="java" pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ tag isELIgnored="false" %>

<%@ attribute name="message" required="true" type="java.lang.String" %>
<%@ attribute name="type" required="false" type="java.lang.String" %>

<c:set var="toastType" value="${not empty type ? type : 'success'}" />

<c:if test="${not empty message}">
    <div class="toast-container position-fixed top-0 end-0 p-3">
        <div id="liveToast" class="toast align-items-center text-white bg-${toastType} border-0"
             role="alert" aria-live="assertive" aria-atomic="true" data-bs-autohide="true">
            <div class="d-flex">
                <div class="toast-body">
                    ${message}
                </div>
                <button type="button" class="btn-close btn-close-white me-2 m-auto"
                        data-bs-dismiss="toast" aria-label="Close"></button>
            </div>
        </div>
    </div>
</c:if>