package wargame;

import java.awt.Color;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.Dimension;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenuBar;
import javax.swing.SwingConstants;


public interface IConfig extends java.io.Serializable {
	
	// Element de la fenetre partager entre la class PanneauJeu & FenetreJeu
	public static final JFrame frame = new JFrame("WarStone");	
	public static final JMenuBar menuBar = new JMenuBar();
	public static final JLabel footer = new JLabel("", SwingConstants.CENTER);		
	
	/* A changer pour mettre en pleine ecran */
	Dimension taille = Toolkit.getDefaultToolkit().getScreenSize();
	
	int LARGEUR_CARTE_CASE = 20; int HAUTEUR_CARTE_CASE = 10; // en nombre de cases
	
	
	int LARGEUR_CARTE = (int)taille.getWidth();
	int HAUTEUR_CARTE = (int)taille.getHeight();
	
	int NB_PIX_CASE = Math.min(LARGEUR_CARTE / LARGEUR_CARTE_CASE, HAUTEUR_CARTE / HAUTEUR_CARTE_CASE);
	int POSITION_X = 100; int POSITION_Y = 50; // Position de la fen�tre
	int NB_HEROS = 6; int NB_MONSTRES = 15; int NB_OBSTACLES = 20;

	// Parametre de dessin 
	Color COULEUR_VIDE = Color.white, COULEUR_INCONNU = Color.lightGray;
	Color COULEUR_TEXTE = Color.black, COULEUR_MONSTRE = new Color(0, 0, 0, 60);
	Color COULEUR_HEROS = Color.red, COULEUR_HEROS_DEJA_JOUE = new Color(175, 25, 75, 100);
	Color COULEUR_EAU = Color.blue, COULEUR_FORET = Color.green, COULEUR_ROCHER = Color.gray;
	Color COULEUR_GRILLE = Color.black; Color COULEUR_MENUBAR = Color.gray; Color COULEUR_FOOTER = Color.black;
	Color COULEUR_DEPLACEMENT = new Color(75, 25, 75, 100), COULEUR_PORTEE = new Color(20, 25, 25, 100);
	Color COULEUR_VIE_R = Color.red, COULEUR_VIE_V = Color.green;
	Color COULEUR_ENEMIS = new Color(255, 10, 20, 50), COULEUR_AMIS = new Color(20,200,10,50);
	
	// Les images 
	Image grass = Toolkit.getDefaultToolkit().getImage("./res/img/background/grass.png");
	Image range = Toolkit.getDefaultToolkit().getImage("./res/img/background/range.jpg");
	Image fleche = Toolkit.getDefaultToolkit().getImage("./res/img/pops/arrow.png");
	
	// Constante pour centrer la barre de vie
	int PADDING_VIE_CASE_LARGEUR = NB_PIX_CASE/8;
	int PADDING_VIE_CASE = PADDING_VIE_CASE_LARGEUR/2;
	
	
	final int MAX_FEN_LARGEUR = 500;
	final int MAX_FEN_HAUTEUR = 500;
	final int PADDING_BAS = 41;
	final int PADDING_DROIT = 18;

	/* Variables calcul�es */
	final int FEN_LARGEUR = LARGEUR_CARTE * NB_PIX_CASE + PADDING_DROIT;
	
	/* Variable Menu */
	final int MENUBAR_HAUTEUR = 35;
	final int MENUBAR_LARGEUR = FEN_LARGEUR;
	
	/* Variable footer */
	final int FOOTER_HAUTEUR = 25;
	final int FOOTER_LARGEUR = FEN_LARGEUR;
	final int FEN_HAUTEUR = HAUTEUR_CARTE * NB_PIX_CASE + PADDING_BAS + MENUBAR_HAUTEUR + FOOTER_HAUTEUR;
	
	
	/* Variable du Boutton */
	final int BOUTTON_HAUTEUR = MENUBAR_HAUTEUR/2;
	final int BOUTTON_LARGEUR = MENUBAR_LARGEUR/6;
			

}