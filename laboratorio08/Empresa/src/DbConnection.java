import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class DbConnection {
    // Parámetros de conexión
    private static final String HOST     = "localhost";
    private static final int    PORT     = 3306;
    private static final String DATABASE = "empresa_sd";

    // URL de conexión JDBC
    // Usamos el formato de URL para MySQL con parámetros comunes
    // - serverTimezone: para evitar problemas de zona horaria
    // - useSSL: desactivado para evitar advertencias de SSL
    // - allowPublicKeyRetrieval: habilitado para evitar problemas de conexión
    //   si se usa autenticación con contraseña en texto plano
    // Solo para entornos de desarrollo, no recomendado en producción
    // Nota: En producción, se recomienda usar SSL y una contraseña más segura.
    private static final String URL      = String.format(
        "jdbc:mysql://%s:%d/%s" +
        "?serverTimezone=UTC" +
        "&useSSL=false" +
        "&allowPublicKeyRetrieval=true",
        HOST, PORT, DATABASE
    );
    private static final String USER     = "sd_user";
    private static final String PASS     = "TuPasswordSeguro";

    private static final Logger LOGGER = Logger.getLogger(DbConnection.class.getName());

    // Evitar instanciación
    // El constructor es privado para evitar que se creen instancias de esta clase
    // ya que solo necesitamos métodos estáticos para obtener la conexión.
    private DbConnection() { }

    /**
     * Abre y devuelve una conexión a la base de datos Empresa_SD.
     * @return Connection o null si ocurre error.
     */
    public static Connection getConnection() {
        try {
            // Carga opcional del driver (desde Java 6+ suele detectarse automáticamente)
            Class.forName("com.mysql.cj.jdbc.Driver");
            
            Connection conn = DriverManager.getConnection(URL, USER, PASS);
            LOGGER.log(Level.INFO, "Conexión establecida a {0}", DATABASE);
            return conn;
        } catch (ClassNotFoundException e) {
            LOGGER.log(Level.SEVERE, "Driver MySQL no encontrado", e);
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error al conectar con la base de datos", e);
        }
        return null;
    }
}
