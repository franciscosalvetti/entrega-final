package com.example.entregafinal.Service;

import com.example.entregafinal.DTO.DetallesFacturaDTO;
import com.example.entregafinal.DTO.FacturaDTO;
import com.example.entregafinal.Model.*;
import com.example.entregafinal.Repository.ClienteRepository;
import com.example.entregafinal.Repository.DetallesFacturaRepository;
import com.example.entregafinal.Repository.FacturaRepository;
import com.example.entregafinal.Repository.ProductoRepository;
import com.google.gson.Gson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;

@Service
public class FacturaService {

    // importamos los 3 repositorios para hacer comprobaciones
    @Autowired
    FacturaRepository facturaRepository;
    @Autowired
    ClienteRepository clienteRepository;
    @Autowired
    ProductoRepository productoRepository;
    @Autowired
    DetallesFacturaRepository detallesFacturaRepository;

    public FacturaDTO create(FacturaModel datosFactura) throws Exception{

        // realizamos el control sobre el cliente definido en la factura
        boolean checkCliente = checkCliente(datosFactura.getCliente());
        if(!checkCliente){
            throw new IllegalArgumentException("La factura no se pudo crear, el cliente no existe.");
        }

        // realizamos le control sobre la existencia del campo de lineas en los datos entrantes
        if(datosFactura.getLineas() == null || datosFactura.getLineas().size() <= 0){
            // vemos que la factura tiene lineas vacío o directamente no tiene líneas de productos
            throw new IllegalArgumentException("La facturano se pudo crear, no incluye productos.");
        }else{
            // realizamos el control sobre los productos incluidos en la factura
            List<DetallesFacturaModel> lineas = datosFactura.getLineas();
            boolean checkProductos = checkProductos(lineas);
            if(!checkProductos){
                throw new IllegalArgumentException("La factura no se pudo crear, revise los productos");
            }
        }

        // creamos una nueva instancia
        FacturaModel nuevaFactura = new FacturaModel();

        // como ya pasamos el chequeo sobre el cliente, asignamos el cliente
        // que recibimos en la fima del metodo a la variable factura
        nuevaFactura.setCliente(datosFactura.getCliente());

        // mediante la funcion obtenerTotal, a la que le pasamos como parámetro las líneas que
        // vienen dentro de la variable en la firma del método, obtenermos el monto total del la factura
        nuevaFactura.setTotal(obtenerTotal(datosFactura.getLineas()));

        // asignamos la fecha de creación con el resultado del método obtenerFechaActual
        // que se encargará de decidir si obtiene la fecha de un servicio externo o del sistema directamente
        nuevaFactura.setFecha_creacion(obtenerFechaActual());

        nuevaFactura = this.facturaRepository.save(nuevaFactura);

        generarDetallesFactura(datosFactura.getLineas(), nuevaFactura);

        descontarStockProductos(datosFactura.getLineas());

        return toDTO(nuevaFactura);
    }

    private FacturaDTO toDTO(FacturaModel factura){
        FacturaDTO facturaDTO = new FacturaDTO();

        facturaDTO.setId(factura.getId());
        facturaDTO.setCliente(factura.getCliente());
        facturaDTO.setTotal(factura.getTotal());
        facturaDTO.setFecha(factura.getFecha_creacion());


        List<DetallesFacturaModel> lineas = this.detallesFacturaRepository.findAll();
        List<DetallesFacturaModel> lineasFactura = new ArrayList<>();

        if(!lineas.isEmpty()){
            for (int i = 0; i < lineas.size(); i++) {
                if(lineas.get(i).getFactura().getId() == factura.getId()){
                    lineasFactura.add(lineas.get(i));
                }
            }
        }

        facturaDTO.setLineas(crearLineasDTO(lineasFactura));

        return facturaDTO;
    }

    private List<DetallesFacturaDTO> crearLineasDTO(List<DetallesFacturaModel> lineas){
        List<DetallesFacturaDTO> lineasDTO = new ArrayList<>();

        if(!lineas.isEmpty()){
            for (DetallesFacturaModel linea : lineas){
                DetallesFacturaDTO dto = new DetallesFacturaDTO();
                dto.setId(linea.getId());
                dto.setCantidad(linea.getCantidadProductos());
                dto.setProducto_id(linea.getProductos().getId());
                dto.setImporte(linea.getImporte());

                lineasDTO.add(dto);
            }
        }

        return lineasDTO;
    }

    // separamos la logica de la obtención de la fecha en un metodo aparte para que el código del método
    // principal no quede tan cargado
    private LocalDateTime obtenerFechaActual(){

        LocalDateTime fechaApp = LocalDateTime.now();

        // vamos a obtener la fecha de un servicio externo
        RestTemplate restTemplate = new RestTemplate();
        // definimos la url, en este caso es de un servicio encontrado en la web, con un apikey generada en su sitio
        // y le pedimos por medio del método zona, para la zona de America/Argentina/Buenos_Aires
        String resultado = null;
        final String url = "http://api.timezonedb.com/v2.1/get-time-zone?key=MU852FWV2O1E&format=json&by=zone&zone=America/Argentina/Buenos_Aires";
        try{
            resultado = restTemplate.getForObject(url, String.class);
            // instanciamos una dependencia que nos permita manejar json
            Gson gson = new Gson();

            // por medio de Gson vamos a convertir al json que nos devuelve el servicio de la hora en un
            // objeto de java llamado HorarioModel
            // Convertir el JSON a un objeto Java
            HorarioModel horario = gson.fromJson(resultado, HorarioModel.class);

            // Acceder a los campos del objeto Java
            // creamos la variable status para ver si la petición esta ok o no
            String estadoPeticion = horario.getEstadoPeticion();

            // creamos la variable fechaApi para almacenar la posible fecha, dependiendo si la
            // petición se realizó existosamente o no
            String fechaApi = horario.getFecha();

            if(Objects.equals(estadoPeticion, "OK")){
                return LocalDateTime.parse(fechaApi);
            }else{
                return fechaApp;
            }
        }catch (Exception e){
            return fechaApp;
        }
    }

