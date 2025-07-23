package com.example.clientesapi.service;

import com.example.clientesapi.dto.ClienteDTO;
import com.example.clientesapi.entity.Cliente;
import com.example.clientesapi.exception.ResourceNotFoundException;
import com.example.clientesapi.exception.DuplicateResourceException;
import com.example.clientesapi.exception.BusinessLogicException;
import com.example.clientesapi.mapper.ClienteMapper;
import com.example.clientesapi.repository.ClienteRepository;
import com.example.clientesapi.repository.TipoClienteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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
    
    @Autowired
    private ClienteMapper clienteMapper;
    
    @Transactional(readOnly = true)
    public List<ClienteDTO> findAll() {
        return clienteRepository.findAll()
                .stream()
                .map(clienteMapper::toDTO)
                .collect(Collectors.toList());
    }
    
    @Transactional(readOnly = true)
    public Page<ClienteDTO> findByFilters(String nombre, String apellido, String email, String codTipoCliente, Pageable pageable) {
        Page<Cliente> clientesPage = clienteRepository.findByFilters(nombre, apellido, email, codTipoCliente, pageable);
        return clientesPage.map(clienteMapper::toDTO);
    }
    
    @Transactional(readOnly = true)
    public ClienteDTO findById(Long id) {
        Cliente cliente = clienteRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Cliente no encontrado con ID: " + id));
        return clienteMapper.toDTO(cliente);
    }
    
    @Transactional(readOnly = true)
    public ClienteDTO findByEmail(String email) {
        Cliente cliente = clienteRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("Cliente no encontrado con email: " + email));
        return clienteMapper.toDTO(cliente);
    }
    
    @Transactional(readOnly = true)
    public List<ClienteDTO> findByTipoCliente(String codTipoCliente) {
        List<Cliente> clientes = clienteRepository.findByCodTipoCliente(codTipoCliente);
        return clientes.stream()
                .map(clienteMapper::toDTO)
                .collect(Collectors.toList());
    }
    
    public ClienteDTO create(ClienteDTO clienteDTO) {
        // Validar que el email no exista
        if (clienteRepository.existsByEmail(clienteDTO.getEmail())) {
            throw new DuplicateResourceException("Ya existe un cliente con email: " + clienteDTO.getEmail());
        }
        
        // Validar que el tipo de cliente exista
        if (!tipoClienteRepository.existsById(clienteDTO.getCodTipoCliente())) {
            throw new BusinessLogicException("No existe el tipo de cliente con código: " + clienteDTO.getCodTipoCliente());
        }
        
        Cliente cliente = clienteMapper.toEntity(clienteDTO);
        Cliente savedCliente = clienteRepository.save(cliente);
        return clienteMapper.toDTO(savedCliente);
    }
    
    public ClienteDTO update(Long id, ClienteDTO clienteDTO) {
        Cliente existingCliente = clienteRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Cliente no encontrado con ID: " + id));
        
        // Validar que el email no exista en otro cliente
        if (clienteRepository.existsByEmailAndIdNot(clienteDTO.getEmail(), id)) {
            throw new DuplicateResourceException("Ya existe otro cliente con email: " + clienteDTO.getEmail());
        }
        
        // Validar que el tipo de cliente exista
        if (!tipoClienteRepository.existsById(clienteDTO.getCodTipoCliente())) {
            throw new BusinessLogicException("No existe el tipo de cliente con código: " + clienteDTO.getCodTipoCliente());
        }
        
        clienteMapper.updateEntityFromDTO(clienteDTO, existingCliente);
        Cliente updatedCliente = clienteRepository.save(existingCliente);
        return clienteMapper.toDTO(updatedCliente);
    }
    
    public void delete(Long id) {
        if (!clienteRepository.existsById(id)) {
            throw new ResourceNotFoundException("Cliente no encontrado con ID: " + id);
        }
        clienteRepository.deleteById(id);
    }
    
}
