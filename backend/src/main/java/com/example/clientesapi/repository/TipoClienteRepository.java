package com.example.clientesapi.repository;

import com.example.clientesapi.entity.TipoCliente;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TipoClienteRepository extends JpaRepository<TipoCliente, String> {
    
    boolean existsByCodigo(String codigo);
    
}
