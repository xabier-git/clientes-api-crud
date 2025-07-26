package com.example.clientesapi.service;

import com.example.clientesapi.dto.ClienteDTO;
import com.example.clientesapi.entity.Cliente;
import com.example.clientesapi.entity.Telefono;
import com.example.clientesapi.entity.TipoCliente;
import com.example.clientesapi.exception.ResourceNotFoundException;
import com.example.clientesapi.exception.DuplicateResourceException;
import com.example.clientesapi.repository.ClienteRepository;
import com.example.clientesapi.repository.TipoClienteRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
@Slf4j
@RequiredArgsConstructor
public class ClienteService {
    
    private final ClienteRepository clienteRepository;
    private final TipoClienteRepository tipoClienteRepository;
    
    @Transactional(readOnly = true)
    public List<ClienteDTO> findAll() {
        log.info("Obteniendo lista de todos los clientes");
        List<ClienteDTO> clientes = clienteRepository.findAll()
                .stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
        log.info("Se obtuvieron {} clientes", clientes.size());
        return clientes;
    }
    
    @Transactional(readOnly = true)
    public ClienteDTO findById(Long id) {
        log.info("Buscando cliente por ID: {}", id);
        Cliente cliente = clienteRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Cliente no encontrado con ID: " + id));
        log.info("Cliente encontrado: {} {}", cliente.getNombre(), cliente.getApellido());
        return toDTO(cliente);
    }
    
    @Transactional(readOnly = true)
    public ClienteDTO findByRut(String rut) {
        log.info("Buscando cliente por RUT: {}", rut);
        Cliente cliente = clienteRepository.findByRut(rut)
                .orElseThrow(() -> new ResourceNotFoundException("Cliente no encontrado con RUT: " + rut));
        log.info("Cliente encontrado: {} {}", cliente.getNombre(), cliente.getApellido());
        return toDTO(cliente);
    }
    
    public ClienteDTO create(ClienteDTO clienteDTO) {
        log.info("Creando nuevo cliente con RUT: {} y email: {}", clienteDTO.getRut(), clienteDTO.getEmail());
        
        // Validar que el RUT no exista
        if (clienteRepository.existsByRut(clienteDTO.getRut())) {
            log.warn("Intento de crear cliente con RUT duplicado: {}", clienteDTO.getRut());
            throw new DuplicateResourceException("Ya existe un cliente con RUT: " + clienteDTO.getRut());
        }
        
        // Validar que el email no exista
        if (clienteRepository.existsByEmail(clienteDTO.getEmail())) {
            log.warn("Intento de crear cliente con email duplicado: {}", clienteDTO.getEmail());
            throw new DuplicateResourceException("Ya existe un cliente con email: " + clienteDTO.getEmail());
        }
        
        // 🎯 NUEVA VALIDACIÓN: Verificar que el código de tipo cliente exista
        TipoCliente tipoCliente = tipoClienteRepository.findById(clienteDTO.getCodTipoCliente())
                .orElseThrow(() -> new ResourceNotFoundException("Tipo de cliente no encontrado con código: " + clienteDTO.getCodTipoCliente()));
        
        Cliente cliente = toEntity(clienteDTO);
        cliente.setTipoCliente(tipoCliente);
        Cliente savedCliente = clienteRepository.save(cliente);
        log.info("Cliente creado exitosamente con ID: {} para {} {}", savedCliente.getId(), savedCliente.getNombre(), savedCliente.getApellido());
        return toDTO(savedCliente);
    }
    
