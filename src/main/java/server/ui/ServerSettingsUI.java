package server.ui;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import java.awt.*;

import javax.swing.BoxLayout;
import javax.swing.SwingConstants;
import javax.swing.JButton;
import java.awt.FlowLayout;

import javax.swing.WindowConstants;


/**
 * Crea un frame per inserire i parametri per connettersi al server
 */
public class ServerSettingsUI extends JFrame {
	private static final long serialVersionUID = 0;
	
	private static String DEFAULT_ADDRESS_IP = "localhost";
	private static String DEFAULT_GAME_NAME = "105";
	private static String DEFAULT_PORTA_HOST = "1099";
	private JPanel jPanelMain;
	private JPanel jPanel1;
	private JPanel jPanel3;
	private JPanel jPanel2;
	private JPanel jPanel4;
	private JLabel nameLabel;
	private JLabel portaLabel;
	private JLabel serverLabel;
	private JButton startServer;
	private JTextField gameNameTF;
	private JTextField serverTF;
	private JTextField portaTF;
	
	/**
	 * L'indirizzo del server
	 */
	public String serverURL;
	
	/**
	 * Il nome del gioco
	 */
	public String gameName;
	
	public static void main(String[] args) {
		ServerSettingsUI inst = new ServerSettingsUI();
		inst.setVisible(true);
		inst.setEnabled(true);
	}
	
	/**
	 * Lancia una nuova finestra per inserire i parametri di connessione
	 * con il server
	 */
	public ServerSettingsUI() {
		// super();
		initGUI();
	}
	
	
	private void initGUI() {
		try {
			

			setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
			this.setTitle("Lancia il server");
			
			Dimension screen = Toolkit.getDefaultToolkit().getScreenSize();
			 int X = (screen.width / 2) - (300 / 2); // Center horizontally.
			 int Y = (screen.height / 2) - (170 / 2); // Center vertically.

			 this.setBounds(X,Y , 300,170);
			 this.setSize(300, 170);
			 this.setResizable(false);
			{
				jPanel2 = new JPanel();
				BoxLayout jPanel2Layout = new BoxLayout(jPanel2, javax.swing.BoxLayout.Y_AXIS);
				jPanel2.setLayout(jPanel2Layout);
				this.getContentPane().add(jPanel2);
				{
					jPanelMain = new JPanel();
					jPanel2.add(jPanelMain);
					FlowLayout jPanelMainLayout = new FlowLayout();
					jPanelMain.setLayout(jPanelMainLayout);
					{
						serverLabel = new JLabel("Server");
						serverLabel.setHorizontalAlignment(SwingConstants.RIGHT);
						serverLabel.setHorizontalTextPosition(SwingConstants.LEFT);
						serverLabel.setMinimumSize(new Dimension(50, 0));
						serverLabel.setSize(80, 15);
						jPanelMain.add(serverLabel);
						serverLabel.setText("Server");
						serverLabel.setPreferredSize(new java.awt.Dimension(50, 15));
					}
					{
						serverTF = new JTextField(15);
						serverTF.setText(DEFAULT_ADDRESS_IP);
						jPanelMain.add(serverTF);
						// serverURL.setHorizontalAlignment(SwingConstants.RIGHT);
					}
				}
				
				{
					jPanel4 = new JPanel();
					jPanel2.add(jPanel4);
					FlowLayout jPanel4Layout = new FlowLayout();
					jPanel4.setLayout(jPanel4Layout);
					{
						portaLabel = new JLabel("Porta");
						portaLabel.setMaximumSize(new Dimension(32, 14));
						portaLabel.setMinimumSize(new Dimension(50, 0));
						jPanel4.add(portaLabel);
						portaLabel.setSize(85, 15);
						portaLabel.setHorizontalAlignment(SwingConstants.RIGHT);
						portaLabel.setHorizontalTextPosition(SwingConstants.LEFT);
						portaLabel.setText("Porta");
						portaLabel.setPreferredSize(new java.awt.Dimension(50, 15));
					}
					{
						portaTF = new JTextField(15);
						jPanel4.add(portaTF);
						portaTF.setText(DEFAULT_PORTA_HOST);
					}
				}
				
				{
					jPanel1 = new JPanel();
					jPanel2.add(jPanel1);
					FlowLayout jPanel1Layout = new FlowLayout();
					jPanel1.setLayout(jPanel1Layout);
					{
						nameLabel = new JLabel("Tavolo");
						jPanel1.add(nameLabel);
						nameLabel.setSize(90, 15);
						nameLabel.setHorizontalAlignment(SwingConstants.RIGHT);
						nameLabel.setHorizontalTextPosition(SwingConstants.LEFT);
						nameLabel.setText("Tavolo");
						nameLabel.setPreferredSize(new java.awt.Dimension(50, 15));
					}
					{
						gameNameTF = new JTextField(15);
						jPanel1.add(gameNameTF);
						gameNameTF.setText(DEFAULT_GAME_NAME);
					}
				}
				
			
				{
					jPanel3 = new JPanel();
					jPanel2.add(jPanel3);
					{
						startServer = new JButton("Play");
						
						startServer.setPreferredSize(new java.awt.Dimension(146, 25));
                        startServer.setActionCommand("startServer");
						jPanel3.add(startServer);
						startServer.setText("Lancia il Server");
						getRootPane().setDefaultButton(startServer);
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
    /**
     * Ritorna il bottone per lanciare il server
     * @return il bottone per lanciare il server
     */
    public JButton getStartServer() {
        return startServer;
    }
	

	
	/**
     * Ritorna l'indirizzo del server scelto dal giocatore
	 * @return l'indirizzo del server scelto dal giocatore
	 */
	public String getServerURL() {
		return serverTF.getText();
	}
	
	/**
     * Ritorna il nome del giocatore
	 * @return il nome del giocatore
	 */
	public String getGameName() {
		return gameNameTF.getText();
	}

	public String getPorta() {
		return portaTF.getText();
	}
	
	public void popUpControlla (){
    	JOptionPane.showMessageDialog(this,"ATTENZIONE Campo vuoto", "ERRORE", JOptionPane.ERROR_MESSAGE);
    }

}
