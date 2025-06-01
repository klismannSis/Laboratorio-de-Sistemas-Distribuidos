# cliente_soap.py

from zeep import Client

client = Client("http://localhost:8000/ventas?wsdl")

# 1. Saludo
print(client.service.saludar())

# 2. Listar todos los productos
print("----- Listar Productos -----")
productos = client.service.listarProductos()
if productos is not None:
    for p in productos:
        print(f"{p.id} - {p.nombre} - S/ {p.precio} - Stock: {p.stock}")
else:
    print("(No hay productos)")

# 3. Agregar un nuevo producto
resp_agregar = client.service.agregarProducto("Monitor", 800.0, 5)
print(resp_agregar)

# 4. Listar productos de nuevo (con el Monitor agregado)
print("----- Listar Productos Actualizado -----")
productos2 = client.service.listarProductos()
if productos2 is not None:
    for p in productos2:
        print(f"{p.id} - {p.nombre} - S/ {p.precio} - Stock: {p.stock}")
else:
    print("(No hay productos)")

# 5. Realizar una venta (ej.: 3 Laptops)
resp_venta = client.service.realizarVenta(1, 3)
print(resp_venta)

# Extraigo el ID de venta del texto de la respuesta
# Por ej. "Venta ID 2 realizada. Total: S/ 7500.0"
venta_id = None
# Buscamos la secuencia "Venta ID <número>"
parts = resp_venta.split()
for i, token in enumerate(parts):
    if token == "ID" and i + 1 < len(parts):
        try:
            venta_id = int(parts[i+1])
        except ValueError:
            pass

# 6. Listar ventas actuales
print("----- Listar Ventas -----")
ventas = client.service.listarVentas()
if ventas is not None and len(ventas) > 0:
    for v in ventas:
        print(f"ID {v.id} - {v.cantidad}x {v.producto} = S/ {v.total}")
else:
    print("(No hay ventas)")

# 7. Cancelar la venta que acabamos de hacer
if venta_id is not None:
    resp_cancelar = client.service.cancelarVenta(venta_id)
    print(resp_cancelar)
else:
    print("No pude obtener el ID de la venta, no la cancelo.")
# ------------------------------------------------


# 8. Listar ventas después de cancelar la venta
print("----- Listar Ventas Después de Cancelar -----")
ventas2 = client.service.listarVentas()
if ventas2 is not None and len(ventas2) > 0:
    for v in ventas2:
        print(f"ID {v.id} - {v.cantidad}x {v.producto} = S/ {v.total}")
else:
    print("(No hay ventas)")
