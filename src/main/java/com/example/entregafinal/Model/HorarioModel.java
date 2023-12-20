package com.example.entregafinal.Model;


import lombok.Data;

// esta clase se crea solamente para manejar las respuestas del servicio externo de hora
// ya que con la libreria de Gson esa respuesta se convierte de json a objeto de java mediante esta clase
@Data
public class HorarioModel {

    private String status;
    private String message;
    private String countryCode;
    private String countryName;
    private String regionName;
    private String cityName;
    private String zoneName;
    private String abbrevation;
    private Long gmtOffset;
    private String dst;
    private String zoneStart;
    private String zoneEnd;
    private String nextAbbreviation;
    private Long timestamp;
    private String formatted;
}
