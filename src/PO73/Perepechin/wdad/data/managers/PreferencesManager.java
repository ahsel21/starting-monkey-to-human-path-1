package PO73.Perepechin.wdad.data.managers;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
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

    private Node getRmiNode() {
        return document.getDocumentElement().getElementsByTagName("rmi").item(0);
    }

    private Node getRegistryNodeChild(String nodeName) {
        Node rmi = getRmiNode();

        NodeList rmiChilds = rmi.getChildNodes();
        for (int i = 0; i < rmiChilds.getLength(); i++) {
            Node rmiChild = rmiChilds.item(i);
            if (rmiChild.getNodeType() == Node.ELEMENT_NODE && rmiChild.getNodeName().equals("server")) {
                NodeList serverChilds = rmiChild.getChildNodes();
                for (int j = 0; j < serverChilds.getLength(); j++) {
                    Node registryNode = serverChilds.item(j);
                    if (registryNode.getNodeType() == Node.ELEMENT_NODE && registryNode.getNodeName().equals("registry")) {
                        return registryNode;
                    }
                }
            }
        }
        return null;
    }

    private void parseXML() {
        try {
            document = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(new File(APPGONFIG_PATH));
        } catch (ParserConfigurationException | IOException | SAXException e) {
            e.printStackTrace();
        }
    }
}
