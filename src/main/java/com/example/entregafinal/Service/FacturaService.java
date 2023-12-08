package com.example.entregafinal.Service;

import com.example.entregafinal.Model.ClienteModel;
import com.example.entregafinal.Model.DetallesFacturaModel;
import com.example.entregafinal.Model.FacturaModel;
import com.example.entregafinal.Model.ProductoModel;
import com.example.entregafinal.Repository.ClienteRepository;
import com.example.entregafinal.Repository.FacturaRepository;
import com.example.entregafinal.Repository.ProductoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class FacturaService {

    // importamos los 3 repositorios para hacer comprobaciones
    @Autowired
    FacturaRepository facturaRepository;
    @Autowired
    ClienteRepository clienteRepository;
    @Autowired
    ProductoRepository productoRepository;

    public FacturaModel create(FacturaModel nuevaFactura) throws Exception{

        // realizamos el control sobre el cliente definido en la factura
        boolean checkCliente = checkCliente(nuevaFactura.getCliente());
        if(!checkCliente){
            throw new IllegalArgumentException("La factura no se pudo crear, el cliente no existe.");
        }

        // realizamos el control sobre los productos incluidos en la factura
        List<DetallesFacturaModel> lineas = nuevaFactura.getLineas();
        boolean checkProductos = checkProductos(lineas);
        if(!checkProductos){
            throw new IllegalArgumentException("La factura no se pudo crear, revise los productos");
        }

        return null;
    }

    private boolean checkCliente(ClienteModel cliente){
        // seteamos la respuesta de esta funcion en false de manera preventiva
        boolean respuesta = false;


        Optional<ClienteModel> clienteDb = clienteRepository.findById(cliente.getId());
        if(clienteDb.isPresent()){
            respuesta = true;
        }

        return respuesta;
    }

    private boolean checkProductos(List<DetallesFacturaModel> lineas) throws Exception{
        // seteamos la respuesta inicialmente en true ya que las condiciones adversas van a cortar
        // el procesamiento del metodo checkProductos y van a devolver una excepción
        ProductoModel producto = null;
        int cantidad = 0;

        for (int i = 0; i < lineas.size(); i++) {

            // en la caja productoDb ponemos el posible producto correspondiente a la linea que estamos recorriendo
            Optional<ProductoModel> productoDb = productoRepository.findById(lineas.get(i).getProductos().getId());
            if(!productoDb.isPresent()){
                // devolvemos una excepción en vez de retornar false al metodo create porque asi tenemos
                // una mejor precisión sobre el error que hace que NO se pueda crear la factura. En este caso
                // es porque el producto no existe
                throw new IllegalArgumentException("No se pudo crear la facutra. El producto ID: "+lineas.get(i).getProductos().getId()+" no existe.");
            }else{
                // si el producto esta presente en la caja productoDb, lo obtenermos para seguir haciendo comprobaciones
                producto = productoDb.get();

                // obtenemos la cantidad deseada de la linea que estamos recorriendo
                cantidad = lineas.get(i).getCantidadProductos();
            }

            // hacemos un control sobre el stock del producto encontrado
            if(producto.getStock() <= 0 || cantidad > producto.getStock()){
                // devolvemos una excepción en vez de retornar false al metodo create porque asi tenemos
                // una mejor precisión sobre el error que hace que NO se pueda crear la factura. En este caso
                // es porque el producto no tiene sl stock suficiente para realizar la factura
                throw new IllegalArgumentException("No se pudo crear la factura. El producto ID: "+producto.getId()+" - "+producto.getDescripcion()+" no tiene stock suficiente para realizar la operación.");
            }
        }

        // si el metodo checkProductos llega hasta este punto es porque todos los productos recorridos
        // existen y todos tienen el stock suficiente para afrontar la operación de crear la factura
        return true;
    }

    /*public FacturaModel create(FacturaModel nuevaFactura) throws Exception {

        // instanciamos una factura nueva llamado factura para llevar adelante la los controles previos a la creación en la db
        FacturaModel factura = new FacturaModel();

        // implementamos los controles pertinentes para cada campo de la clase Factura
        Optional<ClienteModel> clienteDb = clienteRepository.findById(nuevaFactura.getCliente().getId());
        ClienteModel cliente = null;
        if(clienteDb.isPresent()){
            cliente = clienteDb.get();
        }else{
            throw new IllegalArgumentException("El cliente designado no se pudo encontrar");
        }

        LocalDate fecha = nuevaFactura.getFecha_creacion();
        Double total = nuevaFactura.getTotal();

        // el control sobre la fecha No se hace acá por completo, hay una excepción personalizada que se desarrolla en
        // dentro del paquete model que se encarga del formato propiamente dicho, acá solo se testea
        // que la fecha de nacimiento sea distinta de vacio.
        if(String.valueOf(fecha) == null){
            throw new IllegalArgumentException("La fecha de la factura no puede ser vacía");
        }

        // una vez pasamos los controles asignamos con los setters provenientes de @Data los datos que recibe el método create
        nuevaFactura.setFecha_creacion(fecha);
        nuevaFactura.setCliente(cliente);
        nuevaFactura.setTotal(total);

        // retornamos la factura creada
        return this.facturaRepository.save(nuevaFactura);
    }*/

    public List<FacturaModel> findAll(){
        return this.facturaRepository.findAll();
    }

    public FacturaModel findById(Long id){
        Optional<FacturaModel> facturaDb = this.facturaRepository.findById(id);
        if(facturaDb.isPresent()){
            FacturaModel facturaEncontrada = facturaDb.get();

            return facturaEncontrada;
        }else{
            return null;
        }
    }

    public FacturaModel update(FacturaModel factura, Long id){
        Optional<FacturaModel> facturaDb = this.facturaRepository.findById(id);
        if(facturaDb.isPresent()){
            FacturaModel facturaEncontrada = facturaDb.get();
            facturaEncontrada.setCliente(factura.getCliente());
            facturaEncontrada.setTotal(factura.getTotal());
            facturaEncontrada.setFecha_creacion(factura.getFecha_creacion());

            return this.facturaRepository.save(facturaEncontrada);
        }else{
            return null;
        }
    }

    public void delete(Long id) throws Exception {
        Optional<FacturaModel> facturaDb = this.facturaRepository.findById(id);
        if(facturaDb.isPresent()){
            this.facturaRepository.deleteById(id); // eliminamos el cliente
        }else{
            throw new Exception("No se puede eliminar la factura porque no existe");
        }
    }
}
