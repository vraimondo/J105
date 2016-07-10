package server;

import client.Player;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;


public class ServerImpl extends UnicastRemoteObject
  implements Server {

    private static final long serialVersionUID = 1;
   
    private Logger logger;
    private static Random generatore_numeri;
    private boolean gameInit = false;
  
  
    public boolean isGame() {
		return gameInit;
	}

	public void setGame(boolean gameInit) throws RemoteException {
		this.gameInit = gameInit;
	}

	/**
     * Classe che contiene la lista di partecipanti al tavolo
     */
    protected class GameInfo {
        public List<Player> partecipants = new LinkedList<Player>();
    }
    
    /*
     * Map di GameInfo, un elemento per ogni game valore gestito dal server
     */
    private Map<String,GameInfo> games;
       
    /**
     * Il costruttore del server
     * @throws RemoteException
     */
    public ServerImpl() throws RemoteException {
        super();
        logger = Logger.getLogger("game.server");
        logger.setUseParentHandlers(true);
        games = new HashMap<String,GameInfo>();
        generatore_numeri = new Random();
    }
    
    /**
     * Restituisce le informazioni riguardanti il gioco game
     * @param game il gioco in corso
     * @return le informazioni del gioco
     */
    protected GameInfo getGameInfo(String game) {
        return (GameInfo)games.get(game);
    }

    /**
     * Rimuove il gioco game
     * @param game il gioco da rimuovere
     */
    public void resetVars(String game) {
        games.remove(game);
    }
    
    public boolean join(String game, Player client) 
      throws RemoteException {
    	if (!this.isGame()) {
	    	client.setId(generatore_numeri.nextInt(10000));
	        logger.info("Player con id: "+client.getId() +" si è iscritto a " + game);
	       
	        GameInfo info = getGameInfo(game);
	        
	        if (info == null) {
	            // questa e` la prima richiesta di partecipazione a questo game,
	            // impostiamo il timeout per la registrazione
	            info = new GameInfo();
	            games.put(game, info);
	        }
	        
	        info.partecipants.add(0, client);
	       
	        // assegnamo al giocatore un id univoco...
	        // ci ricordiamo di un giocatore in piu` perche' in 
	        // getConnessioniNelGioco non dobbiamo considerare il Player
	        // che fa la richiesta...
	        if (info.partecipants.size() > NUMBER_OF_CRASHES + 2) {
	            info.partecipants.remove(info.partecipants.size() - 1);
	        }
	        logger.info("I partecipanti al gioco sono: " + info.partecipants.size() );
	        games.put(game, info);
	        return true;
    	} else {
    		return false;
    	}
    }
    

    public void leave(String game, Player client) 
      throws RemoteException {
        GameInfo info = getGameInfo(game); 
        try {
            if (info.partecipants.contains(client)) {
                info.partecipants.remove(client);
                if(info.partecipants.size()==0){
                	this.setGame(false);
                }
                // TODO: se client va in crash a questo punto che dobbiamo fare?
           //     if (c != null) info.partecipants.add(c);
                logger.info("Player con id: "+client.getId() + " abbandona " + game);
                logger.info("I partecipanti al gioco ora sono: " + info.partecipants.size() );
                //the_server.addLog(client.toString() + " e'uscito nel gioco");
            }
        } catch (RemoteException e) {
            logger.log(Level.WARNING, "remote exception!", e);
        }
    }
    
    public List<Player> getConnectionsInGame(String game, Player player) 
      throws RemoteException {
        List<Player> retval = new LinkedList<Player>(getGameInfo(game).partecipants);
        // togliamo il giocatore che ha fatto la richiesta...
        retval.remove(player);
        return retval; 
    }
    
    public List<Player> getPlayers(String game) 
    	      throws RemoteException {
    	        List<Player> players = new LinkedList<Player>(getGameInfo(game).partecipants);
    	        return players; 
    	    }
    
}
