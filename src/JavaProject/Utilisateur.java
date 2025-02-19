package JavaProject;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Classe représentant un utilisateur générique du système.
 */
public class Utilisateur {
    
    /** Identifiant unique de l'utilisateur. */
    public int id;
    /** Nom de l'utilisateur. */
    public String nom;
    /** Prénom de l'utilisateur. */
    public String prenom;
    /** Poste occupé par l'utilisateur. */
    public String poste;
    /** Nombre de jours de congé restants. */
    public int jours_conge_restants;
    /** Mot de passe de l'utilisateur. */
    public String mdp;
    /** Statut de l'utilisateur (ex: employé, manager). */
    public String statut;

    /**
     * Constructeur avec paramètres.
     * @param id Identifiant de l'utilisateur.
     * @param nom Nom de l'utilisateur.
     * @param prenom Prénom de l'utilisateur.
     * @param poste Poste de l'utilisateur.
     * @param jours_conge_restants Nombre de jours de congé restants.
     * @param mdp Mot de passe de l'utilisateur.
     * @param statut Statut de l'utilisateur.
     */
    public Utilisateur(int id, String nom, String prenom, String poste, int jours_conge_restants, String mdp, String statut) {
        this.nom = nom;
        this.prenom = prenom;
        this.poste = poste;
        this.jours_conge_restants = jours_conge_restants;
        this.mdp = mdp;
        this.statut = statut;
    }

    /**
     * Constructeur vide de la classe Utilisateur.
     */
    public Utilisateur() {}
    

    /**
     * Récupère la liste des utilisateurs à partir d'un fichier CSV.
     * @param chemin_fichier Chemin du fichier contenant les utilisateurs.
     * @return Liste des utilisateurs chargés.
     */
    public static List<Utilisateur> func_recup_data(String chemin_fichier) {
        List<Utilisateur> liste_utilisateur = new ArrayList<>();
        String ligne;
        try (BufferedReader br = new BufferedReader(new FileReader(chemin_fichier))) {
            br.readLine(); // Ignorer la première ligne (en-tête)
            while ((ligne = br.readLine()) != null) {
                String[] colonne = ligne.split(";");
                int id_csv = Integer.parseInt(colonne[0]);
                int jours_conges_restants_csv = Integer.parseInt(colonne[4]);
                liste_utilisateur.add(new Utilisateur(id_csv, colonne[1], colonne[2], colonne[3], jours_conges_restants_csv, colonne[5], colonne[6]));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return liste_utilisateur;
    }

    /**
     * Calcule le taux horaire d'un employé en fonction de son poste.
     * @param nom Nom de l'utilisateur.
     * @param prenom Prénom de l'utilisateur.
     * @return Le taux horaire de l'utilisateur, ou -1 si non trouvé.
     */
    public static float calculer_salaire(String nom, String prenom) {
        String path_th = "Ressources/taux_horraire_poste.csv";
        String path_utilisateurs = "Ressources/Utilisateurs.csv";
        String ligne;
        try (BufferedReader br_utilisateurs = new BufferedReader(new FileReader(path_utilisateurs))) {
            br_utilisateurs.readLine(); // Ignorer la première ligne
            while ((ligne = br_utilisateurs.readLine()) != null) {
                String[] colonne_utilisateur = ligne.split(";");
                if (colonne_utilisateur[1].equals(nom) && colonne_utilisateur[2].equals(prenom)) {
                    String poste_utilisateur = colonne_utilisateur[3];
                    try (BufferedReader br_th = new BufferedReader(new FileReader(path_th))) {
                        br_th.readLine();
                        while ((ligne = br_th.readLine()) != null) {
                            String[] colonne_th = ligne.split(";");
                            if (poste_utilisateur.equals(colonne_th[0])) {
                                return Float.parseFloat(colonne_th[1]);
                            }
                        }
                    }
                    break;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return -1;
    }
}
