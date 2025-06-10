-- #  SCRIPT PARA LA CREACIÓN DE LA BASE DE DATOS: Empresa_SD

-- 0. ELIMINAR BASE DE DATOS Y USUARIO SI EXISTEN
DROP DATABASE IF EXISTS Empresa_SD;
DROP USER IF EXISTS 'sd_user'@'%';

-- 1. CREACIÓN DE LA BASE DE DATOS
CREATE DATABASE Empresa_SD
  CHARACTER SET utf8mb4
  COLLATE utf8mb4_unicode_ci;
USE Empresa_SD;

-- 2. CREACIÓN DE USUARIO Y PERMISOS
CREATE USER 'sd_user'@'%' IDENTIFIED BY 'TuPasswordSeguroRol_Proyecto';
GRANT ALL PRIVILEGES ON Empresa_SD.* TO 'sd_user'@'%';
FLUSH PRIVILEGES;

-- 3. CREACIÓN DE TABLAS Y RESTRICCIONES
----------------------------------------------------------------------

-- Tabla: Departamento
CREATE TABLE Departamento (
  IDDpto      INT           AUTO_INCREMENT,
  Nombre      VARCHAR(100)  NOT NULL,
  Telefono    CHAR(10),
  Fax         CHAR(10),
  created_at  DATETIME      NOT NULL DEFAULT CURRENT_TIMESTAMP,
  updated_at  DATETIME      NOT NULL DEFAULT CURRENT_TIMESTAMP
                  ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (IDDpto)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_unicode_ci;

-- Tabla: Ingeniero
CREATE TABLE Ingeniero (
  IDIng        INT           AUTO_INCREMENT,
  IDDpto       INT           NOT NULL,
  Nombre       VARCHAR(50)   NOT NULL,
  Apellido     VARCHAR(50)   NOT NULL,
  Especialidad VARCHAR(100)  NOT NULL,
  Cargo        VARCHAR(100),
  created_at   DATETIME      NOT NULL DEFAULT CURRENT_TIMESTAMP,
  updated_at   DATETIME      NOT NULL DEFAULT CURRENT_TIMESTAMP
                   ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (IDIng),
  CONSTRAINT FK_Ingeniero_Departamento
    FOREIGN KEY (IDDpto)
    REFERENCES Departamento(IDDpto)
    ON DELETE RESTRICT
    ON UPDATE CASCADE
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_unicode_ci;

-- Tabla: Proyecto
CREATE TABLE Proyecto (
  IDProy      INT           AUTO_INCREMENT,
  Nombre      VARCHAR(150)  NOT NULL,
  Fec_Inicio  DATE          NOT NULL,
  Fec_Termino DATE,
  created_at  DATETIME      NOT NULL DEFAULT CURRENT_TIMESTAMP,
  updated_at  DATETIME      NOT NULL DEFAULT CURRENT_TIMESTAMP
                  ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (IDProy),
  CONSTRAINT CHK_Fechas
    CHECK (Fec_Termino IS NULL OR Fec_Termino >= Fec_Inicio)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_unicode_ci;

-- Tabla de Unión: Asignacion (N:M Ingeniero–Proyecto) con ID propio
CREATE TABLE Asignacion (
  IDAsignacion INT          AUTO_INCREMENT,
  IDIng        INT          NOT NULL,
  IDProy       INT          NOT NULL,
  Rol_Proyecto VARCHAR(100) NOT NULL,
  created_at   DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (IDAsignacion),
  CONSTRAINT UQ_Asignacion UNIQUE (IDIng, IDProy),
  CONSTRAINT FK_Asignacion_Ingeniero
    FOREIGN KEY (IDIng)
    REFERENCES Ingeniero(IDIng)
    ON DELETE CASCADE
    ON UPDATE CASCADE,
  CONSTRAINT FK_Asignacion_Proyecto
    FOREIGN KEY (IDProy)
    REFERENCES Proyecto(IDProy)
    ON DELETE CASCADE
    ON UPDATE CASCADE
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_unicode_ci;

-- 4. ÍNDICES SECUNDARIOS
----------------------------------------------------------------------

CREATE INDEX IDX_Ing_Espec_Apellido
  ON Ingeniero (Especialidad, Apellido);

CREATE INDEX IDX_Proyecto_Fechas
  ON Proyecto (Fec_Inicio, Fec_Termino);

-- 5. DATOS DE EJEMPLO
----------------------------------------------------------------------

INSERT INTO Departamento (Nombre, Telefono, Fax) VALUES
  ('Tecnologías de la Información', '9876543210', '0000000001'),
  ('Ingeniería Civil',             '9123456780', '0000000002'),
  ('Recursos Humanos',             '9555555550', '0000000003');

INSERT INTO Ingeniero (IDDpto, Nombre, Apellido, Especialidad, Cargo) VALUES
  (1, 'Ana',    'García',    'Desarrollo de Software', 'Ingeniera de Software Senior'),
  (1, 'Carlos', 'Martinez',  'Bases de Datos',         'DBA'),
  (2, 'Laura',  'Rodriguez', 'Estructuras',            'Jefa de Proyectos Civiles'),
  (2, 'Pedro',  'Sanchez',   'Hidráulica',             'Ingeniero Civil');

INSERT INTO Proyecto (Nombre, Fec_Inicio, Fec_Termino) VALUES
  ('CRM',                       '2024-03-01', '2025-02-28'),
  ('Puente "El Progreso"',      '2024-05-15', NULL),
  ('Migración a la Nube AWS',   '2024-08-01', '2024-12-31');

INSERT INTO Asignacion (IDIng, IDProy, Rol_Proyecto) VALUES
  (1, 1, 'Líder de Desarrollo'),
  (2, 1, 'Especialista en Base de Datos'),
  (1, 3, 'Arquitecta de Soluciones'),
  (2, 3, 'Administradora de Migración'),
  (3, 2, 'Directora de Obra'),
  (4, 2, 'Ingeniero de Campo');

-- 6. PROCEDIMIENTO ALMACENADO sp_AsignarIngenieroAProyecto
----------------------------------------------------------------------

DELIMITER $$
CREATE PROCEDURE sp_AsignarIngenieroAProyecto (
  IN  p_IDIng        INT,
  IN  p_IDProy       INT,
  IN  p_Rol_Proyecto VARCHAR(100),
  OUT p_mensaje      VARCHAR(200)
)
BEGIN
  DECLARE EXIT HANDLER FOR SQLEXCEPTION
  BEGIN
    ROLLBACK;
    SET p_mensaje = 'Error: No se pudo asignar el ingeniero al proyecto.';
  END;

  START TRANSACTION;

  IF NOT EXISTS (SELECT 1 FROM Ingeniero WHERE IDIng = p_IDIng) THEN
    SET p_mensaje = 'Error: El ingeniero no existe.';
    ROLLBACK;
  ELSEIF NOT EXISTS (SELECT 1 FROM Proyecto WHERE IDProy = p_IDProy) THEN
    SET p_mensaje = 'Error: El proyecto no existe.';
    ROLLBACK;
  ELSEIF EXISTS (SELECT 1 FROM Asignacion WHERE IDIng = p_IDIng AND IDProy = p_IDProy) THEN
    SET p_mensaje = 'Error: Ya existe una asignación para ese ingeniero y proyecto.';
    ROLLBACK;
  ELSE
    INSERT INTO Asignacion(IDIng, IDProy, Rol_Proyecto)
      VALUES (p_IDIng, p_IDProy, p_Rol_Proyecto);
    SET p_mensaje = 'Asignación realizada con éxito.';
    COMMIT;
  END IF;
END$$
DELIMITER ;