package com.example.entregafinal.DTO;

import com.example.entregafinal.Model.ClienteModel;
import com.example.entregafinal.Model.DetallesFacturaModel;
import lombok.Data;

import java.time.LocalDate;
import java.util.List;

@Data
public class FacturaDTO  {

    private Long id;

    private ClienteModel cliente;

    private LocalDate fecha;

    private Double total;

    private List<DetallesFacturaDTO> lineas;
}
