package Hw9;

import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import java.io.File;
public class Main {
   private JFrame mainFrame;
   private JPanel controlPanel; 
   
   public Main(){
      prepareGUI();
   }

   public static void main(String[] args){
      Main  swingMenuDemo = new Main();     
      swingMenuDemo.showMenuDemo();
   }

   private void prepareGUI(){
      mainFrame = new JFrame("Image Processing App");
      mainFrame.setSize(510,400);
      mainFrame.setLayout(new GridLayout(3, 1));

      mainFrame.addWindowListener(new WindowAdapter() {
         public void windowClosing(WindowEvent windowEvent){
            System.exit(0);
         }        
      });    
   
      controlPanel = new JPanel();
      controlPanel.setLayout(new FlowLayout());
      mainFrame.add(controlPanel);
      mainFrame.setVisible(true);  
   }
 
   private void showMenuDemo(){
      //create a menu bar
      final JMenuBar menuBar = new JMenuBar();

      //create menus
      JMenu fileMenu = new JMenu("File");
      JMenu basicMenu = new JMenu("Basic"); 
      JMenu affineMenu = new JMenu("Affine"); 
      JMenu colorMenu = new JMenu("Color"); 
      JMenu filterMenu = new JMenu("Filter"); 
 
      JMenu morphologyMenu = new JMenu("Morphology"); 
      JMenu othersMenu = new JMenu("Others"); 
      JMenu aboutMenu = new JMenu("About"); 
     
      JMenuItem openMenuItem = new JMenuItem(Util.cmdOpen);
      openMenuItem.setMnemonic(KeyEvent.VK_O);
      openMenuItem.setActionCommand(Util.cmdOpen);
     
      JMenuItem saveAsMenuItem = new JMenuItem(Util.cmdSaveAs);
      saveAsMenuItem.setMnemonic(KeyEvent.VK_A);
      saveAsMenuItem.setActionCommand(Util.cmdSaveAs);
      
      JMenuItem saveMenuItem = new JMenuItem(Util.cmdSave);
      saveMenuItem.setMnemonic(KeyEvent.VK_S);
      saveMenuItem.setActionCommand(Util.cmdSave);
      
      JMenuItem exitMenuItem = new JMenuItem(Util.cmdExit);
      exitMenuItem.setMnemonic(KeyEvent.VK_E);
      exitMenuItem.setActionCommand(Util.cmdExit);

      FileListener fileLis = new FileListener();
      openMenuItem.addActionListener(fileLis);
      saveAsMenuItem.addActionListener(fileLis);
      saveMenuItem.addActionListener(fileLis);
      exitMenuItem.addActionListener(fileLis);

      //add menu items to menus
      fileMenu.add(openMenuItem);
      fileMenu.add(saveAsMenuItem);
      fileMenu.add(saveMenuItem);
      fileMenu.addSeparator();
      fileMenu.add(exitMenuItem); 
          
      //For Basic Menu
      JMenuItem lrMenuItem = new JMenuItem(Util.cmdLR);
      lrMenuItem.setMnemonic(KeyEvent.VK_L);
      lrMenuItem.setActionCommand(Util.cmdLR);
      
      JMenuItem udMenuItem = new JMenuItem(Util.cmdUD);
      udMenuItem.setMnemonic(KeyEvent.VK_U);
      udMenuItem.setActionCommand(Util.cmdUD);
      
      JMenuItem l90MenuItem = new JMenuItem(Util.cmdL90);
      l90MenuItem.setMnemonic(KeyEvent.VK_L);
      l90MenuItem.setActionCommand(Util.cmdL90);
      
      JMenuItem r90MenuItem = new JMenuItem(Util.cmdR90);
      r90MenuItem.setMnemonic(KeyEvent.VK_R);
      r90MenuItem.setActionCommand(Util.cmdR90);
      
      JMenuItem r180MenuItem = new JMenuItem(Util.cmdR180);
      r180MenuItem.setMnemonic(KeyEvent.VK_8); 
      r180MenuItem.setActionCommand(Util.cmdR180);
      
      //setup Inverse parameters
      JMenuItem inverseMenuItem = new JMenuItem(Util.cmdInverse);
      inverseMenuItem.setMnemonic(KeyEvent.VK_I);
      inverseMenuItem.setActionCommand(Util.cmdInverse);
      
      JMenuItem garyMenuItem = new JMenuItem(Util.cmdGray);
      garyMenuItem.setMnemonic(KeyEvent.VK_G);
      garyMenuItem.setActionCommand(Util.cmdGray);
      
      basicMenu.add(lrMenuItem);
      basicMenu.add(udMenuItem);
      basicMenu.add(l90MenuItem);
      basicMenu.add(r90MenuItem);
      basicMenu.add(r180MenuItem);
      basicMenu.addSeparator();
      basicMenu.add(inverseMenuItem);
      basicMenu.add(garyMenuItem);
      
      BasicListener basicLis = new BasicListener();
      lrMenuItem.addActionListener(basicLis);
      inverseMenuItem.addActionListener(basicLis);
      udMenuItem.addActionListener(basicLis);
      l90MenuItem.addActionListener(basicLis);
      r90MenuItem.addActionListener(basicLis);
      r180MenuItem.addActionListener(basicLis);
      garyMenuItem.addActionListener(basicLis);
      
      //For Affine Menu
      JMenuItem translationMenuItem = new JMenuItem(Util.cmdTranslation);
      translationMenuItem.setActionCommand(Util.cmdTranslation);
      JMenuItem scalingMenuItem = new JMenuItem(Util.cmdScaling);
      scalingMenuItem.setActionCommand(Util.cmdScaling);
      JMenuItem rotationMenuItem = new JMenuItem(Util.cmdRotation);
      rotationMenuItem.setActionCommand(Util.cmdRotation);
 
      affineMenu.add(translationMenuItem);
      affineMenu.add(scalingMenuItem);
      affineMenu.add(rotationMenuItem);
         
      AffineListener affineLis = new  AffineListener();
      translationMenuItem.addActionListener(affineLis);
      scalingMenuItem.addActionListener(affineLis);
      rotationMenuItem.addActionListener(affineLis);
      
      //For Color Menu
      JMenuItem hslMenuItem = new JMenuItem(Util.cmdHSL);
      hslMenuItem.setActionCommand(Util.cmdHSL);
    
      colorMenu.add(hslMenuItem);
   
      ColorListener colorLis = new ColorListener();
      hslMenuItem.addActionListener(colorLis);
   
      //For Filter Menu
      JMenuItem hpMenuItem = new JMenuItem(Util.cmdHP);
      hpMenuItem.setActionCommand(Util.cmdHP);
      JMenuItem lpMenuItem = new JMenuItem(Util.cmdLP);
      lpMenuItem.setActionCommand(Util.cmdLP);
      
      filterMenu.add(hpMenuItem);
      filterMenu.add(lpMenuItem);
      
      FilterListener filterLis = new FilterListener();
      hpMenuItem.addActionListener(filterLis);
      lpMenuItem.addActionListener(filterLis);
      
      

      
     
      
      //For Morphology Menu
      JMenuItem erodeMenuItem = new JMenuItem(Util.cmdErode);
      erodeMenuItem.setActionCommand(Util.cmdErode);
      JMenuItem dilateMenuItem = new JMenuItem(Util.cmdDilate);
      dilateMenuItem.setActionCommand(Util.cmdDilate);
      JMenuItem openOpMenuItem = new JMenuItem(Util.cmdOpenOp);
      openOpMenuItem.setActionCommand(Util.cmdOpenOp);
      JMenuItem closeOpMenuItem = new JMenuItem(Util.cmdCloseOp);
      closeOpMenuItem.setActionCommand(Util.cmdCloseOp);
      JMenuItem segmentMenuItem = new JMenuItem(Util.cmdSegment);
      segmentMenuItem.setActionCommand(Util.cmdSegment);
      
      morphologyMenu.add(erodeMenuItem);
      morphologyMenu.add(dilateMenuItem);
      morphologyMenu.add(openOpMenuItem);
      morphologyMenu.add(closeOpMenuItem);
      morphologyMenu.add(segmentMenuItem);
         
      MorphologyListener morphologyLis = new MorphologyListener();
      erodeMenuItem.addActionListener(morphologyLis);
      dilateMenuItem.addActionListener(morphologyLis);
      openOpMenuItem.addActionListener(morphologyLis);
      closeOpMenuItem.addActionListener(morphologyLis);
      segmentMenuItem.addActionListener(morphologyLis);
      
      //For Others Menu
      JMenuItem mosaicMenuItem = new JMenuItem(Util.cmdMosaic);
      mosaicMenuItem.setActionCommand(Util.cmdMosaic);   
      
      othersMenu.add(mosaicMenuItem);
      
      OthersListener othersLis = new OthersListener();
      mosaicMenuItem.addActionListener(othersLis);
      
      //For About Menu
      JMenuItem nknuMenuItem = new JMenuItem(Util.cmdNKNU);
      nknuMenuItem.setActionCommand(Util.cmdNKNU); 
      JMenuItem semMenuItem = new JMenuItem(Util.cmdSEM);
      semMenuItem.setActionCommand(Util.cmdSEM);
      JMenuItem meMenuItem = new JMenuItem(Util.cmdMe);
      meMenuItem.setActionCommand(Util.cmdMe);
      
      aboutMenu.add(nknuMenuItem);
      aboutMenu.add(semMenuItem);
      aboutMenu.add(meMenuItem);
      
      AboutListener aboutsLis = new AboutListener();
      nknuMenuItem.addActionListener(aboutsLis);
      semMenuItem.addActionListener(aboutsLis);
      meMenuItem.addActionListener(aboutsLis);
      
      
      //add menu to menubar
      menuBar.add(fileMenu);
      menuBar.add(basicMenu);
      menuBar.add(affineMenu);
      menuBar.add(colorMenu);
      menuBar.add(filterMenu);
      menuBar.add(morphologyMenu);
      menuBar.add(othersMenu);
      menuBar.add(aboutMenu);


      //add menubar to the frame
      mainFrame.setJMenuBar(menuBar);
      mainFrame.setVisible(true);     
   }
   
