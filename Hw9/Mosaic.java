package Hw9;

import java.awt.Graphics;
import java.awt.GridLayout;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;




public class Mosaic extends JFrame {
	static Mosaic frame;
	int SIZE = 10;//size of the Mosaic block
	enum State {BEGIN, DRAG_ORG, ADJUSTABLE, POINT_SELECTED, FINISH_SELECTION};
	enum MouseState {PRESSED, RELEASED};
	State state;
	MouseState mState;
	int[][][] data;
	int[][][] newdata;
	int height;
	int width;
	BufferedImage img = null;
    Point[] originalPoints;
    boolean adjustable; 
   
	MosaicImagePanel imagePanelBef;
	MosaicImagePanel imagePanelAft;
	
	JButton btnShow;
	JButton btnCmd;
	BufferedImage imgBef = null;
	BufferedImage imgAft = null;
	
	Mosaic() {
	}
	
	Mosaic(File file){
		try {
			imgBef = ImageIO.read(file);
		} catch (IOException e) {
			System.out.println("IO exception");
		}
		height = imgBef.getHeight();
		width = imgBef.getWidth();
		data = new int[height][width][3];
		newdata = new int[height][width][3];
		for (int y = 0; y < height; y++) {
			for (int x = 0; x < width; x++) {
				int rgb = imgBef.getRGB(x, y);
				data[y][x][0] = Util.getR(rgb);
				data[y][x][1] = Util.getG(rgb);
				data[y][x][2] = Util.getB(rgb);
			}
		}
		for (int y = 0; y < height; y++) {
			for (int x = 0; x < width; x++) {
				int rgb = imgBef.getRGB(x, y);
				newdata[y][x][0] = Util.getR(rgb);
				newdata[y][x][1] = Util.getG(rgb);
				newdata[y][x][2] = Util.getB(rgb);
			}
		}
		getContentPane().setLayout(null);
		btnShow = new JButton("Show");
		btnShow.setBounds(20, 50, 100, 30);
		add(btnShow);
		
		btnCmd = new JButton("TBS");
		btnCmd.setBounds(150, 50, 100, 30);
		add(btnCmd);
		
		imagePanelBef = new MosaicImagePanel();
		imagePanelBef.setBounds(20, 100, 700, 700);
		getContentPane().add(imagePanelBef);
		imagePanelAft = new MosaicImagePanel();
		imagePanelAft.setBounds(720, 100, 700, 700);
		getContentPane().add(imagePanelAft);
		state = State.BEGIN;
		decorate();
		
		btnShow.addActionListener(new ActionListener(){ 
			public void actionPerformed(ActionEvent arg0) {
				Graphics g = imagePanelBef.getGraphics();
				imagePanelBef.paintComponent(g);
				g.drawImage(imgBef, 0, 0, null);
				MA ma = new MA();
				imagePanelBef.addMouseListener(ma);
				imagePanelBef.addMouseMotionListener(ma);
			}
	    });   
		    
	  
		btnCmd.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
			// 框框
				int left = originalPoints[0].x;
				int top = originalPoints[0].y;
				int right = originalPoints[1].x;
				int bottom = originalPoints[2].y;
				// 邊長%5 & 邊長//5 -> 
				// 起始y, 終點y
				for(int i=top; i<=bottom; i+=SIZE) {
					// 起始x, 終點x
					int count=0; // count 幾格
					int R_sum =0; // RGB sum 
					int G_sum =0;
					int B_sum =0;
					for(int j=left ; j<=right ; j+=SIZE) {
						// 每五個間隔抓一個定位點 -> 5*5 馬賽克方格的左上角 
						
						for(int b=0; b<SIZE ; b++) {
							for(int a=0; a<SIZE ; a++) {
								if((j+a<=right) && (i+b)<=bottom) {
									count+=1;
									R_sum+=data[i+b][j+a][0];
									G_sum+=data[i+b][j+a][1];
									B_sum+=data[i+b][j+a][2];
								}
							}
						}
						int newR = Util.checkPixelBounds((int)(R_sum/count));
						int newG = Util.checkPixelBounds((int)(G_sum/count));
						int newB = Util.checkPixelBounds((int)(B_sum/count));
						// 新的rgb 
						for(int b=0; b<SIZE ;b++) {
							for(int a=0 ; a<SIZE; a++) {
								if((j+a<=right) && (i+b)<=bottom) {
									newdata[i+b][j+a][0] = newR;
									newdata[i+b][j+a][1] = newG;
									newdata[i+b][j+a][2] = newB;
									count=0;
									R_sum =0; 
									G_sum =0;
									B_sum =0;
								}
							}
						}
						
					}
				}
				imgAft = Util.makeImg(newdata);
				Util.saveImg = imgAft;
				Graphics g = imagePanelAft.getGraphics();
				imagePanelAft.paintComponent(g);
				g.drawImage(imgAft, 0, 0, null);
			}
		});
	 
	 }//end of the constructor
	 // 得到上下左右四個數字 -makePoints-> 四個 Points
	 Point [] makePoints(int left, int top, int right, int bottom) {
		 Point [] points = new Point[4];
		 points[0] = new Point(left, top); // 左上
		 points[1] = new Point(right, top);	// 右上
		 points[2] = new Point(right, bottom);  // 右下
		 points[3] = new Point(left, bottom);	// 左下
        return points;
	 }
	 
	class MA extends MouseAdapter {
		boolean mousePressed;
		boolean mouseReleased;
		int startX;
		int startY;
		int endX;
		int endY;
		int dragPoint = -1;
		
	    public void mouseDragged(MouseEvent arg0) {
			//begin to draw a rectangle
			if (state == State.BEGIN) {
				startX = arg0.getX();
				startY = arg0.getY();
				state = State.DRAG_ORG;
				return;
			}
			int x = arg0.getX();
			int y = arg0.getY();

			//update original rectangle
			if (state == State.DRAG_ORG) {
				imagePanelBef.paintComponent(imagePanelBef.getGraphics(), startX, startY, x, y, imgBef, frame);
			}
			endX = x;
			endY = y;
		}

	    public void mousePressed(MouseEvent arg0) {
			mState = MouseState.PRESSED;
		}

		public void mouseReleased(MouseEvent arg0) {
			mState = MouseState.RELEASED;
			//finish the original rectangle
			if (state == State.DRAG_ORG) {
				originalPoints = makePoints(startX, startY, endX, endY);
				adjustable = true;
				state = State.ADJUSTABLE;
			}
		}
	}
	// image 
	void decorate() {
		setTitle(Util.cmdMosaic);
		btnCmd.setText(Util.cmdMosaic);
	}
	void create() {
		this.setSize(1500, 1500);
		this.setVisible(true);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame = new Mosaic();
//		frame.setSize(1500, 1500);
//		frame.setVisible(true);
//		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}
}
