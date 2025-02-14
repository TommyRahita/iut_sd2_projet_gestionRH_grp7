package JavaProject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;

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
		String path_csv = "resources/Utilisateurs.csv";
		
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
		String path_csv = "Ressources/Utilisateurs.csv";
		File fichier_original = new File(path_csv);
        File new_file = new File("temp_utilisateur"); // Fichier temporaire


		boolean utilisateur_trouve = false;

		
	    // On lit le fichier en mode ecriture
        try (BufferedReader br = new BufferedReader(new FileReader(path_csv));
             PrintWriter pw = new PrintWriter(new FileWriter(new_file))) { // PrintWriter facilite l'écriture des lignes

        	// On parcours chaque ligne du fichier
        	while((ligne = br.readLine()) != null) {
        		// On sépare chaque ligne par les différentes colonnes
        		String[] colonne = ligne.split(";");
                String nom_csv = colonne[1];
                String prenom_csv = colonne[2];
                // Si l'utilisateur que l'on veut supprimer est trouvé, alors on ne le réécrit pas 
                if (nom.equals(nom_csv) && prenom.equals(prenom_csv)) {
                	System.out.println("On a trouvé l'utilisateur à supprimer");
                	utilisateur_trouve = true;
                	continue;
                }
                pw.println(ligne);

        	}
        	
        		

           } catch (IOException e) {
               e.printStackTrace();
           }
        
        
        // On remplace le fichier d'origine par le fichier modifié
        if (utilisateur_trouve) {
        	try {
                Files.delete(fichier_original.toPath()); // Supprime l'ancien fichier
                Files.move(new_file.toPath(), fichier_original.toPath(), StandardCopyOption.REPLACE_EXISTING);
        	}
        	catch (IOException e) {
                e.printStackTrace();
            }
        }
        else {
            System.out.println("Utilisateur non trouvé, aucune suppression effectuée.");
            new_file.delete(); // Supprime le fichier temporaire 
        
	}
}
	
	
	// ---------------------- METHODE CALCULER SALAIRE -----------------------//
	public static float calculer_salaire(String nom, String prenom) {
		String path_th = "Ressources/taux_horraire_poste.csv";
		String path_utilisateurs = "Ressources/Utilisateurs.csv";
		String ligne;
		boolean utilisateur_trouve = false;
		// On lit le fichier utilisateur
		 try (BufferedReader br_utilisateurs = new BufferedReader(new FileReader(path_utilisateurs))) {
				  while((ligne = br_utilisateurs.readLine()) != null) {
					  // On sépare la ligne en fonction des colonnes
					  String[] colonne_utilisateur = ligne.split(";");
					  String nom_utilisateur = colonne_utilisateur[1];
					  String prenom_utilisateur = colonne_utilisateur[2];
					  String poste_utilisateur =  colonne_utilisateur[3];
					  // Si la personne pour qui on veut calculer le salaire est trouvée, on récupere le nom de son poste
					  if (nom_utilisateur.equals(nom) && prenom_utilisateur.equals(prenom)) {
						  // Si l'employé existe dans la base de données, on cherche son poste (qui nous donnera son salaire a l'heure)
						  try (BufferedReader br_th = new BufferedReader(new FileReader(path_th))){
							  while ((ligne = br_th.readLine()) != null){
								  String[] colonne_th = ligne.split(";");
								  String poste_th = colonne_th[0];
								  float taux_horraire = Float.parseFloat(colonne_th[1]) ;
								  // Si le poste de l'utilisateur est dans le fichier taux_horraire.csv, on récupere le taux horraire correspondant
								  if (poste_utilisateur.equals(poste_th)) {
									  return taux_horraire;
								  }
							  }
						  }
						  break;
					  }
				  }


				  
			  }
     	catch (IOException e) {
            e.printStackTrace();
           
        }
		 return -1;
		
	}

}

