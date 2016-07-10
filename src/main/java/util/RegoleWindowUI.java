package util;

import javax.swing.*;
import java.awt.*;
import java.io.File;

public class RegoleWindowUI extends JFrame {

	
	private static final long serialVersionUID = 1L;

   public RegoleWindowUI (){  
	   super();
	 this.setPreferredSize(new Dimension(700,400) );
	 this.setTitle("Regole Centocinque");
	 Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		Dimension labelSize = this.getPreferredSize();
		this.setLocation(screenSize.width/2 - (labelSize.width/2), 
				screenSize.height/2 - (labelSize.height/2));
	 this.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
	 regole();
	 this.pack();
	 this.setVisible(true);
	 
   }
	


   public void regole(){
	   File file = new File("/src/regole.txt");
	   String s = new String("Centocinque (gioco)\nda Wikipedia, l'enciclopedia libera. \n\n " +
	   		"Il Centocinque o 105 è un gioco di carte molto simile a Uno,\n " +
	   		"infatti perde chi totalizza pi� punti, col tetto massimo di 105 punti.\n " +
	   		"Come nel caso di Uno,lo scopo del gioco � di scartare tutte le carte \n " +
	   		"in proprio possesso. Esso viene giocato con le carte napoletane.\n " +
	   		"Discretamente conosciuto al sud Italia, il 105 non ha una tradizione\n " +
	   		"molto antica e viene giocato per lo pi� in famiglia.\n " +
	   		"Non esistono quindi competizioni o tornei che includono questo gioco.\n " +
	   		"Il gioco originale � Uno contro uno, ma � possibile anche giocare una\n" +
	   		"variante multiplayer con una leggera modifica alle regole. ");   
	   JTextArea info = new JTextArea(s);
	   info.setAlignmentX(CENTER_ALIGNMENT);
	   info.setEditable(false);
	   JScrollPane scroll = new JScrollPane(info);
	   scroll.setBounds(0,0,700,400);
	   add(scroll);
	   
   }
   }