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

import Hw9.Util;

public class Erode extends AbstractBasic {
	private static final long serialVersionUID = 1L;
	int [][] neighbor = {{0,1},{0,-1},{1,-1},{1,0},{1,1},{-1,-1},{-1,0},{-1,1}};
	Erode() {
	};

	Erode(File file) {
		super(file);
		imgAft = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
		imgAft = imgBef;
	}
	
	@Override
	void exe() {
		doErode();
	}

	@Override
	void decorate() {
		setTitle(Util.cmdErode);
		btnCmd.setText(Util.cmdErode);
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
}