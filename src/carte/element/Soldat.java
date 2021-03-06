package carte.element;

import java.awt.Graphics;
import java.awt.image.BufferedImage;

import carte.Camera;
import carte.Carte;
import sprite.InitialiseurSprite;
import sprite.Sprite;
import utile.Position;

/**
 * Class Soldat.
 */
public abstract class Soldat extends Element implements ISoldat, Cloneable{
	
	/** Constante serialVersionUID. */
	private static final long serialVersionUID = 1L;
	
	private final int POINTS_DE_VIE_MAX, PUISSANCE, TIR, PORTEE_VISUELLE;
   
	/** sprite soldat. */
	protected transient InitialiseurSprite spriteSoldat;
	public transient Sprite dernierSprite;
	
	private int pointsDeVie;
       
    public Position nouvellePos;
    private Position pos;    
        
    /** boolean informant sur le status du soldat */
    public boolean seDeplace, combat, estMort;
    public boolean estActifDeplacement;
    public boolean aJoue;
    
    private Position[] champVisuelle = new Position[5];
    private Projectile fleche = null;
    
    protected int deplacementX;
    protected int deplacementY;
    
    /**
     * Instancie un nouveau soldat.
     *
     * @param carte
     * @param pts 
     * @param portee 
     * @param puiss 
     * @param tir 
     * @param pos 
     */
    public Soldat(Carte carte, int pts, int portee, int puiss, int tir, Position pos) {
    	super(carte, pos);
    	
    	this.carte = carte;
    	
    	POINTS_DE_VIE_MAX = pointsDeVie = pts;
    	PORTEE_VISUELLE = portee; PUISSANCE = puiss; TIR = tir;
    	
    	this.deplacementX = this.deplacementY = 0;    
    	
    	this.pos = pos;
    	
    	this.aJoue = false;
    	this.seDeplace = false;
    	this.estActifDeplacement = false;     
    }
    
   /**
    * Se deplace.
    *
    * @param nouvPos nouv pos
    */
   public void seDeplace(Position nouvPos) {
    	// Supression du soldat a sa position
	   carte.setElementVide(this.getPosition());
    	
    	// definition des nouvelles position
    	this.getPosition().setX(nouvPos.getX());
    	this.getPosition().setY(nouvPos.getY());
    	
    	// Positionnement du soldat sur la carte
    	carte.setElement(this);
    }
    
   /**
    * Combat.
    *
    * @param soldat soldat
    * @return true, if successful
    */
   public boolean combat(Soldat soldat) {
    	// On verifie que le soldat attaquer se trouve bien a sa portee
    	if(!this.estDedans(soldat.getPosition()))
    		return false;
    	
    	// On creer un projectile uniquement si l'ennemis n'est pas adjacent
    	if(!this.getPosition().estVoisine(soldat.getPosition()))
    		this.fleche = new Projectile(this.getPosition(), soldat.getPosition());

    	int puissance;
    
    	if (!this.getPosition().estVoisine(soldat.getPosition())) 
    		puissance = (int) (Math.random() * this.TIR);
		else
			puissance = (int) (Math.random() * this.PUISSANCE);
   	
    	soldat.pointsDeVie -= puissance;

    	if(soldat.getPoints() <= 0){
    		soldat.pointsDeVie = 0;	
    		soldat.estMort = true;
    		if(!carte.listeActionMort.contains(soldat))
    			carte.listeActionMort.add(soldat);
    	}
    	return true;
    }
    
    /**
     * Inits champ visuelle.
     */
    private void initChampVisuelle() {
    	this.champVisuelle[0] = new Position(this.getPosition().getX() - this.getPortee(), this.getPosition().getY() - this.getPortee());
    	this.champVisuelle[1] = new Position(this.getPosition().getX() + this.getPortee(), this.getPosition().getY() - this.getPortee());
     	this.champVisuelle[2] = new Position(this.getPosition().getX() + this.getPortee(), this.getPosition().getY() + this.getPortee());
     	this.champVisuelle[3] = new Position(this.getPosition().getX() - this.getPortee(), this.getPosition().getY() + this.getPortee());
    }
    
    /**
     * Est dedans.
     *
     * @param p
     * @return true, if successful
     */
    public boolean estDedans(Position p) {
    	int nbCotes = this.champVisuelle.length - 1;
    	int[] listeAngle = new int[nbCotes];
			
		this.initChampVisuelle();
		for(int i = 0; i < nbCotes - 1; i++)
			listeAngle[i] = p.getSigneAngle(this.champVisuelle[i], this.champVisuelle[i + 1]);
		
		listeAngle[nbCotes - 1] = p.getSigneAngle(this.champVisuelle[nbCotes - 1], this.champVisuelle[0]);
		
		for(int k = 0; k < listeAngle.length; k++) {
			for(int j = 1; j < nbCotes - 1; j++)
				if(listeAngle[k] != listeAngle[j])
					return false;
		}
		return true;
   }
    
