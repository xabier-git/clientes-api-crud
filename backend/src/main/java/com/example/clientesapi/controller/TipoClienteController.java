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
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/tipos-cliente")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "Tipos de Cliente", description = "API de consulta para catálogo de tipos de cliente (solo lectura)")
public class TipoClienteController {
    
    private final TipoClienteService tipoClienteService;
    
    @GetMapping
    @Operation(summary = "Obtener todos los tipos de cliente", 
               description = "Retorna una lista de todos los tipos de cliente disponibles para llenar un <select>")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", 
                    description = "Lista de tipos de cliente obtenida exitosamente",
                    content = @Content(mediaType = "application/json",
                                     schema = @Schema(implementation = TipoClienteDTO.class)))
    })
    public ResponseEntity<List<TipoClienteDTO>> getAllTiposCliente() {
        log.info("GET /api/tipos-cliente - Obteniendo todos los tipos de cliente para catálogo");
        List<TipoClienteDTO> tipos = tipoClienteService.findAll();
        log.info("Se encontraron {} tipos de cliente", tipos.size());
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
        log.info("Tipo de cliente encontrado: {} - {}", tipo.getCodigo(), tipo.getDescripcion());
        return ResponseEntity.ok(tipo);
    }
    
}