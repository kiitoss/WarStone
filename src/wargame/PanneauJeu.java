package wargame;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JPanel;
import javax.swing.SwingUtilities;

import carte.Camera;
import carte.Carte;
import element.Element;
import element.Heros;
import element.Obstacle;
import fenetrejeu.IFenetre;
import infosgame.InfosElement;
import infosgame.MiniCarte;
import sprite.ISprite;
import utile.Fleche;
import utile.Position;
import wargame.evenement.ButtonEvent;


/**
 * Class PanneauJeu.
 */
public class PanneauJeu extends JPanel implements IFenetre, ISprite {
	private static final long serialVersionUID = 1L;

	public Position clique, dernierClique, cliqueDragged;
	private Position draggedCam, cliqueRelache;
	public int nombreHeros, nombreMonstre;
	public Fleche flecheDirectionnelle;
	public ButtonEvent buttonEvent;
	private boolean dessineFleche;
	public Heros herosSelectione;
	public boolean estFiniAction;
	private	Position survol;
	public Element elem;
	public Camera cam;
	public Carte c;
	public Position deposeObstacle;

	/**
	 * Instantiates a new panneau jeu.
	 */
	public PanneauJeu(Carte c) {	
		
		this.c = c;
		this.cam = new Camera(c, 0, 0);
		
		this.flecheDirectionnelle = new Fleche(this.cam);
	
		this.herosSelectione = null;		
		
		this.elem = null;
		this.dessineFleche = false;
		this.estFiniAction = true;
				
		this.deposeObstacle = null;
		
		this.buttonEvent = new ButtonEvent(this);		
		this.gestionEvenement();
		
		c.nombreSoldatVivant(this);
	} 
	
	public void setPanneauJeu(Carte c) {
		this.c = c;
			
		this.majMiniCarte();
		
		this.flecheDirectionnelle = new Fleche(this.cam);
		this.herosSelectione = null;		
			
		infosElementBody.removeAll();
		infosElementBody.revalidate();
				
		this.elem = null;
		this.dessineFleche = false;
		this.estFiniAction = true;
		
		c.nombreSoldatVivant(this);
	}		
	
	public void setPanneauJeuConf(Carte c) {
		this.c = c;
			
		this.majMiniCarte();
			
		this.flecheDirectionnelle = new Fleche(this.cam);
		this.herosSelectione = null;		
		
		this.elem = null;
		
		this.dessineFleche = false;
		this.estFiniAction = false;
	}		
			 
