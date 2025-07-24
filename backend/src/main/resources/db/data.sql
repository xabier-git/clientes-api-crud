-- Script de población de datos para el sistema de gestión de clientes
-- Datos de prueba con información aleatoria

USE clientes_db;

-- Insertar tipos de cliente
INSERT INTO tipo_cliente (codigo, descripcion) VALUES
('VIP', 'Cliente VIP - Servicio premium con beneficios especiales'),
('REGULAR', 'Cliente Regular - Servicio estándar'),
('NUEVO', 'Cliente Nuevo - Recién registrado en el sistema'),
('CORPORATIVO', 'Cliente Corporativo - Empresa o entidad jurídica'),
('ESTUDIANTE', 'Cliente Estudiante - Descuentos especiales para estudiantes');

-- Insertar clientes con datos aleatorios (10 registros)
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
