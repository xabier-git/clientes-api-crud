package com.example.clientesapi.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;

@Schema(description = "DTO para crear/actualizar cliente")
public class ClienteDTO {
    
    @Schema(description = "ID único del cliente (solo lectura)", example = "1", accessMode = Schema.AccessMode.READ_ONLY)
    private Long id;
    
    @Schema(description = "Nombre del cliente", example = "Juan", required = true)
    @NotBlank(message = "El nombre es obligatorio")
    @Size(max = 50, message = "El nombre no puede tener más de 50 caracteres")
    private String nombre;
    
    @Schema(description = "Apellido del cliente", example = "Pérez", required = true)
    @NotBlank(message = "El apellido es obligatorio")
    @Size(max = 50, message = "El apellido no puede tener más de 50 caracteres")
    private String apellido;
    
    @Schema(description = "Edad del cliente", example = "30")
    @Min(value = 0, message = "La edad debe ser mayor o igual a 0")
    @Max(value = 150, message = "La edad debe ser menor o igual a 150")
    private Integer edad;
    
    @Schema(description = "Email del cliente", example = "juan.perez@email.com", required = true)
    @NotBlank(message = "El email es obligatorio")
    @Email(message = "El formato del email no es válido")
    @Size(max = 100, message = "El email no puede tener más de 100 caracteres")
    private String email;
    
    @Schema(description = "Código del tipo de cliente", example = "VIP", required = true)
    @NotBlank(message = "El código del tipo de cliente es obligatorio")
    @Size(max = 10, message = "El código del tipo de cliente no puede tener más de 10 caracteres")
    private String codTipoCliente;
    
    @Schema(description = "Información del tipo de cliente", accessMode = Schema.AccessMode.READ_ONLY)
    private TipoClienteDTO tipoCliente;
    
    // Constructores
    public ClienteDTO() {}
    
    public ClienteDTO(String nombre, String apellido, Integer edad, String email, String codTipoCliente) {
        this.nombre = nombre;
        this.apellido = apellido;
        this.edad = edad;
        this.email = email;
        this.codTipoCliente = codTipoCliente;
    }
    
    // Getters y Setters
    public Long getId() {
        return id;
    }
    
    public void setId(Long id) {
        this.id = id;
    }
    
    public String getNombre() {
        return nombre;
    }
    
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }
    
    public String getApellido() {
        return apellido;
    }
    
    public void setApellido(String apellido) {
        this.apellido = apellido;
    }
    
    public Integer getEdad() {
        return edad;
    }
    
    public void setEdad(Integer edad) {
        this.edad = edad;
    }
    
    public String getEmail() {
        return email;
    }
    
    public void setEmail(String email) {
        this.email = email;
    }
    
    public String getCodTipoCliente() {
        return codTipoCliente;
    }
    
    public void setCodTipoCliente(String codTipoCliente) {
        this.codTipoCliente = codTipoCliente;
    }
    
    public TipoClienteDTO getTipoCliente() {
        return tipoCliente;
    }
    
    public void setTipoCliente(TipoClienteDTO tipoCliente) {
        this.tipoCliente = tipoCliente;
    }
}
