package PO73.Perepechin.wdad.learn.xml;

import java.io.*;
import java.nio.file.Files;
import java.util.List;

public class TestXmlTask {
    public static void main(String[] args) {
        File xmlFile = new File("files/testHousekeeper.xml");

        XmlTask xmlTask = new XmlTask();
        xmlTask.setXmlFile(xmlFile);

        printFile(xmlFile);
        System.out.println("Bill: " + xmlTask.getBill("Baker Street", 221, 3));
        xmlTask.setTariff("gas", 10);
        printFile(xmlFile);
        xmlTask.addRegistration("Baker Street", 221, 3, 2019, 9, 5, 5, 10, 50);
        printFile(xmlFile);
    }

    private static void printFile(File file) {
        try {
            List<String> lines = Files.readAllLines(file.toPath());
            lines.forEach(System.out::println);
        } catch (IOException e) {
            System.err.println("File error");
        }
    }
}
