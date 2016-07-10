package game;

import java.io.Serializable;

public class Card implements Serializable {
	
	private static final long serialVersionUID = 3598531505154707902L;
	private int valore;
	private String seme;
	private String icona;
	private int id_card;
	private int score;
	private boolean specialCard=false;
	
	

	/*
     * id del giocatore che possiede la carta, -1 se e` libera
     */
    private int player;
	
	
	 /**
     * Crea una nuova carta
     * @param card_id l'id della carta
     * @param icona l'immagine della carta
     */
	public Card(int  valore,int score, String seme , String icona, int id){
		 player = -1;
		 this.valore=valore;
		 this.score=score;
		 this.seme=seme;
	     this.icona = icona;
	     this.id_card=id;
	}

	 /**
     * La carta viene resettata
     */
    public void reset() {
        player = -1;
    }
    
    /**
     * Controlla se la carta è libera
     * @return true
     */
    public boolean isFree() {
        return player == -1;
    }
	
    /**
     * Restituisce il giocatore che possiede questa carta
     * @return il giocatore che possiede la carta
     */
    public int getPlayer() {
        return player;
    }
    
    /**
     * Imposta a player il Player proprietario della carta
     * @param player il nuovo proprietario di questa carta
     */
    public void setPlayer(int player) {
        this.player = player;
    }
    
   
	public int getValore() {
		return valore;
	}

	public void setValore(int valore) {
		this.valore = valore;
	}

	public String getSeme() {
		return seme;
	}

	public void setSeme(String seme) {
		this.seme = seme;
	}

	public String getIcona() {
		return icona;
	}

	public void setIcona(String icona) {
		this.icona = icona;
	}
	
	 public int getId_card() {
			return id_card;
		}

		public void setId_card(int id_card) {
			this.id_card = id_card;
		}

	public boolean equalsCards(Object o) {
		if (o instanceof Card) {
			return id_card == ((Card)o).id_card;
		}
		return false;
	}

	public boolean isSpecialCard() {
		return specialCard;
	}

	public void setSpecialCard(boolean specialCard) {
		this.specialCard = specialCard;
	}

	public int getScore() {
		return score;
	}

	public void setScore(int score) {
		this.score = score;
	}

}
