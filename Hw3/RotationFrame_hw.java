package Hw3;

import java.awt.Graphics;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import Hw3.Util;

public class RotationFrame_hw extends JFrame {
	private static final long serialVersionUID = 1L;
	JPanel cotrolPanelMain = new JPanel();
	JPanel cotrolPanelShow = new JPanel();;
	JPanel cotrolPanelBackColor = new JPanel();;
	JPanel cotrolPanelRotate = new JPanel();
	ImagePanel imagePanel;
	JButton btnShow;
	JButton btnRotate;
	JTextField tfRed = new JTextField(5);
	JTextField tfGreen = new JTextField(5);
	JTextField tfBlue = new JTextField(5);
	BufferedImage img;
	BufferedImage newImg;
	JTextField tfTheta = new JTextField(5);
	JLabel lbRed = new JLabel("背景 (R)");
	JLabel lbGreen = new JLabel("背景 (G)");
	JLabel lbBlue = new JLabel("背景 (B)");
	JLabel lbTheta = new JLabel("旋轉角度 (1~89度)");
	final int[][][] data;
	int height;
	int width;

	RotationFrame_hw() {
		setBounds(0, 0, 1500, 1500);
		getContentPane().setLayout(null);
		tfRed.setText("0");
		tfGreen.setText("0");
		tfBlue.setText("0");
		tfTheta.setText("0");
		setTitle("Rotation Homework");
		try {
			img = ImageIO.read(new File("plate.png"));
		} catch (IOException e) {
			System.out.println("IO exception");
		}
		height = img.getHeight();
		width = img.getWidth();
		data = new int[height][width][3];
		for (int y = 0; y < height; y++) {
			for (int x = 0; x < width; x++) {
				int rgb = img.getRGB(x, y);
				data[y][x][0] = Util.getR(rgb);
				data[y][x][1] = Util.getG(rgb);
				data[y][x][2] = Util.getB(rgb);
			}
		}
		btnShow = new JButton("顯示");
		btnRotate = new JButton("旋轉");
		cotrolPanelMain = new JPanel();
		cotrolPanelMain.setLayout(new GridLayout(1, 3));
		cotrolPanelShow.add(btnShow);
		cotrolPanelMain.add(cotrolPanelShow);
		cotrolPanelBackColor.setLayout(new GridLayout(3, 2));
		cotrolPanelBackColor.add(lbRed);
		cotrolPanelBackColor.add(tfRed);
		cotrolPanelBackColor.add(lbGreen);
		cotrolPanelBackColor.add(tfGreen);
		cotrolPanelBackColor.add(lbBlue);
		cotrolPanelBackColor.add(tfBlue);
		cotrolPanelMain.add(cotrolPanelBackColor);
		cotrolPanelRotate.add(lbTheta);
		cotrolPanelRotate.add(tfTheta);
		cotrolPanelRotate.add(btnRotate);
		cotrolPanelMain.add(cotrolPanelRotate);
		cotrolPanelMain.setBounds(0, 0, 360, 80);
		getContentPane().add(cotrolPanelMain);
		imagePanel = new ImagePanel();
		imagePanel.setBounds(0, 100, 1500, 1500);
		getContentPane().add(imagePanel);
		btnShow.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				Graphics g = imagePanel.getGraphics();
				imagePanel.paintComponent(g);
				g.drawImage(img, 0, 0, null);
			}
		});

		btnRotate.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
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
				newImg = new BufferedImage(newWidth, newHeight, BufferedImage.TYPE_INT_ARGB);
				// 反矩陣
				double[][] matrix = { { Math.cos(theta), 1.0 * Math.sin(theta), 0 },
						{ -1 * Math.sin(theta), Math.cos(theta), 0.0 }, { 0.0, 0.0, 1.0 } };
				fillColor(newImg);
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
							newImg.setRGB(x,y,rgb);
						}
					}
				}
				Graphics g = imagePanel.getGraphics();
				imagePanel.paintComponent(g);
				g.drawImage(newImg, 0, 0, null);
			}
		});
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
		int leftTopRGB = img.getRGB(fX, fY);		
		int rightTopRGB = img.getRGB(cX, fY);		
		int leftBottomRGB = img.getRGB(fX, cY);		
		int rightBottomRGB = img.getRGB(cX, cY);
		return Util.bilinear(leftTopRGB, rightTopRGB, leftBottomRGB, rightBottomRGB,alpha,beta);
	}
	
	public static void main(String[] args) {
		RotationFrame_hw frame = new RotationFrame_hw();
		frame.setSize(1500, 1500);
		frame.setVisible(true);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}
}
/*
 
 */
