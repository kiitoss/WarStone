package fenetrejeu;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;

import javax.swing.BorderFactory;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.border.Border;
import javax.swing.border.CompoundBorder;
import javax.swing.border.LineBorder;
import javax.swing.border.MatteBorder;

import carte.Carte;
import fenetrejeu.configjeu.ElementDeposable;
import fenetrejeu.infosjeu.InfosElement;
import fenetrejeu.infosjeu.MiniCarte;
import fenetrejeu.menubar.MenuBar;
import fenetrejeu.menubar.MenuBarHeader;
import fenetrejeu.panneaujeu.PanneauJeu;
import menu.loadgame.LoadGamePage;


/**
 * Class FenetreJeu.
 */
public class FenetreJeu extends JPanel implements IFenetre{
	
	/** Constant serialVersionUID. */
	private static final long serialVersionUID = 1L;	
	public static final PanneauJeu panneau = new PanneauJeu(new Carte());
	
	/**
	 * Instancie une nouvelle fenetre jeu.
	 * 
	 * Avec mode jeu si false et mode Config si true
	 *
	 * @param c
	 * @param modeConfig
	 */
	public FenetreJeu(Carte c, boolean modeConfig) {
		
		/** Si le joueur lance une partie en mode config on nettoie la carte */
		if(modeConfig) {
			/** On vide la carte */
			c.setCarteVide();	
			/**Nettoie les listes de soldat */
			c.listeHeros.clear();
			c.listeMonstres.clear();
			/** Supprime toute les actions des listes */
			c.removeAllAction();
		}
		
		/** On change le mode de jeux en fonction du choix du joueur */
		Carte.modeConfig = modeConfig;
		
		/** On initialise le panel principal */
		panelPrincipal.setPreferredSize(DIM_FEN);
		panelPrincipal.setLayout(new BorderLayout());
		panelPrincipal.setOpaque(false);	   	
		
		headerPanel.setLayout(new BorderLayout());
		headerPanel.setOpaque(false);	    	
		headerPanel.setBorder(new MatteBorder(0, 2, 2, 0, COULEUR_BORDURE));
		
		cartePanel.setLayout(new BorderLayout());
		cartePanel.setOpaque(false);	    
		
		/** On charge la liste des sauvegardes */
		LoadGamePage.initListeSauvegarde();
						
		/** Creation d'un panel qui contient les infos du jeux */
		JPanel infosPanel = new JPanel(new FlowLayout());
		
		/* on retire l'espace cree par le flowLayout entre la miniCarte et infosPanel 
		 * Utilisation du BorderLayout.CENTER impossible car les dimension sont ignore
		 */
		FlowLayout layout = (FlowLayout)infosPanel.getLayout();
		layout.setVgap(0);
		
		/** infosPanel est le panel contenant les infos sur la partie en cours (miniCarte, nb Soldat Vivant...) */
		infosPanel.setPreferredSize(new Dimension(INFOS_PANEL_LARGEUR, INFOS_PANEL_HAUTEUR));
		infosPanel.setBackground(COULEUR_INFOS_PANEL);

		infosPanel.setOpaque(true);	    	
		infosPanel.setBorder(new MatteBorder(0, 2, 2, 2, COULEUR_BORDURE));
		
		/** On cree un conteneur avec FlowLayout pour pouvoir centrer la miniCarte */
		JPanel miniCartePanel = new JPanel(new FlowLayout(FlowLayout.LEADING, PADDING_MINI_CARTE_GAUCHE, PADDING_MINI_CARTE_HAUT));

		/** On definie les dimensions et la couleurs du background */
		miniCartePanel.setPreferredSize(new Dimension(INFOS_PANEL_LARGEUR, MINI_CARTE_HAUTEUR + PADDING_MINI_CARTE_HAUT * 2));
		miniCartePanel.setBackground(COULEUR_MINI_CARTE_PANEL);
		miniCartePanel.setBorder(new MatteBorder(0, 2, 0, 2, COULEUR_BORDURE));

		/** On centre le panel qui contient la carte dans le conteneur creer juste avant */
		carteMiniaturePanel.setLayout(new BorderLayout());
		/** On definie les dimensions */
		carteMiniaturePanel.setPreferredSize(new Dimension(MINI_CARTE_LARGEUR, MINI_CARTE_HAUTEUR));
		
		// On ajoute la carte au panel
		carteMiniaturePanel.add(new MiniCarte(panneau.cam));	
		
		// On Met des bordure autour de la mini carte
		carteMiniaturePanel.setBorder(new MatteBorder(5, 5, 5, 5, COULEUR_BORDUR_MINI_CARTE));
		
		/** On rajoute ce panel au conteneur miniCartePanel */
		miniCartePanel.add(carteMiniaturePanel);
		
		/** On ajoute enfin on conteneur principal */
		infosPanel.add(miniCartePanel);
			 
		/** On affiche le nombre de soldant restant sur la carte */
		JPanel infosSoldatPanel = new JPanel(new FlowLayout());
		
		/** On retire l'espace creer par le flow Layout pour que la bordure prenne bien toute la Hauteur du panel */
		FlowLayout infosSoldatLayout = (FlowLayout)infosSoldatPanel.getLayout();
		infosSoldatLayout.setVgap(0);
	
		infosSoldatPanel.setBackground(COULEUR_ELEMENT_RESTANT);
		
		/** Alignement du text */		
		elementRestantLabel.setHorizontalAlignment(SwingConstants.CENTER);
		
		/** On centre le label avec BorderLayou.CENTER */
		elementRestantLabel.setLayout(new BorderLayout());
	
		/** On definit la couleur et la taille */
		elementRestantLabel.setPreferredSize(new Dimension(INFOS_PANEL_LARGEUR, ELEMENT_RESTANT_HAUTEUR));
		
		/** Couleur du text et Police */
		elementRestantLabel.setForeground(COULEUR_GRILLE);
		elementRestantLabel.setFont(new Font("Pushster", Font.BOLD, 15));
	 	Border bordureInterieur = BorderFactory.createLineBorder(Color.GRAY, 4);;
    	Border bordureExterieur = new MatteBorder(2, 2, 0, 2, COULEUR_BORDURE);

    	Border compound = new CompoundBorder(bordureExterieur, bordureInterieur);
    	
		elementRestantLabel.setBorder(compound);
		infosSoldatPanel.add(elementRestantLabel);
		
		infosPanel.add(infosSoldatPanel);
		
		infosElementPanel.setPreferredSize(new Dimension(INFOS_PANEL_LARGEUR, INFOS_ELEMENT_PANEL_HAUTEUR));
		infosElementPanel.setBackground(COULEUR_INFOS_PANEL);		
		
		// Couleur et taille du header dans ce panel
		infosElementHeader.setPreferredSize(new Dimension(INFOS_PANEL_LARGEUR - PADDING_INFOS_PANEL*2, ICON_ELEMENT_HAUTEUR));
		infosElementPanel.setBorder(new MatteBorder(2, 2, 0, 2, COULEUR_BORDURE));
		
		// panel contenant l'icon
		infosIconPanel.setBackground(COULEUR_GRILLE);
		infosIconPanel.setPreferredSize(new Dimension(ICON_ELEMENT_LARGEUR, ICON_ELEMENT_HAUTEUR));
		infosIconPanel.setBorder(new MatteBorder(2, 2, 2, 0, COULEUR_BORDURE));
		
		// Taille du label contenant l'icon
		infosIconLabel.setPreferredSize(new Dimension(ICON_ELEMENT_LARGEUR, ICON_ELEMENT_HAUTEUR));
		
		// Label contant les infos de l'elements
		InfosElementLabel.setOpaque(true);
		InfosElementLabel.setBackground(COULEUR_INFOS_ELEMENT_LABEL);

		// On centre le text
		InfosElementLabel.setHorizontalAlignment(SwingConstants.LEFT);
		InfosElementLabel.setFont(new Font("Pushster", Font.BOLD, 15));
		InfosElementLabel.setBorder(new MatteBorder(2, 0, 2, 2, COULEUR_BORDURE));
		
		// On rajoute les elements a leur conteneur parent
		infosIconPanel.add(infosIconLabel);
		infosElementHeader.add(infosIconPanel, BorderLayout.WEST);
		infosElementHeader.add(InfosElementLabel, BorderLayout.CENTER);
		infosElementPanel.add(infosElementHeader, BorderLayout.NORTH);
		
		descriptifElementPanel.setOpaque(true);
		descriptifElementPanel.setBackground(COULEUR_DESCRIPTIF_INFOS_PANEL);
		descriptifElementPanel.setBorder(new LineBorder(COULEUR_GRILLE));
		
		// En mode config on affiche les elements deposable
		descriptifElementPanel.setPreferredSize(new Dimension(DESCRIPTIF_ELEMENT_LARGEUR, DESCRIPTIF_ELEMENT_HAUTEUR));
		
		/** On nettoi le panel contenant les infos des elements */
		InfosElement.supprimeInfosElement();
		ElementDeposable.supprimeLabelDeposable();
		
		if(Carte.modeConfig) 
			descriptifElementPanel.setLayout(new GridLayout(0, 1));
		
		/** Sinon on affiche le panel avec le detail des elements clique */
		else 
			descriptifElementPanel.setLayout(new BorderLayout());
			
		infosElementPanel.add(descriptifElementPanel, BorderLayout.SOUTH);
		infosPanel.add(infosElementPanel);
		
		/** le panneau existe deja on le modifie */
		if(modeConfig)
			panneau.setPanneauJeuConfig(c);
		else
			panneau.setPanneauJeu(c);	
	
		cartePanel.add(panneau, BorderLayout.CENTER);
		
		footerLabel.setBackground(COULEUR_FOOTER);
		footerLabel.setPreferredSize(new Dimension(FOOTER_LARGEUR, FOOTER_HAUTEUR));
		footerLabel.setOpaque(true);
		footerLabel.setFont(new Font("Arial", Font.BOLD, 14));
		footerLabel.setForeground(Color.WHITE);				
		footerLabel.setBorder(new MatteBorder(2, 2, 0, 0, COULEUR_BORDURE));
		cartePanel.add(footerLabel, BorderLayout.SOUTH);
		
		// On ajoute tout les panels a la frame
		panelPrincipal.add(headerPanel, BorderLayout.NORTH);
		panelPrincipal.add(infosPanel, BorderLayout.EAST);
		panelPrincipal.add(cartePanel, BorderLayout.CENTER);		
		
		new MenuBarHeader();
		new MenuBar();
		
		frame.add(panelPrincipal);	
	}
	
	/**
	 * Instancie une nouvelle fenetre jeu en mode jeu.
	 * 
	 *
	 * @param c
	 */
	public FenetreJeu(Carte c){	
		this(c, false);
	}
	
	/**
	 * Instancie une nouvelle fenetre jeu 
	 * 
	 * avec une carte aleatoire et en mode jeu.
	 * 
	 */
	public FenetreJeu(){	
		this(new Carte());
	}
}