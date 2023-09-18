package Hw2;

public class Util {
	// 矩陣乘法 ret(3*1) = a(3*3)*b(3*1)
	public final static double [] affine(double[][] a, double[] b){
		double[] ret= new double[a.length];	
		for(int y=0; y<a.length; y++) {
			for(int x=0 ; x<b.length; x++) {
				ret[y]+= (a[y][x]*b[x]); 
			}
		}
		return  ret;
	}
	final static int checkImageBound(int value, int bound){
		if (value > bound ) return bound;
		else return value;
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
	// start+( end-start )*weight
}