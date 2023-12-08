package com.example.entregafinal.Controller;

import com.example.entregafinal.Model.ProductoModel;
import com.example.entregafinal.Service.ProductoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequestMapping("api/v1/productos")
@RestController
public class ProductoController {

    // importamos el servicio de productos
    @Autowired
    ProductoService productoService;

    // definimos el post para crear nuevos productos, acepta un ProductoModel como parametros en el body
    @PostMapping("/")
    public ResponseEntity<ProductoModel> create(@RequestBody ProductoModel nuevoProducto) throws Exception{
        return new ResponseEntity<>(this.productoService.create(nuevoProducto), HttpStatus.CREATED);
    }

    // definimos un get para todos los productos que devuelve una lista de ProductoModel
    // devuelve todos, por lo que a futuro se podría agregar algo que si hay 50000 registros los
    // divida de alguna manera
    @GetMapping("/")
    public ResponseEntity<List<ProductoModel>> findAll(){
        return new ResponseEntity<>(this.productoService.findAll(), HttpStatus.OK);
    }

    // devuelve el producto espcificado por {id} en la url
    @GetMapping("/{id}")
    public ResponseEntity<ProductoModel> findById(@PathVariable Long id) throws Exception{
        return new ResponseEntity<>(this.productoService.findById(id), HttpStatus.OK);
    }

    // acepta un ProductoModel del body para modificar el producto especificado
    @PutMapping("/")
    public ResponseEntity<ProductoModel> update(@RequestBody ProductoModel producto) throws Exception{
        // retiramos el id del producto que aceptamos en el body, para saber a
        // que producto vamos a editar
        Long id = Long.valueOf(producto.getId());
        return new ResponseEntity<>(productoService.update(producto, id), HttpStatus.OK);
    }

    // acepta un id en la url para eliminar dicho producto
    @DeleteMapping("/{id}")
    public ResponseEntity<String> delete(@PathVariable Long id) throws Exception{
        this.productoService.delete(id);
        return new ResponseEntity<>("{\"success\":\"producto eliminado\"}", HttpStatus.OK);
    }
}
