import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import org.xml.sax.SAXException;
import org.w3c.dom.Document;
import java.io.IOException;
import java.io.File;

public class DOMParserExample {
    public static void main(String[] args) {
        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            factory.setValidating(true); // Activa la validación contra DTD
            factory.setNamespaceAware(true);

            DocumentBuilder builder = factory.newDocumentBuilder();

            Document doc = builder.parse(new File("boe.xml"));

            System.out.println("DOM: El archivo XML es válido contra el DTD.");
        } catch (SAXException e) {
            System.out.println("DOM: Error de validación: " + e.getMessage());
        } catch (IOException e) {
            System.out.println("DOM: Error de E/S: " + e.getMessage());
        } catch (Exception e) {
            System.out.println("DOM: Otro error: " + e.getMessage());
        }
    }
}