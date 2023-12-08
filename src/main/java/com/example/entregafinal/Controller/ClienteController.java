package com.example.entregafinal.Controller;

import com.example.entregafinal.Model.ClienteModel;
import com.example.entregafinal.Service.ClienteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequestMapping("api/v1/clientes")
@RestController
public class ClienteController{

    // importamos el servicio del cliente
    @Autowired
    ClienteService clienteService;

    // definimos el post para crear nuevos clientes, acepta un ClientModel como parametros en el body
    @PostMapping("/")
    public ResponseEntity<ClienteModel> create(@RequestBody ClienteModel nuevoCliente) throws Exception{
        return new ResponseEntity<>(clienteService.create(nuevoCliente), HttpStatus.CREATED);
    }

    // definimos un get para todos los clientes que devuelve una lista de ClienteModel
    // devuelve todos, por lo que a futuro se podría agregar algo que si hay 50000 registros los
    // divida de alguna manera
    @GetMapping("/")
    public ResponseEntity<List<ClienteModel>> findAll(){
        return new ResponseEntity<>(clienteService.findAll(), HttpStatus.OK);
    }

    // devuelve el cliente espcificado por {id} en la url
    @GetMapping("/{id}")
    public ResponseEntity<ClienteModel> findById(@PathVariable Long id){
        return new ResponseEntity<>(clienteService.findById(id), HttpStatus.OK);
    }

    // acepta un ClientModel del body para modificar el Cliente especificado
    @PutMapping("/")
    public ResponseEntity<ClienteModel> update(@RequestBody ClienteModel cliente) throws Exception{
        // retiramos el id del cliente que aceptamos en el body para saber a que cliente vamos a modificar
        Long id = cliente.getId();

        return new ResponseEntity<>(clienteService.update(cliente, id), HttpStatus.OK);
    }

    // acepta un id en la url para eliminar dicho cliente
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteById(@PathVariable Long id) throws Exception{
        clienteService.delete(id);
        return new ResponseEntity<>("{\"success\":\"cliente eliminado\"}", HttpStatus.OK);
    }
}
