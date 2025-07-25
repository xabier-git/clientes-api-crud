# API de Gestión de Clientes

Una API REST CRUD completa para la gestión de clientes desarrollada con Spring Boot, JPA/Hibernate, MySQL y Lombok.

## Características

- **CRUD completo** para clientes con teléfonos integrados
- **Relaciones JPA** con foreign keys entre Cliente ↔ TipoCliente y Cliente ↔ Telefono
- **Gestión de teléfonos** múltiples por cliente con cascada automática
- **TipoCliente como catálogo** de solo consulta para frontend
- **Búsqueda por RUT** como identificador único secundario
- **Validaciones de datos** con Bean Validation
- **Solo JSON** en requests y responses
- **Documentación API** con Swagger/OpenAPI 3
- **Lombok** para reducir código boilerplate
- **Índices optimizados** para búsquedas por RUT y teléfonos
- **Manejo de errores** centralizado
- **Docker MySQL** con datos de ejemplo

## Tecnologías Utilizadas

- **Java 17**
- **Spring Boot 3.1.0**
- **Spring Data JPA**
- **Hibernate**
- **MySQL 8.0+ (Docker)**
- **Lombok**
- **Swagger/OpenAPI 3**
- **Maven**

## Modelo de Datos

### TipoCliente (Catálogo)
- `codigo` (String, PK): Código único del tipo (VIP, REG, CORP, etc.)
- `descripcion` (String): Descripción del tipo de cliente

### Cliente
- `id` (Long, PK): ID autoincremental
- `rut` (String, Unique): RUT único del cliente (índice principal)
- `nombre` (String): Nombre del cliente
- `apellido` (String): Apellido del cliente
- `edad` (Integer): Edad del cliente
- `email` (String, Unique): Email único
- `codTipoCliente` (String, FK): Código del tipo de cliente
- `telefonos` (List<String>): Lista de números de teléfono
- `tipoCliente` (TipoCliente): Relación @ManyToOne con TipoCliente

### Telefono
- `id` (Long, PK): ID autoincremental
- `numero` (String): Número de teléfono
- `tipo` (String): Tipo de teléfono (MOVIL, FIJO, TRABAJO, etc.)
- `principal` (Boolean): Indica si es el teléfono principal
- `clienteId` (Long, FK): ID del cliente propietario
- `cliente` (Cliente): Relación @ManyToOne con Cliente

### Relaciones
- **Cliente** → **TipoCliente**: Relación Many-to-One con foreign key
- **Cliente** → **Telefono**: Relación One-to-Many con cascada automática

## Configuración Rápida con Docker

### 1. Ejecutar MySQL con Docker

```bash
# Crear y ejecutar contenedor MySQL
docker run --name mysql-clientes-api \
  -e MYSQL_ROOT_PASSWORD= \
  -e MYSQL_DATABASE=clientes_db \
  -p 3306:3306 -d mysql:8.0

# Verificar que esté ejecutándose
docker ps
```

### 2. Configurar la base de datos

**Opción A: Script completo (recomendado)**
```bash
docker exec -i mysql-clientes-api mysql -uroot clientes_db < src/main/resources/db/setup-complete.sql
```

**Opción B: Setup básico**
```bash
docker exec -i mysql-clientes-api mysql -uroot clientes_db < src/main/resources/db/simple-setup.sql
```

### 3. Configurar aplicación

Verificar `application.properties` (ya configurado):

```properties
spring.datasource.url=jdbc:mysql://localhost:3306/clientes_db?useSSL=false&serverTimezone=UTC&allowPublicKeyRetrieval=true
spring.datasource.username=root
spring.datasource.password=
spring.datasource.driver-class-name=com.mysql.cj.jdbc.Driver
```

## Ejecutar la Aplicación

### Prerrequisitos

- JDK 17+
- Maven 3.6+
- Docker (para MySQL)

### Comandos

**Setup automático (recomendado):**
```bash
# Setup completo automático con Docker
./quick-setup.sh
```

**Setup manual:**
```bash
# Hacer ejecutable el script
chmod +x run.sh

# Ejecutar con script interactivo
./run.sh

# O manualmente:
mvn clean compile
mvn spring-boot:run
```

## Scripts Disponibles

- **`quick-setup.sh`**: Setup automático completo con Docker MySQL
- **`run.sh`**: Script interactivo con opciones Docker/manual  
- **`setup-complete.sql`**: Script SQL completo con datos de ejemplo
- **`simple-setup.sql`**: Script SQL básico con datos mínimos

## Documentación de la API

Una vez ejecutada la aplicación, la documentación estará disponible en:

- **Swagger UI**: http://localhost:8080/swagger-ui.html
- **OpenAPI JSON**: http://localhost:8080/api-docs

## Endpoints API

Todos los endpoints solo aceptan y retornan JSON (`application/json`):

### Clientes

- `GET /api/clientes` - Listar todos los clientes
- `GET /api/clientes/{id}` - Obtener cliente por ID
- `GET /api/clientes/rut/{rut}` - Obtener cliente por RUT
- `POST /api/clientes` - Crear nuevo cliente (con teléfonos)
- `PUT /api/clientes/{id}` - Actualizar cliente (incluye teléfonos)
- `DELETE /api/clientes/{id}` - Eliminar cliente (elimina teléfonos automáticamente)

### Tipos de Cliente (Solo Consulta)

- `GET /api/tipos-cliente` - Listar todos los tipos (para catálogo/<select>)
- `GET /api/tipos-cliente/{codigo}` - Obtener tipo por código

