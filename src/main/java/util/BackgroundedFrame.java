package util;

import java.awt.Graphics;
import java.awt.GridLayout;
import java.awt.image.BufferedImage;
import java.io.File;

import javax.imageio.ImageIO;
import javax.swing.JPanel;

import manager.ManagerToken;

public class BackgroundedFrame extends JPanel{
	  BufferedImage img;
	  boolean nome;
	  int myMaster;

	  public BackgroundedFrame(File f,boolean nome){
	    super(true); //crea un JPanel con doubleBuffered true
	    try{
	    	this.nome = nome;
	    	setImage(ImageIO.read(f));
	    } catch(Exception e) {
	    	System.out.println(e);
	    }
	    }

	  public void setImage(BufferedImage img){
	    this.img = img;
	  }

	  // sovrascrivi il metodo paintComponent passandogli l'immagine partendo dalle coordinate 0,0 senza usare un ImageObserver (null)
	  public void paintComponent(Graphics g){
		try {
			super.paintComponent(g);
			if (nome) {
				 g.drawImage(img, 0, 10, null);
			} else {
				g.drawImage(img, 0, 0, null);
			}
			   
		}catch (Exception e) {
			System.out.println(e);
		}
	  }

	public int getMyMaster() {
		return myMaster;
	}

	public void setMyMaster(int myMaster) {
		this.myMaster = myMaster;
	}
	  
	  
	  
	}