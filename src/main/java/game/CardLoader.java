/*
 * Created on 3-feb-2005
 *
 * To change the template for this generated file go to
 * Window - Preferences - Java - Code Generation - Code and Comments
 */
package game;


import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.util.ArrayList;
import java.util.logging.Logger;


public class CardLoader {

    /**
     * Il file di configurazione delle carte
     */
    public static String DEFAULT_CARDS_FILE = CardLoader.class.getResource("/cards.xml").toString();
    private static Logger logger;
    
    /**
     * Carica le carte nel mazzo
     * @return la lista delle carte
     * @throws Exception
     */
    public static ArrayList<Card> loadCards() throws Exception {
        return loadListCards(DEFAULT_CARDS_FILE);
    }
    
    /**
     * Restituisce la lista delle carte prese dal file di configurazione uri
     * @param uri il file xml delle carte
     * @return la lista delle carte del gioco
     * @throws Exception
     */
    public static ArrayList<Card> loadListCards(String uri) throws Exception {
        DocumentBuilder builder;
        Document document;
        NodeList card;
        ArrayList<Card> retval = null;

        try {
            builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
            document = builder.parse(uri);
            card = document.getElementsByTagName("card");
            retval = new ArrayList<Card>();
            int count = 1;

            for (int i = 0; i < card.getLength(); ++i) {
                Element card_elem = (Element) card.item(i);
                String valoreUno = card_elem.getElementsByTagName("valore").item(0).getTextContent();
                int valore = Integer.parseInt(valoreUno);
                String scoreUno = card_elem.getElementsByTagName("score").item(0).getTextContent();
                int score = Integer.parseInt(scoreUno);
                String seme = card_elem.getElementsByTagName("seme").item(0).getTextContent();
                String image = card_elem.getElementsByTagName("image").item(0).getTextContent();
                retval.add(new Card(valore, score, seme, image, count++));

            }
        } catch (Exception e){
            logger.warning("Error loadListCards: " + e.getMessage());
        }

        return retval;
    }
    

}
