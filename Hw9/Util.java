package Hw9;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;

import Hw6.ImagePanel;


public class Util {
    static File selectedFile;
    static File saveFile;
    static BufferedImage saveImg;
    
    static final String cmdOpen = "Open";
    static final String cmdSave = "Save";
    static final String cmdSaveAs = "Save As";
    static final String cmdExit = "Exit";
    
    static final String cmdLR = "Left/Right";
    static final String cmdUD = "Up/Down";
    static final String cmdL90 = "Left 90";
    static final String cmdR90 = "Right 90";
    static final String cmdR180 = "Rotate 180";
    static final String cmdInverse = "Invese";
    static final String cmdGray = "Gray";
    static final String cmdTranslation = "Translation";
    static final String cmdScaling = "Scaling";
    static final String cmdRotation = "Rotation";
    static final String cmdHP = "High Pass";
    static final String cmdLP = "low Pass";
    static final String cmdErode = "Erode";
    static final String cmdDilate = "Dilate";
    static final String cmdNKNU = "NKNU";
    static final String cmdSEM = "SEM";
    static final String cmdMe = "JenniferOu";
    static final String cmdHSL = "HSL";
    static final String cmdSegment = "Segment";
    static final String cmdOpenOp = "Open";
    static final String cmdCloseOp = "Close";
 
 
    static final String cmdMosaic = "Mosaic";
    
   
    

    
	final static int checkPixelBounds(int value){
		if (value >255) return 255;
		if (value <0) return 0;
		return value;
 	} 
	
	//get red channel from colorspace (4 bytes)
	final static int getR(int rgb){
		  return checkPixelBounds((rgb & 0x00ff0000)>>>16);	
   }

	//get green channel from colorspace (4 bytes)
	final static int getG(int rgb){
	  return checkPixelBounds((rgb & 0x0000ff00)>>> 8);
	}
	
	//get blue channel from colorspace (4 bytes)
	final static int getB(int rgb){
		  return  checkPixelBounds(rgb & 0x000000ff);
	}
	
	final static int makeColor(int r, int g, int b){
		return (255<< 24 | r<<16 | g<<8 | b);
	}
	
	final static int covertToGray(int r, int g, int b){
		return checkPixelBounds((int) (0.2126 * r + 0.7152 * g + 0.0722 * b));		
	}

	final static int checkImageBound(int value, int bound){
		if (value > bound-1 ) return bound-1;
		else if (value <0 ) return 0;
		else return value;
 	} 
	
	final static int checkImageBounds(int value, int length){
		 if (value > length-1) return length-1;
		 else if (value < 0) return 0;
		 else return value;
	}
		
	final static void makeFile(int[][][] writedData, BufferedImage img, String filename) {
		for (int y = 0; y < img.getHeight(); y++) {
			for (int x = 0; x < img.getWidth(); x++) {
				int rgb = Util.makeColor(writedData[y][x][0], writedData[y][x][1], writedData[y][x][2]);
				img.setRGB(x, y, rgb);
			}
		}
		File outputfile = new File(filename);
		try {
			ImageIO.write(img, "png", outputfile);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	final static double [] affine(double[][] a, double[] b){
		double[] ret= new double[a.length];	
		for(int y=0; y<a.length; y++) {
			for(int x=0 ; x<b.length; x++) {
				ret[y]+= (a[y][x]*b[x]); 
			}
		}
		return  ret;
	}
	final static int bilinear(int leftTop, int rightTop, int leftBottom,  int rightBottom, double alpha, double beta){
		// leftTop=v(x,y)、rightTop=v(x+1,y)、righBottom=v(x,y+1)、leftBottom(x+1, y+1)
		// 呼叫三次 linear( double )！！！ -> 最後在轉型int -> check 
		double A = linear(leftTop, rightTop, alpha);
		double B = linear(leftBottom, rightBottom, alpha);
		double C = linear(A,B,beta);
		return (int)C;
	}
	// 線性差分		
	final static double linear(double v1, double v2, double weight) {
		return v1 + (v2-v1) * weight;
	}
	final static BufferedImage makeImg(int[][][] newData){
		int height = newData.length;
		int width =  newData[0].length;
		BufferedImage newImg = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB); 
        for (int y=0; y<height; y++) {
        	for (int x=0; x<width; x++) {
        		int rgb = Util.makeColor(newData[y][x][0],
        								 newData[y][x][1], 
        								 newData[y][x][2]);
        		newImg.setRGB(x, y, rgb);
        	}
        	
        }
        return newImg;
	}
	static void drawImg(ImagePanel panel, int[][][] newData) {
		BufferedImage img = makeImg(newData);
		drawImg(panel, img);
	}
	static void drawImg(ImagePanel panel, BufferedImage img) {
		Graphics g = panel.getGraphics();
		panel.paintComponent(g);
		g.drawImage(img, 0, 0, null);
	}

}