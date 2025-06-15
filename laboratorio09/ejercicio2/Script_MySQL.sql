-- Crear base de datos si no existe
CREATE DATABASE IF NOT EXISTS banco_transacciones;
USE banco_transacciones;

-- Crear tabla cuentas
DROP TABLE IF EXISTS cuentas;
CREATE TABLE cuentas (
    id INT PRIMARY KEY AUTO_INCREMENT,
    titular VARCHAR(100),
    saldo DECIMAL(10,2) NOT NULL
);

-- Insertar cuentas iniciales
INSERT INTO cuentas (titular, saldo) VALUES
('Erick', 400.00),
('Mabel', 1100.00),
('Luis', 600.00),
('Gabriela', 900.00);
