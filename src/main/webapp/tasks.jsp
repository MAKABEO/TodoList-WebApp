<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib prefix="template" tagdir="/WEB-INF/tags" %>
<%@ page isELIgnored="false" %>
<html>
<head>
    <link
          rel="stylesheet"
          href="https://cdn.jsdelivr.net/npm/@fontsource/source-sans-3@5.0.12/index.css"
          integrity="sha256-tXJfXfp6Ewt1ilPzLDtQnJV4hclT9XuaZUKyUvmyr+Q="
          crossorigin="anonymous"
        />
    <link
          rel="stylesheet"
          href="https://cdn.jsdelivr.net/npm/overlayscrollbars@2.10.1/styles/overlayscrollbars.min.css"
          integrity="sha256-tZHrRjVqNSRyWg2wbppGnT833E/Ys0DHWGwT04GiqQg="
          crossorigin="anonymous"
        />
    <link
          rel="stylesheet"
          href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.11.3/font/bootstrap-icons.min.css"
          integrity="sha256-9kPW/n5nn53j4WMRYAxe9c1rCY96Oogo/MKSVdKzPmI="
          crossorigin="anonymous"
        />
    <link
          rel="stylesheet"
          href="https://cdn.jsdelivr.net/npm/admin-lte@4.0.0-beta3/dist/css/adminlte.min.css"
          crossorigin="anonymous"
        />
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/flatpickr/dist/flatpickr.min.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/styles.css">
</head>
<body class="layout-fixed sidebar-expand-lg sidebar-mini sidebar-collapse bg-body-tertiary">

    <div class="app-wrapper">
         <template:navbar />
         <jsp:include page="/WEB-INF/fragments/sidebar.jspf"/>

         <% String errorMessage = (String) session.getAttribute("errorMessage");
            if (errorMessage != null) { %>
             <div class="alert alert-danger alert-dismissible fade show" role="alert">
                 <%= errorMessage %>
                 <button type="button" class="btn-close" data-bs-dismiss="alert" aria-label="Close"></button>
             </div>
         <% session.removeAttribute("errorMessage"); } %>

          <main class="app-main">
            <div class="app-content-header">
              <div class="container-fluid">
                <!--begin::Row-->
                <div class="row">
                  <div class="col-sm-6"><h3 class="mb-0">List of TODOS</h3></div>
                  <div class="col-sm-6">
                    <ol class="breadcrumb float-sm-end">
                      <li class="breadcrumb-item"><a href="<c:url value='/TodoList'/>">Home</a></li>
                      <li class="breadcrumb-item active" aria-current="page">TODO List</li>
                    </ol>
                  </div>
                </div>
              </div>
            </div>
            <!--begin::App Content-->
            <div class="app-content">
              <!--begin::Container-->
              <div class="container-fluid">
                <!--begin::Row-->
                <div class="row">
                  <div class="col-12">
                    <c:set var="tableHeaders" value="${['#', 'Title', 'Description', 'Status', 'Start Date', 'Target Date']}" />
                    <c:set var="fields" value="${['id', 'title', 'description', 'status', 'startDate', 'targetDate']}" />
                    <template:card title="Task List">
                        <template:table items="${tasks}" tableHeaders="${tableHeaders}" fields="${fields}" showActions="true">
                        </template:table>
                    </template:card>
                  </div>
                </div>
              </div>
            </div>
          </main>
          <jsp:include page="/WEB-INF/fragments/footer.jspf"/>
        </div>
    <template:modal id="taskModal" title="Task" action="TodoList">
        <template:input label="Title" name="title" />
        <template:input label="Description" name="description" type="textarea" rows="3" />

        <template:select label="Status" name="status" options="${statusOptions}" />

        <template:input label="Start Date" name="startDate" type="date" />
        <template:input label="Target Date" name="targetDate" type="date" />
    </template:modal>
    <template:toast message="Task successfully created!" type="success"/>

    <script src="https://code.jquery.com/jquery-3.6.0.min.js"></script>
    <script src="https://cdn.jsdelivr.net/npm/jquery-validation@1.19.5/dist/jquery.validate.min.js"></script>

    <script
          src="https://cdn.jsdelivr.net/npm/overlayscrollbars@2.10.1/browser/overlayscrollbars.browser.es6.min.js"
          integrity="sha256-dghWARbRe2eLlIJ56wNB+b760ywulqK3DzZYEpsg2fQ="
          crossorigin="anonymous"
        ></script>
    <script
          src="https://cdn.jsdelivr.net/npm/@popperjs/core@2.11.8/dist/umd/popper.min.js"
          integrity="sha384-I7E8VVD/ismYTF4hNIPjVp/Zjvgyol6VFvRkX/vR+Vc4jQkC+hVqc2pM8ODewa9r"
          crossorigin="anonymous"
        ></script>
    <script
          src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.min.js"
          integrity="sha384-0pUGZvbkm6XF6gxjEnlmuGrJXVbNuzT9qBBavbLwCsOGabYfZo0T0to5eqruptLy"
          crossorigin="anonymous"
        ></script>
    <script
      src="https://cdn.jsdelivr.net/npm/admin-lte@4.0.0-beta3/dist/js/adminlte.min.js"
      crossorigin="anonymous"
    ></script>
    <script src="https://cdn.jsdelivr.net/npm/flatpickr"></script>
    <script>
        const urls = {
            api: "<c:url value='/api/TodoList'/>",
            page: "<c:url value='/TodoList'/>",
        };
    </script>
    <script
        src="${pageContext.request.contextPath}/js/scripts.js"
    >
    </script>
</body>
</html>