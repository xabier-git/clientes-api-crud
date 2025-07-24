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
    rut VARCHAR(12) NOT NULL UNIQUE COMMENT 'RUT único del cliente',
    nombre VARCHAR(50) NOT NULL COMMENT 'Nombre del cliente',
    apellido VARCHAR(50) NOT NULL COMMENT 'Apellido del cliente',
    edad INT COMMENT 'Edad del cliente',
    email VARCHAR(100) NOT NULL UNIQUE COMMENT 'Email único del cliente',
    cod_tipo_cliente VARCHAR(10) NOT NULL COMMENT 'Código del tipo de cliente',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT 'Fecha de creación del registro',
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT 'Fecha de última actualización',
    
    -- Índice principal por RUT
    INDEX idx_cliente_rut (rut),
    INDEX idx_cliente_email (email),
    
    -- Restricciones de integridad
    CONSTRAINT chk_cliente_edad CHECK (edad >= 0 AND edad <= 150),
    CONSTRAINT chk_cliente_email_format CHECK (email REGEXP '^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\.[a-zA-Z]{2,}$'),
    CONSTRAINT chk_cliente_nombre_not_empty CHECK (TRIM(nombre) != ''),
    CONSTRAINT chk_cliente_apellido_not_empty CHECK (TRIM(apellido) != ''),
    CONSTRAINT chk_cliente_rut_not_empty CHECK (TRIM(rut) != ''),
    
    -- Foreign Key
    CONSTRAINT fk_cliente_tipo_cliente FOREIGN KEY (cod_tipo_cliente) REFERENCES tipo_cliente(codigo)
    
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci 
COMMENT='Tabla principal de clientes';

-- Insertar clientes con datos aleatorios (15 registros)
INSERT INTO cliente (rut, nombre, apellido, edad, email, cod_tipo_cliente) VALUES
('12345678-9', 'María', 'González', 28, 'maria.gonzalez@email.com', 'VIP'),
('23456789-0', 'Carlos', 'Rodríguez', 35, 'carlos.rodriguez@empresa.com', 'CORPORATIVO'),
('34567890-1', 'Ana', 'López', 22, 'ana.lopez@estudiante.edu', 'ESTUDIANTE'),
('45678901-2', 'Miguel', 'Fernández', 42, 'miguel.fernandez@gmail.com', 'REGULAR'),
('56789012-3', 'Laura', 'Martínez', 31, 'laura.martinez@hotmail.com', 'VIP'),
('67890123-4', 'Diego', 'Sánchez', 26, 'diego.sanchez@yahoo.com', 'NUEVO'),
('78901234-5', 'Carmen', 'Ruiz', 39, 'carmen.ruiz@outlook.com', 'REGULAR'),
('89012345-6', 'Antonio', 'Morales', 45, 'antonio.morales@corporativo.com', 'CORPORATIVO'),
('90123456-7', 'Elena', 'Jiménez', 20, 'elena.jimenez@universidad.edu', 'ESTUDIANTE'),
('01234567-8', 'Roberto', 'Torres', 33, 'roberto.torres@ejemplo.com', 'REGULAR'),
('11223344-5', 'Patricia', 'Vega', 67, 'patricia.vega@senior.com', 'SENIOR'),
('22334455-6', 'Fernando', 'Castro', 29, 'fernando.castro@premium.com', 'PREMIUM'),
('33445566-7', 'Isabel', 'Ramos', 24, 'isabel.ramos@nuevo.com', 'NUEVO'),
('44556677-8', 'Javier', 'Herrera', 38, 'javier.herrera@vip.com', 'VIP'),
('55667788-9', 'Sofía', 'Mendoza', 19, 'sofia.mendoza@estudiante.edu', 'ESTUDIANTE');

-- Insertar tipos de cliente
INSERT INTO tipo_cliente (codigo, descripcion) VALUES
('VIP', 'Cliente VIP - Servicio premium con beneficios especiales'),
('REGULAR', 'Cliente Regular - Servicio estándar'),
('NUEVO', 'Cliente Nuevo - Recién registrado en el sistema'),
('CORPORATIVO', 'Cliente Corporativo - Empresa o entidad jurídica'),
('ESTUDIANTE', 'Cliente Estudiante - Descuentos especiales para estudiantes'),
('SENIOR', 'Cliente Senior - Descuentos para adultos mayores'),
('PREMIUM', 'Cliente Premium - Servicios exclusivos');

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
