<!-- Use this file to provide workspace-specific custom instructions to Copilot. For more details, visit https://code.visualstudio.com/docs/copilot/copilot-customization#_use-a-githubcopilotinstructionsmd-file -->

# Proyecto Frontend Angular - Gestión de Clientes

Este es un proyecto Angular 18+ que implementa un CRUD completo para la gestión de clientes.

## Contexto del Proyecto

- **Frontend**: Angular 18+ con componentes standalone
- **Backend**: API REST Spring Boot en http://localhost:8080
- **Estilo**: CSS nativo sin frameworks externos
- **Formularios**: Reactive Forms con validaciones

## Estructura de Datos

### Cliente
- id (opcional para crear)
- rut (obligatorio, único)
- nombre (obligatorio)  
- apellido (obligatorio)
- edad (obligatorio, 0-150)
- email (obligatorio, formato válido)
- codTipoCliente (obligatorio, FK)
- telefonos (array de strings, opcional)

### TipoCliente (catálogo)
- codigo (string, PK)
- descripcion (string)

## Endpoints API Consumidos

- GET /api/clientes - Lista clientes
- GET /api/clientes/{id} - Cliente por ID
- POST /api/clientes - Crear cliente
- PUT /api/clientes/{id} - Actualizar cliente  
- DELETE /api/clientes/{id} - Eliminar cliente
- GET /api/tipos-cliente - Lista tipos cliente

## Convenciones de Código

1. **Componentes**: Usar standalone components
2. **Servicios**: Inyección en root con providedIn
3. **Formularios**: Reactive Forms para todos los formularios
4. **HTTP**: Usar HttpClient con Observable
5. **Estilos**: CSS nativo en styles.css global
6. **Validaciones**: Usar Angular Validators
7. **Routing**: Lazy loading con loadComponent

## Patrones de Desarrollo

- Usar TypeScript estricto
- Manejar errores en todas las llamadas HTTP
- Mostrar estados de carga (loading)
- Validar formularios antes de envío
- Confirmar eliminaciones con confirm()
- Navegación programática con Router
