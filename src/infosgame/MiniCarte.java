package infosgame;

import java.awt.Graphics;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JPanel;
import javax.swing.SwingUtilities;

import carte.Camera;
import carte.Carte;
import utile.Position;
import wargame.IConfig;

/**
 * Class MiniCarte.
 */
public class MiniCarte extends JPanel implements IConfig {
	
	/** Constante serialVersionUID. */
	private static final long serialVersionUID = 1L;
	
	/** La Carte. */
	private Carte c;
	
	/** La camera. */
	private Camera cam;
	
	/** La pos du clic sur la miniCarte. */
	private Position clic;
	
	/**
	 * Instancie une nouvelle mini carte.
	 *
	 * @param cam 
	 */
	public MiniCarte(Camera cam) {
		this.c = cam.c;
		this.cam = cam;
		this.miniCarteEvent();
	}
	
	/**
	 * Mini carte event.
	 * 
	 * deplacement de la carte en fonctions des cliques sur la mini Carte
	 * 
	 */
	private void miniCarteEvent() {
			
		addMouseListener(new MouseAdapter() {
			public void mousePressed(MouseEvent e) {
				if (SwingUtilities.isLeftMouseButton(e)) {
					clic = new Position(e.getX() / MINI_NB_PIX_CASE, e.getY() / MINI_NB_PIX_CASE);
					if(clic.getX() < cam.getDx()) {
						cam.setDx(clic.getX());
					}
					else if(clic.getX() > cam.getDx() - 1 + LARGEUR_CASE_VISIBLE) {
						Position pos = new Position(clic.getX() + 1, 0);
						cam.setDx((int) pos.distance(new Position(LARGEUR_CASE_VISIBLE, 0)));
					}
					if(clic.getY() < cam.getDy()) {
						cam.setDy(clic.getY());
					}
					else if(clic.getY() > (cam.getDy() - 1  + HAUTEUR_CASE_VISIBLE)) {
						Position pos = new Position(0, clic.getY() + 1);
						cam.setDy((int) pos.distance(new Position(0, HAUTEUR_CASE_VISIBLE)));
					}
				}
			}
		});
	}
	
	/**
	 * Dessine focus.
	 *
	 * Dessin de la zone visible sur la carte
	 *
	 * @param g 
	 * @param dx 
	 * @param dy
	 */
	private void dessineFocus(Graphics g, int dx, int dy) {
		for(int i = 0; i < LARGEUR_CASE_VISIBLE; i++) {
			for(int j = 0; j < HAUTEUR_CASE_VISIBLE; j++) {
				g.setColor(COULEUR_FOCUS);
	    		g.fillRect(i * MINI_NB_PIX_CASE + dx * MINI_NB_PIX_CASE, j * MINI_NB_PIX_CASE + dy * MINI_NB_PIX_CASE, MINI_NB_PIX_CASE, MINI_NB_PIX_CASE); 
			}
		}
	}
	
	/**
	 * Dessine mini carte.
	 *
	 * @param g
	 */
	private void dessineMiniCarte(Graphics g) {
		/** on dessine les heros et element a leur portee */
		for(int n = 0; n < this.c.listeHeros.size(); n++)
			this.c.listeHeros.get(n).seDessinerMinia(g);
	
		/** dessin de la grille */
		for(int i = 0; i < LARGEUR_CARTE_CASE; i++) {
			for(int j = 0; j < HAUTEUR_CARTE_CASE; j++) {
				g.setColor(COULEUR_GRILLE);
				g.drawRect(i * MINI_NB_PIX_CASE, j * MINI_NB_PIX_CASE, MINI_NB_PIX_CASE, MINI_NB_PIX_CASE); 
			}
		}
	}
	
	/**
	 * Dessine carte.
	 *
	 * @param g the g
	 */
	private void dessineCarte(Graphics g) {
		for(int i = 0; i < LARGEUR_CARTE_CASE; i++) {
			for(int j = 0; j < HAUTEUR_CARTE_CASE; j++) {		
				g.drawImage(range, i * MINI_NB_PIX_CASE, j  * MINI_NB_PIX_CASE, MINI_NB_PIX_CASE, MINI_NB_PIX_CASE, null);
				if(!this.c.estCaseVide(new Position(i, j)))
					this.c.getElement(new Position(i, j)).seDessinerMinia(g);
			}
		}
	}
	/**
	 * Paint component.
	 *
	 * @param g the g
	 */
	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		
		if(!Carte.modeConf)
			g.drawImage(grass, 0, 0, LARGEUR_MINI_CARTE, HAUTEUR_MINI_CARTE, null);
		else
			dessineCarte(g);
		
		this.dessineMiniCarte(g);
		this.dessineFocus(g, this.cam.getDx(), this.cam.getDy());

	}
}
