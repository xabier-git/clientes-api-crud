# Solución Temporal para CORS

## ⚠️ Problema Actual:
El backend Spring Boot no tiene CORS configurado para permitir peticiones desde `http://127.0.0.1:4200`

## 🔧 Solución 1: Configurar CORS en el Backend (RECOMENDADO)

### En el backend Spring Boot, agrega esta configuración:

**Opción A: Clase de Configuración Global**
```java
// src/main/java/com/example/clientesapi/config/CorsConfig.java
package com.example.clientesapi.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

@Configuration
public class CorsConfig {

    @Bean
    public CorsFilter corsFilter() {
        CorsConfiguration corsConfiguration = new CorsConfiguration();
        corsConfiguration.setAllowCredentials(true);
        corsConfiguration.addAllowedOriginPattern("*");
        corsConfiguration.addAllowedHeader("*");
        corsConfiguration.addAllowedMethod("*");

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", corsConfiguration);
        return new CorsFilter(source);
    }
}
```

**Opción B: Anotación en Controllers**
```java
// En ClienteController.java y TipoClienteController.java
@CrossOrigin(origins = {"http://localhost:4200", "http://127.0.0.1:4200"})
@RestController
@RequestMapping("/api")
public class ClienteController {
    // ... resto del código
}
```

**Opción C: Properties (más simple)**
```properties
# En application.properties
spring.web.cors.allowed-origins=http://localhost:4200,http://127.0.0.1:4200
spring.web.cors.allowed-methods=GET,POST,PUT,DELETE,OPTIONS
spring.web.cors.allowed-headers=*
spring.web.cors.allow-credentials=true
```

## 🚀 Solución 2: Usar Chrome sin seguridad (SOLO PARA DESARROLLO)

### Comando para Windows:
```cmd
chrome.exe --user-data-dir="C:/Chrome dev session" --disable-web-security --disable-features=VizDisplayCompositor
```

### Comando para Linux:
```bash
google-chrome --user-data-dir="/tmp/chrome_dev_session" --disable-web-security --disable-features=VizDisplayCompositor
```

### Comando para macOS:
```bash
open -n -a /Applications/Google\ Chrome.app/Contents/MacOS/Google\ Chrome --args --user-data-dir="/tmp/chrome_dev_session" --disable-web-security
```

## 🔄 Después de configurar CORS en el backend:
1. Reinicia el backend Spring Boot
2. Recarga la página del frontend
3. Todas las funcionalidades deberían funcionar

## ✅ Para verificar que funciona:
- Lista de clientes: ✅
- Ver detalle: ✅
- Crear cliente: ✅
- Editar cliente: ✅
- Eliminar cliente: ✅
