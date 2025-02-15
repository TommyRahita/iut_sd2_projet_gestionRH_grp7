package JavaProject;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class App {
	
	


	
	   public static void main(String[] args) {
		   
		   
		    // ETAPE 1 : On recupere les données du csv
		    List<Utilisateur> liste_utilisateur = Utilisateur.func_recup_data("resources/Utilisateurs.csv");
		   
		   
		    
		    
		    
		    
		    
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
	        
//	          // Si l'utilisateur est un manager	
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
	      for (Employe employe:liste_employe) {
	    	  System.out.println(employe.nom);
	    	  System.out.println(employe.statut);
	      }
	      
	      
	      
//		   	// ETAPE 3 :  ON TESTE LES METHODES
////	      Manager.ajouter_utilisateur("DUPONT", "Jean", "data analyst", 50, "motdepassedefou", "employe");
//	      Manager.supprimer_utilisateur("DUPONT", "Jean");
   }
	

}

