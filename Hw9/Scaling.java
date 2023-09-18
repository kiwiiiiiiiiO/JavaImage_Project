
package Hw9;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.File;

import javax.swing.JLabel;
import javax.swing.JTextField;

import Hw9.Util;


public class Scaling extends AbstractBasic {
	private static final long serialVersionUID = 1L;
	JTextField tfAmpX;
	JTextField tfAmpY;
	Scaling() {
	};

	Scaling(File file) {
		super(file);
		tfAmpX = new JTextField(5);
		tfAmpY = new JTextField(5);
		JLabel lbAmpX = new JLabel("x軸倍率");
		JLabel lbAmpY = new JLabel("y軸倍率");
		tfAmpX.setText("1.0");
		tfAmpY.setText("1.0");
		lbAmpX.setBounds(400, 5, 150, 30);
		add(lbAmpX);
		tfAmpX.setBounds(400, 50, 150, 30);
		add(tfAmpX);
		lbAmpY.setBounds(600, 5, 150, 30);
		add(lbAmpY);
		tfAmpY.setBounds(600, 50, 150, 30);
		add(tfAmpY);
	}
	
	@Override
	void exe() {
		{	
			double ax = Double.parseDouble(tfAmpX.getText());
			double ay = Double.parseDouble(tfAmpY.getText());
			// 改變大小，並計算新img大小，新影像從舊影像取值
			double newImgWidth = width*(ax);
			double newImgHeight = height*(ay);
			imgAft = new BufferedImage((int)newImgWidth, (int)newImgHeight, BufferedImage.TYPE_INT_ARGB);
			// 由新圖片往舊圖片mapping，因此使用S^-1
			// 處理座標，affine transform
			double a[][] = {{1/ax,0,0},{0,1/ay,0},{0,0,1}};
			
			for(int y=0; y<(int)newImgHeight; y++) {
				for(int x=0; x<(int)newImgWidth; x++) {
					double b[] = {x,y,1};
					// 對應原圖的 x,y
					double[] ret = Util.affine(a, b);
					// 依照比例原則分配RGB，使用bilinear
					// 有四點，並有alpha、beta兩weight
					int fX = Util.checkImageBound((int)Math.floor(ret[0]),width-1);
					int fY = Util.checkImageBound((int)Math.floor(ret[1]),height-1);
					int cX = Util.checkImageBound((int)Math.ceil(ret[0]),width-1);
					int cY = Util.checkImageBound((int)Math.ceil(ret[1]),height-1);
					double alpha = ret[0]-fX;
					double beta = ret[1]-fY;
					
					int leftTopRGB = imgBef.getRGB(fX, fY);		
					int rightTopRGB = imgBef.getRGB(cX, fY);		
					int leftBottomRGB = imgBef.getRGB(fX, cY);		
					int rightBottomRGB = imgBef.getRGB(cX, cY);	
					int rgb = Util.bilinear(leftTopRGB, rightTopRGB, leftBottomRGB, rightBottomRGB,alpha,beta);
		
					imgAft.setRGB(x, y, rgb);
				}
			}
			Util.saveImg = imgAft;
			
			Graphics g = imagePanelAft.getGraphics();
			imagePanelAft.paintComponent(g);
			g.drawImage(imgAft,0,0, null);
		}
		
	}

	@Override
	void decorate() {
		setTitle(Util.cmdScaling);
		btnCmd.setText(Util.cmdScaling);
	}
}