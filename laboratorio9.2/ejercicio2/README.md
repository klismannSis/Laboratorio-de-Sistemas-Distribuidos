
# Simulador de Transacciones Bancarias con Python y MySQL

Se simula un sistema bancario simple que permite realizar transacciones entre cuentas, consultar saldos y resetear datos a su estado inicial. Está construido en Python utilizando `mysql-connector` y una base de datos MySQL.

---

## Requisitos

- Python
- MySQL Server
- Biblioteca `mysql-connector-python`

### Instalación de dependencias

```bash
pip install mysql-connector-python
```

---

##  Configuración de la base de datos

Ejecuta este script 'Script_MySQL.sql' en tu servidor MySQL

## 🚀 Ejecución

1. Asegúrate de tener MySQL en ejecución.
2. asegurate de tener el entorno configurado (mysql-connector-python habilitado)
3. Modifica las credenciales de conexión en el archivo si es necesario:

```python
conexion = mysql.connector.connect(
    host='localhost',
    database='banco_transacciones',
    user='root',
    password='root'
)
```

4. Ejecuta el script:

```bash
python transaccion.py
```
