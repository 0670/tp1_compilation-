import java.io.*;

import java.util.ArrayList;
import java.util.Scanner;
/**
 * La classe AutoVelo met en oeuvre l'automate d'analyse syntaxique des locations de velos
 * Realisation par interpreteur de tables
 * 
 * @author Girard, Grazon, Masson
 * novembre 2019
 */

public class AutoVelo extends Automate{
	
	/**
	 * Rappel: reprise apr√®s erreur demandee sur les items lexicaux VIRG, PTVIRG et BARRE
	 */

	/** table des transitions */
	
	private static final int[][] TRANSIT =
			
			// table de transitions 
		{
		/* Etat      ADULTE DEBUT ENFANT   FIN   HEURES  IDENT  NBENTIER  VIRG PTVIRG  BARRE AUTRES  */
		/* 0 */     {  -1,   -1,    -1,     -1,    -1,     1,      -1,    -1,   -1,    12,    -1   },
		/* 1 */     {  -1,    4,    -1,      5,    -1,    -1,       2,    -1,   -1,    12,    -1   },
		/* 2 */     {  -1,   -1,    -1,     -1,     3,    -1,      -1,    -1,   -1,    12,    -1   },
		/* 3 */     {  -1,	  4,    -1,      5,    -1,    -1,      -1,    -1,   -1,    12,    -1   },
		/* 4 */     {  -1,   -1,    -1,     -1,    -1,    -1,       6,    -1, 	-1,    12,    -1   }, // ‡ verifier		
		/* 5 */     {  -1,   -1,    -1,   	-1,    -1,    -1, 	   -1,    11,   10,    12,    -1   },
		/* 6 */     {   7,   -1,     9,     -1,    -1,    -1,      -1,    -1,   -1,    12,    -1   },
		/* 7 */     {  -1,   -1,    -1,     -1,    -1,    -1,       8,    11,   10,    12,    -1   },
		/* 8 */     {  -1,	 -1,     9,     -1,    -1,    -1,      -1,    -1,   -1,    12,    -1   },
		/* 9 */     {  -1,   -1,    -1,     -1,    -1,    -1,      -1,    11, 	10,    12,    -1   }, 
		/* 10 */    {  -1,   -1,    -1,   	-1,    -1,     1, 	   -1,    -1,   -1,    12,    -1   },		
		/* 11 */    {  -1,   -1,    -1,   	-1,    -1,     1, 	   -1,    -1,   -1,    12,    -1   }, 
		/* 12 */    {  12,   12,    12,   	12,    12,    12, 	   12,    12,   12,    12,    -1   }, 
	

		};

	/** gestion de l'affichage sur la fenetre de trace de l'execution */
	public void newObserver(ObserverAutomate oAuto, ObserverLexique oLex ){
		this.newObserver(oAuto);
		this.analyseurLexical.newObserver(oLex);
		analyseurLexical.notifyObservers(((LexVelo)analyseurLexical).getcarLu());
	}
	/** fin gestion de l'affichage sur la fenetre de trace de l'execution */

	/**
	 *  constructeur classe AutoVelo pour l'application Velo
	 *  
	 * @param flot : donnee a analyser
	 */
	public AutoVelo(InputStream flot) {
		/** on utilise ici un analyseur lexical de type LexVelo */
		analyseurLexical = new LexVelo(flot);
		/** initialisation etats particuliers de l'automate fini d'analyse syntaxique*/
		this.etatInitial = 0;
		this.etatFinal = TRANSIT.length;
		this.etatErreur = TRANSIT.length - 1;
	}

	/** definition de la methode abstraite getTransition de Automate 
	 * 
	 * @param etat : code de l'etat courant
	 * @param unite : code de l'unite lexicale courante
	 * @return code de l'etat suivant
	 **/
	int getTransition(int etat, int unite) {
		return this.TRANSIT[etat][unite];
	}

	/** ici la methode abstraite faireAction de Automate n'est pas encore definie */
	void faireAction(int etat, int unite) {};

	/** ici la methode abstraite initAction de Automate n'est pas encore definie */
	void initAction() {};

	/** ici la methode abstraite getAction de Automate n'est pas encore definie */
	int getAction(int etat, int unite) {return 0;};

}
