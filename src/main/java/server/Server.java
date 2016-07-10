
package server;


import java.rmi.*;
import java.util.List;

import client.Player;

public interface Server extends Remote {

    public final int NUMBER_OF_CRASHES = 2;
    
    
    /**
     * Invocato da un Player per partecipare al game
     * @return true se la richiesta e` soddisfatta, false altrimenti
     * @throws RemoteException
     */
    public boolean join(String game, Player client)
      throws RemoteException;

    /**
     * Invocato da un Player per entrare nell'anello di gioco
     * @param player il giocatore che fa la richiesta
     * @param game identificativo del game a cui si vuole partecipare
     * @return una lista di Player a cui collegarsi per entrare nell'anello
     * @throws RemoteException
     */
    public List<Player> getConnectionsInGame(String game, Player player) 
      throws RemoteException;
    
    /**
     * Invocato da un Player quando abbandona una partita 
     * (in maniera corretta, non per un guasto...). Il metodo non si occupa di
     * aggiornare lo stato del game o le connessioni tra i client (cio` 
     * infatti violerebbe i requisiti di interazione minima con il server 
     * centrale), ma serve solo per tenere aggiornata la lista ritornata 
     * da getConnessioniNelGioco
     * @param client il giocatore che lascia il gioco
     * @param game il game in corso
     * @throws RemoteException
     */
    public void leave(String game, Player client) 
      throws RemoteException;
    
    public List<Player> getPlayers(String game) throws RemoteException;
    
    public void setGame(boolean game) throws RemoteException;
    	
    
   
    
}
