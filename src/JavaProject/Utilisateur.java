package JavaProject;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Classe Utilisateur.
 * <p>
 * Cette classe représente les utilisateurs du système et gère les fonctionnalités associées, 
 * telles que la validation des identifiants et le chargement des données utilisateurs depuis un fichier CSV.
 * Chaque utilisateur est caractérisé par un identifiant, un nom, un prénom, un poste, un nombre de jours de congé restants, 
 * un mot de passe et un statut.
 * </p>
 * 
 * @author Groupe 7
 * @version 1.0
 */
public class Utilisateur {

    // Attributs
    public int id;
    public String nom;
    public String prenom;
    public String poste;
    public int jours_conge_restants;
    public String mdp;
    public String statut;

    /**
     * Constructeur complet de la classe Utilisateur.
     *
     * @param id                   L'identifiant de l'utilisateur.
     * @param nom                  Le nom de l'utilisateur.
     * @param prenom               Le prénom de l'utilisateur.
     * @param poste                Le poste occupé par l'utilisateur.
     * @param jours_conge_restants Le nombre de jours de congé restants.
     * @param mdp                  Le mot de passe de l'utilisateur.
     * @param statut               Le statut de l'utilisateur.
     */
    public Utilisateur(int id, String nom, String prenom, String poste, int jours_conge_restants, String mdp, String statut) {
        this.id = id;
        this.nom = nom;
        this.prenom = prenom;
        this.poste = poste;
        this.jours_conge_restants = jours_conge_restants;
        this.mdp = mdp;
        this.statut = statut;
    }

    /**
     * Constructeur de la classe Utilisateur sans identifiant (pour création de nouveaux utilisateurs).
     *
     * @param nom                  Le nom de l'utilisateur.
     * @param prenom               Le prénom de l'utilisateur.
     * @param poste                Le poste occupé par l'utilisateur.
     * @param jours_conge_restants Le nombre de jours de congé restants.
     * @param mdp                  Le mot de passe de l'utilisateur.
     * @param statut               Le statut de l'utilisateur.
     */
    public Utilisateur(String nom, String prenom, String poste, int jours_conge_restants, String mdp, String statut) {
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
    public Utilisateur() {
    }

    /**
     * Charge les données des utilisateurs depuis un fichier CSV.
     * <p>
     * La méthode lit le fichier CSV spécifié et crée une liste d'objets Utilisateur à partir des lignes lues.
     * La première ligne du fichier (l'en-tête) est ignorée.
     * </p>
     *
     * @param chemin_fichier Le chemin vers le fichier CSV contenant les données utilisateurs.
     * @return Une liste d'objets Utilisateur.
     */
    public static List<Utilisateur> func_recup_data(String chemin_fichier) {
        List<Utilisateur> liste_utilisateur = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(chemin_fichier))) {
            // Ignorer l'en-tête
            br.readLine();
            String ligne;
            while ((ligne = br.readLine()) != null) {
                String[] colonne = ligne.split(";");
                if (colonne.length >= 7) {
                    int id_csv = Integer.parseInt(colonne[0]);
                    String nom_csv = colonne[1];
                    String prenom_csv = colonne[2];
                    String poste_csv = colonne[3];
                    int jours_conges_restants_csv = Integer.parseInt(colonne[4]);
                    String mdp_csv = colonne[5];
                    String statut_csv = colonne[6];
                    liste_utilisateur.add(new Utilisateur(id_csv, nom_csv, prenom_csv, poste_csv, jours_conges_restants_csv, mdp_csv, statut_csv));
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return liste_utilisateur;
    }

    /**
     * Valide l'identifiant et le mot de passe saisis par l'utilisateur.
     * <p>
     * L'identifiant doit être de la forme "prenom.nom" (en minuscules). 
     * La méthode parcourt la liste des utilisateurs chargée depuis le fichier CSV et vérifie
     * si l'identifiant et le mot de passe correspondent.
     * </p>
     *
     * @param id       L'identifiant saisi par l'utilisateur.
     * @param password Le mot de passe saisi par l'utilisateur.
     * @return L'objet Utilisateur correspondant en cas de succès, ou null si la validation échoue.
     */
    public static Utilisateur validerIdentifiant(String id, String password) {
        List<Utilisateur> utilisateurs = func_recup_data("resources/Utilisateurs.csv");
        for (Utilisateur utilisateur : utilisateurs) {
            String identifiant = utilisateur.prenom.toLowerCase() + "." + utilisateur.nom.toLowerCase();
            if (identifiant.equalsIgnoreCase(id) && utilisateur.mdp.equals(password)) {
                return utilisateur;
            }
        }
        return null;
    }
}
