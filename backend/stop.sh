#!/bin/bash

# Script para detener completamente el entorno de Clientes API
# Detiene la aplicación Spring Boot y el contenedor MySQL

echo "=================================================="
echo "    DETENIENDO CLIENTES API"
echo "=================================================="

echo "1. Deteniendo aplicación Spring Boot..."

# Buscar y detener procesos de Spring Boot
SPRING_PIDS=$(pgrep -f "spring-boot:run")
if [ ! -z "$SPRING_PIDS" ]; then
    echo "   Encontrados procesos Spring Boot: $SPRING_PIDS"
    echo "   Deteniendo procesos..."
    kill $SPRING_PIDS 2>/dev/null
    sleep 3
    
    # Verificar si siguen corriendo y forzar cierre si es necesario
    REMAINING_PIDS=$(pgrep -f "spring-boot:run")
    if [ ! -z "$REMAINING_PIDS" ]; then
        echo "   Forzando cierre de procesos restantes..."
        kill -9 $REMAINING_PIDS 2>/dev/null
    fi
    echo "   ✅ Aplicación Spring Boot detenida"
else
    echo "   ℹ️  No se encontraron procesos Spring Boot ejecutándose"
fi

echo ""
echo "2. Deteniendo contenedor MySQL..."

# Verificar si existe el contenedor MySQL
if docker ps -a --format 'table {{.Names}}' | grep -q "mysql-clientes-api"; then
    # Verificar si está corriendo
    if docker ps --format 'table {{.Names}}' | grep -q "mysql-clientes-api"; then
        echo "   Deteniendo contenedor mysql-clientes-api..."
        docker stop mysql-clientes-api >/dev/null 2>&1
        echo "   ✅ Contenedor MySQL detenido"
    else
        echo "   ℹ️  Contenedor MySQL ya estaba detenido"
    fi
else
    echo "   ℹ️  No se encontró contenedor mysql-clientes-api"
fi

echo ""
echo "3. Verificando puertos..."

# Verificar si el puerto 8080 está libre
PORT_8080=$(lsof -ti:8080 2>/dev/null)
if [ ! -z "$PORT_8080" ]; then
    echo "   Puerto 8080 aún en uso por proceso: $PORT_8080"
    echo "   Liberando puerto 8080..."
    kill -9 $PORT_8080 2>/dev/null
    echo "   ✅ Puerto 8080 liberado"
else
    echo "   ✅ Puerto 8080 ya está libre"
fi

# Verificar si el puerto 3306 está libre (MySQL)
PORT_3306=$(lsof -ti:3306 2>/dev/null | grep -v $(docker inspect mysql-clientes-api --format='{{.State.Pid}}' 2>/dev/null))
if [ ! -z "$PORT_3306" ]; then
    echo "   Puerto 3306 en uso por proceso externo: $PORT_3306"
    echo "   ⚠️  No se liberará automáticamente (puede ser MySQL local)"
else
    echo "   ✅ Puerto 3306 está libre"
fi

echo ""
echo "4. Resumen del estado..."

# Mostrar estado final
echo "   📊 Estado final:"
echo "      - Aplicación Spring Boot: $(pgrep -f 'spring-boot:run' >/dev/null && echo '🔴 Ejecutándose' || echo '✅ Detenida')"
echo "      - Contenedor MySQL: $(docker ps --format 'table {{.Names}}' | grep -q 'mysql-clientes-api' && echo '🔴 Ejecutándose' || echo '✅ Detenido')"
echo "      - Puerto 8080: $(lsof -ti:8080 >/dev/null 2>&1 && echo '🔴 Ocupado' || echo '✅ Libre')"
echo "      - Puerto 3306: $(lsof -ti:3306 >/dev/null 2>&1 && echo '🔴 Ocupado' || echo '✅ Libre')"

echo ""
echo "=================================================="
echo "🛑 ENTORNO DETENIDO"
echo "=================================================="
echo ""
echo "Para reiniciar el entorno ejecuta:"
echo "   ./run.sh          (configuración interactiva)"
echo "   ./quick-setup.sh  (configuración automática)"
echo ""

# Opcional: preguntar si quiere eliminar el contenedor MySQL
echo "¿Deseas eliminar completamente el contenedor MySQL? [y/N]:"
read -r response
if [[ "$response" =~ ^[Yy]$ ]]; then
    echo "Eliminando contenedor MySQL..."
    docker rm mysql-clientes-api >/dev/null 2>&1
    echo "✅ Contenedor MySQL eliminado"
    echo "⚠️  Todos los datos de la base de datos se han perdido"
else
    echo "✅ Contenedor MySQL preservado (se puede reiniciar más tarde)"
fi

echo ""
echo "🏁 Proceso de detención completado"
