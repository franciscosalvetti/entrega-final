package com.example.entregafinal.DTO;

import lombok.Data;

@Data
public class DetallesFacturaDTO {
    private Long id;

    private Integer cantidad;

    private Long producto_id;

    private Double importe;
}
