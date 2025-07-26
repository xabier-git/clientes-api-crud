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
DROP TABLE IF EXISTS telefono;
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

-- Tabla telefono
CREATE TABLE telefono (
    id BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT 'ID único del teléfono',
    numero VARCHAR(15) NOT NULL COMMENT 'Número de teléfono',
    tipo VARCHAR(20) DEFAULT 'MOVIL' COMMENT 'Tipo de teléfono (MOVIL, FIJO, TRABAJO, etc.)',
    principal BOOLEAN DEFAULT FALSE COMMENT 'Indica si es el teléfono principal',
    cliente_id BIGINT NOT NULL COMMENT 'ID del cliente (FK)',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT 'Fecha de creación del registro',
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT 'Fecha de última actualización',
    
    -- Índices
    INDEX idx_telefono_cliente_id (cliente_id),
    INDEX idx_telefono_numero (numero),
    INDEX idx_telefono_tipo (tipo),
    
    -- Restricciones
    CONSTRAINT chk_telefono_numero_format CHECK (numero REGEXP '^[0-9+\\-\\s]+$'),
    CONSTRAINT chk_telefono_tipo_valido CHECK (tipo IN ('MOVIL', 'FIJO', 'TRABAJO', 'EMERGENCIA', 'OTRO')),
    
    -- Foreign Key
    CONSTRAINT fk_telefono_cliente FOREIGN KEY (cliente_id) REFERENCES cliente(id) ON DELETE CASCADE
    
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci 
COMMENT='Tabla de teléfonos de clientes';

-- PRIMERO: Insertar tipos de cliente (debe ir antes que clientes por foreign key)
INSERT INTO tipo_cliente (codigo, descripcion) VALUES
('VIP', 'Cliente VIP - Servicio premium con beneficios especiales'),
('REGULAR', 'Cliente Regular - Servicio estándar'),
('NUEVO', 'Cliente Nuevo - Recién registrado en el sistema'),
('CORP', 'Cliente Corporativo - Empresa o entidad jurídica'),
('ESTUD', 'Cliente Estudiante - Descuentos especiales para estudiantes'),
('SENIOR', 'Cliente Senior - Descuentos para adultos mayores'),
('PREMIUM', 'Cliente Premium - Servicios exclusivos');

-- SEGUNDO: Insertar clientes con datos aleatorios (15 registros)
INSERT INTO cliente (rut, nombre, apellido, edad, email, cod_tipo_cliente) VALUES
('12345678-9', 'María', 'González', 28, 'maria.gonzalez@email.com', 'VIP'),
('23456789-0', 'Carlos', 'Rodríguez', 35, 'carlos.rodriguez@empresa.com', 'CORP'),
('34567890-1', 'Ana', 'López', 22, 'ana.lopez@estudiante.edu', 'ESTUD'),
('45678901-2', 'Miguel', 'Fernández', 42, 'miguel.fernandez@gmail.com', 'REGULAR'),
('56789012-3', 'Laura', 'Martínez', 31, 'laura.martinez@hotmail.com', 'VIP'),
('67890123-4', 'Diego', 'Sánchez', 26, 'diego.sanchez@yahoo.com', 'NUEVO'),
('78901234-5', 'Carmen', 'Ruiz', 39, 'carmen.ruiz@outlook.com', 'REGULAR'),
('89012345-6', 'Antonio', 'Morales', 45, 'antonio.morales@corporativo.com', 'CORP'),
('90123456-7', 'Elena', 'Jiménez', 20, 'elena.jimenez@universidad.edu', 'ESTUD'),
('01234567-8', 'Roberto', 'Torres', 33, 'roberto.torres@ejemplo.com', 'REGULAR'),
('11223344-5', 'Patricia', 'Vega', 67, 'patricia.vega@senior.com', 'SENIOR'),
('22334455-6', 'Fernando', 'Castro', 29, 'fernando.castro@premium.com', 'PREMIUM'),
('33445566-7', 'Isabel', 'Ramos', 24, 'isabel.ramos@nuevo.com', 'NUEVO'),
('44556677-8', 'Javier', 'Herrera', 38, 'javier.herrera@vip.com', 'VIP'),
('55667788-9', 'Sofía', 'Mendoza', 19, 'sofia.mendoza@estudiante.edu', 'ESTUD');

-- TERCERO: Insertar teléfonos de ejemplo para los clientes
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
('211234567', 'FIJO', FALSE, 4),

-- Teléfonos para Laura Martínez (ID: 5)
('967890123', 'MOVIL', TRUE, 5),
('612345678', 'TRABAJO', FALSE, 5),

-- Teléfonos para Diego Sánchez (ID: 6)
('978123456', 'MOVIL', TRUE, 6),

-- Teléfonos para Carmen Ruiz (ID: 7)
('989234567', 'MOVIL', TRUE, 7),
('245678901', 'FIJO', FALSE, 7),

-- Teléfonos para Antonio Morales (ID: 8)
('990345678', 'MOVIL', TRUE, 8),
('256789012', 'TRABAJO', FALSE, 8),
('623456789', 'FIJO', FALSE, 8),

-- Teléfonos para Elena Jiménez (ID: 9)
('901456789', 'MOVIL', TRUE, 9),

-- Teléfonos para Roberto Torres (ID: 10)
('912567890', 'MOVIL', TRUE, 10),
('267890123', 'TRABAJO', FALSE, 10),

-- Teléfonos para Patricia Vega (ID: 11)
('923678901', 'MOVIL', TRUE, 11),
('278901234', 'FIJO', FALSE, 11),

-- Teléfonos para Fernando Castro (ID: 12)
('934789012', 'MOVIL', TRUE, 12),

-- Teléfonos para Isabel Ramos (ID: 13)
('945890123', 'MOVIL', TRUE, 13),

-- Teléfonos para Javier Herrera (ID: 14)
('956901234', 'MOVIL', TRUE, 14),
('289012345', 'TRABAJO', FALSE, 14),

-- Teléfonos para Sofía Mendoza (ID: 15)
('967012345', 'MOVIL', TRUE, 15);

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
    (SELECT COUNT(*) FROM telefono) as total_telefonos,
    (SELECT MIN(edad) FROM cliente) as edad_minima,
    (SELECT MAX(edad) FROM cliente) as edad_maxima,
    (SELECT ROUND(AVG(edad), 2) FROM cliente) as edad_promedio;

SELECT '' as separador;
SELECT 'TELÉFONOS POR CLIENTE:' as info;
SELECT 
    CONCAT(c.nombre, ' ', c.apellido) as cliente_nombre,
    GROUP_CONCAT(CONCAT(t.numero, ' (', t.tipo, ')') ORDER BY t.principal DESC SEPARATOR ', ') as telefonos
FROM cliente c
LEFT JOIN telefono t ON c.id = t.cliente_id
GROUP BY c.id, c.nombre, c.apellido
ORDER BY c.id;

SELECT '' as separador;
SELECT 'BASE DE DATOS CONFIGURADA EXITOSAMENTE' as status;
