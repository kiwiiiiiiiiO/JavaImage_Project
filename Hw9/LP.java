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

public class LP extends AbstractBasic {
	private static final long serialVersionUID = 1L;

	LP() {
	};

	LP(File file) {
		super(file);
	}
	
	@Override
	void exe() {
		imgAft = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
		
		for(int y=0; y<height;y++){
			for(int x=0; x<width;x++) {
				for(int r = 0; r<3;r++) {
					newdata[y][x][r] = doLP( eTOpixelsRGB(x,y,r) );
				}
				// new rgb
				int newrgb = Util.makeColor( newdata[y][x][0], newdata[y][x][1], newdata[y][x][2]);
				imgAft.setRGB(x, y, newrgb);
			}
		}
		
		Util.saveImg = imgAft;
		Graphics g = imagePanelAft.getGraphics();
		imagePanelAft.paintComponent(g);
		g.drawImage(imgAft, 0, 0, null);
		
	}
	int[] eTOpixelsRGB(int x, int y, int rgb) {
		int []ret = new int[9];
		int count=0;
		for(int i=0;i<3;i++) { 	// y
			for(int j=0; j<3; j++) { //x
				ret[count]=data[ Util.checkImageBound(y-1+i, height)][ Util.checkImageBound(x-1+j, width)][rgb];
				count+=1;
			}
		}
		return ret;
	}
	 int doLP(int [] pixelsRGB) {
			double e = 0;
			for(int i=0; i<9 ;i++) {
				e += pixelsRGB[i]/9;
			}
			return Util.checkPixelBounds((int)e); 
	}
	@Override
	void decorate() {
		setTitle(Util.cmdLP);
		btnCmd.setText(Util.cmdLP);
	}
	
}