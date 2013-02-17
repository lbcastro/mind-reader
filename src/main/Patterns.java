package main;

import java.awt.Color;
import java.awt.Desktop;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;

import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.border.LineBorder;
import javax.xml.transform.TransformerException;

@SuppressWarnings("serial")
public class Patterns extends JPanel implements ActionListener {


	// Objects.
	JPanel  panel 	 = new JPanel(new GridLayout(3,1,0,5));
	JButton record 	 = new JButton("Record");
	JButton patterns = new JButton("Patterns");
	JButton exit 	 = new JButton("Exit");
	//


	// Main method.
	Patterns() {


		// Add each object.
		panel.add(record);
		panel.add(patterns);
		panel.add(exit);
		//


		// Objects' properties.
		for (int x = 0; x < panel.getComponentCount(); x++) {

			JButton button = (JButton) panel.getComponent(x);

			button.setPreferredSize(new Dimension(94,28));
			button.setBackground(Color.decode("#EDEDED"));
			button.setForeground(Color.DARK_GRAY);
			button.setBorder(new LineBorder(Color.LIGHT_GRAY,1, false));
			button.addActionListener(this);
		}
		//


		// Customization.
		panel.setBackground(Color.WHITE);
		setBackground(Color.WHITE);
		add(panel);
	}
	//


	// Listener.
	public void actionPerformed(ActionEvent a) {


		// Find pressed button.
		String button = ((JButton) a.getSource()).getText();
		//


		// Buttons functions.
		if (button == "Exit") System.exit(0);
		try {

			if (button == "Record" && !Processing.getRecording()) 
			{
				record.setText("Stop");
				new Saving(true);
			}

			if (button == "Stop" && Processing.getRecording()) 
			{
				record.setText("Record");
				new Saving(false);
			}
			
			if (button == "Patterns") 
			{
				Desktop d = Desktop.getDesktop();
				d.open(new File("/home/lourenco/workspace/Mind Reader/ptr/patterns.xml"));
			}

		}
		//
		
		
		// Exceptions
		catch (IOException e) 
		{
			e.printStackTrace();
		} catch (TransformerException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		//

	}
}