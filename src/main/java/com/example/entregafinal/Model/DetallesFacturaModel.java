package com.example.entregafinal.Model;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
@Table(name = "detalles_factura")
public class DetallesFacturaModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
}
