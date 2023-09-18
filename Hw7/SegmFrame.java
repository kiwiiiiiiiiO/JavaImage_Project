package Hw7;

import java.awt.Graphics;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;

import java.util.LinkedList;
import java.util.Queue;

public class SegmFrame extends JFrame {
	String filename = "segm_2.png";
	String title = "Segmentation (Choose one of the three methods)";
	JPanel cotrolPanel;
	JPanel imagePanelLeft;
	JPanel imagePanelRight;
	JButton btnShow;
	JButton btnSegm;
	JButton btnNext;
	JButton btnPrev;
	int[][][] data;
	int [][][] newdata;
	int height;
	int width;
	boolean[][]  visited;
	int[][]  label;
	static BufferedImage img = null;
	static BufferedImage newimg = null;
	int l;
	 
	int click=1;
	SegmFrame() {
		setTitle(title);
		setLayout(null);
		btnShow = new JButton("Show Original Image");
		btnSegm = new JButton("Segment");
		btnNext = new JButton("Next Object");
		btnPrev = new JButton("Prev Object");
		cotrolPanel = new JPanel();
		cotrolPanel.setBounds(0, 0, 1500, 200);
		getContentPane().add(cotrolPanel);
		cotrolPanel.add(btnShow);
		cotrolPanel.add(btnSegm);
		cotrolPanel.add(btnNext);
		cotrolPanel.add(btnPrev);
		
		imagePanelLeft = new ImagePanel();
		imagePanelLeft.setBounds(0, 120, 700, 700);
		getContentPane().add(imagePanelLeft);
		imagePanelRight = new ImagePanel();
		imagePanelRight.setBounds(750, 120, 700, 700);
		getContentPane().add(imagePanelRight);
		
		btnShow.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				loadImg();
				Graphics g = imagePanelLeft.getGraphics();
				g.drawImage(img, 0, 0, null);
			}
		});
		
		btnSegm.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {  
				l =1;
				for(int i=0; i<height ; i++) {
					for(int j=0 ; j<width ; j++) {
						
						if(img.getRGB(j, i)!= -1 && visited[i][j]==false) {
							BFS(new Point(j,i),l);
							l++;
						}
						
					}
				}
				for(int i=0; i<height ; i++) {
					for(int j=0 ; j<width ; j++) {
						if ( label[i][j] == 1) {
							newdata[i][j][0] = 0;
							newdata[i][j][1] = 0;
							newdata[i][j][2] = 0;
						}
					}
				}
				newimg = Util.makeImg(newdata);
				Graphics g = imagePanelRight.getGraphics();
				g.drawImage(newimg, 0, 0, null);
			}
		});

		btnNext.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				
				click++;
				for (int y = 0; y < height; y++) {
					for (int x = 0; x < width; x++) {
						newdata[y][x][0] = 255;
						newdata[y][x][1] = 255;
						newdata[y][x][2] = 255;
					}
				}
				for(int i=0; i<height ; i++) {
					for(int j=0 ; j<width ; j++) {
						if ( label[i][j] == click) {
							newdata[i][j][0] = 0;
							newdata[i][j][1] = 0;
							newdata[i][j][2] = 0;
						}
					}
				}
				newimg = Util.makeImg(newdata);
				Graphics g = imagePanelRight.getGraphics();
				g.drawImage(newimg, 0, 0, null);
			}
		});

		btnPrev.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				click--;
				for (int y = 0; y < height; y++) {
					for (int x = 0; x < width; x++) {
						newdata[y][x][0] = 255;
						newdata[y][x][1] = 255;
						newdata[y][x][2] = 255;
					}
				}
				for(int i=0; i<height ; i++) {
					for(int j=0 ; j<width ; j++) {
						if ( label[i][j] == click) {
							newdata[i][j][0] = 0;
							newdata[i][j][1] = 0;
							newdata[i][j][2] = 0;
						}
					}
				}
				newimg = Util.makeImg(newdata);
				Graphics g = imagePanelRight.getGraphics();
				g.drawImage(newimg, 0, 0, null);
			}
		});
	}
	void BFS(Point point, int l) {
		// 
		int [][] neighbor = {{0,1},{0,-1},{1,-1},{1,0},{1,1},{-1,1},{-1,0},{-1,1}};
		Queue<Point> q = new LinkedList<>();
		// 起始點
		q.offer(point);
		visited[point.y][point.x] = true;
		
		while(!q.isEmpty()) {
			point = q.poll();
			//  新增 point 
			for(int d=0 ; d<8 ; d++) {
				int dx = point.x+neighbor[d][0];
				int dy = point.y+neighbor[d][1];
				if(dx>=0 && dx<width && dy>=0 && dy<height && img.getRGB(dx, dy)!=-1 && visited[dy][dx]==false) {
					 q.offer(new Point(dx,dy));
					 visited[dy][dx] = true;
					 System.out.println( visited[dy][dx]);
					 label[dy][dx] = l;
					 System.out.println( label[dy][dx] );
				}
			}
			if(q.isEmpty()) break ;
		}
		System.out.println("break BFS");
	}
	void loadImg() {
		try {
			img = ImageIO.read(new File(filename));
		} catch (IOException e) {
			System.out.println("IO exception");
		}
		height = img.getHeight();
		width = img.getWidth();
		visited = new boolean[height][width];
		for (int y = 0; y < height; y++) {
			for (int x = 0; x < width; x++) {
				visited[y][x] = false;
			}
		}
		data = new int[height][width][3];
		label = new int[height][width];
		for (int y = 0; y < height; y++) {
			for (int x = 0; x < width; x++) {
				int rgb = img.getRGB(x, y);
				data[y][x][0] = Util.getR(rgb);
				data[y][x][1] = Util.getG(rgb);
				data[y][x][2] = Util.getB(rgb);
			}
		}
		newdata = new int[height][width][3];
		for (int y = 0; y < height; y++) {
			for (int x = 0; x < width; x++) {
				newdata[y][x][0] = 255;
				newdata[y][x][1] = 255;
				newdata[y][x][2] = 255;
			}
		}
	}
	
	public static void main(String[] args) {
		SegmFrame frame = new SegmFrame();
		frame.setSize(1500, 1500);
		frame.setVisible(true);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}
}
