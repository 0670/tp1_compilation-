import java.io.InputStream;
import java.util.ArrayList;
import java.util.*;
import java.util.Scanner;


/**
 * La classe ActVelo met en oeuvre les actions de l'automate d'analyse
 * syntaxique des locations de velos
 * 
 * @author Girard, Grazon, Masson decembre 2019
 */

public class ActVelo extends AutoVelo {

	/** table des actions */
	private final int[][] ACTION = {
			// table des actions 
			/*
	    * Etat        ADULTE DEBUT ENFANT   FIN   HEURES IDENT  NBENTIER  VIRG PTVIRG BARRE AUTRES
		
		/* 0 */     {  -1,   -1,    -1,     -1,    -1,     1,      -1,    -1,   -1,    12,    -1   },
		/* 1 */     {  -1,    3,    -1,      4,    -1,    -1,       2,    -1,   -1,    -1,    -1   },
		/* 2 */     {  -1,   -1,    -1,     -1,     3,    -1,      -1,    -1,   -1,    -1,    -1   },
		/* 3 */     {  -1,	 -1,    -1,      4,    -1,    -1,      -1,    -1,   -1,    -1,    -1   },
		/* 4 */     {  -1,   -1,    -1,     -1,    -1,   -1,        5,    -1, 	-1,    -1,    -1   }, 		
		/* 5 */     {  -1,   -1,    -1,   	 1,    -1,    -1, 	   -1,     8,    7,    -1,    -1   },
		/* 6 */     {   7,   -1,    11,     -1,    -1,    -1,      -1,    -1,   -1,    -1,    -1   },
		/* 7 */     {  -1,   -1,    -1,     -1,    -1,     0,       6,    -1,    7,    -1,    -1   },
		/* 8 */     {  -1,	 -1,     9,      5,    -1,    -1,      -1,    -1,   -1,    -1,    -1   },
		/* 9 */     {  -1,   -1,    -1,     -1,    -1,    -1,      -1,     8, 	 7,    -1,    -1   }, 
		/* 10 */    {  -1,   -1,    -1,   	-1,    -1,     9, 	   -1,    -1,   -1,    12,    -1   },		
		/* 11 */    {  -1,   -1,    -1,   	-1,    -1,    10, 	   -1,    -1,   -1,    12,    -1   },
		/* 12 */    {  12,   12,    12,   	12,    12,    12, 	   12,    12,   12,    12,    12   },
		
		// à verifier par la prof 		 
	};

	/**
	 * constructeur classe ActVelo
	 * 
	 * @param flot : donnee a analyser
	 */
	public ActVelo(InputStream flot) {
		super(flot);
	}

	/**
	 * definition de la methode abstraite getAction de Automate
	 * 
	 * @param etat  : code de l'etat courant
	 * @param unite : code de l'unite lexicale courante
	 * @return code de l'action suivante
	 **/
	public int getAction(int etat, int unite) {
		return ACTION[etat][unite];
	}

	/**
	 * definition methode abstraite initAction de Automate
	 */
	public void initAction() {
		/** correspond a l'action 0 a effectuer a l'init */
		initialisations();
	}

	/**
	 * definition de la methode abstraite faireAction de Automate
	 * 
	 * @param etat  : code de l'etat courant
	 * @param unite : code de l'unite lexicale courante
	 * @return code de l'etat suivant
	 **/
	public void faireAction(int etat, int unite) {
		executer(ACTION[etat][unite]);
	}

	/** types d'erreurs detectees */
	private static final int FATALE = 0, NONFATALE = 1;

	/**
	 * gestion des erreurs
	 * 
	 * @param tErr    type de l'erreur (FATALE ou NONFATALE)
	 * @param messErr message associe a l'erreur
	 */
	private void erreur(int tErr, String messErr) {
		Lecture.attenteSurLecture(messErr);
		switch (tErr) {
		case FATALE:
			errFatale = true;
			break;
		case NONFATALE:
			etatCourant = etatErreur;
			break;
		default:
			Lecture.attenteSurLecture("parametre incorrect pour erreur");
		}
	}

	/** attribut sauvegardant l'ensemble des locations en cours (non terminees) */
	private BaseDeLoc maBaseDeLoc = new BaseDeLoc();

	/** nombre de velos initialement disponibles */
	private static final int MAX_VELOS_ADULTES = 50, MAX_VELOS_ENFANT = 20;

	/**
	 * acces a un attribut lexical cast pour preciser que analyseurLexical est de
	 * type LexVelo
	 * 
	 * @return valEnt associe a l'unite lexicale NBENTIER
	 */
	private int valEnt() {
		return ((LexVelo) analyseurLexical).getvalEnt();
	}

	/**
	 * acces a un attribut lexical cast pour preciser que analyseurLexical est de
	 * type LexVelo
	 * 
	 * @return numIdCourant associe a l'unite lexicale IDENT
	 */
	private int numId() {
		return ((LexVelo) analyseurLexical).getNumIdCourant();
	}

	/**
	 * variables a prevoir pour actions
	 */

	// Rappel: chaque <Validation> correspond a un jour different
	// jourCourant correspond a la <Validation> en cours d'analyse
	private int jourCourant = 1;

	// Rappel: chaque <Validation> est composee de plusieurs operations
	// nbOperationTotales correspond a toutes les operations contenues dans la
	// donnee a analyser
	// erronees ou non
	private int nbOperationTotales;

	// nbOperationCorrectes correspond a toutes les operations sans erreur
	// de la donnee a analyser
	private int nbOperationCorrectes;

