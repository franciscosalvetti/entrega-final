package com.example.entregafinal.Service;

import com.example.entregafinal.Model.ClienteModel;
import com.example.entregafinal.Model.FacturaModel;
import com.example.entregafinal.Repository.ClienteRepository;
import com.example.entregafinal.Repository.FacturaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class FacturaService {
    @Autowired
    FacturaRepository facturaRepository;
    @Autowired
    ClienteRepository clienteRepository;

    public FacturaModel create(FacturaModel nuevaFactura) throws Exception {

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
    }

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
