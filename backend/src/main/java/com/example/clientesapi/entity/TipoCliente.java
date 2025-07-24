package com.example.clientesapi.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@Entity
@Table(name = "tipo_cliente")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class TipoCliente {
    
    @Id
    @Column(name = "codigo", length = 10)
    @NotBlank(message = "El código es obligatorio")
    @Size(max = 10, message = "El código no puede tener más de 10 caracteres")
    private String codigo;
    
    @Column(name = "descripcion", length = 100, nullable = false)
    @NotBlank(message = "La descripción es obligatoria")
    @Size(max = 100, message = "La descripción no puede tener más de 100 caracteres")
    private String descripcion;
    
    @OneToMany(mappedBy = "tipoCliente", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Set<Cliente> clientes;
    
}