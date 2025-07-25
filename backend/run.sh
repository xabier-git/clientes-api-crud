#!/bin/bash

# Script para configurar y ejecutar la aplicación de Clientes API
# Opción 1: MySQL con Docker (recomendado)
# Opción 2: MySQL local

echo "=================================================="
echo "    CONFIGURACIÓN DE CLIENTES API"
echo "=================================================="

# Verificar que Maven esté instalado
if ! command -v mvn &> /dev/null; then
    echo "Error: Maven no está instalado. Por favor instalar Maven primero."
    exit 1
fi

# Función para verificar Docker
check_docker() {
    if command -v docker &> /dev/null; then
        echo "Docker detectado."
        return 0
    else
        echo "Docker no está instalado."
        return 1
    fi
}

# Función para configurar MySQL con Docker
setup_mysql_docker() {
    echo "Configurando MySQL con Docker..."
    
    # Verificar si ya existe el contenedor
    if docker ps -a --format 'table {{.Names}}' | grep -q "mysql-clientes-api\|mi_contenedor_mysql"; then
        echo "Contenedor MySQL ya existe."
        
        # Verificar si está corriendo
        if docker ps --format 'table {{.Names}}' | grep -q "mysql-clientes-api\|mi_contenedor_mysql"; then
            echo "MySQL ya está ejecutándose."
        else
            echo "Iniciando contenedor MySQL existente..."
            docker start mysql-clientes-api 2>/dev/null || docker start mi_contenedor_mysql 2>/dev/null
        fi
    else
        echo "Creando nuevo contenedor MySQL..."
        docker run --name mysql-clientes-api \
          -e MYSQL_ROOT_PASSWORD=root123 \
          -e MYSQL_DATABASE=clientes_db \
          -p 3306:3306 -d mysql:8.0
        
        echo "Esperando que MySQL esté listo..."
        # Esperar hasta que MySQL esté realmente disponible
        echo "Verificando conectividad con MySQL..."
        for i in {1..30}; do
            if docker exec mysql-clientes-api mysql -uroot -proot123 -e "SELECT 1" >/dev/null 2>&1; then
                echo "✅ MySQL está listo!"
                break
            fi
            echo "⏳ Esperando MySQL... (intento $i/30)"
            sleep 2
        done
        
        # Verificación final
        if ! docker exec mysql-clientes-api mysql -uroot -proot123 -e "SELECT 1" >/dev/null 2>&1; then
            echo "❌ Error: MySQL no está respondiendo después de 60 segundos"
            exit 1
        fi
    fi
    
    # Configurar base de datos
    echo "Configurando base de datos y datos de ejemplo..."
    docker exec -i mysql-clientes-api mysql -uroot -proot123 clientes_db < src/main/resources/db/setup-complete.sql 2>/dev/null ||
    docker exec -i mi_contenedor_mysql mysql -uroot -proot123 clientes_db < src/main/resources/db/setup-complete.sql
}

# Verificar configuración de base de datos
echo "1. Verificando configuración de base de datos..."

if check_docker; then
    echo "¿Deseas usar MySQL con Docker? (recomendado) [Y/n]:"
    read -r response
    if [[ "$response" =~ ^[Nn]$ ]]; then
        echo "Configuración manual requerida:"
        echo "   - Asegúrate de que MySQL esté corriendo localmente"
        echo "   - Ejecuta: mysql -u root -p < src/main/resources/db/setup-complete.sql"
    else
        setup_mysql_docker
    fi
else
    echo "Configuración manual requerida:"
    echo "   - Instala MySQL o Docker"
    echo "   - Ejecuta: mysql -u root -p < src/main/resources/db/setup-complete.sql"
fi

echo ""
echo "2. Limpiando y compilando el proyecto..."
mvn clean compile

if [ $? -ne 0 ]; then
    echo "Error en la compilación. Revisa los errores anteriores."
    exit 1
fi

echo ""
echo "3. Ejecutando tests..."
mvn test -q

echo ""
echo "4. Iniciando la aplicación..."
echo "   La aplicación se ejecutará en: http://localhost:8080"
echo "   Swagger UI disponible en: http://localhost:8080/swagger-ui.html"
echo "   API Clientes: http://localhost:8080/api/clientes"
echo "   API Tipos Cliente: http://localhost:8080/api/tipos-cliente"
echo ""

# Ejecutar la aplicación
mvn spring-boot:run
