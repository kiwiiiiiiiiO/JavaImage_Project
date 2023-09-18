package Hw2;

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
import javax.swing.JTextField;

public class AffineFrame_hw extends JFrame {
	JPanel cotrolPanelMain = new JPanel();
	JPanel cotrolPanelShow = new JPanel();;
	JPanel cotrolPanelBackColor = new JPanel();;
	JPanel cotrolPanelTranslate = new JPanel();;
	JPanel cotrolPanelScale = new JPanel();
	JPanel cotrolPanelRotate = new JPanel();
	JPanel cotrolPanelShear = new JPanel();;
	ImagePanel imagePanel;
	JButton btnShow;
	JButton btnTranslate;
	JButton btnScale;
	
	BufferedImage img;
	BufferedImage newImg;
	
	JTextField tfDeltaX = new JTextField(5);
	JTextField tfDeltaY = new JTextField(5);
	JTextField tfAmpX = new JTextField(5);
	JTextField tfAmpY = new JTextField(5);
	JLabel lbDeltaY = new JLabel("y軸位移");
	JLabel lbDeltaX = new JLabel("x軸位移");
	JLabel lbAmpX = new JLabel("x軸倍率");
	JLabel lbAmpY = new JLabel("y軸倍率");
	final int[][][] data;
	int height;
	int width;

