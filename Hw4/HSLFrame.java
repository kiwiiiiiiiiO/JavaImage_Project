package Hw4;

import java.awt.Graphics;
import java.awt.GridLayout;
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
import javax.swing.JSlider;
import javax.swing.JTextField;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

public class HSLFrame  extends JFrame {
	JPanel cotrolPanelMain = new JPanel();
	JPanel cotrolPanelShow = new JPanel();;
	JPanel cotrolPanelHue = new JPanel();
	JPanel cotrolPanelSat = new JPanel();
	JPanel cotrolPanelLum = new JPanel();
	JPanel cotrolPanelBin = new JPanel();
	JPanel cotrolPanelHSL = new JPanel();
	JPanel imagePanelOrg;   
	JPanel imagePanelHSL;   
	JButton btnShow;
	JSlider sliderHue;
	JSlider sliderSat;
	JSlider sliderLum;
	JLabel lbHue = new JLabel("    Hue");
	JLabel lbSat = new JLabel("    Saturation");
	JLabel lbLum = new JLabel("    Luminance");
	JLabel lbHueBeging = new JLabel("-180");
	JLabel lbHueEnd = new JLabel("180");
	JLabel lbSatBeging = new JLabel("-100(%)") ;
	JLabel lbSatEnd =  new JLabel("100(%)") ;;
	JLabel lbLumBeging  = new JLabel("-100(%)") ;;
	JLabel lbLumEnd  = new JLabel("100(%)") ;;
	JTextField tfHueValue = new JTextField(3);
	JTextField tfSatValue = new JTextField(3);
	JTextField tfLumValue = new JTextField(3);
	int[][][] data;
	int height;
	int width;
	static BufferedImage imgOrg = null;  
	static BufferedImage imgHSL = null; 
	
	HSLFrame() {
		setBounds(0, 0, 1500, 1500);
		getContentPane().setLayout(null);
		setTitle("HW4: HSL Coversion");
		btnShow = new JButton("Show Original Image");
		tfHueValue.setText("0");
		tfSatValue.setText("0");
		tfLumValue.setText("0");
		tfHueValue.setEditable(false);
		tfSatValue.setEditable(false);
		tfLumValue.setEditable(false);
		
		cotrolPanelMain = new JPanel();
		cotrolPanelMain.setLayout(new GridLayout(6, 1));
		sliderHue = new JSlider(-180, 180, 0);
		cotrolPanelShow.add(btnShow);
		cotrolPanelHue.add(lbHueBeging);
		cotrolPanelHue.add(sliderHue);
		cotrolPanelHue.add(lbHueEnd);
		cotrolPanelHue.add(tfHueValue);
		cotrolPanelHue.add(lbHue);
		
		sliderSat = new JSlider(-100, 100, 0);
		cotrolPanelSat.add(lbSatBeging);
		cotrolPanelSat.add(sliderSat);
		cotrolPanelSat.add(lbSatEnd);
		cotrolPanelSat.add(tfSatValue);
		cotrolPanelSat.add(lbSat);
		
		sliderLum = new JSlider(-100, 100, 0);
		cotrolPanelLum.add(lbLumBeging);
		cotrolPanelLum.add(sliderLum);
		cotrolPanelLum.add(lbLumEnd);
		cotrolPanelLum.add(tfLumValue);
		cotrolPanelLum.add(lbLum);
		cotrolPanelMain.add(cotrolPanelShow);
		cotrolPanelMain.add(cotrolPanelHue);
		cotrolPanelMain.add(cotrolPanelSat);
		cotrolPanelMain.add(cotrolPanelLum);
		cotrolPanelMain.add(cotrolPanelHSL);
		cotrolPanelMain.add(cotrolPanelBin);
		cotrolPanelMain.setBounds(0, 0, 1200, 200);
		getContentPane().add(cotrolPanelMain);
		imagePanelOrg = new ImagePanel();   
		imagePanelOrg.setBounds(0, 220, 700, 700);  
		getContentPane().add(imagePanelOrg);  
		imagePanelHSL = new ImagePanel();  
		imagePanelHSL.setBounds(750, 220, 700, 700);  
		getContentPane().add(imagePanelHSL);  

		btnShow.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				loadImg();
				Graphics g = imagePanelOrg.getGraphics();  
				g.drawImage(imgOrg, 0, 0, null);  
			}
		});
	    
		sliderHue.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent e) {
				tfHueValue.setText(""+((JSlider)e.getSource()).getValue());
				// 改變hue
				convert();
			}
		});

		sliderSat.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent e) {
				tfSatValue.setText(""+((JSlider)e.getSource()).getValue());
				// 改變sat
				convert();
			}
		});

		sliderLum.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent e) {
				tfLumValue.setText(""+((JSlider)e.getSource()).getValue());
				convert();
			}
		});
	}
	void loadImg() {
		try {
			imgOrg = ImageIO.read(new File("Munich.png"));
			width = imgOrg.getWidth();
			height = imgOrg.getHeight();
		} catch (IOException e) {
			System.out.println("IO exception");
		}
	}
	// get rgb
	// 新rgb
	void convert() {
		double hSet = sliderHue.getValue();
		double sSet = sliderSat.getValue();
		double lSet = sliderLum.getValue();
		imgHSL = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB); 
		data = new int[height][width][3];
		for(int y=0;y<height;y++){
			for(int x=0;x<width;x++) {
				int rgb = imgOrg.getRGB(x, y); //  原來的rgb 
				data[y][x][0] = Util.getR(rgb); data[y][x][1] = Util.getG(rgb); data[y][x][2] = Util.getB(rgb);
				// rgb 轉換成hsl
				double HSL[] = Util.RGBLtoHSL(data[y][x][0], data[y][x][1], data[y][x][2]);
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
				HSL = Util.checkHSL(HSL);
				// hsl 轉成rgb ，check 該rgb 位於正常範圍內
				int [] RGB = Util.HSLtoRGB(HSL[0], HSL[1], HSL[2]);
				rgb = Util.makeColor(RGB[0], RGB[1], RGB[2]);
				imgHSL.setRGB(x,y,rgb);
			}
		}
		// 新影像
		Graphics g= imagePanelHSL.getGraphics();
		g.drawImage(imgHSL, 0,0 , null);
	}
	public static void main(String[] args) {
		HSLFrame frame = new HSLFrame();
		frame.setSize(1500, 1500);
		frame.setVisible(true);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}
}