    public ClienteDTO update(Long id, ClienteDTO clienteDTO) {
        log.info("Actualizando cliente con ID: {}", id);
        Cliente existingCliente = clienteRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Cliente no encontrado con ID: " + id));
        
        // Validar que el RUT no exista en otro cliente
        if (clienteRepository.existsByRutAndIdNot(clienteDTO.getRut(), id)) {
            log.warn("Intento de actualizar cliente con RUT duplicado: {}", clienteDTO.getRut());
            throw new DuplicateResourceException("Ya existe otro cliente con RUT: " + clienteDTO.getRut());
        }
        
        // Validar que el email no exista en otro cliente
        if (clienteRepository.existsByEmailAndIdNot(clienteDTO.getEmail(), id)) {
            log.warn("Intento de actualizar cliente con email duplicado: {}", clienteDTO.getEmail());
            throw new DuplicateResourceException("Ya existe otro cliente con email: " + clienteDTO.getEmail());
        }
        
        // 🎯 NUEVA VALIDACIÓN: Verificar que el código de tipo cliente exista
        TipoCliente tipoCliente = tipoClienteRepository.findById(clienteDTO.getCodTipoCliente())
                .orElseThrow(() -> new ResourceNotFoundException("Tipo de cliente no encontrado con código: " + clienteDTO.getCodTipoCliente()));
        
        updateEntityFromDTO(clienteDTO, existingCliente);
        existingCliente.setTipoCliente(tipoCliente);
        Cliente updatedCliente = clienteRepository.save(existingCliente);
        log.info("Cliente actualizado exitosamente: {} {}", updatedCliente.getNombre(), updatedCliente.getApellido());
        return toDTO(updatedCliente);
    }
    
    public void delete(Long id) {
        log.info("Eliminando cliente con ID: {}", id);
        if (!clienteRepository.existsById(id)) {
            log.warn("Intento de eliminar cliente inexistente con ID: {}", id);
            throw new ResourceNotFoundException("Cliente no encontrado con ID: " + id);
        }
        clienteRepository.deleteById(id);
        log.info("Cliente eliminado exitosamente con ID: {}", id);
    }
    
    // Métodos de conversión privados
    private ClienteDTO toDTO(Cliente entity) {
        List<String> telefonos = entity.getTelefonos()
                .stream()
                .map(Telefono::getNumero)
                .collect(Collectors.toList());
                
        return new ClienteDTO(
            entity.getId(),
            entity.getRut(),
            entity.getNombre(),
            entity.getApellido(),
            entity.getEdad(),
            entity.getEmail(),
            entity.getTipoCliente() != null ? entity.getTipoCliente().getCodigo() : null,
            telefonos
        );
    }
    
    private Cliente toEntity(ClienteDTO dto) {
        Cliente cliente = new Cliente();
        cliente.setId(dto.getId());
        cliente.setRut(dto.getRut());
        cliente.setNombre(dto.getNombre());
        cliente.setApellido(dto.getApellido());
        cliente.setEdad(dto.getEdad());
        cliente.setEmail(dto.getEmail());
        // tipoCliente se establecerá en el método create/update
        
        // Crear teléfonos
        if (dto.getTelefonos() != null && !dto.getTelefonos().isEmpty()) {
            List<Telefono> telefonos = dto.getTelefonos().stream()
                    .map(numero -> {
                        Telefono telefono = new Telefono();
                        telefono.setNumero(numero);
                        telefono.setTipo("MOVIL"); // Tipo por defecto
                        telefono.setPrincipal(false);
                        telefono.setCliente(cliente);
                        return telefono;
                    })
                    .collect(Collectors.toList());
            cliente.setTelefonos(telefonos);
        }
        
        return cliente;
    }
    
    private void updateEntityFromDTO(ClienteDTO dto, Cliente entity) {
        entity.setRut(dto.getRut());
        entity.setNombre(dto.getNombre());
        entity.setApellido(dto.getApellido());
        entity.setEdad(dto.getEdad());
        entity.setEmail(dto.getEmail());
        // tipoCliente se establecerá en el método update
        
        // Actualizar teléfonos
        if (dto.getTelefonos() != null) {
            // Limpiar teléfonos existentes
            entity.getTelefonos().clear();
            
            // Agregar nuevos teléfonos
            List<Telefono> nuevosTelefonos = dto.getTelefonos().stream()
                    .map(numero -> {
                        Telefono telefono = new Telefono();
                        telefono.setNumero(numero);
                        telefono.setTipo("MOVIL"); // Tipo por defecto
                        telefono.setPrincipal(false);
                        telefono.setCliente(entity);
                        return telefono;
                    })
                    .collect(Collectors.toList());
            entity.getTelefonos().addAll(nuevosTelefonos);
        }
    }
    
}
