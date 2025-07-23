package com.example.clientesapi.controller;

import com.example.clientesapi.dto.TipoClienteDTO;
import com.example.clientesapi.service.TipoClienteService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/tipos-cliente")
@Tag(name = "Tipos de Cliente", description = "API para gestión de tipos de cliente")
public class TipoClienteController {
    
    @Autowired
    private TipoClienteService tipoClienteService;
    
    @GetMapping
    @Operation(summary = "Obtener todos los tipos de cliente", 
               description = "Retorna una lista de todos los tipos de cliente disponibles")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Lista de tipos de cliente obtenida exitosamente",
                    content = @Content(mediaType = "application/json", 
                                     schema = @Schema(implementation = TipoClienteDTO.class)))
    })
    public ResponseEntity<List<TipoClienteDTO>> getAllTiposCliente() {
        List<TipoClienteDTO> tiposCliente = tipoClienteService.findAll();
        return ResponseEntity.ok(tiposCliente);
    }
    
    @GetMapping("/{codigo}")
    @Operation(summary = "Obtener tipo de cliente por código", 
               description = "Retorna un tipo de cliente específico basado en su código")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Tipo de cliente encontrado",
                    content = @Content(mediaType = "application/json", 
                                     schema = @Schema(implementation = TipoClienteDTO.class))),
        @ApiResponse(responseCode = "404", description = "Tipo de cliente no encontrado")
    })
    public ResponseEntity<TipoClienteDTO> getTipoClienteById(
            @Parameter(description = "Código único del tipo de cliente", required = true)
            @PathVariable String codigo) {
        TipoClienteDTO tipoCliente = tipoClienteService.findById(codigo);
        return ResponseEntity.ok(tipoCliente);
    }
    
    @PostMapping
    @Operation(summary = "Crear nuevo tipo de cliente", 
               description = "Crea un nuevo tipo de cliente en el sistema")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Tipo de cliente creado exitosamente",
                    content = @Content(mediaType = "application/json", 
                                     schema = @Schema(implementation = TipoClienteDTO.class))),
        @ApiResponse(responseCode = "400", description = "Datos de entrada inválidos"),
        @ApiResponse(responseCode = "409", description = "Ya existe un tipo de cliente con ese código")
    })
    public ResponseEntity<TipoClienteDTO> createTipoCliente(
            @Parameter(description = "Datos del tipo de cliente a crear", required = true)
            @Valid @RequestBody TipoClienteDTO tipoClienteDTO) {
        TipoClienteDTO createdTipoCliente = tipoClienteService.create(tipoClienteDTO);
        return new ResponseEntity<>(createdTipoCliente, HttpStatus.CREATED);
    }
    
    @PutMapping("/{codigo}")
    @Operation(summary = "Actualizar tipo de cliente", 
               description = "Actualiza los datos de un tipo de cliente existente")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Tipo de cliente actualizado exitosamente",
                    content = @Content(mediaType = "application/json", 
                                     schema = @Schema(implementation = TipoClienteDTO.class))),
        @ApiResponse(responseCode = "400", description = "Datos de entrada inválidos"),
        @ApiResponse(responseCode = "404", description = "Tipo de cliente no encontrado")
    })
    public ResponseEntity<TipoClienteDTO> updateTipoCliente(
            @Parameter(description = "Código único del tipo de cliente", required = true)
            @PathVariable String codigo,
            @Parameter(description = "Datos actualizados del tipo de cliente", required = true)
            @Valid @RequestBody TipoClienteDTO tipoClienteDTO) {
        TipoClienteDTO updatedTipoCliente = tipoClienteService.update(codigo, tipoClienteDTO);
        return ResponseEntity.ok(updatedTipoCliente);
    }
    
    @DeleteMapping("/{codigo}")
    @Operation(summary = "Eliminar tipo de cliente", 
               description = "Elimina un tipo de cliente del sistema")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "204", description = "Tipo de cliente eliminado exitosamente"),
        @ApiResponse(responseCode = "404", description = "Tipo de cliente no encontrado")
    })
    public ResponseEntity<Void> deleteTipoCliente(
            @Parameter(description = "Código único del tipo de cliente", required = true)
            @PathVariable String codigo) {
        tipoClienteService.delete(codigo);
        return ResponseEntity.noContent().build();
    }
    
}
