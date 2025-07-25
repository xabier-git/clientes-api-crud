-- Script simplificado para setup básico de Clientes API
-- Usar si prefieres configuración manual

-- Crear base de datos
CREATE DATABASE IF NOT EXISTS clientes_db CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
USE clientes_db;

-- Limpiar tablas existentes (en orden correcto por foreign keys)
DROP TABLE IF EXISTS telefono;
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

-- Crear tabla telefono
CREATE TABLE telefono (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    numero VARCHAR(15) NOT NULL,
    tipo VARCHAR(20) DEFAULT 'MOVIL',
    principal BOOLEAN DEFAULT FALSE,
    cliente_id BIGINT NOT NULL,
    
    INDEX idx_telefono_cliente_id (cliente_id),
    CONSTRAINT fk_telefono_cliente FOREIGN KEY (cliente_id) REFERENCES cliente(id) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- Insertar tipos de cliente básicos
INSERT INTO tipo_cliente (codigo, descripcion) VALUES
('VIP', 'Cliente VIP - Servicio premium'),
('REGULAR', 'Cliente Regular - Servicio estándar'),
('NUEVO', 'Cliente Nuevo - Recién registrado'),
('CORP', 'Cliente Corporativo');

-- Insertar algunos clientes de ejemplo
INSERT INTO cliente (rut, nombre, apellido, edad, email, cod_tipo_cliente) VALUES
('12345678-9', 'María', 'González', 28, 'maria.gonzalez@email.com', 'VIP'),
('23456789-0', 'Carlos', 'Rodríguez', 35, 'carlos.rodriguez@empresa.com', 'CORP'),
('34567890-1', 'Ana', 'López', 22, 'ana.lopez@estudiante.edu', 'NUEVO'),
('45678901-2', 'Miguel', 'Fernández', 42, 'miguel.fernandez@gmail.com', 'REGULAR');

-- Insertar teléfonos de ejemplo
INSERT INTO telefono (numero, tipo, principal, cliente_id) VALUES
-- Teléfonos para María González (ID: 1)
('912345678', 'MOVIL', TRUE, 1),
('223456789', 'FIJO', FALSE, 1),

-- Teléfonos para Carlos Rodríguez (ID: 2)
('987654321', 'MOVIL', TRUE, 2),
('234567890', 'TRABAJO', FALSE, 2),

-- Teléfonos para Ana López (ID: 3)
('945123678', 'MOVIL', TRUE, 3),

-- Teléfonos para Miguel Fernández (ID: 4)
('956789123', 'MOVIL', TRUE, 4),
('211234567', 'FIJO', FALSE, 4);

-- Verificar datos
SELECT 'Tipos de cliente creados:' as info;
SELECT * FROM tipo_cliente;

SELECT 'Clientes creados:' as info;
SELECT * FROM cliente;

SELECT 'Teléfonos creados:' as info;
SELECT 
    t.id,
    t.numero,
    t.tipo,
    t.principal,
    CONCAT(c.nombre, ' ', c.apellido) as cliente_nombre
FROM telefono t
JOIN cliente c ON t.cliente_id = c.id
ORDER BY c.id, t.principal DESC;
SELECT id, rut, nombre, apellido, cod_tipo_cliente FROM cliente;

SELECT 'Setup completado exitosamente' as status;
