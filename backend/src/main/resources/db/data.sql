-- Script de población de datos para el sistema de gestión de clientes
-- Datos de prueba con información aleatoria

USE clientes_db;

-- Insertar tipos de cliente
INSERT INTO tipo_cliente (codigo, descripcion) VALUES
('VIP', 'Cliente VIP - Servicio premium con beneficios especiales'),
('REGULAR', 'Cliente Regular - Servicio estándar'),
('NUEVO', 'Cliente Nuevo - Recién registrado en el sistema'),
('CORP', 'Cliente Corporativo - Empresa o entidad jurídica'),
('ESTUD', 'Cliente Estudiante - Descuentos especiales para estudiantes');

-- Insertar clientes con datos aleatorios (10 registros)
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
('01234567-8', 'Roberto', 'Torres', 33, 'roberto.torres@ejemplo.com', 'REGULAR');

-- Verificar los datos insertados
SELECT 'Tipos de Cliente insertados:' as info;
SELECT * FROM tipo_cliente ORDER BY codigo;

SELECT '' as separador;
SELECT 'Clientes insertados:' as info;
SELECT 
    c.id,
    c.nombre,
    c.apellido,
    c.edad,
    c.email,
    c.cod_tipo_cliente,
    tc.descripcion as tipo_descripcion
FROM cliente c
JOIN tipo_cliente tc ON c.cod_tipo_cliente = tc.codigo
ORDER BY c.id;

-- Estadísticas
SELECT '' as separador;
SELECT 'Estadísticas:' as info;

SELECT 
    tc.codigo,
    tc.descripcion,
    COUNT(c.id) as total_clientes
FROM tipo_cliente tc
LEFT JOIN cliente c ON tc.codigo = c.cod_tipo_cliente
GROUP BY tc.codigo, tc.descripcion
ORDER BY total_clientes DESC;

SELECT '' as separador;
SELECT 'Resumen final:' as info;
SELECT 
    (SELECT COUNT(*) FROM tipo_cliente) as total_tipos_cliente,
    (SELECT COUNT(*) FROM cliente) as total_clientes,
    (SELECT ROUND(AVG(edad), 2) FROM cliente) as edad_promedio;

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
('267890123', 'TRABAJO', FALSE, 10);

-- Verificar los teléfonos insertados
SELECT '' as separador;
SELECT 'Teléfonos insertados:' as info;
SELECT 
    t.id,
    t.numero,
    t.tipo,
    t.principal,
    CONCAT(c.nombre, ' ', c.apellido) as cliente_nombre
FROM telefono t
JOIN cliente c ON t.cliente_id = c.id
ORDER BY c.id, t.principal DESC;
