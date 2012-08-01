package gui;

import java.awt.Dimension;

import javax.swing.JFrame;
import javax.swing.JPanel;

public class Gui extends JFrame{
	
	
	public Gui(){
		setSize(300,400);
		setVisible(true);
		setResizable(false);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setLocationRelativeTo(null);
		setTitle("Coque");
		add( new GamePanel());
	}
	
	public static void main(String[] args){
		
		Gui guiGame = new Gui();
	}

}
