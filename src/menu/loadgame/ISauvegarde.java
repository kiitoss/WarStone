package menu.loadgame;

import java.awt.FlowLayout;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JPanel;

import utile.style.Bouton;

/**
 * Interface ISauvegarde.
 */
public interface ISauvegarde {
	
	/**  Liste de boutton des parties sauvegarder. */
	List<Bouton> listeBoutton = new ArrayList<>();
	
	/**  Liste des chemins des parties sauvegarder. */
	List<String> listeSauvegarde = new ArrayList<>();
	
	/**  le chemin des sauvegardes. */
	String chemin = "./res/sauvegarde/";
	
	/**  NB MAX de partie. */
	int MAX_SAUVEGARDE = 3;
	
	/**  Panel principal pour la page loadgame. */
	JPanel panelLoadGame = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 0));

}
