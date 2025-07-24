#!/bin/bash

# Script rápido para setup completo con Docker
# Ejecuta todo automáticamente sin preguntas

echo "=================================================="
echo "    SETUP AUTOMÁTICO - CLIENTES API"
echo "=================================================="

# Verificar Docker
if ! command -v docker &> /dev/null; then
    echo "Error: Docker no está instalado."
    echo "Instala Docker primero: https://docs.docker.com/get-docker/"
    exit 1
fi

# Verificar Maven
if ! command -v mvn &> /dev/null; then
    echo "Error: Maven no está instalado."
    exit 1
fi

echo "1. Configurando MySQL con Docker..."

# Detener contenedor si existe
docker stop mysql-clientes-api 2>/dev/null
docker rm mysql-clientes-api 2>/dev/null

# Crear nuevo contenedor
docker run --name mysql-clientes-api \
  -e MYSQL_ROOT_PASSWORD= \
  -e MYSQL_DATABASE=clientes_db \
  -p 3306:3306 -d mysql:8.0

echo "2. Esperando que MySQL esté listo..."
sleep 15

echo "3. Configurando base de datos..."
# Ejecutar script de setup con manejo de errores
if docker exec -i mysql-clientes-api mysql -uroot clientes_db < src/main/resources/db/setup-complete.sql; then
    echo "   ✅ Base de datos configurada exitosamente"
else
    echo "   ⚠️  Error configurando base de datos, reintentando..."
    sleep 5
    docker exec -i mysql-clientes-api mysql -uroot clientes_db < src/main/resources/db/setup-complete.sql
fi

echo "4. Compilando aplicación..."
mvn clean compile -q

echo "5. Ejecutando aplicación..."
echo ""
echo "🚀 Aplicación disponible en:"
echo "   - API: http://localhost:8080/api/clientes"
echo "   - Swagger: http://localhost:8080/swagger-ui.html"
echo ""
echo "Presiona Ctrl+C para detener"
echo ""

mvn spring-boot:run
