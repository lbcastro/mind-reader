package main;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.swing.JPanel;
import javax.swing.border.LineBorder;

@SuppressWarnings("serial")
public class Square extends JPanel {

	int a = 38;
	
	private Color neut = new Color(0, 30, 30);
	
	private static List<Integer> lastColor = new ArrayList<Integer>(Collections.nCopies(14, 255));
	
	static List<Color> scolor = new ArrayList<Color>();
	
	private int s1[]  = { a, 	0,   a, a };
	private int s2[]  = { a*4,	0,   a, a };
	private int s3[]  = { 0, 	a,   a, a };
	private int s4[]  = { a*2,	a,   a, a };
	private int s5[]  = { a*3,	a,   a, a };
	private int s6[]  = { a*5,	a,   a, a };
	private int s7[]  = { a, 	a*2, a, a };
	private int s8[]  = { a*4,	a*2, a, a };
	private int s9[]  = { 0, 	a*3, a, a };
	private int s10[] = { a*5,	a*3, a, a };
	private int s11[] = { a, 	a*4, a, a };
	private int s12[] = { a*4,	a*4, a, a };
	private int s13[] = { a*2,	a*5, a, a };
	private int s14[] = { a*3,	a*5, a, a };
	
	private int[] ss[] = { s1, s2, s3, s4, s5, s6, s7, s8, s9, s10, s11, s12, s13, s14 };
	


	// Repaint the squares according to electrodes' activity.
	public void squareRepaint(List<Double> values) {
		int c;
		
		for (int x = 0; x < 14; x++) {
			c = values.get(x).intValue();
			if (c > 255) c = 255;
			if (lastColor.get(x) > c) c = lastColor.get(x) - 1;
			if (c < 0) c = 0;
			
			scolor.set(x, new Color(20, 20, c));
			lastColor.set(x, c);
		}
		repaint();
	}
	//
	

	// Create colored squares.
	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		
		for (int x = 0; x < 14; x++) {
			g.setColor(scolor.get(x));
			g.fillRect(ss[x][0], ss[x][1], ss[x][2], ss[x][3]);
		}
	}
	//
	
	
	// Main method.
	Square() {
		
		for (int x = 0; x < 14; x++) 
		{
			scolor.add(neut);
		}
		
		setBackground(Color.decode("#1D1D1D"));
		setBorder(new LineBorder(Color.LIGHT_GRAY, 1, false));
		setPreferredSize(new Dimension(a*6,a*6));
		setVisible(true);
		
		repaint();
	}
}
