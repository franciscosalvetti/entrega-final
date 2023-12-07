create database ventas;
use ventas;

-- ejecutadas todas seguidas no pude hacerlas funcionar,
-- pero ejecutandolas de a una por vez, se crean correctamente, no pude saber bien por qué

-- creamos la tabla de clientes
create table clientes(
	id INT primary key auto_increment,
    nombre varchar(75),
    apellido varchar(75),
    numero_documento varchar(11)
);

-- creamos la tabla de productos
create table productos(
	id int primary key auto_increment,
    descripcion varchar(150),
    codigo varchar(50),
    stock int,
    precio double
);

-- creamos la tabla de factura
-- definimos la constraint para la fk de clientes
create table factura(
	id int primary key auto_increment,
    clientes_id int,
    fecha_creacion datetime,
    total double,

    constraint fk_clientes_id foreign key(clientes_id) references clientes(id)
);

-- creamos la tabla de detalles_factura
-- con las fk de factura y productos
create table detalles_factura(
    id int primary key auto_increment,
    factura_id int,
    cantidad_productos int,
    productos_id int,
    importe double,

    constraint fk_factura_id foreign key(factura_id) references factura(id),
    constraint fk_productos_id foreign key(productos_id) references productos(id)
);

--select * from clientes;
--select * from detalles_factura;
--select * from factura;
--select * from productos;