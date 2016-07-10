package server.ui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.logging.ConsoleHandler;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.WindowConstants;



/**
 * Lancia la GUI del server
 */
public class ServerUI extends JFrame implements ActionListener {
	
	private static final long serialVersionUID = 0;
	
	
	private JPanel jPanel1;
	private JTextArea jTextPane1;
	private JScrollPane jScrollPane1;
	private JPanel jPanel2;
	private JLabel jLabel1;
	private String serverString;
	
	private static Logger log;

	public static void main(String[] args) {
        log = Logger.getLogger("game.server");
        log.setUseParentHandlers(false);
        ConsoleHandler handler = new ConsoleHandler();
        handler.setLevel(Level.ALL);
        log.setLevel(Level.ALL);
        log.addHandler(handler);
        
        new ServerUI();
	}
	
	/**
     * Lancia la GUI del server
	 */
	public ServerUI() {
		super();
		initGUI();
	}
	
	private void initGUI() {
		try {
			setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
			this.setTitle("J105 Server");
			Dimension screen = Toolkit.getDefaultToolkit().getScreenSize();
			 int X = (screen.width / 2) - (400 / 2); // Center horizontally.
			 int Y = (screen.height / 2) - (300 / 2); // Center vertically.

			 this.setBounds(X,Y , 400,300);
			 this.setSize(400, 300);
		//	this.setLocation(500, 250);
			{
				jPanel1 = new JPanel();
				this.getContentPane().add(jPanel1, BorderLayout.NORTH);
				{
					jLabel1 = new JLabel();
					jPanel1.add(jLabel1);
					jLabel1.setText("Server J105");
				}
			}
			{
				jPanel2 = new JPanel();
				BorderLayout jPanel2Layout = new BorderLayout();
				jPanel2.setLayout(jPanel2Layout);
				this.getContentPane().add(jPanel2, BorderLayout.CENTER);
				{
					jScrollPane1 = new JScrollPane();
					jPanel2.add(jScrollPane1, BorderLayout.CENTER);
					jScrollPane1.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
					{
						jTextPane1 = new JTextArea();
						jScrollPane1.setViewportView(jTextPane1);
						jTextPane1.setRequestFocusEnabled(false);
						jTextPane1.setEditable(false);
						jTextPane1.setFocusable(false);
						jTextPane1.setLineWrap(true);
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	
	public void actionPerformed(ActionEvent evt) {
	}

	/**
     * Imposta l'indirizzo del server a URl
     * @param URL l'indirizzo del server
	 */
	public void setServerString (String URL){
		serverString = URL;
        jLabel1.setText("Server J105: " + URL);
	}
	
    /**
     * Ritorna l'indirizzo del server
     * @return l'indirizzo del server
     */
	public String getServerString (){
		return serverString;
	}
	
    /**
     * Aggiunge un log alla GUI
     * @param text il testo da appendere
     */
	public void addLog(String text){
        jTextPane1.append(text + "\n");
        jTextPane1.setCaretPosition(jTextPane1.getText().length());
	}
}
