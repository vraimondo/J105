package server;

import client.Player;
import server.ui.ServerSettingsUI;
import server.ui.ServerUI;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.rmi.RMISecurityManager;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.logging.Logger;


public class ServerStart extends ServerImpl {

	
	private static final long serialVersionUID = 1L;
	private ServerUI gui;
	
	public ServerStart() throws RemoteException {
		 	super();
		    gui = new ServerUI();
	        gui.setVisible(true);
	}
 
	
	   public boolean join(String game, Player client) throws RemoteException {
	        boolean retval = super.join(game, client);
	        if(retval){
	        gui.addLog("Il giocatore " + client.getPlayerName() + " con ID " + client.getId() +  " e' entrato nel gioco");
	        }
	        return retval;
	    }
	    
	    public void leave(String game, Player client) throws RemoteException {
	        super.leave(game, client);
	        gui.addLog("Il giocatore " + client.getPlayerName() + " con ID " + client.getId() + 
	                " e' uscito dal gioco");
	    }
	    
	    public static void main(String[] args) {
	        System.setSecurityManager(new RMISecurityManager());
	        
	        final Logger logger = Logger.getLogger("server.ServerStart");
	        
	        
	        final ServerSettingsUI settings = new ServerSettingsUI();
	        settings.getStartServer().addActionListener(
	                new ActionListener() {
	                    public void actionPerformed(ActionEvent evt) {
	                        settings.setVisible(false);
	                        String serverURL = settings.getServerURL();
	                        String game = settings.getGameName();	                       
	                        if(serverURL.equals("")||game.equals("")||settings.getPorta().equals("")){
	                        	settings.popUpControlla();
	                        	settings.setVisible(true);
	                        }else{
	                        	 int porta = Integer.parseInt(settings.getPorta());
	                        try {
	                        	
	                        	Registry registry = LocateRegistry.createRegistry(porta);
	                        	ServerStart s = new ServerStart();
	                        	registry.rebind(game, s);
	                            logger.info("Server game pronto");
	                            s.gui.addLog("Il server e' pronto");
	                            s.gui.setServerString("//" + serverURL + "/" + game);
	                        } catch (Exception e) {
	                            logger.severe("Eccezione!!: " + e.getMessage());
	                            e.printStackTrace(System.out);
	                        }
	                    }}
	                });
	        settings.setVisible(true);
	    }

}