    /**
     * Dessine sprite.
     *
     * @param g
     * @param cam
     */
    protected void dessineSprite(Graphics g, Camera cam) {
    	int dx = cam.getDx() * TAILLE_CARREAU;
    	int dy = cam.getDy() * TAILLE_CARREAU;
    	
    	if(this.spriteSoldat == null) {
    		this.spriteSoldat = new InitialiseurSprite(this);
    		this.dernierSprite = this.spriteSoldat.spriteReposDroit;
    	}
    	
    	BufferedImage sprite = this.dernierSprite.getImageSprite(animateur.getProgression()); 
		g.drawImage(sprite, (this.pos.getX() * TAILLE_CARREAU) - dx + this.deplacementX, (this.pos.getY() * TAILLE_CARREAU) - dy + this.deplacementY, TAILLE_CARREAU, TAILLE_CARREAU, null);
		
		if(this.fleche != null && this.combat) {
			this.fleche.dessineProjectile(g, cam);
				if(this.fleche.toucher) this.fleche = null;
		}
			
		this.effectuerDeplacement();
    }
    
    /**
     * Effectue le deplacement.
     */
    private void effectuerDeplacement() {
    	if(this.estActifDeplacement) {
    		this.deplacementX += this.nouvellePos.getX() - this.getPosition().getX();
    		this.deplacementY += this.nouvellePos.getY() - this.getPosition().getY();
    		this.finDeplacement();
    	}
    }
    
    /**
     * Fin deplacement.
     */
    private void finDeplacement() {
    	if(Math.abs(this.deplacementX) >= TAILLE_CARREAU || Math.abs(this.deplacementY) >= TAILLE_CARREAU) {
			this.deplacementX = this.deplacementY = 0;
			this.seDeplace(this.nouvellePos);
			this.estActifDeplacement = false;
    	}
    }
    
    /**
     * Change sprite.
     *
     * @param clic
     * @param cam
     */
    public void changeSprite(Position clic, Camera cam) {   	
    	if(clic == null)
    		return;
    	
    	if(this.spriteSoldat == null) {
    		this.spriteSoldat = new InitialiseurSprite(this);
    		this.dernierSprite = this.spriteSoldat.spriteReposDroit;
    	}
    	   	
    	if(!this.combat && !this.seDeplace && !this.estMort)
    		this.setSpriteRepos(clic);
    	else if (this.combat)
    		this.setSpriteAttaque(clic);
    	else if(this.seDeplace) {
    		this.setSpriteDeplacement(clic);
    		this.estActifDeplacement = true;
    		this.nouvellePos = clic;
    	}
    	else if(this.estMort)
    		this.dernierSprite = this.spriteSoldat.spriteMort;
    }
    
    /**
     * Sets sprite repos.
     *
     * @param clic
     */
    private void setSpriteRepos(Position clic) {
    	if(this.getPosition().getX() > clic.getX())
			this.dernierSprite = this.spriteSoldat.spriteReposGauche;
    	else if(this.getPosition().getX() < clic.getX())
    		this.dernierSprite = this.spriteSoldat.spriteReposDroit;
      	else if(this.getPosition().getY() < clic.getY())
      		this.dernierSprite = this.spriteSoldat.spriteReposBas;
    	else if(this.getPosition().getY() > clic.getY())
    		this.dernierSprite = this.spriteSoldat.spriteReposHaut;
    
    	this.deplacementX = this.deplacementY = 0;
    	this.estActifDeplacement = false;
    }
    
    /**
     * Sets attaque sprite.
     * 
     * On verifie si le soldat est adjacent ou pas : les animations sont differente
     *
     * @param clic
     */
    private void setSpriteAttaque(Position clic) {
    	if(clic.getX() < this.getPosition().getX()) { 
    		this.dernierSprite = this.spriteSoldat.spriteAttaqueGauche;
    		if(this.getPosition().estVoisine(clic))
    			this.dernierSprite = this.spriteSoldat.spriteAttaqueAdjacentGauche;
    	}
    	else if(clic.getX() > this.getPosition().getX()) {
    		this.dernierSprite = this.spriteSoldat.spriteAttaqueDroit;
    		if(this.getPosition().estVoisine(clic))
    			this.dernierSprite = this.spriteSoldat.spriteAttaqueAdjacentDroit;
    	}
    	else if(clic.getY() < this.getPosition().getY()) {
    		this.dernierSprite = this.spriteSoldat.spriteAttaqueHaut;
    		if(this.getPosition().estVoisine(clic))
    			this.dernierSprite = this.spriteSoldat.spriteAttaqueAdjacentHaut;
    	}
    	else if(clic.getY() > this.getPosition().getY()) {
    		this.dernierSprite = this.spriteSoldat.spriteAttaqueBas;
    		if(this.getPosition().estVoisine(clic))
    			this.dernierSprite = this.spriteSoldat.spriteAttaqueAdjacentBas;
    	}
    	this.deplacementX = this.deplacementY = 0;
		this.estActifDeplacement = false;
    }
    
