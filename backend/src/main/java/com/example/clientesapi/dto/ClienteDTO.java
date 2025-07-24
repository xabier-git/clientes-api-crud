package com.example.clientesapi.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Schema(description = "DTO para crear/actualizar cliente")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ClienteDTO {
    
    @Schema(description = "ID único del cliente (solo lectura)", example = "1", accessMode = Schema.AccessMode.READ_ONLY)
    private Long id;
    
    @Schema(description = "RUT del cliente", example = "12.345.678-9", required = true)
    @NotBlank(message = "El RUT es obligatorio")
    @Size(max = 12, message = "El RUT no puede tener más de 12 caracteres")
    private String rut;
    
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
    
}
