package client;


import game.Card;
import game.Leader;

import java.awt.Font;
import java.io.Serializable;
import java.rmi.*;
import java.rmi.server.UnicastRemoteObject; 
import java.util.*;
import java.util.logging.*;

import javax.swing.JLabel;

import manager.ManagerToken;
import manager.Message;
import manager.Token;
import client.Player;
import client.ui.PlayersUI;
import server.Server;

/**
 * Classe che descrive il vicinato
 */
class Neighbour implements Serializable {
	private static final long serialVersionUID = 1;

	public Player player;
	public int player_id;

	public  Neighbour(Player player, int player_id) {
		this.player = player;
		this.player_id = player_id;
	}

	public boolean equals(Object o) {
		if (o instanceof Neighbour) {
			return player_id == ((Neighbour)o).player_id;
		}
		return false;
	}
}

public class PlayerImpl extends UnicastRemoteObject implements Player {

	private final static long serialVersionUID = 1;
	public final static String THE_GAME = "105"; 
	private Logger logger;
	private int myid;
	private int leaderid;
	private String myname;
	private PlayersUI vista;
	private Leader myleader;
	private Set<Integer> quitplayers;
	private Server server;
	private Map<Message,Banner> banners = new HashMap<Message, Banner>();
	private List<Player> listallplayers;
	private List<Neighbour> neighbours;
	private boolean myturn = false;
	private boolean leaderisalive;
	private boolean electionstarted=false;
	private boolean updateCartaTavolo=true; 	
    private boolean condition=false;
    private boolean deleteTimer=true;
    private boolean updateNow=false;
    private boolean myturnquit=false;
	private boolean nextplayerquit = false;
	private Timer timer= new Timer(); 
	private Timer timerPing = new Timer(); 
	 


	public Leader getMy_leader() {
		return myleader;
	}

	public void setMy_leader(Leader myleader) {
		this.myleader = myleader;
	}

	public PlayerImpl() throws RemoteException {
		super();
		neighbours = Collections.synchronizedList(new LinkedList<Neighbour>());
	}

	public boolean initialize(String server_uri) throws RemoteException {
		boolean valueInit=false;
		
		logger = Logger.getLogger("client.PlayerImpl");
		try {
			server = (Server)Naming.lookup(server_uri);
		} catch (Exception e) {
			logger.log(Level.SEVERE, "Impossibile fare il lookup del server", e);
			throw new RemoteException(e.getMessage());
		}
		
		if (server.join(THE_GAME, this)) {
			valueInit=true;
			//una lista di Player a cui collegarsi per entrare nell'anello

			List<Player> vicini_tmp = server.getConnectionsInGame(THE_GAME, this);

			boolean done = true;
			Player prossimo = null;
			Iterator<Player> i = vicini_tmp.iterator();
		
			while(i.hasNext() && done )  	
			{
				try {
			
					prossimo = (Player)i.next();
					List<Neighbour> v = prossimo.getNeighbours();
					int id = prossimo.getId();
					v.add(0, new Neighbour(prossimo, id));
					for (Iterator<Neighbour> it = v.iterator(); it.hasNext(); ) {
						Neighbour n = (Neighbour)it.next();
						if (n.player.equals(this)) {
							it.remove();
						}
					}
					synchronized (this) {
						neighbours = v;
					}              
					done = false;
				} catch (Exception exc) {
					logger.log(Level.WARNING, "inizializza: ", exc);

				}
			}
			logger.info(this.toString() +" iscritto a " + THE_GAME);

           
			Message last_message = null;
			synchronized (this) {
				last_message = new Message(this, "NEW PLAYER");        
				makeBanner(last_message);
				logger.info("Creo il messaggio : " + last_message.toString());
			}
		  
			if (prossimo != null) {
				Thread t =  new SendNewPlayer(last_message, this, this, myid, prossimo);
				t.start();
			}
			
		}
		
		return valueInit;
	}    

	//metodo per controllare se due Player sono uguali
	public boolean equals(Object o) {
		if (o instanceof Player) {
			try {
				Player other = (Player)o;
				return getId() == other.getId();
			} catch (RemoteException re) {
				logger.log(Level.SEVERE, "RemoteException while equals()", re);
				return false;
			}
		}
		return false;
	}

	// Classe per gestire la consegna dei messaggi
	public class Banner {
		public boolean value;
		public Banner() 
		{ value = false; }
	}

	//Metodo per creare un bannere di un messaggio
	private Banner makeBanner(Message message) {
		Banner ban = new Banner();      
		banners.put(message, ban);
		return ban;

	}

	//Restituisce un nuovo banner impostato a true, altrimenti prende il banner nella lista banners
	private Banner getBanner(Message message) {
		logger.entering(getClass().getName(), "getBanner"); 
		Banner ban = (Banner)banners.get(message);
		if (ban==null) {
			logger.info("Il banner del messagio :" +  message.toString() + " non esieste, lo creo");
			ban = new Banner();
			ban.value = true;
			//logger.config("Creo il nuovo banner; banner.value=true del messaggio "+ message.toString());
			//banners.put(message, ret);
		}        
		logger.exiting(getClass().getName(), "getBanner");

		return ban;
	}

	//metodo che rimuove dalla lista dei timeouts il messaggio che ï¿½ stato conseganto consegnato.   
	private void deliveryNotify(Message message) {
		logger.entering("PlayerImpl", "deliveryNotify");
		Banner ban = getBanner(message);
		logger.info("Notifico la consegna del messaggio...");
		synchronized (ban) {
			ban.value = true;
		//	logger.config( "Rimuovo il banner dalla Map dei banners: " + message);
			synchronized (banners) {
				banners.remove(message);
			}
		    logger.info("notify banner " + message.toString());
			ban.notify();
		}
		logger.exiting("PlayerImpl", "deliveryNotify");
	}

	//Thread generico per la spedizione di un messaggio ad un vicino
	private abstract class SendMessageThread extends Thread{
		Player myself;
		Message message;

		public SendMessageThread(Message message, Player player ){
			this.myself=player;
			this.message=message;			
			 Banner delivered = getBanner(message);
	            synchronized (delivered) {
	                delivered.value = false;
	            }
			
			message.addToReceivers(myself);
		}

