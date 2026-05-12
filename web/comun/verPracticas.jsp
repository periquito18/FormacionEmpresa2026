<%-- 
    Document   : verPracticas
    Created on : 15 abr 2026, 17:33:04
    Author     : SGame
--%>

<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html lang="es">
    <head>
        <meta charset="UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1">
        <title>Gestión de Prácticas</title>
    </head>
    <body>

        <%@ include file="../cabecera.jsp" %>

        <div class="container">
            <div class="d-flex justify-content-between align-items-center mb-4">
                <h2><i class="bi bi-journal-check"></i> Gestión de Prácticas</h2>
                <a href="${pageContext.request.contextPath}/comun/GestionPracticas?accion=nuevo"
                   class="btn btn-primary">
                    <i class="bi bi-plus-circle"></i> Nueva Práctica
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

            <%-- Tabla de practicas --%>
            <div class="card shadow">
                <div class="card-body">
                    <table class="table table-hover align-middle mb-0">
                        <thead class="table-dark">
                            <tr>
                                <th>#</th>
                                <th>Alumno</th>
                                <th>Curso</th>
                                <th>Empresa</th>
                                <th>Fecha Inicio</th>
                                <th>Fecha Fin</th>
                                <th>Comentarios</th>
                                <th class="text-center">Acciones</th>
                            </tr>
                        </thead>
                        <tbody>
                            <c:choose>
                                <c:when test="${empty practicas}">
                                    <tr>
                                        <td colspan="8" class="text-center text-muted p-4">
                                            No hay prácticas registradas
                                        </td>
                                    </tr>
                                </c:when>
                                <c:otherwise>
                                    <c:forEach var="practica" items="${practicas}">
                                        <tr>
                                            <td>${practica.id}</td>
                                            <td>
                                                <strong>
                                                    ${practica.alumno.nombre} 
                                                    ${practica.alumno.apellidos}
                                                </strong>
                                                <br>
                                                <small class="text-muted">
                                                    ${practica.alumno.email}
                                                </small>
                                            </td>
                                            <td>
                                                <span class="badge bg-secondary">
                                                    ${practica.alumno.curso.nombre}
                                                </span>
                                            </td>
                                            <td>
                                                <strong>${practica.empresa.nombre}</strong>
                                                <br>
                                                <small class="text-muted">
                                                    ${practica.empresa.tutorNombre}
                                                </small>
                                            </td>
                                            <td>${practica.fechaInicio}</td>
                                            <td>${practica.fechaFin}</td>
                                            <td>
                                                <c:choose>
                                                    <c:when test="${not empty practica.comentarios}">
                                                        <%-- Truncamos comentarios largos --%>
                                                        <span title="${practica.comentarios}">
                                                            <c:choose>
                                                                <c:when test="${practica.comentarios.length() > 40}">
                                                                    ${practica.comentarios.substring(0, 40)}...
                                                                </c:when>
                                                                <c:otherwise>
                                                                    ${practica.comentarios}
                                                                </c:otherwise>
                                                            </c:choose>
                                                        </span>
                                                    </c:when>
                                                    <c:otherwise>
                                                        <span class="text-muted">Sin comentarios</span>
                                                    </c:otherwise>
                                                </c:choose>
                                            </td>
                                            <td class="text-center">
                                                <a href="${pageContext.request.contextPath}/comun/GestionPracticas?accion=editar&id=${practica.id}"
                                                   class="btn btn-sm btn-warning me-1">
                                                    <i class="bi bi-pencil"></i> Editar
                                                </a>
                                                <a href="${pageContext.request.contextPath}/comun/GestionPracticas?accion=eliminar&id=${practica.id}"
                                                   class="btn btn-sm btn-danger"
                                                   onclick="return confirm('¿Seguro que deseas eliminar esta práctica?')">
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
