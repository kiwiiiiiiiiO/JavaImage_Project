
package Hw9;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.File;

import javax.swing.JLabel;
import javax.swing.JTextField;

import Hw9.Util;


public class Translation extends AbstractBasic {
	private static final long serialVersionUID = 1L;
	JTextField tfDeltaX ;
	JTextField tfDeltaY ;
	// 位移
	
	Translation() {
	};

	Translation(File file) {
		super(file);
		tfDeltaX = new JTextField(5);
		tfDeltaY = new JTextField(5);
		JLabel lbDeltaY = new JLabel("y軸位移");
		JLabel lbDeltaX = new JLabel("x軸位移");
		
		tfDeltaX.setText("0");
		tfDeltaY.setText("0");
		
		lbDeltaX.setBounds(400, 5, 150, 30);
		add(lbDeltaX);
		tfDeltaX.setBounds(400, 50, 150, 30);
		add(tfDeltaX);
		lbDeltaY.setBounds(600, 5, 150, 30);
		add(lbDeltaY);
		tfDeltaY.setBounds(600, 50, 150, 30);
		add(tfDeltaY);
		// 倍率
	}
	
	@Override
	void exe() {
		{
			double deltax = Double.parseDouble(tfDeltaX.getText());
			double deltay = Double.parseDouble(tfDeltaY.getText());
			double a [][]= {{1,0,deltax},{0,1,deltay},{0,0,1}};
			double b []= new double[3];
			
			b[0]=0;b[1]=0;b[2]=1;
			
			double[] ret = Util.affine(a, b);
			Util.saveImg = imgBef;
			Graphics g = imagePanelBef.getGraphics();
			imagePanelBef.paintComponent(g);
			g.drawImage(imgBef , (int)(ret[0]), (int)(ret[1]), null);
		}
		
	}

	@Override
	void decorate() {
		setTitle(Util.cmdTranslation);
		btnCmd.setText(Util.cmdTranslation);
	}
}