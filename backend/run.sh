#!/bin/bash

# Script para configurar y ejecutar la aplicación de Clientes API
# Asegúrate de tener MySQL corriendo y configurado

echo "=================================================="
echo "    CONFIGURACIÓN DE CLIENTES API"
echo "=================================================="

# Verificar que Maven esté instalado
if ! command -v mvn &> /dev/null; then
    echo "Error: Maven no está instalado. Por favor instalar Maven primero."
    exit 1
fi

# Verificar que MySQL esté corriendo
if ! pgrep -x "mysqld" > /dev/null; then
    echo "Advertencia: MySQL no parece estar corriendo."
    echo "Por favor, asegúrate de que MySQL esté ejecutándose."
fi

echo "1. Limpiando y compilando el proyecto..."
mvn clean compile

if [ $? -ne 0 ]; then
    echo "Error en la compilación. Revisa los errores anteriores."
    exit 1
fi

echo ""
echo "2. Ejecutando tests..."
mvn test -q

echo ""
echo "3. Configuración de base de datos:"
echo "   - Asegúrate de ejecutar el script: src/main/resources/db/setup-complete.sql"
echo "   - O ejecutar manualmente:"
echo "     mysql -u root -p < src/main/resources/db/setup-complete.sql"

echo ""
echo "4. Iniciando la aplicación..."
echo "   La aplicación se ejecutará en: http://localhost:8080"
echo "   Swagger UI disponible en: http://localhost:8080/swagger-ui.html"
echo ""

# Ejecutar la aplicación
mvn spring-boot:run
