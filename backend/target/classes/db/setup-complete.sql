-- Script completo de setup para la base de datos clientes_db
-- Ejecutar como usuario root o con privilegios de administración

-- Crear usuario específico para la aplicación (opcional pero recomendado)
CREATE USER IF NOT EXISTS 'clientes_app'@'localhost' IDENTIFIED BY 'clientes_password';
CREATE USER IF NOT EXISTS 'clientes_app'@'%' IDENTIFIED BY 'clientes_password';

-- Crear la base de datos
CREATE DATABASE IF NOT EXISTS clientes_db CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

-- Otorgar permisos al usuario de la aplicación
GRANT ALL PRIVILEGES ON clientes_db.* TO 'clientes_app'@'localhost';
GRANT ALL PRIVILEGES ON clientes_db.* TO 'clientes_app'@'%';
FLUSH PRIVILEGES;

USE clientes_db;

-- Tabla tipo_cliente
DROP TABLE IF EXISTS cliente;
DROP TABLE IF EXISTS tipo_cliente;

CREATE TABLE tipo_cliente (
    codigo VARCHAR(10) NOT NULL PRIMARY KEY COMMENT 'Código único del tipo de cliente',
    descripcion VARCHAR(100) NOT NULL COMMENT 'Descripción del tipo de cliente',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT 'Fecha de creación del registro',
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT 'Fecha de última actualización',
    
    UNIQUE KEY uk_tipo_cliente_codigo (codigo),
    INDEX idx_tipo_cliente_descripcion (descripcion)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci 
COMMENT='Catálogo de tipos de cliente';

-- Tabla cliente
CREATE TABLE cliente (
    id BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT 'ID único del cliente',
    nombre VARCHAR(50) NOT NULL COMMENT 'Nombre del cliente',
    apellido VARCHAR(50) NOT NULL COMMENT 'Apellido del cliente',
    edad INT COMMENT 'Edad del cliente',
    email VARCHAR(100) NOT NULL UNIQUE COMMENT 'Email único del cliente',
    cod_tipo_cliente VARCHAR(10) NOT NULL COMMENT 'Código del tipo de cliente (FK)',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT 'Fecha de creación del registro',
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT 'Fecha de última actualización',
    
    -- Índices para optimizar consultas
    INDEX idx_cliente_email (email),
    INDEX idx_cliente_nombre_apellido (nombre, apellido),
    INDEX idx_cliente_tipo (cod_tipo_cliente),
    INDEX idx_cliente_edad (edad),
    INDEX idx_cliente_created_at (created_at),
    
    -- Clave foránea
    CONSTRAINT fk_cliente_tipo_cliente 
        FOREIGN KEY (cod_tipo_cliente) 
        REFERENCES tipo_cliente(codigo) 
        ON UPDATE CASCADE 
        ON DELETE RESTRICT,
    
    -- Restricciones de integridad
    CONSTRAINT chk_cliente_edad CHECK (edad >= 0 AND edad <= 150),
    CONSTRAINT chk_cliente_email_format CHECK (email REGEXP '^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\.[a-zA-Z]{2,}$'),
    CONSTRAINT chk_cliente_nombre_not_empty CHECK (TRIM(nombre) != ''),
    CONSTRAINT chk_cliente_apellido_not_empty CHECK (TRIM(apellido) != '')
    
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci 
COMMENT='Tabla principal de clientes';

-- Insertar tipos de cliente
INSERT INTO tipo_cliente (codigo, descripcion) VALUES
('VIP', 'Cliente VIP - Servicio premium con beneficios especiales'),
('REGULAR', 'Cliente Regular - Servicio estándar'),
('NUEVO', 'Cliente Nuevo - Recién registrado en el sistema'),
('CORPORATIVO', 'Cliente Corporativo - Empresa o entidad jurídica'),
('ESTUDIANTE', 'Cliente Estudiante - Descuentos especiales para estudiantes'),
('SENIOR', 'Cliente Senior - Descuentos para adultos mayores'),
('PREMIUM', 'Cliente Premium - Servicios exclusivos');

-- Insertar clientes con datos aleatorios (15 registros)
INSERT INTO cliente (nombre, apellido, edad, email, cod_tipo_cliente) VALUES
('María', 'González', 28, 'maria.gonzalez@email.com', 'VIP'),
('Carlos', 'Rodríguez', 35, 'carlos.rodriguez@empresa.com', 'CORPORATIVO'),
('Ana', 'López', 22, 'ana.lopez@estudiante.edu', 'ESTUDIANTE'),
('Miguel', 'Fernández', 42, 'miguel.fernandez@gmail.com', 'REGULAR'),
('Laura', 'Martínez', 31, 'laura.martinez@hotmail.com', 'VIP'),
('Diego', 'Sánchez', 26, 'diego.sanchez@yahoo.com', 'NUEVO'),
('Carmen', 'Ruiz', 39, 'carmen.ruiz@outlook.com', 'REGULAR'),
('Antonio', 'Morales', 45, 'antonio.morales@corporativo.com', 'CORPORATIVO'),
('Elena', 'Jiménez', 20, 'elena.jimenez@universidad.edu', 'ESTUDIANTE'),
('Roberto', 'Torres', 33, 'roberto.torres@ejemplo.com', 'REGULAR'),
('Patricia', 'Vega', 67, 'patricia.vega@senior.com', 'SENIOR'),
('Fernando', 'Castro', 29, 'fernando.castro@premium.com', 'PREMIUM'),
('Isabel', 'Ramos', 24, 'isabel.ramos@nuevo.com', 'NUEVO'),
('Javier', 'Herrera', 38, 'javier.herrera@vip.com', 'VIP'),
('Sofía', 'Mendoza', 19, 'sofia.mendoza@estudiante.edu', 'ESTUDIANTE');

-- Mostrar información de las tablas creadas
SELECT 'ESTRUCTURA DE LA BASE DE DATOS' as info;
SHOW TABLES;

SELECT '' as separador;
SELECT 'TIPOS DE CLIENTE:' as info;
SELECT * FROM tipo_cliente ORDER BY codigo;

SELECT '' as separador;
SELECT 'CLIENTES POR TIPO:' as info;
SELECT 
    tc.codigo,
    tc.descripcion,
    COUNT(c.id) as total_clientes
FROM tipo_cliente tc
LEFT JOIN cliente c ON tc.codigo = c.cod_tipo_cliente
GROUP BY tc.codigo, tc.descripcion
ORDER BY total_clientes DESC;

SELECT '' as separador;
SELECT 'RESUMEN ESTADÍSTICO:' as info;
SELECT 
    (SELECT COUNT(*) FROM tipo_cliente) as total_tipos,
    (SELECT COUNT(*) FROM cliente) as total_clientes,
    (SELECT MIN(edad) FROM cliente) as edad_minima,
    (SELECT MAX(edad) FROM cliente) as edad_maxima,
    (SELECT ROUND(AVG(edad), 2) FROM cliente) as edad_promedio;

SELECT '' as separador;
SELECT 'BASE DE DATOS CONFIGURADA EXITOSAMENTE' as status;
