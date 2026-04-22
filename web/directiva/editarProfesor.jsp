<%-- 
    Document   : editarProfesor
    Created on : 15 abr 2026, 16:41:41
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
                <c:when test="${accion == 'nuevo'}">Nuevo Profesor</c:when>
                <c:otherwise>Editar Profesor</c:otherwise>
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
                                        <i class="bi bi-plus-circle"></i> Nuevo Profesor
                                    </c:when>
                                    <c:otherwise>
                                        <i class="bi bi-pencil"></i> Editar Profesor
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
                            <form action="${pageContext.request.contextPath}/directiva/GestionProfesores" 
                                  method="post">

                                <%-- Campo oculto con el ID para distinguir crear de editar --%>
                                <input type="hidden" name="id" value="${profesor.id == 0 ? '' : profesor.id}">

                                <div class="row">
                                    <div class="col-md-6 mb-3">
                                        <label for="nombre" class="form-label">
                                            Nombre <span class="text-danger">*</span>
                                        </label>
                                        <input type="text" class="form-control" id="nombre" 
                                               name="nombre" value="${profesor.nombre}" required>
                                    </div>

                                    <div class="col-md-6 mb-3">
                                        <label for="apellidos" class="form-label">
                                            Apellidos <span class="text-danger">*</span>
                                        </label>
                                        <input type="text" class="form-control" id="apellidos" 
                                               name="apellidos" value="${profesor.apellidos}" required>
                                    </div>

                                    <div class="col-md-6 mb-3">
                                        <label for="email" class="form-label">
                                            Email <span class="text-danger">*</span>
                                        </label>
                                        <input type="text" class="form-control" id="email" 
                                               name="email" value="${profesor.email}" required>
                                    </div>

                                    <div class="col-md-6 mb-3">
                                        <label for="password" class="form-label">
                                            Password
                                            <%-- Avisamos de que es opcional --%>
                                            <c:if test="${accion = 'editar'}">
                                                <small class="text-muted">
                                                    (deja la password vacia para no cambiarla)
                                                </small>
                                            </c:if>
                                            <c:if test="${accion = 'nuevo'}">
                                                <span class="text-danger">*</span>
                                            </c:if>
                                        </label>
                                        <input type="password" class="form-control" id="password" 
                                               name="password" placeholder="${accion = 'nuevo' ? 'Password' : 'Nueva password (opcional)'}">
                                    </div>
                                </div>

                                <div class="mb-4">
                                    <label for="tipo" class="form-label">
                                        Tipo <span class="text-danger">*</span>
                                    </label>
                                    <%-- 
                                        selected: seleccionamos la opcion actual del profesor
                                        En JSP usamos c:if para añadir el atributo selected
                                    --%>
                                    <select class="form-select" id="tipo" name="tipo">
                                        <option value="NORMAL"
                                            <c:if test="${profesor.tipo == 'NORMAL'}">selected</c:if>>
                                            Normal
                                        </option>
                                        <option value="DIRECTIVA"
                                            <c:if test="${profesor.tipo == 'DIRECTIVA'}">selected</c:if>>
                                            Directiva
                                        </option>
                                    </select>
                                </div>

                                <div class="d-flex gap-2">
                                    <button type="submit" class="btn btn-primary">
                                        <i class="bi bi-save"></i> Guardar
                                    </button>
                                    <%-- Botón cancelar vuelve a la lista --%>
                                    <a href="${pageContext.request.contextPath}/directiva/GestionProfesores?accion=listar"
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
