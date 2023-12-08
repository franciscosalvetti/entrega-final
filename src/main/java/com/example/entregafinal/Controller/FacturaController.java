package com.example.entregafinal.Controller;

import com.example.entregafinal.Model.FacturaModel;
import com.example.entregafinal.Service.FacturaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/v1/facturas")
public class FacturaController {

    // importamos el servicio de facturas
    @Autowired
    FacturaService facturaService;

    // importamos el servicio de facturas
    // definimos el post para crear nuevas facturas, acepta un FacturaModel como parametros en el body
    @PostMapping("/")
    public ResponseEntity<FacturaModel> create(@RequestBody FacturaModel nuevaFactura) throws Exception{
        return new ResponseEntity<>(facturaService.create(nuevaFactura), HttpStatus.CREATED);
    }

    // definimos un get para todas las facturas que devuelve una lista de FacturaModel
    // devuelve todas, por lo que a futuro se podría agregar algo que si hay 50000 registros los
    // divida de alguna manera
    @GetMapping("/")
    public ResponseEntity<List<FacturaModel>> listAll(){
        return new ResponseEntity<>(facturaService.findAll(), HttpStatus.OK);
    }

    // devuelve la factura espcificada por {id} en la url
    @GetMapping("/{id}")
    public ResponseEntity<FacturaModel> findById(@PathVariable Long id){
        return new ResponseEntity<>(facturaService.findById(id), HttpStatus.OK);
    }

    // acepta un FacturaModel del body para modificar la Factura especificada
    @PutMapping("/")
    public ResponseEntity<FacturaModel> update(@RequestBody FacturaModel factura) throws Exception{
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
