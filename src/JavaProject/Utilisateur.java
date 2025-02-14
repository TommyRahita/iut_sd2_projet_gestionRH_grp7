import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
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
	
	public static List<Utilisateur> func_recup_data(String chemin_fichier) {
		
	    
	    String ligne;
	    
	    // Creation des listes d'utilisateurs
	    List<Utilisateur> liste_utilisateur = new ArrayList<>();

	    try (BufferedReader br = new BufferedReader(new FileReader(chemin_fichier))) {
	        // Lire et ignorer la première ligne (en-tête)
	        br.readLine();
	       
	        // Lire chaque ligne du fichier
	        while ((ligne = br.readLine()) != null) {
	            // Récupérer les données du csv
	            String[] colonne = ligne.split(";");
	            String id_csv_str = colonne[0];
	            int id_csv = Integer.parseInt(id_csv_str);
	            String nom_csv = colonne[1];
	            String prenom_csv = colonne[2];
	            String poste_csv = colonne[3];
	            String jours_conges_restants_csv_str = colonne[4];
	            int jours_conges_restants_csv = Integer.parseInt(jours_conges_restants_csv_str);
	            String mdp_csv = colonne[5];
	            String statut_csv = colonne[6];
	            
	            // Création des nouveaux objets
	            liste_utilisateur.add(new Utilisateur(id_csv,nom_csv, prenom_csv, poste_csv, jours_conges_restants_csv, mdp_csv,statut_csv));
	                     
	            
	            }
	            System.out.println();
	        
	    } catch (IOException e) {
	        e.printStackTrace();
	    }
	    
	    return liste_utilisateur;
	}
	
	

}

