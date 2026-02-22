<%-- 
    Document   : inicio
    Created on : 21 feb 2026, 14:26:29
    Author     : SGame
--%>

<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>Inicio - FormacionEmpresa2026</title>
</head>
<body>

    <%-- Incluimos la cabecera común con navbar --%>
    <%@ include file="cabecera.jsp" %>

    <div class="container">

        <%-- Mensaje de error de acceso denegado si viene del FiltroDirectiva --%>
        <c:if test="${not empty sessionScope.errorAcceso}">
            <div class="alert alert-warning alert-dismissible fade show" role="alert">
                <i class="bi bi-exclamation-triangle"></i> ${sessionScope.errorAcceso}
                <button type="button" class="btn-close" data-bs-dismiss="alert"></button>
            </div>
            <%-- Eliminamos el mensaje de la sesión para que no reaparezca --%>
            <c:remove var="errorAcceso" scope="session"/>
        </c:if>

        <%-- Saludo personalizado --%>
        <h2 class="mb-4">
            Bienvenido, ${sessionScope.profesor.nombre}
            <small class="text-muted fs-5">
                — Panel de control
            </small>
        </h2>

        <%-- Tarjetas de resumen --%>
        <div class="row g-4 mb-5">

            <div class="col-md-4">
                <div class="card text-white bg-primary shadow">
                    <div class="card-body d-flex justify-content-between align-items-center">
                        <div>
                            <h6 class="card-title">Total Alumnos</h6>
                            <h2 class="mb-0">${totalAlumnos}</h2>
                        </div>
                        <i class="bi bi-people fs-1 opacity-50"></i>
                    </div>
                    <div class="card-footer">
                        <a href="${pageContext.request.contextPath}/Comun/GestionAlumnos" 
                           class="text-white text-decoration-none">
                            Ver alumnos <i class="bi bi-arrow-right"></i>
                        </a>
                    </div>
                </div>
            </div>

            <div class="col-md-4">
                <div class="card text-white bg-success shadow">
                    <div class="card-body d-flex justify-content-between align-items-center">
                        <div>
                            <h6 class="card-title">Total Empresas</h6>
                            <h2 class="mb-0">${totalEmpresas}</h2>
                        </div>
                        <i class="bi bi-briefcase fs-1 opacity-50"></i>
                    </div>
                    <div class="card-footer">
                        <a href="${pageContext.request.contextPath}/Comun/GestionEmpresas" 
                           class="text-white text-decoration-none">
                            Ver empresas <i class="bi bi-arrow-right"></i>
                        </a>
                    </div>
                </div>
            </div>

            <div class="col-md-4">
                <div class="card text-white bg-warning shadow">
                    <div class="card-body d-flex justify-content-between align-items-center">
                        <div>
                            <h6 class="card-title">Total Prácticas</h6>
                            <h2 class="mb-0">${totalPracticas}</h2>
                        </div>
                        <i class="bi bi-journal-check fs-1 opacity-50"></i>
                    </div>
                    <div class="card-footer">
                        <a href="${pageContext.request.contextPath}/Comun/GestionPracticas" 
                           class="text-white text-decoration-none">
                            Ver prácticas <i class="bi bi-arrow-right"></i>
                        </a>
                    </div>
                </div>
            </div>

        </div>
    </div>
</body>
</html>