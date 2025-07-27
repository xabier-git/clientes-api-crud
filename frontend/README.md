# Clientes Frontend - Angular 18+

Frontend desarrollado en Angular 18+ para el sistema de gestión de clientes.

## Características

- **CRUD completo** de clientes
- **Formularios reactivos** con validaciones
- **Lista paginada** de clientes 
- **Detalle** de cliente
- **Select dinámico** para tipos de cliente
- **Gestión de múltiples teléfonos** por cliente
- **Responsive design** con CSS nativo

## Tecnologías Utilizadas

- **Angular 18+**
- **TypeScript 5.4+**
- **Reactive Forms**
- **HttpClient** para consumir API REST
- **Standalone Components**
- **CSS nativo** (sin frameworks)

## API Backend

Este frontend consume la API REST que debe estar ejecutándose en:
- **URL Base**: `http://localhost:8080`
- **Documentación**: http://localhost:8080/swagger-ui.html

### Endpoints consumidos:
- `GET /api/clientes` - Lista de clientes
- `GET /api/clientes/{id}` - Cliente por ID
- `POST /api/clientes` - Crear cliente
- `PUT /api/clientes/{id}` - Actualizar cliente
- `DELETE /api/clientes/{id}` - Eliminar cliente
- `GET /api/tipos-cliente` - Lista tipos de cliente

## Instalación y Ejecución

### Prerrequisitos
- Node.js 18+
- npm 9+
- Backend API ejecutándose en puerto 8080

### Comandos

```bash
# Instalar dependencias
npm install

# Ejecutar en modo desarrollo
npm start

# Compilar para producción
npm run build

# Ejecutar tests
npm test
```

## Estructura del Proyecto

```
src/
├── app/
│   ├── components/           # Componentes Angular
│   │   ├── cliente-list/    # Lista de clientes
│   │   ├── cliente-form/    # Formulario crear/editar
│   │   └── cliente-detail/  # Detalle de cliente
│   ├── models/              # Interfaces TypeScript
│   │   └── cliente.model.ts # Cliente y TipoCliente
│   ├── services/            # Servicios HTTP
│   │   ├── cliente.service.ts
│   │   └── tipo-cliente.service.ts
│   ├── app.component.ts     # Componente principal
│   ├── app.config.ts        # Configuración app
│   └── app.routes.ts        # Rutas
├── index.html               # HTML principal
├── main.ts                  # Bootstrap application
└── styles.css               # Estilos globales
```

## Funcionalidades

### Lista de Clientes
- Visualización de todos los clientes en tabla
- Navegación a detalle, edición y eliminación
- Mensaje cuando no hay clientes

### Formulario de Cliente
- Validaciones en tiempo real
- Select dinámico para tipos de cliente
- Gestión de múltiples teléfonos
- Modo crear y editar

### Detalle de Cliente
- Vista completa de información del cliente
- Navegación a edición
- Listado de teléfonos

## Modelo de Datos

### Cliente
```typescript
interface Cliente {
  id?: number;
  rut: string;
  nombre: string;
  apellido: string;
  edad: number;
  email: string;
  codTipoCliente: string;
  telefonos: string[];
  tipoCliente?: TipoCliente;
}
```

### TipoCliente
```typescript
interface TipoCliente {
  codigo: string;
  descripcion: string;
}
```

## Validaciones

- **RUT**: Obligatorio
- **Nombre**: Obligatorio
- **Apellido**: Obligatorio
- **Edad**: Obligatorio, entre 0 y 150
- **Email**: Obligatorio, formato válido
- **Tipo Cliente**: Obligatorio, debe existir en catálogo
- **Teléfonos**: Opcional, permite múltiples números

## Rutas

- `/clientes` - Lista de clientes
- `/clientes/nuevo` - Crear cliente
- `/clientes/editar/:id` - Editar cliente
- `/clientes/detalle/:id` - Ver detalle

## Desarrollo

### Agregar nuevos componentes
```bash
ng generate component components/nuevo-componente --standalone
```

### Agregar nuevos servicios
```bash
ng generate service services/nuevo-servicio
```

## Configuración de Proxy (Configurado)

El proyecto ya incluye configuración de proxy para evitar problemas de CORS:

```json
// proxy.conf.json (ya configurado)
{
  "/api/**": {
    "target": "http://localhost:8080",
    "secure": false,
    "changeOrigin": true,
    "logLevel": "debug"
  }
}
```

**El comando `npm start` ya usa automáticamente esta configuración.**

### Uso del Proxy:
- **Frontend**: `http://localhost:4200`
- **Peticiones API**: `/api/clientes` → `http://localhost:8080/api/clientes`
- **Sin CORS**: El proxy maneja la comunicación con el backend

### Backend CORS:
El backend también tiene CORS configurado para permitir peticiones desde:
- `http://localhost:4200`
- `http://127.0.0.1:4200`
- `http://localhost:8080`
