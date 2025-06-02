from spyne import Application, rpc, ServiceBase, Unicode, Integer, Float, Array, ComplexModel
from spyne.protocol.soap import Soap11 #Se importa el protocolo SOAP 1.1 para usarse como protocolo de comunicación.
from spyne.server.wsgi import WsgiApplication

# modelo para representar un producto
class Producto(ComplexModel):
    id = Integer      
    nombre = Unicode  
    precio = Float    
    stock = Integer   #

# modelo para representar una venta
class Venta(ComplexModel):
    id = Integer         # Identificador 
    producto_id = Integer  
    producto = Unicode   
    cantidad = Integer   # Cantidad vendida
    total = Float        # Total a pagar 

# Definición del servicio de ventas con operaciones SOAP
class ServicioVentas(ServiceBase):
    # Listas para almacenar productos y ventas en memoria
    productos = []
    ventas = []
    # Contadores para asignar IDs unicos a productos y ventas
    contador_producto = 1
    contador_venta = 1


#Cada metodo decorado con @rpc define una operación del servicio SOAP con WSDL.
    @rpc(_returns=Unicode)
    def saludar(ctx):
        return "¡Bienvenido al servicio de ventas en línea!"

    @rpc(Unicode, Float, Integer, _returns=Unicode)
    def agregarProducto(ctx, nombre, precio, stock):
        
        """Añade un nuevo producto a la lista con un ID unico."""
        producto = Producto(id=ServicioVentas.contador_producto, nombre=nombre, precio=precio, stock=stock)
        ServicioVentas.productos.append(producto)
        ServicioVentas.contador_producto += 1
        return f"Producto '{nombre}' agregado correctamente."

    @rpc(_returns=Array(Producto))
    def listarProductos(ctx):
        """Devuelve la lista de productos disponibles en el inventario."""
        return ServicioVentas.productos

    @rpc(Integer, Integer, _returns=Unicode)
    def realizarVenta(ctx, producto_id, cantidad):
        """Realiza una venta si hay suficiente stock del producto."""
        for producto in ServicioVentas.productos:
            if producto.id == producto_id:
                if producto.stock >= cantidad:
                    producto.stock -= cantidad
                    total = producto.precio * cantidad
                    venta = Venta(
                        id=ServicioVentas.contador_venta,
                        producto_id=producto.id,
                        producto=producto.nombre,
                        cantidad=cantidad,
                        total=total
                    )
                    ServicioVentas.ventas.append(venta)
                    ServicioVentas.contador_venta += 1
                    return f"Venta realizada: {cantidad} x {producto.nombre} = S/ {total}"
                else:
                    return "Stock insuficiente"
        return "Producto no encontrado"

    @rpc(_returns=Array(Venta))
    def listarVentas(ctx):
        """Devuelve la lista de ventas realizadas."""
        return ServicioVentas.ventas

    @rpc(Integer, _returns=Unicode)
    def cancelarVenta(ctx, venta_id):
        """Cancela una venta y repone el stock del producto si existe."""
        for venta in ServicioVentas.ventas:
            if venta.id == venta_id:
                # Reponer stock del producto cancelado
                for producto in ServicioVentas.productos:
                    if producto.id == venta.producto_id:
                        producto.stock += venta.cantidad
                        break
                ServicioVentas.ventas.remove(venta)
                return f"Venta con ID {venta_id} cancelada."
        return "Venta no encontrada"
    



#Aquí se especifica que tanto las solicitudes (in_protocol) 
# como las respuestas (out_protocol) se manejarán usando SOAP 1.1.

# Configuración del servidor SOAP
app = Application(
    [ServicioVentas],  # Clase del servicio
    tns='ventas.soap',  # Espacio de nombres del servicio
    in_protocol=Soap11(validator='lxml'),  
    out_protocol=Soap11()  
)

if __name__ == '__main__':
    from wsgiref.simple_server import make_server
    wsgi_app = WsgiApplication(app)
    print("Servidor SOAP iniciado en http://localhost:8000/ventas")
    server = make_server('localhost', 8000, wsgi_app)
    server.serve_forever()  # Inicia el servidor para manejar solicitudes SOAP