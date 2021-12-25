/********************************************************************
 * 							WarStone								*
 *  -------------------------------------------------------------	*
 * |	 Universit� Jean-Monnet    L3-Infos 		    2021	 |	*
 *  -------------------------------------------------------------	*
 * 	  BEGGARI ISLEM - CHATAIGNIER ANTOINE - BENGUEZZOU Idriss		*
 * 																	*
 * 														utile		*
 * ******************************************************************/

package utile;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import carte.Carte;


/**
 * The Class Sauvegarde.
 */
public class Sauvegarde {
	
	/** The nom fichier. */
	private static String nomFichier = "./res/wargame.ser";
	
	/**
	 * Instantiates a new sauvegarde.
	 *
	 * @param c the c
	 */
	// Creaction d'une nouvelle sauvegarde dans monFichier
	public Sauvegarde(Carte c){
		try
		{   
			FileOutputStream fichier = new FileOutputStream(nomFichier);
			ObjectOutputStream sortie = new ObjectOutputStream(fichier);
			
			sortie.writeObject(c);
			sortie.close();			
			sortie.flush(); // Pour le buffer
			
			fichier.close();  
		}
		catch(IOException ex)
		{
			System.out.println("IOException : " + ex);
			ex.printStackTrace();
		}
	}
	
	/**
	 * Recup sauvegarde.
	 *
	 * @param c the c
	 * @return the carte
	 */
	// Chargement de la sauvegarde dans nomFichier
	public static Carte recupSauvegarde(Carte c){
		try
		{   
			FileInputStream fichier = new FileInputStream(nomFichier);
			ObjectInputStream in = new ObjectInputStream(fichier);
			
			c = (Carte)in.readObject();
			
			in.close();
			fichier.close();
		}
		catch(IOException ex)
		{
			System.out.println("IOException : " + ex + " -> ");
			ex.printStackTrace();
		}    	        
		catch(ClassNotFoundException ex)
		{
			System.out.println("ClassNotFoundException : " + ex);
		}
		
		return c;
	}
}
