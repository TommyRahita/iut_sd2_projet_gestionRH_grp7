package JavaProject;

/**
 * Classe Salaire.
 * Gère salaire dans le système.
 */
public class Salaire {
	
	String mois_salaire; 
	float heures_travaillees;
	float retenues;
	float primes;
	float rappel;
	
	// Constructeur vide
	public Salaire() {
		
	}
	
	// Constructeur avec paramètres
	public Salaire(String mois_salaire, float heures_travaillees, float retenues, float primes, float rappel) {
		
		this.mois_salaire = mois_salaire;
		this.heures_travaillees = heures_travaillees;
		this.retenues = retenues;
		this.primes = primes;
		this.rappel = rappel;
	}
}

