<%-- 
    Document   : verProfesores
    Created on : 15 abr 2026, 16:41:30
    Author     : SGame
--%>

<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>Gestión de Profesores</title>
</head>
<body>

<%@ include file="../cabecera.jsp" %>

<div class="container">
    <div class="d-flex justify-content-between align-items-center mb-4">
        <h2><i class="bi bi-book"></i> Gestión de Profesores</h2>
        <%-- Botón para ir al formulario de nuevo curso --%>
        <a href="${pageContext.request.contextPath}/directiva/GestionProfesores?accion=nuevo"
           class="btn btn-primary">
            <i class="bi bi-plus-circle"></i> Nuevo Profesor
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

    <%-- Tabla de profesores --%>
    <div class="card shadow">
        <div class="card-body">
            <table class="table table-hover align-middle mb-0">
                <thead class="table-dark">
                    <tr>
                        <%-- <th>#</th> --%>
                        <th>Nombre</th>
                        <th>Email</th>
                        <th>Tipo</th>
                        <th class="text-center">Acciones</th>
                    </tr>
                </thead>
                <tbody>
                    <%-- c:choose es como un if/else en JSTL --%>
                    <c:choose>
                        <c:when test="${empty profesores}">
                            <tr>
                                <td colspan="6" class="text-center text-muted">
                                    No hay profesores registrados
                                </td>
                            </tr>
                        </c:when>
                        <c:otherwise>
                            <%-- c:forEach itera la lista de cursos --%>
                            <c:forEach var="profesor" items="${profesores}">
                                <tr>
                                    <%-- <td>${profesor.id}</td> --%>
                                    <td>${profesor.nombre} ${profesor.apellidos}</td>
                                    <td>
                                        <a href="mailto:${profesor.email}">${profesor.email}</a>
                                    </td>
                                    <td>
                                    <%-- Badge de color segun el tipo de profesor --%>
                                    <c:choose>
                                        <c:when test="${profesor.tipo == 'DIRECTIVA'}">
                                            <span class="badge bg-warning text-dark">
                                                Directiva
                                            </span>
                                        </c:when>
                                        <c:otherwise>
                                            <span class="badge bg-secondary">
                                                Normal
                                            </span>
                                        </c:otherwise>
                                    </c:choose>
                                    </td>
                                    <td class="text-center">
                                        <%-- Botón editar --%>
                                        <a href="${pageContext.request.contextPath}/directiva/GestionProfesores?accion=editar&id=${profesor.id}"
                                           class="btn btn-sm btn-warning me-1">
                                            <i class="bi bi-pencil"></i> Editar
                                        </a>
                                        <%-- Botón eliminar con confirmación --%>
                                        <%-- No mostramos el boton eliminar si es el profesor de la sesion actual--%>
                                        <c:if test="${profesor.id != sessionScope.profesor.id}">
                                            <a href="${pageContext.request.contextPath}/directiva/GestionProfesores?accion=eliminar&id=${profesor.id}"
                                               class="btn btn-sm btn-danger" 
                                               onclick="return confirm('¿Seguro que deseas eliminar al profesor ${profesor.nombre} ${profesor.apellidos}?')">
                                                <i class="bi bi-trash"></i> Eliminar
                                            </a>
                                        </c:if>
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
