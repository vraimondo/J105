
package client;
import manager.Message;
import manager.Token;
import game.Leader;

import java.rmi.*;
import java.util.*;


public interface Player extends Remote {
    
	/**
	 * Numero max di neighbours
	 */
    public final int MAX_TOLERED_CRASHES = 4;

    /**
     * Intervallo di tempo in millisecondi entro cui deve giungere una
     * risposta di ack prima che si consideri il Player in crash 
     */
    public final long ACK_TIMEOUT = 20000 ;
    
    
    /**
     * Inizializza il Player per connetterlo al server identificato
     * da uri_server_gioco
     * @param server_uri indirizzo del server
     * @return l'id del giocatore
     * @throws RemoteException
     */
    public boolean initialize(String server_uri) throws RemoteException;
    
  
    /**
     * Inserisce new_player nell'anello al posto precedentemente occupato
     * da previous
     * @param new_player il giocatore da aggiungere
     * @param new_player_id id del nuovo giocatore
     * @param previous giocatore che occupava la posizione ora presa dal 
     * nuovo giocatore
     * @throws RemoteException
     */
    public void newPlayer(Message message, Player new_player, int new_player_id, Player previous, Player sender) 
      throws RemoteException;
    
    /**
     * Restituisce il Player successivo
     * @return il Player successivo nell'anello che collega i partecipanti
     * @throws RemoteException
     */
    public Player getNext() throws RemoteException;
    
    /**
     * Ritorna la lista dei vicini con cui comunicare
     * @return la lista di Player con cui questo puo` comunicare direttamente
     * (e unidirezionalmente)
     * @throws RemoteException
     */
    public List<Neighbour> getNeighbours() throws RemoteException;
    
    /**
     * Ritorna la lista degli id dei vicini con cui comunicare
     * @return la lista di Player con cui questo puo` comunicare direttamente
     * (e unidirezionalmente)
     * @throws RemoteException
     */
    
    public ArrayList<Integer> getNeighboursID() throws RemoteException;

    /**
     * Restituisce l'id del Player
     * @return l'identificativo di questo Player
     * @throws RemoteException
     */
    public int getId() throws RemoteException;
       
    /**
     * Chiamato dal server per assegnare al giocatore un id univoco 
     * nella partita
     * @param id id univoco da assegnare al giocatore
     * @throws RemoteException
     */
    public void setId(int id) throws RemoteException;
    
    /**
     * Restituisce il nome del Player
     * @return La stringa contenente il nome del Player
     * @throws RemoteException
     */
	public String getPlayerName()throws RemoteException;
	
	  /**
     * Assegna il nome del giocatore
     * @param il nome del Player
     * @throws RemoteException
     */
    public void setPlayerName(String name)throws RemoteException;
    
    
    /**
     * Acknowledgement
     * @param message messaggio di cui si notifica la ricezione
     */
    public void ack(Player player,Message message) throws RemoteException;
    
    
    /**
     * Riceve una richiesta di elezione e la notifica ai suoi vicini
     * @param message messaggio di elezione
     * @param elezioneStartedYet 
     * @throws RemoteException
     */
    public void electionLeader(Message message, Leader leader, Player sender, boolean elezioneStartedYet) 
      throws RemoteException;
    
    /**
     * Riceve una conferma di leader e la notifica ai suoi vicini
     * @param message messaggio di leder
     * @throws RemoteException
     */
    public void endElectionLeader(Message message, Leader leader, Player sender) 
      throws RemoteException;
    
    
    /**
     * Ogni player all'inizio del gioco prende le carte dal mazzo e aggiorna il token.
     * @param message delle carte prese
     * @param sender player che ha spedito il messaggio 
     * @param token del gioco
     * @throws RemoteException
     */
    public void takeCards(Message message, Player self, Token token) throws RemoteException;
    
    
    /**
     * Ogni player aggiorna il proprio stato dal token
     * @param message di update del token
     * @param sender player che ha spedito il messaggio 
     * @param token del gioco
     * @throws RemoteException
     */
    public void tokenUpdate(Message message, Player self, Token token)throws RemoteException;
    
    /**
     * Avvia il timer 
     * @param message del timer
     * @param sender player che ha spedito il messaggio 
     * @param playerTurn 
     * @param countdown countdown del timer 
     * @throws RemoteException
     */
    public void nextStartTimer(Message message, Player sender, int countdown)throws RemoteException;
    
    /**
     * Aggiorna il token per questo giocatore
     * @param elezioneStartedYet 
     * @param deleteTimer 
     * @param new_state il nuovo stato del gioco
     * @throws RemoteException
     */
    public void updateState(Message message, Token new_token, Player sender,boolean elezioneStartedYet,boolean updateCartaTavolo, boolean deleteTimer) 
      throws RemoteException;
    
    
    /**
     * Passa il turno al giocatore successivo nell'anello
     * @param message l'ultimo messaggio ricevuto dal Player precedente
     * @param token 
     * @param manager 
     * @throws RemoteException
     */
    public void nextTurn(Message message, Token token) throws RemoteException;
    
	 /**
     * Abbandona il gioco, notificando al server e agli altri giocatori
     * @param message messaggio di abbandono
     * @param sender giocatore che invia il messaggio
     * @param player giocatore che abbandona il gioco
     * @param player_id id del giocatore che abbandona il gioco
     * @param myturnquit verifico se è il mio turno
     * @param token token del gioco
     * @param countdown del timer
     * @throws RemoteException
     */
    public void quit(Message message, Player sender, Player player, int player_id,boolean myturnquit, boolean firstPlayer, Token token, int countdown) 
      throws RemoteException;


	public void nextPingTimer(Message message, Player myself, int idsender) throws RemoteException;
}


