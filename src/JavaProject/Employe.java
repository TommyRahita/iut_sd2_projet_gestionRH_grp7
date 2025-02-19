package JavaProject;

import java.util.List;

/**
 * Classe représentant un employé, hérite de la classe Utilisateur.
 */
public class Employe extends Utilisateur {
    
    /**
     * Constructeur de la classe Employe avec paramètres.
     * @param id Identifiant de l'employé.
     * @param nom Nom de l'employé.
     * @param prenom Prénom de l'employé.
     * @param poste Poste occupé par l'employé.
     * @param jours_conge_restants Nombre de jours de congé restants.
     * @param mdp Mot de passe de l'employé.
     * @param statut Statut de l'employé.
     */
    public Employe(int id, String nom, String prenom, String poste, int jours_conge_restants, String mdp, String statut) {
        super(id, nom, prenom, poste, jours_conge_restants, mdp, statut);
    }

    /**
     * Constructeur vide de la classe Employe.
     */
    public Employe() {
        super();
    }

    /**
     * Vérifie si un employé peut se connecter en fonction de ses identifiants.
     * @param id_saisi Identifiant saisi par l'utilisateur.
     * @param mdp_saisi Mot de passe saisi par l'utilisateur.
     * @param liste_employe Liste des employés existants.
     * @return True si les identifiants sont corrects, sinon False.
     */
    public Boolean se_connecter_employe(String id_saisi, String mdp_saisi, List<Employe> liste_employe) {
        Boolean verif = false;
        for (Employe employe : liste_employe) {
            if (id_saisi.equals(employe.id)) {
                verif = true;
                return verif;
            }
        }
        return verif;
    }
}
