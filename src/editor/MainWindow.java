package editor;


import gui.GamePanel;

import java.awt.Dimension;

import javax.swing.JFrame;
import javax.swing.JPanel;


public class MainWindow extends JFrame{
	
	
	public MainWindow(){
		
		JPanel editor = new EditorPanel();
		
		setSize(1200,800);
		setVisible(true);
		setResizable(true);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setLocationRelativeTo(null);
		setTitle("Coque Editor");
		add( editor );
	
	}

	public static void main(String[] arg){
	
		JFrame main = new MainWindow();
		
	}
}
