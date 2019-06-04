package socketserver.util;

import org.w3c.dom.Document;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.IOException;

public class Configuration {

    private Document document;

    public Configuration() throws ParserConfigurationException, IOException, SAXException {
        File file = new File("config.xml");
        if (!file.exists()) {
            System.out.println("Config file not found; Using default values...");
            file = new File(getClass().getResource("config.xml").getFile());
        } else {
            System.out.println("Config loaded...");
        }
        DocumentBuilderFactory fac = DocumentBuilderFactory.newInstance();
        DocumentBuilder b = fac.newDocumentBuilder();
        document = b.parse(file);
    }

    public String getDatabaseHost() {
        return document.getElementsByTagName("dbhost").item(0).getTextContent();
    }

    public int getDatabasePort() {
        String txtversion = document.getElementsByTagName("dbport").item(0).getTextContent();
        return Integer.parseInt(txtversion);
    }

    public String getServerHost() {
        return document.getElementsByTagName("serverhost").item(0).getTextContent();
    }

    public int getServerPort() {
        return Integer.parseInt(document.getElementsByTagName("serverport").item(0).getTextContent());
    }
}
