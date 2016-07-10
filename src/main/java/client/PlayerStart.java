package client;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.rmi.RMISecurityManager;
import java.rmi.RemoteException;
import java.util.logging.ConsoleHandler;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import manager.ManagerToken;
import client.ui.PlayerSettingsUI;
import client.ui.PlayersUI;
import javax.swing.border.TitledBorder;
import java.awt.Color;
import javax.swing.border.BevelBorder;

public class PlayerStart implements ActionListener {
	
	private PlayersUI player_ui ;
	private PlayerSettingsUI settings_ui;
	private PlayerImpl player;
	private ManagerToken manager;
	private Logger logger;
	
	public PlayerStart(){
		super();
		settings_ui = new PlayerSettingsUI();
		player_ui = new PlayersUI(player);
		player_ui.getTimer().setBorder(new TitledBorder(new BevelBorder(BevelBorder.LOWERED, null, null, null, null), "Timer", TitledBorder.LEFT, TitledBorder.TOP, null, null));
		player_ui.getCartaTavolo().setBorder(new TitledBorder(new BevelBorder(BevelBorder.LOWERED, null, null, null, null), "Carta", TitledBorder.LEFT, TitledBorder.TOP, null, null));
		player_ui.getTavolo().setBorder(new TitledBorder(new BevelBorder(BevelBorder.LOWERED, null, null, null, null), "Tavolo", TitledBorder.LEFT, TitledBorder.TOP, null, new Color(0, 0, 0)));
		player_ui.getCarte().setBorder(new TitledBorder(new BevelBorder(BevelBorder.LOWERED, null, null, null, null), "Carte", TitledBorder.LEFT, TitledBorder.TOP, null, new Color(0, 0, 0)));
		logger = Logger.getLogger("game.player.ui");
		manager=null;
		settings_ui.getStartGame().addActionListener(this);
        settings_ui.setVisible(true);
        
        class WindowCloseListener extends WindowAdapter {
            public void windowClosing(WindowEvent evt) {
                logger.info("quitting...");
                if (player.quitClient(true)) {
                    System.exit(0);
                } 
            }
        }
        player_ui.addWindowListener(new WindowCloseListener()); 
	}
	
	 

	public void actionPerformed(ActionEvent evt) {
		  String command = evt.getActionCommand();
	        if (command.equals("login")) {
	            String serverURL = "//localhost:1099/105";
	            String playerName = "";                  
	            if (!settings_ui.getServerURL().equals("")) {
	                serverURL = "//" + settings_ui.getServerURL() +":"+ settings_ui.getPortaTF() +  "/"+ settings_ui.getTavoloTF();
	            }        
	            // Mi salvo il nome del giocatore prese dalla gui
	            playerName = settings_ui.getPlayerName();
	            if(playerName.equals("")){
 	            	settings_ui.popUpControllaNome();
 	            }else{           
	            initialize(serverURL, playerName);}
	        }	
	}
	
	  private void initialize(String server_url, String player_name) {
	        try {
	            // creiamo il game manager
	            manager = new ManagerToken();
	            ManagerToken.setInstance(manager);
	            manager.setPlayerName(player_name);
	            // creiamo il giocatore
	            player = new PlayerImpl();
	            player.setPlayerName(player_name);
	            boolean tmp= player.initialize(server_url);
	            if(tmp){
	            // mostriamo la gui iniziale...
	            settings_ui.setVisible(false);
	            player_ui.setVisible(true);
	            player.setVista(player_ui);
	            }else{
	            	settings_ui.popUpGiocoIniziato();
	            }
	                        		
	        } catch (RemoteException e) {
	            logger.log(Level.SEVERE, "remote exception!", e);
	            JOptionPane.showMessageDialog(null, "Errore nella connessione al server:\n");
	            settings_ui.setVisible(true);
	            // System.exit(-1);
	        } 
	    }
	  

	  public static void main(String[] args) throws Exception {	        
	        System.setSecurityManager(new RMISecurityManager()); 	               
	        Logger logger = Logger.getLogger("game.player");
	        logger.setUseParentHandlers(false);
	        logger.setLevel(Level.ALL);
	        ConsoleHandler handler = new ConsoleHandler();
	        handler.setLevel(Level.ALL);
	        logger.addHandler(handler);
	        new PlayerStart();
	  }
}