    /**
     * Sets deplacement sprite.
     *
     * @param clic
     */
    private void setSpriteDeplacement(Position clic) {
    	
    	if(clic.getX() < this.getPosition().getX())
			this.dernierSprite = this.spriteSoldat.spriteDeplacementGauche;
    	else if(clic.getX() > this.getPosition().getX())
    		this.dernierSprite = this.spriteSoldat.spriteDeplacementDroit;
      	else if(clic.getY() > this.getPosition().getY())
      		this.dernierSprite = this.spriteSoldat.spriteDeplacementBas;
    	else if(clic.getY() < this.getPosition().getY())
    		this.dernierSprite = this.spriteSoldat.spriteDeplacementHaut;
    }
    
    /**
     * Dessin barre vie.
     *
     * @param g
     * @param cam
     */
    public void dessineBarreVie(Graphics g, Camera cam) {
    	int dx = cam.getDx() * TAILLE_CARREAU;
    	int dy = cam.getDy() * TAILLE_CARREAU;
    			
    	g.setColor(COULEUR_BARRE_VIE_ROUGE);
 		g.fillRect(((this.pos.getX() * TAILLE_CARREAU) - ( Math.min(this.getPointsMax(), TAILLE_CARREAU - PADDING_VIE_CASE_GAUCHE) / 2) + TAILLE_CARREAU/2) - dx, (this.pos.getY() * TAILLE_CARREAU + PADDING_VIE_CASE_HAUT) - dy, Math.min(this.getPointsMax(), TAILLE_CARREAU - PADDING_VIE_CASE_GAUCHE), TAILLE_CARREAU/8); 
 		
 		g.setColor(COULEUR_BARRE_VIE_VERT);
 		g.fillRect(((this.pos.getX() * TAILLE_CARREAU) - ( Math.min(this.getPointsMax(), TAILLE_CARREAU - PADDING_VIE_CASE_GAUCHE) / 2) + TAILLE_CARREAU/2) - dx, (this.pos.getY() * TAILLE_CARREAU + PADDING_VIE_CASE_HAUT) - dy,  (int) (Math.min(this.getPointsMax(), TAILLE_CARREAU - PADDING_VIE_CASE_GAUCHE) * ((float)this.getPoints() / (float)this.getPointsMax())), TAILLE_CARREAU/8); 
 		
 		g.setColor(COULEUR_BORDURE);
 		g.drawRect(((this.pos.getX() * TAILLE_CARREAU) - ( Math.min(this.getPointsMax(), TAILLE_CARREAU - PADDING_VIE_CASE_GAUCHE) / 2) + TAILLE_CARREAU/2) - dx, (this.pos.getY() * TAILLE_CARREAU + PADDING_VIE_CASE_HAUT) - dy, Math.min(this.getPointsMax(), TAILLE_CARREAU - PADDING_VIE_CASE_GAUCHE), TAILLE_CARREAU/8); 
    }
   
    /**
     * Clone un objet et le retourne sous forme d'objet de type soldat
     * Utiliser pour copier un soldat lors d'une action de type deplacement
     * Dans la liste d'action .
     *
     * @return object
     * @throws CloneNotSupportedException clone not supported exception
     */
    public Soldat clone() throws CloneNotSupportedException {	
    	return (Soldat) super.clone();
    }
    
    /**
     * Gets points max.
     *
     * @return points max
     */
    public int getPointsMax() { return this.POINTS_DE_VIE_MAX; }
    
    /**
     * Sets points.
     *
     * Utilise pour le bonus de repos
     *
     * @param pts new points
     */
    protected void setPoints(int pts) { this.pointsDeVie = pts; } 
    
    /**
     * Gets portee.
     *
     * @return portee
     */
    public int getPortee() { return this.PORTEE_VISUELLE; }
    
    /**
     * Gets puissance.
     *
     * @return puissance
     */
    public int getPuissance() { return this.PUISSANCE; }
    
    /**
     * Gets points.
     *
     * @return points
     */
    public int getPoints() { return this.pointsDeVie; }
        
    /**
     * Gets tir.
     *
     * @return tir
     */
    public int getTir() { return this.TIR; }
    
    /**
     * Gets index soldat.
     *
     * @return index soldat
     */
    public abstract int getIndexSoldat();
    
    /**
     * Mort.
     *
     * @param index
     */
    public abstract void mort(int index);
    
    /**
     * Gets chemin sprite.
     *
     * @return chemin sprite
     */
    public abstract String getCheminSprite();
}