use ventas;

-- primero insertamos los clientes en la db
-- cada uno con su nombre, apellido y dni
insert into clientes (nombre, apellido, numero_documento) values
("francisco","salvetti","11111111"),
("juan","perez","22222222"),
("maria","gonzalez","33333333"),
("sofia","ramirez","44444444");
-- select * from clientes;

-- segundo creamos los productos, ya que no tenemos fk todavia
insert into productos (descripcion,codigo,stock,precio) values
("remera A","01-rem",15,250),
("remera B","02-rem",25,275),
("pantalon A","01-pant",8,550),
("pantalon B","02-pant",12,505),
("pantalon C","03-pant",19,485),
("bufanda","01-buff",25,125);
--select * from productos;

-- creamos la tabla de factura aun con las columnas de total en null porque
-- todavia no tenemos lineas de detalles_factura para calcular el importe de cada linea
-- y luego calcular el total final de la factura
insert into factura (clientes_id, fecha_creacion, total) values
(1, now(), null),
(1, now(), null),
(3, now(), null),
(4, now(), null);
--select * from factura;

-- creamos la ultima tabla de detalles_factura, para cada detalle pasamos
-- el id de la factura a la que pertenece
-- la cantidad de productos en el detalle_factura actual
-- el id del producto involucrado en el detalle_factura actual
-- el importe total de la linea, que va a contribuir al total de la factura a la que pertenece
-- este ultimo dato se ingresa con un select calculando el precio * cantidad
insert into detalles_factura(factura_id, cantidad_productos, productos_id, importe) values
(1,1,2,(select precio from productos as a where a.id = 2) * 1),
(1,2,3,(select precio from productos as a where a.id = 3) * 2),
(1,2,6,(select precio from productos as a where a.id = 6) * 2),
(2,1,2,(select precio from productos as a where a.id = 2) * 1),
(2,3,5,(select precio from productos as a where a.id = 5) * 3),
(3,2,1,(select precio from productos as a where a.id = 1) * 2),
(3,3,2,(select precio from productos as a where a.id = 2) * 3),
(3,2,3,(select precio from productos as a where a.id = 3) * 2),
(3,3,5,(select precio from productos as a where a.id = 5) * 3),
(3,4,6,(select precio from productos as a where a.id = 6) * 4),
(4,6,4,(select precio from productos as a where a.id = 4) * 6);
-- select * from detalles_factura;

-- por último actualizamos el importe final de cada factura, listando todos los detalles_factura
-- pertenecientes a cada factura y sumando cada importe de cada línea.
update factura set total = (select SUM(importe) from detalles_factura where factura_id = 1) where id = 1;
update factura set total = (select SUM(importe) from detalles_factura where factura_id = 2) where id = 2;
update factura set total = (select SUM(importe) from detalles_factura where factura_id = 3) where id = 3;
update factura set total = (select SUM(importe) from detalles_factura where factura_id = 4) where id = 4;