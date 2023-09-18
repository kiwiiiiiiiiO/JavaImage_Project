package Hw1;
public class Util {
	final static int checkPixelBounds(int value){
		if (value > 255 ) return 255;
		else if (value < 0 ) return 0;
		else return value;
 	} 
	
	//get red channel from colorspace (4 bytes)
	final static int getR(int rgb){
		return checkPixelBounds((rgb & 0x00ff0000)>>>16); //右移 2 bytes
   }

	//get green channel from colorspace (4 bytes)
	final static int getG(int rgb){
		return checkPixelBounds((rgb & 0x0000ff00)>>>8); 
	}
	
	//get blue channel from colorspace (4 bytes)
	final static int getB(int rgb){
		return checkPixelBounds((rgb & 0x000000ff)); 
	}
	
	//make ARGB color format from R, G, and B channels 
	final static int makeColor(int r, int g, int b){
		return (255<<24 | r<<16 | g<<8 | b); // 左移 
	}
	
	final static int covertToGray(int r, int g, int b){
		return checkPixelBounds((int)(0.2126 * r + 0.7152 * g + 0.0722 * b));		
	}
}

