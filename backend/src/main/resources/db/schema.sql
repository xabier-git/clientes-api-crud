-- Script de creación de base de datos y tablas para el sistema de gestión de clientes
-- MySQL 8.0+

-- Crear la base de datos
CREATE DATABASE IF NOT EXISTS clientes_db CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

USE clientes_db;

-- Tabla tipo_cliente
CREATE TABLE tipo_cliente (
    codigo VARCHAR(10) NOT NULL PRIMARY KEY COMMENT 'Código único del tipo de cliente',
    descripcion VARCHAR(100) NOT NULL COMMENT 'Descripción del tipo de cliente',
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
    cod_tipo_cliente VARCHAR(10) NOT NULL COMMENT 'Código del tipo de cliente (FK)',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT 'Fecha de creación del registro',
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT 'Fecha de última actualización',
    
    -- Índices
    INDEX idx_cliente_rut (rut),
    INDEX idx_cliente_email (email),
    INDEX idx_cliente_nombre_apellido (nombre, apellido),
    INDEX idx_cliente_tipo (cod_tipo_cliente),
    INDEX idx_cliente_edad (edad),
    
    -- Clave foránea
    CONSTRAINT fk_cliente_tipo_cliente 
        FOREIGN KEY (cod_tipo_cliente) 
        REFERENCES tipo_cliente(codigo) 
        ON UPDATE CASCADE 
        ON DELETE RESTRICT,
    
    -- Restricciones
    CONSTRAINT chk_cliente_edad CHECK (edad >= 0 AND edad <= 150),
    CONSTRAINT chk_cliente_email_format CHECK (email REGEXP '^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\.[a-zA-Z]{2,}$'),
    CONSTRAINT chk_cliente_rut_not_empty CHECK (TRIM(rut) != '')
    
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci 
COMMENT='Tabla principal de clientes';

-- Verificar que las tablas se crearon correctamente
SHOW TABLES;

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
    
    -- Clave foránea
    CONSTRAINT fk_telefono_cliente 
        FOREIGN KEY (cliente_id) 
        REFERENCES cliente(id) 
        ON UPDATE CASCADE 
        ON DELETE CASCADE,
    
    -- Restricciones
    CONSTRAINT chk_telefono_numero_format CHECK (numero REGEXP '^[0-9+\\-\\s]+$'),
    CONSTRAINT chk_telefono_tipo_valido CHECK (tipo IN ('MOVIL', 'FIJO', 'TRABAJO', 'EMERGENCIA', 'OTRO'))
    
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci 
COMMENT='Tabla de teléfonos de clientes';

-- Mostrar la estructura de las tablas
DESCRIBE tipo_cliente;
DESCRIBE cliente;
DESCRIBE telefono;
