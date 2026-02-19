-- ================================================
-- SCRIPT DE CREACIÓN DE BASE DE DATOS
-- FormacionEmpresa2026
-- ================================================

-- (Ejecutar como root en phpMyAdmin)
-- CREATE DATABASE formacion2026;
-- CREATE USER 'formacion2026'@localhost IDENTIFIED BY 'formacion2026';
-- GRANT ALL PRIVILEGES ON formacion2026.* TO 'formacion2026'@localhost;

USE formacion2026;

-- ================================================
-- TABLAS
-- ================================================

CREATE TABLE IF NOT EXISTS profesores (
    id          INT AUTO_INCREMENT PRIMARY KEY,
    nombre      VARCHAR(100) NOT NULL,
    apellidos   VARCHAR(150) NOT NULL,
    email       VARCHAR(150) NOT NULL UNIQUE,
    password    VARCHAR(255) NOT NULL,
    tipo        ENUM('NORMAL', 'DIRECTIVA') NOT NULL DEFAULT 'NORMAL'
);

CREATE TABLE IF NOT EXISTS cursos (
    id      INT AUTO_INCREMENT PRIMARY KEY,
    nombre  VARCHAR(100) NOT NULL UNIQUE
);

CREATE TABLE IF NOT EXISTS alumnos (
    id               INT AUTO_INCREMENT PRIMARY KEY,
    nombre           VARCHAR(100) NOT NULL,
    apellidos        VARCHAR(150) NOT NULL,
    email            VARCHAR(150) NOT NULL UNIQUE,
    fecha_nacimiento DATE NOT NULL,
    curso_id         INT NOT NULL,
    CONSTRAINT fk_alumno_curso FOREIGN KEY (curso_id) REFERENCES cursos(id)
);

CREATE TABLE IF NOT EXISTS empresas (
    id           INT AUTO_INCREMENT PRIMARY KEY,
    nombre       VARCHAR(150) NOT NULL,
    descripcion  TEXT,
    tutor_nombre VARCHAR(200) NOT NULL,
    tutor_email  VARCHAR(150) NOT NULL
);

CREATE TABLE IF NOT EXISTS practicas (
    id           INT AUTO_INCREMENT PRIMARY KEY,
    alumno_id    INT NOT NULL UNIQUE,
    empresa_id   INT NOT NULL,
    fecha_inicio DATE NOT NULL,
    fecha_fin    DATE NOT NULL,
    comentarios  TEXT,
    CONSTRAINT fk_practica_alumno  FOREIGN KEY (alumno_id)  REFERENCES alumnos(id),
    CONSTRAINT fk_practica_empresa FOREIGN KEY (empresa_id) REFERENCES empresas(id)
);

-- ================================================
-- DATOS DE PRUEBA
-- ================================================

-- Profesores (contraseña: 1234 para todos, hasheada con MD5 (Message Digest Algorithm 5) por simplicidad)
-- En la aplicación usaremos hash real, pero para pruebas nos vale
INSERT INTO profesores (nombre, apellidos, email, password, tipo) VALUES
('Carlos',  'García López',    'carlos.garcia@centro.es',  MD5('1234'), 'DIRECTIVA'),
('Laura',   'Martínez Ruiz',   'laura.martinez@centro.es', MD5('1234'), 'DIRECTIVA'),
('Pedro',   'Sánchez Mora',    'pedro.sanchez@centro.es',  MD5('1234'), 'NORMAL'),
('Ana',     'López Fernández', 'ana.lopez@centro.es',      MD5('1234'), 'NORMAL');

-- Cursos
INSERT INTO cursos (nombre) VALUES
('1 DAW'),
('2 DAW'),
('1 DAM'),
('2 DAM');

-- Alumnos
INSERT INTO alumnos (nombre, apellidos, email, fecha_nacimiento, curso_id) VALUES
('Miguel',   'Torres Alba',      'miguel.torres@alumno.es',   '2005-03-15', 1),
('Sara',     'Díaz Navarro',     'sara.diaz@alumno.es',       '2004-11-22', 1),
('Javier',   'Ruiz Castillo',    'javier.ruiz@alumno.es',     '2003-07-08', 2),
('Elena',    'Moreno Gil',       'elena.moreno@alumno.es',    '2004-02-14', 2),
('Pablo',    'Jiménez Vega',     'pablo.jimenez@alumno.es',   '2005-09-30', 3),
('Cristina', 'Hernández Pardo',  'cristina.hern@alumno.es',   '2003-12-05', 3),
('David',    'Fernández Reyes',  'david.fernandez@alumno.es', '2004-06-18', 4),
('Lucía',    'Gómez Serrano',    'lucia.gomez@alumno.es',     '2005-01-25', 4);

-- Empresas
INSERT INTO empresas (nombre, descripcion, tutor_nombre, tutor_email) VALUES
('Tecnología Soluciones SL',  'Empresa de desarrollo de software a medida',         'Roberto Vega Lara',    'roberto.vega@tecnosol.es'),
('Innovatech SA',             'Consultoría tecnológica y transformación digital',    'María Blanco Ruiz',    'maria.blanco@innovatech.es'),
('DataSystems Corp',          'Empresa especializada en bases de datos y Big Data',  'Fernando Castro Mora', 'fernando.castro@datasys.es'),
('WebCreators Studio',        'Agencia de diseño web y aplicaciones móviles',        'Isabel Romero Gil',    'isabel.romero@webcreators.es');

-- Prácticas
INSERT INTO practicas (alumno_id, empresa_id, fecha_inicio, fecha_fin, comentarios) VALUES
(3, 1, '2026-03-01', '2026-06-30', 'Alumno con buen nivel técnico, se adapta bien al equipo.'),
(4, 2, '2026-03-01', '2026-06-30', 'Pendiente de confirmar proyecto asignado.'),
(7, 3, '2026-03-01', '2026-06-30', NULL),
(8, 4, '2026-03-01', '2026-06-30', 'Muy buena actitud y proactividad.');