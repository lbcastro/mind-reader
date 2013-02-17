package main;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.ListSelectionModel;
import javax.swing.border.LineBorder;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

public class SavingOld {
	
	
	// Initiate variables.
	static File 			xml 		= new File("/home/lourenco/workspace/Mind Reader/ptr/blink.xml");
	static File				xmlList		= new File("/home/lourenco/workspace/Mind Reader/ptr/locations.xml");
	
	static List<Double> 	rawList0 	= new ArrayList<Double>();
	static List<Double> 	rawList1	= new ArrayList<Double>();
	static List<Double> 	rawList2 	= new ArrayList<Double>();
	static List<Double> 	rawList3 	= new ArrayList<Double>();
	static List<Double> 	rawList4 	= new ArrayList<Double>();
	static List<Double> 	rawList5 	= new ArrayList<Double>();
	static List<Double> 	rawList6 	= new ArrayList<Double>();
	static List<Double> 	rawList7 	= new ArrayList<Double>();
	static List<Double> 	rawList8 	= new ArrayList<Double>();
	static List<Double> 	rawList9 	= new ArrayList<Double>();
	static List<Double> 	rawList10 	= new ArrayList<Double>();
	static List<Double> 	rawList11 	= new ArrayList<Double>();
	static List<Double> 	rawList12 	= new ArrayList<Double>();
	static List<Double> 	rawList13 	= new ArrayList<Double>();

	static ArrayList<List<Double>> 	rawList		= new ArrayList<List<Double>>();
	
	static double 			rawMax		= Float.NEGATIVE_INFINITY;
	static double 			rawMin 		= Float.POSITIVE_INFINITY;
	static double 			rawTotal	= 0.0;
	
	static List<Double> 	alphaList 	= new ArrayList<Double>();
	static List<Double> 	betaList 	= new ArrayList<Double>();
	static List<Double> 	deltaList 	= new ArrayList<Double>();
	static List<Double> 	thetaList 	= new ArrayList<Double>();
	
	/*
	Node rawValue 						= null;
	Node rawMinValue 					= null;
	Node rawMaxValue 					= null;
	Node rawAverageValue 				= null;
	*/
	
	Node deltaValue 					= null;
	Node thetaValue 					= null;
	Node alphaValue 					= null;
	Node betaValue 						= null;
	//


	// Getters & Setters.
	private static File getXml() 
	{
		return xml;
	}
	
	private static List<Double> getRawList(int x)
	{
		return rawList.get(x);
	}
	
	private static List<Double> getAlphaList()
	{
		return alphaList;
	}
	
	private static List<Double> getThetaList()
	{
		return thetaList;
	}
	
	private static List<Double> getDeltaList()
	{
		return deltaList;
	}

	private static List<Double> getBetaList()
	{
		return betaList;
	}
	//


	// Class methods.
	
	
	// 
	private void popList() {
		rawList.add(rawList0);
		rawList.add(rawList1);
		rawList.add(rawList2);
		rawList.add(rawList3);
		rawList.add(rawList4);
		rawList.add(rawList5);
		rawList.add(rawList6);
		rawList.add(rawList7);
		rawList.add(rawList8);
		rawList.add(rawList9);
		rawList.add(rawList10);
		rawList.add(rawList11);
		rawList.add(rawList12);
		rawList.add(rawList13);
	}
	
