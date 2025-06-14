package app;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class JRollback {

    public static void main(String[] args) {
        Connection connection = Database.getConnection();

        PreparedStatement stmt1 = null;
        PreparedStatement stmt2 = null;

        try {
            connection.setAutoCommit(false); // Desactiva autocommit

            stmt1 = connection.prepareStatement("INSERT INTO mitabla VALUES( ?, ? );");
            stmt2 = connection.prepareStatement("INSERT INTO miotratabla VALUES( ?, ?, ? );");

            System.out.println("Primer INSERT tabla [mitabla]");
            stmt1.setString(1, "000001");
            stmt1.setString(2, "micorreo@mail.com");
            stmt1.executeUpdate();

            System.out.println("Segundo INSERT tabla [mitabla]");
            stmt1.setString(1, "000002");
            stmt1.setString(2, "amayuya@mail.com");
            stmt1.executeUpdate();

            System.out.println("Tercer INSERT tabla [mitabla]");
            stmt1.setString(1, "000003");
            stmt1.setString(2, "diosdado@mail.com");
            stmt1.executeUpdate();

            System.out.println("Primer INSERT tabla [miotratabla]");
            stmt2.setString(1, "Juan");
            stmt2.setString(2, "Perez");
            stmt2.setString(3, "Hola soy un error"); // Error intencional
            stmt2.executeUpdate();

            connection.commit(); // Aplica los cambios

        } catch (SQLException ex) {
            System.err.println("ERROR: " + ex.getMessage());
            if (connection != null) {
                try {
                    System.out.println("Rollback");
                    connection.rollback(); // Reversión
                } catch (SQLException ex1) {
                    System.err.println("No se pudo deshacer: " + ex1.getMessage());
                }
            }
        } finally {
            System.out.println("Cierra conexión a la base de datos");
            try {
                if (stmt1 != null) stmt1.close();
                if (stmt2 != null) stmt2.close();
                if (connection != null) connection.close();
            } catch (SQLException ex) {
                System.err.println(ex.getMessage());
            }
        }
    }
}
