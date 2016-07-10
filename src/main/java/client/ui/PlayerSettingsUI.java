package client.ui;


import javax.swing.*;
import java.awt.*;


/**
 * Frame per inserire i parametri per connettersi al server
 */
public class PlayerSettingsUI extends javax.swing.JFrame {
	private static final long serialVersionUID = 0;
	
	private static String DEFAULT_URL = "localhost";
	
	private boolean done = false;
	private JPanel hostServer;
	private JPanel giocatore;
	private JPanel bottone;
	private JPanel jPanel2;
	private JPanel porta;
	private JLabel nameLabel;
	private JLabel serverLabel;
	private JLabel portaLabel;
	private JButton startGame;
	private JTextField playerNameTF;
	private JTextField serverTF;
	private JTextField portaTF;
	public String playerName;
	private JPanel tavolo;
	private JLabel lblTavolo;
	private JTextField tavoloTF;
	

	public String serverURL;

	
	public static void main(String[] args) {
		PlayerSettingsUI inst = new PlayerSettingsUI();
		inst.setVisible(true);
		inst.setEnabled(true);
	}
	
	/**
	 * Lancia una nuova finestra per inserire i parametri di connessione
	 * con il server
	 */
	public PlayerSettingsUI() {
		// super();
		initGUI();
	}
	
	
	private void initGUI() {
		try {
			

			setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
			this.setTitle("Avvia il gioco");
			Dimension screen = Toolkit.getDefaultToolkit().getScreenSize();
			int X = (screen.width / 2) - (380 / 2); // Center horizontally.
			int Y = (screen.height / 2) - (380 / 2); // Center vertically.

			this.setBounds(X,Y , 380,200);
			this.setSize(380, 201);
			this.setResizable(false);
			{
				jPanel2 = new JPanel();
				BoxLayout jPanel2Layout = new BoxLayout(jPanel2, javax.swing.BoxLayout.Y_AXIS);
				jPanel2.setLayout(jPanel2Layout);
				this.getContentPane().add(jPanel2);
				{
					hostServer = new JPanel();
					jPanel2.add(hostServer);
					FlowLayout fl_hostServer = new FlowLayout();
					hostServer.setLayout(fl_hostServer);
					{
						serverLabel = new JLabel("Server");
						serverLabel.setPreferredSize(new Dimension(60, 15));
						serverLabel.setHorizontalAlignment(SwingConstants.CENTER);
						serverLabel.setHorizontalTextPosition(SwingConstants.LEFT);
						serverLabel.setMinimumSize(new Dimension(90, 15));
						serverLabel.setSize(80, 15);
						hostServer.add(serverLabel);
					}
					{
						serverTF = new JTextField(15);
						serverTF.setText(DEFAULT_URL);
						hostServer.add(serverTF);
				
					}
				}
				{
					porta = new JPanel();
					jPanel2.add(porta);
					porta.setLayout(new FlowLayout());
					{
						portaLabel = new JLabel("Porta");
						portaLabel.setPreferredSize(new Dimension(60, 15));
						portaLabel.setHorizontalTextPosition(SwingConstants.LEFT);
						portaLabel.setHorizontalAlignment(SwingConstants.CENTER);
						porta.add(portaLabel);
					}
					{
						portaTF = new JTextField(15);
						portaTF.setText("1099");
						porta.add(portaTF);
					}
				}
				{
					tavolo = new JPanel();
					jPanel2.add(tavolo);
					tavolo.setLayout(new FlowLayout());
					{
						lblTavolo = new JLabel("Tavolo");
						lblTavolo.setPreferredSize(new Dimension(60, 15));
						lblTavolo.setHorizontalTextPosition(SwingConstants.LEFT);
						lblTavolo.setHorizontalAlignment(SwingConstants.CENTER);
						tavolo.add(lblTavolo);
					}
					{
						tavoloTF = new JTextField(15);
						tavoloTF.setText("105");
						tavolo.add(tavoloTF);
					}
				}
				{
					giocatore = new JPanel();
					jPanel2.add(giocatore);
					FlowLayout fl_giocatore = new FlowLayout();
					giocatore.setLayout(fl_giocatore);
					{
						nameLabel = new JLabel("User");
						nameLabel.setMaximumSize(new Dimension(60, 14));
						nameLabel.setMinimumSize(new Dimension(60, 14));
						nameLabel.setPreferredSize(new Dimension(60, 15));
						nameLabel.setSize(80, 15);
						nameLabel.setHorizontalAlignment(SwingConstants.CENTER);
						nameLabel.setHorizontalTextPosition(SwingConstants.LEFT);
						giocatore.add(nameLabel);
					}
					{
						playerNameTF = new JTextField(15);
						giocatore.add(playerNameTF);
					}
				}
				{
					bottone = new JPanel();
					jPanel2.add(bottone);
					{
						startGame = new JButton("Accedi");
						
						startGame.setPreferredSize(new Dimension(113, 25));
                        startGame.setActionCommand("login");
						bottone.add(startGame);
						getRootPane().setDefaultButton(startGame);
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
    /**
     * Ritorna il pulsante per iniziare il gioco
     * @return il pulsante di inizio gioco
     */
    public JButton getStartGame() {
        return startGame;
    }
	
	private void setComponentPopupMenu(
		final java.awt.Component parent,
		final javax.swing.JPopupMenu menu) {
		parent.addMouseListener(new java.awt.event.MouseAdapter() {
			public void mousePressed(java.awt.event.MouseEvent e) {
				if (e.isPopupTrigger())
					menu.show(parent, e.getX(), e.getY());
			}
			public void mouseReleased(java.awt.event.MouseEvent e) {
				if (e.isPopupTrigger())
					menu.show(parent, e.getX(), e.getY());
			}
		});
	}
	
	/**
     * Ritorna l'indirizzo del server scelto
	 * @return l'indirizzo del server scelto dal giocatore
	 */
	public String getServerURL() {
		return serverTF.getText();
	}
	
	/**
     * Ritorna il nome del giocatore
	 * @return il nome del giocatore
	 */
	public String getPlayerName() {
		return playerNameTF.getText();
	}
	
	public String getTavoloTF() {
		return tavoloTF.getText();
	}
	
	public String getPortaTF() {
		return portaTF.getText();
	}
	
	public void popUpControllaNome (){
    	JOptionPane.showMessageDialog(this,"Inserisci il tuo nome", "ERRORE", JOptionPane.ERROR_MESSAGE);
    }
	
	public void popUpGiocoIniziato (){
    	JOptionPane.showMessageDialog(this,"Il gioco è gia iniziato, riprova più tardi...", "ATTENZIONE", JOptionPane.INFORMATION_MESSAGE );
    }

}
