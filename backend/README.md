# API de Gestión de Clientes

Una API REST completa para la gestión de clientes desarrollada con Spring Boot, JPA/Hibernate y MySQL.

## Características

- **CRUD completo** para clientes y tipos de cliente
- **Validaciones de datos** con Bean Validation
- **Relaciones JPA** entre entidades
- **Paginación y filtros** en las consultas
- **Documentación API** con Swagger/OpenAPI 3
- **Manejo de errores** centralizado
- **Base de datos MySQL** con índices optimizados

## Tecnologías Utilizadas

- **Java 17**
- **Spring Boot 3.1.0**
- **Spring Data JPA**
- **Hibernate**
- **MySQL 8.0+**
- **Swagger/OpenAPI 3**
- **Maven**

## Estructura del Proyecto

```
src/
├── main/
│   ├── java/com/example/clientesapi/
│   │   ├── controller/          # Controladores REST
│   │   ├── service/             # Lógica de negocio
│   │   ├── repository/          # Acceso a datos (JPA)
│   │   ├── entity/              # Entidades JPA
│   │   ├── dto/                 # DTOs para transferencia de datos
│   │   ├── mapper/              # Mapeo entre entidades y DTOs
│   │   ├── exception/           # Manejo de excepciones
│   │   ├── config/              # Configuraciones
│   │   └── ClientesApiApplication.java
│   └── resources/
│       ├── db/                  # Scripts de base de datos
│       └── application.properties
└── test/                        # Tests unitarios e integración
```

## Configuración de Base de Datos

### 1. Crear la base de datos

```sql
-- Ejecutar el script completo
mysql -u root -p < src/main/resources/db/setup-complete.sql
```

### 2. Configurar conexión

Editar `application.properties`:

```properties
spring.datasource.url=jdbc:mysql://localhost:3306/clientes_db
spring.datasource.username=root
spring.datasource.password=tu_password
```

## Ejecutar la Aplicación

### Prerrequisitos

- JDK 17+
- Maven 3.6+
- MySQL 8.0+

### Comandos

```bash
# Compilar el proyecto
mvn clean compile

# Ejecutar tests
mvn test

# Ejecutar la aplicación
mvn spring-boot:run

# Crear JAR ejecutable
mvn clean package
java -jar target/clientes-api-0.0.1-SNAPSHOT.jar
```

## Documentación de la API

Una vez ejecutada la aplicación, la documentación estará disponible en:

- **Swagger UI**: http://localhost:8080/swagger-ui.html
- **OpenAPI JSON**: http://localhost:8080/api-docs

## Endpoints Principales

### Tipos de Cliente

- `GET /api/tipos-cliente` - Listar todos los tipos
- `GET /api/tipos-cliente/{codigo}` - Obtener por código
- `POST /api/tipos-cliente` - Crear nuevo tipo
- `PUT /api/tipos-cliente/{codigo}` - Actualizar tipo
- `DELETE /api/tipos-cliente/{codigo}` - Eliminar tipo

### Clientes

- `GET /api/clientes` - Listar todos los clientes
- `GET /api/clientes/search` - Buscar con filtros y paginación
- `GET /api/clientes/{id}` - Obtener por ID
- `GET /api/clientes/email/{email}` - Obtener por email
- `GET /api/clientes/tipo/{codigo}` - Obtener por tipo
- `POST /api/clientes` - Crear nuevo cliente
- `PUT /api/clientes/{id}` - Actualizar cliente
- `DELETE /api/clientes/{id}` - Eliminar cliente

## Ejemplos de Uso

### Crear un Tipo de Cliente

```bash
curl -X POST http://localhost:8080/api/tipos-cliente \
  -H "Content-Type: application/json" \
  -d '{
    "codigo": "GOLD",
    "descripcion": "Cliente Gold - Beneficios premium"
  }'
```

### Crear un Cliente

```bash
curl -X POST http://localhost:8080/api/clientes \
  -H "Content-Type: application/json" \
  -d '{
    "nombre": "Juan",
    "apellido": "Pérez",
    "edad": 30,
    "email": "juan.perez@email.com",
    "codTipoCliente": "VIP"
  }'
```

### Buscar Clientes con Filtros

```bash
curl "http://localhost:8080/api/clientes/search?nombre=Juan&page=0&size=10&sortBy=apellido&sortDir=asc"
```

## Modelo de Datos

### TipoCliente
- `codigo` (String, PK): Código único del tipo
- `descripcion` (String): Descripción del tipo

### Cliente
- `id` (Long, PK): ID autoincremental
- `nombre` (String): Nombre del cliente
- `apellido` (String): Apellido del cliente
- `edad` (Integer): Edad del cliente
- `email` (String, Unique): Email único
- `codTipoCliente` (String, FK): Referencia a TipoCliente

## Validaciones

- Email debe tener formato válido y ser único
- Nombre y apellido son obligatorios
- Edad debe estar entre 0 y 150 años
- Código de tipo cliente debe existir

## Características Técnicas

- **Índices de base de datos** para optimizar consultas
- **Paginación** automática con Spring Data
- **Filtros dinámicos** en búsquedas
- **Validación de datos** con anotaciones
- **Manejo de errores** centralizado con ResponseEntity
- **Documentación automática** con Swagger

## Desarrollo

Para contribuir al proyecto:

1. Fork el repositorio
2. Crear una rama para la funcionalidad
3. Hacer commit de los cambios
4. Crear un Pull Request

## Licencia

Este proyecto está bajo la licencia MIT.
