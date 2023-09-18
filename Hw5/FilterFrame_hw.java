package Hw5;


import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;

public class FilterFrame_hw extends JFrame {
	JPanel cotrolPanelMain = new JPanel();
	JPanel cotrolPanelShow = new JPanel();;
	JPanel cotrolPanelLP = new JPanel();
	JPanel cotrolPanelHP = new JPanel();
	ImagePanel imagePanel;
	ImagePanel imagePanel2;
	JButton btnShow;
	JButton btnLP = new JButton("Low-Pass(Blur)");
	JButton btnHP = new JButton("High-Pass(Sharp)");
	int[][][] data;
	int[][][] newData;
	int height;
	int width;
	BufferedImage img = null;
	BufferedImage imgNew = null;

	FilterFrame_hw() {
		setBounds(0, 0, 1500, 1500);
		getContentPane().setLayout(null);
		setTitle("HW 5: Image Filters 2023/04/06");
		try {
			img = ImageIO.read(new File("Munich.png"));
		} catch (IOException e) {
			System.out.println("IO exception");
		}
		btnShow = new JButton("Show");
		cotrolPanelMain = new JPanel();
		cotrolPanelMain.setBounds(0, 0, 1200, 200);
		getContentPane().add(cotrolPanelMain);
		cotrolPanelShow.add(btnShow);
		cotrolPanelShow.add(btnLP);
		cotrolPanelShow.add(btnHP);
		cotrolPanelMain.add(cotrolPanelShow);
		imagePanel = new ImagePanel();
		imagePanel.setBounds(20, 220, 700, 700);
		getContentPane().add(imagePanel);
		imagePanel2 = new ImagePanel();
		imagePanel2.setBounds(720, 220, 700, 700);
		getContentPane().add(imagePanel2);

		btnShow.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				try {
					img = ImageIO.read(new File("Munich.png"));
					width = img.getWidth();
					height = img.getHeight();
					Graphics g = imagePanel.getGraphics();  
					g.drawImage(img, 0, 0, null);  
					// get data 
					data = new int[height][width][3];
					newData = new int[height][width][3];
					for(int y=0;y<height;y++){
						for(int x=0;x<width;x++) {
							int rgb = img.getRGB(x, y);
							data[y][x][0] = Util.getR(rgb);
							data[y][x][1] = Util.getG(rgb);
							data[y][x][2] = Util.getB(rgb);
						}
					}
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		});

		btnLP.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				imgNew= new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB); 
				for(int y=0; y<height;y++){
					for(int x=0; x<width;x++) {
						for(int r = 0; r<3;r++) {
							newData[y][x][r] = Util.doLP( eTOpixelsRGB(x,y,r) );
						}
						// new rgb
						int newrgb = Util.makeColor( newData[y][x][0], newData[y][x][1], newData[y][x][2]);
						imgNew.setRGB(x, y, newrgb);
					}
				}
				Graphics g= imagePanel.getGraphics();
				g.drawImage(imgNew, 0, 0 , null);
			}
		});
		
		btnHP.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				imgNew= new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB); 
				for(int y=0; y<height;y++){
					for(int x=0; x<width;x++) {
						for(int r = 0; r<3;r++) {
							newData[y][x][r] = Util.checkPixelBounds(data[y][x][r]+Util.doHP( eTOpixelsRGB(x,y,r)));
						}
						// new rgb
						int newrgb = Util.makeColor( newData[y][x][0], newData[y][x][1], newData[y][x][2]);
						imgNew.setRGB(x, y, newrgb);
					}
				}
				Graphics g= imagePanel.getGraphics();
				g.drawImage(imgNew, 0, 0 , null);
			}
		});
	}
	
	// 傳入 e 值座標 -> 自動得出 pixelsRGB 之陣列 [] 
	public int[] eTOpixelsRGB(int x, int y, int rgb) {
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
	
	/* 
	 -1-1  0-1 -1-1 
	 -10   00   10
	 -11   01   11
	 */
	public static void main(String[] args) {
		FilterFrame_hw frame = new FilterFrame_hw();
		frame.setSize(1500, 1500);
		frame.setVisible(true);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}
}
