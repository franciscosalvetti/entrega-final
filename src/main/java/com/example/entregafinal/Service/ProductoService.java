package com.example.entregafinal.Service;

import com.example.entregafinal.Model.ProductoModel;
import com.example.entregafinal.Repository.ProductoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ProductoService {

    @Autowired
    ProductoRepository productoRepository;

    public ProductoModel create(ProductoModel nuevoProducto) throws Exception {

        if(nuevoProducto.getCodigo() == "" || nuevoProducto.getCodigo().length() > 50){
            // el nombre es inválido, por largo o por vacio
            throw new IllegalArgumentException("El código del producto es inválido. No puede ser vacío y debe tener entre 1 y 50 caracteres.");
        }

        if(nuevoProducto.getDescripcion() == "" || nuevoProducto.getDescripcion().length() > 150){
            // la descripción es inválida, por larga o por vacia
            throw new IllegalArgumentException("La descripción del producto es inválida. No puede ser vacía y debe tener entre 1 y 150 caracteres.");
        }

        if(nuevoProducto.getStock() == null){
            // el stock es inválido por ser nulo
            throw new IllegalArgumentException("El stock del producto es inválido. No puede ser vacío.");
        }

        if(nuevoProducto.getPrecio() == null || nuevoProducto.getPrecio() <= 0){
            // el precio es inválido porque es nulo, vacío o menor o igual a 0
            throw new IllegalArgumentException("El precio del producto es inválido. No puede ser vacío, nulo o menor o igual a 0.");
        }

        // una vez se pasaron todas las validaciones, procedemos a crear el producto y devolverlo al controller
        return this.productoRepository.save(nuevoProducto);
    }

    public List<ProductoModel> findAll(){
        return this.productoRepository.findAll();
    }

    public ProductoModel findById(Long id){
        Optional<ProductoModel> productoDb = this.productoRepository.findById(id);
        if(productoDb.isPresent()){
            ProductoModel productoEncontrado = productoDb.get();

            return productoEncontrado;
        }else{
            return null;
        }
    }

    public ProductoModel update(ProductoModel producto, Long id){
        Optional<ProductoModel> productoDb = this.productoRepository.findById(id);
        if(productoDb.isPresent()){
            ProductoModel productoEncontrado = productoDb.get();
            productoEncontrado.setCodigo(producto.getCodigo());
            productoEncontrado.setDescripcion(producto.getDescripcion());
            productoEncontrado.setPrecio(producto.getPrecio());
            productoEncontrado.setStock(producto.getStock());

            return this.productoRepository.save(productoEncontrado);
        }else{
            return null;
        }
    }

    public void delete(Long id) throws Exception {
        Optional<ProductoModel> productoDb = this.productoRepository.findById(id);
        if(productoDb.isPresent()){
            this.productoRepository.deleteById(id); // eliminamos el producto
        }else{
            throw new Exception("No se puede eliminar el producto porque no existe");
        }
    }
}
