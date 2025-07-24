-- Script simplificado para setup básico de Clientes API
-- Usar si prefieres configuración manual

-- Crear base de datos
CREATE DATABASE IF NOT EXISTS clientes_db CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
USE clientes_db;

-- Limpiar tablas existentes
DROP TABLE IF EXISTS cliente;
DROP TABLE IF EXISTS tipo_cliente;

-- Crear tabla tipo_cliente
CREATE TABLE tipo_cliente (
    codigo VARCHAR(10) NOT NULL PRIMARY KEY,
    descripcion VARCHAR(100) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- Crear tabla cliente
CREATE TABLE cliente (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    rut VARCHAR(12) NOT NULL UNIQUE,
    nombre VARCHAR(50) NOT NULL,
    apellido VARCHAR(50) NOT NULL,
    edad INT,
    email VARCHAR(100) NOT NULL UNIQUE,
    cod_tipo_cliente VARCHAR(10) NOT NULL,
    
    INDEX idx_cliente_rut (rut),
    CONSTRAINT fk_cliente_tipo_cliente FOREIGN KEY (cod_tipo_cliente) REFERENCES tipo_cliente(codigo)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- Insertar tipos de cliente básicos
INSERT INTO tipo_cliente (codigo, descripcion) VALUES
('VIP', 'Cliente VIP - Servicio premium'),
('REG', 'Cliente Regular - Servicio estándar'),
('NEW', 'Cliente Nuevo - Recién registrado'),
('CORP', 'Cliente Corporativo');

-- Insertar algunos clientes de ejemplo
INSERT INTO cliente (rut, nombre, apellido, edad, email, cod_tipo_cliente) VALUES
('12345678-9', 'María', 'González', 28, 'maria.gonzalez@email.com', 'VIP'),
('23456789-0', 'Carlos', 'Rodríguez', 35, 'carlos.rodriguez@empresa.com', 'CORP'),
('34567890-1', 'Ana', 'López', 22, 'ana.lopez@estudiante.edu', 'NEW'),
('45678901-2', 'Miguel', 'Fernández', 42, 'miguel.fernandez@gmail.com', 'REG');

-- Verificar datos
SELECT 'Tipos de cliente creados:' as info;
SELECT * FROM tipo_cliente;

SELECT 'Clientes creados:' as info;
SELECT id, rut, nombre, apellido, cod_tipo_cliente FROM cliente;

SELECT 'Setup completado exitosamente' as status;
