package com.example.entregafinal.Model;

import jakarta.persistence.*;
import lombok.Data;

@Data                               // importamos getters y setters
@Entity                             // señalamos a la clase como una entidad
@Table(name = "detalles_factura")   // definimos que apuntamos a la tabla detalles_factura en la db
public class DetallesFacturaModel {

    // señalamos la pk de la tabla detalles_factura
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // definimos la relación con respecto a la tabla facturas
    // desde la vista de detalles_factura, y con respecto a la tabla de facturas, la relación es de muchos a uno.
    // el campo factura_id de detalles_factura es una clave foranea que referencia a la tabla de factura, a la columna id
    @ManyToOne
    @JoinColumn(name = "factura_id")
    private FacturaModel factura;

    // definimos la relación con respecto a la tabla productos
    // desde la vista de detalles_factura, tenemos una relacion de muchos a uno con respecto a productos
    // el campo productos_id es una clave foranea que referencia a la tabla productos, a la columna id
    @ManyToOne
    @JoinColumn(name = "productos_id")
    private ProductoModel productos;

    // definimos la cantidad del producto en que va a contener la "linea de detalle de la factura"
    @Column(name = "cantidad_productos")
    private Integer cantidadProductos;

    // definimos el campo importe, que será el resultado de la columna cantidad_productos de esta misma tabla, multiplicado
    // por el campo precio de la tabla de productos
    private Double importe;
}
