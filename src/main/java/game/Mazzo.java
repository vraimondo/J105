package game;


import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.logging.Logger;

public class Mazzo implements Serializable{

	private static final long serialVersionUID = 1L;
	private ArrayList<Card> mazzo = new ArrayList<Card>();
	private Logger logger;
	
		public Mazzo(){
				try {
					mazzo = CardLoader.loadCards();
				} catch (Exception e) {
					logger.warning("Errore in mazzo" + e.getMessage());
				}
			
		}

		
		public void shuffleCards(){
			Collections.shuffle(mazzo);		
		}		
		
		public ArrayList<Card> prendiCarteMazzo(int cartedaprendere){
			
		//	System.out.println("Mazzo Prima" + mazzo.size());
			
			ArrayList<Card> list = new ArrayList<Card>();
			
			for(int i=0; i<cartedaprendere; i++ ){ 
			    	shuffleCards();
			    	list.add(mazzo.get(i));
					mazzo.remove(i);
			}			
		//	System.out.println("Mazzo Dopo" + mazzo.size());
			return list;
		}
		
		public void addCarteMazzo(Card c){
			mazzo.add(c);
			shuffleCards();
		}

		public ArrayList<Card> prendiMazzoCarte() {
			return mazzo;
		}

}
