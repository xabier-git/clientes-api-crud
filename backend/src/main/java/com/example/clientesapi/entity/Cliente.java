package com.example.clientesapi.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;

@Entity
@Table(name = "cliente", 
       indexes = {
           @Index(name = "idx_cliente_email", columnList = "email"),
           @Index(name = "idx_cliente_nombre_apellido", columnList = "nombre, apellido"),
           @Index(name = "idx_cliente_tipo", columnList = "cod_tipo_cliente")
       })
public class Cliente {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;
    
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
    
    @Column(name = "cod_tipo_cliente", length = 10, nullable = false)
    private String codTipoCliente;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cod_tipo_cliente", referencedColumnName = "codigo", insertable = false, updatable = false,
                foreignKey = @ForeignKey(name = "fk_cliente_tipo_cliente"))
    private TipoCliente tipoCliente;
    
    // Constructores
    public Cliente() {}
    
    public Cliente(String nombre, String apellido, Integer edad, String email, String codTipoCliente) {
        this.nombre = nombre;
        this.apellido = apellido;
        this.edad = edad;
        this.email = email;
        this.codTipoCliente = codTipoCliente;
    }
    
    // Getters y Setters
    public Long getId() {
        return id;
    }
    
    public void setId(Long id) {
        this.id = id;
    }
    
    public String getNombre() {
        return nombre;
    }
    
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }
    
    public String getApellido() {
        return apellido;
    }
    
    public void setApellido(String apellido) {
        this.apellido = apellido;
    }
    
    public Integer getEdad() {
        return edad;
    }
    
    public void setEdad(Integer edad) {
        this.edad = edad;
    }
    
    public String getEmail() {
        return email;
    }
    
    public void setEmail(String email) {
        this.email = email;
    }
    
    public String getCodTipoCliente() {
        return codTipoCliente;
    }
    
    public void setCodTipoCliente(String codTipoCliente) {
        this.codTipoCliente = codTipoCliente;
    }
    
    public TipoCliente getTipoCliente() {
        return tipoCliente;
    }
    
    public void setTipoCliente(TipoCliente tipoCliente) {
        this.tipoCliente = tipoCliente;
    }
    
    @Override
    public String toString() {
        return "Cliente{" +
                "id=" + id +
                ", nombre='" + nombre + '\'' +
                ", apellido='" + apellido + '\'' +
                ", edad=" + edad +
                ", email='" + email + '\'' +
                ", codTipoCliente='" + codTipoCliente + '\'' +
                '}';
    }
}
