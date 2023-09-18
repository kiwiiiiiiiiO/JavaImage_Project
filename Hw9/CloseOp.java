package Hw9;

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
import javax.swing.JTextField;

import Hw9.Util;

public class CloseOp extends AbstractBasic {
	private static final long serialVersionUID = 1L;
	JLabel lbCloseCount;
	JTextField tfCloseCount;
	int [][] neighbor = {{0,1},{0,-1},{1,-1},{1,0},{1,1},{-1,-1},{-1,0},{-1,1}};
	CloseOp() {
	};

	CloseOp(File file) {
		super(file);
		lbCloseCount = new JLabel("Times (Close)");
		tfCloseCount = new JTextField(5);
		imgAft = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
		imgAft = imgBef;
		tfCloseCount.setText("0");
		lbCloseCount.setBounds(280, 50, 100, 30);
		add( lbCloseCount);
		tfCloseCount.setBounds(410, 50, 100, 30);
		add( tfCloseCount);
		tfCloseCount.setText("0");
	}
	
	@Override
	void exe() {
		System.out.println("Start Closing");
	
		int times = Integer.parseInt(tfCloseCount.getText());
		
		for(int i=0 ; i<times ; i++) {
			doDilate();
		}
		for(int j=0 ; j<times ; j++) {
			doErode();
		}
		System.out.println("End Closing");
		
	}
	
	void doDilate() {
		// labeled 確認沒有 重複dilate
		boolean label[][] = new boolean[height][width];
		
		for(int y=0 ; y<height; y++) {
			for(int x =0 ; x<width ; x++) {
				
				// == -1 = false
				boolean p;
				if(imgAft.getRGB(x, y) == -1 ) {
					p = false; 
				}else {
					p = true;
				}
				
				boolean neighborB = false;
				
				for(int d=0; d<8;d++) {
					int dx = x+neighbor[d][0];
					int dy = y+neighbor[d][1];
					
					if(dx>=0 && dx<width && dy>=0 && dy<height) {
						if(imgAft.getRGB(dx, dy)!= -1 && label[dy][dx]==false) {
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
		imgAft = Util.makeImg(data);
		Util.saveImg = imgAft;
		Graphics g = imagePanelAft.getGraphics();
		imagePanelAft.paintComponent(g);
		g.drawImage(imgAft, 0, 0, null);
	}
	void doErode() {
		// labeled 確認沒有 重複erode
		boolean label[][] = new boolean[height][width];
		
		for(int y=0 ; y<height; y++) {
			for(int x =0 ; x<width ; x++) {
				
				// == -1 = white
				boolean p;
				if(imgAft.getRGB(x, y) == -1 ) {
					p = true; 
				}else {
					p = false;
				}
				
				boolean neighborB = false;
				
				for(int d=0; d<8;d++) {
					int dx = x+neighbor[d][0];
					int dy = y+neighbor[d][1];
					
					if(dx>=0 && dx<width && dy>=0 && dy<height) {
						if(imgAft.getRGB(dx, dy)== -1 && label[dy][dx]==false) {
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
		imgAft = Util.makeImg(data);
		Util.saveImg = imgAft;
		Graphics g = imagePanelAft.getGraphics();
		imagePanelAft.paintComponent(g);
		g.drawImage(imgAft, 0, 0, null);
	}
	@Override
	void decorate() {
		setTitle(Util.cmdCloseOp);
		btnCmd.setText(Util.cmdCloseOp);
	}
}