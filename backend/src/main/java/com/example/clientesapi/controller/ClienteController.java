package com.example.clientesapi.controller;

import com.example.clientesapi.dto.ClienteDTO;
import com.example.clientesapi.service.ClienteService;
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
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/clientes")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "Clientes", description = "API para gestión de clientes")
public class ClienteController {
    
    private final ClienteService clienteService;
    
    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Listar todos los clientes", 
               description = "Retorna una lista de todos los clientes del sistema")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Lista de clientes obtenida exitosamente",
                    content = @Content(mediaType = "application/json", 
                                     schema = @Schema(implementation = ClienteDTO.class)))
    })
    public ResponseEntity<List<ClienteDTO>> getAllClientes() {
        log.info("Solicitando lista de todos los clientes");
        List<ClienteDTO> clientes = clienteService.findAll();
        log.info("Se encontraron {} clientes", clientes.size());
        return ResponseEntity.ok(clientes);
    }
    
    @GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Obtener cliente por ID", 
               description = "Retorna un cliente específico basado en su ID")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Cliente encontrado",
                    content = @Content(mediaType = "application/json", 
                                     schema = @Schema(implementation = ClienteDTO.class))),
        @ApiResponse(responseCode = "404", description = "Cliente no encontrado")
    })
    public ResponseEntity<ClienteDTO> getClienteById(
            @Parameter(description = "ID único del cliente", required = true)
            @PathVariable Long id) {
        log.info("Solicitando cliente con ID: {}", id);
        ClienteDTO cliente = clienteService.findById(id);
        log.info("Cliente encontrado: {} {}", cliente.getNombre(), cliente.getApellido());
        return ResponseEntity.ok(cliente);
    }
    
    @GetMapping(value = "/rut/{rut}", produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Obtener cliente por RUT", 
               description = "Retorna un cliente específico basado en su RUT")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Cliente encontrado",
                    content = @Content(mediaType = "application/json", 
                                     schema = @Schema(implementation = ClienteDTO.class))),
        @ApiResponse(responseCode = "404", description = "Cliente no encontrado")
    })
    public ResponseEntity<ClienteDTO> getClienteByRut(
            @Parameter(description = "RUT del cliente", required = true)
            @PathVariable String rut) {
        log.info("Solicitando cliente con RUT: {}", rut);
        ClienteDTO cliente = clienteService.findByRut(rut);
        log.info("Cliente encontrado con RUT {}: {} {}", rut, cliente.getNombre(), cliente.getApellido());
        return ResponseEntity.ok(cliente);
    }
    
    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Crear nuevo cliente", 
               description = "Crea un nuevo cliente en el sistema")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Cliente creado exitosamente",
                    content = @Content(mediaType = "application/json", 
                                     schema = @Schema(implementation = ClienteDTO.class))),
        @ApiResponse(responseCode = "400", description = "Datos de entrada inválidos"),
        @ApiResponse(responseCode = "409", description = "Ya existe un cliente con ese RUT o email")
    })
    public ResponseEntity<ClienteDTO> createCliente(
            @Parameter(description = "Datos del cliente a crear", required = true)
            @Valid @RequestBody ClienteDTO clienteDTO) {
        log.info("Creando nuevo cliente con RUT: {} y email: {}", clienteDTO.getRut(), clienteDTO.getEmail());
        ClienteDTO createdCliente = clienteService.create(clienteDTO);
        log.info("Cliente creado exitosamente con ID: {}", createdCliente.getId());
        return new ResponseEntity<>(createdCliente, HttpStatus.CREATED);
    }
    
    @PutMapping(value = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Actualizar cliente", 
               description = "Actualiza los datos de un cliente existente")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Cliente actualizado exitosamente",
                    content = @Content(mediaType = "application/json", 
                                     schema = @Schema(implementation = ClienteDTO.class))),
        @ApiResponse(responseCode = "400", description = "Datos de entrada inválidos"),
        @ApiResponse(responseCode = "404", description = "Cliente no encontrado"),
        @ApiResponse(responseCode = "409", description = "Ya existe otro cliente con ese RUT o email")
    })
    public ResponseEntity<ClienteDTO> updateCliente(
            @Parameter(description = "ID del cliente a actualizar", required = true)
            @PathVariable Long id,
            @Parameter(description = "Datos actualizados del cliente", required = true)
            @Valid @RequestBody ClienteDTO clienteDTO) {
        log.info("Actualizando cliente con ID: {}", id);
        ClienteDTO updatedCliente = clienteService.update(id, clienteDTO);
        log.info("Cliente actualizado exitosamente con ID: {}", updatedCliente.getId());
        return ResponseEntity.ok(updatedCliente);
    }
    
    @DeleteMapping("/{id}")
    @Operation(summary = "Eliminar cliente", 
               description = "Elimina un cliente del sistema")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "204", description = "Cliente eliminado exitosamente"),
        @ApiResponse(responseCode = "404", description = "Cliente no encontrado")
    })
    public ResponseEntity<Void> deleteCliente(
            @Parameter(description = "ID del cliente a eliminar", required = true)
            @PathVariable Long id) {
        log.info("Eliminando cliente con ID: {}", id);
        clienteService.delete(id);
        log.info("Cliente eliminado exitosamente con ID: {}", id);
        return ResponseEntity.noContent().build();
    }
    
}
