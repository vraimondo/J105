package client.ui;

import client.Player;
import client.PlayerImpl;
import game.Card;
import manager.ManagerToken;
import util.BackgroundedFrame;
import util.ImagePanel;
import util.ImagePanelIcon;

import javax.swing.*;
import javax.swing.GroupLayout.Alignment;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.Timer;
import javax.swing.border.BevelBorder;
import javax.swing.border.Border;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.rmi.RemoteException;
import java.util.*;
import java.util.List;
import java.util.logging.Logger;



public class PlayersUI extends JFrame 
implements ActionListener {
	private static final long serialVersionUID = 1;


	private Player player;
	private Logger logger;
	private PlayerImpl playerImp;
	private ManagerToken manager;
	private int indiceCartaSelezionata = -1;
	private int sommaSecondi;
	private Timer countDown;
	private JPanel carte;
	private JButton centocinqueButton;
	private JMenuBar barraMenu;
	private JMenuItem esciMenu;
	private JMenu fileMenuBar;
	private JButton nextTurnButton;
	private JPanel pannelloBottone;
	private JPanel pannelloGenerale;
	private JPanel pannelloIniziale;
	private JPanel pannelloFinale;
	private JPanel panelSpecial;
	private ImagePanel tavolo;
	private JLabel cartaRetro;
	private JPanel timer;
	private JPanel cartaTavolo;
	private JPanel nomePanel;
	private JLabel name;
	private JLabel secondi; 
	private JLabel messaggio;
	private JList listaCarteSwing;
	private ArrayList<Card> listaCarteGiocabili;
	private ArrayList<JPanel> listaPannelli;
	private boolean shoutDone;
	private JLabel loading;
	private boolean reallySelected = false;
	private JPanel pannelloNO;
	private JPanel pannelloNC;
	private JPanel pannelloNE;
	private JPanel pannelloCO;
	private JPanel pannelloCE;
	private JPanel pannelloMio;
	private int numberPlayersBefore = 0;
	private JLabel simboloCentrale;
	private JPanel pannelloMessaggi;
	private JPanel info;
	private JPanel score;
	private DefaultListModel listModel;
	private LinkedHashMap<Integer, Integer> scoreLH;
	private LinkedHashMap<Integer, String> nameLH;
	private JLabel textWin;
	private JLabel test;
	private JButton closeButton;
	private String imagePath = "/images/" ;
	

	/**
	 * Crea e lancia la GUI di prova
	 */
	 public PlayersUI(Player player) {
		super();
		setResizable(false);
		this.player = player;
		listaPannelli = new ArrayList<JPanel>();
		initGUI();
		logger = Logger.getLogger("game.giocatore.ui");
	 }


	 private void initGUI() {
		 try {

			 pannelloIniziale = new ImagePanel( new ImageIcon(getClass().getResource(imagePath + "panelinit.jpg")).getImage());
			 pannelloFinale =  new ImagePanel(new ImageIcon(PlayersUI.class.getResource(imagePath + "bgwinner.jpg")).getImage());
			 pannelloGenerale = new JPanel();

			 carte = new JPanel();
			 carte.setBackground(new Color(102, 51, 0));
			 tavolo = new ImagePanel(new ImageIcon(PlayersUI.class.getResource(imagePath + "tavolo.jpg")).getImage());
			 panelSpecial = new ImagePanel(new ImageIcon(PlayersUI.class.getResource(imagePath + "panelspec.png")).getImage());
			 cartaTavolo = new JPanel();
			 cartaTavolo.setBackground(new Color(84,137,169));
			 nomePanel = new JPanel();
			 timer = new JPanel();
			 messaggio = new JLabel();
			 info = new JPanel();
			 score = new JPanel();
			 pannelloBottone = new JPanel();
			 nextTurnButton = new JButton();
			 nextTurnButton.setHorizontalTextPosition(SwingConstants.CENTER);
			 centocinqueButton = new JButton();
			 closeButton = new JButton("Close");
			 barraMenu = new JMenuBar();
			 fileMenuBar = new JMenu();
			 esciMenu = new JMenuItem();
			 listModel = new DefaultListModel();
			 
			 
			 setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

			 //posiziono al centro la finestra
			 
			 Dimension screen = Toolkit.getDefaultToolkit().getScreenSize();
			 int X = (screen.width / 2) - (1020 / 2); // Center horizontally.
			 int Y = (screen.height / 2) - (680 / 2); // Center vertically.

			 this.setBounds(X,Y , 1020,680);

			 this.setTitle("JCentocinque");
			 
			 
			 this.setPreferredSize(new Dimension(1020,680));

			 carte.setBorder(new TitledBorder(new BevelBorder(BevelBorder.LOWERED, null, null, null, null), "Carte", TitledBorder.LEFT, TitledBorder.TOP, null, null));
			 carte.setEnabled(false);
		

			 GroupLayout gl_carte = new GroupLayout(carte);
			 gl_carte.setHorizontalGroup(
					 gl_carte.createParallelGroup(Alignment.LEADING)
					 .addGap(0, 801, Short.MAX_VALUE)
					 );
			 gl_carte.setVerticalGroup(
					 gl_carte.createParallelGroup(Alignment.LEADING)
					 .addGap(0, 196, Short.MAX_VALUE)
					 );
			 carte.setLayout(gl_carte);

			 panelSpecial.setEnabled(false);
			 panelSpecial.setVisible(false);
			 
			 GroupLayout gl_panelSpecial = new GroupLayout(panelSpecial);
			 panelSpecial.setLayout(gl_panelSpecial);
			 gl_panelSpecial.setHorizontalGroup(
					 gl_panelSpecial.createParallelGroup(GroupLayout.Alignment.LEADING)
					 .addGap(0, 517, Short.MAX_VALUE)
					 );
			 gl_panelSpecial.setVerticalGroup(
					 gl_panelSpecial.createParallelGroup(GroupLayout.Alignment.LEADING)
					 .addGap(0, 223, Short.MAX_VALUE)
					 );

			 nomePanel.setBorder(new TitledBorder(new BevelBorder(BevelBorder.LOWERED, null, null, null, null), "Giocatore", TitledBorder.LEFT, TitledBorder.TOP, null, null));
			 nomePanel.setBackground(new Color(129, 117, 103));
			 GroupLayout gl_nomePanel = new GroupLayout(nomePanel);
			 nomePanel.setLayout(gl_nomePanel);
			 gl_nomePanel.setHorizontalGroup(
					 gl_nomePanel.createParallelGroup(GroupLayout.Alignment.LEADING)
					 .addGap(0, 269, Short.MAX_VALUE)
					 );
			 gl_nomePanel.setVerticalGroup(
					 gl_nomePanel.createParallelGroup(GroupLayout.Alignment.LEADING)
					 .addGap(0, 36, Short.MAX_VALUE)
					 );
			 
			 tavolo.setBorder(new TitledBorder(new BevelBorder(BevelBorder.LOWERED, null, null, null, null), "Tavolo", TitledBorder.LEFT, TitledBorder.TOP, null, null));
			 tavolo.setEnabled(false);
		
		
			 GroupLayout gl_tavolo = new GroupLayout(tavolo);
			 tavolo.setLayout(gl_tavolo);
			 gl_tavolo.setHorizontalGroup(
					 gl_tavolo.createParallelGroup(GroupLayout.Alignment.LEADING)
					 .addGap(0, 517, Short.MAX_VALUE)
					 );
			 gl_tavolo.setVerticalGroup(
					 gl_tavolo.createParallelGroup(GroupLayout.Alignment.LEADING)
					 .addGap(0, 223, Short.MAX_VALUE)
					 );

			 cartaTavolo.setBorder(new TitledBorder(new BevelBorder(BevelBorder.LOWERED, null, null, null, null), "Carta Tavolo", TitledBorder.LEFT, TitledBorder.TOP, null, null));
			 GroupLayout gl_ultimaCarta = new GroupLayout(cartaTavolo);
			 cartaTavolo.setLayout(gl_ultimaCarta);
			 gl_ultimaCarta.setHorizontalGroup(
					 gl_ultimaCarta.createParallelGroup(GroupLayout.Alignment.LEADING)
					 .addGap(0, 250, Short.MAX_VALUE)
					 );
			 gl_ultimaCarta.setVerticalGroup(
					 gl_ultimaCarta.createParallelGroup(GroupLayout.Alignment.LEADING)
					 .addGap(0, 131, Short.MAX_VALUE)
					 );

			 timer.setBorder(new TitledBorder(new BevelBorder(BevelBorder.LOWERED, null, null, null, null), "Timer", TitledBorder.LEFT, TitledBorder.TOP, null, null));

			 GroupLayout gl_timer = new GroupLayout(timer);
			 timer.setLayout(gl_timer);
			 gl_timer.setHorizontalGroup(
					 gl_timer.createParallelGroup(GroupLayout.Alignment.LEADING)
					 .addGap(0, 269, Short.MAX_VALUE)
					 );
			 gl_timer.setVerticalGroup(
					 gl_timer.createParallelGroup(GroupLayout.Alignment.LEADING)
					 .addGap(0, 36, Short.MAX_VALUE)
					 );

			 GroupLayout gl_pannelloBottone = new GroupLayout(pannelloBottone);
			 gl_pannelloBottone.setHorizontalGroup(
			 	gl_pannelloBottone.createParallelGroup(Alignment.LEADING)
			 		.addGroup(gl_pannelloBottone.createSequentialGroup()
			 			.addGap(87)
			 			.addComponent(centocinqueButton)
			 			.addGap(285)
			 			.addComponent(nextTurnButton, GroupLayout.PREFERRED_SIZE, 138, GroupLayout.PREFERRED_SIZE)
			 			.addContainerGap(400, Short.MAX_VALUE))
			 );
			 gl_pannelloBottone.setVerticalGroup(
			 	gl_pannelloBottone.createParallelGroup(Alignment.TRAILING)
			 		.addGroup(gl_pannelloBottone.createParallelGroup(Alignment.BASELINE)
			 			.addComponent(centocinqueButton, GroupLayout.PREFERRED_SIZE, 31, GroupLayout.PREFERRED_SIZE)
			 			.addComponent(nextTurnButton, GroupLayout.DEFAULT_SIZE, 39, Short.MAX_VALUE))
			 );
			 pannelloBottone.setLayout(gl_pannelloBottone);
			 
			
			 info.setBorder(new TitledBorder(new BevelBorder(BevelBorder.LOWERED, null, null, null, null), "Info", TitledBorder.LEFT, TitledBorder.TOP, null, null));
			 info.setBackground(new Color(129, 117, 103));
			 GroupLayout gl_info = new GroupLayout(info);
			 gl_info.setHorizontalGroup(
			 	gl_info.createParallelGroup(Alignment.LEADING)
			 		.addGap(0, 298, Short.MAX_VALUE)
			 		.addGap(0, 278, Short.MAX_VALUE)
			 );
			 gl_info.setVerticalGroup(
			 	gl_info.createParallelGroup(Alignment.LEADING)
			 		.addGap(0, 199, Short.MAX_VALUE)
			 		.addGap(0, 164, Short.MAX_VALUE)
			 );
			 info.setLayout(gl_info);
			 
			
			 score.setBorder(new TitledBorder(new BevelBorder(BevelBorder.LOWERED, null, null, null, null), "Score", TitledBorder.LEFT, TitledBorder.TOP, null, null));
			 score.setBackground(new Color(129, 117, 103));
			 GroupLayout gl_score = new GroupLayout(score);
			 gl_score.setHorizontalGroup(
			 	gl_score.createParallelGroup(Alignment.LEADING)
			 		.addGap(0, 298, Short.MAX_VALUE)
			 		.addGap(0, 298, Short.MAX_VALUE)
			 		.addGap(0, 278, Short.MAX_VALUE)
			 );
			 gl_score.setVerticalGroup(
			 	gl_score.createParallelGroup(Alignment.LEADING)
			 		.addGap(0, 92, Short.MAX_VALUE)
			 		.addGap(0, 199, Short.MAX_VALUE)
			 		.addGap(0, 164, Short.MAX_VALUE)
			 );
			 score.setLayout(gl_score);

			 GroupLayout gl_pannelloGenerale = new GroupLayout(pannelloGenerale);
			 gl_pannelloGenerale.setHorizontalGroup(
			 	gl_pannelloGenerale.createParallelGroup(Alignment.LEADING)
			 		.addGroup(gl_pannelloGenerale.createSequentialGroup()
			 			.addContainerGap()
			 			.addGroup(gl_pannelloGenerale.createParallelGroup(Alignment.LEADING)
			 				.addComponent(pannelloBottone, Alignment.TRAILING, GroupLayout.DEFAULT_SIZE, 974, Short.MAX_VALUE)
			 				.addGroup(gl_pannelloGenerale.createSequentialGroup()
			 					.addGroup(gl_pannelloGenerale.createParallelGroup(Alignment.LEADING)
			 						.addComponent(carte, GroupLayout.DEFAULT_SIZE, 664, Short.MAX_VALUE)
			 						
			 						.addComponent(panelSpecial, Alignment.TRAILING, GroupLayout.DEFAULT_SIZE, 664, Short.MAX_VALUE)
			 						.addComponent(tavolo, Alignment.TRAILING, GroupLayout.DEFAULT_SIZE, 664, Short.MAX_VALUE))
			 				    	.addPreferredGap(ComponentPlacement.RELATED)
			 					.addGroup(gl_pannelloGenerale.createParallelGroup(Alignment.TRAILING)
			 						.addComponent(score, GroupLayout.DEFAULT_SIZE, 304, Short.MAX_VALUE)
			 						.addComponent(info, GroupLayout.DEFAULT_SIZE, 304, Short.MAX_VALUE)
			 						.addComponent(nomePanel, GroupLayout.DEFAULT_SIZE, 304, Short.MAX_VALUE)
			 						.addComponent(timer, GroupLayout.DEFAULT_SIZE, 304, Short.MAX_VALUE)
			 						.addComponent(cartaTavolo, GroupLayout.DEFAULT_SIZE, 304, Short.MAX_VALUE))))
			 			.addContainerGap())
			 );
			 gl_pannelloGenerale.setVerticalGroup(
			 	gl_pannelloGenerale.createParallelGroup(Alignment.TRAILING)
			 		.addGroup(gl_pannelloGenerale.createSequentialGroup()
			 			.addContainerGap()
			 			.addGroup(gl_pannelloGenerale.createParallelGroup(Alignment.LEADING)
			 				.addGroup(gl_pannelloGenerale.createSequentialGroup()
			 					.addComponent(nomePanel, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
			 					.addPreferredGap(ComponentPlacement.RELATED)
			 					.addComponent(timer, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
			 					.addPreferredGap(ComponentPlacement.RELATED)
			 					.addComponent(cartaTavolo, GroupLayout.DEFAULT_SIZE, 181, Short.MAX_VALUE))
			 					.addComponent(panelSpecial, GroupLayout.DEFAULT_SIZE, 317, Short.MAX_VALUE)
			 				.addComponent(tavolo, GroupLayout.DEFAULT_SIZE, 317, Short.MAX_VALUE))
			 			.addPreferredGap(ComponentPlacement.RELATED, 20, Short.MAX_VALUE)
			 			.addGroup(gl_pannelloGenerale.createParallelGroup(Alignment.BASELINE)
			 				.addComponent(carte, GroupLayout.PREFERRED_SIZE, 216, GroupLayout.PREFERRED_SIZE)
			 				.addGroup(gl_pannelloGenerale.createSequentialGroup()
			 					.addComponent(info, GroupLayout.PREFERRED_SIZE, 91, GroupLayout.PREFERRED_SIZE)
			 					.addPreferredGap(ComponentPlacement.RELATED, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
			 					.addComponent(score, GroupLayout.PREFERRED_SIZE, 119, GroupLayout.PREFERRED_SIZE)))
			 			.addPreferredGap(ComponentPlacement.RELATED)
			 			.addComponent(pannelloBottone, GroupLayout.PREFERRED_SIZE, 50, GroupLayout.PREFERRED_SIZE)
			 			.addContainerGap())
			 );
			 pannelloGenerale.setLayout(gl_pannelloGenerale);
			 
			 nextTurnButton.setText("Play");
			 nextTurnButton.setVisible(false);
			 nextTurnButton.setActionCommand("next");
			 nextTurnButton.addActionListener(new ClickNextTurn() );

			 centocinqueButton.setBackground(Color.RED);
			 centocinqueButton.setText("Penultima");
			 centocinqueButton.setVisible(false);
			 
			 fileMenuBar.setText("J105");
			 
			 esciMenu.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_Q, InputEvent.CTRL_MASK));
			 esciMenu.setText("Esci");
			 esciMenu.setActionCommand("quit");
			 esciMenu.addActionListener(this);
			 fileMenuBar.add(esciMenu);

			 barraMenu.add(fileMenuBar);
			 barraMenu.setVisible(false);
			 setJMenuBar(barraMenu);
			
			 info.setLayout(new FlowLayout());
			 setInfo(info);
			 score.setLayout(new FlowLayout());
			 this.setScore(score);
			 
			 this.setPanelSpecial(panelSpecial);
			 			 	 
			 test = new JLabel(new ImageIcon(PlayersUI.class.getResource(imagePath + "stop.png")));
			 
			 pannelloFinale.setSize(1020, 680);
			 pannelloFinale.setVisible(false);
			 textWin = new JLabel();
			 textWin.setText("");
			 textWin.setHorizontalAlignment( JLabel.CENTER );
			 Font f = new Font("Arial", Font.BOLD, 26);	 
			 textWin.setFont(f);	
			 closeButton.setVisible(false);
			 closeButton.setActionCommand("quit");
			 closeButton.addActionListener(this);
			 
			 
			 
	
			  			 	 pannelloIniziale.setSize(1020, 680);
			  			 	 
			  			 	 loading = new JLabel(new ImageIcon(PlayersUI.class.getResource(imagePath + "loading.gif")));
			  			 	 
			  			 	 			 			 	 GroupLayout gl_pannelloIniziale = new GroupLayout(pannelloIniziale);
			  			 	 			 			 	 gl_pannelloIniziale.setHorizontalGroup(
			  			 	 			 			 	 	gl_pannelloIniziale.createParallelGroup(Alignment.TRAILING)
			  			 	 			 			 	 		.addGroup(Alignment.LEADING, gl_pannelloIniziale.createSequentialGroup()
			  			 	 			 			 	 			.addGap(417)
			  			 	 			 			 	 			.addComponent(loading, GroupLayout.PREFERRED_SIZE, 136, GroupLayout.PREFERRED_SIZE)
			  			 	 			 			 	 			.addContainerGap(447, Short.MAX_VALUE))
			  			 	 			 			 	 );
			  			 	 			 			 	 gl_pannelloIniziale.setVerticalGroup(
			  			 	 			 			 	 	gl_pannelloIniziale.createParallelGroup(Alignment.LEADING)
			  			 	 			 			 	 		.addGroup(Alignment.TRAILING, gl_pannelloIniziale.createSequentialGroup()
			  			 	 			 			 	 			.addContainerGap(510, Short.MAX_VALUE)
			  			 	 			 			 	 			.addComponent(loading, GroupLayout.PREFERRED_SIZE, 52, GroupLayout.PREFERRED_SIZE)
			  			 	 			 			 	 			.addGap(118))
			  			 	 			 			 	 );
			  			 	 			 			 	 pannelloIniziale.setLayout(gl_pannelloIniziale);
			  			 	 			 			 	 
			 getContentPane().add(pannelloIniziale, BorderLayout.CENTER);
			  
			 GroupLayout gl_pannelloFinale = new GroupLayout(pannelloFinale);
			 gl_pannelloFinale.setHorizontalGroup(
			 	gl_pannelloFinale.createParallelGroup(Alignment.TRAILING)
			 		.addGroup(gl_pannelloFinale.createSequentialGroup()
			 			.addGroup(gl_pannelloFinale.createParallelGroup(Alignment.LEADING)
			 				.addGroup(gl_pannelloFinale.createSequentialGroup()
			 					.addContainerGap()
			 					.addComponent(textWin, GroupLayout.PREFERRED_SIZE, 992, GroupLayout.PREFERRED_SIZE))
			 				.addGroup(gl_pannelloFinale.createSequentialGroup()
			 					.addGap(458)
			 					.addComponent(closeButton, GroupLayout.PREFERRED_SIZE, 102, GroupLayout.PREFERRED_SIZE)))
			 			.addContainerGap(18, Short.MAX_VALUE))
			 );
			 gl_pannelloFinale.setVerticalGroup(
			 	gl_pannelloFinale.createParallelGroup(Alignment.LEADING)
			 		.addGroup(gl_pannelloFinale.createSequentialGroup()
			 			.addGap(470)
			 			.addComponent(textWin, GroupLayout.PREFERRED_SIZE, 59, GroupLayout.PREFERRED_SIZE)
			 			.addGap(61)
			 			.addComponent(closeButton)
			 			.addContainerGap(67, Short.MAX_VALUE))
			 );
			 
			 pannelloFinale.setLayout(gl_pannelloFinale);
			 getContentPane().add(pannelloFinale, BorderLayout.CENTER); 
			 getContentPane().add(pannelloGenerale, BorderLayout.CENTER);
			 pack();
			 validate();
			 
		 } catch (Exception e) {
			 e.printStackTrace();
		 }
	 }



	 // METODI DI MODIFICA DA PARTE DEL MANAGER O DEL PLAYER
	 
	 
	 public void assegnaIconeAGiocatori(String nome, String carte,JPanel pannello,int master) {
		 pannello.removeAll();
		 JLabel nomeLabel = new JLabel(nome,JLabel.CENTER);
		 JLabel carteLabel = new JLabel(carte,JLabel.CENTER);
		 ImageIcon immagineCarta = new ImageIcon(PlayersUI.class.getResource(imagePath + "retroPiccolissima.jpg"));
		 JLabel immagine = new JLabel(immagineCarta,JLabel.CENTER);
		 nomeLabel.setForeground(Color.WHITE);
		 carteLabel.setForeground(Color.YELLOW);
		// pannello.setMyMaster(master);
		 immagine.setLayout(new BorderLayout());
		 immagine.add(carteLabel,BorderLayout.CENTER);
		 pannello.add(nomeLabel,BorderLayout.NORTH);
		 pannello.add(immagine,BorderLayout.SOUTH);
	 }
	 
	 public void modificaNumeroCarteGiocatore(String nome,String carte,BackgroundedFrame pannello,int master) {
		 pannello.removeAll();
		 JLabel nomeLabel = new JLabel(nome,JLabel.CENTER);
		 JLabel carteLabel = new JLabel(carte,JLabel.CENTER);
		 ImageIcon immagineCarta = new ImageIcon(PlayersUI.class.getResource(imagePath + "retroPiccolissima.jpg"));
		 JLabel immagine = new JLabel(immagineCarta,JLabel.CENTER);
		 nomeLabel.setForeground(Color.WHITE);
		 carteLabel.setForeground(Color.YELLOW);
		 pannello.setMyMaster(master);
		 immagine.setLayout(new BorderLayout());
	     immagine.add(carteLabel,BorderLayout.CENTER);
		 pannello.add(nomeLabel,BorderLayout.NORTH);
		 if (manager.getVisualizzaEvento() == -1) {
			 ImageIcon immagineCentrale = new ImageIcon(PlayersUI.class.getResource(imagePath + "cartello-Divieto.gif"));
			 getSimboloCentrale().setIcon(immagineCentrale);
			 this.setSimboloCentrale(getSimboloCentrale());
		 } else if (manager.getVisualizzaEvento() > 0 ) {
			 getSimboloCentrale().setText("+ " + manager.getVisualizzaEvento());
			 this.setSimboloCentrale(getSimboloCentrale());
		 } else if (manager.getVisualizzaEvento() == -2) {
			 getSimboloCentrale().setVisible(false);
		 }
		 pannello.add(getSimboloCentrale(),BorderLayout.CENTER);
		 pannello.add(immagine,BorderLayout.SOUTH);
		 
	 }
	 
	 public void cancellaSimboloCentrale(JPanel pannello) {
		 pannello.remove(getSimboloCentrale());
	 }
	 
	 
	 public void disegnaTavolo(JPanel pannelloTavolo,PlayerImpl playerImp) {
		// if (getNumberPlayersBefore() == 0 || getNumberPlayersBefore() != manager.getPlayers_cards().size()) {
			 try {
				 	ArrayList<Integer> list = playerImp.getNeighboursID();
				 	ArrayList<Integer> listaVicini = new ArrayList<Integer>();
				 	if (list.size() >= manager.getPlayers_cards().size()) {
				 		for (int i = 0; i < list.size(); i++) {
				 				String prova = manager.getPlayers_names().get(list.get(i));
				 				if (prova != null) {
					 				listaVicini.add(list.get(i));
				 				}
				 		}
				 	}
				 	if (list.size() < manager.getPlayers_cards().size()) {
				 		listaVicini = list;
				 	}
				 	
				 	if(manager.getPlayers_cards().size()==1){
				 		playerImp.deleteTimers();
				 		manager.getToken().setVincitore(manager.getPlayerName());
				 		this.updateFinalTable(manager);
				 	}
			 
				 if (manager.getPlayers_cards().size() == 2) {
					 
						 setNumberPlayersBefore(2);
						 getPannelloNO().setVisible(false);
						 getPannelloNC().setVisible(true);
						 getPannelloNE().setVisible(false);
						 getPannelloCO().setVisible(false);
						 getPannelloCE().setVisible(false);
						 
						 this.assegnaIconeAGiocatori(manager.getPlayers_names().get(listaVicini.get(0)), "Cards " + manager.getPlayers_cards().get(listaVicini.get(0)).size(), getPannelloNC(), listaVicini.get(0));
				 }
				 if (manager.getPlayers_cards().size() == 3) {
						setNumberPlayersBefore(3);
						getPannelloNO().setVisible(false);
						getPannelloNC().setVisible(false);
						getPannelloNE().setVisible(false);
						getPannelloCO().setVisible(true);
						getPannelloCE().setVisible(true);
						
						int idPrimoGiocatore = listaVicini.get(0);
						this.assegnaIconeAGiocatori(manager.getPlayers_names().get(idPrimoGiocatore), "Cards " + manager.getPlayers_cards().get(idPrimoGiocatore).size(), getPannelloCE(), idPrimoGiocatore);			
						int idSecondoGiocatore = listaVicini.get(1);
						this.assegnaIconeAGiocatori(manager.getPlayers_names().get(idSecondoGiocatore), "Cards " + manager.getPlayers_cards().get(idSecondoGiocatore).size(), getPannelloCO(), idSecondoGiocatore);

						
					 
				 }
				 if (manager.getPlayers_cards().size() == 4) {
						setNumberPlayersBefore(4); 
						getPannelloNO().setVisible(false);
						getPannelloNC().setVisible(true);
						getPannelloNE().setVisible(false);
						getPannelloCO().setVisible(true);
						getPannelloCE().setVisible(true);
						
						this.assegnaIconeAGiocatori(manager.getPlayers_names().get(listaVicini.get(0)), "Cards " + manager.getPlayers_cards().get(listaVicini.get(0)).size() , getPannelloCE(), listaVicini.get(0));
						this.assegnaIconeAGiocatori(manager.getPlayers_names().get(listaVicini.get(1)), "Cards " + manager.getPlayers_cards().get(listaVicini.get(1)).size() , getPannelloNC(), listaVicini.get(1));
						this.assegnaIconeAGiocatori(manager.getPlayers_names().get(listaVicini.get(2)), "Cards " + manager.getPlayers_cards().get(listaVicini.get(2)).size() , getPannelloCO(), listaVicini.get(2));



				 }
				 if (manager.getPlayers_cards().size() == 5) {
					 setNumberPlayersBefore(5);
					 getPannelloNO().setVisible(true);
					 getPannelloNC().setVisible(false);
					 getPannelloNE().setVisible(true);
					 getPannelloCO().setVisible(true);
					 getPannelloCE().setVisible(true);
					 
					 this.assegnaIconeAGiocatori(manager.getPlayers_names().get(listaVicini.get(0)), "Cards " + manager.getPlayers_cards().get(listaVicini.get(0)).size(), getPannelloCE(), listaVicini.get(0));
					 this.assegnaIconeAGiocatori(manager.getPlayers_names().get(listaVicini.get(1)), "Cards " + manager.getPlayers_cards().get(listaVicini.get(1)).size(), getPannelloNE(), listaVicini.get(1));
					 this.assegnaIconeAGiocatori(manager.getPlayers_names().get(listaVicini.get(2)), "Cards " + manager.getPlayers_cards().get(listaVicini.get(2)).size(), getPannelloNO(), listaVicini.get(2));
					 this.assegnaIconeAGiocatori(manager.getPlayers_names().get(listaVicini.get(3)), "Cards " + manager.getPlayers_cards().get(listaVicini.get(3)).size(), getPannelloCO(), listaVicini.get(3));



				 }
				 getListaPannelli().add(getPannelloNO());
				 getListaPannelli().add(getPannelloNC());
				 getListaPannelli().add(getPannelloNE());
				 getListaPannelli().add(getPannelloCO());
				 getListaPannelli().add(getPannelloCE());
				 this.setListaPannelli(getListaPannelli());
				 pack();
				 validate();
				 repaint();
				 
				 
		 } catch (Exception e) {
				e.printStackTrace();
		 } 
	 	//}
	 }


	 public void disegnaCarte(ManagerToken manager,boolean myTurn, PlayerImpl impl) {
		 	 this.playerImp = impl;
			 this.manager = manager;
			 JPanel pannelloNomePlayer = this.getNomePanel();
			 pannelloNomePlayer.setLayout(new FlowLayout());
			 name = new JLabel();
			 name.setText(manager.getPlayerName());
			 pannelloNomePlayer.add(name);
		   	this.setNameLabel(name);

			 JPanel pannelloCartaTavolo = this.getCartaTavolo();
			 ImageIcon immagineCarta = new ImageIcon(getClass().getResource(manager.getCartaTavolo().getIcona()));
			 JLabel iconaCarta = new JLabel(immagineCarta);
			 pannelloCartaTavolo.setLayout(new BorderLayout());
			 pannelloCartaTavolo.add(iconaCarta,BorderLayout.CENTER);
			 this.setCartaTavolo(pannelloCartaTavolo);
			  
			 JPanel pannelloTavolo = this.getTavolo();
			 this.createUserIcons(pannelloTavolo);
			 this.disegnaTavolo(pannelloTavolo,playerImp); 
			 
			 this.updatePannelloScore();
			 barraMenu.setVisible(true);

			 if (myTurn) {
				 name.setForeground(Color.RED);
				 name.setText(manager.getPlayerName() + " tocca a te");
				 this.setNameLabel(name);			
				 secondi = new JLabel();
				 secondi.setText("50");
				 this.setSecondi(secondi);
				 JPanel pannelloTimer = this.getTimer();
				 pannelloTimer.setBackground(new Color(95, 186, 85));
				 pannelloTimer.setLayout(new FlowLayout());
				 pannelloTimer.add(secondi);
				 this.setTimer(pannelloTimer);
				 sommaSecondi = 0;

				 JPanel pannelloCarte = this.getCarte(); // prende il pannello con le carte
				 pannelloCarte.setEnabled(true);
				 
			     updateCardMyTurn(pannelloCarte);
			     
				 listaCarteSwing.addMouseListener(new MyMouseEventYourPlay());		
				 
				 countDown = new Timer(1000,new CountDown());
				 countDown.setRepeats(true);
				 countDown.start();
				 
				 this.getNextTurnButton().setEnabled(true);

			 }else {
				 name.setText(manager.getPlayerName());
				 this.setNameLabel(name);
				 pannelloNomePlayer.add(name);
				 JLabel secondiNotMyTurn = new JLabel();
				 secondiNotMyTurn.setText("50");
				 this.setSecondi(secondiNotMyTurn);
				 JPanel pannelloTimer = this.getTimer();
				 pannelloTimer.setLayout(new FlowLayout());
				 pannelloTimer.add(secondiNotMyTurn);
				 this.setTimer(pannelloTimer);
				 				 
				 JPanel pannelloCarte = this.getCarte(); // prende il pannello con le carte
				 pannelloCarte.setEnabled(true);
			
				 			 
				 updateCardNoTurn(pannelloCarte);			 
			 }
		
			
			setVisible(true);
			pack();
			validate();
		
	 }
	 
	 
	 public void updatePannelloMessaggi(JLabel messaggio) {
	
			 JPanel pannelloInfo = this.getInfo();
			 DefaultListModel listModel = this.getListModel();
			 listModel.add(0, messaggio.getText());
			 JList listaMessaggi = new JList(listModel);
			 listaMessaggi.setBackground(pannelloInfo.getBackground());
			 listaMessaggi.setLayoutOrientation( JList.VERTICAL);
			 listaMessaggi.setVisibleRowCount(-1);
			 listaMessaggi.setAlignmentX(JList.CENTER_ALIGNMENT);
			 listaMessaggi.setCellRenderer(new MyCellRendererScoreInfo());
			 JScrollPane infoConteiner = new JScrollPane(listaMessaggi);			
			 Border emptyBorder = BorderFactory.createEmptyBorder();
			 infoConteiner.setBorder(emptyBorder);
			 infoConteiner.setPreferredSize(new Dimension(pannelloInfo.getWidth(), pannelloInfo.getHeight()));
			 pannelloInfo.setLayout(new BorderLayout());
			 pannelloInfo.add(infoConteiner, BorderLayout.CENTER);
			 this.setListModel(listModel);
			 this.setInfo(pannelloInfo);	 
	 }
	 
	 
	 public void updatePannelloScore() {
		 JPanel pannelloScore = this.getScore();
		 pannelloScore.removeAll();
			 
		 try {
		 DefaultListModel listModelScore = new DefaultListModel();
		 nameLH = manager.getPlayers_names();
		 scoreLH = manager.getPlayers_score();
		 Set<Integer> keyName = nameLH.keySet();
		 for(Integer key:keyName){
			 String valueN = nameLH.get(key);
			 Integer valueS = scoreLH.get(key);
			 if(key==playerImp.getId()){
				 listModelScore.addElement("Hai " + valueS + " punti"); 
		 }else{
			 listModelScore.addElement(valueN + ": " + valueS + " punti");}
			 
		 }
		 JList listaScore = new JList(listModelScore);
		 listaScore.setBackground(pannelloScore.getBackground());
		 listaScore.setLayoutOrientation( JList.VERTICAL);
		 listaScore.setVisibleRowCount(-1);
		 listaScore.setSelectionBackground(pannelloScore.getBackground());
		 listaScore.setAlignmentX(JList.CENTER_ALIGNMENT);
		 listaScore.setCellRenderer(new MyCellRendererScoreInfo());
		 JScrollPane infoConteiner = new JScrollPane(listaScore);			
		 Border emptyBorder = BorderFactory.createEmptyBorder();
		 infoConteiner.setBorder(emptyBorder);
		 infoConteiner.setPreferredSize(new Dimension(pannelloScore.getWidth(), pannelloScore.getHeight()));
		 pannelloScore.setLayout(new BorderLayout());
		 pannelloScore.add(infoConteiner, BorderLayout.CENTER);
		 this.setScore(pannelloScore);
		 }catch (Exception e ) {
			 System.out.println("Errore in updatePannelloScore");
			 e.printStackTrace();
		 }
		 
	 }
	 
	 class MyCellRendererScoreInfo extends JLabel implements ListCellRenderer {

		private static final long serialVersionUID = 1L;
		public MyCellRendererScoreInfo() {
	         setOpaque(true);
	     }
	     public Component getListCellRendererComponent(
	         JList list,
	         Object value,
	         int index,
	         boolean isSelected,
	         boolean cellHasFocus)
	     {
	    	 if(value!=null){
	         setText(value.toString());}
	         setBackground(new Color(129, 117, 103));
	         setForeground(Color.WHITE);
	         
	      //   setBackground(isSelected ? Color.red : Color.white);
	      //   setForeground(isSelected ? Color.white : Color.black);
	         return this;
	     }
	 }


	 private void createUserIcons(JPanel pannelloTavolo) {
		 
		 //togliere meno 5 di distanza da top se non va bene
		 this.simboloCentrale = new JLabel();
		 
		 
		 /*
		 pannelloNO = new BackgroundedFrame( new File(PlayersUI.class (imagePath + "userPinkPiccola.png").toString()),true);
		 pannelloNC = new BackgroundedFrame(new File(PlayersUI.class.getResource(imagePath + "userPinkPiccola.png").toString()),true);
		 pannelloNE = new BackgroundedFrame(new File(PlayersUI.class.getResource(imagePath + "userOrangePiccola.png").toString()),true);
		 pannelloCO = new BackgroundedFrame(new File(PlayersUI.class.getResource(imagePath + "userBluPiccola.png").toString()),true);
		 pannelloCE = new BackgroundedFrame(new File(PlayersUI.class.getResource(imagePath + "userGreyPiccola.png").toString()),true);
		 pannelloMio = new BackgroundedFrame(new File(PlayersUI.class.getResource(imagePath + "userRedPiccola.png").toString()),false);*/
		 
		 pannelloNO = new ImagePanelIcon( new ImageIcon(PlayersUI.class.getResource(imagePath + "userPinkPiccola.png")).getImage(),true);
		 pannelloNC = new ImagePanelIcon( new ImageIcon(PlayersUI.class.getResource(imagePath + "userPinkPiccola.png")).getImage(),true);
		 pannelloNE = new ImagePanelIcon( new ImageIcon(PlayersUI.class.getResource(imagePath + "userOrangePiccola.png")).getImage(),true);
		 pannelloCO = new ImagePanelIcon( new ImageIcon(PlayersUI.class.getResource(imagePath + "userBluPiccola.png")).getImage(),true);
		 pannelloCE = new ImagePanelIcon( new ImageIcon(PlayersUI.class.getResource(imagePath + "userGreyPiccola.png")).getImage(),true);
		 pannelloMio = new ImagePanelIcon( new ImageIcon(PlayersUI.class.getResource(imagePath + "userRedPiccola.png")).getImage(),false);
		 
		 pannelloNO.setBackground(pannelloTavolo.getBackground());
		 pannelloNC.setBackground(pannelloTavolo.getBackground());
		 pannelloNE.setBackground(pannelloTavolo.getBackground());
		 pannelloCO.setBackground(pannelloTavolo.getBackground());
		 pannelloCE.setBackground(pannelloTavolo.getBackground());
		 pannelloMio.setBackground(pannelloTavolo.getBackground());
		 pannelloNO.setOpaque(false);
		 pannelloNC.setOpaque(false);
		 pannelloNE.setOpaque(false);
		 pannelloCO.setOpaque(false);
		 pannelloCE.setOpaque(false);
		 pannelloMio.setOpaque(false);
		 pannelloTavolo.setLayout(null);	
		 JPanel pannelloNO = this.getPannelloNO();
		 pannelloTavolo.add(pannelloNO);
		 pannelloNO.setBounds(165, 15, 65, 80);
		 pannelloNO.setLayout(new BorderLayout());
		 pannelloNO.setVisible(false);
		 this.setPannelloNO(pannelloNO);
		 JPanel pannelloNC = this.getPannelloNC();
		 pannelloTavolo.add(pannelloNC);
		 pannelloNC.setBounds(300, 15, 65, 80);
		 pannelloNC.setLayout(new BorderLayout());
		 pannelloNC.setVisible(false);
		 this.setPannelloNC(pannelloNC);
		 JPanel pannelloNE = this.getPannelloNE();
		 pannelloTavolo.add(pannelloNE);
		 pannelloNE.setBounds(430, 15, 67, 80);
		 pannelloNE.setLayout(new BorderLayout());
		 pannelloNE.setVisible(false);
		 this.setPannelloNE(pannelloNE);
		 JPanel panelloCO = this.getPannelloCO();
		 pannelloTavolo.add(pannelloCO);
		 pannelloCO.setBounds(43, 120, 60, 80);
		 pannelloCO.setLayout(new BorderLayout());
		 pannelloCO.setVisible(false);
		 this.setPannelloCO(panelloCO);
		 ImageIcon img = new ImageIcon(PlayersUI.class.getResource(imagePath + "retroPiccola.jpg"));
		 cartaRetro = new JLabel(img);
		 pannelloTavolo.add(cartaRetro);
		 cartaRetro.setBounds(295, 105, 76, 120);
		 JPanel panelloCE = this.getPannelloCE();
		 pannelloTavolo.add(pannelloCE);
		 pannelloCE.setBounds(565, 120, 60, 80);
		 pannelloCE.setLayout(new BorderLayout());
		 pannelloCE.setVisible(false);
		 this.setPannelloCE(panelloCE);
		 JPanel panelloMio = this.getPannelloMio();
		 pannelloTavolo.add(pannelloMio);
		 pannelloMio.setBounds(297, 230, 67, 60);
		 this.setPannelloMio(panelloMio);
		 this.setCartaRetro(cartaRetro);
		 this.setTavolo(pannelloTavolo);
		
	}
	 
	public void  colorRed(){
		JLabel cartaRetroTmp = this.getCartaRetro();
		ImageIcon immagine = (ImageIcon) cartaRetroTmp.getIcon();
		 int borderWidth = 1;
		 int spaceAroundIcon = 0;
		 Color borderColor = Color.RED;
		 BufferedImage bi = new BufferedImage(immagine.getIconWidth() + (2 * borderWidth + 2 * spaceAroundIcon),immagine.getIconHeight() + (2 * borderWidth + 2 * spaceAroundIcon), BufferedImage.TYPE_INT_ARGB);

		 Graphics2D g = bi.createGraphics();
		 g.setColor(borderColor);
		 g.drawImage(immagine.getImage(), borderWidth + spaceAroundIcon, borderWidth + spaceAroundIcon, null);

		 BasicStroke stroke = new BasicStroke(5); //5 pixels wide (thickness of the border)
		 g.setStroke(stroke);

		 g.drawRect(0, 0, bi.getWidth() - 1, bi.getHeight() - 1);
		 g.dispose();
		 immagine = new ImageIcon(bi);
		 cartaRetroTmp.setIcon(immagine);
		 setCartaRetro(cartaRetroTmp);
	}


	public String getOraAttuale()
	{
	java.util.TimeZone t=java.util.TimeZone.getTimeZone("ECT");
	java.util.Calendar oggi = java.util.Calendar.getInstance(t);

	String s = "";
	String secondi = "" + oggi.get(oggi.SECOND);
	String minuti = "" + oggi.get(oggi.MINUTE);
	String ora = "" +oggi.get(oggi.HOUR_OF_DAY);

	if (secondi.length() == 1)
	secondi = "0" + secondi;
	if (minuti.length() == 1)
	minuti = "0" + minuti;
	if (ora.length() == 1)
	ora = "0" + ora;
	s=ora + ":" + minuti + ":" + secondi + " ";

	return s;
	}
	
	public void updateCardMyTurn(JPanel pannelloCarte){
		 int myId;
		 try {
			 myId = playerImp.getId();
		 }catch(Exception e) {
			 myId = 0;
			 e.printStackTrace();
		 }
		 List<Card> list = manager.getMy_cards();
		 DefaultListModel listModel = new DefaultListModel();
		 Iterator<Card> iter = list.iterator();
		 while (iter.hasNext()) {						
			 Card c = (Card) iter.next();						
			 listModel.addElement(c);
		 }

		 listaCarteSwing = new JList(listModel);		
		 listaCarteSwing.setSelectionMode( ListSelectionModel.SINGLE_SELECTION);				
		 listaCarteSwing.setLayoutOrientation( JList.VERTICAL_WRAP);
		 listaCarteSwing.setVisibleRowCount(-1);
		 listaCarteSwing.setAlignmentX(JList.CENTER_ALIGNMENT);
		 JPanel pannellotavolo = this.getTavolo();
		 pannellotavolo.setEnabled(true);
		 
		 if(manager.getToken().getCartaTavolo().getValore()==1){		 
			 if(manager.getToken().getCartaTavolo().isSpecialCard()) 
			 {
			 
			 messaggio.setText(getOraAttuale() + " Sei stato bloccato per un turno");
			 messaggio.setForeground(Color.WHITE);
			 manager.setIdUpdate(myId);
			 manager.getToken().setIdUpdate(myId);
			 manager.setVisualizzaEvento(-1);
			 manager.getToken().setVisualizzaEvento(-1);
			 manager.setMessaggio(getOraAttuale() + manager.getPlayerName() + " e' stato bloccato per un turno");
			 manager.getToken().setMessaggio(getOraAttuale() + manager.getPlayerName() + " e' stato bloccato per un turno");		 
			 manager.getCartaTavolo().setSpecialCard(false);
			 manager.getToken().getCartaTavolo().setSpecialCard(false);		 
		
             this.setMessaggio(messaggio);
			 this.setTavolo(pannellotavolo);
			 listaCarteGiocabili=null;
			 visualizzaCartaSpeciale(1);	
			 }else{
				 listaCarteGiocabili = manager.controllaCartaTavolo(false);
				 setListaCarteGiocabili(listaCarteGiocabili);
				 if(listaCarteGiocabili.size()==0){
					 nextTurnButton.setEnabled(false);
					 colorRed();
					 manager.setCarteDaPrendere(1);
					 messaggio.setText(getOraAttuale() + "Non hai carte giocabili pesca dal mazzo");
					 messaggio.setForeground(Color.WHITE);
					 manager.setIdUpdate(myId);
					 manager.getToken().setIdUpdate(myId);
					 manager.setVisualizzaEvento(1);
					 manager.getToken().setVisualizzaEvento(1);
					 manager.getToken().setMessaggio(getOraAttuale() + manager.getPlayerName()  + " ha preso " + manager.getCarteDaPrendere()  + " carte dal mazzo");
					 manager.setMessaggio(getOraAttuale() + manager.getPlayerName()  + " ha preso " + manager.getCarteDaPrendere()  + " carte dal mazzo");
		             this.setMessaggio(messaggio);
					 this.setTavolo(pannellotavolo);
					 getCartaRetro().addMouseListener(new TakeCardFromTable());
					 listaCarteGiocabili = manager.controllaCartaTavolo(false);
					 setListaCarteGiocabili(listaCarteGiocabili);
				 }
			 }
            } else 
            	if (manager.getToken().getCartaTavolo().getValore()==2) {

            		 if (manager.getToken().getCartaTavolo().isSpecialCard()) {		 	 
            			 manager.getCartaTavolo().setSpecialCard(false);
            			 manager.getToken().getCartaTavolo().setSpecialCard(false);
            			listaCarteGiocabili = manager.controllaCartaTavolo(true);
            			setListaCarteGiocabili(listaCarteGiocabili);
            			manager.setCarteDaPrendere(manager.getCarteDaPrendere() +2);
            			this.setManager(manager);
            				if (listaCarteGiocabili.size() == 0) {
            					    nextTurnButton.setEnabled(false);
            						colorRed();
            						messaggio.setText(getOraAttuale() + "Prendi " + manager.getCarteDaPrendere() + " carte dal mazzo");	
            						manager.setIdUpdate(myId);
            						manager.getToken().setIdUpdate(myId);
            						manager.setVisualizzaEvento(manager.getCarteDaPrendere());
            						manager.getToken().setVisualizzaEvento(manager.getCarteDaPrendere());
            						manager.getToken().setMessaggio(getOraAttuale() + manager.getPlayerName()  + " ha preso " + manager.getCarteDaPrendere()  + " carte dal mazzo");
            						manager.setMessaggio(getOraAttuale() + manager.getPlayerName()  + " ha preso " + manager.getCarteDaPrendere()  + " carte dal mazzo");           						      						
            						this.setTavolo(pannellotavolo);
            			            this.setMessaggio(messaggio);
            						visualizzaCartaSpeciale(2);
            						manager.takeCards();
            						
            						         						
				 }
			 } else {
				 listaCarteGiocabili = manager.controllaCartaTavolo(false);
				 setListaCarteGiocabili(listaCarteGiocabili);
				
				 if(listaCarteGiocabili.size()==0){
					 nextTurnButton.setEnabled(false);
					 colorRed();
					 manager.setCarteDaPrendere(1);
					 messaggio.setText(getOraAttuale() + "Non hai carte giocabili pesca dal mazzo");
					 manager.setIdUpdate(myId);
					 manager.getToken().setIdUpdate(myId);
					 manager.setVisualizzaEvento(1);
					 manager.getToken().setVisualizzaEvento(1);
					 manager.getToken().setMessaggio(getOraAttuale() + manager.getPlayerName()  + " ha preso " + manager.getCarteDaPrendere()  + " carte dal mazzo");
					 manager.setMessaggio(getOraAttuale() + manager.getPlayerName()  + " ha preso " + manager.getCarteDaPrendere()  + " carte dal mazzo");
		             this.setMessaggio(messaggio);
					 this.setTavolo(pannellotavolo);
					 getCartaRetro().addMouseListener(new TakeCardFromTable());
					 listaCarteGiocabili = manager.controllaCartaTavolo(false);
					 setListaCarteGiocabili(listaCarteGiocabili);
				 }					
			 }
			 

		 }else if(manager.getToken().getCartaTavolo().getValore()==9) {
			 if (!manager.getToken().getCartaTavolo().getSeme().equals(manager.getToken().getSemeCorrente())) {
				 	 
				 manager.getCartaTavolo().setSpecialCard(false);
				 messaggio.setText(getOraAttuale() + "Si gioca con il seme: " + manager.getSemeCorrente() );
				 messaggio.setForeground(Color.WHITE);
				 manager.setIdUpdate(myId);
				 manager.getToken().setIdUpdate(myId);
				 manager.setVisualizzaEvento(-2);
				 manager.getToken().setVisualizzaEvento(-2);
				 manager.getToken().setMessaggio(getOraAttuale() + manager.getPlayerName()  + " ha cambiato il seme : " + manager.getSemeCorrente());
				 manager.setMessaggio(getOraAttuale() + manager.getPlayerName()  + " ha cambiato il seme : " + manager.getSemeCorrente());
				 visualizzaCartaSpeciale(9);
			 }else {
				 manager.getCartaTavolo().setSpecialCard(false);
				 messaggio.setText(getOraAttuale() + "Si gioca con il seme: " + manager.getSemeCorrente() );
				 manager.setIdUpdate(myId);
				 manager.getToken().setIdUpdate(myId);
				 manager.setVisualizzaEvento(-2);
				 manager.getToken().setVisualizzaEvento(-2);
				 manager.getToken().setMessaggio(getOraAttuale() + "Si gioca con il seme: " + manager.getSemeCorrente() );
				 manager.setMessaggio(getOraAttuale() + "Si gioca con il seme: " + manager.getSemeCorrente() );	
			 }
             this.setMessaggio(messaggio);
			 this.setTavolo(pannellotavolo);
				 listaCarteGiocabili = manager.controllaCartaTavolo(false);
				 if(listaCarteGiocabili.size()==0){
					 nextTurnButton.setEnabled(false);
					 colorRed();
					 manager.setCarteDaPrendere(1);
					 messaggio.setText(getOraAttuale() + "Non hai carte giocabili pesca dal mazzo");
					 messaggio.setForeground(Color.WHITE);
					 manager.setIdUpdate(myId);
					 manager.getToken().setIdUpdate(myId);
					 manager.setVisualizzaEvento(1);
					 manager.getToken().setVisualizzaEvento(1);
					 manager.getToken().setMessaggio(getOraAttuale() + manager.getPlayerName()  + " ha preso " + manager.getCarteDaPrendere()  + " carte dal mazzo");
					 manager.setMessaggio(getOraAttuale() + manager.getPlayerName()  + " ha preso " + manager.getCarteDaPrendere()  + " carte dal mazzo");		
					 this.setTavolo(pannellotavolo);
					 getCartaRetro().addMouseListener(new TakeCardFromTable());
					 listaCarteGiocabili = manager.controllaCartaTavolo(false);
					 setListaCarteGiocabili(listaCarteGiocabili);
				 }			 

		 } else {
			 listaCarteGiocabili = manager.controllaCartaTavolo(false);
			 setListaCarteGiocabili(listaCarteGiocabili);
			 if(listaCarteGiocabili.size()==0){
				 nextTurnButton.setEnabled(false);
				 colorRed();		
				 manager.setCarteDaPrendere(1);
				 messaggio.setText(getOraAttuale() + "Non hai carte giocabili pesca dal mazzo");
				 messaggio.setForeground(Color.WHITE);
				 manager.setIdUpdate(myId);
				 manager.getToken().setIdUpdate(myId);
				 manager.setVisualizzaEvento(1);
				 manager.getToken().setVisualizzaEvento(1);
				 manager.getToken().setMessaggio(getOraAttuale() + manager.getPlayerName()  + " ha preso " + manager.getCarteDaPrendere()  + " carte dal mazzo");
				 manager.setMessaggio(getOraAttuale() + manager.getPlayerName()  + " ha preso " + manager.getCarteDaPrendere()  + " carte dal mazzo");
		         this.setMessaggio(messaggio);
		         this.setTavolo(pannellotavolo);
				 getCartaRetro().addMouseListener(new TakeCardFromTable());
				 listaCarteGiocabili = manager.controllaCartaTavolo(false);
				 setListaCarteGiocabili(listaCarteGiocabili);
			 } 
		 }
		 manager.setIdUpdate(myId);
		 manager.getToken().setIdUpdate(myId);
		 updateJListMyCard();
		 listaCarteSwing.setCellRenderer(new MyCellRenderer());
		 
		 JScrollPane conteiner = new JScrollPane(listaCarteSwing);	
		 Border emptyBorder = BorderFactory.createEmptyBorder();
		 conteiner.setBorder(emptyBorder);			
		 conteiner.setPreferredSize(new Dimension(900, 177));
		 pannelloCarte.setLayout(new BorderLayout());
		 pannelloCarte.add(conteiner, BorderLayout.CENTER);		 
		 this.setCarte(pannelloCarte);
		 this.updatePannelloMessaggi(messaggio);
		 
		 if (manager.getMytotalScore() >= 105 ) {
			 JOptionPane.showMessageDialog(this,"Hai superato il punteggio di 105! Mi dispiace, HAI PERSO!!!","Inane warning",JOptionPane.WARNING_MESSAGE);
			 if (playerImp.quitClient(true)) {
                 System.exit(0);
             } 
		 }
		 
	 }
	
	
	
	public void visualizzaCartaSpeciale(int carta) {
		
		JPanel pannelloSpecial = this.getPanelSpecial();
		this.setEnabled(true);
		pannelloSpecial.setVisible(true);
		if (carta == 1) {
			ImageIcon immagine = new ImageIcon(PlayersUI.class.getResource(imagePath + "stop.png"));
			JLabel stop = new JLabel(immagine);
			
			pannelloSpecial.setLayout(new BorderLayout());
			pannelloSpecial.add(stop,BorderLayout.CENTER);
			
			this.setPanelSpecial(pannelloSpecial);
			System.out.println("Sono prima del timer");

			TimerTask task = new TimerTaskIcona();
			java.util.Timer timerIcona = new java.util.Timer();
			timerIcona.schedule(task,3000);
		}
		if (carta == 2) {
			JLabel stop = new JLabel("+" + getManager().getCarteDaPrendere(),SwingConstants.CENTER);
			stop.setVerticalAlignment(SwingConstants.CENTER);
			Font f = new Font("Arial", Font.BOLD, 250);
			stop.setText("+" + getManager().getCarteDaPrendere());
			stop.setFont(f);
			stop.setForeground(Color.RED);
			stop.setBounds(295, 105, 76, 120);
			pannelloSpecial.setLayout(new BorderLayout());
			
			this.setPanelSpecial(pannelloSpecial);
			
			Timer coloreTimer = new Timer(1000, new ColoreIcona(stop));
            coloreTimer.setRepeats(true);
            coloreTimer.start();
			TimerTask task = new TimerTaskIcona();
			java.util.Timer timerIcona = new java.util.Timer();
			timerIcona.schedule(task,3000);
		}
		if (carta == 9) {
			ImageIcon immagine = null;
			if (getManager().getSemeCorrente().equals("denari")) {
				immagine = new ImageIcon(PlayersUI.class.getResource(imagePath + "denari.png"));
			}
			if (getManager().getSemeCorrente().equals("coppe")) {
				immagine = new ImageIcon(PlayersUI.class.getResource(imagePath + "coppe.png"));
			}
			if (getManager().getSemeCorrente().equals("spade")) {
				immagine = new ImageIcon(PlayersUI.class.getResource(imagePath + "spade.png"));
			}
			if (getManager().getSemeCorrente().equals("bastoni")) {
				immagine = new ImageIcon(PlayersUI.class.getResource(imagePath + "bastoni.png"));
			}
			JLabel seme = new JLabel(immagine);
			
			pannelloSpecial.setLayout(new BorderLayout());
			pannelloSpecial.add(seme,BorderLayout.CENTER);
			
			this.setPanelSpecial(pannelloSpecial);

			TimerTask task = new TimerTaskIcona();
			java.util.Timer timerIcona = new java.util.Timer();
			timerIcona.schedule(task,3000);
		}
	}
	 
	 public void updateCardNoTurn(JPanel pannelloCarte){
		 List<Card> list = manager.getMy_cards();
		 DefaultListModel listModeltmp = new DefaultListModel();
		 Iterator<Card> iter = list.iterator();
		 while (iter.hasNext()) {						
			 Card c = (Card) iter.next();	
			 listModeltmp.addElement(c);

		 }

		 listaCarteSwing = new JList(listModeltmp);
		 listaCarteSwing.setBackground(new Color(102, 51, 0));
		 listaCarteSwing.setLayoutOrientation( JList.VERTICAL_WRAP);
		 listaCarteSwing.setVisibleRowCount(-1);
		 listaCarteSwing.setAlignmentX(JList.CENTER_ALIGNMENT);
		 listaCarteSwing.setCellRenderer(new MyCellRendererNoTourn());

		 JScrollPane conteiner = new JScrollPane(listaCarteSwing);			
		 Border emptyBorder = BorderFactory.createEmptyBorder();
		 conteiner.setBorder(emptyBorder);
		 conteiner.setPreferredSize(new Dimension(900, 177));
		 pannelloCarte.setLayout(new BorderLayout());
		 pannelloCarte.add(conteiner, BorderLayout.CENTER);
		 this.setCarte(pannelloCarte);
		 
	 }


	 class MyCellRenderer extends JLabel implements ListCellRenderer{

		 private static final long serialVersionUID = 1L;
		 ArrayList<Card> listaCarteGiocabili = getListaCarteGiocabili();
         
		 public Component getListCellRendererComponent(
				 JList list,
				 Object value, 
				 int index, 
				 boolean isSelected,
				 boolean cellHasFocus) {
			 	     
			 Card c = (Card) value;

			 if(listaCarteGiocabili == null){

				 ImageIcon immagine = new ImageIcon(getClass().getResource(c.getIcona()));
				 setIcon(immagine);	
				 setEnabled(false);

			 } else {

				 boolean isIn = false;
				 for (Card card : listaCarteGiocabili)	 {
					 if (c.equalsCards(card)){
						 isIn = true;
						 break;
					 }
				 }

				 ImageIcon immagine = new ImageIcon(getClass().getResource(c.getIcona()));

				 if (isSelected && isReallySelected()) {
						 int borderWidth = 1;
						 int spaceAroundIcon = 0;
						 Color borderColor = Color.YELLOW;
						 BufferedImage bi = new BufferedImage(immagine.getIconWidth() + (2 * borderWidth + 2 * spaceAroundIcon),immagine.getIconHeight() + (2 * borderWidth + 2 * spaceAroundIcon), BufferedImage.TYPE_INT_ARGB);

						 Graphics2D g = bi.createGraphics();
						 g.setColor(borderColor);
						 g.drawImage(immagine.getImage(), borderWidth + spaceAroundIcon, borderWidth + spaceAroundIcon, null);

						 BasicStroke stroke = new BasicStroke(5); //5 pixels wide (thickness of the border)
						 g.setStroke(stroke);

						 g.drawRect(0, 0, bi.getWidth() - 1, bi.getHeight() - 1);
						 g.dispose();
						 immagine = new ImageIcon(bi);
				 }

				 setIcon(immagine);

				 if (isIn) {
				 	setEnabled(true);
				 	setOpaque(false);
				 } else {
				 	setEnabled(false);
				 }
			 }

			 Border insideBorder = BorderFactory.createEmptyBorder(5, 20, 5, 20);				
			 setBorder(insideBorder);
			 list.setBackground(new Color(102, 51, 0));
			 
			 return this; 
		 }

	 }


	 class MyCellRendererNoTourn extends JLabel implements ListCellRenderer{

		 private static final long serialVersionUID = 1L;


		 public Component getListCellRendererComponent(
				 JList list,
				 Object value, 
				 int index, 
				 boolean isSelected,
				 boolean cellHasFocus) {

			 Card c=(Card) value;
			 String img = c.getIcona();

			 ImageIcon immagine = new ImageIcon(getClass().getResource(img));
			 setIcon(immagine);
			 new Dimension(113,170);
			 setEnabled(true);
			 setOpaque(false);

			 Border insideBorder = BorderFactory.createEmptyBorder(5, 20, 5, 20);				
			 setBorder(insideBorder);
			 list.setBackground(new Color(102, 51, 0));

			 return this; 
		 }

	 }



	 public void cancellaPannelloIniziale() {
		 this.remove(this.pannelloIniziale);
		 this.nextTurnButton.setVisible(true);
		 this.nextTurnButton.setEnabled(false);
		 this.pack();
		 this.validate();
		 this.repaint();
	 }


	 public void yourTurn(ManagerToken manager) {
		 
	
		 JPanel pannelloNomePlayer = this.getNomePanel();
		 pannelloNomePlayer.setLayout(new FlowLayout());
		 pannelloNomePlayer.remove(getNameLabel());
		 JLabel nameNew = new JLabel();
		 nameNew.setText(manager.getPlayerName());
		 nameNew.setForeground(Color.RED);
		 nameNew.setText(manager.getPlayerName() + " tocca a te");
		 pannelloNomePlayer.add(nameNew);
		 this.setNameLabel(nameNew);
		 this.setNomePanel(pannelloNomePlayer);
		 	
		 JPanel pannelloTimer = this.getTimer();
		 JLabel timerVecchio = this.getSecondi();
		 pannelloTimer.remove(timerVecchio);
		 pannelloTimer.setLayout(new FlowLayout());
		 JLabel secondiNew = new JLabel();
		 secondiNew.setText("50");	 
		 pannelloTimer.setBackground(new Color(95, 186, 85));
		 pannelloTimer.add(secondiNew);
		 this.setSecondi(secondiNew);
		 this.setTimer(pannelloTimer);
		 
		 JPanel pannelloCartaTavolo = this.getCartaTavolo();
		 pannelloCartaTavolo.removeAll();
		 ImageIcon immagineCarta = new ImageIcon(PlayersUI.class.getResource(manager.getCartaTavolo().getIcona()));
		 JLabel iconaCarta = new JLabel(immagineCarta);
		 pannelloCartaTavolo.setLayout(new BorderLayout());
		 pannelloCartaTavolo.add(iconaCarta,BorderLayout.CENTER);
		 this.setCartaTavolo(pannelloCartaTavolo);
		  
		 JPanel pannelloTavoloVecchio = this.getTavolo();
		 this.disegnaTavolo(pannelloTavoloVecchio, playerImp);
		 this.setTavolo(pannelloTavoloVecchio);
		 
		 JPanel pannelloCarte = this.getCarte();			
		 pannelloCarte.setEnabled(true);
		 pannelloCarte.setBackground(new Color(102, 51, 0));
		 pannelloCarte.removeAll();	    	
		    		 
		 this.updateTableScoreInfo(manager);
	     updateCardMyTurn(pannelloCarte);
  
		 listaCarteSwing.addMouseListener(new MyMouseEventYourPlay());
		 	 
		 sommaSecondi = 0;
		 countDown = new Timer(1000,new CountDown());
		 countDown.setRepeats(true);		
		 countDown.start();		
		 
		 nextTurnButton.setEnabled(true);
		 
		 setVisible(true);
		 pack();
		 validate();
		 repaint();

	 }

	 /**Aggiorna la JList delle mie carte.*/  
	 public void updateJListMyCard(){
		 JPanel pannelloCarte = this.getCarte();
		 pannelloCarte.removeAll();
		 List<Card> list_new = manager.getMy_cards();
		 DefaultListModel listModel_new = new DefaultListModel();
		 Iterator<Card> iter_new = list_new.iterator();
		 while (iter_new.hasNext()) {						
			 Card c = (Card) iter_new.next();						
			 listModel_new.addElement(c);
		 }

		 listaCarteSwing = new JList(listModel_new);		
		 listaCarteSwing.setSelectionMode( ListSelectionModel.SINGLE_SELECTION);				
		 listaCarteSwing.setLayoutOrientation( JList.VERTICAL_WRAP);
		 listaCarteSwing.setVisibleRowCount(-1);
		 listaCarteSwing.setAlignmentX(JList.CENTER_ALIGNMENT);
	 }

	 public class MyMouseEventYourPlay extends  MouseAdapter {

		 public void mouseClicked(MouseEvent e) {
			 List<Card> list = getManager().getMy_cards();
			 if(SwingUtilities.isLeftMouseButton(e)) {
				 JList listaCarteTempSwing = (JList) e.getSource();
				  Point punto = e.getPoint();
				 int index = listaCarteTempSwing.locationToIndex(punto);
				 if(index>=0){
					 
					 Rectangle rettangoloCella = listaCarteTempSwing.getCellBounds(index, index);

					 boolean tmp = false;
		                if (rettangoloCella.contains(punto)) {
	                		 setReallySelected(true);
		                	 if (getManager().getMy_cards().size() == 2 && getListaCarteGiocabili().size() > 0) {
								 getCintocinqueButton().setVisible(true);
								 getCintocinqueButton().addActionListener(new ClickShoutButton());
								 getCintocinqueButton().setEnabled(true);
								 setShoutDone(false);}
								 
								 Card c=list.get(index);
								 Iterator<Card> iter = getListaCarteGiocabili().iterator();
								
								 
								 while(iter.hasNext()){
									 Card ca= iter.next();
									 
									 if(c.equalsCards(ca)){
										// System.out.println("Double Click!!!");
										 setIndiceCartaSelezionata(index);	
										 nextTurnButton.setEnabled(true);
										 tmp=true;
									 }
									 
								 }
												
		                }
		                if(!tmp){
						//	 System.out.println("Click!!!");
							 setIndiceCartaSelezionata(-1);
							 setReallySelected(false);
							 nextTurnButton.setEnabled(true);
						 }
					 
					
				 }

			 }
		 }
	 }
	 
	 public class TakeCardFromTable extends  MouseAdapter {

		 public void mouseClicked(MouseEvent e) {
			 if(SwingUtilities.isLeftMouseButton(e)) {
				 JLabel cartaSulTavolo = (JLabel) e.getSource();
				 ImageIcon immagine = (ImageIcon) cartaSulTavolo.getIcon();
				 int borderWidth = 1;
				 int spaceAroundIcon = 0;
				 Color borderColor = Color.YELLOW;
				 BufferedImage bi = new BufferedImage(immagine.getIconWidth() + (2 * borderWidth + 2 * spaceAroundIcon),immagine.getIconHeight() + (2 * borderWidth + 2 * spaceAroundIcon), BufferedImage.TYPE_INT_ARGB);

				 Graphics2D g = bi.createGraphics();
				 g.setColor(borderColor);
				 g.drawImage(immagine.getImage(), borderWidth + spaceAroundIcon, borderWidth + spaceAroundIcon, null);

				 BasicStroke stroke = new BasicStroke(5); //5 pixels wide (thickness of the border)
				 g.setStroke(stroke);

				 g.drawRect(0, 0, bi.getWidth() - 1, bi.getHeight() - 1);
				 g.dispose();
				 immagine = new ImageIcon(bi);
				 cartaSulTavolo.setIcon(immagine);
				 setCartaRetro(cartaSulTavolo);
				 getManager().takeCards();
				 cartaSulTavolo.removeMouseListener(cartaSulTavolo.getMouseListeners()[0]);
				 updateJListMyCard();
				 listaCarteGiocabili = getManager().controllaCartaTavolo(false);
				 setListaCarteGiocabili(listaCarteGiocabili);
				 listaCarteSwing.setCellRenderer(new MyCellRenderer());
				 
				 JScrollPane conteiner = new JScrollPane(listaCarteSwing);	
				 Border emptyBorder = BorderFactory.createEmptyBorder();
				 conteiner.setBorder(emptyBorder);			
				 conteiner.setPreferredSize(new Dimension(9000, 177));
				 getCarte().setLayout(new BorderLayout());
				 getCarte().add(conteiner, BorderLayout.CENTER);		 

				 setCarte(getCarte());
				
				 listaCarteSwing.removeMouseListener(listaCarteSwing.getMouseListeners()[0]);
				 listaCarteSwing.addMouseListener(new MyMouseEventYourPlay());
				 nextTurnButton.setEnabled(true);
			 }
		 }
	 }

	 /**Aggiorna il Tavolo.*/  
	 public void updateCardOnTable(ManagerToken manager){
		 JPanel pannelloCartaTavolo = this.getCartaTavolo();
		 pannelloCartaTavolo.removeAll();
		 ImageIcon immagineCarta = new ImageIcon(PlayersUI.class.getResource(manager.getCartaTavolo().getIcona()));
		 JLabel iconaCarta = new JLabel(immagineCarta);
		 pannelloCartaTavolo.setLayout(new BorderLayout());
		 pannelloCartaTavolo.add(iconaCarta,BorderLayout.CENTER);
		 this.setCartaTavolo(pannelloCartaTavolo);
		 pack();
		 validate();
		 repaint();
	 }
	 
	 public void updateFinalTable(ManagerToken manager){
		 JPanel pannelloFinaleVecchio = this.getPannelloFinale();
		 pannelloFinaleVecchio.setVisible(true);
		 textWin.setText(manager.getToken().getVincitore() + " ha vinto...!!!");
		 this.setPannelloFinale(pannelloFinaleVecchio);
		 closeButton.setVisible(true);
		 JPanel pannelloGeneraleVecchio = this.getPannelloGenerale();
		 pannelloGeneraleVecchio.setVisible(false);
		 pack();
		 validate();
		 repaint();
	 }
	 
	 public void updateTableScoreInfo(ManagerToken manager){
		 JPanel pannelloTavoloVecchio = this.getTavolo();
		 this.disegnaTavolo(pannelloTavoloVecchio, playerImp);
		 this.setTavolo(pannelloTavoloVecchio);
		 JLabel mes =new JLabel();
		 mes.setText(manager.getMessaggio());		 
		 updatePannelloMessaggi(mes);
		// setMessaggio(mes);
         updatePannelloScore();
		 pack();
		 validate();
		 repaint();
	 }
	 


	 // metodo richiamato dall'azione del bottone Next Turn per far vedere all'utente il cambio di turno
	 public void passTurn () {
		 sommaSecondi = 0;
		 this.nextTurnButton.setEnabled(false);
		 JPanel pannelloNome = this.getNomePanel();
		 JLabel nameVecchio = this.getNameLabel();
		 pannelloNome.remove(nameVecchio);
		 JLabel nameNew = new JLabel();
		 nameNew.setText(manager.getPlayerName());
		 nameNew.setForeground(Color.BLACK);
		 this.setNameLabel(nameNew);
		 pannelloNome.add(nameNew);
		 this.setNomePanel(pannelloNome);
		 JLabel cartaRetro = this.getCartaRetro();
		 cartaRetro.setBorder(null);
		 this.setCartaRetro(cartaRetro);
		 
		 
		 JLabel timerNotMyturn = new JLabel();
		 timerNotMyturn.setText("50");
		 timerNotMyturn.validate();
		 JLabel vecchioTimer = this.getSecondi();
		 JPanel pannelloTimer = this.getTimer();
		 pannelloTimer.setBackground(new Color(240, 240, 240));
		 pannelloTimer.remove(vecchioTimer);
		 setSecondi(timerNotMyturn);
		 pannelloTimer.add(timerNotMyturn);
		 pannelloTimer.validate();
		 this.setTimer(pannelloTimer);
		 
		 JPanel pannelloTavoloVecchio = this.getTavolo();
		 
		 this.updatePannelloScore();
		 manager.setVisualizzaEvento(-2); 
		 pannelloTavoloVecchio.remove(getCartaRetro());
		 ImageIcon img = new ImageIcon(PlayersUI.class.getResource(imagePath + "retroPiccola.jpg"));
		 cartaRetro = new JLabel(img);		
		 pannelloTavoloVecchio.add(cartaRetro);
		 cartaRetro.setBounds(295, 105, 76, 120);
		 this.setCartaRetro(cartaRetro);
		 this.setTavolo(pannelloTavoloVecchio);
		 
		 JPanel pannelloCartaSulTavolo = this.getCartaTavolo();
		 Card ultimaCarta = manager.getCartaTavolo();
		 ImageIcon immagine = new ImageIcon(PlayersUI.class.getResource(ultimaCarta.getIcona()));
		 JLabel pic = new JLabel(immagine);
		 pannelloCartaSulTavolo.setLayout(new BorderLayout());
		 pannelloCartaSulTavolo.add(pic,BorderLayout.CENTER);
		 this.setCartaTavolo(pannelloCartaSulTavolo);

		 JPanel pannelloCarte = this.getCarte();
		 pannelloCarte.setEnabled(false);
		 pannelloCarte.removeAll();
		 this.getCintocinqueButton().setVisible(false);
		 this.getCintocinqueButton().setEnabled(false);
		 
		 updateCardNoTurn(pannelloCarte);
		 JLabel mess= this.getMessaggio();
		 mess.setText("");
		 pack();
		 validate();
		 repaint();
	 }


	 // AZIONI DELL'INTERFACCIA


	 public void actionPerformed(ActionEvent evt) {
		 if (evt.getActionCommand().equals("quit")) {
			 dispatchEvent(new WindowEvent(this, WindowEvent.WINDOW_CLOSING));
		 } 
	 }

	 class ClickNextTurn implements ActionListener {
		 public void actionPerformed(ActionEvent e) {
			 countDown.stop();
			 if (manager.getMy_cards().size() == 2 && (getListaCarteGiocabili()!= null) ) {
				 if (getListaCarteGiocabili().size() > 0) {
					 if (isShoutDone() == false) {
						 manager.setCarteDaPrendere(5);
						 manager.takeCards();
						 updateJListMyCard();
						 manager.setCarteDaPrendere(0);
					 } 
				 }
			 }
			 
			 int index = getIndiceCartaSelezionata();
			 			 
			 if (index >= 0) {
				 
			
				 
				 if(manager.getMy_cards().size()==1){	
					 		 
					 pannelloFinale.setVisible(true);	
					 manager.getToken().setWinner(true);
					 manager.setWinner(true);	 
					 manager.getToken().setVincitore(manager.getPlayerName());
					 manager.setVincitore(manager.getPlayerName());
					 getVittoria().setText("Il giocatore" + manager.getPlayerName() + "ha vinto...!!!" );
					 pack();
					 validate();
					 repaint();
				 }else{
				 
				 Card lastcard = manager.getCartaTavolo();
				 lastcard.reset();
				 manager.getMazzo().addCarteMazzo(lastcard);
				 List<Card> list = manager.getMy_cards();
				 Card c = list.get(index);
				 if(c.getValore()==1 || c.getValore()==2 || c.getValore()==9){
		    		 c.setSpecialCard(true);	
		    	 }else{
		    		 String message ="";
		    		 manager.setVisualizzaEvento(-2);
		    		 manager.getToken().setVisualizzaEvento(-2);
		    		 manager.setMessaggio(message);
					 manager.getToken().setMessaggio(message);	
		    	 }
				 if (c.getValore()== 9) {	 
 					 
					 Object[] options = {"Denari","Spade","Bastoni", "Coppe"};
					 int n = JOptionPane.showOptionDialog(null,"Scegli il seme","Puoi cambiare il seme",JOptionPane.YES_NO_CANCEL_OPTION,JOptionPane.QUESTION_MESSAGE,null,options,options[0]);
					 if (n == 0) {
						 manager.setSemeCorrente("denari");
					 }
					 if (n == 1) {
						 manager.setSemeCorrente("spade");
					 }
					 if (n == 2) {
						 manager.setSemeCorrente("bastoni");
					 }
					 if (n == 3) {
						 manager.setSemeCorrente("coppe");
					 }
					 					 
				 }else {
					 manager.setSemeCorrente(c.getSeme()); 
				 }
				 
				
				 c.reset();
				 manager.setCartaTavolo(c);
				 int score = manager.getMytotalScore() - c.getScore();
				 manager.setMytotalScore(score);
                 System.out.println("Update mytotalScore: " + manager.getMytotalScore());
                 try {
					 int id =playerImp.getId();
					 manager.getPlayers_score().put(id, manager.getMytotalScore());
					} catch (RemoteException e1) {	
						e1.printStackTrace();
					}
                 
				 manager.getMy_cards().remove(index);
				 try {
					 int id =playerImp.getId();
					 manager.getPlayers_cards().put(id, (ArrayList<Card>) manager.getMy_cards());
					} catch (RemoteException e1) {	
						e1.printStackTrace();
					}
				 setIndiceCartaSelezionata(-1);
			   }
			 }
			 manager.updateTokenFromLocal();
			 passTurn();
			 playerImp.nextTurnClient(getManager());
			
		 }
	 }
	 
	 
	 class ClickShoutButton implements ActionListener {
		 public void actionPerformed(ActionEvent e) {
			 setShoutDone(true);
			 messaggio.setText(getOraAttuale() + "Penultima carta...");
			 messaggio.setForeground(Color.WHITE);
			 manager.getToken().setMessaggio(getOraAttuale() +manager.getPlayerName() + " ha fatto Penultima");
			 manager.setMessaggio(getOraAttuale() + manager.getPlayerName() + " ha fatto Penultima");
			 setMessaggio(messaggio);
			 updatePannelloMessaggi(messaggio);
			 pack();
			 validate();
			 repaint();
		 }
	 }


	 class CountDown implements ActionListener {
		 public void actionPerformed(ActionEvent e) {
			 sommaSecondi++;
			 JPanel pannello = getTimer();
			 //pannello.remove(secondi);
			 Integer secDaVisualizzare = 50 - sommaSecondi;
			 String visualizzaSecondi = secDaVisualizzare.toString();
			 JLabel secTimer = getSecondi();
			 secTimer.setText(visualizzaSecondi);
			 if (secDaVisualizzare <= 10) {
				 secTimer.setForeground(Color.RED);
				 setSecondi(secTimer);

			 }
			 pannello.add(secTimer);
			 setTimer(pannello);
			 pack();
			 validate();
			 repaint();
			 if (secDaVisualizzare == 0) {
				 countDown.stop();
				 sommaSecondi = 0;
				 setSecondi(secTimer);
				 //TODO qui va inserito la mossa automatica di scelta delle carte
				 passTurn();
				 playerImp.nextTurnClient(getManager());
			 }

		 }
	 }

	 
	 
	 class ColoreIcona implements ActionListener {
		 
		 JLabel label;
		 int count=0;
		 
		 public ColoreIcona(JLabel label) {
			 this.label = label;
		 }
		 
		 public void actionPerformed(ActionEvent e) {
			 JPanel pannelloSpecial = getPanelSpecial();
			 count=count + 85;
			 label.setForeground(new Color(255, 0, 0, 255-count));
			 pannelloSpecial.add(label,BorderLayout.CENTER);
			 label.setAlignmentX(CENTER_ALIGNMENT);

		 }
	 }
	 
	 
	 public class TimerTaskIcona extends TimerTask {
		 private TimerTaskIcona () {
			 
		 }

		public void run() {
			JPanel pannelloSpecial = getPanelSpecial();
			pannelloSpecial.removeAll();
			pannelloSpecial.setVisible(false);
			pannelloSpecial.setEnabled(false);
			setPanelSpecial(pannelloSpecial);
			
		}
		 
		 
	 }


	 // GET AND SET


	 public JButton getCintocinqueButton() {
		 return centocinqueButton;
	 }

	 public void setCintocinqueButton(JButton cintocinqueButton) {
		 this.centocinqueButton = cintocinqueButton;
	 }


	public JButton getNextTurnButton() {
		 return nextTurnButton;
	 }

	 public JPanel getPannelloMessaggi() {
		return pannelloMessaggi;
	}


	public void setPannelloMessaggi(JPanel pannelloMessaggi) {
		this.pannelloMessaggi = pannelloMessaggi;
	}


	public JLabel getCartaRetro() {
		return cartaRetro;
	}


	public boolean isReallySelected() {
		return reallySelected;
	}


	public void setReallySelected(boolean reallySelected) {
		this.reallySelected = reallySelected;
	}


	public int getNumberPlayersBefore() {
		return numberPlayersBefore;
	}


	public void setNumberPlayersBefore(int numberPlayersBefore) {
		this.numberPlayersBefore = numberPlayersBefore;
	}


	public boolean isShoutDone() {
		return shoutDone;
	}


	public void setShoutDone(boolean shoutDone) {
		this.shoutDone = shoutDone;
	}


	public void setCartaRetro(JLabel cartaRetro) {
		this.cartaRetro = cartaRetro;
	}


	public void setNextTurnButton(JButton nextTurnButton) {
		 this.nextTurnButton = nextTurnButton;
	 }

	 public javax.swing.JPanel getPannelloBottone() {
		 return pannelloBottone;
	 }

	 public void setPannelloBottone(javax.swing.JPanel pannelloBottone) {
		 this.pannelloBottone = pannelloBottone;
	 }

	 public javax.swing.JPanel getPannelloGenerale() {
		 return pannelloGenerale;
	 }

	 public javax.swing.JPanel getNomePanel() {
		 return nomePanel;
	 }

	 public JLabel getMessaggio() {
		 return messaggio;
	 }


	 public void setMessaggio(JLabel messaggio) {
		 this.messaggio = messaggio;
	 }


	 public void setNomePanel(javax.swing.JPanel nomePanel) {
		 this.nomePanel = nomePanel;
	 }

	 public void setPannelloGenerale(javax.swing.JPanel pannelloGenerale) {
		 this.pannelloGenerale = pannelloGenerale;
	 }


	 public Player getPlayer() {
		 return player;
	 }

	 public void setPlayer(Player player) {
		 this.player = player;
	 }

	 public ArrayList<Card> getListaCarteGiocabili() {
		 return listaCarteGiocabili;
	 }


	 public void setListaCarteGiocabili(ArrayList<Card> listaCarteGiocabili) {
		 this.listaCarteGiocabili = listaCarteGiocabili;
	 }


	 public javax.swing.JPanel getCarte() {
		 return this.carte;
	 }

	 public int getIndiceCartaSelezionata() {
		 return indiceCartaSelezionata;
	 }


	 public JPanel getInfo() {
		return info;
	}


	public void setInfo(JPanel info) {
		this.info = info;
	}


	public JPanel getScore() {
		return score;
	}


	public void setScore(JPanel score) {
		this.score = score;
	}


	public void setIndiceCartaSelezionata(int cartaDaGiocare) {
		 this.indiceCartaSelezionata = cartaDaGiocare;
	 }


	 public JLabel getSecondi() {
		 return secondi;
	 }


	 public void setSecondi(JLabel secondi) {
		 this.secondi = secondi;
	 }


	 public void setCarte(javax.swing.JPanel carte) {
		 this.carte = carte;
	 }

	 public javax.swing.JMenuItem getEsciMenu() {
		 return esciMenu;
	 }

	 public void setEsciMenu(javax.swing.JMenuItem esciMenu) {
		 this.esciMenu = esciMenu;
	 }

	 public javax.swing.JMenu getFileMenuBar() {
		 return fileMenuBar;
	 }

	 public void setFileMenuBar(javax.swing.JMenu fileMenuBar) {
		 this.fileMenuBar = fileMenuBar;
	 }


	 public javax.swing.JPanel getTavolo() {
		 return tavolo;
	 }

	 public void setTavolo(javax.swing.JPanel tavolo) {
		 this.tavolo = (ImagePanel) tavolo;
	 }

	 public javax.swing.JPanel getTimer() {
		 return timer;
	 }

	 public void setTimer(javax.swing.JPanel timer) {
		 this.timer = timer;
	 }

	 public javax.swing.JPanel getCartaTavolo() {
		 return cartaTavolo;
	 }

	 public void setCartaTavolo(JPanel cartaTavolo) {
		 this.cartaTavolo = cartaTavolo;
	 }

	 public void setPannelloIniziale(JPanel pannelloIniziale) {
		 this.pannelloIniziale = pannelloIniziale;
	 }

	 public JPanel getPannelloIniziale() {
		 return pannelloIniziale;
	 }


	 public PlayerImpl getPlayerImp() {
		 return playerImp;
	 }


	 public void setPlayerImp(PlayerImpl playerImp) {
		 this.playerImp = playerImp;
	 }


	 public ManagerToken getManager() {
		 return manager;
	 }


	 public void setManager(ManagerToken manager) {
		 this.manager = manager;
	 }


	 public JLabel getNameLabel() {
		 return name;
	 }


	 public void setNameLabel(JLabel name) {
		 this.name = name;
	 }


	 public JList getListaCarteSwing() {
		 return listaCarteSwing;
	 }


	 public void setListaCarteSwing(JList listaCarteSwing) {
		 this.listaCarteSwing = listaCarteSwing;
	 }


	 public JPanel getPannelloNO() {
			return pannelloNO;
		}

	
		public void setPannelloNO(JPanel pannelloNO) {
			this.pannelloNO = pannelloNO;
		}
	
	
		public JPanel getPannelloNC() {
			return pannelloNC;
		}
	
	
		public void setPannelloNC(JPanel pannelloNC) {
			this.pannelloNC = pannelloNC;
		}
	
	
		public JPanel getPannelloNE() {
			return pannelloNE;
		}
	
	
		public void setPannelloNE(JPanel pannelloNE) {
			this.pannelloNE = pannelloNE;
		}
	
	
		public JPanel getPannelloCO() {
			return pannelloCO;
		}
	
	

		public void setPannelloCO(JPanel pannelloCO) {
			this.pannelloCO = pannelloCO;
		}
	
	
		public JPanel getPannelloCE() {
			return pannelloCE;
		}
	
	
		public void setPannelloCE(JPanel pannelloCE) {
			this.pannelloCE = pannelloCE;
		}
	
	
	
	
		public JPanel getPannelloMio() {
			return pannelloMio;
		}
	
	
		public void setPannelloMio(JPanel pannelloMio) {
			this.pannelloMio = pannelloMio;
		}


		public JPanel getPannelloFinale() {
			return pannelloFinale;
		}


		public void setPannelloFinale(JPanel pannelloFinale) {
			this.pannelloFinale = pannelloFinale;
		}


		public void setListaPannelli(ArrayList<JPanel> listaPannelli) {
			this.listaPannelli = listaPannelli;
		}


		public ArrayList<JPanel> getListaPannelli() {
			return listaPannelli;
		}


		public void setSimboloCentrale(JLabel simboloCentrale) {
			this.simboloCentrale = simboloCentrale;
		}


		public DefaultListModel getListModel() {
			return listModel;
		}


		public void setListModel(DefaultListModel listModel) {
			this.listModel = listModel;
		}


		public JLabel getSimboloCentrale() {
			return simboloCentrale;
		}


		public JPanel getPanelSpecial() {
			return panelSpecial;
		}


		public void setPanelSpecial(JPanel panelSpecial) {
			this.panelSpecial = panelSpecial;
		}


		public JLabel getVittoria() {
			return textWin;
		}


		public void setVittoria(JLabel vittoria) {
			this.textWin = vittoria;
		}
}
