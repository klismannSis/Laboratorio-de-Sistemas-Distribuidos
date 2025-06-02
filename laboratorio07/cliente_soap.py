from zeep import Client


#El servidor responde a peticiones SOAP en la ruta: http://localhost:8000/ventas?wsdl
client = Client("http://localhost:8000/ventas?wsdl")
#es un cliente SOAP que consume el WSDL (Web Services Description Language) 
# del servidor, generando automáticamente métodos remotos disponibles.


print(client.service.saludar())

# 2. Listar todos los productos
print("----- Listar Productos -----")
productos = client.service.listarProductos()
if productos:
    for p in productos:
        print(f"{p.id} - {p.nombre} - S/ {p.precio} - Stock: {p.stock}")
else:
    print("(No hay productos)")

# 3. Agregar un nuevo producto
resp_agregar = client.service.agregarProducto("Monitor", 800.0, 5)
print(resp_agregar)

# 4. Listar productos actualizados
print("----- Listar Productos Actualizado -----")
productos2 = client.service.listarProductos()
if productos2:
    for p in productos2:
        print(f"{p.id} - {p.nombre} - S/ {p.precio} - Stock: {p.stock}")
else:
    print("(No hay productos)")

# 5. Realizar una venta 
resp_venta = client.service.realizarVenta(1, 3)
print(resp_venta)

# Extraer ID de venta desde la respuesta
venta_id = None
parts = resp_venta.split()
for i, token in enumerate(parts):
    if token == "ID" and i + 1 < len(parts):
        try:
            venta_id = int(parts[i+1])
        except ValueError:
            pass

# 6. Listar ventas
print("----- Listar Ventas -----")
ventas = client.service.listarVentas()
if ventas:
    for v in ventas:
        print(f"ID {v.id} - Producto ID {v.producto_id} ({v.producto}) - {v.cantidad} unidades - Total: S/ {v.total}")
else:
    print("(No hay ventas)")

# 7. Cancelar venta
if venta_id is not None:
    resp_cancelar = client.service.cancelarVenta(venta_id)
    print(resp_cancelar)
else:
    print("No se pudo obtener el ID de la venta.")

# 8. Ver ventas después de cancelar
print("----- Listar Ventas Después de Cancelar -----")
ventas2 = client.service.listarVentas()
if ventas2:
    for v in ventas2:
        print(f"ID {v.id} - Producto ID {v.producto_id} ({v.producto}) - {v.cantidad} unidades - Total: S/ {v.total}")
else:
    print("(No hay ventas)")
