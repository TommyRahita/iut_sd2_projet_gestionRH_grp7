package JavaProject;

import java.util.Date;
public class Conge {
	Date date_debut;
	Date date_fin;
	String type_conge;
	float txhoraire;
	String statut;
	
	public Conge (Date date_debut, Date date_fin, String type_conge, float txhoraire, String statut) {
		this.date_debut = date_debut;
		this.date_fin = date_fin;
		this.type_conge = type_conge;
		this.txhoraire = txhoraire;
		this.statut = statut;
	}
}

