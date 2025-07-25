package com.example.clientesapi.repository;

import com.example.clientesapi.entity.Telefono;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TelefonoRepository extends JpaRepository<Telefono, Long> {
    
    @Query("SELECT t FROM Telefono t WHERE t.clienteId = :clienteId")
    List<Telefono> findByClienteId(@Param("clienteId") Long clienteId);
    
    @Query("SELECT t FROM Telefono t WHERE t.numero = :numero")
    List<Telefono> findByNumero(@Param("numero") String numero);
    
    @Query("DELETE FROM Telefono t WHERE t.clienteId = :clienteId")
    void deleteByClienteId(@Param("clienteId") Long clienteId);
    
}
