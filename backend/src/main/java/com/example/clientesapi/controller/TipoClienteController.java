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
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/tipos-cliente")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "Tipos de Cliente", description = "API para gestión de tipos de cliente")
public class TipoClienteController {
    
    private final TipoClienteService tipoClienteService;
    
    @GetMapping
    @Operation(summary = "Obtener todos los tipos de cliente", 
               description = "Retorna una lista de todos los tipos de cliente disponibles")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", 
                    description = "Lista de tipos de cliente obtenida exitosamente",
                    content = @Content(mediaType = "application/json",
                                     schema = @Schema(implementation = TipoClienteDTO.class)))
    })
    public ResponseEntity<List<TipoClienteDTO>> getAllTiposCliente() {
        log.info("GET /api/tipos-cliente - Obteniendo todos los tipos de cliente");
        List<TipoClienteDTO> tipos = tipoClienteService.findAll();
        return ResponseEntity.ok(tipos);
    }
    
    @GetMapping("/{codigo}")
    @Operation(summary = "Obtener tipo de cliente por código", 
               description = "Retorna un tipo de cliente específico por su código")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", 
                    description = "Tipo de cliente encontrado",
                    content = @Content(mediaType = "application/json",
                                     schema = @Schema(implementation = TipoClienteDTO.class))),
        @ApiResponse(responseCode = "404", 
                    description = "Tipo de cliente no encontrado")
    })
    public ResponseEntity<TipoClienteDTO> getTipoClienteByCodigo(
            @Parameter(description = "Código del tipo de cliente", required = true)
            @PathVariable String codigo) {
        log.info("GET /api/tipos-cliente/{} - Obteniendo tipo de cliente por código", codigo);
        TipoClienteDTO tipo = tipoClienteService.findById(codigo);
        return ResponseEntity.ok(tipo);
    }
    
    @PostMapping
    @Operation(summary = "Crear nuevo tipo de cliente", 
               description = "Crea un nuevo tipo de cliente")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", 
                    description = "Tipo de cliente creado exitosamente",
                    content = @Content(mediaType = "application/json",
                                     schema = @Schema(implementation = TipoClienteDTO.class))),
        @ApiResponse(responseCode = "400", 
                    description = "Datos de entrada inválidos"),
        @ApiResponse(responseCode = "409", 
                    description = "Tipo de cliente ya existe")
    })
    public ResponseEntity<TipoClienteDTO> createTipoCliente(
            @Parameter(description = "Datos del tipo de cliente a crear", required = true)
            @Valid @RequestBody TipoClienteDTO tipoClienteDTO) {
        log.info("POST /api/tipos-cliente - Creando tipo de cliente: {}", tipoClienteDTO.getCodigo());
        TipoClienteDTO savedTipo = tipoClienteService.save(tipoClienteDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedTipo);
    }
    
    @PutMapping("/{codigo}")
    @Operation(summary = "Actualizar tipo de cliente", 
               description = "Actualiza un tipo de cliente existente")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", 
                    description = "Tipo de cliente actualizado exitosamente",
                    content = @Content(mediaType = "application/json",
                                     schema = @Schema(implementation = TipoClienteDTO.class))),
        @ApiResponse(responseCode = "400", 
                    description = "Datos de entrada inválidos"),
        @ApiResponse(responseCode = "404", 
                    description = "Tipo de cliente no encontrado")
    })
    public ResponseEntity<TipoClienteDTO> updateTipoCliente(
            @Parameter(description = "Código del tipo de cliente", required = true)
            @PathVariable String codigo,
            @Parameter(description = "Datos actualizados del tipo de cliente", required = true)
            @Valid @RequestBody TipoClienteDTO tipoClienteDTO) {
        log.info("PUT /api/tipos-cliente/{} - Actualizando tipo de cliente", codigo);
        TipoClienteDTO updatedTipo = tipoClienteService.update(codigo, tipoClienteDTO);
        return ResponseEntity.ok(updatedTipo);
    }
    
    @DeleteMapping("/{codigo}")
    @Operation(summary = "Eliminar tipo de cliente", 
               description = "Elimina un tipo de cliente por su código")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "204", 
                    description = "Tipo de cliente eliminado exitosamente"),
        @ApiResponse(responseCode = "404", 
                    description = "Tipo de cliente no encontrado")
    })
    public ResponseEntity<Void> deleteTipoCliente(
            @Parameter(description = "Código del tipo de cliente", required = true)
            @PathVariable String codigo) {
        log.info("DELETE /api/tipos-cliente/{} - Eliminando tipo de cliente", codigo);
        tipoClienteService.deleteById(codigo);
        return ResponseEntity.noContent().build();
    }
}
