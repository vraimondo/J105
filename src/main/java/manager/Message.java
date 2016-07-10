/*
 * Created on 13-gen-2005
 */
package manager;

import java.io.Serializable;
import java.rmi.RemoteException;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.Vector;

import client.Player;

/**
   Message, contiene informazioni sul mittente, sui nodi che hanno gi`a
   ricevuto il messaggio e su quelli che sono andati in crash durante il “giro
   dell’anello” corrispondente alla trasmissione di questo messaggio.
 */
public class Message implements Serializable {
    
    private static final long serialVersionUID = 1;
    
    /**
     * La classe di chi ha creato il messaggio. Serve per poter individuare
     * a chi deve tornare il messaggio durante il passaggio dei messaggi
     */
    public class Sender implements Serializable {
        
        private static final long serialVersionUID = 1;   
        public Player player;
        public int steps;
        
        public Sender(Player p, int s) {
            player = p;
            steps = s;
        }
    }
    
    private static long the_counter = 0;
    
    private List<Sender> senders;
    private Player origin;
    private int tries;
    
    private int sender_id;
    private long id_message;    
    
    private String info;
    
    private boolean resent;
    
    private Vector<Integer> crashed_players;
    
    /**
     * Insieme di id dei Player    
     * 
     *
     */
    private Set<Player> receivers;

    /**
     * Costruttore del messaggio
     * @param sender giocatore che crea il messaggio
     * @param info informazioni da inserire nel messaggio
     * @throws RemoteException
     */
    public Message(Player sender, String info) throws RemoteException {
        senders = new LinkedList<Sender>();
        senders.add(new Sender(sender, Player.MAX_TOLERED_CRASHES));
        tries = 1;
        origin = sender;
        this.info = info;
        sender_id = sender.getId();
        id_message = ++the_counter;
        resent = false;
        crashed_players = new Vector<Integer>();
        receivers = new HashSet<Player>();
    }
    
    /**
     * Imposta il flag di resent
     * @param value true se il messaggio deve essere rispedito, false 
     * altrimenti
     */
    public void setResent(boolean value) {
        resent = value;
    }
    
    /**
     * Controlla se il messaggio e' stato gia rispedito
     * @return true se il messaggio e' stato gia' rispedito, false altrimenti
     */
    public boolean isResent() {
        return resent;
    }
    
    /**
     * Controlla se il messaggio e' stato originato da player
     * @param player giocatore da controllare
     * @return true se player e' il giocatore che ha creato il messaggio,
     * false altrimenti
     */
    public boolean isOrigin(Player player) {
        return origin.equals(player);
    }
    
    /**
     * Prende l'id del Player che ha generato il messaggio
     * @return l'id del Player da cui il messaggio ha avuto origine
     */
    public int getOriginId() {
        return sender_id;
    }
    
    /**
     * Numero di tentativi di invio del messaggio
     * @return il numero di tentativi di invio del messaggio
     */
    public int getTries() { return tries; }
    
    /**
     * Incrementa il numero di tentativi di uno
     */
    public void incTries() { ++tries; }
    
    /**
     * Restituisce la lista dei giocatori che sono nel messaggio
     * @return la lista dei giocatori presenti nel messaggio
     */
    public List<Player> getSenders() {
        List<Player> retval = new LinkedList<Player>();
        for (Iterator<Sender> i = senders.iterator(); i.hasNext(); ) {
            retval.add(((Sender)i.next()).player);
        }
        return retval; 
    }
    
    /**
     * Controlla se c'e' un host lontano da lui MAX_TOLERED_CRASHES
     * a cui deve mandare un ack
     * @return il Player a cui mandare l'ack
     */
    public Player getSenderForAck() {
    	Iterator<Sender> i = senders.iterator();
        while ( i.hasNext() ) {
            Sender s = (Sender)i.next();
            if (s.steps == 0) {
                i.remove();
                return s.player;
            }
        }
        return null;
    }
    
    public void decrementSteps(){
    	int count=0;
    	Iterator<Sender> i = senders.iterator();
        while ( i.hasNext() ) {
            Sender s = (Sender)i.next();
            s.steps--;
            count=s.steps;
        }
    }
    
    /**
     * Aggiunge player alla lista dei mittenti
     * @param player giocatore da aggiungere alla lista dei mittenti
     */
    public void addToSenders(Player player) {
    	Sender s= new Sender(player,Player.MAX_TOLERED_CRASHES );
        senders.add(s);
    }
    
    /**
     * Controlla se due messaggi sono uguali
     * @param o l'oggetto da controllare
     */
    public boolean equals(Object o) {
        if (o instanceof Message) {
            Message other = (Message)o;
            //return uid.equals(other.uid);
            return id_message == other.id_message && sender_id == other.sender_id
                && tries == other.tries;
        }
        return false;
    }
    
    /**
     * Trasforma l'oggetto in una stringa
     */
    public String toString() {
        return "<" + info + "," + sender_id + "," + id_message + "," + tries + ">";
    }
    
    /**
     * Trasforma l'oggetto in una hashCode
     */
    public int hashCode() {
        return toString().hashCode();
    }
    
    public String getInfo(){
    	return this.info;
    }
       
    /**
     * Inserisce il giocatore con id player_id nella lista dei giocatori 
     * andati in crash
     * @param player_id l'id del giocatore andato in crash
     */
    public void addToCrashed(int player_id) {
        crashed_players.add(new Integer(player_id));
    }
    
    /**
     * Esamina l'insieme dei giocatori andati in crash
     * @return l'insieme dei giocatori andati in crash
     */
    public Set<Integer> getCrashedPlayers() {
        Set<Integer> retval = new HashSet<Integer>();
        retval.addAll(crashed_players);
        return retval;
    }
    
    /**
     * Controlla se ci sono giocatori andati in crash
     * @return true se ci sono giocatori andati in crash, false altrimenti
     */
    public boolean hasCrashedPlayers() {
        return crashed_players.size() > 0;
    }
    
    /**
     * Aggiunge il Player player alla lista dei ricevitori
     * @param player il giocatore da aggiungere ai ricevitori
     */
    public void addToReceivers(Player player) {
        receivers.add(player);
    }
    
    /**
     * Controlla se il Player player e' nellal ista dei ricevitori
     * @param player il Player da controllare
     * @return true se il Player player e' nella lista dei ricevitori
     */
    public boolean received(Player player) {
        return receivers.contains(player);
    }
    
    public void deleteCrashed() {
    	crashed_players.removeAllElements();
    }
}
