<%-- 
    Document   : editarEmpresa
    Created on : 15 abr 2026, 12:45:34
    Author     : SGame
--%>

<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <%-- Título dinámico según si es nuevo o edición --%>
    <title>
        <c:choose>
            <c:when test="${accion == 'nuevo'}">Nueva Empresa</c:when>
            <c:otherwise>Editar Empresa</c:otherwise>
        </c:choose>
    </title>
</head>
<body>

<%@ include file="../cabecera.jsp" %>

<div class="container">
    <div class="row justify-content-center">
        <div class="col-md-6">
            <div class="card shadow">
                <div class="card-header bg-primary text-white">
                    <h4 class="mb-0">
                        <c:choose>
                            <c:when test="${accion == 'nuevo'}">
                                <i class="bi bi-plus-circle"></i> Nueva Empresa
                            </c:when>
                            <c:otherwise>
                                <i class="bi bi-pencil"></i> Editar Empresa
                            </c:otherwise>
                        </c:choose>
                    </h4>
                </div>
                <div class="card-body">

                    <c:if test="${not empty error}">
                        <div class="alert alert-danger">
                            <i class="bi bi-exclamation-triangle"></i> ${error}
                        </div>
                    </c:if>

                    <%--
                        El formulario siempre hace POST al mismo Servlet.
                        Si el curso tiene ID (edición) lo enviamos como campo oculto.
                        Si no tiene ID (nuevo) el campo id viene vacío.
                    --%>
                    <form action="${pageContext.request.contextPath}/comun/GestionEmpresas" 
                          method="post">

                        <%-- Campo oculto con el ID para distinguir crear de editar --%>
                        <input type="hidden" name="id" value="${empresa.id == 0 ? '' : empresa.id}">

                        <div class="mb-3">
                            <label for="nombre" class="form-label">
                                Nombre <span class="text-danger">*</span>
                            </label>
                            <input type="text" class="form-control" id="nombre" 
                                   name="nombre" value="${empresa.nombre}" required>
                        </div>
                        
                        <div class="mb-3">
                            <label for="descripcion" class="form-label">Descripcion</label>
                            <textarea class="form-control" id="descripcion" name="descripcion" 
                                      rows="3">${empresa.descripcion}</textarea>
                        </div>
                        
                        <div class="mb-3">
                            <label for="tutorNombre" class="form-label">
                                Nombre del tutor laboral <span class="text-danger">*</span>
                            </label>
                            <input type="text" class="form-control" id="tutorNombre" 
                                   name="tutorNombre" value="${empresa.tutorNombre}" required>
                        </div>
                        
                        <div class="mb-3">
                            <label for="tutorEmail" class="form-label">
                                Email del tutor laboral <span class="text-danger">*</span>
                            </label>
                            <input type="email" class="form-control" id="tutorEmail" 
                                   name="tutorEmail" value="${empresa.tutorEmail}" required>
                        </div>

                        <div class="d-flex gap-2">
                            <button type="submit" class="btn btn-primary">
                                <i class="bi bi-save"></i> Guardar
                            </button>
                            <%-- Botón cancelar vuelve a la lista --%>
                            <a href="${pageContext.request.contextPath}/comun/GestionEmpresas?accion=listar"
                               class="btn btn-secondary">
                                <i class="bi bi-x-circle"></i> Cancelar
                            </a>
                        </div>
                    </form>
                </div>
            </div>
        </div>
    </div>
</div>
</body>
</html>