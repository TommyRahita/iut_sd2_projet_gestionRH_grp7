package JavaProject;

import java.util.List;
public class Employe extends Utilisateur {
	
	// Constructeur 1
	public Employe(int id, String nom, String prenom, String poste,	int jours_conge_restants, String mdp,String statut) {
		 super(id, nom, prenom, poste, jours_conge_restants, mdp,statut);
	}
	
	// Constructeur vide
	public Employe() {
		super();
	}
	
	// Méthode 1
	public Boolean se_connecter_employe (String id_saisi, String mdp_saisi, List<Employe>liste_employe) {   // l'id est la concaténation du nom prenom de l'utilisateur
		Boolean verif = false;
		for (Employe employe : liste_employe) {
			if (id_saisi.equals(employe.id)) {
				verif = true;
				return verif;
			}
		} return verif;
	}
}

