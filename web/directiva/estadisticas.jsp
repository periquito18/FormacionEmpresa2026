<%-- 
    Document   : estadisticas
    Created on : 14 may 2026, 12:05:29
    Author     : SGame
--%>

<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html lang="es">
    <head>
        <meta charset="UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1">
        <title>Estadísticas de Prácticas</title>
    </head>
    <body>

        <%@ include file="../cabecera.jsp" %>

        <div class="container">

            <h2 class="mb-4">
                <i class="bi bi-bar-chart"></i> Estadísticas de Prácticas
            </h2>

            <%-- ===================== TABLAS ===================== --%>
            <div class="row g-4 mb-5">

                <%-- Tabla: alumnos por empresa --%>
                <div class="col-md-6">
                    <div class="card shadow h-100">
                        <div class="card-header bg-primary text-white">
                            <i class="bi bi-briefcase"></i>
                            Alumnos por Empresa
                        </div>
                        <div class="card-body p-0">
                            <table class="table table-hover align-middle mb-0">
                                <thead class="table-dark">
                                    <tr>
                                        <th>Empresa</th>
                                        <th class="text-center">Nº Alumnos</th>
                                    </tr>
                                </thead>
                                <tbody>
                                    <c:choose>
                                        <c:when test="${empty alumnosPorEmpresa}">
                                            <tr>
                                                <td colspan="2" class="text-center text-muted p-3">
                                                    No hay prácticas registradas
                                                </td>
                                            </tr>
                                        </c:when>
                                        <c:otherwise>
                                            <c:forEach var="fila" items="${alumnosPorEmpresa}">
                                                <tr>
                                                    <%--
                                                        fila es un Object[]:
                                                        fila[0] → nombre de la empresa
                                                        fila[1] → número de alumnos
                                                    --%>
                                                    <td>${fila[0]}</td>
                                                    <td class="text-center">
                                                        <span class="badge bg-primary fs-6">
                                                            ${fila[1]}
                                                        </span>
                                                    </td>
                                                </tr>
                                            </c:forEach>
                                        </c:otherwise>
                                    </c:choose>
                                </tbody>
                            </table>
                        </div>
                    </div>
                </div>

                <%-- Tabla: alumnos por curso --%>
                <div class="col-md-6">
                    <div class="card shadow h-100">
                        <div class="card-header bg-success text-white">
                            <i class="bi bi-book"></i>
                            Alumnos en Prácticas por Curso
                        </div>
                        <div class="card-body p-0">
                            <table class="table table-hover align-middle mb-0">
                                <thead class="table-dark">
                                    <tr>
                                        <th>Curso</th>
                                        <th class="text-center">Nº Alumnos</th>
                                    </tr>
                                </thead>
                                <tbody>
                                    <c:choose>
                                        <c:when test="${empty alumnosPorCurso}">
                                            <tr>
                                                <td colspan="2" class="text-center text-muted p-3">
                                                    No hay prácticas registradas
                                                </td>
                                            </tr>
                                        </c:when>
                                        <c:otherwise>
                                            <c:forEach var="fila" items="${alumnosPorCurso}">
                                                <tr>
                                                    <td>${fila[0]}</td>
                                                    <td class="text-center">
                                                        <span class="badge bg-success fs-6">
                                                            ${fila[1]}
                                                        </span>
                                                    </td>
                                                </tr>
                                            </c:forEach>
                                        </c:otherwise>
                                    </c:choose>
                                </tbody>
                            </table>
                        </div>
                    </div>
                </div>

            </div>

            <%-- ===================== GRÁFICAS ===================== --%>
            <div class="row g-4">

                <%-- Gráfica de barras: alumnos por empresa --%>
                <div class="col-md-6">
                    <div class="card shadow">
                        <div class="card-header bg-primary text-white">
                            <i class="bi bi-bar-chart"></i>
                            Alumnos por Empresa
                        </div>
                        <div class="card-body">
                            <%--
                                canvas es el elemento HTML donde Chart.js dibuja la gráfica.
                                El id lo usamos en JavaScript para identificarlo.
                            --%>
                            <canvas id="graficaEmpresas"></canvas>
                        </div>
                    </div>
                </div>

                <%-- Gráfica de sectores: alumnos por curso --%>
                <div class="col-md-6">
                    <div class="card shadow">
                        <div class="card-header bg-success text-white">
                            <i class="bi bi-pie-chart"></i>
                            Alumnos en Prácticas por Curso
                        </div>
                        <div class="card-body">
                            <canvas id="graficaCursos"></canvas>
                        </div>
                    </div>
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

        <%-- Chart.js via CDN --%>
        <script src="https://cdn.jsdelivr.net/npm/chart.js@4.4.0/dist/chart.umd.min.js"></script>

        <script>
            // ---- Gráfica de barras: alumnos por empresa ----
            // Los datos vienen del Servlet como strings JSON
            // que insertamos directamente en el JavaScript con EL
            const labelsEmpresas = [${labelsEmpresa}];
            const valuesEmpresas = [${valuesEmpresa}];

            new Chart(document.getElementById('graficaEmpresas'), {
                type: 'bar', // Tipo columnas
                data: {
                    labels: labelsEmpresas,
                    datasets: [{
                            label: 'Nº de alumnos',
                            data: valuesEmpresas,
                            backgroundColor: [
                                'rgba(54, 162, 235, 0.8)',
                                'rgba(255, 99, 132, 0.8)',
                                'rgba(255, 206, 86, 0.8)',
                                'rgba(75, 192, 192, 0.8)',
                                'rgba(153, 102, 255, 0.8)'
                            ],
                            borderColor: [
                                'rgba(54, 162, 235, 1)',
                                'rgba(255, 99, 132, 1)',
                                'rgba(255, 206, 86, 1)',
                                'rgba(75, 192, 192, 1)',
                                'rgba(153, 102, 255, 1)'
                            ],
                            borderWidth: 2
                        }]
                },
                options: {
                    responsive: true,
                    plugins: {
                        legend: {display: false},
                        title: {
                            display: true,
                            text: 'Alumnos asignados por empresa'
                        }
                    },
                    scales: {
                        y: {
                            beginAtZero: true,
                            // Forzamos que el eje Y muestre solo enteros
                            ticks: {
                                stepSize: 1,
                                precision: 0
                            }
                        }
                    }
                }
            });

            // ---- Gráfica de sectores: alumnos por curso ----
            const labelsCursos = [${labelsCurso}];
            const valuesCursos = [${valuesCurso}];

            new Chart(document.getElementById('graficaCursos'), {
                type: 'doughnut', // Tipo donut
                data: {
                    labels: labelsCursos,
                    datasets: [{
                            label: 'Nº de alumnos',
                            data: valuesCursos,
                            backgroundColor: [
                                'rgba(75, 192, 192, 0.8)',
                                'rgba(255, 159, 64, 0.8)',
                                'rgba(153, 102, 255, 0.8)',
                                'rgba(255, 99, 132, 0.8)'
                            ],
                            borderColor: [
                                'rgba(75, 192, 192, 1)',
                                'rgba(255, 159, 64, 1)',
                                'rgba(153, 102, 255, 1)',
                                'rgba(255, 99, 132, 1)'
                            ],
                            borderWidth: 2
                        }]
                },
                options: {
                    responsive: true,
                    plugins: {
                        legend: {
                            position: 'bottom'
                        },
                        title: {
                            display: true,
                            text: 'Distribución de alumnos por curso'
                        }
                    }
                }
            });
        </script>

    </body>
</html>