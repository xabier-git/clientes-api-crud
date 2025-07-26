# Solución de Problemas CORS

## ✅ Cambios Realizados:

1. **Configuración de Proxy actualizada** (`proxy.conf.json`)
   - URLs relativas en servicios (`/api/clientes` en lugar de `http://localhost:8080/api/clientes`)
   - Headers CORS añadidos al proxy
   - Configuración mejorada de proxy

2. **Servicios actualizados**:
   - `ClienteService`: Usa `/api/clientes`
   - `TipoClienteService`: Usa `/api/tipos-cliente`

3. **Interceptor de errores HTTP creado** para mejor manejo de errores

## 🔧 Para que funcione completamente:

### 1. Backend debe estar ejecutándose
```bash
cd ../backend
./run.sh
# O manualmente:
mvn spring-boot:run
```

### 2. Verificar que el backend tenga CORS configurado
El backend Spring Boot debe tener esta configuración en algún archivo de configuración:

```java
@Configuration
@EnableWebMvc
public class WebConfig implements WebMvcConfigurer {
    
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/api/**")
                .allowedOrigins("http://localhost:4200", "http://127.0.0.1:4200")
                .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")
                .allowedHeaders("*")
                .allowCredentials(true);
    }
}
```

### 3. O agregar anotación en controllers:
```java
@CrossOrigin(origins = {"http://localhost:4200", "http://127.0.0.1:4200"})
@RestController
@RequestMapping("/api")
public class ClienteController {
    // ... código del controller
}
```

## 🚀 Estado Actual:

- ✅ **Frontend**: Ejecutándose en http://localhost:4200
- ✅ **Proxy configurado**: Redirige `/api/*` a `http://localhost:8080`
- ✅ **URLs relativas**: Evita problemas de CORS directo
- ⚠️ **Backend**: Debe estar ejecutándose en puerto 8080

## 🧪 Para probar:

1. **Verificar backend**: http://localhost:8080/api/clientes
2. **Acceder frontend**: http://localhost:4200
3. **Verificar proxy**: Las peticiones a `/api/*` se redirigen automáticamente

Si aún hay problemas de CORS, es porque el backend no está configurado correctamente para permitir peticiones desde el frontend.