		public void run() {
			logger.entering("PlyerImp", "SendMessageThread");
			try{
				boolean get_next = true; //controllo per non prendere piï¿½ vicini
				Player receiver = null;
				Neighbour neighbour = null;
				Iterator<Neighbour> iter = neighbours.iterator();	

				boolean addedSenders = false; // controllo per verificare se ho giï¿½ inserito il sender nella lista dei senders del messaggio
				boolean block=true; //controllo per non prendere lo stesso vicino

				while ( iter.hasNext() || block) { 					    
					Player sender = null;     

					if (get_next && iter.hasNext() ) {  	
						Object obj = iter.next();
						neighbour = (Neighbour)obj;
						receiver = neighbour.player;

						if (!message.isOrigin(myself)){
							logger.info( "Non sono io l'origine del messaggio: " + message.toString()); 	
							if(!addedSenders){	
								message.addToSenders(myself);
								addedSenders=true;
							}
							//Insieme ad un messaggio, si inviano ogni volta anche il mittente e un contatore , inizialmente impostato
							//a MAX_TOLERED_CRASHES, che viene decrementato dal destinatario
							//quando riceve il messaggio. Quando il contatore arriva a zero, viene inviato
							//l'acknowledge al mittente associato a quel contatore, per comunicargli
							//che il messaggio e' giunto a destinazione (ovvero `e arrivato correttamente
							//a n+1 nodi, con n il numero di crash tollerati).

							//	logger.info("Decremento steps dei Senders del messaggio : " + message.toString() );
							message.decrementSteps();						
							//	logger.info("Verifico se c'ï¿½ un sender distante 3" + message.toString());
							sender = message.getSenderForAck();
							if (sender != null) {							
								if (sender.equals(myself)) {
									logger.info(" Il Sender sono io, mi blocco senza ack cumulativo: " + message.toString());  
									deliveryNotify(message);
								} else {
									logger.info("Mando l'ack cumulativo al Sender : " + sender.getId() + ", " + message.toString());
									sendAck(sender, message);
								} 
							}
						} 
					}else {
						
					//Altrimenti se non ho vicini mi fermo
						if (!iter.hasNext()) {
							logger.info( "Non ho piu` vicini, mi fermo");
							block = false;
							break;
						}
						//altrimenti vado avanti se ho vicini
						get_next = true;						
					}					
					try {
						// prima di propagare il messaggio, lo aggiorno...
						logger.info("Invio il messaggio : " + message.toString() +" al receiver " + receiver.getId());
						// invio il messaggio aggiornato...
						sendMessage(receiver); 
						// aspetto il mio ack...
						logger.info("Verifico la consegna del messaggio" + message.toString() +" al receiver " + receiver.getId() );
						Banner ban = getBanner(message);

						// Potrebbe succedere che un nodo faccia crash dopo aver ricevuto
						// correttamente un messaggio, ma prima di averlo inoltrato: in questo
						// caso, pero', tale nodo non mandera' mai i messaggi di ack;
						// basta quindi settare un timeout per la ricezione del messaggio di acknowledge,
						// alla scadenza del quale il messaggio viene ritrasmesso: a questo
						// punto se uno dei processi `e andato in crash la trasmissione sollevera' una
						// RemoteException.
						new Timer().schedule(new AckTimerTask(ban), ACK_TIMEOUT);
						synchronized (ban) 
						{
							//logger.info( "banner.value= " + ban.value + " del messaggio " + message.toString()); 	                             	                        	
							boolean arrivato_ack = false;
							//se banner.value e' uguale a false
							if (!ban.value) {      		                            	
								logger.info("Aspetto la consegna del messaggio: " + message.toString());
								ban.wait();
								logger.info("Dopo l'attesa della consegna del messaggio " +  message.toString());
								arrivato_ack = true;
							} else {
								logger.info("Il messaggio e' gia' stato consegnato correttamente: " +   message.toString());
							}  
							//se delivered.value ï¿½ uguale a true
							logger.info( "banner.value= " + ban.value + " del messaggio " + message.toString()); 
							logger.info("Verifico la ricezione dell'ack");
							if (ban.value) {  
								if (arrivato_ack) {
									logger.info("Ok, mi e` arrivato l'ack, quindi posso uscire: " + message.toString());
								} else {
									logger.info("ok, messaggio consegnato, esco: " + message.toString());
								}
								break;
							} else {
								logger.info("Non mando l'ack!!");
								logger.info( "e` scattato il timeout ack, riprovo a trasmettere: " +
										message.toString());
											message.incTries();
												get_next = false;
								return;
							}
						}
					} 

					// Possiamo accorgerci che un nodo `e andato in crash nel momento in cui tentiamo di
					// trasmettergli un messaggio, in quanto tale trasmissione corrisponde allï¿½invocazione
					// remota di un metodo dellï¿½oggetto Player corrispondente a quel
					// nodo, che solleva unï¿½eccezione di tipo RemoteException se ci sono dei problemi.
					// In questo caso, si prevede la trasmissione del messaggio al nodo successivo: 
					//visto che ogni nodo `e collegato ad f+1 nodi successivi,
					// ï¿½ garantito che almeno uno di essi non sia in crash.
					catch (RemoteException re) {
						logger.log(Level.WARNING, "Remote exception!", re);
						logger.log(Level.CONFIG, "Il destinatario e` andato in crash, ritrasmetto al successivo");    									
						if (receiver != null) {
							logger.info( "receiver in crash, lo rimuovo dai neighbours " + message.toString());   
							try{	
								//    neighbours.remove(receiver);
									iter.remove();
							
							}
							catch(Exception e){
								logger.log(Level.SEVERE, 
										"Exception receiver", e);
							}
						} 
						if (neighbour != null) {
							logger.info( "aggiungo l'id del giocatore a quelli in crash");										
							message.addToCrashed(neighbour.player_id);				
						}
						if (!iter.hasNext()) {
							logger.info( "Non ho piu` vicini, provo a ritrasmettere a me stesso...");
							block = false;
							get_next = false;
							neighbour = new Neighbour(myself, myid);
							receiver = myself;                            
						}
					} 
				}
			}catch (Exception e) {         	
				logger.log(Level.SEVERE, 
						"Exception in SendMessageThread.run", e);

			}
			logger.exiting("PlayerImpl", "SendMessageThread");

		}

		public abstract void sendMessage(Player recivier) throws RemoteException;

	}

	//Thred per la spedizione di un messaggio NewPlayer
	class SendNewPlayer extends SendMessageThread {
		Player new_player;
		int new_player_id;
		Player previous;

		public SendNewPlayer(Message message, Player self, Player new_player,
				int new_player_id, Player previous) {
			super(message, self);
			this.new_player = new_player;
			this.previous = previous;
			this.new_player_id = new_player_id;
		}

		public void sendMessage(Player receiver) throws RemoteException {
			// quando siamo qui il messaggio e pronto per essere inoltrato
			logger.entering("SendNewPlayer", "sendMessage");
			receiver.newPlayer(message, new_player, new_player_id, previous, this.myself );
			logger.exiting("SendNewPlayer", "sendMessage");
		}
	}

