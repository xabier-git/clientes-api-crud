package com.example.clientesapi.service;

import com.example.clientesapi.dto.TipoClienteDTO;
import com.example.clientesapi.entity.TipoCliente;
import com.example.clientesapi.exception.ResourceNotFoundException;
import com.example.clientesapi.exception.DuplicateResourceException;
import com.example.clientesapi.mapper.TipoClienteMapper;
import com.example.clientesapi.repository.TipoClienteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class TipoClienteService {
    
    @Autowired
    private TipoClienteRepository tipoClienteRepository;
    
    @Autowired
    private TipoClienteMapper tipoClienteMapper;
    
    @Transactional(readOnly = true)
    public List<TipoClienteDTO> findAll() {
        return tipoClienteRepository.findAll()
                .stream()
                .map(tipoClienteMapper::toDTO)
                .collect(Collectors.toList());
    }
    
    @Transactional(readOnly = true)
    public TipoClienteDTO findById(String codigo) {
        TipoCliente tipoCliente = tipoClienteRepository.findById(codigo)
                .orElseThrow(() -> new ResourceNotFoundException("Tipo de cliente no encontrado con código: " + codigo));
        return tipoClienteMapper.toDTO(tipoCliente);
    }
    
    public TipoClienteDTO create(TipoClienteDTO tipoClienteDTO) {
        if (tipoClienteRepository.existsByCodigo(tipoClienteDTO.getCodigo())) {
            throw new DuplicateResourceException("Ya existe un tipo de cliente con código: " + tipoClienteDTO.getCodigo());
        }
        
        TipoCliente tipoCliente = tipoClienteMapper.toEntity(tipoClienteDTO);
        TipoCliente savedTipoCliente = tipoClienteRepository.save(tipoCliente);
        return tipoClienteMapper.toDTO(savedTipoCliente);
    }
    
    public TipoClienteDTO update(String codigo, TipoClienteDTO tipoClienteDTO) {
        TipoCliente existingTipoCliente = tipoClienteRepository.findById(codigo)
                .orElseThrow(() -> new ResourceNotFoundException("Tipo de cliente no encontrado con código: " + codigo));
        
        tipoClienteMapper.updateEntityFromDTO(tipoClienteDTO, existingTipoCliente);
        TipoCliente updatedTipoCliente = tipoClienteRepository.save(existingTipoCliente);
        return tipoClienteMapper.toDTO(updatedTipoCliente);
    }
    
    public void delete(String codigo) {
        if (!tipoClienteRepository.existsById(codigo)) {
            throw new ResourceNotFoundException("Tipo de cliente no encontrado con código: " + codigo);
        }
        tipoClienteRepository.deleteById(codigo);
    }
    
}
