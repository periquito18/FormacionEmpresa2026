<%-- 
    Document   : verAlumnos
    Created on : 15 abr 2026, 17:31:56
    Author     : SGame
--%>

<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>
<!DOCTYPE html>
<html lang="es">
    <head>
        <meta charset="UTF-8"> 
        <meta name="viewport" content="width=device-width, initial-scale=1">

        <%--
        fmt:setLocale establece el idioma para este JSP.
        Recoge el locale de la sesión, o español por defecto si no hay ninguno.
        fmt:setBundle carga el fichero de mensajes correspondiente al locale.
        basename apunta al paquete y nombre base de los ficheros .properties
        --%>

        <fmt:setLocale value="${not empty sessionScope.locale 
                                ? sessionScope.locale : 'es'}"/>
        <fmt:setBundle basename="bundle.mensajes" var="msg"/>

        <title>
            <fmt:message key="alumnosTitulo" bundle="${msg}"/>
        </title>
    </head>
    <body>

        <%@ include file="../cabecera.jsp" %>

        <div class="container">
            <div class="d-flex justify-content-between align-items-center mb-4">
                <h2><i class="bi bi-people"></i>
                    <fmt:message key="alumnosTitulo" bundle="${msg}"/>
                </h2>
                <a href="${pageContext.request.contextPath}/comun/GestionAlumnos?accion=nuevo"
                   class="btn btn-primary">
                    <i class="bi bi-plus-circle"></i>
                    <fmt:message key="nuevoAlumno" bundle="${msg}"/>
                </a>
            </div>

            <%-- Mensajes de éxito o error --%>
            <c:if test="${not empty exito}">
                <div class="alert alert-success alert-dismissible fade show">
                    <i class="bi bi-check-circle"></i> ${exito}
                    <button type="button" class="btn-close" data-bs-dismiss="alert"></button>
                </div>
            </c:if>
            <c:if test="${not empty error}">
                <div class="alert alert-danger alert-dismissible fade show">
                    <i class="bi bi-exclamation-triangle"></i> ${error}
                    <button type="button" class="btn-close" data-bs-dismiss="alert"></button>
                </div>
            </c:if>

            <%-- Errores detallados de importación CSV --%>
            <c:if test="${not empty erroresCSV}">
                <div class="alert alert-warning alert-dismissible fade show">
                    <strong><i class="bi bi-exclamation-triangle"></i> 
                        <fmt:message key="alumnosErrorImportacion" bundle="${msg}"/>
                    </strong>
                    <ul class="mb-0 mt-2">
                        <c:forEach var="err" items="${erroresCSV}">
                            <li>${err}</li>
                            </c:forEach>
                    </ul>
                    <button type="button" class="btn-close" data-bs-dismiss="alert"></button>
                </div>
            </c:if>

            <div class="row g-4">

                <%-- Panel izquierdo: filtro por curso e importación CSV --%>
                <div class="col-md-3">

                    <%-- Filtro por curso --%>
                    <div class="card shadow mb-3">
                        <div class="card-header bg-secondary text-white">
                            <i class="bi bi-funnel"></i> 
                            <fmt:message key="alumnosFiltro" bundle="${msg}"/>
                        </div>
                        <div class="card-body">
                            <div class="list-group list-group-flush">
                                <%-- Opción "Todos" --%>
                                <a href="${pageContext.request.contextPath}/comun/GestionAlumnos?accion=listar"
                                   class="list-group-item list-group-item-action
                                   ${empty cursoSeleccionado ? 'active' : ''}">
                                    <fmt:message key="alumnosLista" bundle="${msg}"/>
                                </a>
                                <%-- Un enlace por cada curso --%>
                                <c:forEach var="curso" items="${cursos}">
                                    <a href="${pageContext.request.contextPath}/comun/GestionAlumnos?accion=listar&cursoId=${curso.id}"
                                       class="list-group-item list-group-item-action
                                       ${cursoSeleccionado.id == curso.id ? 'active' : ''}">
                                        ${curso.nombre}
                                        <span class="badge bg-primary float-end">
                                            ${curso.alumnos.size()}
                                        </span>
                                    </a>
                                </c:forEach>
                            </div>
                        </div>
                    </div>

                    <%-- Importación CSV --%>
                    <div class="card shadow">
                        <div class="card-header bg-success text-white">
                            <i class="bi bi-upload"></i> 
                            <fmt:message key="alumnosTituloCSV" bundle="${msg}"/>
                        </div>
                        <div class="card-body">
                            <%--
                                enctype="multipart/form-data" es OBLIGATORIO
                                para poder subir ficheros en un formulario HTML.
                                Sin esto el servidor no recibe el fichero.
                            --%>
                            <form action="${pageContext.request.contextPath}/comun/GestionAlumnos"
                                  method="post" enctype="multipart/form-data">

                                <%-- Campo oculto para identificar la acción --%>
                                <input type="hidden" name="accion" value="importar">

                                <div class="mb-2">
                                    <label class="form-label small">
                                        <fmt:message key="alumnosCursoCSV" bundle="${msg}"/>
                                    </label>
                                    <select class="form-select form-select-sm" name="cursoId" required>
                                        <option value="">
                                            <fmt:message key="alumnosSeleccionarCursoCSV" bundle="${msg}"/>
                                        </option>
                                        <c:forEach var="curso" items="${cursos}">
                                            <option value="${curso.id}">${curso.nombre}</option>
                                        </c:forEach>
                                    </select>
                                </div>

                                <div class="mb-2">
                                    <label class="form-label small">
                                        <fmt:message key="alumnosFicheroCSV" bundle="${msg}"/>
                                    </label>
                                    <input type="file" class="form-control form-control-sm"
                                           name="ficheroCSV" accept=".csv" required>
                                </div>

                                <div class="mb-2">
                                    <small class="text-muted">
                                        <fmt:message key="alumnosFormatoCSV" bundle="${msg}"/>
                                    </small>
                                </div>

                                <button type="submit" class="btn btn-success btn-sm w-100">
                                    <i class="bi bi-upload"></i>
                                    <fmt:message key="alumnosImportarCSV" bundle="${msg}"/>
                                </button>
                            </form>
                        </div>
                    </div>

                </div>

                <%-- Panel derecho: tabla de alumnos --%>
                <div class="col-md-9">
                    <div class="card shadow">
                        <div class="card-header">
                            <c:choose>
                                <c:when test="${not empty cursoSeleccionado}">
                                    <fmt:message key="alumnosCursosFormulario" bundle="${msg}"/> <strong>${cursoSeleccionado.nombre}</strong>
                                </c:when>
                                <c:otherwise>
                                    <fmt:message key="alumnosLista" bundle="${msg}"/>
                                </c:otherwise>
                            </c:choose>
                            <span class="badge bg-primary ms-2">
                                ${alumnos.size()} <fmt:message key="alumnos" bundle="${msg}"/>
                            </span>
                        </div>
                        <div class="card-body">
                            <table class="table table-hover align-middle mb-0">
                                <thead class="table-dark">
                                    <tr>
                                        <%-- <th>#</th> --%>
                                        <th>
                                            <fmt:message key="alumnosColumnaNombre" bundle="${msg}"/>
                                        </th>
                                        <th>
                                            <fmt:message key="alumnosColumnaEmail" bundle="${msg}"/>
                                        </th>
                                        <th>
                                            <fmt:message key="alumnosColumnaCurso" bundle="${msg}"/>
                                        </th>
                                        <th>
                                            <fmt:message key="alumnosColumnaFechaNacimiento" bundle="${msg}"/>
                                        </th>
                                        <th class="text-center">
                                            <fmt:message key="alumnosColumnaAcciones" bundle="${msg}"/>
                                        </th>
                                    </tr>
                                </thead>
                                <tbody>
                                    <c:choose>
                                        <c:when test="${empty alumnos}">
                                            <tr>
                                                <td colspan="6" class="text-center text-muted p-4">
                                                    <fmt:message key="alumnosSinRegistros" bundle="${msg}"/>
                                                </td>
                                            </tr>
                                        </c:when>
                                        <c:otherwise>
                                            <c:forEach var="alumno" items="${alumnos}">
                                                <%-- Guardamos el mensaje traducido en una variable Java --%>
                                                <fmt:message key="alumnosConfirmacionEliminar" 
                                                             bundle="${msg}" var="msgConfirmar"/>
                                                <tr>
                                                    <%-- <td>${alumno.id}</td> --%>
                                                    <td>${alumno.nombre} ${alumno.apellidos}</td>
                                                    <td>
                                                        <a href="mailto:${alumno.email}">
                                                            ${alumno.email}
                                                        </a>
                                                    </td>
                                                    <td>
                                                        <span class="badge bg-secondary">
                                                            ${alumno.curso.nombre}
                                                        </span>
                                                    </td>
                                                    <td>
                                                        <%--
                                                            fmt:parseDate convierte el String
                                                            de LocalDate a un objeto Date
                                                            que fmt:formatDate puede formatear.
                                                            pattern dd/MM/yyyy es el formato
                                                            europeo estándar.
                                                        --%>
                                                        <fmt:parseDate value="${alumno.fechaNacimiento}"
                                                                       pattern="yyyy-MM-dd"
                                                                       var="fechaParseada"/>
                                                        <fmt:formatDate value="${fechaParseada}"
                                                                        pattern="dd/MM/yyyy"/>
                                                    </td>
                                                    <td class="text-center">
                                                        <a href="${pageContext.request.contextPath}/comun/GestionAlumnos?accion=editar&id=${alumno.id}"
                                                           class="btn btn-sm btn-warning me-1">
                                                            <i class="bi bi-pencil"></i>
                                                            <fmt:message key="editarAlumno" bundle="${msg}"/>
                                                        </a>
                                                        <a href="${pageContext.request.contextPath}/comun/GestionAlumnos?accion=eliminar&id=${alumno.id}"
                                                           class="btn btn-sm btn-danger"
                                                           onclick="return confirm('${msgConfirmar} ${alumno.nombre} ${alumno.apellidos}?')">
                                                            <i class="bi bi-trash"></i>
                                                            <fmt:message key="eliminarAlumno" bundle="${msg}"/>
                                                        </a>
                                                    </td>
                                                </tr>
                                            </c:forEach>
                                        </c:otherwise>
                                    </c:choose>
                                </tbody>
                            </table>
                        </div>
                    </div>
                </div>
            </div>
            <%-- Botón para volver a Inicio --%>
            <div class="d-flex justify-content-start align-items-center mt-4">
                <a href="${pageContext.request.contextPath}/Inicio"
                   class="btn btn-primary">
                    <i class="bi bi-arrow-left-circle"></i>
                    <fmt:message key="regresar" bundle="${msg}"/>
                </a>
            </div>
        </div>
    </body>
</html>
