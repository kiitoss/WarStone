package carte;

import java.awt.Graphics;
import java.util.List;

import element.Element;
import element.Heros;
import element.Soldat;
import utile.Position;
import wargame.PanneauJeu;

public interface ICarte {
	
	Element getElement(Position pos);
	Position trouvePositionVide(); // Trouve al�atoirement une position vide sur la carte
	Position trouvePositionVide(Position pos); // Trouve une position vide choisie
								// al�atoirement parmi les 8 positions adjacentes de pos
	Heros trouveHeros(); // Trouve al�atoirement un h�ros sur la carte
	Heros trouveHeros(List<Heros>listeHeros); // Trouve un h�ros choisi al�atoirement
											 // parmi les 8 positions adjacentes de pos
	boolean deplaceSoldat(Position pos, Soldat soldat);
	void mort(Soldat perso);
	boolean actionHeros(PanneauJeu pj, Position pos, Position pos2);
	void jouerSoldats(PanneauJeu pj);
	void toutDessiner(Graphics g);
}