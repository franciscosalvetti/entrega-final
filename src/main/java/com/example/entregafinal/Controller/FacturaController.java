package com.example.entregafinal.Controller;

import com.example.entregafinal.DTO.FacturaDTO;
import com.example.entregafinal.Model.FacturaModel;
import com.example.entregafinal.Model.HorarioModel;
import com.example.entregafinal.Service.FacturaService;
import com.google.gson.Gson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("api/v1/facturas")
public class FacturaController {

    // importamos el servicio de facturas
    @Autowired
    FacturaService facturaService;

    /*@PostMapping("/algo")
    public String algo(@RequestBody FacturaModel nuevaFactura) throws Exception{
        return nuevaFactura.getLineas().toString();
    }

        /*RestTemplate restTemplate = new RestTemplate();
        final String url = "http://api.timezonedb.com/v2.1/get-time-zone?key=MU852FWV2O1E&format=json&by=zone&zone=America/Argentina/Buenos_Aires";
        try{
            String resultado = restTemplate.getForObject(url, String.class);

            Gson gson = new Gson();

            // Convertir el JSON a un objeto Java
            HorarioModel horario = gson.fromJson(resultado, HorarioModel.class);

            // Acceder a los campos del objeto Java
            String status = horario.getEstadoPeticion();
            String fecha = horario.getFecha();

            return LocalDate.parse("2023-12-16 18:37:47");

            // Imprimir los valores
            System.out.println("Nombre: " + nombre);
            System.out.println("Edad: " + edad);*/

            /*if(Objects.equals(status, "OK")){
                return LocalDate.parse(fecha).toString();
            }else{
                return "no se pudo";
            }/

            //return status+" "+fecha;
        }catch (Exception e){
            return LocalDate.now();
        }*/

    //}

    // importamos el servicio de facturas
    // definimos el post para crear nuevas facturas, acepta un FacturaModel como parametros en el body
    @PostMapping("/")
    public ResponseEntity<FacturaDTO> create(@RequestBody FacturaModel nuevaFactura) throws Exception{
        return new ResponseEntity<>(facturaService.create(nuevaFactura), HttpStatus.CREATED);
    }

    // definimos un get para todas las facturas que devuelve una lista de FacturaModel
    // devuelve todas, por lo que a futuro se podría agregar algo que si hay 50000 registros los
    // divida de alguna manera
    @GetMapping("/")
    public ResponseEntity<List<FacturaDTO>> listAll(){
        return new ResponseEntity<>(facturaService.findAll(), HttpStatus.OK);
    }

    // devuelve la factura espcificada por {id} en la url
    @GetMapping("/{id}")
    //public ResponseEntity<FacturaModel> findById(@PathVariable Long id){
    public ResponseEntity<FacturaDTO> findById(@PathVariable Long id){
        return new ResponseEntity<>(facturaService.findById(id), HttpStatus.OK);
    }

    // acepta un FacturaModel del body para modificar la Factura especificada
    @PutMapping("/")
    public ResponseEntity<FacturaDTO> update(@RequestBody FacturaModel factura) throws Exception{
        // retiramos el id de la factura que recibimos en el body
        // para saber que factura vamos a modificar
        Long id = Long.valueOf(factura.getId());
        return new ResponseEntity<>(facturaService.update(factura, id), HttpStatus.OK);
    }

    // acepta un id en la url para eliminar dicha factura
    @DeleteMapping("/{id}")
    public ResponseEntity<String> delete(@PathVariable Long id) throws Exception{
        this.facturaService.delete(id);
        return new ResponseEntity<>("{\"success\":\"factura eliminada\"}", HttpStatus.OK);
    }
}
