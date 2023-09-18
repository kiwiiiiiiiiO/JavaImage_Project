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
import javax.swing.JSlider;
import javax.swing.JTextField;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import Hw9.Util;

public class HSL extends AbstractBasic {
	private static final long serialVersionUID = 1L;
	JSlider sliderHue;
	JSlider sliderSat;
	JSlider sliderLum;
	HSL() {
	};

	HSL(File file) {
		super(file);
		sliderHue = new JSlider(-180, 180, 0);
		sliderHue.setBounds(400, 50, 200, 10);
		add(sliderHue);
		sliderSat = new JSlider(-100, 100, 0);
		sliderSat.setBounds(700, 50, 200, 10);
		add(sliderSat);
		sliderLum = new JSlider(-100, 100, 0);
		sliderLum.setBounds(1000, 50, 200, 10);
		add(sliderLum);
		
		JLabel lbHue = new JLabel("Hue:");
		lbHue.setBounds(400, 5, 80, 30);
		add(lbHue);
		JLabel lbSat = new JLabel("Saturation:");
		lbSat.setBounds(700, 5, 80, 30);
		add(lbSat);
		JLabel lbLum  = new JLabel("Luminance:");
		lbLum.setBounds(1000, 5, 80, 30);
		add(lbLum);
		
		JLabel lbHueBeging = new JLabel("-180");
		lbHueBeging.setBounds(385, 50, 80,30 );
		add( lbHueBeging);
		JLabel lbHueEnd = new JLabel("180");
		lbHueEnd.setBounds(585, 50, 80, 30);
		add(lbHueEnd);
		JLabel lbSatBeging = new JLabel("-100(%)") ;
		lbSatBeging.setBounds(685, 50, 80, 30);
		add(lbSatBeging);
		JLabel lbSatEnd =  new JLabel("100(%)") ;;
		lbSatEnd.setBounds(885, 50, 80, 30);
		add(lbSatEnd);
		JLabel lbLumBeging  = new JLabel("-100(%)") ;
		lbLumBeging.setBounds(985, 50, 80, 30);
		add( lbLumBeging);
		JLabel lbLumEnd  = new JLabel("100(%)") ;
		lbLumEnd.setBounds(1185, 50, 80, 30);
		add(lbLumEnd);
		
		JTextField tfHueValue = new JTextField(3);
		tfHueValue.setBounds(490, 5, 100, 30);
		add( tfHueValue );
		JTextField tfSatValue = new JTextField(3);
		tfSatValue.setBounds(790, 5, 100, 30);
		add(tfSatValue );
		JTextField tfLumValue = new JTextField(3);
		tfLumValue.setBounds(1090, 5, 80, 30);
		add(tfLumValue);
		
		tfHueValue.setText("0");
		tfSatValue.setText("0");
		tfLumValue.setText("0");
		tfHueValue.setEditable(false);
		tfSatValue.setEditable(false);
		tfLumValue.setEditable(false);
		
		// 改變 input 值
		sliderHue.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent e) {
				tfHueValue.setText(""+((JSlider)e.getSource()).getValue());
			}
		});

		sliderSat.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent e) {
				tfSatValue.setText(""+((JSlider)e.getSource()).getValue());
			}
		});

		sliderLum.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent e) {
				tfLumValue.setText(""+((JSlider)e.getSource()).getValue());
			}
		});
	}
	
	@Override
	void exe() {
		double hSet = sliderHue.getValue();
		double sSet = sliderSat.getValue();
		double lSet = sliderLum.getValue();
		imgAft = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB); 
		data = new int[height][width][3];
		for(int y=0;y<height;y++){
			for(int x=0;x<width;x++) {
				int rgb = imgBef.getRGB(x, y); //  原來的rgb 
				data[y][x][0] = Util.getR(rgb); data[y][x][1] = Util.getG(rgb); data[y][x][2] = Util.getB(rgb);
				// rgb 轉換成hsl
				double HSL[] = RGBLtoHSL(data[y][x][0], data[y][x][1], data[y][x][2]);
				// 進行 set ，並 check 該hsl 位於正常範圍內
				double h = HSL[0]; double s = HSL[1]; double l = HSL[2];
				h+=hSet; 
				s+=(sSet*0.01); 
				l+=(lSet*0.01); 
				if (s<0) s=0;
				else if (s>1.0) s=1;
				if (l<0) l=0;
				else if (l>1.0) l=1;
				HSL[0]=h; HSL[1] = s; HSL[2] = l;
				HSL = checkHSL(HSL);
				// hsl 轉成rgb ，check 該rgb 位於正常範圍內
				int [] RGB = HSLtoRGB(HSL[0], HSL[1], HSL[2]);
				rgb = Util.makeColor(RGB[0], RGB[1], RGB[2]);
				imgAft.setRGB(x,y,rgb);
			}
		}
		Util.saveImg = imgAft;
		Graphics g = imagePanelAft.getGraphics();
		imagePanelAft.paintComponent(g);
		g.drawImage(imgAft, 0, 0, null);
		
	}
	
	
	@Override
	void decorate() {
		setTitle(Util.cmdHSL);
		btnCmd.setText(Util.cmdHSL);
	}
	
	double[] RGBLtoHSL(double r, double g, double b) {
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
		return checkHSL(HSL);
	}
	int[] HSLtoRGB(double h, double s,double l) {
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
	double [] checkHSL(double [] HSL){
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
}