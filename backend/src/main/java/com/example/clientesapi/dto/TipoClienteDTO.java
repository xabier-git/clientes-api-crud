package com.example.clientesapi.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;

@Schema(description = "DTO para crear/actualizar tipo de cliente")
public class TipoClienteDTO {
    
    @Schema(description = "Código único del tipo de cliente", example = "VIP", required = true)
    @NotBlank(message = "El código es obligatorio")
    @Size(max = 10, message = "El código no puede tener más de 10 caracteres")
    private String codigo;
    
    @Schema(description = "Descripción del tipo de cliente", example = "Cliente VIP", required = true)
    @NotBlank(message = "La descripción es obligatoria")
    @Size(max = 100, message = "La descripción no puede tener más de 100 caracteres")
    private String descripcion;
    
    // Constructores
    public TipoClienteDTO() {}
    
    public TipoClienteDTO(String codigo, String descripcion) {
        this.codigo = codigo;
        this.descripcion = descripcion;
    }
    
    // Getters y Setters
    public String getCodigo() {
        return codigo;
    }
    
    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }
    
    public String getDescripcion() {
        return descripcion;
    }
    
    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }
}
