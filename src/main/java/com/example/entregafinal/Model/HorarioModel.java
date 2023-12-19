package com.example.entregafinal.Model;


import lombok.Data;

// esta clase se crea solamente para manejar las respuestas del servicio externo de hora
// ya que con la libreria de Gson esa respuesta se convierte de json a objeto de java mediante esta clase
@Data
public class HorarioModel {

    private String estadoPeticion;
    private String fecha;
}
