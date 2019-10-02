package PO73.Perepechin.wdad.data.managers;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.File;
import java.io.IOException;

public class PreferencesManager {
    private static String APPGONFIG_PATH = "";
    private static PreferencesManager ourInstance = new PreferencesManager();

    public static PreferencesManager getInstance() {
        return ourInstance;
    }

    private Document document;

    private PreferencesManager() {
        parseXML();
    }

    public boolean getCreateRegistry() {
        Node createRegistryNode = findNode("createregistry");
        return createRegistryNode.getTextContent().equals("yes");
    }

    public void setCreateRegistry(boolean createRegistry) {
        Node createRegistryNode = findNode("createregistry");
        createRegistryNode.setTextContent(createRegistry ? "yes" : "no");
        saveXML();
    }

    public String getRegistryAddress() {
        Node registryAddressNode = findNode("registryaddress");
        return registryAddressNode.getTextContent();
    }

    public void setRegistryAddress(String address) {
        Node registryAddressNode = findNode("registryaddress");
        registryAddressNode.setTextContent(address);
        saveXML();
    }

    public int getRegistryPort() {
        Node registryPortNode = findNode("registryport");
        return Integer.parseInt(registryPortNode.getTextContent());
    }

    public void setRegistryPort(int port) {
        Node registryPortNode = findNode("registryport");
        registryPortNode.setTextContent(Integer.toString(port));
        saveXML();
    }

    private Node findNode(String name) {
        return document.getElementsByTagName(name).item(0);
    }

    private void parseXML() {
        try {
            document = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(new File(APPGONFIG_PATH));
        } catch (ParserConfigurationException | IOException | SAXException e) {
            e.printStackTrace();
        }
    }

    private void saveXML() {
        try {
            Transformer transformer = TransformerFactory.newInstance().newTransformer();
            transformer.setOutputProperty(OutputKeys.INDENT, "yes");
            transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "3");
            transformer.transform(new DOMSource(document), new StreamResult(new File(APPGONFIG_PATH)));
        } catch (TransformerException e) {
            e.printStackTrace();
        }
    }
}
