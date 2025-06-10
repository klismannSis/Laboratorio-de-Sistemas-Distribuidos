package main.java.com.miempresa.jdbcapp.util;

import java.sql.*;
import java.util.logging.*;

public class DbConnection {
    private static final String URL = "jdbc:mysql://localhost:3306/Empresa_SD"
        + "?serverTimezone=UTC&useSSL=false&allowPublicKeyRetrieval=true";
    private static final String USER = "sd_user";
    private static final String PASS = "TuPasswordSeguro";
    private static final Logger LOGGER = Logger.getLogger(DbConnection.class.getName());

    private DbConnection() { }

    public static Connection getConnection() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            return DriverManager.getConnection(URL, USER, PASS);
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error conectando a BD", e);
            return null;
        }
    }
}
