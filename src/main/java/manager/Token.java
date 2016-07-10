package manager;

import client.Player;
import game.Card;
import game.Mazzo;
import java.io.Serializable;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.logging.Logger;

public class Token implements Serializable {
	
	private static final long serialVersionUID = -4089838922962699410L;
	private Logger logger;
	private	Mazzo mazzo;
	private Card cartaTavolo;
	private String semeCorrente;
	private LinkedHashMap<Integer, ArrayList<Card>> players_cards;
	private LinkedHashMap<Integer, Integer> players_score;
	private LinkedHashMap<Integer, String> players_names;
	public static final int CARD_PLAYER=5;
	private int carteDaPrendere;
	private String messaggio;
	private int idUpdate;
	private int visualizzaEvento;
	private int idplayerTurn;
	private Player playerTurn;
	private boolean winner=false;
	private String playerWin;
	
	

	public Token(){	
		players_cards = new LinkedHashMap<Integer, ArrayList<Card>>();
		players_score = new LinkedHashMap<Integer, Integer>();
		players_names = new LinkedHashMap<Integer, String>();
		loadMazzo();
	}
	
	public Token(boolean check){	
	}

	private void loadMazzo () {
		try {
			mazzo = new Mazzo();
		}catch (NullPointerException e){
			logger.severe("Errore in empty" + e.getMessage());
		}
	}
	
	public ArrayList<Card> giveInitialCards(Player player) throws RemoteException{	
		
		 int summScore = 0;

		 ArrayList<Card> play_cards = mazzo.prendiCarteMazzo(CARD_PLAYER);

			for (Card card : play_cards){
				card.setPlayer(player.getId());
				summScore = summScore + card.getScore();
			}

			populateToken(player,play_cards,summScore);

	    	return play_cards;
	    }
	
	public ArrayList<Card> giveCards(Player player, ArrayList<Card> play_cards, int myscore ) throws RemoteException{
	  	
	    ArrayList<Card> cards = mazzo.prendiCarteMazzo(carteDaPrendere);

		for (Card card : cards) {
			card.setPlayer(player.getId());
			play_cards.add(card);
			myscore = myscore + card.getScore();
		}
		cards.clear();
		//cards = null;

		populateToken(player,play_cards,myscore);

		return play_cards;
		
	}

	public void populateToken (Player player, ArrayList<Card> cards, int score){
		try {
			players_cards.put(player.getId(), (ArrayList<Card>) cards);
			players_score.put(player.getId(), score);
			players_names.put(player.getId(), player.getPlayerName());
		} catch (RemoteException e) {
			logger.severe("Errore in populateToken" + e.getMessage());
		}
	}
	
	
	 /**
     * Rimuove un giocatore dal token
     * @param player_id id del giocatore da rimuovere
     */
    public Token removePlayer(int player_id) {

		ArrayList<Card> cards = players_cards.get(player_id);
		Token tk = null;

		try{
			if(cards != null){

				for (Iterator<Card> i = cards.iterator(); i.hasNext(); ) {
					Card card = i.next();
					i.remove();
					card.reset();
					mazzo.addCarteMazzo(card);
				}

				players_cards.remove(player_id);
				players_names.remove(player_id);
				players_score.remove(player_id);

			}

			tk = this;

		}catch (Exception e) {
			logger.severe("Errore in removePlayer" + e.getMessage());
		}

		return tk ;
	}

	
	public Card getCartaTavolo() {
		return cartaTavolo;
	}

	public void setCartaTavolo(Card cartaTavolo) {
		this.cartaTavolo = cartaTavolo;
	}
    
	public int getCarteDaPrendere() {
		return carteDaPrendere;
	}

	public void setCarteDaPrendere(int carteDaPrendere) {
		this.carteDaPrendere = carteDaPrendere;
	}


	public LinkedHashMap<Integer, ArrayList<Card>> getPlayers_cards() {
		return players_cards;
	}

	public void setPlayers_cards(LinkedHashMap<Integer, ArrayList<Card>> players_cards) {
		this.players_cards = players_cards;
	}

	public void setPlayers_names(LinkedHashMap<Integer, String> players_names) {
		this.players_names = players_names;
	}

	public LinkedHashMap<Integer, Integer> getPlayers_score() {
		return players_score;
	}

	public void setPlayers_score(LinkedHashMap<Integer, Integer> players_score) {
		this.players_score = players_score;
	}

	public LinkedHashMap<Integer, String> getPlayers_names() {
		return players_names;
	}

	public void setSemeCorrente(String semeCorrente) {
		this.semeCorrente = semeCorrente;
	}


	public String getSemeCorrente() {
		return semeCorrente;
	}

	public String getMessaggio() {
		return messaggio;
	}

	public void setMessaggio(String messaggio) {
		this.messaggio = messaggio;
	}

	public Mazzo getMazzo() {
		return mazzo;
	}

	public void setMazzo(Mazzo mazzo) {
		this.mazzo = mazzo;
	}

	public void setIdUpdate(int idUpdate) {
		this.idUpdate = idUpdate;
	}

	public int getIdUpdate() {
		return idUpdate;
	}

	public void setVisualizzaEvento(int visualizzaEvento) {
		this.visualizzaEvento = visualizzaEvento;
	}

	public int getVisualizzaEvento() {
		return visualizzaEvento;
	}

	public int getIdPlayerTurn() {
		return idplayerTurn;
	}

	public void setIdPlayerTurn(int idplayerTurn) {
		this.idplayerTurn = idplayerTurn;
	}

	public Player getPlayerTurn() {
		return playerTurn;
	}

	public void setPlayerTurn(Player playerTurn) {
		this.playerTurn = playerTurn;
	}

	public String getVincitore() {
		return playerWin;
	}

	public void setVincitore(String vincitore) {
		this.playerWin = vincitore;
	}

	public boolean isWinner() {
		return winner;
	}

	public void setWinner(boolean winner) {
		this.winner = winner;
	}
	

}