    // creamos un metodo para que una vez generada la factura y los detalles de factura
    // se descuenten las unidades compradas de los productos correspondientes
    private void descontarStockProductos(List<DetallesFacturaModel> lineas){
        // creamos una variable producto para ir obteniendo los productos presentes en la lista
        ProductoModel producto = null;

        for (int i = 0; i < lineas.size(); i++) {
            // en la caja productoDb ponemos el posible producto correspondiente a la linea que estamos recorriendo
            Optional<ProductoModel> productoDb = productoRepository.findById(lineas.get(i).getProductos().getId());
            // directamente obtenemos el producto de la caja productoDb porque estos datos han pasado
            // las validaciones previas a la hora de crear la factura en la db
            producto = productoDb.get();

            // seteamos el nuevo stock del producto de la linea
            // que corresponde al stock que tenia el producto menos la cantidad señalada en la linea
            producto.setStock(producto.getStock() - lineas.get(i).getCantidadProductos());

            // guardamos el producto para actualizar el stock en la db
            this.productoRepository.save(producto);
        }
    }

    // definimos un método para generar en la db los detalles de factura correspondientes
    // a las lineas incluidas en la info entrante al método create, y además para asignarle
    // el id de la factura recientemente creada a los nuevos detallesFactura a crear
    private void generarDetallesFactura(List<DetallesFacturaModel> lineas, FacturaModel factura){

        // creamos una variable producto para ir obteniendo los productos presentes en la lista
        ProductoModel producto = null;
        // creamos una variable monto para ir acumulando los importes de cada producto
        Double importeLinea = Double.valueOf(0);

        for (int i = 0; i < lineas.size(); i++) {
            // al entrar al ciclo reseteamos la variable detallesFactura
            // para generar una nueva linea con nuevos datos
            DetallesFacturaModel detallesFactura = new DetallesFacturaModel();

            // en la caja productoDb ponemos el posible producto correspondiente a la linea que estamos recorriendo
            Optional<ProductoModel> productoDb = productoRepository.findById(lineas.get(i).getProductos().getId());
            // directamente obtenemos el producto de la caja productoDb porque estos datos han pasado
            // las validaciones previas a la hora de crear la factura en la db
            producto = productoDb.get();

            // calculamos el importe de la linea resultante entre la cantidad y el precio del producto
            // de la linea que estamos recorriendo
            importeLinea = lineas.get(i).getCantidadProductos() * producto.getPrecio();

            // asignación de campos para crear el detallesFactura correspondiente a la linea que estamos recorriendo

            // seteamos la factura
            detallesFactura.setFactura(factura);

            // seteamos el importe
            detallesFactura.setImporte(importeLinea);

            // seteamos la cantidad del producto
            detallesFactura.setCantidadProductos(lineas.get(i).getCantidadProductos());

            // seteamos el producto
            detallesFactura.setProductos(producto);

            // generamos el detallesFactura correspondiente a la línea que estamos recorriendo
            this.detallesFacturaRepository.save(detallesFactura);
        }
    }

    // definimos el método obtenerTotal para poder obtener le monto total que la factura
    // debe llevar cuando se vaya a crear en la db
    private Double obtenerTotal(List<DetallesFacturaModel> lineas){

        // creamos una variable producto para ir obteniendo los productos presentes en la lista
        ProductoModel producto = null;
        // creamos una variable monto para ir acumulando los importes de cada producto
        Double monto = Double.valueOf(0);

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
                monto += lineas.get(i).getCantidadProductos() * producto.getPrecio();
            }
        }

        return monto;
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

    // método que devuelve todas las facturas que existen cargadas se encarga de
    // conseguir todas las FacturaModel y convertirlas en FacturaDTO antes de devolverlo
    public List<FacturaDTO> findAll(){
        List<FacturaModel> listaFacturas = this.facturaRepository.findAll();
        List<FacturaDTO> listaFacturasDTO = new ArrayList<>();

        for (int i = 0; i < listaFacturas.size(); i++) {
            listaFacturasDTO.add(toDTO(listaFacturas.get(i)));
        }

        return listaFacturasDTO;
    }

    // método usado para encontrar una factura en particular según el ID ingresado por parámetro
    public FacturaDTO findById(Long id){
        Optional<FacturaModel> facturaDb = this.facturaRepository.findById(id);
        if(facturaDb.isPresent()){
            FacturaModel facturaEncontrada = facturaDb.get();

            return toDTO(facturaEncontrada);
        }else{
            throw new IllegalArgumentException("La factura no existe.");
        }
    }


    public FacturaDTO update(FacturaModel factura, Long id){
        Optional<FacturaModel> facturaDb = this.facturaRepository.findById(id);
        if(facturaDb.isPresent()){
            FacturaModel facturaEncontrada = facturaDb.get();
            facturaEncontrada.setCliente(factura.getCliente());
            facturaEncontrada.setTotal(factura.getTotal());
            facturaEncontrada.setFecha_creacion(factura.getFecha_creacion());

            return toDTO(this.facturaRepository.save(facturaEncontrada));
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