	// ensemble des clients differents vus chaque jour
	// clientsParJour.get(i) donne l'ensemble des clients differents vus le ieme
	// jour
	private ArrayList<SmallSet> clientsParJour;

	// TODO completer la declaration des variables necessaires aux actions
	// *******************************************************************
	// le nom du client  
	private String nomClient;
	//  le reste de vélos dans la journée 
	private  int rest  ;
	// nombre de velo enfant 
	private int nbEnfant;
	// nombre de velo adulte
	private int nbAdulte;
	 
	// nb horraire;
	private int heure;
	private int heureDeb, heureFin;
	private int duree;
	private int reste_Adulte, reste_Enfant;
	private int numId;
	/**
	 * initialisations a effectuer avant les actions
	 */
	private void initialisations() {
		nbOperationCorrectes = 0; nbOperationTotales = 0;
		clientsParJour=new ArrayList<SmallSet>();
		/** initialisation clients du premier jour
		 * NB: le jour 0 n'est pas utilise */
		clientsParJour.add(0,new SmallSet()); 
		clientsParJour.add(1,new SmallSet()); 	
		
		// TODO completer l'initialisation des variables necessaires aux actions
		// *********************************************************************
	nomClient="";
	nbEnfant=0;
	nbAdulte=0;
	heure=0;
	heureDeb=0;
	heureFin=0;
	reste_Adulte=MAX_VELOS_ADULTES;
	reste_Enfant=MAX_VELOS_ENFANT;
	duree=0;
	numId=0;
		
	} 

	/**
	 * execution d'une action
	 * 
	 * @param numAction : numero de l'action a executer
	 */
	public void executer(int numAction) {
		// System.out.println("etat " + etatCourant + " action " + numAction);

		switch (numAction) {
		case -1: // action vide
			break;
			
		case 1:
			nomClient=this.analyseurLexical.chaineIdent(this.analyseurLexical.tabIdent.size()-1); 
			break;
		case 2:
			heure=valEnt();
		    break;
			
		case 3:
			heureDeb=heure;
			break;
			
		case 4:
			heureFin=heure;
				
			duree= calculDureeLoc(jourCourant, heureDeb, jourCourant, heureFin);
			
			break;
		case 5:
			heureFin=19;
			heure= calculDureeLoc(jourCourant, heureDeb, jourCourant,heureFin );
			break;
		
		case 6:
			nbAdulte=valEnt();
			if(nbAdulte<reste_Adulte && !maBaseDeLoc.isPresent(nomClient)) {
			reste_Adulte=reste_Adulte - nbAdulte;
			}
			else {
				System.out.println( nomClient + "  il n'y a pas de velo adulte disponible ou il a une location en cours ");
			}
			
			break;
			
		case 7: 
				nbEnfant=valEnt();
				if(nbAdulte<reste_Adulte && !maBaseDeLoc.isPresent(nomClient)) {
				
					reste_Enfant=reste_Enfant-nbEnfant;
				} else {
					
					System.out.println(nomClient + "  il n'y pas de velo enfant diponible ou le client a une location en cours  ");
					
				}
			
				break;
			
		case 8: 
		
				maBaseDeLoc.enregistrerLoc(nomClient, jourCourant, heureDeb, nbAdulte, nbEnfant);
			//	clientsParJour.add(jourCourant,);							
				nbOperationCorrectes++;
				nbOperationTotales++;
		
			break;
			
		case 9: 
			
			duree = calculDureeLoc(jourCourant, heureDeb,jourCourant , heureFin);
			maBaseDeLoc.afficherLocationsEnCours();
			
			break;
			
		case 10:
			
			if(!maBaseDeLoc.isPresent(nomClient)) {
				nomClient=this.analyseurLexical.chaineIdent(numId()); 
				
			} else {
				
				System.out.println("Client " + nomClient + " à une location en cours");
			}
			
			break;
		case 11:
			
			if (maBaseDeLoc.isPresent(nomClient)) {
				int duree = calculDureeLoc(jourCourant, heureDeb,12 , heureFin);// faux
				
				maBaseDeLoc.supprimerClient(nomClient);
			}	
			
			maBaseDeLoc.afficherLocationsEnCours();
					
		default:
			Lecture.attenteSurLecture("action " + numAction + " non prevue");
		}
	}
		
	/**
	 * 
	 * utilitaire de calcul de la duree d'une location
	 *
	 * @param jourDebutLoc : numero du jour de dÃ©but de la location Ã  partir de 1
	 * @param              heureDebutLoc: heure du debut de la location, entre 8 et
	 *                     19
	 * @param jourFinLoc   : numero du jour de la fin de la location Ã  partir de 1
	 * @param heureFinLoc  : heure de fin de la location, entre 8 et 19
	 * @return nombre d'heures comptabilisÃ©es pour la location (les heures de nuit
	 *         entre 19h et 8h ne sont pas comptabilisees)
	 */
	private int calculDureeLoc(int jourDebutLoc, int heureDebutLoc, int jourFinLoc, int heureFinLoc) {
		int duree;
		// velos rendus le jour de l'emprunt
		if (jourDebutLoc == jourFinLoc) {
			duree = heureFinLoc - heureDebutLoc;
			// velos rendus quelques jours apres l'emprunt (la duree ne peut pas etre
			// negative)
		} else {
			duree = 19 - heureDebutLoc; // duree du premier jour
			duree = duree + (heureFinLoc - 8); // ajout de la duree du dernier jour
			if (jourFinLoc > jourDebutLoc + 1) { // plus 24h par jour intermediaire
				duree = duree + 11 * (jourFinLoc - jourDebutLoc - 1);
			}
		}
		return duree;
	}
	
}
