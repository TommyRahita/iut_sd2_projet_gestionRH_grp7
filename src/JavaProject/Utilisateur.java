package JavaProject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

/**
 * Classe Utilisateur.
 * Gère utilisateur dans le système.
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

    // Constructeur 1
    public Utilisateur(int id, String nom, String prenom, String poste, int jours_conge_restants, String mdp, String statut) {
        this.id = id;
        this.nom = nom;
        this.prenom = prenom;
        this.poste = poste;
        this.jours_conge_restants = jours_conge_restants;
        this.mdp = mdp;
        this.statut = statut;
    }

    // Constructeur 2
    public Utilisateur(String nom, String prenom, String poste, int jours_conge_restants, String mdp, String statut) {
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
/**
 * Méthode ajouter_utilisateur.
 * Description de la méthode.
 * @param utilisateur Description du paramètre.
 */
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

            out.println(nouvelleLigne);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static List<Utilisateur> func_recup_data(String chemin_fichier) {
        String ligne;
        List<Utilisateur> liste_utilisateur = new ArrayList<>();

        try (BufferedReader br = new BufferedReader(new FileReader(chemin_fichier))) {
            // Lire et ignorer la première ligne (en-tête)
            br.readLine();

            // Lire chaque ligne du fichier
            while ((ligne = br.readLine()) != null) {
                String[] colonne = ligne.split(";");
                int id_csv = Integer.parseInt(colonne[0]);
                String nom_csv = colonne[1];
                String prenom_csv = colonne[2];
                String poste_csv = colonne[3];
                int jours_conges_restants_csv = Integer.parseInt(colonne[4]);
                String mdp_csv = colonne[5];
                String statut_csv = colonne[6];

                liste_utilisateur.add(new Utilisateur(id_csv, nom_csv, prenom_csv, poste_csv, jours_conges_restants_csv, mdp_csv, statut_csv));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return liste_utilisateur;
    }

    /**
     * Méthode qui valide l'identifiant et le mot de passe.
     * Elle retourne l'objet Utilisateur correspondant si la validation est réussie,
     * sinon elle retourne null.
     *
     * @param id       L'identifiant saisi (de la forme prenom.nom).
     * @param password Le mot de passe saisi.
     * @return L'objet Utilisateur correspondant ou null.
     */
/**
 * Méthode validerIdentifiant.
 * Description de la méthode.
 * @param id Description du paramètre.
 * @param password Description du paramètre.
 * @return Utilisateur Description du retour.
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

    // ---------------------- METHODE CALCULER SALAIRE -----------------------//
/**
 * Méthode calculer_salaire.
 * Description de la méthode.
 * @param nom Description du paramètre.
 * @param prenom Description du paramètre.
 * @return float Description du retour.
 */
    public static float calculer_salaire(String nom, String prenom) {
        String path_th = "Ressources/taux_horraire_poste.csv";
        String path_utilisateurs = "Ressources/Utilisateurs.csv";
        String ligne;
        try (BufferedReader br_utilisateurs = new BufferedReader(new FileReader(path_utilisateurs))) {
            // Ignorer la première ligne
            br_utilisateurs.readLine();

            while ((ligne = br_utilisateurs.readLine()) != null) {
                String[] colonne_utilisateur = ligne.split(";");
                String nom_utilisateur = colonne_utilisateur[1];
                String prenom_utilisateur = colonne_utilisateur[2];
                String poste_utilisateur = colonne_utilisateur[3];

                if (nom_utilisateur.equals(nom) && prenom_utilisateur.equals(prenom)) {
                    try (BufferedReader br_th = new BufferedReader(new FileReader(path_th))) {
                        br_th.readLine();
                        while ((ligne = br_th.readLine()) != null) {
                            String[] colonne_th = ligne.split(";");
                            String poste_th = colonne_th[0];
                            float taux_horraire = Float.parseFloat(colonne_th[1]);
                            if (poste_utilisateur.equals(poste_th)) {
                                return taux_horraire;
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