	// Add raw values and calculate min and max.
	public static void raw(double value, int x) 
	{
		getRawList(x).add(value);
		rawTotal += value;
		if (value > rawMax) rawMax = value;
		else if (value < rawMin) rawMin = value;
	}
	//
	
	
	// Returns specified event file location. Creates one if it doesn't exist.
	public static File isEvent(String name) {

		
		// Variables to store the specific file location.
		String 	fileLoc   = null;
		File 	eventFile = null;
		Node 	root	  = null;
		Element event     = null;
		//
		
		
		try {
		
			
			// Initiate builders.
			DocumentBuilderFactory 	documentFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder 		documentBuilder = documentFactory.newDocumentBuilder();
			Document 				document 		= documentBuilder.newDocument();
			//

			
			// If the file already exists.
			if (xmlList.exists()) {


				// Find and list all event nodes.
				document 		   = documentBuilder.parse(xmlList);
				root 			   = document.getFirstChild();
				NodeList eventList = root.getChildNodes();
				//

				
				// Find the specified event node.
				for (int x = 0; x < eventList.getLength(); x++) {
					if (eventList.item(x).getNodeName() == name) 
					{
						fileLoc   = eventList.item(x).getTextContent();
						eventFile = new File(fileLoc);
						break;
					}
				}
				//


				// If the node doesn't exist, create one.
				fileLoc = "/home/lourenco/workspace/Mind Reader/ptr/" + name.toString() + ".xml";
				event 	= document.createElement(name.toString());

				event.setTextContent(fileLoc);
				root.appendChild(event);
				eventFile = new File(fileLoc);
			}
			//
			
			
			// If the locations file doesn't exist, create one.
			else {
				createXmlList(document);

				root 	= document.getFirstChild();
				fileLoc = "/home/lourenco/workspace/Mind Reader/ptr/" + name.toString() + ".xml";
				event 	= document.createElement(name.toString());

				event.setTextContent(fileLoc);
				root.appendChild(event);
				eventFile = new File(fileLoc);
				
				TransformerFactory 	transformerFactory 	= TransformerFactory.newInstance();
				Transformer 		transformer 		= transformerFactory.newTransformer();
				DOMSource 			source 				= new DOMSource(document);
				StreamResult 		result 				= new StreamResult(xmlList);

				transformer.transform(source, result);
			}
		}
		//


		// Exceptions.
		catch (ParserConfigurationException e) {
			e.printStackTrace();
		}
		catch (SAXException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		//
		catch (TransformerConfigurationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (TransformerException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}


		// Return the event's file location.
		return eventFile;
	}
	//


	// Saves each band's values in arrays.
	public static void bands(double[] bands) 
	{
		getDeltaList().add(bands[0]);
		getThetaList().add(bands[1]);
		getAlphaList().add(bands[2]);
		getBetaList().add(bands[3]);
	}
	//

	
	// Creates specific nodes to save EEG data.
	private void createNodes(Document document) {

		
		try {

			
			// ID of the next recording. 1 if it's the first one saved.
			int nextInt = 1;
			//
			
			
			// Define needed nodes.
			Node root 	 = document.getFirstChild();
			Node lastRec = root.getLastChild();
			//

			
			// Find last recording's ID.
			if (lastRec != null)
			{
				NamedNodeMap 	attr 		= lastRec.getAttributes();
				Node 			recNumber 	= attr.getNamedItem("id");
				nextInt 					= Integer.parseInt(recNumber.getNodeValue().toString()) + 1;
			}
			//


			// Create the next recording node.
			Element next = document.createElement("recording");
			next.setAttribute("id", String.valueOf(nextInt));
			
			root.appendChild(next);
			//
			
			
			// Create blank nodes for each channel's values.
			for (int x = 0; x < 1; x++) {
				
				
				// Create channel root node.
				Element chan = document.createElement("channel");
				chan.setAttribute("number", String.valueOf(x));
				next.appendChild(chan);
				//

				
				// Create raw data nodes.
				Node raw 		= document.createElement("raw");
				
				Node rawValue 		= document.createElement("values");
				Node rawMinValue		= document.createElement("min");
				Node rawMaxValue 	= document.createElement("max");
				Node rawAverageValue = document.createElement("average");

				chan.appendChild(raw);
				raw.appendChild(rawValue);
				raw.appendChild(rawMinValue);
				raw.appendChild(rawMaxValue);
				raw.appendChild(rawAverageValue);
				//
				
				
				// Create bands nodes.
				Node bands = document.createElement("bands");
				
				deltaValue = document.createElement("delta");
				thetaValue = document.createElement("theta");
				alphaValue = document.createElement("alpha");
				betaValue  = document.createElement("beta");

				chan.appendChild(bands);
				bands.appendChild(deltaValue);
				bands.appendChild(thetaValue);
				bands.appendChild(alphaValue);
				bands.appendChild(betaValue);
				//
			}


			// Save the changes.
			TransformerFactory 	transformerFactory 	= TransformerFactory.newInstance();
			Transformer 		transformer 		= transformerFactory.newTransformer();
			DOMSource 			source 				= new DOMSource(document);
			StreamResult 		result 				= new StreamResult(getXml());

			transformer.transform(source, result);
		} 
		//


		//
		catch (TransformerConfigurationException e) {
			e.printStackTrace();
		} catch (TransformerException e) {
			e.printStackTrace();
		}
	}
	//

	
	// Create an XML file to store events' XML files location
	private static void createXmlList(Document document) {

		
		// Create a root node.
		Element root = document.createElement("events");
		document.appendChild(root);
		//
		
		
		// Saves the file.
		try {
			TransformerFactory 	transformerFactory 	= TransformerFactory.newInstance();
			Transformer 		transformer 		= transformerFactory.newTransformer();
			DOMSource 			source 				= new DOMSource(document);
			StreamResult 		result 				= new StreamResult(xmlList);

			transformer.transform(source, result);
		}
		//
		

		// Exceptions
		catch (TransformerConfigurationException e) {
			e.printStackTrace();
		} 
		catch (TransformerException e) {
			e.printStackTrace();
		}
	}
	//


	// Create an XML file to save the EEG data.
	private void createXml() {
		try {

			
			// Set up document builders.
			DocumentBuilderFactory 	documentFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder 		documentBuilder = documentFactory.newDocumentBuilder();
			Document 				document 		= documentBuilder.newDocument();
			//


			// Create root node.
			Element rootElement = document.createElement("event");
			document.appendChild(rootElement);
			//


			// Save the changes.
			TransformerFactory 	transformerFactory 	= TransformerFactory.newInstance();
			Transformer 		transformer 		= transformerFactory.newTransformer();
			DOMSource 			source 				= new DOMSource(document);
			StreamResult 		result 				= new StreamResult(getXml());

			transformer.transform(source, result);
		}
		//


		//
		catch (ParserConfigurationException e) {
			e.printStackTrace();
		} catch (TransformerException e) {
			e.printStackTrace();
		}
	}
	//
	
	
	// Enables recording.
	private void start() 
	{
		if (!getXml().exists()) createXml();
		new SelectEvent();
		
	}
	//


	// Finishes recording process.
	private void stop() 
	{
		separate();
		Processing.setRecording(false);
	}
	//

	
	// Separates and writes each value to a file.
	private void separate() {
		try {

			// Initiate builders.
			DocumentBuilderFactory 	documentFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder 		documentBuilder = documentFactory.newDocumentBuilder();
			Document 				document 		= documentBuilder.parse(getXml());
			//

			
			// Create required nodes.
			createNodes(document);
			//
			

			Node root 	 = document.getFirstChild();
			Node lastRec = root.getLastChild();
			
			Node nextChan = lastRec.getFirstChild();
			Node nextRaw = nextChan.getFirstChild();
			Node rawValue = nextRaw.getFirstChild();

			
				//raw.appendChild(rawMinValue);
				//raw.appendChild(rawMaxValue);
				//raw.appendChild(rawAverageValue);
				
				

			rawValue.setTextContent(String.valueOf(rawList));
				//


				// Create bands nodes.
				//Node bands = document.createElement("bands");

				//deltaValue = document.createElement("delta");
				//thetaValue = document.createElement("theta");
				//alphaValue = document.createElement("alpha");
				//betaValue  = document.createElement("beta");

				//chan.appendChild(bands);
				//bands.appendChild(deltaValue);
				//bands.appendChild(thetaValue);
				//bands.appendChild(alphaValue);
				//bands.appendChild(betaValue);
				
				//
			//}
			
			rawList.clear();
			
			// Remove baseline.

			/*
			// Add new recorded data.
			for (int x = 0; x < 14; x++) {
				rawValue.setTextContent(String.valueOf(getRawList(x)));
				rawMinValue.setTextContent(String.valueOf(rawMin));
				rawMaxValue.setTextContent(String.valueOf(rawMax));

				double av = Math.round(rawTotal/getRawList(x).size()*100.0)/100.0;
				rawAverageValue.setTextContent(String.valueOf(av));


				//rawValue = rawValue.get


				getRawList(x).clear();
			}
			//


			// Add bands data.
			deltaValue.setTextContent(String.valueOf(getDeltaList()));
			thetaValue.setTextContent(String.valueOf(getThetaList()));
			alphaValue.setTextContent(String.valueOf(getAlphaList()));
			betaValue.setTextContent(String.valueOf(getBetaList()));
			//


			// Clears all lists.
			getDeltaList().clear();
			getThetaList().clear();
			getAlphaList().clear();
			getBetaList().clear();
			//
			 */

			// Save changes.
			TransformerFactory 	transformerFactory 	= TransformerFactory.newInstance();
			Transformer 		transformer 		= transformerFactory.newTransformer();
			DOMSource 			source 				= new DOMSource(document);
			StreamResult 		result 				= new StreamResult(getXml());

			transformer.transform(source, result);
		} 
		//


		//
		catch (ParserConfigurationException e1) {
			e1.printStackTrace();
		} catch (TransformerConfigurationException e) {
			e.printStackTrace();
		} catch (TransformerException e) {
			e.printStackTrace();
		} catch (SAXException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	//
	
	
	// Event selection frame.
	@SuppressWarnings("serial")
	private class SelectEvent extends JFrame implements ActionListener {
		
		
		// Initiate list.
		JList list = null;
		//
		
		
		// Class method.
		SelectEvent() {
			
			
			// Initiate objects.
			JPanel 				panel 		= new JPanel(new GridBagLayout());
			GridBagConstraints 	c 			= new GridBagConstraints();
			DefaultListModel 	listModel 	= null;
			Document 			document;
			//

			
			// XML List related methods.
			try {
				
				
				// Initiate builders.
				DocumentBuilderFactory 	documentFactory = DocumentBuilderFactory.newInstance();
				DocumentBuilder 		documentBuilder = documentFactory.newDocumentBuilder();
				//
				
				
				// Create a new file if it doesn't exist.
				if (!xmlList.exists()) 
				{
					document = documentBuilder.newDocument();
					createXmlList(document);
				}
				//

				
				// Open the existing file.
				else 
				{ 
					document = documentBuilder.parse(xmlList); 
				}
				//
				
				
				// Create a list of the existing events.
				Node 	 root 	= document.getFirstChild();
				NodeList events = root.getChildNodes();
				//

				
				// Add each event to a JList.
				listModel = new DefaultListModel();
				
				for (int x = 0; x < events.getLength(); x++) 
				{
					listModel.addElement(events.item(x).getNodeName());
				}
				
			}
			//



			// Exceptions
			catch (SAXException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			} catch (ParserConfigurationException e) {
				e.printStackTrace();
			}
			//

			
			// JList configuration.
			list = new JList(listModel);

			list.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
			list.setPreferredSize(new Dimension(200,100));
			list.setBorder(new LineBorder(Color.LIGHT_GRAY,1, false));
			//
			
			
			// Add the list to the panel.
			c.gridx 	 = 0;
			c.gridy 	 = 0;
			c.gridheight = 4;
			c.insets	 = new Insets(10,10,10,10);
			c.fill 		 = GridBagConstraints.HORIZONTAL;
			
			panel.add(list,c);
			//
			
			
			// OK.
			JButton okButton = new JButton("OK");
			
			c.insets 	 = new Insets(10,0,0,10);
			c.gridx 	 = 1;
			c.gridy 	 = 0;
			c.gridheight = 1;
			
			panel.add(okButton,c);
			//
			
			
			// New.
			JButton newButton = new JButton("New");
			
			c.insets = new Insets(8,0,0,10);
			c.gridx  = 1;
			c.gridy  = 1;
			
			panel.add(newButton,c);
			//
			
			
			// Cancel.
			JButton cancelButton = new JButton("Cancel");
			
			c.gridx = 1;
			c.gridy = 2;
			
			panel.add(cancelButton,c);
			//
			
			
			// Buttons customization.
			for (int x = 1; x < 4; x++) {
				JButton button = (JButton) panel.getComponent(x);
				
				button.setPreferredSize(new Dimension(94,28));
				button.setBackground(Color.decode("#EDEDED"));
				button.setForeground(Color.DARK_GRAY);
				button.setBorder(new LineBorder(Color.LIGHT_GRAY,1, false));
				button.addActionListener(this);
			}
			//
			
			
			// Frame properties.
			setSize(400,400);
			setTitle("Select or create an event");
			setResizable(false);
			setContentPane(panel);
			pack();
			setLocationRelativeTo(null);
			setVisible(true);
		}
		//
		
		
		// Action listener.
		public void actionPerformed(ActionEvent e) {

			
			// Find pressed button.
			String button = ((JButton) e.getSource()).getText();
			//
			
			
			// Buttons functions.
			if (button == "OK") {
				xml = isEvent(list.getSelectedValue().toString());
				Processing.setRecording(true);
				dispose();
			}

			if (button == "Cancel") 
			{
				dispose();
			}
		}
	}
	//
	
	
	// Main method.
	public SavingOld(boolean bool) {
		if (bool) {
			popList();
			start();
		}
		else stop();
	}
	//
}
