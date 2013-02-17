package main;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

import javax.swing.ButtonGroup;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.border.LineBorder;

@SuppressWarnings("serial")
public class Interface extends JFrame {
	
	boolean init = true;
	public static EegChart eeg;
	
	public static Square square = new Square();

	// Channels' names list.
	private final static String[] channelsList = { 
		"1 (AF3)", "2 (F7)",   "3 (F3)",
		"4 (FC5)", "5 (T7)",   "6 (P7)",
		"7 (O1)",  "8 (O2)",   "9 (P8)",
		"10 (T8)", "11 (FC6)", "12 (F4)",
		"13 (F8)", "14 (AF4)"
	};

	public static String getChannelName(int channel) 
	{
		return channelsList[channel];
	}
	//

	
	// Colors.
	private final Color panelColor = Color.WHITE;
	private final Color chartColor = Color.decode("#1D1D1D");
	private final Color linesColor = Color.decode("#858585");
	//
	
	
	Interface() throws InterruptedException {
		
		// Define the panel and constraints.
		JPanel panel = new JPanel(new GridBagLayout());
		GridBagConstraints c = new GridBagConstraints();
		//
		
		
		// Radiogroup.
		RadioGroup radio = new RadioGroup();
		
		c.gridx 		 = 0;
		c.gridy 		 = 0;
		
		c.insets 		 = new Insets(10, 10, 10, 0);
		c.gridheight 	 = 2;
		c.ipadx 		 = 10;
		c.ipady 		 = 0;
		c.anchor 		 = GridBagConstraints.NORTH;
		c.fill 			 = GridBagConstraints.VERTICAL;
		
		panel.add(radio, c);
		//
		
		
		// EEG plot.
		XYChart eeg 	= new EegChart();
		eeg.setChartColor(chartColor);
		eeg.setLinesColor(linesColor);
		
		c.gridx 		= 1;
		c.gridy 		= 0;
		
		c.insets 		= new Insets(5, 5, 0, 0);
		c.gridheight 	= 1;
		c.ipadx 		= 0;
		c.fill 			= GridBagConstraints.CENTER;
		c.gridwidth 	= 3;
		
		panel.add(eeg, c);
		//
		
		
		// FFT plot.
		XYChart fft 	= new FftChart();
		fft.setChartColor(chartColor);
		fft.setLinesColor(linesColor);
		
		c.gridx 		= 1;
		c.gridy 		= 1;
		
		c.gridwidth 	= 1;
		c.insets 		= new Insets(0, 5, 5, 0);
		
		panel.add(fft, c);
		//


		//Square sq = new Square();
		JPanel squarePanel = new JPanel(new GridBagLayout());
		
		c.gridx 	= 0;
		c.gridy 	= 0;
		c.gridwidth = 1;
		c.insets 	= new Insets(0,0,0,0);
		
		squarePanel.add(square,c);
		squarePanel.setPreferredSize(new Dimension(250,250));
		squarePanel.setBorder(new LineBorder(Color.LIGHT_GRAY,1, false));
		squarePanel.setBackground(Color.WHITE);
		
		c.gridx  = 2;
		c.gridy  = 1;
		c.insets = new Insets(5,5,0,0);
		
		panel.add(squarePanel,c);
		//
		
		
		// Bands chart.
		BarChart bands 	= new BarChart();
		
		c.gridx 		= 3;
		c.gridy 		= 1;
		
		c.insets 		= new Insets(0, 5, 0, 0);
		
		bands.setChartColor(chartColor);
		bands.setLinesColor(linesColor);
		panel.add(bands, c);
		//
		
		
		// Values panel
		Values values 	 = new Values();
		
		c.gridx 		 = 4;
		c.gridy 		 = 0;
		
		c.insets 		 = new Insets(10, 5, 10, 10);
		c.anchor 		 = GridBagConstraints.NORTH;
		c.fill 			 = GridBagConstraints.VERTICAL;
		c.gridheight 	 = 2;
		
		panel.add(values, c);
		//
		
		
		// Frame properties.
		setSize(1500, 600);
		setTitle("Mind Reader");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setResizable(false);
		getContentPane().add(panel);
		pack();
		setLocationRelativeTo(null);
		setVisible(true);
		//
		
		
		// Initiate connection to the device.
		new Connection();
	}
	//
	
	
	// Radiogroup
	private class RadioGroup extends JPanel implements ItemListener {
		
		private RadioGroup() {
			
			
			// Define a temporary object to create new buttons.
			ButtonGroup radioGroup = new ButtonGroup();
			//
			
			
			// Layout.
			setBorder(new LineBorder(Color.LIGHT_GRAY,1, false));
			setBackground(panelColor);
			setLayout(new GridLayout(channelsList.length, 1));
			//
			
			
			// Add each button to the group and panel.
			for (int x = 0; x < 14; x++) {

				JRadioButton radioButton = new JRadioButton();
				
				radioButton.setText(channelsList[x]);
				radioButton.setName(Integer.toString(x));
				radioButton.addItemListener(this);
				radioButton.setBackground(panelColor);
				radioButton.setForeground(Color.DARK_GRAY);
				radioButton.setFont(new Font("Dialog", Font.ITALIC, 12));
				radioButton.setMargin(new Insets(0,10,0,15));
				
				if (x == Processing.getChannel()) radioButton.setSelected(true);
				
				radioGroup.add(radioButton);
				add(radioButton);
			}
		}
		//
		
		
		// Listen for clicks on the buttons.
		public void itemStateChanged(ItemEvent e) {
			
			
			// Find the selected button.
			JRadioButton selectedRadio = (JRadioButton) e.getItem();
			//
			
			
			// If there is a new selection.
			if (e.getStateChange() == ItemEvent.SELECTED) {
				Processing.setChannel(Integer.parseInt(selectedRadio.getName()));
				Values.setMaxValue(0.0);
				Values.setMinValue(0.0);

				if (init) init = false;
				else 
				{
					EegChart.addMarker(Integer.parseInt(selectedRadio.getName()));
				}
			}
		}
	}
}