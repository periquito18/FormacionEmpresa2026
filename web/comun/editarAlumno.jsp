<%-- 
    Document   : editarAlumno
    Created on : 15 abr 2026, 17:32:21
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
        
        <%-- Título dinámico según si es nuevo o edición --%>
        <title>
            <c:choose>
                <c:when test="${accion == 'nuevo'}">
                    <fmt:message key="alumnoNuevoTituloFormulario" bundle="${msg}"/>
                </c:when>
                <c:otherwise>
                    <fmt:message key="alumnoActualizadoTituloFormulario" bundle="${msg}"/>
                </c:otherwise>
            </c:choose>
        </title>
    </head>
    <body>
        <%@ include file="../cabecera.jsp" %>
        
        <div class="container">
            <div class="row justify-content-center">
                <div class="col-md-7">
                    <div class="card shadow">
                        <div class="card-header bg-primary text-white">
                            <h4 class="mb-0">
                                <c:choose>
                                    <c:when test="${accion == 'nuevo'}">
                                        <i class="bi bi-plus-circle"></i>
                                        <fmt:message key="alumnoNuevoTituloFormulario" bundle="${msg}"/>
                                    </c:when>
                                    <c:otherwise>
                                        <i class="bi bi-pencil"></i>
                                        <fmt:message key="alumnoActualizadoTituloFormulario" bundle="${msg}"/>
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

                            <form action="${pageContext.request.contextPath}/comun/GestionAlumnos"
                                  method="post">

                                <input type="hidden" name="id"
                                       value="${alumno.id == 0 ? '' : alumno.id}">

                                <div class="row">
                                    <div class="col-md-6 mb-3">
                                        <label for="nombre" class="form-label">
                                            <fmt:message key="alumnosCampoNombreFormulario" bundle="${msg}"/>
                                            <span class="text-danger">*</span>
                                        </label>
                                        <input type="text" class="form-control" id="nombre"
                                               name="nombre" value="${alumno.nombre}" required>
                                    </div>
                                    <div class="col-md-6 mb-3">
                                        <label for="apellidos" class="form-label">
                                            <fmt:message key="alumnosCampoApellidosFormulario" bundle="${msg}"/> 
                                            <span class="text-danger">*</span>
                                        </label>
                                        <input type="text" class="form-control" id="apellidos"
                                               name="apellidos" value="${alumno.apellidos}" required>
                                    </div>
                                </div>

                                <div class="mb-3">
                                    <label for="email" class="form-label">
                                        <fmt:message key="alumnosCampoEmailFormulario" bundle="${msg}"/> 
                                        <span class="text-danger">*</span>
                                    </label>
                                    <input type="email" class="form-control" id="email"
                                           name="email" value="${alumno.email}" required>
                                </div>

                                <div class="mb-3">
                                    <label for="fechaNacimiento" class="form-label">
                                        <fmt:message key="alumnosCampoFechaNacimientoFormulario" bundle="${msg}"/> 
                                        <span class="text-danger">*</span>
                                    </label>
                                    <%--
                                        input type="date" muestra un selector de fecha nativo.
                                        El valor debe estar en formato yyyy-MM-dd para que
                                        el navegador lo entienda correctamente.
                                        LocalDate.toString() devuelve exactamente ese formato.
                                    --%>
                                    <input type="date" class="form-control" id="fechaNacimiento"
                                           name="fechaNacimiento" value="${alumno.fechaNacimiento}" required>
                                </div>

                                <div class="mb-4">
                                    <label for="cursoId" class="form-label">
                                        <fmt:message key="alumnosCampoCursoFormulario" bundle="${msg}"/> 
                                        <span class="text-danger">*</span>
                                    </label>
                                    <%--
                                        Iteramos todos los cursos y marcamos como selected
                                        el que coincide con el curso actual del alumno.
                                    --%>
                                    <select class="form-select" id="cursoId"
                                            name="cursoId" required>
                                        <option value="">
                                            <fmt:message key="alumnosCampoCursoMensajeFormulario" bundle="${msg}"/> 
                                        </option>
                                        <c:forEach var="curso" items="${cursos}">
                                            <option value="${curso.id}"
                                                    <c:if test="${alumno.curso.id == curso.id}">
                                                        selected
                                                    </c:if>>
                                                ${curso.nombre}
                                            </option>
                                        </c:forEach>
                                    </select>
                                </div>

                                <div class="d-flex gap-2">
                                    <button type="submit" class="btn btn-primary">
                                        <i class="bi bi-save"></i>
                                        <fmt:message key="alumnosBotonGuardarFormulario" bundle="${msg}"/>
                                    </button>
                                    <a href="${pageContext.request.contextPath}/comun/GestionAlumnos?accion=listar"
                                       class="btn btn-secondary">
                                        <i class="bi bi-x-circle"></i>
                                        <fmt:message key="alumnosBotonCancelarFormulario" bundle="${msg}"/>
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
