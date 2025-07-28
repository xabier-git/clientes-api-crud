# 🏢 Sistema de Gestión de Clientes - Full Stack

Una aplicación full-stack completa para la gestión de clientes desarrollada con **Spring Boot** (backend) y **Angular 18+** (frontend).

## 📋 Descripción del Proyecto

Sistema CRUD completo que permite:
- ✅ **Gestión de clientes** con información completa
- ✅ **Múltiples teléfonos** por cliente
- ✅ **Tipos de cliente** como catálogo
- ✅ **Búsqueda por RUT** como identificador único
- ✅ **API REST documentada** con Swagger
- ✅ **Frontend responsive** con Angular

## 🏗️ Arquitectura

```
clientes-api-crud/
├── backend/          # API REST - Spring Boot + MySQL
└── frontend/         # SPA - Angular 18+ + TypeScript
```

- **Backend**: Spring Boot 3.1 + Java 17 + MySQL + Swagger
- **Frontend**: Angular 18+ + TypeScript + CSS nativo
- **Comunicación**: API REST con CORS configurado

## 🚀 Inicio Rápido

### 📋 Prerrequisitos

- **Java 17+**
- **Maven 3.6+**
- **Node.js 18+**
- **npm 9+**
- **MySQL 8.0+** (o Docker)

### 🔧 Instalación y Ejecución

#### 1️⃣ **Clonar el repositorio**
```bash
git clone https://github.com/xabier-git/clientes-api-crud.git
cd clientes-api-crud
```

#### 2️⃣ **Configurar Base de Datos**

**Opción A: Con Docker (Recomendado)**
```bash
# Desde la raíz del proyecto
cd backend
./quick-setup.sh
```

**Opción B: MySQL Local**
```bash
# Crear base de datos
mysql -u root -p
CREATE DATABASE clientes_db;
exit

# Ejecutar scripts
cd backend
mysql -u root -p clientes_db < src/main/resources/db/setup-complete.sql
```

#### 3️⃣ **Ejecutar Backend (Puerto 8080)**
```bash
cd backend

# Opción A: Con script
./run.sh

# Opción B: Con Maven
mvn spring-boot:run
```

**✅ Verificar backend**: [http://localhost:8080/swagger-ui.html](http://localhost:8080/swagger-ui.html)

#### 4️⃣ **Ejecutar Frontend (Puerto 4200)**
```bash
# En otra terminal
cd frontend

# Instalar dependencias
npm install

# Ejecutar aplicación
npm start
```

**✅ Acceder a la aplicación**: [http://localhost:4200](http://localhost:4200)

## 🌐 URLs de Acceso

| Servicio | URL | Descripción |
|----------|-----|-------------|
| **Frontend** | http://localhost:4200 | Aplicación Angular |
| **Backend API** | http://localhost:8080/api | API REST |
| **Swagger UI** | http://localhost:8080/swagger-ui.html | Documentación API |
| **API Docs** | http://localhost:8080/api-docs | OpenAPI JSON |

## 📁 Estructura del Proyecto

### Backend (`./backend/`)
```
backend/
├── src/main/java/com/example/clientesapi/
│   ├── config/          # Configuraciones (CORS, Swagger)
│   ├── controller/      # REST Controllers
│   ├── dto/             # Data Transfer Objects
│   ├── entity/          # Entidades JPA
│   ├── service/         # Lógica de negocio
│   └── repository/      # Acceso a datos
├── src/main/resources/
│   ├── db/              # Scripts SQL
│   └── application.properties
└── pom.xml              # Dependencias Maven
```

### Frontend (`./frontend/`)
```
frontend/
├── src/app/
│   ├── components/      # Componentes Angular
│   ├── services/        # Servicios HTTP
│   ├── models/          # Interfaces TypeScript
│   └── interceptors/    # Interceptores HTTP
├── proxy.conf.json      # Configuración proxy
└── package.json         # Dependencias npm
```

## 🛠️ Scripts Útiles

### Backend
```bash
cd backend

# Ejecutar aplicación
./run.sh

# Verificar estado
./status.sh

# Detener aplicación
./stop.sh

# Ver logs
tail -f logs/clientes-api.log
```

### Frontend
```bash
cd frontend

# Modo desarrollo
npm start

# Compilar para producción
npm run build

# Ejecutar tests
npm test

# Ejecutar sin proxy
npm run start-no-proxy
```

## 🔗 API Endpoints Principales

| Método | Endpoint | Descripción |
|--------|----------|-------------|
| `GET` | `/api/clientes` | Listar todos los clientes |
| `GET` | `/api/clientes/{id}` | Obtener cliente por ID |
| `GET` | `/api/clientes/rut/{rut}` | Buscar cliente por RUT |
| `POST` | `/api/clientes` | Crear nuevo cliente |
| `PUT` | `/api/clientes/{id}` | Actualizar cliente |
| `DELETE` | `/api/clientes/{id}` | Eliminar cliente |
| `GET` | `/api/tipos-cliente` | Listar tipos de cliente |

## 🔧 Configuraciones

### CORS
- **API REST**: Restrictiva - Solo frontend autorizado
- **Swagger**: Permisiva - Acceso global para documentación
- **Configuración**: `backend/src/main/java/.../config/CorsConfig.java`

### Proxy Frontend
- **Configurado automáticamente** en `frontend/proxy.conf.json`
- **Redirige**: `/api/**` → `http://localhost:8080`
- **Sin problemas CORS** durante desarrollo

### Base de Datos
- **Motor**: MySQL 8.0+
- **Base de datos**: `clientes_db`
- **Usuario**: `root` / **Password**: `root123`
- **Scripts**: `backend/src/main/resources/db/`

## 🐛 Troubleshooting

### Backend no inicia
```bash
# Verificar Java
java -version

# Verificar MySQL
mysql -u root -p -e "SHOW DATABASES;"

# Ver logs
cd backend && tail -f logs/clientes-api.log
```

### Frontend no conecta
```bash
# Verificar backend está corriendo
curl http://localhost:8080/api/clientes

# Verificar proxy
cat frontend/proxy.conf.json

# Limpiar caché
cd frontend && rm -rf node_modules && npm install
```

## 📖 Documentación Detallada

- **Backend**: Ver [./backend/README.md](./backend/README.md)
- **Frontend**: Ver [./frontend/README.md](./frontend/README.md)
- **Scripts SQL**: Ver [./backend/SCRIPTS-GUIDE.md](./backend/SCRIPTS-GUIDE.md)

## 🤝 Contribuir

1. Fork el repositorio
2. Crear rama para funcionalidad (`git checkout -b feature/nueva-funcionalidad`)
3. Commit cambios (`git commit -am 'Agregar nueva funcionalidad'`)
4. Push a la rama (`git push origin feature/nueva-funcionalidad`)
5. Crear Pull Request

## 📄 Licencia

Este proyecto está bajo la licencia MIT.

---

**🔗 Repositorio**: https://github.com/xabier-git/clientes-api-crud