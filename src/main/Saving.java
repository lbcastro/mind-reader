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
import java.net.URL;
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


public class Saving {


	// Lists to store raw data.
	static List<List<Double>> rawList = new ArrayList<List<Double>>();

	public static List<List<Double>> getRawList() 
	{
		return rawList;
	}

	public static List<Double> getRawList(int channel) 
	{
		return rawList.get(channel);
	}

	static List<Double> rawList0  = new ArrayList<Double>();
	static List<Double> rawList1  = new ArrayList<Double>();
	static List<Double> rawList2  = new ArrayList<Double>();
	static List<Double> rawList3  = new ArrayList<Double>();
	static List<Double> rawList4  = new ArrayList<Double>();
	static List<Double> rawList5  = new ArrayList<Double>();
	static List<Double> rawList6  = new ArrayList<Double>();
	static List<Double> rawList7  = new ArrayList<Double>();
	static List<Double> rawList8  = new ArrayList<Double>();
	static List<Double> rawList9  = new ArrayList<Double>();
	static List<Double> rawList10 = new ArrayList<Double>();
	static List<Double> rawList11 = new ArrayList<Double>();
	static List<Double> rawList12 = new ArrayList<Double>();
	static List<Double> rawList13 = new ArrayList<Double>();

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
	
	private void clearLists() {
		for (int x = 0; x < 14; x++) {
			rawList.get(x).clear();
		}
	}

	public static void addRaw(double value, int channel) 
	{
		getRawList(channel).add(value);
	}
	//


	// Events list.
	static File	 xmlList = eventLocation("locations");

	private static File getXmlList() 
	{
		return xmlList;
	}
	//


	// Current event.
	private static String eventName;

	private static void setEventName(String name) 
	{
		eventName = name;
	}

	private static String getEventName() 
	{
		return eventName;
	}

	public static File eventLocation(String name) 
	{
		String fileLoc = "ptr/" + name.toString() + ".xml";
		
		
		File file = new File(fileLoc);

		return file;
	}
	//

	
	// Returns saved data.
	public static String[] returnData(String event, int channel) {

		File file = eventLocation(event);
		String[] saved = new String[14];
		String[] savedSplit = null;
		
		try {
			DocumentBuilderFactory 	documentFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder 		documentBuilder = documentFactory.newDocumentBuilder();
			Document 				document 		= documentBuilder.parse(file); 
			
			Node root = document.getFirstChild();
			Node rec  = root.getFirstChild();
			Node raw = rec.getFirstChild();
			Node values = raw.getFirstChild();
			
			
			saved = values.getTextContent().split("],");
			
			for (int x = 0; x < 14; x++) {
				saved[x] = saved[x].trim().replaceAll("]", "");
				if (x == 0) saved[x] = saved[x].substring(2);
				else saved[x] = saved[x].substring(1);
				
			}
			
			savedSplit = saved[channel].split(",");
			
		} 


		
		catch (ParserConfigurationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SAXException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return savedSplit;
	}
	//
	
	
	
	
	
	// Save the changes made to specific documents.
	private void saveFile(Document document, File file) {
		try {
			TransformerFactory 	transformerFactory 	= TransformerFactory.newInstance();
			Transformer 		transformer			= transformerFactory.newTransformer();
			DOMSource 			source 				= new DOMSource(document);
			StreamResult 		result 				= new StreamResult(file);

			transformer.transform(source, result);
		} 
		//


		// Exceptions.
		catch (TransformerException e) {
			e.printStackTrace();
		}
	}
	//


	// Create XML with a specific root element.
	private void createXml(File file, String root) throws TransformerException {
		try {


			// Set up document builders.
			DocumentBuilderFactory 	documentFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder 		documentBuilder = documentFactory.newDocumentBuilder();
			Document 				document 		= documentBuilder.newDocument();
			//


			// Create a root node.
			Element first = document.createElement(root);
			document.appendChild(first);
			//


			// Save the changes.
			saveFile(document, file);
			//
		}


		// Exceptions.
		catch (ParserConfigurationException e) {
			e.printStackTrace();
		}
	}
	//


	// Creates specific nodes to save EEG data.
	private void createNodes(Document document, File file) {


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
			//Element chan = document.createElement("channel");
			//chan.setAttribute("number", String.valueOf(x));
			//next.appendChild(chan);
			//


			// Create raw data nodes.
			Node raw 	  = document.createElement("raw");
			Node rawValue = document.createElement("values");

			//Node rawMinValue		= document.createElement("min");
			//Node rawMaxValue 	= document.createElement("max");
			//Node rawAverageValue = document.createElement("average");

			next.appendChild(raw);
			raw.appendChild(rawValue);

			//raw.appendChild(rawMinValue);
			//raw.appendChild(rawMaxValue);
			//raw.appendChild(rawAverageValue);
			//

			/*
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
			 */
		}


		// Save the changes.
		saveFile(document, file);
	}
	//


	// Event selection frame.
	@SuppressWarnings("serial")
	private class SelectEvent extends JFrame implements ActionListener {


		// Initiate list.
		JList list = null;
		//


		// Class method.
		SelectEvent() throws TransformerException {


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
					createXml(getXmlList(), "events");
				}
				//


				// Open the existing file.
				document = documentBuilder.parse(xmlList); 
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
				setEventName(list.getSelectedValue().toString());
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


	// Separates and writes each value to a file.
	private void sortEeg() {
		try {


			// Load/create required event XML.
			File file = eventLocation(getEventName());
			if (!file.exists()) {
				createXml(file, "event");
			}
			//


			// Initiate builders.
			DocumentBuilderFactory 	documentFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder 		documentBuilder = documentFactory.newDocumentBuilder();
			Document 				document 		= documentBuilder.parse(file);
			//


			// Create required nodes.
			createNodes(document, file);
			//


			Node root 	  = document.getFirstChild();
			Node lastRec  = root.getLastChild();

			//Node nextChan = lastRec.getFirstChild();
			Node nextRaw  = lastRec.getFirstChild();
			Node rawValue = nextRaw.getFirstChild();


			//raw.appendChild(rawMinValue);
			//raw.appendChild(rawMaxValue);
			//raw.appendChild(rawAverageValue);

			

			rawValue.setTextContent(String.valueOf(getRawList()));
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
			clearLists();
			getRawList().clear();

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
			saveFile(document, file);
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


	// Main method.
	public Saving(boolean bool) throws TransformerException {

		if (bool) {
			popList();
			new SelectEvent();
		}
		else {
			Processing.setRecording(false);
			sortEeg();
		}
	}
	//
}
