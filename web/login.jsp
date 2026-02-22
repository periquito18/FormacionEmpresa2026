<%-- 
    Document   : login
    Created on : 21 feb 2026, 14:26:17
    Author     : SGame
--%>

<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>

<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>Login - FormacionEmpresa2026</title>
    
    <!-- Bootstrap 5 CSS -->
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" 
          rel="stylesheet">
    <link href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.11.3/font/bootstrap-icons.css" 
          rel="stylesheet">
    
    <!-- Estilos -->
    <style>
        
        /* Centramos el formulario vertical y horizontalmente */
        body {
            background-color: #f0f4f8;
            display: flex;
            align-items: center;
            justify-content: center;
            min-height: 100vh;
        }
        .login-card {
            width: 100%;
            max-width: 420px;
        }
    </style>
</head>
<body>
     <div class="login-card">
        <div class="card shadow">
            <div class="card-body p-5">

                <!-- Cabecera del formulario -->
                <div class="text-center mb-4">
                    <i class="bi bi-building fs-1 text-primary"></i>
                    <h3 class="mt-2">FormacionEmpresa2026</h3>
                    <p class="text-muted">Gestión de Formación en Empresa</p>
                </div>

                <!-- Mensaje de error si el login falló -->
                <c:if test="${not empty error}">
                    <div class="alert alert-danger" role="alert">
                        <i class="bi bi-exclamation-triangle"></i> ${error}
                    </div>
                </c:if>

                <!-- Formulario de login -->
                <!-- action="/login" envía al LoginServlet -->
                <!-- method="post" para no exponer datos en la URL -->
                <form action="${pageContext.request.contextPath}/Login" method="post">

                    <div class="mb-3">
                        <label for="email" class="form-label">Email</label>
                        <div class="input-group">
                            <span class="input-group-text">
                                <i class="bi bi-envelope"></i>
                            </span>
                            <!-- Mantenemos el email si el login falla -->
                            <input type="email" class="form-control" id="email" 
                                   name="email" value="${param.email}"
                                   placeholder="profesor@centro.es" required>
                        </div>
                    </div>

                    <div class="mb-4">
                        <label for="password" class="form-label">Contraseña</label>
                        <div class="input-group">
                            <span class="input-group-text">
                                <i class="bi bi-lock"></i>
                            </span>
                            <input type="password" class="form-control" 
                                   id="password" name="password" required>
                        </div>
                    </div>

                    <div class="d-grid">
                        <button type="submit" class="btn btn-primary btn-lg">
                            <i class="bi bi-box-arrow-in-right"></i> Iniciar Sesion
                        </button>
                    </div>

                </form>
            </div>
        </div>
    </div>

    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js">
    </script>
</body>
</html>