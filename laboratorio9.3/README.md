# Simulador de Transacciones Bancarias en Java (Swing + MySQL)

Este proyecto es una aplicación de escritorio en Java que simula operaciones bancarias básicas (consulta de cuentas, transferencias y reseteo de saldos) utilizando una interfaz gráfica Swing y una base de datos MySQL.

## Requisitos

- Java JDK 8 o superior
- MySQL Server en ejecución
- [mysql-connector-j-9.3.0.jar](https://dev.mysql.com/downloads/connector/j/) (colócalo en la carpeta `lib/` o donde prefieras)
- Base de datos y tabla `cuentas` creadas (ver ejemplo abajo)

## Configuración de la base de datos

1. Crea la base de datos y la tabla ejecutando en tu cliente MySQL:

```sql
CREATE DATABASE IF NOT EXISTS banco_transacciones;
USE banco_transacciones;

CREATE TABLE IF NOT EXISTS cuentas (
    id INT PRIMARY KEY,
    titular VARCHAR(50),
    saldo DECIMAL(10,2)
);

INSERT INTO cuentas (id, titular, saldo) VALUES
    (1, 'Ana', 400.00),
    (2, 'Luis', 1100.00),
    (3, 'Maria', 600.00),
    (4, 'Pedro', 900.00)
ON DUPLICATE KEY UPDATE titular=VALUES(titular), saldo=VALUES(saldo);


## Ejecución

1. **Descarga el conector MySQL**  
   Descarga el archivo `mysql-connector-j-9.3.0.jar` desde [MySQL Connector/J](https://dev.mysql.com/downloads/connector/j/) y colócalo en una carpeta llamada `lib` dentro de tu proyecto (o en la ubicación que prefieras).

2. **Compila el archivo Java**  
   Abre una terminal en la carpeta donde está `transac.java` y ejecuta:

   ```sh
   java -cp ".;lib/mysql-connector-j-9.3.0.jar" transac