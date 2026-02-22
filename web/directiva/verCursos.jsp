<%-- 
    Document   : verCursos
    Created on : 22 feb 2026, 11:48:18
    Author     : SGame
--%>

<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>Gestión de Cursos</title>
</head>
<body>

<%@ include file="../cabecera.jsp" %>

<div class="container">
    <div class="d-flex justify-content-between align-items-center mb-4">
        <h2><i class="bi bi-book"></i> Gestión de Cursos</h2>
        <%-- Botón para ir al formulario de nuevo curso --%>
        <a href="${pageContext.request.contextPath}/directiva/GestionCursos?accion=nuevo"
           class="btn btn-primary">
            <i class="bi bi-plus-circle"></i> Nuevo Curso
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

    <%-- Tabla de cursos --%>
    <div class="card shadow">
        <div class="card-body">
            <table class="table table-hover align-middle mb-0">
                <thead class="table-dark">
                    <tr>
                        <th>#</th>
                        <th>Nombre</th>
                        <th>Nº Alumnos</th>
                        <th class="text-center">Acciones</th>
                    </tr>
                </thead>
                <tbody>
                    <%-- c:choose es como un if/else en JSTL --%>
                    <c:choose>
                        <c:when test="${empty cursos}">
                            <tr>
                                <td colspan="4" class="text-center text-muted">
                                    No hay cursos registrados
                                </td>
                            </tr>
                        </c:when>
                        <c:otherwise>
                            <%-- c:forEach itera la lista de cursos --%>
                            <c:forEach var="curso" items="${cursos}">
                                <tr>
                                    <td>${curso.id}</td>
                                    <td>${curso.nombre}</td>
                                    <%-- Número de alumnos del curso --%>
                                    <td>
                                        <span class="badge bg-primary">
                                            ${curso.alumnos.size()} alumnos
                                        </span>
                                    </td>
                                    <td class="text-center">
                                        <%-- Botón editar --%>
                                        <a href="${pageContext.request.contextPath}/directiva/GestionCursos?accion=editar&id=${curso.id}"
                                           class="btn btn-sm btn-warning me-1">
                                            <i class="bi bi-pencil"></i> Editar
                                        </a>
                                        <%-- Botón eliminar con confirmación --%>
                                        <a href="${pageContext.request.contextPath}/directiva/GestionCursos?accion=eliminar&id=${curso.id}"
                                           class="btn btn-sm btn-danger"
                                           onclick="return confirm('¿Seguro que deseas eliminar el curso ${curso.nombre}?')">
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