package utils;

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Iterator;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;
import data.Hydrant;

public class ReadXMLFileUsingSaxparser extends DefaultHandler {

	private Hydrant hydrant;
	private String temp;
	private ArrayList<Hydrant> hydrantList = new ArrayList<Hydrant>();

	/** The main method sets things up for parsing */
	public static void main(String[] args) throws IOException, SAXException,
			ParserConfigurationException {

		// Create a "parser factory" for creating SAX parsers
		SAXParserFactory spfac = SAXParserFactory.newInstance();

		// Now use the parser factory to create a SAXParser object
		SAXParser sp = spfac.newSAXParser();

		// Create an instance of this class; it defines all the handler methods
		ReadXMLFileUsingSaxparser handler = new ReadXMLFileUsingSaxparser();

		// Finally, tell the parser to parse the input and notify the handler
		sp.parse(new File("D:\\消防\\大臺北地區消防栓分布點位圖.kml"), handler);

		handler.readList();

	}

	public void characters(char[] buffer, int start, int length) {
		temp = new String(buffer, start, length);
	}

	public void startElement(String uri, String localName, String qName,
			Attributes attributes) throws SAXException {
		temp = "";

		if (qName.equalsIgnoreCase("Point")) {
			hydrant = new Hydrant();
			// acct.setType(attributes.getValue("type"));
		}

	}

	public void endElement(String uri, String localName, String qName)
			throws SAXException {

		if (qName.equalsIgnoreCase("Point")) {
			// add it to the list
			hydrantList.add(hydrant);

		} else if (qName.equalsIgnoreCase("coordinates")) {

			if (!temp.matches("\\s*")) {

				temp = temp.replaceAll("[^\\d|^,|^\\.]*", "");

				BigDecimal lat = new BigDecimal(temp.replaceAll(
						"([\\.|\\d]*),([\\.|\\d]*),0", "$1"));
				BigDecimal lng = new BigDecimal(temp.replaceAll(
						"([\\.|\\d]*),([\\.|\\d]*),0", "$2"));

				hydrant.setLat(lat);
				hydrant.setLng(lng);
				
			}
		}
	}

	public void readList() {
		System.out.println("ponit number:" + hydrantList.size());
		Iterator<Hydrant> it = hydrantList.iterator();
		while (it.hasNext()) {
			System.out.println(it.next().toString());
		}
	}
	
	public ArrayList<Hydrant> getHydrantList(){
		return hydrantList;
	}

}
