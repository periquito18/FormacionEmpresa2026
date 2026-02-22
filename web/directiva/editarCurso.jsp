<%-- 
    Document   : editarCurso
    Created on : 22 feb 2026, 11:53:36
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
            <c:when test="${accion == 'nuevo'}">Nuevo Curso</c:when>
            <c:otherwise>Editar Curso</c:otherwise>
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
                                <i class="bi bi-plus-circle"></i> Nuevo Curso
                            </c:when>
                            <c:otherwise>
                                <i class="bi bi-pencil"></i> Editar Curso
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
                    <form action="${pageContext.request.contextPath}/directiva/GestionCursos" 
                          method="post">

                        <%-- Campo oculto con el ID para distinguir crear de editar --%>
                        <input type="hidden" name="id" value="${curso.id == 0 ? '' : curso.id}">

                        <div class="mb-3">
                            <label for="nombre" class="form-label">
                                Nombre del curso <span class="text-danger">*</span>
                            </label>
                            <input type="text" class="form-control" id="nombre" 
                                   name="nombre" value="${curso.nombre}"
                                   placeholder="Ej: 1 DAW" required>
                        </div>

                        <div class="d-flex gap-2">
                            <button type="submit" class="btn btn-primary">
                                <i class="bi bi-save"></i> Guardar
                            </button>
                            <%-- Botón cancelar vuelve a la lista --%>
                            <a href="${pageContext.request.contextPath}/directiva/GestionCursos?accion=listar"
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