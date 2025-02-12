package JavaProject;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.List;
public class Utilisateur {
	
	// Attributs
	public int id;
	public String nom;
	public String prenom;
	public String poste;
	public int jours_conge_restants;
	public String mdp;
	public String statut;
	
	// Constructeur 1
	public Utilisateur(int id, String nom, String prenom, String poste,	int jours_conge_restants, String mdp, String statut) {
		this.nom = nom;
		this.prenom = prenom;
		this.poste = poste;
		this.jours_conge_restants = jours_conge_restants;
		this.mdp = mdp;
		this.statut = statut;
	
	}
	
	// Constructeur vide
	public Utilisateur() {
		
	}
	
	
	
}