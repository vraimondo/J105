package util;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;

import javax.swing.ImageIcon;
import javax.swing.JPanel;

public class ImagePanelIcon extends JPanel {

	private static final long serialVersionUID = 1L;
	private Image img;
	private boolean nome;

	  public ImagePanelIcon(String img,boolean nome) {		  
	    this(new ImageIcon(img).getImage(),nome);
	  }

	  public ImagePanelIcon(Image img,boolean nome) {
	    this.img = img;
	    this.nome = nome;
	    /*Dimension size = new Dimension(img.getWidth(null), img.getHeight(null));
	    setPreferredSize(size);
	    setMinimumSize(size);
	    setMaximumSize(size);
	    setSize(size);
	    setLayout(null);*/
	  }

	  public void paintComponent(Graphics g) {
		if (nome == true) {  
	    g.drawImage(img, 0, 10, null);
		} else {
		g.drawImage(img, 0, 0, null);	
		}
	  }

	}
