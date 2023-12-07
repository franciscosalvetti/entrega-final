package com.example.entregafinal.Model;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
@Table(name = "factura")
public class FacturaModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
}
