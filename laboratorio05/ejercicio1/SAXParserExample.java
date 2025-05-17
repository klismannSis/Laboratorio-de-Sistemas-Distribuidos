import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import java.io.IOException;

public class SAXParserExample {
    public static void main(String[] args) {
        try {
            SAXParserFactory factory = SAXParserFactory.newInstance();
            factory.setValidating(true);
            SAXParser parser = factory.newSAXParser();

            parser.parse("boe.xml", new DefaultHandler());

            System.out.println("SAX: El archivo XML es válido contra el DTD.");
        } catch (SAXException e) {
            System.out.println("SAX: Error de validación: " + e.getMessage());
        } catch (IOException e) {
            System.out.println("SAX: Error de E/S: " + e.getMessage());
        } catch (Exception e) {
            System.out.println("SAX: Otro error: " + e.getMessage());
        }
    }
}