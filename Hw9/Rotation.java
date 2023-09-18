
package Hw9;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.File;

import javax.swing.JLabel;
import javax.swing.JTextField;

import Hw9.Util;


public class Rotation extends AbstractBasic {
	private static final long serialVersionUID = 1L;
	JTextField tfTheta;
	JTextField tfRed;
	JTextField tfGreen;
	JTextField tfBlue;
	Rotation() {
	};

	Rotation(File file) {
		super(file);
		JLabel lbRed = new JLabel("背景 (R)");
		lbRed.setBounds(400, 5, 50, 30);
		add(lbRed);
		tfRed = new JTextField(5);
		tfRed.setBounds(480, 5, 50, 30);
		add(tfRed);
		
		JLabel lbGreen = new JLabel("背景 (G)");
		lbGreen.setBounds(560, 5, 50, 30);
		add(lbGreen);
		tfGreen = new JTextField(5);
		tfGreen.setBounds(640, 5, 50, 30);
		add(tfGreen);
		
		JLabel lbBlue = new JLabel("背景 (B)");
		lbBlue.setBounds(720, 5, 50, 30);
		add(lbBlue);
		tfBlue = new JTextField(5);
		tfBlue.setBounds(800, 5, 50, 30);
		add(tfBlue);
		
		JLabel lbTheta = new JLabel("旋轉角度 (1~89度)");
		lbTheta.setBounds(350, 40, 150, 30);
		add(lbTheta);
		tfTheta = new JTextField(5);
		tfTheta.setBounds(500, 40, 100, 30);
		add(tfTheta);
	}
	
	@Override
	void exe() {
		{	
			int deg = Integer.parseInt(tfTheta.getText());
			if (deg > 89 || deg < 1)
				return;
			double theta = 1.0 * deg / 180 * Math.PI;	// 徑度
			double heightCos = height * Math.cos(theta);
			double heightSin = height * Math.sin(theta);
			double widthCos = width * Math.cos(theta);
			double widthSin = width * Math.sin(theta);
			int newWidth = (int) (widthCos + heightSin);
			int newHeight = (int) (heightCos + widthSin);

			imgAft= new BufferedImage(newWidth, newHeight, BufferedImage.TYPE_INT_ARGB);
			// 反矩陣
			double[][] matrix = { { Math.cos(theta), 1.0 * Math.sin(theta), 0 },
					{ -1 * Math.sin(theta), Math.cos(theta), 0.0 }, { 0.0, 0.0, 1.0 } };
			fillColor(imgAft);
			for(int y=0;y<newHeight; y++) {
				for(int x=0 ; x<newWidth ; x++) {
					// 中心點平移到原點
					double Position1 [] = { x -newWidth / 2 ,newHeight - y - newHeight/ 2, 1};	// x' y' 
					// 旋轉
					double ret[] = Util.affine(matrix, Position1);  // x, y
					// 移回來，得到 (x,y)
					double X = ret[0] + width/2.0;
					double Y = height - (ret[1]+height/2.0);
					// 使用 bilinear 
					if(X >=0 && X < width && Y>=0 && Y<height) {
						int rgb = exeBilinear(X,Y) ;
						imgAft.setRGB(x,y,rgb);
					}
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
		setTitle(Util.cmdRotation);
		btnCmd.setText(Util.cmdRotation);
	}
	void fillColor(BufferedImage img) {
		int r = Integer.parseInt(tfRed.getText());
		int g = Integer.parseInt(tfGreen.getText());
		int b = Integer.parseInt(tfBlue.getText());
		int rgb = Util.makeColor(r, g, b);
		for (int i = 0; i < img.getHeight(); i++) {
			for (int j = 0; j < img.getWidth(); j++) {
				img.setRGB(j, i, rgb);
			}
		}
	}

	int exeBilinear(double x, double y) {
		int fX = Util.checkImageBound((int) Math.floor(x), width);
		int cX = Util.checkImageBound((int) Math.ceil(x), width);
		int fY = Util.checkImageBound((int) Math.floor(y), height);
		int cY = Util.checkImageBound((int) Math.ceil(y), height);
		double alpha = x - fX;
		double beta = y - fY;
		int leftTopRGB = imgBef.getRGB(fX, fY);		
		int rightTopRGB = imgBef.getRGB(cX, fY);		
		int leftBottomRGB = imgBef.getRGB(fX, cY);		
		int rightBottomRGB = imgBef.getRGB(cX, cY);
		return Util.bilinear(leftTopRGB, rightTopRGB, leftBottomRGB, rightBottomRGB,alpha,beta);
	}
}