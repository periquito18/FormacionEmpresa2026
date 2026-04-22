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
        <title>Gestión de Alumnos</title>
    </head>
    <body>

        <%@ include file="../cabecera.jsp" %>

        <div class="container">
            <div class="d-flex justify-content-between align-items-center mb-4">
                <h2><i class="bi bi-people"></i> Gestión de Alumnos</h2>
                <a href="${pageContext.request.contextPath}/comun/GestionAlumnos?accion=nuevo"
                   class="btn btn-primary">
                    <i class="bi bi-plus-circle"></i> Nuevo Alumno
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
                        Errores durante la importación:
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
                            <i class="bi bi-funnel"></i> Filtrar por curso
                        </div>
                        <div class="card-body">
                            <div class="list-group list-group-flush">
                                <%-- Opción "Todos" --%>
                                <a href="${pageContext.request.contextPath}/comun/GestionAlumnos?accion=listar"
                                   class="list-group-item list-group-item-action
                                   ${empty cursoSeleccionado ? 'active' : ''}">
                                    Todos los alumnos
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
                            <i class="bi bi-upload"></i> Importar CSV
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
                                    <label class="form-label small">Curso destino</label>
                                    <select class="form-select form-select-sm" name="cursoId" required>
                                        <option value="">Selecciona curso...</option>
                                        <c:forEach var="curso" items="${cursos}">
                                            <option value="${curso.id}">${curso.nombre}</option>
                                        </c:forEach>
                                    </select>
                                </div>

                                <div class="mb-2">
                                    <label class="form-label small">Fichero CSV</label>
                                    <input type="file" class="form-control form-control-sm"
                                           name="ficheroCSV" accept=".csv" required>
                                </div>

                                <div class="mb-2">
                                    <small class="text-muted">
                                        Formato: nombre, apellidos, email, fecha (yyyy-MM-dd)
                                    </small>
                                </div>

                                <button type="submit" class="btn btn-success btn-sm w-100">
                                    <i class="bi bi-upload"></i> Importar
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
                                    Alumnos de <strong>${cursoSeleccionado.nombre}</strong>
                                </c:when>
                                <c:otherwise>
                                    Todos los alumnos
                                </c:otherwise>
                            </c:choose>
                            <span class="badge bg-primary ms-2">
                                ${alumnos.size()} alumnos
                            </span>
                        </div>
                        <div class="card-body p-0">
                            <table class="table table-hover align-middle mb-0">
                                <thead class="table-dark">
                                    <tr>
                                        <th>#</th>
                                        <th>Nombre</th>
                                        <th>Email</th>
                                        <th>Curso</th>
                                        <th>Fecha Nacimiento</th>
                                        <th class="text-center">Acciones</th>
                                    </tr>
                                </thead>
                                <tbody>
                                    <c:choose>
                                        <c:when test="${empty alumnos}">
                                            <tr>
                                                <td colspan="6" class="text-center text-muted p-4">
                                                    No hay alumnos en este curso
                                                </td>
                                            </tr>
                                        </c:when>
                                        <c:otherwise>
                                            <c:forEach var="alumno" items="${alumnos}">
                                                <tr>
                                                    <td>${alumno.id}</td>
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
                                                            fmt:formatDate formatea fechas.
                                                            pattern="dd/MM/yyyy" es el formato
                                                            europeo que usamos en España.
                                                            value necesita un objeto Date,
                                                            pero nosotros tenemos LocalDate,
                                                            por eso usamos toString() y
                                                            lo mostramos directamente.
                                                        --%>
                                                        ${alumno.fechaNacimiento}
                                                    </td>
                                                    <td class="text-center">
                                                        <a href="${pageContext.request.contextPath}/comun/GestionAlumnos?accion=editar&id=${alumno.id}"
                                                           class="btn btn-sm btn-warning me-1">
                                                            <i class="bi bi-pencil"></i> Editar
                                                        </a>
                                                        <a href="${pageContext.request.contextPath}/comun/GestionAlumnos?accion=eliminar&id=${alumno.id}"
                                                           class="btn btn-sm btn-danger"
                                                           onclick="return confirm('¿Seguro que deseas eliminar a ${alumno.nombre} ${alumno.apellidos}?')">
                                                            <i class="bi bi-trash"></i> Eliminar
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
                    <i class="bi bi-arrow-left-circle"></i> Regresar
                </a>
            </div>
        </div>
    </body>
</html>