   class FileListener implements ActionListener {
      
	   public void actionPerformed(ActionEvent e) {
    	  String command = e.getActionCommand();
    	  if (command.endsWith(Util.cmdExit))
    		  System.exit(0);
    	  if (command.equals(Util.cmdOpen)){
    		  JFileChooser fc = new JFileChooser();
    		  int returnVal = fc.showOpenDialog(mainFrame);
    		  if (returnVal == JFileChooser.APPROVE_OPTION)
    			 Util.selectedFile = fc.getSelectedFile();
    		  System.out.println(Util.selectedFile);
    	  }
    	  
			if (command.equals(Util.cmdSaveAs)) {
				JFileChooser fc = new JFileChooser();
				int returnVal = fc.showOpenDialog(mainFrame);
				if (returnVal == JFileChooser.APPROVE_OPTION)
					Util.saveFile = fc.getSelectedFile();
				File outputfile = new File((Util.saveFile).getPath()+".png");
				System.out.println(outputfile);
				try {
					ImageIO.write(Util.saveImg, "png", outputfile);
				} catch (IOException e1) {
					e1.printStackTrace();
				}
				Util.saveImg = null;
			}
			
			if (command.equals(Util.cmdSave)) {
				try {
					ImageIO.write(Util.saveImg, "png", Util.selectedFile);
				} catch (IOException e1) {
					e1.printStackTrace();
				}
				Util.saveImg = null;
			}
		}
	}

