package it.unibo.conversational.vocalization.utils;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import org.xml.sax.SAXException;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

public class XmlParser {

    private final Element xmlRoot;

    public XmlParser(Element xmlRoot) {
        this.xmlRoot = xmlRoot;
    }

    public static XmlParser initialize(String xmlPath) throws IOException, ParserConfigurationException, SAXException {
        File xmlInputFile = new File(xmlPath);
        DocumentBuilder documentBuilder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
        return new XmlParser(documentBuilder.parse(xmlInputFile).getDocumentElement());
    }

    public int countDimensions() {
        return this.xmlRoot.getElementsByTagName("dimension_table").getLength();
    }

    public List<String> parseDimension(int dimensionIndex) {
        List<String> parsed = new ArrayList<>();
        Element dimension = (Element) this.xmlRoot.getElementsByTagName("dimension_table").item(dimensionIndex);
        parsed.add(dimension.getElementsByTagName("db_name").item(0).getTextContent());
        parsed.add(dimension.getElementsByTagName("spoken_name").item(0).getTextContent());
        return parsed;
    }

    public List<List<String>> parseLevels(int dimensionIndex) {
        List<List<String>> parsed = new ArrayList<>();
        Element dimension = (Element) this.xmlRoot.getElementsByTagName("dimension_table").item(dimensionIndex);
        NodeList levels = dimension.getElementsByTagName("level");
        IntStream.range(0, 3).forEach(i -> parsed.add(new ArrayList<>()));
        for (int i = 0; i < levels.getLength(); i++) {
            Element level = (Element) levels.item(i);
            parsed.get(0).add(level.getElementsByTagName("db_name").item(0).getTextContent());
            parsed.get(1).add(level.getElementsByTagName("spoken_name").item(0).getTextContent());
            parsed.get(2).add(level.getElementsByTagName("introduction").item(0).getTextContent());
        }
        return parsed;
    }

    public int countFacts() {
        return this.xmlRoot.getElementsByTagName("fact_table").getLength();
    }

    public List<String> parseFact(int factIndex) {
        List<String> parsed = new ArrayList<>();
        Element fact = (Element) this.xmlRoot.getElementsByTagName("fact_table").item(factIndex);
        parsed.add(fact.getElementsByTagName("db_name").item(0).getTextContent());
        parsed.add(fact.getElementsByTagName("spoken_name").item(0).getTextContent());
        return parsed;
    }

    public List<List<String>> parseMeasures(int factIndex) {
        List<List<String>> parsed = new ArrayList<>();
        Element fact = (Element) this.xmlRoot.getElementsByTagName("fact_table").item(factIndex);
        NodeList measures = fact.getElementsByTagName("measure");
        IntStream.range(0, measures.getLength()).forEach(i -> parsed.add(new ArrayList<>()));
        for (int i = 0; i < measures.getLength(); i++) {
            Element measure = (Element) measures.item(i);
            parsed.get(i).add(measure.getElementsByTagName("db_name").item(0).getTextContent());
            parsed.get(i).add(measure.getElementsByTagName("spoken_name").item(0).getTextContent());
        }
        return parsed;
    }

}
