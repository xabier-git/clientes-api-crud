# 📋 Guía de Scripts SQL y Shell - Clientes API

## 🎯 **Resumen de Consistencia**

Todos los scripts han sido revisados y corregidos para mantener consistencia total en:
- Códigos de tipos de cliente
- Contraseñas de base de datos  
- Estructura de tablas
- Orden de inserción (foreign keys)

---

## 📁 **Scripts SQL Disponibles**

### 1. **`setup-complete.sql`** 
**🎯 Propósito:** Script completo y robusto para producción/desarrollo completo

**Características:**
- ✅ **15 clientes** de ejemplo con datos completos
- ✅ **25 teléfonos** distribuidos entre clientes
- ✅ **7 tipos de cliente** (VIP, REGULAR, NUEVO, CORP, ESTUD, SENIOR, PREMIUM)
- ✅ **Usuarios de aplicación** con permisos específicos
- ✅ **Restricciones completas** (CHECK constraints, validaciones)
- ✅ **Índices optimizados** para rendimiento
- ✅ **Reportes de verificación** incluidos
- ✅ **Timestamps** automáticos (created_at, updated_at)

**Usar cuando:** Quieres un entorno completo con datos realistas para desarrollo o demo.

### 2. **`schema.sql`**
**🎯 Propósito:** Solo estructura de tablas, sin datos

**Características:**
- ✅ **Solo definición de tablas** (tipo_cliente, cliente, telefono)
- ✅ **Foreign keys y restricciones** completas
- ✅ **Índices optimizados**
- ✅ **Sin datos** de ejemplo
- ✅ **Timestamps** automáticos

**Usar cuando:** Quieres crear la estructura y poblar datos manualmente o mediante la aplicación.

### 3. **`data.sql`**
**🎯 Propósito:** Datos de ejemplo para usar con schema.sql

**Características:**
- ✅ **10 clientes** de ejemplo básicos
- ✅ **20 teléfonos** de ejemplo
- ✅ **5 tipos de cliente** básicos (VIP, REGULAR, NUEVO, CORP, ESTUD)
- ✅ **Reportes de verificación** incluidos

**Usar cuando:** Ya tienes la estructura creada con `schema.sql` y solo quieres datos de ejemplo.

### 4. **`simple-setup.sql`**
**🎯 Propósito:** Setup mínimo y rápido para desarrollo básico

**Características:**
- ✅ **4 clientes** básicos
- ✅ **7 teléfonos** básicos
- ✅ **4 tipos de cliente** (VIP, REGULAR, NUEVO, CORP)
- ✅ **Estructura mínima** sin restricciones complejas
- ✅ **Sin timestamps** automáticos

**Usar cuando:** Necesitas setup rápido para pruebas o desarrollo inicial.

---

## 🐚 **Scripts Shell Disponibles**

### 1. **`run.sh`**
**🎯 Propósito:** Script interactivo y flexible para desarrollo

**Características:**
- ✅ **Interactivo:** Pregunta qué configuración usar
- ✅ **Flexible:** Soporta Docker o MySQL local
- ✅ **Completo:** Compilación, tests y ejecución
- ✅ **Verificaciones:** Valida Docker y Maven
- ✅ **Espera MySQL:** Verificación real de conectividad antes de continuar
- ✅ **Usa:** `setup-complete.sql`

**Comando:** `./run.sh`

### 2. **`quick-setup.sh`**
**🎯 Propósito:** Setup automático sin preguntas

**Características:**
- ✅ **Automático:** Sin interacción del usuario
- ✅ **Rápido:** Setup completo en minutos
- ✅ **Docker obligatorio:** Solo funciona con Docker
- ✅ **Reinicia:** Borra y recrea todo desde cero
- ✅ **Espera MySQL:** Verificación real de conectividad antes de continuar
- ✅ **Usa:** `setup-complete.sql`

**Comando:** `./quick-setup.sh`

### 3. **`stop.sh`** 🆕
**🎯 Propósito:** Detener completamente el entorno