	/**
	 * Gestion evenement : souris / boutton.
	 */
	public void gestionEvenement() {
		PanneauJeu pj = this;
		
		// Actualisation des sprites
		ISprite.spriteEngine.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				carteMiniature.repaint();
				repaint();
			}
		});
		
		// Recuperation des clics a la souris
		this.addMouseListener(new MouseAdapter() {
			public void mousePressed(MouseEvent e) {
				if (SwingUtilities.isLeftMouseButton(e)) {
					clique = new Position(e.getX() / TAILLE_CARREAU + cam.getDx(), e.getY() / TAILLE_CARREAU + cam.getDy());
					dernierClique = new Position(e.getX() / TAILLE_CARREAU + cam.getDx(), e.getY() / TAILLE_CARREAU + cam.getDy());
					deposeObstacle = new Position(e.getX() / TAILLE_CARREAU + cam.getDx(), e.getY() / TAILLE_CARREAU + cam.getDy());
					
					if(!c.estCaseVide(clique)) dessineFleche = false;
											
					if(!clique.estValide()) return;	
					
					elem = c.getElement(clique);
				
					InfosElement.dessineInfosElement(elem);
					
					// Si on a Selectionnee un heros et que l'on a effectuer un clic autre part alors on appelle jouerSoldat
					if(elem instanceof Heros && estFiniAction) {
						herosSelectione = (Heros)elem;
						if(dernierClique != null)
							c.jouerSoldats(pj, pj.buttonEvent.tour);
					}
				}
				if(SwingUtilities.isRightMouseButton(e) && Carte.modeConf) {
					Position delete = new Position(e.getX() / TAILLE_CARREAU + cam.getDx(), e.getY() / TAILLE_CARREAU + cam.getDy());
					if(c.getElement(delete) != null)
						c.setElementVide(delete);
				}
			}
			
			public void mouseReleased(MouseEvent e) {
				if (SwingUtilities.isLeftMouseButton(e)) {
					deposeObstacle = null;
					dessineFleche = false;
					cliqueRelache = new Position((int)e.getX() / TAILLE_CARREAU + cam.getDx(), (int)e.getY() / TAILLE_CARREAU + cam.getDy());
					
					// On recupere les clic lorsque la souris est egalement relache
					dernierClique = new Position((int)e.getX() / TAILLE_CARREAU + cam.getDx(), (int)e.getY() / TAILLE_CARREAU + cam.getDy());
					// Si On a un heros de selectionner et que clic actuellement sur autre chose alors on appelle jouerSoldat
					if(dernierClique != null && herosSelectione != null && estFiniAction)
						c.jouerSoldats(pj, pj.buttonEvent.tour);
					
					/** 
					 * 	Option de jeu suplementaire avec MouseDragged 
					 *	Si le clic est relacher dans la case du heros alors on "memorise" le heros selectionner
					 *	Sinon si le clic est relacher sur un enemis ou sur case de deplacement alors laction est effectuer
					 */
					if(herosSelectione != null && dernierClique != null)
						if(!dernierClique.estIdentique(herosSelectione.getPosition()))
							herosSelectione = null;
					/**
					 * gestion des mouvements de la camera en fonction des position de la souris lorsqu'elle est dragged puis released
					 * Cette option fonction uniquement dans le mode jeu et non en mode config
					 *
					 */
					if(deposeObstacle == null && c.getElement(clique) == null && !clique.estIdentique(cliqueRelache) && c.estCaseVide(dernierClique)) {
						int distance = (int) clique.distance(cliqueRelache);
						switch(clique.getPositionCardinal(cliqueRelache)) {
						case NORD: cam.deplacement(0, -distance);
						break;
						case NORD_OUEST: cam.deplacement(-distance, -distance);
						break;
						case OUEST: cam.deplacement(-distance, 0);
						break;
						case SUD_OUEST: cam.deplacement(-distance, distance);
						break;
						case SUD: cam.deplacement(0, distance);
						break;
						case SUD_EST: cam.deplacement(distance, distance);
						break;
						case EST: cam.deplacement(distance, 0);
						break;
						case NORD_EST: cam.deplacement(distance, -distance);
						break;
						default: break;
						}
					}
				}
			}
		});		
		
		/* Affiche les infos des elements survole avec la souris */
		addMouseMotionListener(new MouseAdapter() {
			public void mouseDragged(MouseEvent e) {
				cliqueDragged = new Position ((int)e.getX() / TAILLE_CARREAU + cam.getDx(), (int)e.getY() / TAILLE_CARREAU + cam.getDy());
				draggedCam = new Position ((int)e.getX() / TAILLE_CARREAU + cam.getDx(), (int)e.getY() / TAILLE_CARREAU + cam.getDy());		
				
				if(c.estCaseVide(clique)) dessineFleche = true;
				else dessineFleche = false;
			}	
			
			public void mouseMoved(MouseEvent e) {
				survol = new Position((int)e.getX() / TAILLE_CARREAU + cam.getDx(), (int)e.getY() / TAILLE_CARREAU + cam.getDy());
				
				elem = null;
				
				if(!survol.estValide()) return;
					
				// Onj affiche les elements visible uniquement
				for(Heros h : c.listeHeros)
					if(h.estDedans(survol))
						elem = c.getElement(survol);
				
				/* Si le clic est relacher dans la case du heros on continue a memoriser les position */ 
				if(herosSelectione != null) cliqueDragged = new Position(survol.getX(), survol.getY());
				// Ajouter un moyen de ne pas afficher les elements cache
			}
		});
	}

	/**
	 * Maj mini carte.
	 */
	public void majMiniCarte() {
		cam = new Camera(c, 0, 0);
		// Une supprime l'ancien conteneur
		carteMiniature.removeAll();
		// On valide les changement
		carteMiniature.revalidate();	
		// Ajout de la nouvelle MiniCarte
		carteMiniature.add(new MiniCarte(cam));
	}
	
	/**
	 * Paint component.
	 *
	 * @param g the g
	 */
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		if(Carte.modeConf) {		
			for(int i = cam.getDx(); i < NB_COLONNES_VISIBLES + cam.getDx(); i++) {
				for(int j = cam.getDy(); j < NB_LIGNES_VISIBLES + cam.getDy(); j++) {		
					g.drawImage(terre, i * TAILLE_CARREAU - cam.getDx() * TAILLE_CARREAU, j  * TAILLE_CARREAU - cam.getDy() * TAILLE_CARREAU, TAILLE_CARREAU, TAILLE_CARREAU, null);
					if(this.c.getElement(new Position(i, j)) != null) {
						this.c.getElement(new Position(i, j)).seDessiner(g, this.cam);	
					}
									
					g.setColor(COULEUR_GRILLE);
					g.drawRect(i * TAILLE_CARREAU - cam.getDx() * TAILLE_CARREAU, j  * TAILLE_CARREAU - cam.getDy() * TAILLE_CARREAU, TAILLE_CARREAU, TAILLE_CARREAU); 
				}
			}
			if(deposeObstacle != null && InfosElement.obstacleSelectione != null) {
				c.setElement(new Obstacle(c, InfosElement.obstacleSelectione, deposeObstacle));
			}
			soldatRestant.setText("" + InfosElement.nbElementDeposer + "  MAX : " + NB_OBSTACLES);	
		}
		else {
			g.drawImage(herbe, 0, 0, TAILLE_CARREAU * NB_COLONNES_VISIBLES, TAILLE_CARREAU * NB_LIGNES_VISIBLES, null);
			this.c.toutDessiner(g, cam);
		
			// Affichage du nombre de soldat restant
			soldatRestant.setText("" + nombreHeros + " Heros VS " + nombreMonstre + " Monstre");
								
		    // Affiche les deplacement possible du heros selectionne
			if(this.herosSelectione != null && !this.herosSelectione.aJoue) {
				this.herosSelectione.dessineSelection(g, this.herosSelectione, cliqueDragged, cam);
				this.herosSelectione.changeSprite(cliqueDragged, cam);
			}
		}
		
		 // On verifie si on doit dessiner la fleche ou non
	    if(flecheDirectionnelle.estFlecheDessinable(herosSelectione, dessineFleche, draggedCam)) 
	    	flecheDirectionnelle.dessineFleche(g, clique.getX() * TAILLE_CARREAU - cam.getDx() * TAILLE_CARREAU + TAILLE_CARREAU/2, 
	    			clique.getY() * TAILLE_CARREAU - cam.getDy() * TAILLE_CARREAU + TAILLE_CARREAU/2, 
	    			draggedCam.getX() * TAILLE_CARREAU - cam.getDx() * TAILLE_CARREAU + TAILLE_CARREAU/2, 
	    			draggedCam.getY() * TAILLE_CARREAU - cam.getDy() * TAILLE_CARREAU + TAILLE_CARREAU/2, 
	    			TAILLE_CARREAU/4, TAILLE_CARREAU/6, clique);
	    
	 // Affichage du label en bas de la fenetre
	    if(this.elem != null) footer.setText(" " + this.elem.toString());
	    else footer.setText(" ");
	 			
	}	
}