	AffineFrame_hw() {
		setBounds(0, 0, 1500, 1500);
		getContentPane().setLayout(null);
		// 位移
		tfDeltaX.setText("0");
		tfDeltaY.setText("0");
		// 倍率
		tfAmpX.setText("1.0");
		tfAmpY.setText("1.0");
		setTitle("Affine Transform Homework");
		try {
			 img = ImageIO.read(new File("plate.png"));
		} catch (IOException e) {
			System.out.println("IO exception");
		}
		height = img.getHeight();
		width = img.getWidth();
		data = new int[height][width][3];
		System.out.println("width: "+width+" , height "+height);
		
		btnShow = new JButton("顯示");
		btnTranslate = new JButton("平移");
		btnScale = new JButton("放大/縮小");
		cotrolPanelMain = new JPanel();
		cotrolPanelMain.setLayout(new GridLayout(1, 7));
		cotrolPanelShow.add(btnShow);
		cotrolPanelMain.add(cotrolPanelShow);
		cotrolPanelMain.add(cotrolPanelBackColor);
		cotrolPanelTranslate.add(lbDeltaX);
		cotrolPanelTranslate.add(tfDeltaX);
		cotrolPanelTranslate.add(lbDeltaY);
		cotrolPanelTranslate.add(tfDeltaY);
		cotrolPanelTranslate.add(btnTranslate);
		cotrolPanelMain.add(cotrolPanelTranslate);
		cotrolPanelScale.add(lbAmpX);
		cotrolPanelScale.add(tfAmpX);
		cotrolPanelScale.add(lbAmpY);
		cotrolPanelScale.add(tfAmpY);
		cotrolPanelScale.add(btnScale);
		cotrolPanelMain.add(cotrolPanelScale);
		cotrolPanelMain.add(cotrolPanelRotate);
		cotrolPanelMain.add(cotrolPanelShear);
		cotrolPanelMain.add(new JPanel());
		cotrolPanelMain.add(new JPanel());
		cotrolPanelMain.add(new JPanel());
		cotrolPanelMain.setBounds(0, 0, 1200, 150);
		getContentPane().add(cotrolPanelMain);
		imagePanel = new ImagePanel();
		imagePanel.setBounds(0, 180, 1500, 1500);
		getContentPane().add(imagePanel);
		
		//show 
		btnShow.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				Graphics g = imagePanel.getGraphics(); // 畫布
				imagePanel.paintComponent(g); 	// 畫的內容
				g.drawImage(img,0,0, null); // 畫
			}
		});
		
		// 平移，使用 Affine Transform 方式將加法轉為乘法
		btnTranslate.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				double deltax = Double.parseDouble(tfDeltaX.getText());
				double deltay = Double.parseDouble(tfDeltaY.getText());
				double a [][]= {{1,0,deltax},{0,1,deltay},{0,0,1}};
				double b []= new double[3];
				b[0]=0;b[1]=0;b[2]=1;
				newImg = new BufferedImage((width+(int)deltay),(height+(int)deltax), BufferedImage.TYPE_INT_ARGB);
				double[] ret = Util.affine(a, b);
				Graphics g = imagePanel.getGraphics();
				imagePanel.paintComponent(g);
				g.drawImage(img, (int)(ret[0]), (int)(ret[1]), null);
			}
		});
		// x, y軸 放大、縮小
		btnScale.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				double ax = Double.parseDouble(tfAmpX.getText());
				double ay = Double.parseDouble(tfAmpY.getText());
				// 改變大小，並計算新img大小，新影像從舊影像取值
				double newImgWidth = width*(ax);
				double newImgHeight = height*(ay);
				newImg = new BufferedImage((int)newImgWidth, (int)newImgHeight, BufferedImage.TYPE_INT_ARGB);
				// 由新圖片往舊圖片mapping，因此使用S^-1
				// 處理座標，affine transform
				double a[][] = {{1/ax,0,0},{0,1/ay,0},{0,0,1}};
				
				for(int y=0; y<(int)newImgHeight; y++) {
					for(int x=0; x<(int)newImgWidth; x++) {
						double b[] = {x,y,1};
						// 對應原圖的 x,y
						double[] ret = Util.affine(a, b);
						// 依照比例原則分配RGB，使用bilinear
						// 有四點，並有alpha、beta兩weight
						int fX = Util.checkImageBound((int)Math.floor(ret[0]),width-1);
						int fY = Util.checkImageBound((int)Math.floor(ret[1]),height-1);
						int cX = Util.checkImageBound((int)Math.ceil(ret[0]),width-1);
						int cY = Util.checkImageBound((int)Math.ceil(ret[1]),height-1);
						double alpha = ret[0]-fX;
						double beta = ret[1]-fY;
						
						int leftTopRGB = img.getRGB(fX, fY);		
						int rightTopRGB = img.getRGB(cX, fY);		
						int leftBottomRGB = img.getRGB(fX, cY);		
						int rightBottomRGB = img.getRGB(cX, cY);	
						int rgb = Util.bilinear(leftTopRGB, rightTopRGB, leftBottomRGB, rightBottomRGB,alpha,beta);
			
						newImg.setRGB(x, y, rgb);
					}
				}
				Graphics g = imagePanel.getGraphics();
				imagePanel.paintComponent(g);
				g.drawImage(newImg, 0, 0, null);
			}
		});
	}
	public int bylinear(double[] ret) {
		int fX = Util.checkImageBound((int)Math.floor(ret[0]),width-1);
		int fY = Util.checkImageBound((int)Math.floor(ret[1]),height-1);
		int cX = Util.checkImageBound((int)Math.ceil(ret[0]),width-1);
		int cY = Util.checkImageBound((int)Math.ceil(ret[1]),height-1);
		double alpha = ret[0]-fX;
		double beta = ret[1]-fY;
		
		int leftTopRGB = img.getRGB(fX, fY);		
		int rightTopRGB = img.getRGB(cX, fY);		
		int leftBottomRGB = img.getRGB(fX, cY);		
		int rightBottomRGB = img.getRGB(cX, cY);	
		int rgb = Util.bilinear(leftTopRGB, rightTopRGB, leftBottomRGB, rightBottomRGB,alpha,beta);
		return rgb ;
	}
	
	public static void main(String[] args) {
		AffineFrame_hw frame = new AffineFrame_hw();
		frame.setSize(1500, 1500);
		frame.setVisible(true);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}
}

//bylinear 教學上
