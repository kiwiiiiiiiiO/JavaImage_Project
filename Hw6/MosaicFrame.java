package Hw6;

import java.awt.GridLayout;
// Point(int x, int y)
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

public class MosaicFrame extends JFrame {
	private static final long serialVersionUID = 1L;
	String filename = "f16.png";
	String title = "Home Work: Mosaic Processing";
	static MosaicFrame frame;
	int SIZE = 10;//size of the Mosaic block
	enum State {BEGIN, DRAG_ORG, ADJUSTABLE, POINT_SELECTED, FINISH_SELECTION};
	enum MouseState {PRESSED, RELEASED};
	State state;
	MouseState mState;
	JPanel cotrolPanelMain = new JPanel();
	JPanel cotrolPanelShow = new JPanel();;
	ImagePanel imagePanel;
	ImagePanel imagePanel2;
	JButton btnShow = new JButton("Show Original Image");
	JButton btnMosaic = new JButton("Show Mosaic Image");
	int[][][] data;
	int[][][] newData;
	int height;
	int width;
	BufferedImage img = null;
    Point[] originalPoints;
    boolean adjustable; 
	
	 MosaicFrame(){
		setBounds(0, 0, 1500, 1500);
		getContentPane().setLayout(null);
	 	setTitle(title);
		cotrolPanelMain = new JPanel();
		cotrolPanelMain.setLayout(new GridLayout(6,1));
		cotrolPanelShow.add(btnShow);
		cotrolPanelShow.add(btnMosaic);
		cotrolPanelMain.add(cotrolPanelShow);
		cotrolPanelMain.setBounds(0, 0,1200,200);
		getContentPane().add(cotrolPanelMain);
		state = State.BEGIN;
	    
		btnShow.addActionListener(new ActionListener(){ 
			public void actionPerformed(ActionEvent arg0) {
				loadImg();
				Util.drawImg(imagePanel, img);
			}
	    });   
		    
	  
		btnMosaic.addActionListener(new ActionListener() {
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
									newData[i+b][j+a][0] = newR;
									newData[i+b][j+a][1] = newG;
									newData[i+b][j+a][2] = newB;
									count=0;
									R_sum =0; 
									G_sum =0;
									B_sum =0;
								}
							}
						}
						
					}
				}
			Util.drawImg(imagePanel2, newData);
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
	 void loadImg(){
		 try {
				img = ImageIO.read(new File(filename));
			} catch (IOException e) {
				System.out.println("IO exception");
			}
			
			height = img.getHeight();
			width = img.getWidth();
			data = new int[height][width][3];
			newData = new int[height][width][3];
			for (int y=0; y<height; y++){
		    	for (int x=0; x<width; x++){
		    		int rgb = img.getRGB(x, y);
		    		data[y][x][0] = newData[y][x][0] = Util.getR(rgb);
		    		data[y][x][1] = newData[y][x][1] = Util.getG(rgb);
		    		data[y][x][2] = newData[y][x][2] = Util.getB(rgb);
		    	}
		    }
			imagePanel = new ImagePanel();
		    imagePanel.setBounds(20,220, width,height);
		    getContentPane().add(imagePanel);
		    imagePanel2 = new ImagePanel();
		    imagePanel2.setBounds(750,220, width,height);
		    getContentPane().add(imagePanel2);
		    MA ma = new MA();
			imagePanel.addMouseListener(ma);
			imagePanel.addMouseMotionListener(ma);
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
				imagePanel.paintComponent(imagePanel.getGraphics(), startX, startY, x, y, img, frame);
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

	public static void main(String[] args) {
		frame = new MosaicFrame();
		frame.setSize(1500, 1500);
		frame.setVisible(true);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}
}
	 