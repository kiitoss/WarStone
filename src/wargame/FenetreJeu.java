package wargame;


import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.border.LineBorder;
import javax.swing.border.MatteBorder;

import infosgame.MiniCarte;
import utile.Position;


public class FenetreJeu extends JFrame implements IConfig{
	private static final long serialVersionUID = 1L;	
	private Position currentLocation;

	
	FenetreJeu(){	
		frame.setSize(FEN_LARGEUR, FEN_HAUTEUR);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		// Creation du menu qui contiendra les bouttons
		menuBar.setBackground(COULEUR_MENUBAR); 
        menuBar.setOpaque(true);
		menuBar.setPreferredSize(new Dimension(MENUBAR_LARGEUR, MENUBAR_HAUTEUR));
		menuBar.setLayout(new FlowLayout(FlowLayout.CENTER));  

		// Creation d'un panel dans lequel on va mettre notre menu
		JPanel header = new JPanel();	

		header.setPreferredSize(new Dimension(MENUBAR_LARGEUR, MENUBAR_HAUTEUR));
        header.setLayout(new BorderLayout());
        header.setOpaque(true);	    		
		
        // On ajoute le menu a notre HEADER
        header.add(menuBar, BorderLayout.CENTER);
        
     // Creation du panel principal qui contient la carte
    	JPanel panel = new JPanel();
    	
		
		panel.setPreferredSize(new Dimension(LARGEUR_CARTE, HAUTEUR_CARTE));
		panel.setLayout(new BorderLayout());
		panel.setBackground(COULEUR_FORET);
		panel.setOpaque(true);	    
		
		// On cree notre panneau 
		PanneauJeu panneau = new PanneauJeu();
		// On l'ajoute au panel principale
		panel.add(panneau);	
				
		// Creation d'un panel qui contient les infos du jeux
		JPanel infosPanel = new JPanel(new FlowLayout());
		
		/* on retire l'espace cree par le flowLayout entre la miniCarte et infosPanel 
		 * Utilisation du BorderLayout.CENTER impossible car les dimension sont ignore
		 */
		FlowLayout layout = (FlowLayout)infosPanel.getLayout();
		layout.setVgap(0);
		
		infosPanel.setPreferredSize(new Dimension(LARGEUR_INFOS_PANEL, HAUTEUR_INFOS_PANEL));
		infosPanel.setBackground(COULEUR_INFOS_PANEL);

		infosPanel.setOpaque(true);	    	
		infosPanel.setBorder(new MatteBorder(0, 2, 2, 2, COULEUR_BORDURE));
		
		
		// On cree un conteneur avec FlowLayout pour pouvoir centrer la miniCarte
		JPanel miniCartePanel = new JPanel(new FlowLayout(FlowLayout.LEADING, LARGEUR_INFOS_PANEL/8, LARGEUR_INFOS_PANEL/8));

		// On definie les dimensions et la couleurs du background
		miniCartePanel.setPreferredSize(new Dimension(LARGEUR_INFOS_PANEL, HAUTEUR_MINI_CARTE + LARGEUR_INFOS_PANEL/4));
		miniCartePanel.setBackground(COULEUR_EAU);
		miniCartePanel.setBorder(new MatteBorder(0, 2, 0, 2, COULEUR_BORDURE));

		// On centre le panel qui contient la carte dans le conteneur creer juste avant
		carteMiniature.setLayout(new BorderLayout());
		
		// On definie les dimensions
		carteMiniature.setPreferredSize(new Dimension(LARGEUR_MINI_CARTE, HAUTEUR_MINI_CARTE));
		
		// On ajoute la carte au panel
		carteMiniature.add(new MiniCarte(panneau.c));	
		
		// On Met des bordure autour de la carte
		carteMiniature.setBorder(new MatteBorder(5, 5, 5, 5, COULEUR_MENUBAR));
		
		// On rajoute ce panel au conteneur miniCartePanel
		miniCartePanel.add(carteMiniature);
		
		// On ajoute enfin on conteneur principal
		infosPanel.add(miniCartePanel);
			 
		// On affiche le nombre de soldant restant sur la carte
		JPanel infosSoldatPanel = new JPanel(new FlowLayout());
		
		// On retire l'espace creer par le flow Layout pour que la bordure prenne bien toute la Hauteur du panel
		FlowLayout infosSoldatLayout = (FlowLayout)infosSoldatPanel.getLayout();
		infosSoldatLayout.setVgap(0);
	
		infosSoldatPanel.setBackground(COULEUR_FORET);
		
		// Alignement du text		
		soldatRestant.setHorizontalAlignment(SwingConstants.CENTER);
		
		// On centre le label avec BorderLayou.CENTER
		soldatRestant.setLayout(new BorderLayout());
	
		// On definit la couleur et la taille
		soldatRestant.setPreferredSize(new Dimension(LARGEUR_INFOS_PANEL, HAUTEUR_NB_SOLDAT_VIVANT));
		
		// Couleur du text et Police
		soldatRestant.setForeground(COULEUR_GRILLE);
		soldatRestant.setFont(new Font("Pushster", Font.BOLD, 15));
		
		// On rajoute une La bordure gauche et droite
		soldatRestant.setBorder(new MatteBorder(2, 2, 0, 2, COULEUR_BORDURE));
		
		// On ajoute le label au panel
		infosSoldatPanel.add(soldatRestant);
		
		// On aoute le conteneur dans la panel principal 
		infosPanel.add(infosSoldatPanel);
		
		// Couleur et taille du panel Principal contenant les infos sur les elements
		infosElementPanel.setPreferredSize(new Dimension(LARGEUR_INFOS_PANEL, HAUTEUR_INFOS_PANEL));
		infosElementPanel.setBackground(COULEUR_HEROS);		

		// Couleur et taille du header dans ce panel
		infosElementHeader.setPreferredSize(new Dimension(LARGEUR_INFOS_PANEL, HAUTEUR_ICON_ELEMENT));
		infosElementHeader.setBackground(COULEUR_MENUBAR);
		infosElementPanel.setBorder(new MatteBorder(2, 2, 0, 2, COULEUR_BORDURE));
		
		// panel contenant l'icon
		iconPanel.setBackground(COULEUR_GRILLE);
		iconPanel.setBorder(new LineBorder(COULEUR_BORDURE, 2, true));
		
		// Taille du label contenant l'icon
		iconLabel.setPreferredSize(new Dimension(LARGEUR_ICON_ELEMENT, HAUTEUR_ICON_ELEMENT));
		
		// Label contant les infos de l'elements
		iconInfosLabel.setOpaque(true);
		iconInfosLabel.setBackground(COULEUR_HEROS_DEJA_JOUE);

		// On centre le text
		iconInfosLabel.setHorizontalAlignment(SwingConstants.LEFT);
		iconInfosLabel.setFont(new Font("Pushster", Font.BOLD, 15));
		iconInfosLabel.setBorder(new MatteBorder(0, 0, 0, 2, COULEUR_BORDURE));
		
		// On rajoute les elements a leur conteneur parent
		iconPanel.add(iconLabel);
		infosElementHeader.add(iconPanel, BorderLayout.WEST);
		infosElementHeader.add(iconInfosLabel, BorderLayout.CENTER);
		infosElementPanel.add(infosElementHeader, BorderLayout.NORTH);
		
		infosPanel.add(infosElementPanel);
		
		// On ajoute tout les panels a la frame
		frame.add(footer, BorderLayout.SOUTH);		
		frame.add(header, BorderLayout.NORTH);
		frame.add(infosPanel, BorderLayout.EAST);
		frame.add(panel, BorderLayout.CENTER);
		
		// On creer notre bar de menu secondaire
		new MenuBar();
		
		// On affiche la frame
        frame.pack();
        frame.setVisible(true); 	 
		
	}
	
	public void setDraggable(JPanel panel) {
	    panel.addMouseListener(new MouseAdapter() {
	        public void mousePressed(MouseEvent e) {
	            currentLocation = new Position((int)e.getPoint().getX(), (int)e.getPoint().getY());
	            System.out.println("clic --> " + currentLocation.toString());
	        }
	    });
	    panel.addMouseMotionListener(new MouseAdapter() {
	        public void mouseDragged(MouseEvent e) {
	        	Position currentScreenLocation = new Position((int)e.getLocationOnScreen().getX(),(int)e.getLocationOnScreen().getY());
	            setLocation(currentScreenLocation.getX() - currentLocation.getX(), currentScreenLocation.getY()- currentLocation.getY());
	            System.out.println("----- DEPLACEMENT ------");

	        }
	    });
	}
	public static void main(String[] args) {
		new FenetreJeu();
	}
}