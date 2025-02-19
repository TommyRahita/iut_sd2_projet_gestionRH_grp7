package JavaProject;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Classe principale de l'application.
 */
/**
 * Classe App.
 * Gère app dans le système.
 */
public class App {
    
    /**
     * Point d'entrée de l'application.
     * @param args Arguments de la ligne de commande (non utilisés).
     */
/**
 * Méthode main.
 * Description de la méthode.
 * @param args Description du paramètre.
 */
    public static void main(String[] args) {
        // ETAPE 1 : Récupération des données du fichier CSV
        List<Utilisateur> liste_utilisateur = Utilisateur.func_recup_data("resources/Utilisateurs.csv");

        // Création des listes pour stocker les employés et managers
        List<Manager> liste_managers = new ArrayList<>();
        List<Employe> liste_employe = new ArrayList<>();

        // Création des nouveaux objets
        Manager new_manager;
        Employe new_employe;
        
        // Parcours de la liste des utilisateurs et répartition selon le statut
        for (Utilisateur utilisateur : liste_utilisateur) {
            int utilisateur_id = utilisateur.id;
            String utilisateur_nom = utilisateur.nom;
            String utilisateur_prenom = utilisateur.prenom;
            String utilisateur_poste = utilisateur.poste;
            int utilisateur_jours_conge_restants = utilisateur.jours_conge_restants;
            String utilisateur_mdp = utilisateur.mdp;
            String utilisateur_statut = utilisateur.statut;

            // Si l'utilisateur est un manager
            if (utilisateur_statut.equals("manager")) {
                new_manager = new Manager(utilisateur_id, utilisateur_nom, utilisateur_prenom, utilisateur_poste, utilisateur_jours_conge_restants, utilisateur_mdp, utilisateur_statut);
                liste_managers.add(new_manager);
            }
            // Sinon, c'est un employé
            else {
                new_employe = new Employe(utilisateur_id, utilisateur_nom, utilisateur_prenom, utilisateur_poste, utilisateur_jours_conge_restants, utilisateur_mdp, utilisateur_statut);
                liste_employe.add(new_employe);
            }
        }

        // Affichage des employés chargés
        for (Employe employe : liste_employe) {
            System.out.println(employe.nom);
            System.out.println(employe.statut);
        }

        // Exemple de tests des méthodes
        // Manager.supprimer_utilisateur("DUPONT", "Jean");
    }
}
