package com.example.clientesapi.repository;

import com.example.clientesapi.entity.Cliente;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ClienteRepository extends JpaRepository<Cliente, Long> {
    
    Optional<Cliente> findByRut(String rut);
    
    boolean existsByRut(String rut);
    
    boolean existsByRutAndIdNot(String rut, Long id);
    
    boolean existsByEmail(String email);
    
    boolean existsByEmailAndIdNot(String email, Long id);
    
}
