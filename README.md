# FormacionEmpresa2026

Aplicación web desarrollada con **Jakarta EE 10** para la gestión de la fase de **Formación en Empresa (FCT)** del ciclo formativo de Desarrollo de Aplicaciones Web (DAW).

Proyecto correspondiente a la **Segunda Evaluación** de la asignatura *Desarrollo Web en Entorno Servidor* (DWES).

---

## 📋 Descripción

La aplicación permite a los profesores gestionar toda la información relacionada con las prácticas en empresa de sus alumnos: cursos, alumnos, empresas y asignación de prácticas. Existe un rol de **Directiva** con permisos ampliados para la gestión de profesores, cursos y consulta de estadísticas.

---

## ✨ Funcionalidades

- 🔐 **Autenticación y control de sesiones** con dos roles: Profesor y Directiva
- 👨‍🏫 **Gestión de profesores y cursos** (solo Directiva)
- 🎓 **Gestión de alumnos** con importación masiva desde fichero CSV
- 🏢 **Gestión de empresas**
- 📋 **Gestión de prácticas** con envío automático de email al alumno
- 📊 **Estadísticas y gráficas** sobre prácticas por empresa y por curso
- 🌐 **Internacionalización** en español e inglés
- 🔌 **Servicio REST** para consulta y gestión de alumnos

---

## 🛠️ Tecnologías utilizadas

| Capa | Tecnología |
|------|-----------|
| Lenguaje | Java 23 |
| Servidor | Apache Tomcat (integrado en NetBeans) |
| Framework web | Jakarta EE 10 (Servlets, JSP, JSTL) |
| Persistencia | JPA 3.1 con EclipseLink |
| Base de datos | MariaDB (XAMPP) |
| REST | Jakarta JAX-RS (Jersey) |
| Email | Jakarta Mail |
| IDE | NetBeans 22 (proyecto Ant) |

---

## 🗂️ Estructura del proyecto

```
FormacionEmpresa2026/
├── crear_bd.sql                  ← Script de creación de BD y datos de prueba
├── build.xml
├── src/
│   ├── conf/
│   │   ├── MANIFEST.MF
│   │   └── persistence.xml       ← Configuración JPA
│   └── java/
│       ├── bundle/               ← Ficheros i18n (ES/EN)
│       ├── controladores/        ← Servlets
│       │   ├── directiva/        ← Solo accesibles con rol Directiva
│       │   └── comun/            ← Accesibles a todos los profesores
│       ├── filtros/              ← Filtros de seguridad de sesión
│       ├── rest/                 ← Servicio REST
│       └── modelo/
│           ├── entidades/        ← Clases JPA
│           ├── dao/              ← Acceso a datos
|           |    └── exceptions/
│           └── util/             ← Email, CSV, utilidades
└── web/
    ├── directiva/                ← JSPs de administración
    ├── comun/                    ← JSPs comunes
    ├── css/
    ├── js/
    ├── login.jsp
    ├── inicio.jsp
    └── WEB-INF/
        └── web.xml
```

---

## ⚙️ Instalación y configuración

### Requisitos previos

- NetBeans 22 con JDK 23
- XAMPP (MariaDB activo)
- Apache Tomcat (integrado en NetBeans)

### Pasos

**1. Clonar el repositorio**
```bash
git clone https://github.com/tuusuario/FormacionEmpresa2026.git
```

**2. Configurar la base de datos**

Ejecutar en phpMyAdmin (como root):
```sql
CREATE DATABASE formacion2026;
CREATE USER 'formacion2026'@localhost IDENTIFIED BY 'formacion2026';
GRANT ALL PRIVILEGES ON formacion2026.* TO 'formacion2026'@localhost;
```

Después ejecutar el script completo:
```
crear_bd.sql
```

**3. Abrir el proyecto en NetBeans**

File → Open Project → seleccionar la carpeta `FormacionEmpresa2026`

**4. Verificar las librerías**

Comprobar que en `Libraries` están presentes todos los JARs necesarios. Si alguno aparece con error, añadirlo manualmente desde clic derecho en Libraries → Add JAR/Folder.

**5. Arrancar la aplicación**

Clic en Run Project (F6). La aplicación estará disponible en:
```
http://localhost:8080/FormacionEmpresa2026
```

### Credenciales de prueba

| Email | Contraseña | Rol |
|-------|-----------|-----|
| carlos.garcia@centro.es | 1234 | Directiva |
| laura.martinez@centro.es | 1234 | Directiva |
| pedro.sanchez@centro.es | 1234 | Profesor |
| ana.lopez@centro.es | 1234 | Profesor |

---

## 📦 Librerías necesarias

Los JARs deben añadirse manualmente al proyecto (no incluidos en el repositorio):

- `mariadb-java-client-3.5.6.jar`
- `eclipselink.jar`
- `jakarta.persistence-api.jar`
- `jakarta.servlet.jsp.jstl-3.0.1.jar`
- `jakarta.servlet.jsp.jstl-api-3.0.0.jar`
- `jakarta.mail-2.0.1.jar`
- `jakarta.validation-api.jar`
- Jersey (JAX-RS) — pendiente de añadir

---

## 📅 Estado del desarrollo

- [x] Arquitectura y estructura del proyecto
- [x] Base de datos y entidades JPA
- [ ] Autenticación y gestión de sesiones
- [x] CRUD Profesores y Cursos
- [x] CRUD Alumnos + importación CSV
- [ ] Gestión de Prácticas + email
- [ ] Servicio REST
- [ ] Estadísticas y gráficas
- [ ] Internacionalización

---

## 👨‍💻 Autor

Proyecto académico — Ciclo Formativo DAW  
Asignatura: Desarrollo Web en Entorno Servidor
Autor: Sergio Mate Palacios
