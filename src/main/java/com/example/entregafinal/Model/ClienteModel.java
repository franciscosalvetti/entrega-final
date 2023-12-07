package com.example.entregafinal.Model;

import jakarta.persistence.*;
import lombok.Data;

@Data                       // importamos getters y setters
@Entity
@Table(name = "clientes")   // señalamos el nombre de la tabla en la db
public class ClienteModel {

    // definimos el la propiedad ID de la entidad que oficiará de PK
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // definimos el nombre del cliente
    private String nombre;

    // definimos el apellido del cliente
    private String apellido;

    // definimos el numero de documento del cliente
    // ademas agregamos la etiqueta @Column porque en la entidad definimos la propiedad
    // numeroDocumento de acuerdo a las buenas prácticas, pero que difiere del nombre de la columna
    // en la db, que es numero_documento
    @Column(name = "numero_documento")
    private String numeroDocumento;
}
