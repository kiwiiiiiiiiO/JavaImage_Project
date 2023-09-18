package Hw4;

public class Util {
//	static int[][][] doHSL(){
////		return new {1,2,3};
//	}
	static double[] RGBLtoHSL(double r, double g, double b) {
		double h; double s; double l;
		double[] HSL = new double[3];
		r = r/255.0; g = g/255.0; b = b/255.0;
		double Cmax = Math.max( Math.max(r,g),b);
		double Cmin = Math.min( Math.min(r,g),b);
		double delta = Cmax - Cmin ;
		// 計算h
		if (Cmax == Cmin) {
			h =0;
		}else if (Cmax == r) {
			h = 60*( ((g-b)/delta)%6 );
		}else if (Cmax == g) {
			h = 60*( ((b-r)/delta)+2 );
		}else if (Cmax == b) {
			h = 60*( ((r-g)/delta)+4 );
		}else{
			h=0;
			System.out.println(" RGBLtoHSL Wrong");
		}
		// 計算l
		l = (Cmax + Cmin)*0.5;
		// 計算s
		if (Cmax == Cmin) {
			s = 0;
		}else if (l>0 && l<=0.5) {
			s = delta/ (2*l);
		}else if (l>0.5){
			s =  delta/(2-2*l);
		}else {
			s = 1;
			System.out.println("here Worng");
		}
		HSL[0] = h; HSL[1]= s; HSL[2] = l;
		return Util.checkHSL(HSL);
	}
	static int[] HSLtoRGB(double h, double s,double l) {
//		System.out.println(h+" "+s+" "+l+" ");
		double r; double g; double b;
		int[] RGB = new int[3];
		double c = (1 - Math.abs(2*l -1))*s;
		double x = c*(1 - Math.abs( (h/60)%2-1 ));
		double m = l-c/2;
		if (h >= 0 && h < 60) {
		    r = c; g = x; b = 0;
		} else if (h >= 60 && h < 120) {
		    r = x; g = c; b = 0;
		} else if (h >= 120 && h < 180) {
		    r = 0; g = c; b = x;
		} else if (h >= 180 && h < 240) {
		    r = 0; g = x; b = c;
		} else if (h >= 240 && h < 300) {
		    r = x; g = 0; b = c;
		} else if (h >= 300 && h < 360) {
		    r = c; g = 0; b = x;
		}else {
			r=0; g=0; b=0;
			System.out.println(" HSLtoRGB Wrong");
		}
		r = Util.checkPixelBounds((int)(Math.round((r + m) * 255)));
		g = Util.checkPixelBounds((int)(Math.round((g + m) * 255)));
		b = Util.checkPixelBounds((int)(Math.round((b + m) * 255)));
		RGB [0]= (int)r; RGB[1]= (int)g ; RGB[2]=(int)b;
		return RGB;
	}
	
	public static double [] checkHSL(double [] HSL){
		double h =HSL[0]; double s = HSL[1]; double l = HSL[2];
//		 check h
		if (h<0.0) {
			h+=360;
		}else if (h>360.0) {
			h-=360;
		}
		// check s
		if (s < 0.0) {
			s += 1;
		}else if ( s>1.0 ) {
			s  -= 1;
		}
		// check l
		if (l < 0.0) {
			l +=1.0;
		}else if (l > 1.0) {
			l -= 1.0 ;
		}
		HSL[0] = h; HSL[1]= s; HSL[2] = l;
		return HSL;
 	} 
	
	public final static int checkPixelBounds(int value){
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
	
}
