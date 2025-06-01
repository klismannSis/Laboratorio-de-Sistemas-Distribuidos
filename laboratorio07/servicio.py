# servicio.py

from spyne import Application, rpc, ServiceBase, Integer, Unicode, Float, Iterable, ComplexModel
from spyne.protocol.soap import Soap11
from spyne.server.wsgi import WsgiApplication
import threading

# Importamos directamente las referencias mutables de productos y ventas:
import productos_db

class Producto(ComplexModel):
    id = Integer
    nombre = Unicode
    precio = Float
    stock = Integer

class Venta(ComplexModel):
    id = Integer
    producto = Unicode
    cantidad = Integer
    total = Float

class ServicioVentas(ServiceBase):

    @rpc(_returns=Unicode)
    def saludar(ctx):
        return "Bienvenido al servicio de ventas en línea extendido"

    @rpc(Integer, _returns=Producto)
    def consultarProducto(ctx, id_producto):
        prod = productos_db.productos.get(id_producto)
        if prod:
            return Producto(
                id=id_producto,
                nombre=prod['nombre'],
                precio=prod['precio'],
                stock=prod['stock']
            )
        # Spyne serializa None como nil, el cliente Zeep recibirá None
        return None

    @rpc(_returns=Iterable(Producto))
    def listarProductos(ctx):
        lista = []
        for pid, prod in productos_db.productos.items():
            lista.append(
                Producto(
                    id=pid,
                    nombre=prod['nombre'],
                    precio=prod['precio'],
                    stock=prod['stock']
                )
            )
        return lista

    @rpc(Unicode, Float, Integer, _returns=Unicode)
    def agregarProducto(ctx, nombre, precio, stock):
        # Para evitar condiciones de carrera (teórico), usamos un lock
        with threading.Lock():
            nuevos_ids = list(productos_db.productos.keys())
            nuevo_id = max(nuevos_ids) + 1 if nuevos_ids else 1
            productos_db.productos[nuevo_id] = {
                "nombre": nombre,
                "precio": precio,
                "stock": stock
            }
        return f"Producto '{nombre}' agregado con ID {nuevo_id}"

    @rpc(Integer, Integer, _returns=Unicode)
    def realizarVenta(ctx, id_producto, cantidad):
        prod = productos_db.productos.get(id_producto)
        if not prod:
            return "Producto no disponible"
        if prod['stock'] < cantidad:
            return "Stock insuficiente"
        total = cantidad * prod['precio']
        # Actualizamos stock
        prod['stock'] -= cantidad
        # Para generar un ID único de venta
        with threading.Lock():
            productos_db.ultimo_id_venta += 1
            vid = productos_db.ultimo_id_venta
            productos_db.ventas[vid] = {
                "producto": prod['nombre'],
                "cantidad": cantidad,
                "total": total
            }
        return f"Venta ID {vid} realizada. Total: S/ {total}"

    @rpc(_returns=Iterable(Venta))
    def listarVentas(ctx):
        lista_ventas = []
        for vid, v in productos_db.ventas.items():
            lista_ventas.append(
                Venta(
                    id=vid,
                    producto=v['producto'],
                    cantidad=v['cantidad'],
                    total=v['total']
                )
            )
        return lista_ventas

    @rpc(Integer, _returns=Unicode)
    def cancelarVenta(ctx, id_venta):
        if id_venta not in productos_db.ventas:
            return "Venta no encontrada"
        venta = productos_db.ventas.pop(id_venta)
        # Restaurar stock al producto correspondiente:
        for pid, prod in productos_db.productos.items():
            if prod['nombre'] == venta['producto']:
                prod['stock'] += venta['cantidad']
                break
        return f"Venta ID {id_venta} cancelada y stock restaurado"

app = Application(
    [ServicioVentas],
    tns='spyne.ventas.extendido',
    in_protocol=Soap11(validator='lxml'),
    out_protocol=Soap11()
)

if __name__ == '__main__':
    from werkzeug.serving import run_simple
    print("Servicio extendido corriendo en http://localhost:8000/ventas?wsdl")
    wsgi_app = WsgiApplication(app)
    run_simple('localhost', 8000, wsgi_app)
