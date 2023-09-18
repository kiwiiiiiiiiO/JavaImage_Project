package Hw5;

public class Util {
	// currentPos = e 位置
	// pixelsRGB [] = {a,b,c,d,e,f,d,h,i}，return e' 
	public static int doLP(int [] pixelsRGB) {
		double e = 0;
		for(int i=0; i<9 ;i++) {
			e += pixelsRGB[i]/9;
		}
		return checkPixelBounds((int)e); 
	}
	public static int doHP(int [] pixelsRGB) {
		int e = 0;
		for(int i =0; i<9 ;i++) {
			if(i==4) 
				e+=8*pixelsRGB[i]/9;
			else
				e-=pixelsRGB[i]/9;
		}
		return checkPixelBounds(e);
	}
	
	static public int checkPixelBounds(int value) {
		if (value >255) return 255;
		if (value <0) return 0;
		return value;
	}

	public final static int checkImageBound(int value, int bound){
		if (value > bound-1 ) return bound-1;
		else if (value <0 ) return 0;
		else return value;
 	} 
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
}
