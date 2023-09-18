package Hw8;


import java.awt.Graphics;
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

import Hw7.Util;

import java.awt.Point;

public class MorFrame extends JFrame {
	String filename = "mor_3.png";
	int count;
	JPanel cotrolPanel;
	JPanel imagePanelLeft;
	JPanel imagePanelRight;
	int[][] binary;
	int[][] newBinary;
	JButton btnShow;
	JButton btnDilate;
	JButton btnErode;
	JButton btnOpen;
	JButton btnClose;
	JButton btnReset;
	JLabel lbCount;
	JTextField tfCount;
	JLabel lbOpenCount;
	JTextField tfOpenCount;
	JLabel lbCloseCount;
	JTextField tfCloseCount;
	int[][][] data;
	int[][][] oridata;
	int height;
	int width;
	static BufferedImage img = null;
	static BufferedImage imgMor = null;
	int [][] neighbor = {{0,1},{0,-1},{1,-1},{1,0},{1,1},{-1,-1},{-1,0},{-1,1}};
	MorFrame() {
		setTitle("Morphological Image Processing (Homework)");
		loadImg();
		setLayout(null);
		btnShow = new JButton("Show Original Image");
		btnDilate = new JButton("Dilate");
		btnErode = new JButton("Erode");
		lbCount = new JLabel("Count");
		tfCount = new JTextField(5);
		tfCount.setEditable(false);
		btnOpen = new JButton("Open");
		lbOpenCount = new JLabel("Times (Open)");
		tfOpenCount = new JTextField(5);
		btnClose = new JButton("Close");
		lbCloseCount = new JLabel("Times (Close)");
		tfCloseCount = new JTextField(5);
		btnReset = new JButton("Reset");
		cotrolPanel = new JPanel();
		cotrolPanel.setBounds(0, 0, 1500, 200);
		getContentPane().add(cotrolPanel);
		cotrolPanel.add(btnShow);
		cotrolPanel.add(btnDilate);
		cotrolPanel.add(btnErode);
		cotrolPanel.add(lbCount);
		cotrolPanel.add(tfCount);
		cotrolPanel.add(btnOpen);
		cotrolPanel.add(lbOpenCount);
		cotrolPanel.add(tfOpenCount);
		cotrolPanel.add(btnClose);
		cotrolPanel.add(lbCloseCount);
		cotrolPanel.add(tfCloseCount);
		cotrolPanel.add(btnReset);
		imagePanelLeft = new ImagePanel();
		imagePanelLeft.setBounds(0, 120, 700, 700);
		getContentPane().add(imagePanelLeft);
		imagePanelRight = new ImagePanel();
		imagePanelRight.setBounds(750, 120, 700, 700);
		getContentPane().add(imagePanelRight);
		
		btnShow.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				loadImg();
				Graphics g = imagePanelLeft.getGraphics();
				g.drawImage(img, 0, 0, null);
				count=0;
				tfCount.setText(count + "");
			}
		});

		btnDilate.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				// 按一下 count +1
				count+=1;
				
				doDilate();
				
				tfCount.setText(count + "");
				
			}

		});

		btnErode.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				// 按一下 count -1
				count-=1;
				
				doErode();
				
				tfCount.setText(count + "");
			}
		});
		
		btnOpen.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				System.out.println("Start Opening");
				// Erode -> Dilate
				count=0;
				tfCount.setText(count + "");
				data  = oridata;
				int times = Integer.parseInt(tfOpenCount.getText());
				
				for(int i=0 ; i<times ; i++) {
					doErode();
				}
				for(int j=0 ; j<times ; j++) {
					doDilate();
				}
				System.out.println("End Opening");
			}
		});

		btnClose.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				System.out.println("Start Closing");
				// Dilate -> Erode 
				count=0;
				tfCount.setText(count + "");
				
				data  = oridata;
				int times = Integer.parseInt(tfCloseCount.getText());
				for(int i=0 ; i<times ; i++) {
					 doDilate();
				}
				for(int j=0 ; j<times ; j++) {
					doErode();
				}
				System.out.println("End Closing");
			}
		});

		btnReset.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				System.out.println("Start Reset");
				
				// count
				count=0;
				tfCount.setText(count + "");
				data = oridata;
				// img
				img = Util.makeImg(oridata);
				Graphics g = imagePanelRight.getGraphics();
				g.drawImage(img, 0, 0, null);
				System.out.println("End Reset");
			}
		});
	}
	
	
	void doDilate() {
		// labeled 確認沒有 重複dilate
		boolean label[][] = new boolean[height][width];
		
		for(int y=0 ; y<height; y++) {
			for(int x =0 ; x<width ; x++) {
				
				// == -1 = false
				boolean p;
				if(img.getRGB(x, y) == -1 ) {
					p = false; 
				}else {
					p = true;
				}
				
				boolean neighborB = false;
				
				for(int d=0; d<8;d++) {
					int dx = x+neighbor[d][0];
					int dy = y+neighbor[d][1];
					
					if(dx>=0 && dx<width && dy>=0 && dy<height) {
						if(img.getRGB(dx, dy)!= -1 && label[dy][dx]==false) {
							neighborB =  neighborB || true;
						}else {
							neighborB = neighborB || false;
						}
					}
				}
				// 進行if判斷
				// if 自身是白點，但鄰居有黑點
				if( p == false && neighborB==true ){
					data[y][x][0] = 0;
					data[y][x][1] = 0;
					data[y][x][2] = 0;
					label[y][x] = true;
				}	
			}
		}
		img = Util.makeImg(data);
		Graphics g = imagePanelRight.getGraphics();
		g.drawImage(img, 0, 0, null);
	}
	
	void doErode() {
		
		// labeled 確認沒有 重複erode
		boolean label[][] = new boolean[height][width];
		
		for(int y=0 ; y<height; y++) {
			for(int x =0 ; x<width ; x++) {
				
				// == -1 = white
				boolean p;
				if(img.getRGB(x, y) == -1 ) {
					p = true; 
				}else {
					p = false;
				}
				
				boolean neighborB = false;
				
				for(int d=0; d<8;d++) {
					int dx = x+neighbor[d][0];
					int dy = y+neighbor[d][1];
					
					if(dx>=0 && dx<width && dy>=0 && dy<height) {
						if(img.getRGB(dx, dy)== -1 && label[dy][dx]==false) {
							neighborB =  neighborB || true;
						}else {
							neighborB = neighborB || false;
						}
					}
				}
				// 進行if判斷
				// if 自身是黑點，但鄰居有白點
				if( p == false && neighborB==true ){
					data[y][x][0] = 255;
					data[y][x][1] = 255;
					data[y][x][2] = 255;
					label[y][x] = true;
				}	
			}
		}
		img = Util.makeImg(data);
		Graphics g = imagePanelRight.getGraphics();
		g.drawImage(img, 0, 0, null);
	}
	// 邊緣偵測
	void loadImg() {
		try {
			img = ImageIO.read(new File(filename));
		} catch (IOException e) {
			System.out.println("IO exception");
		}
		height = img.getHeight();
		width = img.getWidth();
		data = new int[height][width][3];
		oridata = new int[height][width][3];
		for (int y = 0; y < height; y++) {
			for (int x = 0; x < width; x++) {
				int rgb = img.getRGB(x, y);
				data[y][x][0] = Util.getR(rgb);
				data[y][x][1] = Util.getG(rgb);
				data[y][x][2] = Util.getB(rgb);
				oridata[y][x][0] = Util.getR(rgb);
				oridata[y][x][1] = Util.getG(rgb);
				oridata[y][x][2] = Util.getB(rgb);
			}
		}

	}

	public static void main(String[] args) {
		MorFrame frame = new MorFrame();
		frame.setSize(1500, 1500);
		frame.setVisible(true);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}
}