**Características:**
- ✅ **Detiene Spring Boot:** Mata procesos Java elegantemente
- ✅ **Detiene MySQL:** Para el contenedor Docker
- ✅ **Libera puertos:** 8080 y 3306
- ✅ **Opción de limpieza:** Permite eliminar contenedor MySQL
- ✅ **Verificación:** Muestra estado final del sistema

**Comando:** `./stop.sh`

### 4. **`status.sh`** 🆕
**🎯 Propósito:** Verificar estado completo del entorno

**Características:**
- ✅ **Estado Spring Boot:** PIDs, salud, conectividad
- ✅ **Estado MySQL:** Contenedor, conectividad, datos
- ✅ **Estado puertos:** 8080 y 3306
- ✅ **Herramientas:** Docker, Maven, Java
- ✅ **Recomendaciones:** Acciones sugeridas según el estado

**Comando:** `./status.sh`

### 5. **`validate-scripts.sh`**
**🎯 Propósito:** Verificar consistencia entre todos los scripts

**Características:**
- ✅ **Validación completa** de códigos de tipos de cliente
- ✅ **Verificación** de contraseñas y configuración
- ✅ **Chequeo** de estructura de tablas
- ✅ **Validación** de orden de foreign keys

**Comando:** `./validate-scripts.sh`

---

## 🔧 **Configuración Unificada**

### **Base de Datos:**
```
Host: localhost
Puerto: 3306
Base de datos: clientes_db
Usuario root: root / root123
Charset: utf8mb4
Collation: utf8mb4_unicode_ci
```

### **Códigos de Tipos de Cliente Estándar:**
```
VIP      - Cliente VIP (servicios premium)
REGULAR  - Cliente Regular (servicios estándar)  
NUEVO    - Cliente Nuevo (recién registrado)
CORP     - Cliente Corporativo (empresas)
ESTUD    - Cliente Estudiante (descuentos especiales)
SENIOR   - Cliente Senior (adultos mayores)
PREMIUM  - Cliente Premium (servicios exclusivos)
```

### **Estructura de Tablas:**
```
tipo_cliente (codigo VARCHAR(10) PK, descripcion)
    ↓
cliente (id BIGINT PK, rut VARCHAR(12), nombre, apellido, edad, email, cod_tipo_cliente FK)
    ↓  
telefono (id BIGINT PK, numero, tipo, principal, cliente_id FK)
```

---

## 🚀 **Recomendaciones de Uso**

### **Para Desarrollo Rápido:**
```bash
./quick-setup.sh
# Todo configurado automáticamente en 2-3 minutos
```

### **Para Desarrollo con Control:**
```bash
./run.sh
# Configuración interactiva, puedes elegir opciones
```

### **Para Verificar Estado:**
```bash
./status.sh
# Muestra estado completo del sistema
```

### **Para Detener Todo:**
```bash
./stop.sh
# Detiene aplicación y base de datos limpiamente
```

### **Para Producción/Demo:**
```bash
# Usar setup-complete.sql directamente
docker exec -i mysql-container mysql -uroot -proot123 clientes_db < src/main/resources/db/setup-complete.sql
```

### **Para Setup Manual:**
```bash
# 1. Solo estructura
mysql -uroot -p clientes_db < src/main/resources/db/schema.sql

# 2. Agregar datos después
mysql -uroot -p clientes_db < src/main/resources/db/data.sql
```

### **Para Verificar Consistencia:**
```bash
./validate-scripts.sh
# Valida que todos los scripts estén alineados
```

### **Flujo de Trabajo Típico:**
```bash
# 1. Verificar estado
./status.sh

# 2. Si está todo detenido, iniciar
./quick-setup.sh

# 3. Desarrollar y probar...

# 4. Detener cuando termines
./stop.sh

# 5. Verificar que se detuvo todo
./status.sh
```

---

## ✅ **Estado Actual: TODOS LOS SCRIPTS CONSISTENTES**

- ✅ Códigos de tipos de cliente unificados
- ✅ Contraseñas alineadas (root123)
- ✅ Estructura de tablas consistente
- ✅ Foreign keys en orden correcto
- ✅ Configuración de aplicación sincronizada
