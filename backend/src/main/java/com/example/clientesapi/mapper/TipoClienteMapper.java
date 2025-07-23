package com.example.clientesapi.mapper;

import com.example.clientesapi.dto.TipoClienteDTO;
import com.example.clientesapi.entity.TipoCliente;
import org.springframework.stereotype.Component;

@Component
public class TipoClienteMapper {
    
    public TipoClienteDTO toDTO(TipoCliente entity) {
        if (entity == null) {
            return null;
        }
        
        TipoClienteDTO dto = new TipoClienteDTO();
        dto.setCodigo(entity.getCodigo());
        dto.setDescripcion(entity.getDescripcion());
        
        return dto;
    }
    
    public TipoCliente toEntity(TipoClienteDTO dto) {
        if (dto == null) {
            return null;
        }
        
        TipoCliente entity = new TipoCliente();
        entity.setCodigo(dto.getCodigo());
        entity.setDescripcion(dto.getDescripcion());
        
        return entity;
    }
    
    public void updateEntityFromDTO(TipoClienteDTO dto, TipoCliente entity) {
        if (dto == null || entity == null) {
            return;
        }
        
        entity.setDescripcion(dto.getDescripcion());
    }
    
}
