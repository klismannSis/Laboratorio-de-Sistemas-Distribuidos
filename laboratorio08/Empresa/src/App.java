import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class App {
    public static void main(String[] args) {
        String sql = "SELECT IDDpto, Nombre, Telefono, Fax FROM Departamento";

        // try-with-resources para cerrar conn, stmt y rs automáticamente
        try (Connection conn = DbConnection.getConnection();
             Statement stmt = (conn != null) ? conn.createStatement() : null;
             ResultSet rs   = (stmt != null) ? stmt.executeQuery(sql) : null) {

            if (rs != null) {
                System.out.println("ID | Nombre                        | Teléfono   | Fax");
                System.out.println("--------------------------------------------------------");
                while (rs.next()) {
                    int    id       = rs.getInt("IDDpto");
                    String nombre   = rs.getString("Nombre");
                    String telefono = rs.getString("Telefono");
                    String fax      = rs.getString("Fax");
                    System.out.printf("%2d | %-28s | %10s | %10s%n",
                                      id, nombre, telefono, fax);
                }
            } else {
                System.err.println("No se pudo ejecutar la consulta o la conexión es nula.");
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
