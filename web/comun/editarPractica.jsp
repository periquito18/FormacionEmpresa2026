<%-- 
    Document   : editarPractica
    Created on : 15 abr 2026, 17:33:28
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
            <div class="row justify-content-center">
                <div class="col-md-8">
                    <div class="card shadow">
                        <div class="card-header bg-primary text-white">
                            <h4 class="mb-0">
                                <c:choose>
                                    <c:when test="${accion == 'nuevo'}">
                                        <i class="bi bi-plus-circle"></i> Nueva Práctica
                                    </c:when>
                                    <c:otherwise>
                                        <i class="bi bi-pencil"></i> Editar Práctica
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

                            <form action="${pageContext.request.contextPath}/comun/GestionPracticas"
                                  method="post">

                                <input type="hidden" name="id"
                                       value="${practica.id == 0 ? '' : practica.id}">

                                <%-- Selector de alumno: solo en modo nuevo --%>
                                <c:choose>
                                    <c:when test="${accion == 'nuevo'}">
                                        <div class="mb-3">
                                            <label for="alumnoId" class="form-label">
                                                Alumno <span class="text-danger">*</span>
                                            </label>
                                            <select class="form-select" id="alumnoId"
                                                    name="alumnoId" required>
                                                <option value="">Selecciona un alumno...</option>
                                                <c:forEach var="alumno" items="${alumnosSinPractica}">
                                                    <option value="${alumno.id}">
                                                        ${alumno.nombre} ${alumno.apellidos}
                                                        (${alumno.curso.nombre})
                                                    </option>
                                                </c:forEach>
                                            </select>
                                            <div class="form-text">
                                                Solo se muestran alumnos sin práctica asignada.
                                            </div>
                                        </div>
                                    </c:when>
                                    <c:otherwise>
                                        <%-- En edición mostramos el alumno pero no lo dejamos cambiar --%>
                                        <div class="mb-3">
                                            <label class="form-label">Alumno</label>
                                            <input type="text" class="form-control" 
                                                   value="${practica.alumno.nombre} ${practica.alumno.apellidos} (${practica.alumno.curso.nombre})"
                                                   disabled>
                                        </div>
                                    </c:otherwise>
                                </c:choose>

                                <%-- Selector de empresa --%>
                                <div class="mb-3">
                                    <label for="empresaId" class="form-label">
                                        Empresa <span class="text-danger">*</span>
                                    </label>
                                    <select class="form-select" id="empresaId"
                                            name="empresaId" required>
                                        <option value="">Selecciona una empresa...</option>
                                        <c:forEach var="empresa" items="${empresas}">
                                            <option value="${empresa.id}"
                                                    <c:if test="${practica.empresa.id == empresa.id}">
                                                        selected
                                                    </c:if>>
                                                ${empresa.nombre}
                                            </option>
                                        </c:forEach>
                                    </select>
                                </div>

                                <div class="row">
                                    <div class="col-md-6 mb-3">
                                        <label for="fechaInicio" class="form-label">
                                            Fecha de inicio <span class="text-danger">*</span>
                                        </label>
                                        <input type="date" class="form-control" 
                                               id="fechaInicio" name="fechaInicio" 
                                               value="${practica.fechaInicio}" required>
                                    </div>
                                    <div class="col-md-6 mb-3">
                                        <label for="fechaFin" class="form-label">
                                            Fecha de fin <span class="text-danger">*</span>
                                        </label>
                                        <input type="date" class="form-control"
                                               id="fechaFin" name="fechaFin"
                                               value="${practica.fechaFin}" required>
                                    </div>
                                </div>

                                <div class="mb-4">
                                    <label for="comentarios" class="form-label">
                                        Comentarios
                                    </label>
                                    <textarea class="form-control" id="comentarios" name="comentarios" 
                                              rows="4" placeholder="Observaciones sobre la práctica...">
                                        ${practica.comentarios}
                                    </textarea>
                                </div>

                                <c:if test="${accion == 'nuevo'}">
                                    <div class="alert alert-info">
                                        <i class="bi bi-envelope"></i>
                                        Al crear la práctica se enviará automáticamente
                                        un email de notificación al alumno.
                                    </div>
                                </c:if>

                                <div class="d-flex gap-2">
                                    <button type="submit" class="btn btn-primary">
                                        <i class="bi bi-save"></i> Guardar
                                    </button>
                                    <a href="${pageContext.request.contextPath}/comun/GestionPracticas?accion=listar"
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
