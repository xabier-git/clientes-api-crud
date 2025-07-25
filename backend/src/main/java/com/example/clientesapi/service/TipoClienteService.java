package com.example.clientesapi.service;

import com.example.clientesapi.dto.TipoClienteDTO;
import com.example.clientesapi.entity.TipoCliente;
import com.example.clientesapi.exception.DuplicateResourceException;
import com.example.clientesapi.exception.ResourceNotFoundException;
import com.example.clientesapi.repository.TipoClienteRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class TipoClienteService {
    
    private final TipoClienteRepository tipoClienteRepository;
    
    @Transactional(readOnly = true)
    public List<TipoClienteDTO> findAll() {
        log.info("Buscando todos los tipos de cliente");
        List<TipoCliente> tipos = tipoClienteRepository.findAll();
        return tipos.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }
    
    @Transactional(readOnly = true)
    public TipoClienteDTO findById(String codigo) {
        log.info("Buscando tipo de cliente por código: {}", codigo);
        TipoCliente tipoCliente = tipoClienteRepository.findById(codigo)
                .orElseThrow(() -> new ResourceNotFoundException("Tipo de cliente no encontrado con código: " + codigo));
        return convertToDTO(tipoCliente);
    }
    
    public TipoClienteDTO save(TipoClienteDTO tipoClienteDTO) {
        log.info("Creando nuevo tipo de cliente: {}", tipoClienteDTO.getCodigo());
        
        if (tipoClienteRepository.existsByCodigo(tipoClienteDTO.getCodigo())) {
            throw new DuplicateResourceException("Ya existe un tipo de cliente con código: " + tipoClienteDTO.getCodigo());
        }
        
        TipoCliente tipoCliente = convertToEntity(tipoClienteDTO);
        TipoCliente savedTipoCliente = tipoClienteRepository.save(tipoCliente);
        return convertToDTO(savedTipoCliente);
    }
    
    public TipoClienteDTO update(String codigo, TipoClienteDTO tipoClienteDTO) {
        log.info("Actualizando tipo de cliente: {}", codigo);
        
        TipoCliente existingTipoCliente = tipoClienteRepository.findById(codigo)
                .orElseThrow(() -> new ResourceNotFoundException("Tipo de cliente no encontrado con código: " + codigo));
        
        existingTipoCliente.setDescripcion(tipoClienteDTO.getDescripcion());
        
        TipoCliente updatedTipoCliente = tipoClienteRepository.save(existingTipoCliente);
        return convertToDTO(updatedTipoCliente);
    }
    
    public void deleteById(String codigo) {
        log.info("Eliminando tipo de cliente: {}", codigo);
        
        if (!tipoClienteRepository.existsById(codigo)) {
            throw new ResourceNotFoundException("Tipo de cliente no encontrado con código: " + codigo);
        }
        
        tipoClienteRepository.deleteById(codigo);
    }
    
    // Métodos de conversión usando Lombok
    private TipoClienteDTO convertToDTO(TipoCliente tipoCliente) {
        TipoClienteDTO dto = new TipoClienteDTO();
        dto.setCodigo(tipoCliente.getCodigo());
        dto.setDescripcion(tipoCliente.getDescripcion());
        return dto;
    }
    
    private TipoCliente convertToEntity(TipoClienteDTO dto) {
        TipoCliente tipoCliente = new TipoCliente();
        tipoCliente.setCodigo(dto.getCodigo());
        tipoCliente.setDescripcion(dto.getDescripcion());
        return tipoCliente;
    }
}
