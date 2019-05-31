package util;

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

    Document document;

    public Configuration() throws ParserConfigurationException, IOException, SAXException, URISyntaxException {
        File file = new File("config.xml");
        if (!file.exists()) {
            System.out.println("Config file not found; Using default values...");
            URL url = getClass().getResource("src\\main\\resources\\config.xml");
            file = new File(url.getFile());
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
}
