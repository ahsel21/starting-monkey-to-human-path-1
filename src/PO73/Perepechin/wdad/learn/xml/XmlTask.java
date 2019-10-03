package PO73.Perepechin.wdad.learn.xml;

import org.w3c.dom.*;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.*;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.File;
import java.io.IOException;

public class XmlTask {
    private File xmlFile;
    private Document document;

    public File getXmlFile() {
        return xmlFile;
    }

    public void setXmlFile(File xmlFile) {
        this.xmlFile = xmlFile;
        parseXML();
    }

    public double getBill(String street, int buildingNumber, int flatNumber) {
        Node flat = getFlatNode(street, buildingNumber, flatNumber);
        if (flat == null) return 0;
        double bill = 0;

        //registrations
        NodeList registrations = flat.getChildNodes();
        for (int k = 0; k < registrations.getLength(); k++) {
            Node registration = registrations.item(k);
            if (registration.getNodeType() == Node.ELEMENT_NODE) {
                //items
                NodeList items = registration.getChildNodes();
                for (int i = 0; i < items.getLength(); i++) {
                    Node item = items.item(i);
                    if (item.getNodeType() == Node.ELEMENT_NODE) {
                        bill += Double.parseDouble(item.getTextContent());
                    }
                }
            }

        }
        return bill;
    }

    public void setTariff(String tariffName, double newValue) {
        Node tariffs = document.getElementsByTagName("tariffs").item(0);
        NamedNodeMap tariffsNames = tariffs.getAttributes();
        tariffsNames.getNamedItem(tariffName).setTextContent(Double.toString(newValue));
        saveXML();
    }

    public void addRegistration(String street, int buildingNumber, int flatNumber, int year, int month, double coldWater, double hotWater, double electricity, double gas) {
        Node flat = getFlatNode(street, buildingNumber, flatNumber);

        if (flat != null) {
            Element registration = document.createElement("registration");
            registration.setAttribute("month", Integer.toString(month));
            registration.setAttribute("year", Integer.toString(year));

            Element coldWaterReg = document.createElement("coldwater");
            coldWaterReg.setTextContent(Double.toString(coldWater));

            Element hotWaterReg = document.createElement("hotwater");
            hotWaterReg.setTextContent(Double.toString(hotWater));

            Element electricityReg = document.createElement("electricity");
            electricityReg.setTextContent(Double.toString(electricity));

            Element gasReg = document.createElement("gas");
            gasReg.setTextContent(Double.toString(gas));

            registration.appendChild(coldWaterReg);
            registration.appendChild(hotWaterReg);
            registration.appendChild(electricityReg);
            registration.appendChild(gasReg);

            flat.appendChild(registration);

            saveXML();
        }
    }

    private Node getFlatNode(String street, int buildingNumber, int flatNumber) {
        NodeList buildingsList = document.getDocumentElement().getElementsByTagName("building");
        for (int i = 0; i < buildingsList.getLength(); i++) {
            Node building = buildingsList.item(i);
            NamedNodeMap buildingAttributes = building.getAttributes();

            //building check
            if (buildingAttributes.getNamedItem("street").getNodeValue().equals(street) &&
                    buildingAttributes.getNamedItem("number").getNodeValue().equals(Integer.toString(buildingNumber))) {
                NodeList flats = building.getChildNodes();
                for (int j = 0; j < flats.getLength(); j++) {
                    Node flat = flats.item(j);

                    //flat check
                    if (flat.getNodeType() == Node.ELEMENT_NODE &&
                            flat.getAttributes().getNamedItem("number").getNodeValue().equals(Integer.toString(flatNumber))) {
                        return flat;
                    }
                }
            }
        }
        return null;
    }

    private void parseXML() {
        try {
            document = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(xmlFile);
        } catch (ParserConfigurationException | SAXException | IOException e) {
            e.printStackTrace();
        }
    }

    private void saveXML() {
        try {
            Transformer transformer = TransformerFactory.newInstance().newTransformer();
            transformer.setOutputProperty(OutputKeys.INDENT, "yes");
            transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "3");
            transformer.setOutputProperty(OutputKeys.DOCTYPE_SYSTEM, "housekeeper.dtd");
            transformer.transform(new DOMSource(document), new StreamResult(xmlFile));
        } catch (TransformerException e) {
            e.printStackTrace();
        }
    }
}
