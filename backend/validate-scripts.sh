#!/bin/bash

# Script de validación de consistencia para todos los archivos SQL y Shell
# Verifica que todos los scripts estén alineados y funcionando

echo "=================================================="
echo "    VALIDACIÓN DE CONSISTENCIA DE SCRIPTS"
echo "=================================================="

ERRORS=0

echo "1. Verificando códigos de tipos de cliente..."

# Verificar que todos los scripts usen los mismos códigos de tipos de cliente
EXPECTED_CODES=("VIP" "REGULAR" "NUEVO" "CORP" "ESTUD" "SENIOR" "PREMIUM")

echo "   Verificando setup-complete.sql..."
for code in "${EXPECTED_CODES[@]}"; do
    if ! grep -q "'$code'" src/main/resources/db/setup-complete.sql; then
        echo "   ❌ Código '$code' no encontrado en setup-complete.sql"
        ERRORS=$((ERRORS + 1))
    fi
done

echo "   Verificando data.sql..."
EXPECTED_CODES_DATA=("VIP" "REGULAR" "NUEVO" "CORP" "ESTUD")
for code in "${EXPECTED_CODES_DATA[@]}"; do
    if ! grep -q "'$code'" src/main/resources/db/data.sql; then
        echo "   ❌ Código '$code' no encontrado en data.sql"
        ERRORS=$((ERRORS + 1))
    fi
done

echo "   Verificando simple-setup.sql..."
EXPECTED_CODES_SIMPLE=("VIP" "REGULAR" "NUEVO" "CORP")
for code in "${EXPECTED_CODES_SIMPLE[@]}"; do
    if ! grep -q "'$code'" src/main/resources/db/simple-setup.sql; then
        echo "   ❌ Código '$code' no encontrado en simple-setup.sql"
        ERRORS=$((ERRORS + 1))
    fi
done

echo "2. Verificando estructura de tablas..."

# Verificar que todos los scripts tengan el campo RUT
echo "   Verificando campo RUT en schema.sql..."
if ! grep -q "rut VARCHAR(12)" src/main/resources/db/schema.sql; then
    echo "   ❌ Campo RUT no encontrado en schema.sql"
    ERRORS=$((ERRORS + 1))
fi

echo "   Verificando campo RUT en simple-setup.sql..."
if ! grep -q "rut VARCHAR(12)" src/main/resources/db/simple-setup.sql; then
    echo "   ❌ Campo RUT no encontrado en simple-setup.sql"
    ERRORS=$((ERRORS + 1))
fi

echo "3. Verificando contraseñas en scripts shell..."

echo "   Verificando run.sh..."
if ! grep -q "root123" run.sh; then
    echo "   ❌ Contraseña 'root123' no encontrada en run.sh"
    ERRORS=$((ERRORS + 1))
fi

echo "   Verificando quick-setup.sh..."
if ! grep -q "root123" quick-setup.sh; then
    echo "   ❌ Contraseña 'root123' no encontrada en quick-setup.sh"
    ERRORS=$((ERRORS + 1))
fi

echo "4. Verificando foreign keys y orden de inserción..."

echo "   Verificando orden de inserción en setup-complete.sql..."
# Verificar que tipos de cliente se inserten antes que clientes
TIPO_LINE=$(grep -n "INSERT INTO tipo_cliente" src/main/resources/db/setup-complete.sql | head -1 | cut -d: -f1)
CLIENTE_LINE=$(grep -n "INSERT INTO cliente" src/main/resources/db/setup-complete.sql | head -1 | cut -d: -f1)

if [ "$TIPO_LINE" -gt "$CLIENTE_LINE" ]; then
    echo "   ❌ Tipos de cliente deben insertarse antes que clientes en setup-complete.sql"
    ERRORS=$((ERRORS + 1))
fi

echo "5. Verificando sintaxis SQL básica..."

echo "   Verificando terminadores de sentencia..."
if ! grep -q ";" src/main/resources/db/setup-complete.sql; then
    echo "   ❌ Falta terminador de sentencia en setup-complete.sql"
    ERRORS=$((ERRORS + 1))
fi

echo "6. Verificando archivos de configuración de aplicación..."

echo "   Verificando application.properties..."
if ! grep -q "root123" src/main/resources/application.properties; then
    echo "   ❌ Contraseña 'root123' no encontrada en application.properties"
    ERRORS=$((ERRORS + 1))
fi

echo ""
echo "=================================================="
if [ $ERRORS -eq 0 ]; then
    echo "✅ VALIDACIÓN EXITOSA - Todos los scripts están consistentes"
    echo "=================================================="
    
    echo ""
    echo "📋 RESUMEN DE CONFIGURACIÓN:"
    echo "   - Códigos de tipos de cliente: VIP, REGULAR, NUEVO, CORP, ESTUD, SENIOR, PREMIUM"
    echo "   - Contraseña MySQL: root123"
    echo "   - Base de datos: clientes_db"
    echo "   - Puerto: 3306"
    echo "   - Estructura: tipo_cliente → cliente → telefono"
    echo ""
    echo "🚀 Scripts recomendados para usar:"
    echo "   - Desarrollo rápido: ./quick-setup.sh"
    echo "   - Configuración manual: ./run.sh"
    echo "   - Solo estructura: schema.sql + data.sql"
    echo "   - Setup básico: simple-setup.sql"
    echo ""
else
    echo "❌ VALIDACIÓN FALLIDA - $ERRORS errores encontrados"
    echo "=================================================="
    echo "Por favor corrige los errores antes de ejecutar los scripts."
fi

exit $ERRORS
