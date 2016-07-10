package manager;

import game.Card;
import game.Mazzo;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import client.Player;



public class ManagerToken {
	
	private Token my_token;    
	private	Mazzo mazzo;
	private Card cartaTavolo;
	private ArrayList<Card> carteFree;
	private LinkedHashMap<Integer, ArrayList<Card>> players_cards;
	private LinkedHashMap<Integer, Integer> players_score;
	private LinkedHashMap<Integer, String> players_names;
	private ArrayList<Card> my_cards;
	private String semeCorrente;
	private boolean init = true;
	private static ManagerToken the_instance=null;
	private Logger logger;
	private Player player;
	private String playerName;
	private int carteDaPrendere;
	private String messaggio;
	private int idUpdate;
	private int visualizzaEvento;
	private int idplayerTurn;
	private Player playerTurn;
	private int mytotalScore;
	private boolean winner=false;
	private String playerWin;
	
	
	
	public ManagerToken(){
		logger = Logger.getLogger("manager.ManagerToken");	
		my_cards = new ArrayList<Card>();
		players_cards = new LinkedHashMap<Integer, ArrayList<Card>>();
		players_score = new LinkedHashMap<Integer, Integer>();
		players_names = new LinkedHashMap<Integer, String>();
	}
	
    public static void setInstance(ManagerToken instance) {
    	the_instance = instance;
    }
    
    public static ManagerToken getInstance() {
        return the_instance;
    }
    
    
    public void initLeader(Player new_player, Token token) throws RemoteException{
    	logger.entering("ManagerToken", "initLeader");
    	player= new_player;
    	
    	synchronized (token) {
    		cartaTavoloLeader(token);
    		my_cards =	token.giveInitialCards(player);
     		players_cards = token.getPlayers_cards();
     		players_score = token.getPlayers_score();
     		players_names = token.getPlayers_names();
     		Set<Integer> keySet = players_score.keySet();
     		for(Integer key:keySet){
     			if(key==player.getId()){
     				mytotalScore = mytotalScore + players_score.get(key);
     		}}
     		System.out.println("Total Score: " + mytotalScore);
     		
		}	  	
    	 init=false;
 		 updateLocalFromToken(token);
 		 logger.exiting("ManagerToken", "initFirst");
 	
    }
    
    public void initPlayer(Player new_player, Token token) throws RemoteException{
    	logger.entering("ManagerToken", "initFirst");
   	 init=false;
   	 player= new_player;
   	 synchronized (token) {
   //		logger.info("Prendo le 5 carte dal mazzo");
   	   	my_cards=token.giveInitialCards(player);
   		players_cards = token.getPlayers_cards();
   		players_score = token.getPlayers_score();
   		players_names = token.getPlayers_names();
   		
   		Set<Integer> keySet = players_score.keySet();
 		for(Integer key:keySet){
 			if(key==player.getId()){
 				mytotalScore = players_score.get(key);
 		}
 			}
 		System.out.println("Total Score: " + mytotalScore);
	}
		updateLocalFromToken(token);
		logger.exiting("ManagerToken", "initFirst");
   }
    
	// questo metodo va chiamato ogni volta che viene mandato un messaggio di tipo Update perchè ogni nodo deve salvare il token nel suo manager
	public void updateLocalFromToken (Token token) {
		synchronized (token) {
		my_token=token;	
		this.cartaTavolo = token.getCartaTavolo();
		this.semeCorrente = token.getSemeCorrente();
		this.mazzo = token.getMazzo();
		this.players_cards = token.getPlayers_cards();
		this.players_score= token.getPlayers_score();
		this.players_names = token.getPlayers_names();
		this.carteDaPrendere = token.getCarteDaPrendere();
		this.messaggio= token.getMessaggio();
		this.idUpdate = token.getIdUpdate();
		this.visualizzaEvento = token.getVisualizzaEvento();
		this.idplayerTurn=token.getIdPlayerTurn();
		}
	}
	
	public Token updateTokenFromLocal () {
		synchronized (my_token) {
			my_token.setCartaTavolo(this.cartaTavolo);
			my_token.setSemeCorrente(this.semeCorrente);
			my_token.setMazzo(this.mazzo);
			my_token.setPlayers_cards(this.players_cards);
			my_token.setPlayers_names(this.players_names);
			my_token.setPlayers_score(this.players_score);
		    my_token.setCarteDaPrendere(carteDaPrendere);
		    my_token.setMessaggio(this.messaggio);
		    my_token.setIdUpdate(this.idUpdate);
		    my_token.setVisualizzaEvento(this.visualizzaEvento);
		    my_token.setIdPlayerTurn(this.idplayerTurn);
		}
		return my_token;
	}
		
	/**Il leader posiziona una carta sul tavolo */
    public void cartaTavoloLeader(Token token){
    	ArrayList<Card> carte = token.getMazzo().prendiCarteMazzo(1);
    	while(carte.get(0).getValore()==1||carte.get(0).getValore()==2||carte.get(0).getValore()==9){
    		token.getMazzo().addCarteMazzo(carte.get(0));
    		token.getMazzo().shuffleCards();
    		carte.clear();
    		carte = token.getMazzo().prendiCarteMazzo(1);   	
    	}
    	 Card carta = carte.get(0);
    	/* if(carta.getValore()==1 || carta.getValore()==2 || carta.getValore()==9){
    		 carta.setSpecialCard(true);
    	 }*/	 
    	//Card carta = token.getMazzo().get(1);
    	synchronized (token) {
    	token.setCartaTavolo(carta);
    	token.setSemeCorrente(carta.getSeme());
    	token.getMazzo().prendiMazzoCarte().remove(carta);
    	}
    	this.updateLocalFromToken(token);
    }
    
