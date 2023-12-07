package com.example.entregafinal.Model;

import jakarta.persistence.*;
import lombok.Data;

@Data                       // importamos getters y setters
@Entity                     // señalamos a la clase como entidad
@Table(name = "productos")  // definimos a qué tabla en la db apuntamos con esta entidad
public class ProductoModel {

    //  señalamos la pk de la tabla
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // definimos la descripción del producto, columna descripcion en schema.sql en la tabla producto
    private String descripcion;

    // definimos el código del producto
    private String codigo;

    // definimos el stock que el producto va a tener
    private Integer stock;

    // definimos el precio unitario del producto
    private Double precio;
}
