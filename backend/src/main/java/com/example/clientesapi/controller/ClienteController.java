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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/clientes")
@Tag(name = "Clientes", description = "API para gestión de clientes")
public class ClienteController {
    
    @Autowired
    private ClienteService clienteService;
    
    @GetMapping
    @Operation(summary = "Obtener todos los clientes", 
               description = "Retorna una lista de todos los clientes del sistema")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Lista de clientes obtenida exitosamente",
                    content = @Content(mediaType = "application/json", 
                                     schema = @Schema(implementation = ClienteDTO.class)))
    })
    public ResponseEntity<List<ClienteDTO>> getAllClientes() {
        List<ClienteDTO> clientes = clienteService.findAll();
        return ResponseEntity.ok(clientes);
    }
    
    @GetMapping("/search")
    @Operation(summary = "Buscar clientes con filtros y paginación", 
               description = "Busca clientes aplicando filtros opcionales y paginación")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Búsqueda realizada exitosamente",
                    content = @Content(mediaType = "application/json"))
    })
    public ResponseEntity<Page<ClienteDTO>> searchClientes(
            @Parameter(description = "Filtro por nombre (búsqueda parcial)")
            @RequestParam(required = false) String nombre,
            @Parameter(description = "Filtro por apellido (búsqueda parcial)")
            @RequestParam(required = false) String apellido,
            @Parameter(description = "Filtro por email (búsqueda parcial)")
            @RequestParam(required = false) String email,
            @Parameter(description = "Filtro por código de tipo de cliente (búsqueda exacta)")
            @RequestParam(required = false) String codTipoCliente,
            @Parameter(description = "Número de página (empezando desde 0)")
            @RequestParam(defaultValue = "0") int page,
            @Parameter(description = "Tamaño de página")
            @RequestParam(defaultValue = "10") int size,
            @Parameter(description = "Campo por el cual ordenar")
            @RequestParam(defaultValue = "id") String sortBy,
            @Parameter(description = "Dirección del ordenamiento (asc/desc)")
            @RequestParam(defaultValue = "asc") String sortDir) {
        
        Sort sort = sortDir.equalsIgnoreCase("desc") ? 
                    Sort.by(sortBy).descending() : 
                    Sort.by(sortBy).ascending();
        
        Pageable pageable = PageRequest.of(page, size, sort);
        Page<ClienteDTO> clientesPage = clienteService.findByFilters(nombre, apellido, email, codTipoCliente, pageable);
        return ResponseEntity.ok(clientesPage);
    }
    
    @GetMapping("/{id}")
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
        ClienteDTO cliente = clienteService.findById(id);
        return ResponseEntity.ok(cliente);
    }
    
    @GetMapping("/email/{email}")
    @Operation(summary = "Obtener cliente por email", 
               description = "Retorna un cliente específico basado en su email")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Cliente encontrado",
                    content = @Content(mediaType = "application/json", 
                                     schema = @Schema(implementation = ClienteDTO.class))),
        @ApiResponse(responseCode = "404", description = "Cliente no encontrado")
    })
    public ResponseEntity<ClienteDTO> getClienteByEmail(
            @Parameter(description = "Email del cliente", required = true)
            @PathVariable String email) {
        ClienteDTO cliente = clienteService.findByEmail(email);
        return ResponseEntity.ok(cliente);
    }
    
    @GetMapping("/tipo/{codTipoCliente}")
    @Operation(summary = "Obtener clientes por tipo", 
               description = "Retorna todos los clientes de un tipo específico")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Lista de clientes obtenida exitosamente",
                    content = @Content(mediaType = "application/json", 
                                     schema = @Schema(implementation = ClienteDTO.class)))
    })
    public ResponseEntity<List<ClienteDTO>> getClientesByTipo(
            @Parameter(description = "Código del tipo de cliente", required = true)
            @PathVariable String codTipoCliente) {
        List<ClienteDTO> clientes = clienteService.findByTipoCliente(codTipoCliente);
        return ResponseEntity.ok(clientes);
    }
    
    @PostMapping
    @Operation(summary = "Crear nuevo cliente", 
               description = "Crea un nuevo cliente en el sistema")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Cliente creado exitosamente",
                    content = @Content(mediaType = "application/json", 
                                     schema = @Schema(implementation = ClienteDTO.class))),
        @ApiResponse(responseCode = "400", description = "Datos de entrada inválidos"),
        @ApiResponse(responseCode = "409", description = "Ya existe un cliente con ese email")
    })
    public ResponseEntity<ClienteDTO> createCliente(
            @Parameter(description = "Datos del cliente a crear", required = true)
            @Valid @RequestBody ClienteDTO clienteDTO) {
        ClienteDTO createdCliente = clienteService.create(clienteDTO);
        return new ResponseEntity<>(createdCliente, HttpStatus.CREATED);
    }
    
    @PutMapping("/{id}")
    @Operation(summary = "Actualizar cliente", 
               description = "Actualiza los datos de un cliente existente")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Cliente actualizado exitosamente",
                    content = @Content(mediaType = "application/json", 
                                     schema = @Schema(implementation = ClienteDTO.class))),
        @ApiResponse(responseCode = "400", description = "Datos de entrada inválidos"),
        @ApiResponse(responseCode = "404", description = "Cliente no encontrado"),
        @ApiResponse(responseCode = "409", description = "Ya existe otro cliente con ese email")
    })
    public ResponseEntity<ClienteDTO> updateCliente(
            @Parameter(description = "ID único del cliente", required = true)
            @PathVariable Long id,
            @Parameter(description = "Datos actualizados del cliente", required = true)
            @Valid @RequestBody ClienteDTO clienteDTO) {
        ClienteDTO updatedCliente = clienteService.update(id, clienteDTO);
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
            @Parameter(description = "ID único del cliente", required = true)
            @PathVariable Long id) {
        clienteService.delete(id);
        return ResponseEntity.noContent().build();
    }
    
}
