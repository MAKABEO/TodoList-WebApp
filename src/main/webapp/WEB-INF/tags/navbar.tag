<%@ tag language="java" pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<nav class="app-header navbar navbar-expand bg-body">
    <div class="container-fluid">
        <ul class="navbar-nav">
            <li class="nav-item">
                <a class="nav-link mt-1 mb-n1" data-lte-toggle="sidebar" href="#" role="button">
                    <i class="bi bi-list"></i>
                </a>
            </li>
            <li class="nav-item d-none d-md-block">
                <a href="<c:url value='/TodoList'/>" class="nav-link">Home</a>
            </li>
        </ul>

        <ul class="navbar-nav ms-auto">
            <jsp:doBody />
            <li class="nav-item">
                <a class="nav-link mt-1 mb-n1" href="#" data-lte-toggle="fullscreen">
                    <i data-lte-icon="maximize" class="bi bi-arrows-fullscreen"></i>
                    <i data-lte-icon="minimize" class="bi bi-fullscreen-exit" style="display: none"></i>
                </a>
            </li>

            <li class="nav-item dropdown user-menu">
                <a href="#" class="nav-link dropdown-toggle" data-bs-toggle="dropdown">
                    <span class="d-none d-md-inline">
                        <c:choose>
                            <c:when test="${not empty sessionScope.userName}">
                                ${sessionScope.userName}
                            </c:when>
                            <c:otherwise>Guest</c:otherwise>
                        </c:choose>
                    </span>
                </a>
            </li>
        </ul>
    </div>
</nav>