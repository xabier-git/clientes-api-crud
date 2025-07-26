#!/bin/bash

# Script para verificar el estado del entorno de Clientes API
# Muestra información detallada sobre todos los componentes

echo "=================================================="
echo "    ESTADO DEL ENTORNO - CLIENTES API"
echo "=================================================="

echo "🔍 Verificando componentes del sistema..."
echo ""

# 1. Verificar aplicación Spring Boot
echo "1. 📱 APLICACIÓN SPRING BOOT:"
SPRING_PIDS=$(pgrep -f "spring-boot:run")
if [ ! -z "$SPRING_PIDS" ]; then
    echo "   ✅ Estado: EJECUTÁNDOSE"
    echo "   🔢 PIDs: $SPRING_PIDS"
    echo "   🌐 URL: http://localhost:8080"
    
    # Verificar si responde en el puerto 8080
    if curl -s http://localhost:8080/actuator/health >/dev/null 2>&1; then
        echo "   💚 Salud: RESPONDIENDO"
    elif curl -s http://localhost:8080 >/dev/null 2>&1; then
        echo "   💛 Salud: PARCIALMENTE RESPONDIENDO"
    else
        echo "   💔 Salud: NO RESPONDE"
    fi
else
    echo "   ❌ Estado: DETENIDA"
    echo "   ℹ️  Para iniciar: ./run.sh o ./quick-setup.sh"
fi

echo ""

# 2. Verificar contenedor MySQL
echo "2. 🗄️  BASE DE DATOS MYSQL:"
if docker ps -a --format 'table {{.Names}}' | grep -q "mysql-clientes-api"; then
    if docker ps --format 'table {{.Names}}' | grep -q "mysql-clientes-api"; then
        echo "   ✅ Estado: EJECUTÁNDOSE"
        
        # Verificar conectividad
        if docker exec mysql-clientes-api mysql -uroot -proot123 -e "SELECT 1" >/dev/null 2>&1; then
            echo "   💚 Conectividad: OK"
            
            # Verificar base de datos
            DB_EXISTS=$(docker exec mysql-clientes-api mysql -uroot -proot123 -e "SHOW DATABASES LIKE 'clientes_db'" 2>/dev/null | grep clientes_db)
            if [ ! -z "$DB_EXISTS" ]; then
                echo "   📊 Base de datos: clientes_db EXISTE"
                
                # Contar registros
                TIPOS_COUNT=$(docker exec mysql-clientes-api mysql -uroot -proot123 clientes_db -e "SELECT COUNT(*) FROM tipo_cliente" 2>/dev/null | tail -1)
                CLIENTES_COUNT=$(docker exec mysql-clientes-api mysql -uroot -proot123 clientes_db -e "SELECT COUNT(*) FROM cliente" 2>/dev/null | tail -1)
                TELEFONOS_COUNT=$(docker exec mysql-clientes-api mysql -uroot -proot123 clientes_db -e "SELECT COUNT(*) FROM telefono" 2>/dev/null | tail -1)
                
                echo "   📈 Datos: $TIPOS_COUNT tipos, $CLIENTES_COUNT clientes, $TELEFONOS_COUNT teléfonos"
            else
                echo "   ⚠️  Base de datos: clientes_db NO EXISTE"
            fi
        else
            echo "   💔 Conectividad: ERROR"
        fi
    else
        echo "   ⏸️  Estado: DETENIDO"
        echo "   ℹ️  Para iniciar: docker start mysql-clientes-api"
    fi
else
    echo "   ❌ Estado: NO EXISTE"
    echo "   ℹ️  Para crear: ./run.sh o ./quick-setup.sh"
fi

echo ""

# 3. Verificar puertos
echo "3. 🚪 PUERTOS:"
PORT_8080=$(lsof -ti:8080 2>/dev/null)
if [ ! -z "$PORT_8080" ]; then
    echo "   🔴 Puerto 8080: OCUPADO (PID: $PORT_8080)"
else
    echo "   ✅ Puerto 8080: LIBRE"
fi

PORT_3306=$(lsof -ti:3306 2>/dev/null)
if [ ! -z "$PORT_3306" ]; then
    echo "   🔴 Puerto 3306: OCUPADO (PID: $PORT_3306)"
else
    echo "   ✅ Puerto 3306: LIBRE"
fi

echo ""

# 4. Verificar Docker
echo "4. 🐳 DOCKER:"
if command -v docker &> /dev/null; then
    if docker info >/dev/null 2>&1; then
        echo "   ✅ Docker: DISPONIBLE Y CORRIENDO"
        echo "   📦 Contenedores MySQL: $(docker ps -a --filter name=mysql --format 'table {{.Names}}' | grep -c mysql || echo '0')"
    else
        echo "   ⚠️  Docker: INSTALADO PERO NO CORRIENDO"
    fi
else
    echo "   ❌ Docker: NO INSTALADO"
fi

echo ""

# 5. Verificar Maven
echo "5. ⚙️  HERRAMIENTAS:"
if command -v mvn &> /dev/null; then
    MVN_VERSION=$(mvn -version 2>/dev/null | head -1)
    echo "   ✅ Maven: $MVN_VERSION"
else
    echo "   ❌ Maven: NO INSTALADO"
fi

if command -v java &> /dev/null; then
    JAVA_VERSION=$(java -version 2>&1 | head -1)
    echo "   ✅ Java: $JAVA_VERSION"
else
    echo "   ❌ Java: NO INSTALADO"
fi

echo ""

# 6. Resumen y acciones recomendadas
echo "=================================================="
echo "📋 RESUMEN Y ACCIONES RECOMENDADAS:"
echo "=================================================="

# Determinar estado general
if [ ! -z "$SPRING_PIDS" ] && docker ps --format 'table {{.Names}}' | grep -q "mysql-clientes-api"; then
    echo "🟢 Estado general: SISTEMA COMPLETAMENTE OPERATIVO"
    echo ""
    echo "🔗 Enlaces disponibles:"
    echo "   • API REST: http://localhost:8080/api/clientes"
    echo "   • Swagger UI: http://localhost:8080/swagger-ui/index.html"
    echo "   • Tipos de Cliente: http://localhost:8080/api/tipos-cliente"
    echo ""
    echo "🛠️  Acciones disponibles:"
    echo "   • Detener todo: ./stop.sh"
    echo "   • Ver logs: mvn spring-boot:run"
    
elif [ ! -z "$SPRING_PIDS" ] && ! docker ps --format 'table {{.Names}}' | grep -q "mysql-clientes-api"; then
    echo "🟡 Estado general: APLICACIÓN CORRIENDO PERO SIN BASE DE DATOS"
    echo ""
    echo "🚨 Problema: Spring Boot está corriendo pero MySQL no está disponible"
    echo "💡 Solución: ./stop.sh && ./run.sh"
    
elif [ -z "$SPRING_PIDS" ] && docker ps --format 'table {{.Names}}' | grep -q "mysql-clientes-api"; then
    echo "🟡 Estado general: BASE DE DATOS CORRIENDO PERO SIN APLICACIÓN"
    echo ""
    echo "💡 Para iniciar aplicación: mvn spring-boot:run"
    echo "💡 Para setup completo: ./run.sh"
    
else
    echo "🔴 Estado general: SISTEMA DETENIDO"
    echo ""
    echo "💡 Para iniciar todo:"
    echo "   • Setup automático: ./quick-setup.sh"
    echo "   • Setup interactivo: ./run.sh"
fi

echo ""
echo "🏁 Verificación completada"
