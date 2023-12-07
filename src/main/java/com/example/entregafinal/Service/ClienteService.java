package com.example.entregafinal.Service;

import com.example.entregafinal.Model.ClienteModel;
import com.example.entregafinal.Repository.ClienteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ClienteService {

    @Autowired
    ClienteRepository clienteRepository;

    public ClienteModel create(ClienteModel nuevoCliente){
        if(nuevoCliente.getNombre() == "" || nuevoCliente.getNombre().length() > 50){
            // el nombre es inválido, por largo o por vacio
            throw new IllegalArgumentException("El nombre es inválido. No puede ser vacío y debe tener entre 1 y 50 caracteres.");
        }

        if(nuevoCliente.getApellido() == "" || nuevoCliente.getApellido().length() > 50){
            // el apellido es inválido, por largo o por vacio
            throw new IllegalArgumentException("El apellido es inválido. No puede ser vacío y debe tener entre 1 y 50 caracteres.");
        }

        if(nuevoCliente.getNumeroDocumento() == "" || nuevoCliente.getNumeroDocumento().length() < 6 || nuevoCliente.getNumeroDocumento().length() > 9){
            // el dni es inválido, por corto, por largo o por vacio
            throw new IllegalArgumentException("El número de documento es inválido. No puede ser vacío y debe tener entre 6 y 9 números.");
        }

        return this.clienteRepository.save(nuevoCliente);
    }

    public List<ClienteModel> findAll(){
        return this.clienteRepository.findAll();
    }

    public ClienteModel findById(Long id){
        Optional<ClienteModel> clienteDb = this.clienteRepository.findById(id);
        if(clienteDb.isPresent()){
            ClienteModel clienteEncontrado = clienteDb.get();

            return clienteEncontrado;
        }else{
            return null;
        }
    }

    public ClienteModel update(ClienteModel cliente, Long id){
        Optional<ClienteModel> clienteDb = this.clienteRepository.findById(id);
        if(clienteDb.isPresent()){
            ClienteModel clienteEncontrado = clienteDb.get();
            clienteEncontrado.setNombre(cliente.getNombre());
            clienteEncontrado.setApellido(cliente.getApellido());
            clienteEncontrado.setNumeroDocumento(cliente.getNumeroDocumento());

            return this.clienteRepository.save(clienteEncontrado);
        }else{
            return null;
        }
    }

    // retornamos un booleano para saber si se elimina correctamente o no
    public void delete(Long id) throws Exception {
        Optional<ClienteModel> clienteDb = this.clienteRepository.findById(id);
        if(clienteDb.isPresent()){
            this.clienteRepository.deleteById(id); // eliminamos el cliente
        }else{
            throw new Exception("No se puede eliminar el cliente porque no existe");
        }
    }
}
