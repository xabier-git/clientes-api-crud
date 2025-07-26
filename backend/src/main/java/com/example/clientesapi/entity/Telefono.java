package com.example.clientesapi.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "telefono",
       indexes = {
           @Index(name = "idx_telefono_cliente_id", columnList = "cliente_id")
       })
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Telefono {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;
    
    @Column(name = "numero", length = 15, nullable = false)
    @NotBlank(message = "El número de teléfono es obligatorio")
    @Size(max = 15, message = "El número de teléfono no puede tener más de 15 caracteres")
    @Pattern(regexp = "^[0-9+\\-\\s]+$", message = "El número de teléfono solo puede contener números, +, - y espacios")
    private String numero;
    
    @Column(name = "tipo", length = 20)
    @Size(max = 20, message = "El tipo no puede tener más de 20 caracteres")
    private String tipo; // MOVIL, FIJO, TRABAJO, etc.
    
    @Column(name = "principal")
    private Boolean principal = false;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cliente_id", nullable = false,
                foreignKey = @ForeignKey(name = "fk_telefono_cliente"))
    private Cliente cliente;
    
    @Column(name = "cliente_id", insertable = false, updatable = false)
    private Long clienteId;
    
}