   class BasicListener implements ActionListener {
	   public void actionPerformed(ActionEvent e) {
	    	  String command = e.getActionCommand();
	    	  if (command.equals(Util.cmdLR)){
	    		  new LR(Util.selectedFile).create();
	    		  System.out.println(Util.selectedFile);
	    		  
	    	  }else if (command.equals(Util.cmdInverse)){
	    		  new Inverse(Util.selectedFile).create();
	    		  System.out.println(Util.selectedFile);
	    		  
	    	  }else if (command.equals(Util.cmdUD)){
		 		  new UD(Util.selectedFile).create();
			       System.out.println(Util.selectedFile);
			       
	    	  }else if (command.equals(Util.cmdUD)){
		 		  new Inverse(Util.selectedFile).create();
			       System.out.println(Util.selectedFile); 
	    	  }else if (command.equals(Util.cmdL90)){
		 		  new L90(Util.selectedFile).create();
			       System.out.println(Util.selectedFile); 
	    	  }else if (command.equals(Util.cmdR90)){
		 		  new R90(Util.selectedFile).create();
			       System.out.println(Util.selectedFile); 
	    	  }else if (command.equals(Util.cmdR180)){
		 		  new R180(Util.selectedFile).create();
			       System.out.println(Util.selectedFile); 
	    	  }else if (command.equals(Util.cmdGray)){
		 		  new Gray(Util.selectedFile).create();
			       System.out.println(Util.selectedFile); 
	    	  }
	      }     
   }
   
   class AffineListener implements ActionListener {
	      public void actionPerformed(ActionEvent e) {
	    	  String command = e.getActionCommand();
	    	  if (command.equals(Util.cmdTranslation)){
	    		  new Translation(Util.selectedFile).create();
	    		  System.out.println(Util.selectedFile);
	    	  }else if (command.equals(Util.cmdScaling)){
	    		  new Scaling(Util.selectedFile).create();
	    		  System.out.println(Util.selectedFile);
	    	  }else if (command.equals(Util.cmdRotation)){
	    		  new Rotation(Util.selectedFile).create();
	    		  System.out.println(Util.selectedFile);
	    	  }
			}
		}
   
