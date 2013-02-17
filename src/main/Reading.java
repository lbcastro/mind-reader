package main;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;


public class Reading {
	
	
	// Initiate variables.
	static File xml = new File("/home/lourenco/workspace/Mind Reader/ptr/patterns.xml");
	
	static List<Double> alphaRead 	= new ArrayList<Double>();
	static List<Double> deltaRead 	= new ArrayList<Double>();
	static List<Double> thetaRead 	= new ArrayList<Double>();
	static List<Double> betaRead 	= new ArrayList<Double>();
	
	static List<Double> rawRead 	= new ArrayList<Double>();
	static double minRead 			= 0.0;
	static double maxRead 			= 0.0;
	static double averageRead 		= 0.0;
	//
	
	
	// Main method.
	Reading(int rec) {
		try {

			
			// Initiate builders.
			DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder 	   dBuilder  = dbFactory.newDocumentBuilder();
			Document 			   doc 		 = dBuilder.parse(xml);
			//
			
			
			// Find values' node.
			Node 	 root 		 = doc.getFirstChild();
			NodeList recList 	 = root.getChildNodes();
			Node 	 recording 	 = recList.item(rec);
			Node 	 channel 	 = recording.getFirstChild();
			Node 	 rawNode 	 = channel.getFirstChild();
			Node 	 rawValues 	 = rawNode.getFirstChild();
			
			Node 	 rawMin 	 = rawValues.getNextSibling();
			Node 	 rawMax 	 = rawMin.getNextSibling();
			Node 	 rawAverage  = rawMax.getNextSibling();
			
			Node 	 bands 		 = rawNode.getNextSibling();
			NodeList bandsList 	 = bands.getChildNodes();
			Node 	 deltaValues = bandsList.item(0);
			Node 	 thetaValues = bandsList.item(1);
			Node 	 alphaValues = bandsList.item(2);
			Node 	 betaValues  = bandsList.item(3);
			//
			
			
			// Convert values to doubles.
			rawRead.add(Double.parseDouble(rawValues.getTextContent()));
			minRead = Double.parseDouble(rawMin.getTextContent());
			maxRead = Double.parseDouble(rawMax.getTextContent());
			averageRead = Double.parseDouble(rawAverage.getTextContent());

			deltaRead.add(Double.parseDouble(deltaValues.getTextContent()));
			thetaRead.add(Double.parseDouble(thetaValues.getTextContent()));
			alphaRead.add(Double.parseDouble(alphaValues.getTextContent()));
			betaRead.add(Double.parseDouble(betaValues.getTextContent()));
			//
			
			
			// Print values.
			System.out.println(rawRead.toString());
			System.out.println(deltaValues.toString());
		}
		//
		
		
		// Exceptions.
		catch (ParserConfigurationException e) {
			e.printStackTrace();
		} catch (SAXException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}