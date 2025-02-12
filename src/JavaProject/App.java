package JavaProject;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


public class App {
	
	
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


	
	   public static void main(String[] args) {
		   
		    // ETAPE 1 : On recupere les données du csv
		    List<Utilisateur> liste_utilisateur = func_recup_data("Utilisateurs.csv");
		   
		   
		    
		    
		    
		    
		    
		    // ETAPE 2 : On Parcours la liste d'utilisateurs pour les mettre dans la liste manageurs ou employé selon leur statut
		    
		    // Creation des listes qui stockeront les infos relatives aux Employés et aux Managers
		    List<Manager> liste_managers = new ArrayList<>();
		    List<Employe> liste_employe = new ArrayList<>();
		    
	        // Création des nouveaux objets
	        Manager new_manager = new Manager();
	        Employe new_employe = new Employe();
	        
	        
	        // Pour chaque utilisateur
	        for(Utilisateur utilisateur : liste_utilisateur) {
	        	int utilisateur_id = utilisateur.id;
	        	String utilisateur_nom = utilisateur.nom;
	        	String utilisateur_prenom = utilisateur.prenom;
	        	String utilisateur_poste = utilisateur.poste;
	        	int utilisateur_jours_conge_restants = utilisateur.jours_conge_restants;
	        	String utilisateur_mdp = utilisateur.mdp;
	        	String utilisateur_statut = utilisateur.statut;
	        
	          // Si l'utilisateur est un manager	
		      if(utilisateur_statut.equals("manager")) {
		      	new_manager = new Manager(utilisateur_id, utilisateur_nom, utilisateur_prenom, utilisateur_poste, utilisateur_jours_conge_restants, utilisateur_mdp, utilisateur_statut);
		      	liste_managers.add(new_manager);
		      }
		      
		      // Si l'utilisateur est un Employé
		      else {
		         	new_employe = new Employe(utilisateur_id, utilisateur_nom, utilisateur_prenom, utilisateur_poste, utilisateur_jours_conge_restants, utilisateur_mdp, utilisateur_statut);
		          	liste_employe.add(new_employe); 
		      }
	   }
	      // Tests
	      for (Employe employe:liste_employe) {
	    	  System.out.println(employe.nom);
	    	  System.out.println(employe.statut);
	      }
	      for (Manager manager:liste_managers) {
	    	  System.out.println(manager.nom);
	    	  System.out.println(manager.statut);
	      }
	      
	      
		   	// ETAPE 3 : Ajouter un utilisateurON TESTE LES METHODES
   }
	


}