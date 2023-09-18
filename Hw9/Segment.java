package Hw9;

import java.awt.Graphics;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.LinkedList;
import java.util.Queue;

import javax.imageio.ImageIO;
import javax.swing.JButton;
import javax.swing.JFrame;

import Hw9.Util;

public class Segment extends AbstractBasic {
	private static final long serialVersionUID = 1L;
	JButton btnNext;
	JButton btnPrev;
	boolean[][]  visited;
	int[][]  label;
	int l=1; 
	int click=1;
	
	Segment() {
	};

	Segment(File file) {
		super(file);
		//
		btnNext = new JButton("Next Object");
		btnNext.setBounds(280, 50, 100, 30);
		btnPrev = new JButton("Prev Object");
		btnPrev.setBounds(410, 50, 100, 30);
		add(btnNext);
		btnNext.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				next();
		}});
		btnPrev.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				prev();
		}});
		add(btnPrev);
		//
		label = new int[height][width];
		for (int y = 0; y < height; y++) {
			for (int x = 0; x < width; x++) {
				int rgb = imgBef.getRGB(x, y);
				data[y][x][0] = Util.getR(rgb);
				data[y][x][1] = Util.getG(rgb);
				data[y][x][2] = Util.getB(rgb);
			}
		}
		visited = new boolean[height][width];
		for (int y = 0; y < height; y++) {
			for (int x = 0; x < width; x++) {
				visited[y][x] = false;
			}
		}
		for (int y = 0; y < height; y++) {
			for (int x = 0; x < width; x++) {
				newdata[y][x][0] = 255;
				newdata[y][x][1] = 255;
				newdata[y][x][2] = 255;
			}
		}
		
	}
	
	@Override
	void exe() {
		imgAft = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
		l =1;
		for(int i=0; i<height ; i++) {
			for(int j=0 ; j<width ; j++) {
				
				if(imgBef.getRGB(j, i)!= -1 && visited[i][j]==false) {
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
		
		imgAft = Util.makeImg(newdata);	
		Util.saveImg = imgAft;
		Graphics g = imagePanelAft.getGraphics();
		imagePanelAft.paintComponent(g);
		g.drawImage(imgAft, 0, 0, null);
		
	}
	
	void next() {
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
		imgAft = Util.makeImg(newdata);	
		Util.saveImg = imgAft;
		Graphics g = imagePanelAft.getGraphics();
		imagePanelAft.paintComponent(g);
		g.drawImage(imgAft, 0, 0, null);
	}
	void prev() {
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
		imgAft = Util.makeImg(newdata);	
		Util.saveImg = imgAft;
		Graphics g = imagePanelAft.getGraphics();
		imagePanelAft.paintComponent(g);
		g.drawImage(imgAft, 0, 0, null);
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
				if(dx>=0 && dx<width && dy>=0 && dy<height && imgBef.getRGB(dx, dy)!=-1 && visited[dy][dx]==false) {
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
	@Override
	void decorate() {
		setTitle(Util.cmdSegment);
		btnCmd.setText(Util.cmdSegment);
	}
}