    public void takeCards(){
    	try {
    		this.setCarteDaPrendere(carteDaPrendere);
    		my_token.setCarteDaPrendere(carteDaPrendere);
			my_cards = my_token.giveCards(player, my_cards, this.getMytotalScore());
			players_cards = my_token.getPlayers_cards();
			players_score= my_token.getPlayers_score();
			Set<Integer> keySet = players_score.keySet();
     		for(Integer key:keySet){
     			if(key==player.getId()){
     				mytotalScore = players_score.get(key);
     		}}
     		System.out.println("Total Score: " + mytotalScore);
		//	System.out.println("My card " + my_cards.size());		
			this.setCarteDaPrendere(0);
			my_token.setCarteDaPrendere(carteDaPrendere);
					
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
    }
  
    
    public ArrayList<Card> controllaCartaTavolo(boolean isTwo) {
    	ArrayList<Card> listaCarteDaGiocare = new ArrayList<Card>();
	    	for (int i = 0;i < this.getMy_cards().size();i++) {
				Card carta = this.getMy_cards().get(i);
				if (isTwo==false) {
						if (this.getCartaTavolo().getValore() == carta.getValore() || this.getSemeCorrente().equals(carta.getSeme())) {
								listaCarteDaGiocare.add(carta);
						}
				}else {
					if (this.getCartaTavolo().getValore() == carta.getValore()) {
						listaCarteDaGiocare.add(carta);
					}
				}
    		
	    	}
	    	return listaCarteDaGiocare;
    }
      
    //metodo per notificare il crash di alcuni players
    public Token notifyCrashes(Set<Integer> crashed_players, Token token) {     
    	logger.log(Level.INFO, "Il mio ManagerToken rimuove i giocatori in crash e aggiorna il Token ...");
    	for (Iterator<Integer> i = crashed_players.iterator(); i.hasNext(); ) {
            int player = i.next();          
				System.out.println("playerCrashed " + player);
				token.removePlayer(player);	
				
        }
    	this.updateLocalFromToken(token);
      return token;
    }
    
   
   public Token notifyQuitPlayers( Set<Integer> quit_players,Token token) {    
       logger.log(Level.INFO, "Il mio ManagerToken rimuove i giocatori usciti e aggiorna il Token...");   
           for (Iterator<Integer> i = quit_players.iterator(); i.hasNext(); ) {
               int player = i.next();
               System.out.println("playerOut " + player);
               token.removePlayer(player);      
       }
           this.updateLocalFromToken(token);
       return token;
   }
    
    public void cambiaSeme (String seme) {
    	my_token.setSemeCorrente(seme);
    	this.setSemeCorrente(seme);
    	
    }
	

	public Mazzo getMazzo() {
		return mazzo;
	}

	public void setMazzo(Mazzo mazzo) {
		this.mazzo = mazzo;
	}

	public Card getCartaTavolo() {
		return cartaTavolo;
	}

	public void setCartaTavolo(Card cartaTavolo) {
		this.cartaTavolo = cartaTavolo;
	}

	public List<Card> getCarteFree() {
		return carteFree;
	}

	public void setCarteFree(ArrayList<Card> carteFree) {
		this.carteFree = carteFree;
	}

	public int getCarteDaPrendere() {
		return carteDaPrendere;
	}

	public void setCarteDaPrendere(int carteDaPrendere) {
		this.carteDaPrendere = carteDaPrendere;
	}

	public Token getToken() {
		return my_token;
	}

	public void setToken(Token token) {
		this.my_token = token;
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

	public LinkedHashMap<Integer, String> getPlayers_names() {
		return players_names;
	}
	
	public LinkedHashMap<Integer, Integer> getPlayers_score() {
		return players_score;
	}

	public void setPlayers_score(LinkedHashMap<Integer, Integer> players_score) {
		this.players_score = players_score;
	}

	

	public void setSemeCorrente(String semeCorrente) {
		this.semeCorrente = semeCorrente;
	}

	public String getSemeCorrente() {
		return semeCorrente;
	}

	public boolean isInit() {
		return init;
	}

	public String getMessaggio() {
		return messaggio;
	}

	public void setMessaggio(String messaggio) {
		this.messaggio = messaggio;
	}

	public void setInit(boolean init) {
		this.init = init;
	}

	public List<Card> getMy_cards() {
		return my_cards;
	}

	public void setMy_cards(ArrayList<Card> my_cards) {
		this.my_cards = my_cards;
	}

	public void setPlayerName(String playerName) {
		this.playerName = playerName;
		
	}

	public String getPlayerName() {
		return playerName;
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

	public int getMytotalScore() {
		return mytotalScore;
	}

	public void setMytotalScore(int mytotalScore) {
		this.mytotalScore = mytotalScore;
	}

	public boolean isWinner() {
		return winner;
	}

	public void setWinner(boolean winner) {
		this.winner = winner;
	}

	public String getVincitore() {
		return playerWin;
	}

	public void setVincitore(String playerWin) {
		this.playerWin = playerWin;
	}

	
}
