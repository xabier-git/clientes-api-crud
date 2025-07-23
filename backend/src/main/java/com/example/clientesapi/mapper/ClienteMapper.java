package com.example.clientesapi.mapper;

import com.example.clientesapi.dto.ClienteDTO;
import com.example.clientesapi.entity.Cliente;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ClienteMapper {
    
    @Autowired
    private TipoClienteMapper tipoClienteMapper;
    
    public ClienteDTO toDTO(Cliente entity) {
        if (entity == null) {
            return null;
        }
        
        ClienteDTO dto = new ClienteDTO();
        dto.setId(entity.getId());
        dto.setNombre(entity.getNombre());
        dto.setApellido(entity.getApellido());
        dto.setEdad(entity.getEdad());
        dto.setEmail(entity.getEmail());
        dto.setCodTipoCliente(entity.getCodTipoCliente());
        dto.setTipoCliente(tipoClienteMapper.toDTO(entity.getTipoCliente()));
        
        return dto;
    }
    
    public Cliente toEntity(ClienteDTO dto) {
        if (dto == null) {
            return null;
        }
        
        Cliente entity = new Cliente();
        entity.setId(dto.getId());
        entity.setNombre(dto.getNombre());
        entity.setApellido(dto.getApellido());
        entity.setEdad(dto.getEdad());
        entity.setEmail(dto.getEmail());
        entity.setCodTipoCliente(dto.getCodTipoCliente());
        
        return entity;
    }
    
    public void updateEntityFromDTO(ClienteDTO dto, Cliente entity) {
        if (dto == null || entity == null) {
            return;
        }
        
        entity.setNombre(dto.getNombre());
        entity.setApellido(dto.getApellido());
        entity.setEdad(dto.getEdad());
        entity.setEmail(dto.getEmail());
        entity.setCodTipoCliente(dto.getCodTipoCliente());
    }
    
}
