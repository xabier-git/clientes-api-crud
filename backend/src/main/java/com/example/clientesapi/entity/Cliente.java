package com.example.clientesapi.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.List;
import java.util.ArrayList;

@Entity
@Table(name = "cliente", 
       indexes = {
           @Index(name = "idx_cliente_rut", columnList = "rut")
       })
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Cliente {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;
    
    @Column(name = "rut", length = 12, unique = true, nullable = false)
    @NotBlank(message = "El RUT es obligatorio")
    @Size(max = 12, message = "El RUT no puede tener más de 12 caracteres")
    private String rut;
    
    @Column(name = "nombre", length = 50, nullable = false)
    @NotBlank(message = "El nombre es obligatorio")
    @Size(max = 50, message = "El nombre no puede tener más de 50 caracteres")
    private String nombre;
    
    @Column(name = "apellido", length = 50, nullable = false)
    @NotBlank(message = "El apellido es obligatorio")
    @Size(max = 50, message = "El apellido no puede tener más de 50 caracteres")
    private String apellido;
    
    @Column(name = "edad")
    @Min(value = 0, message = "La edad debe ser mayor o igual a 0")
    @Max(value = 150, message = "La edad debe ser menor o igual a 150")
    private Integer edad;
    
    @Column(name = "email", length = 100, unique = true, nullable = false)
    @NotBlank(message = "El email es obligatorio")
    @Email(message = "El formato del email no es válido")
    @Size(max = 100, message = "El email no puede tener más de 100 caracteres")
    private String email;
    
    @OneToMany(mappedBy = "cliente", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<Telefono> telefonos = new ArrayList<>();
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cod_tipo_cliente", referencedColumnName = "codigo", nullable = false,
                foreignKey = @ForeignKey(name = "fk_cliente_tipo_cliente"))
    private TipoCliente tipoCliente;
    
}