	//Inserisce new_player nell'anello al posto precedentemente occupato da previous
	public void newPlayer(Message message, Player new_player,int new_player_id, Player previous, Player sender) 
			throws RemoteException {
		logger.entering("PlayerImp", "newPlayer");
		logger.info("Ho un nuovo messaggio newPlayer " + message.toString() + "dal sender: "+ sender.getId() );
		synchronized (neighbours) {
			if (!messageNeedsForwarding(message)) {
				logger.config("Ho gia` ricevuto questo messaggio di newPlayer" + message.toString() );   
				List<Player> players = server.getPlayers(THE_GAME);   
				if(players.size()==3){
					logger.info("Tento di avviare l'elezione");
					this.election(3);
				}
				if(players.size() == 4) {
					logger.info("Tento di avviare l'elezione");
					this.election(4);
				}
				if(players.size() == 5) {
					this.election(5);
				}

			} else {    
				logger.config("Non ho mai ricevuto questo messaggio di newPlayer" + message.toString() );			
				Iterator<Neighbour> i = neighbours.iterator();
				while (i.hasNext()) {
					Neighbour n = (Neighbour)i.next();
					if (n.player.equals(new_player)) {                						
						break;
					}
				}
				//controllo se sono io il new_player o lo conosco giÃ 
				if (this.equals(new_player)) {
					logger.info("Sono il new_player oppure lo conosco gia`");
				} else {      
					logger.info("Non conosco il new player");
					int index = -1;
					int j = 0;
					Iterator<Neighbour> x = neighbours.iterator();
					while ( x.hasNext() ) {
						Neighbour n = (Neighbour)x.next();
						if (n.player.equals(previous)) {
							index = j;					
							break;
						}
						++j; }
					logger.info("Aggiorno la mia lista di vicini...");
					Neighbour new_n = new Neighbour(new_player, new_player_id);
					if (index >= 0) {
						neighbours.add(index, new_n);
						logger.config("Ho aggiunto new_player : "+ new_player_id +" alla lista dei vicini in posizione: " + index );
						if (neighbours.size() > MAX_TOLERED_CRASHES) {
							logger.config("Rimuovo il vicino in piu`" + neighbours.get(neighbours.size()-1).player_id);
							neighbours.remove(neighbours.size()-1);

						}
					} else if (neighbours.size() < MAX_TOLERED_CRASHES) {
						logger.config("Ho aggiunto new_player : "+ new_player_id +" alla lista dei vicini in posizione: " + neighbours.size() );
						neighbours.add(new_n);
					}                    
				}

				logger.config("Dopo new player, ho " + neighbours.size() + " neighbours");
				new SendNewPlayer(message, this, new_player, new_player_id, previous).start();
			}
		}
	}
  
	//Rispedisce l'ultimo messaggio allo scadere del timer del ack
	private class AckTimerTask extends TimerTask {
		Banner ban;

		public AckTimerTask(Banner ban) {
			this.ban = ban;
		}

		public void run() {
			// rispedisci l'ultimo messaggio al prossimo Player
			// nella lista
			synchronized (ban) {
				ban.value = false;
				ban.notify();
			}
		}
	}

	//Restituisce la lista dei player ad intervallo di tempo
	private class ListofPlayersTimerTask extends TimerTask {
		Server server;
		int numero;
		int count = 0;
		Message message;
		Player sender;
		Leader leader;

		private ListofPlayersTimerTask(Server server, int numero,Message last_message,Player sender, Leader leader) {
			this.server=server;
			this.numero = numero;
			this.message = last_message;
			this.sender=sender;
			this.leader = leader;
		}

		public void run() {    
			try {
				List<Player> players = server.getPlayers(THE_GAME);
				if (players.size() > numero) {
					logger.info("Fermo il timer ListPlayerTimerTask ");
					this.cancel();
				} else {
					if (count == 2) { // questo count conta quante volte deve aspettare dieci secondi
						logger.info("Fermo il timeout ListPlayerTimerTask");
						this.cancel();
						server.setGame(true);
						listallplayers=server.getPlayers(THE_GAME);
						Thread t=new SendElection(message,sender,leader,electionstarted);				
						try {
							t.start();
							t.join();
						} catch (InterruptedException e) {
						
							e.printStackTrace();
						}	
					}
				}
				count++;
			} catch (RemoteException e) {
				
				e.printStackTrace();
			}         	
		}
	}
	
