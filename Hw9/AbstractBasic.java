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

abstract public class AbstractBasic extends JFrame {
	private static final long serialVersionUID = 1L;
	BufferedImage oldImg;
	ImagePanel imagePanelBef;
	ImagePanel imagePanelAft;
	JButton btnShow;
	JButton btnCmd;
	int[][][] data;
	int[][][] newdata;
	int height;
	int width;
	BufferedImage imgBef = null;
	BufferedImage imgAft = null;

	AbstractBasic() {
	};

	AbstractBasic(File file) {
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
		getContentPane().setLayout(null);
		btnShow = new JButton("Show");
		btnShow.setBounds(20, 50, 100, 30);
		add(btnShow);
		
		btnCmd = new JButton("TBS");
		btnCmd.setBounds(150, 50, 100, 30);
		add(btnCmd);
		
		imagePanelBef = new ImagePanel();
		imagePanelBef.setBounds(20, 100, 700, 700);
		getContentPane().add(imagePanelBef);
		imagePanelAft = new ImagePanel();
		imagePanelAft.setBounds(720, 100, 700, 700);
		getContentPane().add(imagePanelAft);
		decorate();
		
		btnShow.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				Graphics g = imagePanelBef.getGraphics();
				imagePanelBef.paintComponent(g);
				g.drawImage(imgBef, 0, 0, null);
			}
		});
		
		btnCmd.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				exe();
			}
		});
	}

	void create() {
		this.setSize(1500, 1500);
		this.setVisible(true);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}
	
	abstract void exe();
	abstract void decorate(); 
}