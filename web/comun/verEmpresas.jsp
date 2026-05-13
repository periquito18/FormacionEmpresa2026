<%-- 
    Document   : verEmpresas
    Created on : 15 abr 2026, 12:45:15
    Author     : SGame
--%>

<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html lang="es">
    <head>
        <meta charset="UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1">
        <title>Gestión de Empresas</title>
    </head>
    <body>
        <%@ include file="../cabecera.jsp" %>
        <div class="container">
            <div class="d-flex justify-content-between align-items-center mb-4">
                <h2><i class="bi bi-briefcase"></i> Gestión de Empresas</h2>
                <%-- Botón para ir al formulario de nuevo curso --%>
                <a href="${pageContext.request.contextPath}/comun/GestionEmpresas?accion=nuevo" 
                   class="btn btn-primary">
                    <i class="bi bi-plus-circle"></i> Nueva Empresa
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

            <%-- Tabla de empresas --%>
            <div class="card show">
                <div class="card-body">
                    <table class="table table-hover align-middle mb-0">
                        <thead class="table-dark">
                            <tr>
                                <%-- <th>#</th> --%>
                                <th>Nombre</th>
                                <th>Tutor</th>
                                <th>Practicas</th>
                                <th class="text-center">Acciones</th>
                            </tr>
                        </thead>
                        <tbody>
                            <c:choose>
                                <c:when test="${empty empresas}">
                                    <tr>
                                        <td colspan="6" class="text-center text-muted">
                                            No hay empresas registradas
                                        </td>
                                    </tr>
                                </c:when>
                                <c:otherwise>
                                    <c:forEach var="empresa" items="${empresas}">
                                        <tr>
                                            <%-- <td>${empresa.id}</td> --%>
                                            <td>
                                                <strong>${empresa.nombre}</strong>
                                                <c:if test="${not empty empresa.descripcion}">
                                                    <br>
                                                    <small class="text-muted">${empresa.descripcion}</small>
                                                </c:if>
                                            </td>
                                            <td>
                                                <strong>${empresa.tutorNombre}</strong>
                                                <small class="text-muted">
                                                    <a href="mailto:${empresa.tutorEmail}">
                                                        ${empresa.tutorEmail}
                                                    </a>
                                                </small>
                                            </td>
                                            <td>
                                                <span class="badge bg-info text-dark">
                                                    ${empresa.practicas.size()}
                                                </span>
                                            </td>
                                            <td class="text-center">
                                                <a href="${pageContext.request.contextPath}/comun/GestionEmpresas?accion=editar&id=${empresa.id}" 
                                                   class="btn btn-sm btn-warning me-1">
                                                    <i class="bi bi-pencil"></i> Editar
                                                </a>
                                                <a href="${pageContext.request.contextPath}/comun/GestionEmpresas?accion=eliminar&id=${empresa.id}" 
                                                   class="btn btn-sm btn-danger" 
                                                   onclick="return confirm('¿Seguro que deseas eliminar la empresa ${empresa.nombre}?')">
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