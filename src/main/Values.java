package main;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSeparator;
import javax.swing.border.LineBorder;

@SuppressWarnings("serial")
public class Values extends JPanel implements MouseListener {

	
	// Labels.
	private 		 JLabel instantLabel = new JLabel("Instant");
	private static JLabel	instantValue = new JLabel("(n/a)");
	
	private 		 JLabel lateLabel = new JLabel("1 Second");
	private static JLabel lateValue = new JLabel("(n/a)");
	
	private 		 JLabel rangeLabel = new JLabel("Range");
	private static JLabel rangeValue = new JLabel("(n/a)");
	
	private 		 JLabel bandsLabel = new JLabel("Bands");
	private static JLabel bandsValue = new JLabel("(n/a)");
	
	private 		 JLabel maxLabel = new JLabel("Max");
	private static JLabel maxValue = new JLabel("0.0");
	
	private 		 JLabel minLabel = new JLabel("Min");
	private static JLabel minValue = new JLabel("0.0");
	//
	
	
	private Color textColor = Color.decode("#1D1D1D");
	
	
	// Getters & Setters.
	public static JLabel getInstantValue()
	{
		return instantValue;
	}
	
	public static void setInstantValue(double value) 
	{
		getInstantValue().setText(Double.toString(value));
	}
	
	public static void setInstantColor(Color color)
	{
		getInstantValue().setForeground(color);
	}
	
	public static JLabel getLateValue()
	{
		return lateValue;
	}
	
	public static void setLateValue(double value)
	{
		getLateValue().setText(Double.toString(value));
	}
	
	public static JLabel getRangeValue()
	{
		return rangeValue;
	}
	
	public static void setRangeValue(String range)
	{
		getRangeValue().setText(range);
	}
	
	public static JLabel getBandsValue()
	{
		return bandsValue;
	}
	
	public static void setBandValue(String band)
	{
		getBandsValue().setText(band);
	}
	
	public static void setMaxValue(double max)
	{
		maxValue.setText(Double.toString(max));
	}
	
	public static double getMaxValue() 
	{
		return Double.parseDouble(maxValue.getText());
	}
	
	public static void setMinValue(double min)
	{
		minValue.setText(Double.toString(min));
	}
	
	public static double getMinValue()
	{
		return Double.parseDouble(minValue.getText());
	}
	//
	
	
	// Main method.
	Values() {
		
		
		setLayout(new GridBagLayout());
		
		
		// Recurrent properties.
		GridBagConstraints c = new GridBagConstraints();
		Font 	boldFont	 = new Font("Dialog", Font.BOLD, 10);
		Font 	valueFont	 = new Font("Dialog", Font.ITALIC, 15);
		Insets 	labelInsets	 = new Insets(30,10,0,0);
		Insets 	valueInsets	 = new Insets(5,10,0,0);
		//
		
		
		// Initial constraints.
		c.anchor = GridBagConstraints.LINE_START;
		c.fill 	 = GridBagConstraints.LINE_START;
		//
		
		
		// Instant label.
		c.insets = new Insets(10,10,0,0);
		c.gridx  = 0;
		c.gridy  = 0;
		
		instantLabel.setFont(boldFont);
		instantLabel.setForeground(textColor);
		
		add(instantLabel,c);
		//
		
		
		// Instant value.
		getInstantValue().setFont(valueFont);
		
		c.insets = valueInsets;
		c.gridx  = 0;
		c.gridy  = 1;
		
		add(getInstantValue(),c);
		//
		
		
		// Late label.
		c.insets = labelInsets;
		c.gridx  = 0;
		c.gridy  = 2;
		
		lateLabel.setFont(boldFont);
		lateLabel.setForeground(textColor);
		
		add(lateLabel,c);
		//
		
		
		// Late value.
		getLateValue().setFont(valueFont);
		
		c.insets = valueInsets;
		c.gridx  = 0;
		c.gridy  = 3;
		
		add(getLateValue(),c);
		//
		
		
		// Range label.
		c.insets = labelInsets;
		c.gridx  = 0;
		c.gridy  = 4;
		
		rangeLabel.setFont(boldFont);
		rangeLabel.setForeground(textColor);
		
		add(rangeLabel,c);
		//
		
		
		// Range value.
		getRangeValue().setFont(valueFont);
		
		c.insets = valueInsets;
		c.gridx  = 0;
		c.gridy  = 5;
		
		add(getRangeValue(),c);
		//
		
		
		// Bands label.
		c.insets = labelInsets;
		c.gridx  = 0;
		c.gridy  = 6;
		
		bandsLabel.setFont(boldFont);
		bandsLabel.setForeground(textColor);
		
		add(bandsLabel,c);
		//
		
		// Bands value.
		getBandsValue().setFont(valueFont);
		
		c.insets = valueInsets;
		c.gridx  = 0;
		c.gridy  = 7;
		
		add(getBandsValue(),c);
		//
		
		
		// Max label.
		c.insets = labelInsets;
		c.gridx  = 0;
		c.gridy  = 8;
		
		maxLabel.setFont(boldFont);
		maxLabel.setForeground(textColor);
		
		add(maxLabel,c);
		//
		
		
		// Max value.
		maxValue.setName("max");
		maxValue.setFont(valueFont);
		maxValue.addMouseListener(this);
		
		c.insets = valueInsets;
		c.gridx  = 0;
		c.gridy  = 9;
		
		add(maxValue,c);
		//
		
		
		// Min label.
		c.insets = labelInsets;
		c.gridx  = 0;
		c.gridy  = 10;
		
		minLabel.setFont(boldFont);
		minLabel.setForeground(textColor);
		
		add(minLabel,c);
		//
		
		
		// Min value.
		minValue.setName("min");
		minValue.setFont(valueFont);
		minValue.addMouseListener(this);
		
		c.insets = valueInsets;
		c.gridx  = 0;
		c.gridy  = 11;

		add(minValue,c);
		//
		
		
		// Separator.
		JSeparator s = new JSeparator(JSeparator.HORIZONTAL);
		s.setPreferredSize(new Dimension(1,5));
		s.setForeground(Color.LIGHT_GRAY);
		
		c.gridx  = 0;
		c.gridy  = 12;
		c.fill   = GridBagConstraints.HORIZONTAL;
		c.insets = new Insets(25,4,0,4);
		
		add(s,c);
		//
		
		
		// Patterns buttons.
		Patterns patterns = new Patterns();
		
		c.insets = new Insets(-3,0,0,0);
		c.anchor = GridBagConstraints.SOUTH;
		c.gridx  = 0;
		c.gridy  = 13;
		
		add(patterns,c);
		//
		
		
		// Customization.
		setBorder(new LineBorder(Color.LIGHT_GRAY,1, false));
		setPreferredSize(new Dimension(110,500));
		
		setBackground(Color.WHITE);
		//
	}


	// Mouse events.
	public void mouseClicked(MouseEvent e) {
		if (e.getClickCount() == 2) {
			if (e.getComponent().getName() == "max") maxValue.setText("0.0");
			else if (e.getComponent().getName() == "min") minValue.setText("0.0");
		}
	}

	public void actionPerformed(ActionEvent arg0) {}
	public void mouseEntered(MouseEvent arg0) {}
	public void mouseExited(MouseEvent arg0) {}
	public void mousePressed(MouseEvent arg0) {}
	public void mouseReleased(MouseEvent arg0) {}
	//
}

