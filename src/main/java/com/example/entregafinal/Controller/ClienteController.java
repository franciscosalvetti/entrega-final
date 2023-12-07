package com.example.entregafinal.Controller;

import com.example.entregafinal.Model.ClienteModel;
import com.example.entregafinal.Service.ClienteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("api/v1/clientes")
@RestController
public class ClienteController{

    @Autowired
    ClienteService clienteService;

    @PostMapping("/")
    public ResponseEntity<ClienteModel> create(@RequestBody ClienteModel nuevoCliente) throws Exception{
        return new ResponseEntity<>(clienteService.create(nuevoCliente), HttpStatus.CREATED);
    }
}
