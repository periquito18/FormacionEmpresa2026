<%-- 
    Document   : cabecera
    Created on : 21 feb 2026, 12:46:56
    Author     : SGame
--%>

<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>

<!-- Bootstrap 5 CSS via CDN -->
<link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" 
      rel="stylesheet">
<!-- Bootstrap Icons -->
<link href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.11.3/font/bootstrap-icons.css" 
      rel="stylesheet">

<!-- Barra de navegación superior -->
<nav class="navbar navbar-expand-lg navbar-dark bg-primary mb-4">
    <div class="container">
        <a class="navbar-brand" href="${pageContext.request.contextPath}/Inicio">
            <i class="bi bi-building"></i> FormacionEmpresa2026
        </a>
        <button class="navbar-toggler" type="button" data-bs-toggle="collapse" 
                data-bs-target="#navbarNav">
            <span class="navbar-toggler-icon"></span>
        </button>
        <div class="collapse navbar-collapse" id="navbarNav">
            <ul class="navbar-nav me-auto">

                <!-- Menú común para todos los profesores -->
                <li class="nav-item">
                    <a class="nav-link" href="${pageContext.request.contextPath}/comun/GestionAlumnos">
                        <i class="bi bi-people"></i> Alumnos
                    </a>
                </li>
                <li class="nav-item">
                    <a class="nav-link" href="${pageContext.request.contextPath}/comun/GestionEmpresas">
                        <i class="bi bi-briefcase"></i> Empresas
                    </a>
                </li>
                <li class="nav-item">
                    <a class="nav-link" href="${pageContext.request.contextPath}/comun/GestionPracticas">
                        <i class="bi bi-journal-check"></i> Prácticas
                    </a>
                </li>

                <!-- Menú exclusivo para Directiva -->
                <c:if test="${sessionScope.profesor.tipo == 'DIRECTIVA'}">
                    <li class="nav-item dropdown">
                        <a class="nav-link dropdown-toggle" href="#" 
                           data-bs-toggle="dropdown">
                            <i class="bi bi-shield-lock"></i> Directiva
                        </a>
                        <ul class="dropdown-menu">
                            <li>
                                <a class="dropdown-item" 
                                   href="${pageContext.request.contextPath}/directiva/GestionProfesores">
                                    <i class="bi bi-person-gear"></i> Profesores
                                </a>
                            </li>
                            <li>
                                <a class="dropdown-item" 
                                   href="${pageContext.request.contextPath}/directiva/GestionCursos">
                                    <i class="bi bi-book"></i> Cursos
                                </a>
                            </li>
                            <li><hr class="dropdown-divider"></li>
                            <li>
                                <a class="dropdown-item" 
                                   href="${pageContext.request.contextPath}/directiva/Estadisticas">
                                    <i class="bi bi-bar-chart"></i> Estadísticas
                                </a>
                            </li>
                        </ul>
                    </li>
                </c:if>

            </ul>

            <!-- Info del profesor logueado + botón cerrar sesión -->
            <ul class="navbar-nav">
                <li class="nav-item">
                    <span class="navbar-text me-3 text-white">
                        <i class="bi bi-person-circle"></i>
                        ${sessionScope.profesor.nombre} ${sessionScope.profesor.apellidos}
                        <c:if test="${sessionScope.profesor.tipo == 'DIRECTIVA'}">
                            <span class="badge bg-warning text-dark ms-1">Directiva</span>
                        </c:if>
                    </span>
                </li>
                <li class="nav-item">
                    <a class="nav-link btn btn-outline-light btn-sm" 
                       href="${pageContext.request.contextPath}/CerrarSesion">
                        <i class="bi bi-box-arrow-right"></i> Cerrar sesión
                    </a>
                </li>
            </ul>
        </div>
    </div>
</nav>

<!-- Bootstrap 5 JS via CDN -->
<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js"></script>