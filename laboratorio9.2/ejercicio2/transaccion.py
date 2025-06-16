import mysql.connector
from mysql.connector import Error

def conectar():
    return mysql.connector.connect(
        host='localhost',
        database='banco_transacciones',
        user='root',
        password='root'
    )

def mostrar_todas_las_cuentas(cursor):
    print("\n ESTADO GENERAL DE CUENTAS:")
    cursor.execute("SELECT id, titular, saldo FROM cuentas ORDER BY id")
    cuentas = cursor.fetchall()
    print("┌────┬────────────┬─────────────┐")
    print("│ ID │  Titular   │  Saldo (S/.)│")
    print("├────┼────────────┼─────────────┤")
    for cuenta in cuentas:
        print(f"│ {cuenta[0]:<2} │ {cuenta[1]:<10} │ {cuenta[2]:>11.2f} │")
    print("└────┴────────────┴─────────────┘")

def mostrar_estado_cuenta(cursor):
    id_cuenta = input(" Ingrese el ID de la cuenta: ")
    cursor.execute("SELECT titular, saldo FROM cuentas WHERE id = %s", (id_cuenta,))
    resultado = cursor.fetchone()
    if resultado:
        print(f" Titular: {resultado[0]} | Saldo disponible: S/. {resultado[1]:.2f}")
    else:
        print(" Cuenta no encontrada.")

def hacer_transaccion(conexion, cursor):
    try:
        origen = input(" ID de cuenta que envía el dinero: ")
        destino = input(" ID de cuenta que recibe el dinero: ")
        monto = float(input(" Monto a transferir: "))

        # Validaciones básicas
        if origen == destino:
            print(" No puedes transferir a la misma cuenta.")
            return

        cursor.execute("SELECT saldo FROM cuentas WHERE id = %s", (origen,))
        fila = cursor.fetchone()
        if not fila:
            print(" Cuenta de origen no encontrada.")
            return
        saldo_origen = fila[0]

        if saldo_origen < monto:
            print(" Saldo insuficiente.")
            return

        cursor.execute("SELECT id FROM cuentas WHERE id = %s", (destino,))
        if not cursor.fetchone():
            print(" Cuenta de destino no encontrada.")
            return

        cursor.execute("UPDATE cuentas SET saldo = saldo - %s WHERE id = %s", (monto, origen))
        cursor.execute("UPDATE cuentas SET saldo = saldo + %s WHERE id = %s", (monto, destino))
        
        conexion.commit()
        print(" Transacción realizada con éxito.")

    except Exception as e:
        conexion.rollback()
        print(f" Error en la transacción: {e}\n Rollback ejecutado.")

def resetear_cuentas(cursor, conexion):
    try:
        cursor.execute("UPDATE cuentas SET saldo = CASE id WHEN 1 THEN 400.00 WHEN 2 THEN 1100.00 WHEN 3 THEN 600.00 WHEN 4 THEN 900.00 END")
        conexion.commit()
        print(" Saldos reiniciados correctamente.")
    except Exception as e:
        conexion.rollback()
        print(f" Error al resetear: {e}")

def menu():
    try:
        conexion = conectar()
        if not conexion.is_connected():
            print(" No se pudo conectar a la base de datos.")
            return

        cursor = conexion.cursor()
        mostrar_todas_las_cuentas(cursor) 
        
        while True:
            print("\n MENÚ DE OPERACIONES")
            print("1. Ver estado de cuenta")
            print("2. Hacer transacción")
            print("3. Resetear saldos")
            print("4. Mostrar todos los saldos")
            print("5. Salir")
            opcion = input("Seleccione una opción: ")

            if opcion == '1':
                mostrar_estado_cuenta(cursor)
            elif opcion == '2':
                hacer_transaccion(conexion, cursor)
            elif opcion == '3':
                resetear_cuentas(cursor, conexion)
            elif opcion == '4' :
                mostrar_todas_las_cuentas(cursor)
            elif opcion == '5':
                print(" Saliendo del programa...")
                break
            else:
                print(" Opción no válida.")

    except Error as e:
        print(f" Error general: {e}")

    finally:
        if conexion.is_connected():
            cursor.close()
            conexion.close()
            print(" Conexión cerrada.")

if __name__ == "__main__":
    menu()