	//Avvia l'elezione del leader
	public synchronized boolean election(int num) {
		try {	          
			Message last_message = new Message(this, "ELEZIONE");
			this.makeBanner(last_message);
			logger.info("Creo il messaggio : " + last_message.toString());
			Leader leader = new Leader();
			leader.setLeader(this);
			leaderisalive=true;
			if (num  <= 4) {      
				Timer timer= new Timer();
				TimerTask task= new ListofPlayersTimerTask(server,num,last_message,this ,leader);
				timer.schedule(task,0, 10000);	            
				logger.info("Attendo l'ingresso di altri giocatori...");
			} else {
				logger.info("Avvio l'elezione...");
			server.setGame(true);	
			listallplayers= server.getPlayers(THE_GAME);
			Thread t=	new SendElection(last_message,this ,leader,electionstarted);
			t.start();
			t.join();
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		return true;
	}
	
	public  boolean electionInGame() {		
		try {	
			electionstarted=true;
			Message last_message = new Message(this, "ELEZIONE NEL GIOCO");
			this.makeBanner(last_message);
			logger.info("Creo il messaggio : " + last_message.toString());
			Leader leader = new Leader();
			leader.setLeader(this);
			Thread t = new SendElection(last_message,this ,leader, electionstarted);
			t.start();	
			t.join();
			//t.sleep(3000);	
		} catch (Exception e) {
			e.printStackTrace();
		}
		return true;
	}
	

	class SendElection extends SendMessageThread {
		Leader leader;

		public SendElection(Message message,  Player self, Leader leader, boolean elezioneStartedYet) {
			super(message, self);
			this.leader = leader;			
		}

		public void sendMessage(Player outPlayer) throws RemoteException {
			logger.entering("PlayerImpl", "SendElection");
			outPlayer.electionLeader(message, leader, myself,electionstarted);           
			logger.exiting("PlayerImpl", "SendElection");
		}
	}

	public synchronized void electionLeader(Message message, Leader leader, Player sender, boolean elezioneStartedYet) 
			throws RemoteException {
		logger.entering("PlayerImp", "electionLeader");
		logger.info("Ricevuto messaggio elezione: " + message + ", da " + sender.getId());
	  		
		if (messageNeedsForwarding(message)) {
			if (leader.getLeader().getId() < this.getId()) {	  		    
				leader.setLeader(this);
				leaderisalive=true;
				logger.info("Setto il nuovo leader: " + leader.getLeader().getId());
			}					      
			new SendElection(message, this, leader,elezioneStartedYet).start();
			  
		} else {
			Message leader_message = new Message(this, "LEADER");
			this.makeBanner(message);
			logger.severe("Il messaggio di richiesta elezione ha fatto il giro adesso spedisco a tutti chi e' il leader: " + leader.getLeader().getId());
			logger.info("Creo il messaggio : " + leader_message.toString());
			this.setMy_leader(leader);
			this.setLeader_id(leader.getLeader().getId());
			logger.info("Leader: " + this.getMy_leader().getLeader().getId());
			Thread t = new SendLeder(leader_message, this, leader);
		    t.start();
		}
		logger.exiting("PlayerImp", "electionLeader");	  
	}    

	class SendLeder extends SendMessageThread {
		Leader leader;

		public SendLeder(Message message,  Player self, Leader leader) {
			super(message, self);
			this.leader = leader;
		}

		public void sendMessage(Player outPlayer) throws RemoteException {
			logger.entering("PlayerImp", "SendLeder");
			outPlayer.endElectionLeader(message, leader,this.myself);           
			logger.exiting("PlayerImp", "SendLeder");
		}
	}

	public synchronized void endElectionLeader(Message message, Leader leader, Player sender) 
			throws RemoteException {
		logger.entering("PlayerImp", "endElectionLeader");
		logger.info("Ricevuto messaggio: " + message + ", da " + sender.getId());
		if (messageNeedsForwarding(message)) {		
		   this.setMy_leader(leader);
		   this.setLeader_id(leader.getLeader().getId());
		   new SendLeder(message, this, leader).start();
		}
			if(myleader.getLeader().getId()==this.myid){
				ManagerToken managerToken = ManagerToken.getInstance();			
				if(managerToken.isInit()){	
					logger.info("Leader: " + this.getMy_leader().getLeader().getId());
					Token token= new Token();				
					managerToken.initLeader(this,token);
					vista.cancellaPannelloIniziale();
						Message message_cards = new Message(this, "CARDS");
						makeBanner(message_cards);
						logger.info("Creo il messaggio : " + message_cards.toString());
					   new SendCardsThread(message_cards, this, token).start();
				}
			}
		logger.exiting("PlayerImp", "endElectionLeader");	  
	}  
	
	class SendCardsThread extends SendMessageThread {
    Token token;
		
		public SendCardsThread(Message message,  Player self, Token token) {
			super(message, self);
			this.token=token;
		}

		public void sendMessage(Player outPlayer) throws RemoteException {
			logger.entering("PlayerImp", "SendLeder");
			outPlayer.takeCards(message,this.myself,token);           
			logger.exiting("PlayerImp", "SendLeder");
		}
	}
	
	public void takeCards(Message message,  Player sender, Token token) throws RemoteException {
		logger.entering("PlayerImpl", "takeCards");
		try {
			ManagerToken managerToken = ManagerToken.getInstance();
			managerToken.updateLocalFromToken(token);
			if (messageNeedsForwarding(message)) {
					managerToken.initPlayer(this, token);			
					Thread carte  = new SendCardsThread(message, this, token);
					carte.start();
					carte.join();
			}
			
			if(myleader.getLeader().getId()==myid){
				try {
					Message last_message = new Message(this,"UPDATE TOKEN");
					this.makeBanner(last_message);
					logger.info("Creo il messaggio : " + last_message.toString());
					managerToken.setPlayerTurn(this);
					managerToken.setIdPlayerTurn(myid);
					managerToken.updateTokenFromLocal();
					new SendTokenThread( last_message, this,managerToken.getToken()).start();
					
				} catch (Exception e) {
					logger.log(Level.SEVERE, "Eccezione in SendTokenThread", e);
				}
				
			}	
		   			
		} catch (Exception e) {
			logger.info("errore " + e);
			e.printStackTrace();
		}
		logger.exiting("PlayerImpl", "takeCards");
	}
	
	
	
	class SendTokenThread extends SendMessageThread {
    Token token;
		
		public SendTokenThread(Message message,  Player self, Token token) {
			super(message, self);
			this.token=token;
		}

		public void sendMessage(Player outPlayer) throws RemoteException {
			logger.entering("PlayerImp", "SendToken");
			outPlayer.tokenUpdate(message,this.myself,token);           
			logger.exiting("PlayerImp", "SendToken");
		}
	}
	
	public void tokenUpdate(Message message,  Player sender, Token token) throws RemoteException {
		logger.entering("PlayerImpl", "tokenUpdate");
		try {
			
			ManagerToken managerToken = ManagerToken.getInstance();
			if (messageNeedsForwarding(message)) {
				managerToken.updateLocalFromToken(token);			
					Thread updateToken  = new SendTokenThread(message, this, token);
					updateToken.start();
					updateToken.join();
			}else{
				this.setMy_turn(true);
				
			}
				    			
			if(myleader.getLeader().getId()==myid){
				try {
					Message last_message = new Message(this,"START TIMER");
					logger.info("Creo il messaggio : " + last_message.toString());
					int count=0;
					this.makeBanner(last_message);
					Thread t =  new StartTimerThread( last_message, this,count);
							t.start();
							t.join();
				} catch (Exception e) {
					logger.log(Level.SEVERE, "Eccezione in StartTimerThread", e);
				}
				
				try {
				Message ping_message = new Message(this,"PING TIMER");
				this.makeBanner(ping_message);
				logger.info("Creo il messaggio : " + ping_message.toString());
				Thread tt = new PingTimerThread(ping_message, this,myid);
				tt.start();
				}catch (Exception e) {
					logger.log(Level.SEVERE, "Eccezione in StartTimerThread", e);
				}
			}	
			
			vista.cancellaPannelloIniziale();
			vista.disegnaCarte(managerToken,isMy_turn(),this);		
			
		} catch (Exception e) {
			logger.info("errore " + e);
			e.printStackTrace();
		}
		logger.exiting("PlayerImpl", "tokenUpdate");
	}
	
	
	 class StartTimerThread extends SendMessageThread {
	
        int count;
     
       
        public StartTimerThread( Message message, Player self, int count){
        	 super(message,self);
        	 this.count=count;
        	
        }
        
        public void sendMessage(Player reciever) throws RemoteException {      	    
        	logger.entering("PlayerImp", "StartTimerThread");
        	try {	        		
                logger.log(Level.FINER, "Avvio il timer del reciever: " + reciever.getId());	               
            } catch (RemoteException re) {	            	
                logger.log(Level.FINER, "StartTimerThread su : REMOTE_EXCEPTION!");
            } 
        	reciever.nextStartTimer(message, myself, count);
            logger.exiting("PlayerImpl", "StartTimerThread");
        }        
    }
	
	
	 
	public void nextStartTimer(Message message, Player sender ,int countdown) throws RemoteException{
		 logger.entering("PlayerImple" , "nextStartTimer" );  
		 logger.info("Ricevuto messaggio di nextStartTimer " + message.toString()+ " da " + sender.getId());

	        logger.info("Cancello il Timer");
	        timer.cancel();
	        timerPing.cancel();
	      
		 
		    ManagerToken manager = ManagerToken.getInstance();
		    Token token = manager.getToken();
	        boolean leaderDeath=false; 
	        
	        if (message != null && message.hasCrashedPlayers()) {
	        	
	            logger.log(Level.INFO, "Alcuni Player sono andati in crash, informo il GameManager...");	          
	            token = manager.notifyCrashes(message.getCrashedPlayers(),token);
	            message.deleteCrashed();
	        	sendUpdate(token,electionstarted);
				            
	            logger.info("Controllo se il player caduto e' il leader");
	            try {
	  				myleader.getLeader().getId();
	  				logger.info("Il Leader e' vivo: " + myleader.getLeader().getId());					
	  			}catch (Exception e) {
	  				leaderDeath=true;
	  				logger.log(Level.WARNING,"Il Leader e' morto ");
	  			} 	            
	            if(leaderDeath){
	            	 logger.info("Condition in nextStartTimer" + condition);
	  		    	electionstarted=true;
		  		    electionInGame();
	  		    	synchronized (this) {
	  		    		while (!condition){
	  						try {
	  							wait();  		 
	  						} catch (InterruptedException e) {
	  							e.printStackTrace();
	  						}
	  					}
	  		    	 	condition=false; 	
	  		    		}	   
	  		    }
	                          	
	        }
	        
	   	 message.deleteCrashed();
	   	 
	   
		 if (messageNeedsForwarding(message)) {
			 
		        countdown += 50000;
			    TimerTask task= new StartTimerTask();
			    logger.info("Count " + countdown);
			    timer = new Timer();
				timer.schedule(task, countdown);	 
				logger.info("Attendo il nextTurn");	
			    Thread t = new StartTimerThread(message,this,countdown);
			    t.start();  
			    try {
					t.join();
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				  
		 }
		 
		
		 	 
		 logger.exiting("PlayerImpl", "nextStartTimer");
		
	}
	
		public class StartTimerTask extends TimerTask {
			
		
			private StartTimerTask() {			
			}

			public void run() {  
				logger.entering("PlayerImp", "StartTimerTask");
				try {
					ManagerToken manager = ManagerToken.getInstance();	
					logger.info("Timer scaduto, ora tocca a me");
			    	sendNewTimer(); 
			    	sendNewPing();
			    	manager.getToken().getCartaTavolo().setSpecialCard(false);
			    	manager.getCartaTavolo().setSpecialCard(false);
		                	
			    //	vista.yourTurn(manager);	
			    	
				} catch (Exception e) {
					
					e.printStackTrace();
				}   
				 logger.exiting("PlayerImpl", "StartTimerTask");
			}
		}
		
		
		
		public void sendUpdate(Token token, boolean elezioneStartedYet){
	      
	        	Message message;
	        	ManagerToken manager= ManagerToken.getInstance();
				try {
					message = new Message(this, "UPDATE STATE");
					logger.info("Creo il messaggio : " + message.toString());
					makeBanner(message);
			        manager.updateLocalFromToken(token);	
			        deleteTimer=false;
			        updateCartaTavolo=false;
			        Thread tt =  new SendUpdateState(message, this, token, elezioneStartedYet, updateCartaTavolo ,deleteTimer);
			        tt.start();	        	 		        
				} catch (RemoteException e) {			
					logger.log(Level.WARNING, "Eccezione in sendUpdate", e);
				} 
	        }
	
	        
		public void sendNewTimer(){
			Message message;
			 try {
					message = new Message(this,"START TIMER");
					logger.info("Creo il messaggio : " + message.toString());
					int count=0;
					makeBanner(message);
					Thread t = new StartTimerThread( message,this ,count);
					t.start();
					t.join();
				} catch (RemoteException e) {
					logger.log(Level.WARNING, "Eccezione in sendNewTimer", e);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
		}
		
		
		public void sendNewPing(){
			ManagerToken manager= ManagerToken.getInstance();
			Message ping_message;
		try {
			ping_message = new Message(this,"PING TIMER");
			this.makeBanner(ping_message);
			logger.info("Creo il messaggio : " + ping_message.toString());
			manager.setPlayerTurn(this);
			manager.setIdPlayerTurn(myid);
			manager.getToken().setPlayerTurn(this);
			manager.getToken().setIdPlayerTurn(myid);
			Thread tt = new PingTimerThread(ping_message, this, myid);
			tt.start();
			tt.join();
			}catch (Exception e) {
				logger.log(Level.SEVERE, "Eccezione in sendNewPing", e);
			}
		}
		
		 class PingTimerThread extends SendMessageThread {
			 
			 int idsender;
				      
		        public PingTimerThread( Message message, Player self, int idsender){
		        	 super(message,self);
		        	 this.idsender=idsender;  	
		        }
		        
		        public void sendMessage(Player reciever) throws RemoteException {      	    
		        	logger.entering("PlayerImp", "PingTimerThread");
		        	try {	        		
		                logger.log(Level.FINER, "Avvio il timer del reciever: " + reciever.getId());	               
		            } catch (RemoteException re) {	            	
		                logger.log(Level.FINER, "PingTimerThread su : REMOTE_EXCEPTION!");
		            } 
		        	reciever.nextPingTimer(message, myself, idsender);
		            logger.exiting("PlayerImpl", "PingTimerThread");
		        }        
		    }
		 
		
		 public void nextPingTimer(Message message, Player sender, int idsender) throws RemoteException{
			 logger.entering("PlayerImple" , "nextPingTimer" );  
			 logger.info("Ricevuto messaggio di nextPingTimer " + message.toString()+ " da " + sender.getId());
			 timerPing.cancel();
			 
			  ManagerToken manager= ManagerToken.getInstance();
			  
			 if (messageNeedsForwarding(message)) {
				    timerPing  = new Timer();
					TimerTask taskPing= new PingTimerTask(sender, idsender, manager.getIdPlayerTurn(),manager.getPlayerTurn() );
					timerPing.schedule(taskPing, 0,10000);
					Thread t = new PingTimerThread(message, this, this.myid);
					t.start();
					try {
						t.join();
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
		          }
			 logger.exiting("PlayerImple" , "nextPingTimer" ); 
			 }
		
		private class PingTimerTask extends TimerTask {
		
			Player sender;
			int idsender;
			int idplayerturn;
			Player playerTurn;
			
			public PingTimerTask(Player sender, int idsender, int idplayerturn,Player playerTurn) {
				this.sender=sender;
				this.idsender=idsender;
				this.idplayerturn=idplayerturn;
				this.playerTurn= playerTurn;
			}

			public void run() {
				int posixSender = 0;
				int posixTurn = 0;
				ManagerToken manager = ManagerToken.getInstance();
				try {		
					 sender.getId();
					 System.out.println("Ping : " + sender.getId());
				} catch (RemoteException re) {					

				if(idsender==idplayerturn){
						timerPing.cancel();
						this.cancel();			
						System.out.println("Timer scaduto, ora tocca a me");
				    	sendNewTimer(); 
				    	sendNewPing();
				    	vista.yourTurn(manager);	
					}
							
					for (int i = 0; i < neighbours.size();i++) {
					//	System.out.println("Vicini " + neighbours.get(i).player_id);
					//	System.out.println("Sender " + idsender);
						if (neighbours.get(i).player_id == idsender) {
						//	System.out.println("posixSender: " + i);
							posixSender = i;
						}
					//	System.out.println("Vicini " + neighbours.get(i).player_id);
					//	System.out.println("Turnista " + idplayerturn);
						if (neighbours.get(i).player_id == idplayerturn) {
						//	System.out.println("posixTurn: " + i);
							posixTurn = i;
						}
					}
					if (posixSender == posixTurn+1) {
					//	System.out.println("prima del try");
						
					//		System.out.println("Id player turn: " + manager.getIdPlayerTurn());
							for (int j = 0; j < neighbours.size();j++) {
							//	System.out.println("Vicini " + neighbours.get(j).player_id);
								if (neighbours.get(j).player_id==idplayerturn) {
									try {
										neighbours.get(j).player.getId();
								}catch(RemoteException re2) {
									timerPing.cancel();
									this.cancel();			
									System.out.println("Timer scaduto, ora tocca a me");
							    	sendNewTimer(); 
							    	sendNewPing();
							    	vista.yourTurn(manager);
								}
						
								}
				    	
					}
					
					logger.log(Level.WARNING, "Eccezione in PingTimerTask, alcuni player sono andati in crash...!!! ", re);	
					}
			}}
		}	
		

	
	public void nextTurnClient(ManagerToken manager) {
		logger.entering("PlayerImple" , "nextTurnClient" );
	
		try {
			logger.info("Cancello il Timer");
			timer.cancel();
			timerPing.cancel();
			myturn = false;
			logger.info("Crea un nuovo NEXT TURN");
			Message last_message = new Message(this,"NEXT TURN");
			this.makeBanner(last_message);
			logger.info("Creo il messaggio : " + last_message.toString());
			Token token = manager.getToken();
			Thread t = new NextTurnThread(this, last_message, token);
			t.start();
		} catch (Exception e) {
			logger.log(Level.SEVERE, "Eccezione in nextTurnCLient", e);
		}
		logger.exiting("PlayerImpl", "nextTurnClient");
	}
	
	
	private class NextTurnThread extends Thread {
        Player self;
        Message message;
        Token token;
       
        
        public NextTurnThread(Player self, Message message, Token token) {
            super();
            this.self = self;
            this.message = message;
            this.token=token;
            
        }
        
        public void run() {
        	logger.entering("PlayerImp", "NextTurnThread");
            Neighbour next = null;      
            Iterator<Neighbour> iter = neighbours.iterator();
            for ( ; iter.hasNext(); ) {
                try {        	
                    next = iter.next();
                    next.player.nextTurn(message,token);
                    break;
                } catch (RemoteException re) {
                    logger.log(Level.WARNING, "RemoteException chiamando nextTurn", re);
                    
                    if (next != null) {
                        logger.log(Level.FINE, "next in crash, lo rimuovo dai neighbours");
                        iter.remove();
                        logger.log(Level.FINE, "aggiungo l'id a quelli in crash");
                        if(neighbours.size()==0){
                        	 ManagerToken manager = ManagerToken.getInstance();
                        	manager.getToken().setVincitore(manager.getPlayerName());
        					 deleteTimers();
        					 vista.updateFinalTable(manager);
                        }
                        if (message == null) {
                            try {
                                message = new Message(self, "nextTurn");
                                makeBanner(message);
                            } catch (Exception e) {
                                logger.log(Level.WARNING, "Exception", e);
                            }
                        }
                        if (message != null) {
                            logger.log(Level.FINE, "Aggiungo next a quelli  in crash: " + next.player_id);
                            message.addToCrashed(next.player_id);  
                           
                        } 
                    }
                }
            }                   
            logger.exiting("PlayerImp", "NextTurnThread");
        }
        
    }

	public void nextTurn(Message message, Token token) throws RemoteException {
		 logger.entering("PlayerImple" , "ricevuto nextTurn");  
		 logger.info("Ricevuto messaggio di nextTurn " + message.toString());
	        synchronized (this) {
	            myturn = true;
	            token.setPlayerTurn(this);
				token.setIdPlayerTurn(myid);
	        }
	        logger.info("Cancello il Timer");
	        timer.cancel();
	        timerPing.cancel();
	        
	        	
	        ManagerToken manager = ManagerToken.getInstance();
	     
	        
	        boolean leaderDeath=false; 
	          
	        if (message != null && message.hasCrashedPlayers()) {
	        	
	            logger.log(Level.INFO, "Alcuni Player sono andati in crash, informo il GameManager...");
	          
	            token = manager.notifyCrashes(message.getCrashedPlayers(),token);
	         
	            	
	            logger.info("Controllo se il player caduto e' il leader");
	            try {
					myleader.getLeader().getId();
					logger.info("Il Leader e' vivo: " + myleader.getLeader().getId());
					
				}catch (Exception e) {
					leaderDeath=true;
					logger.info("Il Leader e' morto ");
				} 	            
	            if(leaderDeath){
		  		    electionInGame();   
	  		    }
	        }
	        	  
	     
	        message.deleteCrashed();
	        token = doQuitPlayers(manager, token);
	        quitplayers=null;
	            
	        Message last_message = null;
	        last_message = new Message(this, "UPDATE STATE");
	    	logger.info("Creo il messaggio : " + last_message.toString());
	        makeBanner(last_message);	            
	        manager.updateLocalFromToken(token);
	              
	       
	        
	        logger.log(Level.FINE,  "nuovo stato calcolato, invio gli aggiornamenti: ");
	        
	        if (neighbours.isEmpty()) {
	            logger.log(Level.FINE, "Non ho vicini, nextTurn su me stesso...");
	            manager.updateLocalFromToken(token);
	            nextTurn(last_message,token);
	            vista.yourTurn(manager);
	        } else {        
	      Thread tt =  new SendUpdateState(last_message, this, token, electionstarted,updateCartaTavolo,deleteTimer);
	      try {
	    	tt.start();
	  	   	tt.join();
		} catch (InterruptedException e1) {
			e1.printStackTrace();
		}	     
	      
	      if(token.isWinner()){
				vista.updateFinalTable(manager);
				}else{
	      
	      try {
				logger.info("Creo un nuovo START TIMER");
				Message last_message_new = new Message(this,"START TIMER");
				logger.info("Creo il messaggio : " + last_message_new.toString());
				int count=0;
				this.makeBanner(last_message_new);
				new StartTimerThread( last_message_new, this,count).start();
			} catch (Exception e) {
				logger.log(Level.SEVERE, "Eccezione in StartTimerThread", e);
			}
	      
	      try {
				Message ping_message = new Message(this,"PING TIMER");
				this.makeBanner(ping_message);
				logger.info("Creo il messaggio : " + ping_message.toString());
				Thread ttt = new PingTimerThread(ping_message, this,this.myid);
				ttt.start();
				}catch (Exception e) {
					logger.log(Level.SEVERE, "Eccezione in StartTimerThread", e);
				}
			  }  
	      vista.yourTurn(manager);
	   
	        }
	    
	       
	        
	       
	      
	        
	        logger.exiting("PlayerImp", "Exit nextTurn");            
	}

	 private Token doQuitPlayers(ManagerToken manager, Token token) {
		 logger.entering("PlayerImple" , "doQuitPlayers"); 
	        if (quitplayers != null) {
	        	boolean leaderDeath=false;
	            if (!quitplayers.isEmpty()) {
	                logger.log(Level.INFO, "rimuovo i giocatori usciti...");
	                token = manager.notifyQuitPlayers(quitplayers,token);   

	                try {
                        myleader.getLeader().getId();
                        logger.info("Il Leader e' vivo: " + myleader.getLeader().getId());
                        
                }catch (Exception e) {
                        leaderDeath=true;
                        logger.info("Il Leder e' morto");
                }
	                
	                if(leaderDeath && !electionstarted){     
	                	electionstarted=true;
	                    this.electionInGame();
	                }
	                
	            }
	        }
	        logger.exiting("PlayerImp", "doQuitPlayers");
	        return token;
	        
	    }
		
	
	 class SendUpdateState extends SendMessageThread {
	        Token token;
	       	        
	        public SendUpdateState(Message message, Player self,Token token, boolean electionstarted,boolean updateCartaTavolo, boolean deleteTimer) {
	            super(message, self);
	            this.token = token;
	        }
	        
	        public void sendMessage(Player target) throws RemoteException {
	        	logger.entering("PlayerImpl" , "SendUpdateState");  
	        	try {	        		
	                logger.log(Level.FINER, "update state su: " + target.getId());	               
	            } catch (RemoteException re) {	            	
	                logger.log(Level.FINER, "update state su: REMOTE_EXCEPTION!");
	            }    	
	            target.updateState(message, token, this.myself,electionstarted,updateCartaTavolo, deleteTimer );
	            logger.exiting("PlayerImpl", "SendUpdateState");
	        }
	    }
	
	public void updateState(Message message, Token token, Player sender, boolean electionstarted, boolean updateCartaTavolo, boolean deleteTimer)
			throws RemoteException {
		logger.entering("PlayerImp", "ricevuto updateState");
		logger.info("Ho un nuovo messaggio updateState " + message.toString() + "dal sender: "+ sender.getId() );		
		
		if(deleteTimer){
		logger.info("Cancello il Timer in Update");	
		timer.cancel();
		timerPing.cancel();
		}else{
			logger.info("Non Cancello il Timer in Update");	
		}
		
		 boolean updateNow=false;
		
				
		 ManagerToken manager = ManagerToken.getInstance();
		 manager.updateLocalFromToken(token);
		 boolean leaderDeath=false;
		  
		 if (message != null && message.hasCrashedPlayers()) {
	        	
	            logger.log(Level.INFO, "Alcuni Player sono andati in crash, informo il ManagerToken...");        
	            token = manager.notifyCrashes(message.getCrashedPlayers(), token);		                     
	            updateNow=true;	
	         
	            logger.info("Controllo se il player caduto e' il leader");
	            try {
	  				myleader.getLeader().getId();
	  				logger.info("Il Leader e' vivo: " + myleader.getLeader().getId());					
	  			}catch (Exception e) {
	  				leaderDeath=true;
	  				logger.log(Level.WARNING,"Il Leader e' morto ");
	  			} 	            
	            if(leaderDeath&&!electionstarted){
		  		    electionInGame();	    		   
	  		    }
	            
	        }
		 			 	
		 if (messageNeedsForwarding(message)) {	 
			 
		    manager.updateLocalFromToken(token);
		    vista.disegnaTavolo(vista.getTavolo(), this);
		    vista.updateTableScoreInfo(manager);
		    
		    if(updateCartaTavolo){
	    	vista.updateCardOnTable(manager);
		    }
	    	
	    	Thread update= new SendUpdateState(message,this,token,electionstarted,updateCartaTavolo,deleteTimer);
			try {
				update.start(); 
				update.join();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			
	    	    	
	    	if(updateNow){
	    		Message last_message = null;
		        last_message = new Message(this, "UPDATE NEW STATE");
		    	logger.info("Creo il messaggio : " + last_message.toString());
		        makeBanner(last_message);	
		        updateCartaTavolo=false;
		        deleteTimer=false;
		        Thread update_new= new SendUpdateState(last_message,this,token,electionstarted,updateCartaTavolo,deleteTimer);
		        updateNow=false;
		        try {
		        	update_new.start();
		        	update_new.join();
				} catch (InterruptedException e1) {
					e1.printStackTrace();
				}			
	    	}
	    	
	    	if(token.isWinner()){
		
			vista.updateFinalTable(manager);
				 
			}
	    	
		 }
		 
		 
		 synchronized (this) {
				condition=true;
				notify();
			}
		 
		 
		 logger.exiting("PlayerImpl", "updateState");
	}

	
	
	
	public synchronized boolean quitClient(boolean notify_others) {
		if (!notify_others) {
			try {
			//	server.leave(THE_GAME, this);
			} catch (Exception e) {
				logger.log(Level.SEVERE, "Exception in quit", e);
			}
			return true;
		}
		try {
		//	server.leave(THE_GAME, this);
			Message last_message = new Message(this, "QUIT");
			logger.info("Creo il messaggio : " + last_message.toString());
			this.makeBanner(last_message);
			ManagerToken manager = ManagerToken.getInstance();
			if(isMy_turn()){
			 	myturnquit=true;
			}
			nextplayerquit = true;
			
			Token token = manager.getToken();
			 manager.setMessaggio(vista.getOraAttuale() + manager.getPlayerName() + " e' uscito dal gioco");
			 manager.getToken().setMessaggio(vista.getOraAttuale() + manager.getPlayerName() + " e' uscito dal gioco");	
			int count=0;
			Thread t = new SendQuit(last_message,this, this, myid , myturnquit, nextplayerquit, token, count);
			t.start();
			t.join();
		} catch (Exception e) {
			logger.log(Level.SEVERE, "Exception in quit", e);
		}
		return true;
	}

	class SendQuit extends SendMessageThread {
		Player player;
		int player_id;
        int exit;
        boolean myturnquit;
        boolean nextplayerquit;
        Token token;
        int countdown;
		
		public SendQuit(Message message, Player self, Player playerOut, int playerOut_id, boolean myturnquit,boolean nextplayerquit, Token token, int countdown) {
			super(message, self);
			this.player = playerOut;
			this.player_id = playerOut_id;
			this.myturnquit=myturnquit;
			this.nextplayerquit=nextplayerquit;
			this.token=token;
			this.countdown=countdown;
			
		}

		public void sendMessage(Player outPlayer) throws RemoteException {
			logger.entering("SendQuit", "sendMessage");	           
			outPlayer.quit (message, this.myself, player, player_id,myturnquit, nextplayerquit, token, countdown);        
			logger.exiting("SendQuit", "sendMessage");
		}
	}
	
	
	public void quit(Message message, Player sender, Player player, int player_id, boolean myturnquit, boolean nextplayerquit, Token token,int countdown ) 
            throws RemoteException {
    logger.entering("PlayerImp", "void quit");
    logger.info("Ricevuto messaggio quit: " + message + ", da " + sender.getId());  

   
    ManagerToken manager = ManagerToken.getInstance();
    
    if (messageNeedsForwarding(message)) {
    
            Neighbour n = new Neighbour(player, player_id);
            int which_neighbour = neighbours.indexOf(n);
            if (which_neighbour >= 0) {
                    logger.config( "Il giocatore che abbandona e` un mio vicino");
                    logger.info( "Numero di vicini prima : " + neighbours.size());
                    neighbours.remove(n);
                    boolean done = false;
                    while (!done && neighbours.size() > 0) {
                            try {
                                    //prende la lista dei vicini 
                                    logger.info( "Numero di vicini middle : " + neighbours.size());
                                    List<Neighbour> l = ((Neighbour)neighbours.get( neighbours.size()-1)).player.getNeighbours();
                                    Iterator<Neighbour> i = l.iterator();
                                    while ( i.hasNext() ) {
                                            Neighbour new_n = (Neighbour)i.next();
                                            Player new_p = new_n.player;
                                            if (!new_p.equals(myid) && !new_p.equals(player) ) {
                                                    logger.info( "Numero di vicini dopo : " + neighbours.size());
                                                    logger.info( "ok, ora ho un nuovo vicino: "   + new_n.player_id);
                                                    neighbours.add(new_n);
                                                    System.out.println("wegfwgasg: " + neighbours.size());
                                                    break;
                                            }
                                    }
                                    done = true;
                                    
                            } catch (RemoteException re) {
                                    logger.log(Level.WARNING, "L'ultimo vicino della lista "
                                                    + "e` andato in crash, lo rimuovo e riprovo... " + player_id,
                                                    re);    
                                    message.addToCrashed(player_id);
                                    neighbours.remove(neighbours.size()-1);
                                    sendUpdate(token, true); 
                                 
                            }
                    }
                    if (neighbours.size() == 0) {
                            sendAck(sender, message);
                  
                    }

            }
            if (quitplayers == null) {
                    quitplayers = new HashSet<Integer>();
            }
            quitplayers.add(new Integer(player_id));

        	if (nextplayerquit) {  		
        		token = manager.notifyQuitPlayers(quitplayers,token); 
        		nextplayerquit = false;
			} else{
				manager.updateLocalFromToken(token);
				
			}
           
        	
        vista.disegnaTavolo(vista.getTavolo(), this);
        vista.updateTableScoreInfo(manager); 
          
        if(myturnquit){
	    	   synchronized (this) {
		            myturn = true;
		            token.setPlayerTurn(this);
					token.setIdPlayerTurn(myid);
		        }
		        logger.info("Cancello il Timer, è il mio turno");
		        timer.cancel();
		        timerPing.cancel();
		        
		        try {
					logger.info("Creo un nuovo START TIMER");
					Message last_message_new = new Message(this,"START TIMER");
					logger.info("Creo il messaggio : " + last_message_new.toString());
					countdown=0;
					this.makeBanner(last_message_new);
					Thread newTimer = new StartTimerThread( last_message_new,this,countdown);
					newTimer.start();
					newTimer.join();
				} catch (Exception e) {
					logger.log(Level.SEVERE, "Eccezione in StartTimerThread", e);
				}
		        
		        try {
					Message ping_message = new Message(this,"PING TIMER");
					this.makeBanner(ping_message);
					logger.info("Creo il messaggio : " + ping_message.toString());
					Thread tt = new PingTimerThread(ping_message, this,this.myid);
					tt.start();
					}catch (Exception e) {
						logger.log(Level.SEVERE, "Eccezione in StartTimerThread", e);
					}
		                 
		          vista.yourTurn(manager);
		           myturnquit=false;
	      }
        

    
	      Thread t=  new SendQuit(message, this, player, player_id, myturnquit,nextplayerquit,token,countdown);
	      t.start();
	      try {
			t.join();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
                       
    }  
   
    
    logger.exiting("PlayerImpl", "quit");
}

	

	
	private boolean messageNeedsForwarding(Message message) throws RemoteException {
		logger.entering("PlayerImpl", "messageNeedsForwarding");  
		boolean no_forward = message.received(this); 
		//  logger.info("Sono nella lista dei ricevitori del messaggio: " + message.toString()+ " " + no_forward);   
		//Quando un nodo riceve un messaggio che ha gi`a ricevuto, tale messaggio
		// ha fatto il giro completo dellï¿½anello e non deve quindi essere propagato
		// oltre: il nodo quindi si limita a rispondere con un messaggio di
		// acknowledge a tutti i mittenti nel vettore.
		if (no_forward) {
			logger.info("Messaggio "+message.toString() + " creato da " + message.getOriginId() + " gia' ricevuto");           
			//logger.info("Message senders size: " + message.getSenders().size());
			Iterator<Player> i = message.getSenders().iterator(); 			
			while(i.hasNext()) {
				try {
				Player next = (Player)i.next();
				if (this.equals(next)) {
					//     logger.info("Non mando ack a me stesso...");
					deliveryNotify(message);
				} else {      
					logger.info("Invio ack al Sender: " + next.getId() + " per il messaggio " + message.toString());
					sendAck(next, message);
				}
				}catch (Exception e) {
					logger.info("Il sender del messaggio se n'e' andato");
				}
			}   
			logger.info( "Non devo inoltrare il messaggio: " + message);
			logger.exiting("PlayerImpl ", "messageNeedsForwarding");
			return false;
		} else {        
			logger.info( "Devo inoltrare il messaggio: " + message);
			logger.exiting("PlayerImpl", "messageNeedsForwarding");
			return true;
		}
	}

	public void ack(Player mitt,Message message) throws RemoteException {
		logger.info("Ricevuto ack da "+ mitt.getId() + " per il messaggio: " + message);
		deliveryNotify(message);
	}

	private void sendAck(Player dest, Message message) {
		try {
			dest.ack(this,message);
		} catch (RemoteException re) {
			// non facciamo niente	                    
		}
	}

	public int getId() throws RemoteException {
		return myid;
	}

	public List<Neighbour> getNeighbours() throws RemoteException {
		return neighbours;
	}
	
	public ArrayList<Integer> getNeighboursID() throws RemoteException {
		ArrayList<Integer> neighboursId = new ArrayList<Integer>();
	 //	System.out.println("Neighbours Size  " + neighbours.size() );
		for (int i =0; i < neighbours.size(); i++) {
			neighboursId.add(neighbours.get(i).player_id);
		}
		return neighboursId;
	}

	public Player getNext() throws RemoteException {
		if (neighbours == null || neighbours.isEmpty()) {
			return null;
		}
		try {
			return ((Neighbour)neighbours.get(0)).player;
		} catch (Exception e) {
			throw new RemoteException(e.getMessage());
		}
	}

	public synchronized void setId(int id) throws RemoteException {
		myid = id;
	}

	public PlayersUI getVista() {
		return vista;
	}

	public void setVista(PlayersUI vista) {
		this.vista = vista;
	}

	public boolean isMy_turn() {
		return myturn;
	}

	public void setMy_turn(boolean myTurn) {
		myturn = myTurn;
	}

	public String toString() {
		return "<Player " + myname + " con ID " + myid + ">";
	}
	
	public void setPlayerName(String name)throws RemoteException{
		myname=name;
	}
    
	public String getPlayerName()throws RemoteException{
		return myname;
	}

	public int getLeader_id() {
		return leaderid;
	}

	public void setLeader_id(int leader_id) {
		this.leaderid = leader_id;
	}

    public void deleteTimers(){
    	timer.cancel();
    	timerPing.cancel();
    	
    }

}
