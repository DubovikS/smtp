package template.main;

import java.awt.Dimension;
import java.awt.Toolkit;

import Constants.Constants;
import template.frame.MainFrame;

public class Main {

	public static void main(String[] args) {
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		int sizeWidth = Constants.sizeWidth;
		int sizeHeight = Constants.sizeHeight;
		int locationX = (screenSize.width - sizeWidth) / 2;
		int locationY = (screenSize.height - sizeHeight) / 2;
		
		MainFrame frame = new MainFrame();
		frame.setBounds(locationX, locationY, sizeWidth, sizeHeight);
		frame.setVisible(true);
	}
}