   class ColorListener implements ActionListener {
	      public void actionPerformed(ActionEvent e) {
	    	 String command = e.getActionCommand();  
	    	 if (command.equals(Util.cmdHSL)){
	    		  new HSL(Util.selectedFile).create();
	    		  System.out.println(Util.selectedFile);
	    		  
	    	  }
	    	  
			}
		}
   
   class FilterListener implements ActionListener {
	      public void actionPerformed(ActionEvent e) {
	    	  String command = e.getActionCommand();
	    	  if (command.equals(Util.cmdHP)){
	    		  new HP(Util.selectedFile).create();
	    		  System.out.println(Util.selectedFile);
	    		  
	    	  }else if (command.equals(Util.cmdLP)){
		 		  new LP(Util.selectedFile).create();
			       System.out.println(Util.selectedFile); 
	    	  }
	    	  
			}
	}
   

   class MorphologyListener implements ActionListener {
	      public void actionPerformed(ActionEvent e) {
	    	  String command = e.getActionCommand();
	    	  if (command.equals(Util.cmdErode)){
	    		  new Erode(Util.selectedFile).create();
	    		  System.out.println(Util.selectedFile);
	    		  
	    	  }else if (command.equals(Util.cmdDilate)){
		 		  new Dilate(Util.selectedFile).create();
			       System.out.println(Util.selectedFile); 
			       
	    	  }else if (command.equals(Util.cmdOpenOp)){
		 		  new OpenOp(Util.selectedFile).create();
			       System.out.println(Util.selectedFile); 
			       
	    	  }else if (command.equals(Util.cmdCloseOp)){
		 		  new CloseOp(Util.selectedFile).create();
			       System.out.println(Util.selectedFile); 
			       
	    	  }else if (command.equals(Util.cmdSegment)){
		 		  new Segment(Util.selectedFile).create();
			       System.out.println(Util.selectedFile); 
	    	  }
			}
		}
   
   class OthersListener implements ActionListener {
	      public void actionPerformed(ActionEvent e) {
	    	  String command = e.getActionCommand();  
	    	  if (command.equals(Util.cmdMosaic)){
	    		  new Mosaic(Util.selectedFile).create();
	    		  System.out.println(Util.selectedFile);
	    		  
	    	  }
			}
		}
   
   class AboutListener implements ActionListener {
	      public void actionPerformed(ActionEvent e) {
	    	  String command = e.getActionCommand();  
	    	  if(command.equals(Util.cmdNKNU)) {
	    			try {
	    				String url = "https://w3.nknu.edu.tw/zh/";
	    				java.net.URI uri = java.net.URI.create(url);
	    				java.awt.Desktop dp = java.awt.Desktop.getDesktop();
	    				if (dp.isSupported(java.awt.Desktop.Action.BROWSE)) {
	    					dp.browse(uri);
	    				}
	    			} catch (java.lang.NullPointerException e1) {
	    				e1.printStackTrace();
	    			} catch (java.io.IOException e1) {
	    				e1.printStackTrace();
	    			}
	    		}else if(command.equals(Util.cmdSEM)) {
	    			try {
	    				String url = "https://dept.nknu.edu.tw/WE/zh/";
	    				java.net.URI uri = java.net.URI.create(url);
	    				java.awt.Desktop dp = java.awt.Desktop.getDesktop();
	    				if (dp.isSupported(java.awt.Desktop.Action.BROWSE)) {
	    					dp.browse(uri);
	    				}
	    			} catch (java.lang.NullPointerException e1) {
	    				e1.printStackTrace();
	    			} catch (java.io.IOException e1) {
	    				e1.printStackTrace();
	    			}
	    		}else if(command.equals(Util.cmdMe)) {
	    			try {
	    				String url = "https://github.com/kiwiiiiiiiiO";
	    				java.net.URI uri = java.net.URI.create(url);
	    				java.awt.Desktop dp = java.awt.Desktop.getDesktop();
	    				if (dp.isSupported(java.awt.Desktop.Action.BROWSE)) {
	    					dp.browse(uri);
	    				}
	    			} catch (java.lang.NullPointerException e1) {
	    				e1.printStackTrace();
	    			} catch (java.io.IOException e1) {
	    				e1.printStackTrace();
	    			}
	    		}
	    	  }
		}
}