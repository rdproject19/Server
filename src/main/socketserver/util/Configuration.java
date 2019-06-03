package socketserver.util;

import org.w3c.dom.Document;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;

public class Configuration {

    private Document document;
    private boolean noconfig;

    public Configuration() throws ParserConfigurationException, IOException, SAXException {
        File file = new File("config.xml");
        if (!file.exists()) {
            System.out.println("Config file not found; Using default values...");
            noconfig = true;
            return;
        }
        DocumentBuilderFactory fac = DocumentBuilderFactory.newInstance();
        DocumentBuilder b = fac.newDocumentBuilder();
        document = b.parse(file);
    }

    public String getDatabaseHost() {
        if (noconfig) return "127.0.0.1";
        return document.getElementsByTagName("dbhost").item(0).getTextContent();
    }

    public int getDatabasePort() {
        if (noconfig) return 27017;
        String txtversion = document.getElementsByTagName("dbport").item(0).getTextContent();
        return Integer.parseInt(txtversion);
    }
}