## Datos de Ejemplo

El sistema viene con datos precargados:

### Tipos de Cliente:
- **VIP**: Cliente VIP - Servicio premium con beneficios especiales
- **REG**: Cliente Regular - Servicio estándar
- **NEW**: Cliente Nuevo - Recién registrado en el sistema
- **CORP**: Cliente Corporativo - Empresa o entidad jurídica
- **EST**: Cliente Estudiante - Descuentos especiales para estudiantes
- **SEN**: Cliente Senior - Descuentos para adultos mayores
- **PREM**: Cliente Premium - Servicios exclusivos

### Clientes:
10+ clientes de ejemplo con diferentes tipos y múltiples teléfonos por cliente

## Ejemplos de Uso

### Listar Tipos de Cliente

```bash
curl http://localhost:8080/api/tipos-cliente
```

### Crear un Cliente con Teléfonos

```bash
curl -X POST http://localhost:8080/api/clientes \
  -H "Content-Type: application/json" \
  -d '{
    "rut": "66666666-6",
    "nombre": "Test",
    "apellido": "Validacion",
    "edad": 25,
    "email": "test.validacion@email.com",
    "codTipoCliente": "VIP",
    "telefonos": ["941894939", "989549924"]
  }'
```

### Crear un Cliente (sin teléfonos)

```bash
curl -X POST http://localhost:8080/api/clientes \
  -H "Content-Type: application/json" \
  -d '{
    "rut": "99888777-6",
    "nombre": "Juan",
    "apellido": "Pérez",
    "edad": 30,
    "email": "juan.perez@email.com",
    "codTipoCliente": "VIP"
  }'
```

### Obtener Cliente por RUT (incluye teléfonos)

```bash
curl http://localhost:8080/api/clientes/rut/12345678-9
```

### Ver tipos disponibles para el <select> del frontend

```bash
curl http://localhost:8080/api/tipos-cliente
```

### Crear Tipo de Cliente

**Nota**: Los tipos de cliente son un catálogo estático. Para agregar nuevos tipos, modifica directamente la base de datos o los scripts SQL.

```bash
# Ver tipos disponibles para el <select> del frontend
curl http://localhost:8080/api/tipos-cliente
```

### Actualizar un Cliente

```bash
curl -X PUT http://localhost:8080/api/clientes/1 \
  -H "Content-Type: application/json" \
  -d '{
    "rut": "12345678-9",
    "nombre": "Juan Carlos",
    "apellido": "Pérez",
    "edad": 31,
    "email": "juan.perez@email.com",
    "codTipoCliente": "PREM",
    "telefonos": ["912345678", "223456789", "987654321"]
  }'
```

### Eliminar un Cliente

```bash
curl -X DELETE http://localhost:8080/api/clientes/1
```

## Validaciones

- **RUT** debe ser único y obligatorio
- **Email** debe tener formato válido y ser único
- **Nombre y apellido** son obligatorios
- **Edad** debe estar entre 0 y 150 años
- **Código de tipo cliente** es obligatorio y debe existir en tipo_cliente
- **Código de tipo** debe ser único (máximo 10 caracteres)
- **Teléfonos** deben tener formato válido (números, +, -, espacios)
- **Tipo de teléfono** debe ser válido (MOVIL, FIJO, TRABAJO, EMERGENCIA, OTRO)

## Características Técnicas

- **Índice principal por RUT** para búsquedas optimizadas
- **Índices de teléfonos** por cliente y número para consultas rápidas
- **Lombok** para getters, setters y constructores automáticos
- **Solo JSON** en todas las comunicaciones
- **Validación de datos** con anotaciones Bean Validation
- **Manejo de errores** centralizado con ResponseEntity
- **Documentación automática** con Swagger
- **Relaciones JPA** con foreign keys entre entidades
- **Cascada automática** para teléfonos (eliminación y actualización)
- **Base de datos dockerizada** para fácil setup

## Estructura del Proyecto

```
src/
├── main/
│   ├── java/com/example/clientesapi/
│   │   ├── controller/          # API REST Controllers
│   │   │   ├── ClienteController.java
│   │   │   └── TipoClienteController.java
│   │   ├── service/             # Lógica de negocio
│   │   │   ├── ClienteService.java
│   │   │   └── TipoClienteService.java
│   │   ├── repository/          # Acceso a datos JPA
│   │   │   ├── ClienteRepository.java
│   │   │   ├── TelefonoRepository.java
│   │   │   └── TipoClienteRepository.java
│   │   ├── entity/              # Entidades JPA con Lombok
│   │   │   ├── Cliente.java
│   │   │   ├── Telefono.java
│   │   │   └── TipoCliente.java
│   │   ├── dto/                 # DTOs con Lombok
│   │   │   ├── ClienteDTO.java
│   │   │   └── TipoClienteDTO.java
│   │   ├── exception/           # Manejo de excepciones
│   │   ├── config/              # OpenApiConfig - Configuración Swagger
│   │   └── ClientesApiApplication.java
│   └── resources/
│       ├── db/                  # Scripts de base de datos
│       │   ├── setup-complete.sql
│       │   ├── schema.sql
│       │   └── data.sql
│       └── application.properties
└── test/                        # Tests unitarios e integración
```

## Desarrollo

Para contribuir al proyecto:

1. Fork el repositorio
2. Crear una rama para la funcionalidad
3. Hacer commit de los cambios
4. Crear un Pull Request

## Licencia

Este proyecto está bajo la licencia MIT.
