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
('Roberto', 'Torres', 33, 'roberto.torres@ejemplo.com', 'REGULAR');

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
