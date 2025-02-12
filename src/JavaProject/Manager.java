package JavaProject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
public class Manager extends Utilisateur {
	
	// ---------------------- CONSTRUCTEURS -----------------------//
	// Constructeur 1
	public Manager(int id, String nom, String prenom, String poste,	int jours_conge_restants, String mdp, String statut) {
		 super(id, nom, prenom, poste, jours_conge_restants, mdp, statut);
	}
	
	// Constructeur vide
	public Manager() {
		super();
	}
	
	
	// ---------------------- METHODE AJOUTER UTILISATEUR -----------------------//
	public static void ajouter_utilisateur(String nom, String prenom, String poste, int jours_conge_restants, String mdp, String statut) {
		String ligne;
		int nb_lignes = 0;
		String path_csv = "Ressources/Utilisateurs.csv";
		
		// Compter le nombre de lignes
	    try (BufferedReader br = new BufferedReader(new FileReader(path_csv))) {
	        // Lire et ignorer la première ligne (en-tête)
	        br.readLine();
	      
	        // Lire chaque ligne du fichier
	        while ((ligne = br.readLine()) != null) {
	        	nb_lignes++;
	            }
	    } catch (IOException e) {
	        e.printStackTrace();
	    }
	   
	    // Ajouter le nouvel utilisateur
       try (FileWriter writer = new FileWriter(path_csv, true);
               BufferedWriter bw = new BufferedWriter(writer);
               PrintWriter out = new PrintWriter(bw)) { // PrintWriter facilite l'écriture des lignes
              // Construire la ligne utilisateur
              String nouvelleLigne = (nb_lignes + 1) + ";" +
                                      nom + ";" +
                                      prenom + ";" +
                                      poste + ";" +
                                      jours_conge_restants + ";" +
                                      mdp + ";" +
                                      statut;
              out.println(nouvelleLigne); //  Ajout direct avec un saut de ligne
              System.out.println("Utilisateur ajouté avec succès !");
          } catch (IOException e) {
              e.printStackTrace();
          }
      }
	
	
	
	
	
	
	// ---------------------- METHODE SUPPRIMER UTILISATEUR -----------------------//
	public static void supprimer_utilisateur(String nom, String prenom) {
		String ligne;
		
	    // Ajouter le nouvel utilisateur
       try (FileWriter writer = new FileWriter(path_csv, true);
               BufferedWriter bw = new BufferedWriter(writer);
               PrintWriter out = new PrintWriter(bw)) { // PrintWriter facilite l'écriture des lignes
       	// Chercher la ligne à supprimmer
       	while(ligne = br.readLine() != null) {
       		!! trouver le moyen d'identifier la ligne a supprimer
       	}
       	
       		
              System.out.println("Utilisateur ajouté avec succès !");
          } catch (IOException e) {
              e.printStackTrace();
          }
	}
}



