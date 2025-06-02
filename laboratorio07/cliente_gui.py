import tkinter as tk
from tkinter import messagebox, simpledialog
from zeep import Client

# Cliente Zeep
client = Client("http://localhost:8000/ventas?wsdl")

# Funciones que llaman un servicio SOAP para interactuar con el servidor
def saludar():
    saludo = client.service.saludar()
    messagebox.showinfo("Saludo", saludo)

def listar_productos():
    productos = client.service.listarProductos()
    text_area.delete("1.0", tk.END)
    if productos:
        text_area.insert(tk.END, "ID - Nombre - Precio - Stock\n")
        text_area.insert(tk.END, "-"*40 + "\n")
        for p in productos:
            text_area.insert(tk.END, f"{p.id} - {p.nombre} - S/ {p.precio} - Stock: {p.stock}\n")
    else:
        text_area.insert(tk.END, "(No hay productos)\n")

def agregar_producto():
    nombre = simpledialog.askstring("Agregar Producto", "Nombre del producto:")
    precio = simpledialog.askfloat("Agregar Producto", "Precio:")
    stock = simpledialog.askinteger("Agregar Producto", "Stock:")
    if nombre and precio is not None and stock is not None:
        resultado = client.service.agregarProducto(nombre, precio, stock)
        messagebox.showinfo("Resultado", resultado)
        listar_productos()

def realizar_venta():
    id_prod = simpledialog.askinteger("Realizar Venta", "ID del producto:")
    cantidad = simpledialog.askinteger("Realizar Venta", "Cantidad:")
    if id_prod and cantidad:
        resultado = client.service.realizarVenta(id_prod, cantidad)
        messagebox.showinfo("Resultado Venta", resultado)

def listar_ventas():
    ventas = client.service.listarVentas()
    text_area.delete("1.0", tk.END)
    if ventas:
        text_area.insert(tk.END, "ID Venta - ID Producto - Producto - Cantidad - Total\n")
        text_area.insert(tk.END, "-"*55 + "\n")
        for v in ventas:
            text_area.insert(tk.END, f"{v.id} - {v.producto_id} - {v.producto} - {v.cantidad} - S/ {v.total}\n")
    else:
        text_area.insert(tk.END, "(No hay ventas)\n")

def cancelar_venta():
    id_venta = simpledialog.askinteger("Cancelar Venta", "ID de la venta:")
    if id_venta:
        resultado = client.service.cancelarVenta(id_venta)
        messagebox.showinfo("Cancelación", resultado)

# Interfaz Tkinter
ventana = tk.Tk()
ventana.title("Cliente SOAP - Ventas en Línea")

frame = tk.Frame(ventana)
frame.pack(pady=10)

tk.Button(frame, text="Saludar", width=20, command=saludar).grid(row=0, column=0, padx=5, pady=5)
tk.Button(frame, text="Listar Productos", width=20, command=listar_productos).grid(row=0, column=1, padx=5, pady=5)
tk.Button(frame, text="Agregar Producto", width=20, command=agregar_producto).grid(row=1, column=0, padx=5, pady=5)
tk.Button(frame, text="Realizar Venta", width=20, command=realizar_venta).grid(row=1, column=1, padx=5, pady=5)
tk.Button(frame, text="Listar Ventas", width=20, command=listar_ventas).grid(row=2, column=0, padx=5, pady=5)
tk.Button(frame, text="Cancelar Venta", width=20, command=cancelar_venta).grid(row=2, column=1, padx=5, pady=5)

text_area = tk.Text(ventana, height=15, width=70)
text_area.pack(padx=10, pady=10)

ventana.mainloop()
