package JavaProject;

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
		this.id = id;
		this.nom = nom;
		this.prenom = prenom;
		this.poste = poste;
		this.jours_conge_restants = jours_conge_restants;
		this.mdp = mdp;
		this.statut = statut;
	
	}
	// Constructeur 2
	public Utilisateur(String nom, String prenom, String poste,	int jours_conge_restants, String mdp, String statut) {
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

	// Méthode pour ajouter un utilisateur dans le fichier CSV
    public static void ajouter_utilisateur(Utilisateur utilisateur) {
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
             PrintWriter out = new PrintWriter(bw)) {

            // Construire la ligne utilisateur
            String nouvelleLigne = (nb_lignes + 1) + ";" +
                                    utilisateur.nom + ";" +
                                    utilisateur.prenom + ";" +
                                    utilisateur.poste + ";" +
                                    utilisateur.jours_conge_restants + ";" +
                                    utilisateur.mdp + ";" +
                                    utilisateur.statut;

            out.println(nouvelleLigne); // Ajout direct avec un saut de ligne

        } catch (IOException e) {
            e.printStackTrace();
        }
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
	
	
	// ---------------------- METHODE CALCULER SALAIRE -----------------------//
	public static float calculer_salaire(String nom, String prenom) {
		String path_th = "Ressources/taux_horraire_poste.csv";
		String path_utilisateurs = "Ressources/Utilisateurs.csv";
		String ligne;
		boolean utilisateur_trouve = false;
		// On lit le fichier utilisateur
		 try (BufferedReader br_utilisateurs = new BufferedReader(new FileReader(path_utilisateurs))) {
			 // Ignorer la première ligne
			 br_utilisateurs.readLine();
			 
				  while((ligne = br_utilisateurs.readLine()) != null) {
					  // On sépare la ligne en fonction des colonnes
					  String[] colonne_utilisateur = ligne.split(";");
					  String nom_utilisateur = colonne_utilisateur[1];
					  String prenom_utilisateur = colonne_utilisateur[2];
					  String poste_utilisateur =  colonne_utilisateur[3];
					  // Si la personne pour qui on veut calculer le salaire est trouvée, on récupere le nom de son poste
					  if (nom_utilisateur.equals(nom) && prenom_utilisateur.equals(prenom)) {
						  // Si l'employé existe dans la base de données, on cherche son poste (qui nous donnera son salaire a l'heure)
						  System.out.println("utilisateur trouvé");
						  try (BufferedReader br_th = new BufferedReader(new FileReader(path_th))){
							  br_th.readLine();
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

