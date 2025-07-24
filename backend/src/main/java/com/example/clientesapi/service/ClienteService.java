package com.example.clientesapi.service;

import com.example.clientesapi.dto.ClienteDTO;
import com.example.clientesapi.entity.Cliente;
import com.example.clientesapi.exception.ResourceNotFoundException;
import com.example.clientesapi.exception.DuplicateResourceException;
import com.example.clientesapi.repository.ClienteRepository;
import com.example.clientesapi.repository.TipoClienteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class ClienteService {
    
    @Autowired
    private ClienteRepository clienteRepository;
    
    @Autowired
    private TipoClienteRepository tipoClienteRepository;
    
    @Transactional(readOnly = true)
    public List<ClienteDTO> findAll() {
        return clienteRepository.findAll()
                .stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }
    
    @Transactional(readOnly = true)
    public ClienteDTO findById(Long id) {
        Cliente cliente = clienteRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Cliente no encontrado con ID: " + id));
        return toDTO(cliente);
    }
    
    @Transactional(readOnly = true)
    public ClienteDTO findByRut(String rut) {
        Cliente cliente = clienteRepository.findByRut(rut)
                .orElseThrow(() -> new ResourceNotFoundException("Cliente no encontrado con RUT: " + rut));
        return toDTO(cliente);
    }
    
    public ClienteDTO create(ClienteDTO clienteDTO) {
        // Validar que el RUT no exista
        if (clienteRepository.existsByRut(clienteDTO.getRut())) {
            throw new DuplicateResourceException("Ya existe un cliente con RUT: " + clienteDTO.getRut());
        }
        
        // Validar que el email no exista
        if (clienteRepository.existsByEmail(clienteDTO.getEmail())) {
            throw new DuplicateResourceException("Ya existe un cliente con email: " + clienteDTO.getEmail());
        }
        
        // 🎯 NUEVA VALIDACIÓN: Verificar que el código de tipo cliente exista
        if (!tipoClienteRepository.existsById(clienteDTO.getCodTipoCliente())) {
            throw new ResourceNotFoundException("Tipo de cliente no encontrado con código: " + clienteDTO.getCodTipoCliente());
        }
        
        Cliente cliente = toEntity(clienteDTO);
        Cliente savedCliente = clienteRepository.save(cliente);
        return toDTO(savedCliente);
    }
    
    public ClienteDTO update(Long id, ClienteDTO clienteDTO) {
        Cliente existingCliente = clienteRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Cliente no encontrado con ID: " + id));
        
        // Validar que el RUT no exista en otro cliente
        if (clienteRepository.existsByRutAndIdNot(clienteDTO.getRut(), id)) {
            throw new DuplicateResourceException("Ya existe otro cliente con RUT: " + clienteDTO.getRut());
        }
        
        // Validar que el email no exista en otro cliente
        if (clienteRepository.existsByEmailAndIdNot(clienteDTO.getEmail(), id)) {
            throw new DuplicateResourceException("Ya existe otro cliente con email: " + clienteDTO.getEmail());
        }
        
        // 🎯 NUEVA VALIDACIÓN: Verificar que el código de tipo cliente exista
        if (!tipoClienteRepository.existsById(clienteDTO.getCodTipoCliente())) {
            throw new ResourceNotFoundException("Tipo de cliente no encontrado con código: " + clienteDTO.getCodTipoCliente());
        }
        
        updateEntityFromDTO(clienteDTO, existingCliente);
        Cliente updatedCliente = clienteRepository.save(existingCliente);
        return toDTO(updatedCliente);
    }
    
    public void delete(Long id) {
        if (!clienteRepository.existsById(id)) {
            throw new ResourceNotFoundException("Cliente no encontrado con ID: " + id);
        }
        clienteRepository.deleteById(id);
    }
    
    // Métodos de conversión privados
    private ClienteDTO toDTO(Cliente entity) {
        return new ClienteDTO(
            entity.getId(),
            entity.getRut(),
            entity.getNombre(),
            entity.getApellido(),
            entity.getEdad(),
            entity.getEmail(),
            entity.getCodTipoCliente()
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
        cliente.setCodTipoCliente(dto.getCodTipoCliente());
        return cliente;
    }
    
    private void updateEntityFromDTO(ClienteDTO dto, Cliente entity) {
        entity.setRut(dto.getRut());
        entity.setNombre(dto.getNombre());
        entity.setApellido(dto.getApellido());
        entity.setEdad(dto.getEdad());
        entity.setEmail(dto.getEmail());
        entity.setCodTipoCliente(dto.getCodTipoCliente());
    }
    
}
