package com.example.clientesapi.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Schema(description = "DTO para Tipo de Cliente")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class TipoClienteDTO {
    
    @Schema(description = "Código del tipo de cliente", example = "P", required = true)
    @NotBlank(message = "El código es obligatorio")
    @Size(max = 5, message = "El código no puede tener más de 5 caracteres")
    private String codigo;
    
    @Schema(description = "Descripción del tipo de cliente", example = "Persona", required = true)
    @NotBlank(message = "La descripción es obligatoria")
    @Size(max = 100, message = "La descripción no puede tener más de 100 caracteres")
    private String descripcion;
}