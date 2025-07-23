package com.example.clientesapi.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import java.util.Set;

@Entity
@Table(name = "tipo_cliente")
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
    
    // Constructores
    public TipoCliente() {}
    
    public TipoCliente(String codigo, String descripcion) {
        this.codigo = codigo;
        this.descripcion = descripcion;
    }
    
    // Getters y Setters
    public String getCodigo() {
        return codigo;
    }
    
    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }
    
    public String getDescripcion() {
        return descripcion;
    }
    
    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }
    
    public Set<Cliente> getClientes() {
        return clientes;
    }
    
    public void setClientes(Set<Cliente> clientes) {
        this.clientes = clientes;
    }
    
    @Override
    public String toString() {
        return "TipoCliente{" +
                "codigo='" + codigo + '\'' +
                ", descripcion='" + descripcion + '\'' +
                '}';
    }
}
