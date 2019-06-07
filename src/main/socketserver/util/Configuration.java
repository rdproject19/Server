package socketserver.util;

import org.w3c.dom.Document;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.IOException;

/**
 * Basic configuration manager
 */
public class Configuration {

    private Document document;

    public Configuration() throws ParserConfigurationException, IOException, SAXException {
        File file = new File("config.xml");
        if (!file.exists()) {
            System.out.println("Config file not found; Using default values...");
            //file = new File(getClass().getResource("config.xml").getFile());
        } else {
            System.out.println("Config loaded...");
        }
        //DocumentBuilderFactory fac = DocumentBuilderFactory.newInstance();
        //DocumentBuilder b = fac.newDocumentBuilder();
        //document = b.parse(file);
    }

    /**
     * Get the database host
     * @return database host
     */
    public String getDatabaseHost() {
        return "127.0.0.1";
        //return document.getElementsByTagName("dbhost").item(0).getTextContent();
    }

    /**
     * Gets the database port
     * @return database port
     */
    public int getDatabasePort() {
        return 27017;
        //String txtversion = document.getElementsByTagName("dbport").item(0).getTextContent();
        //return Integer.parseInt(txtversion);
    }

    /**
     * Gets the host where the server should be spawned
     * @return Server host
     */
    public String getServerHost() {
        return "0.0.0.0";
        //return document.getElementsByTagName("serverhost").item(0).getTextContent();
    }

    /**
     * Gets the port where the server should be spawned
     * @return Server port
     */
    public int getServerPort() {
        return 7070;
        //return Integer.parseInt(document.getElementsByTagName("serverport").item(0).getTextContent());
    }
}
