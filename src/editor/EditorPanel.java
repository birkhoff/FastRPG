package editor;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.*;

import javax.swing.JFrame;
import javax.swing.JPanel;


public class EditorPanel extends JPanel{
	
public EditorPanel(){
				
		setPreferredSize( new Dimension(1200,800) );
		setSize(1200,800);
		setVisible(true);
		setFocusable(true);
		requestFocus(true);
		setBackground(Color.BLACK);
		// JPanel layoutManager = new JPanel( new FlowLayout() );
		// layoutManager.add( this);
		JPanel left = new JPanel();
		left.setPreferredSize( new Dimension (1000,800) );
		
		setLayout( new BorderLayout() );
		
		JPanel right = new JPanel();
		right.setPreferredSize( new Dimension(200,800) );
		
		left.setBackground(Color.red);
		right.setBackground(Color.white);

		add(right,BorderLayout.LINE_END);
		add(left, BorderLayout.LINE_START);
		
	}
	
	
	public void addNotify(){
		
		
	}

}
