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

public class LR extends AbstractBasic {
	private static final long serialVersionUID = 1L;

	LR() {
	};

	LR(File file) {
		super(file);
	}
	
	@Override
	void exe() {
		{
			 
			imgAft = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
			for (int y = 0; y < height; y++) {
				for (int x = 0; x < width; x++) {
					int rgb = Util.makeColor(data[y][width - x - 1][0],
							                 data[y][width - x - 1][1],
							                 data[y][width - x - 1][2]);
					imgAft.setRGB(x, y, rgb);
				}
			}
			Util.saveImg = imgAft;
			Graphics g = imagePanelAft.getGraphics();
			imagePanelAft.paintComponent(g);
			g.drawImage(imgAft, 0, 0, null);
		}
		
	}

	@Override
	void decorate() {
		setTitle(Util.cmdLR);
		btnCmd.setText(Util.cmdLR);
	}
}