package com.example.entregafinal.Model;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDate;
import java.util.List;

@Data                       // importamos getters y setters
@Entity                     // señalamos a la clase como entidad
@Table(name = "factura")    // definimos que apuntamos a la tabla de facturas en la db
public class FacturaModel {

    // señalamos la primary key de la tabla
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // definimos la relación que esta tabla de facturas va a tener con clientes
    // desde la vista de factura, tenemos una relación de muchos a uno con respecto a clientes
    // el campo clientes_id de la tabla factura, es una clave foranea que referencia a la tabla clientes, a la columna id
    @ManyToOne
    @JoinColumn(name = "clientes_id")
    private ClienteModel cliente;

    // definimos la fecha de creación de la factura, columna fecha_creacion en la tabla de factura en schema.sql
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private LocalDate fecha_creacion;

    // definimos el total de la factura
    private Double total;

    @OneToMany
    private List<DetallesFacturaModel> lineas;
}
