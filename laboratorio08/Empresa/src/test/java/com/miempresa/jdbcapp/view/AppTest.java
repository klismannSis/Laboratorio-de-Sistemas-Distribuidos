import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class AppTest {

    @Test
    void testAppInitialization() {
        App app = new App();
        assertNotNull(app);
    }

    @Test
    void testLoadFXML() {
        // Test loading of FXML files
        assertDoesNotThrow(() -> {
            app.loadFXML("mainview");
        });
    }

    // Additional tests can be added here
}