package JavaProject;

import java.util.Date;

/**
 * Classe Conge.
 * <p>
 * Cette classe gère les fonctionnalités liées aux congés dans le système.
 * Elle permet de représenter un congé avec une date de début, une date de fin, un type de congé,
 * un taux horaire et un statut associé.
 * </p>
 * 
 * @author Groupe 7
 * @version 1.0
 */
public class Conge {
    private Date date_debut;
    private Date date_fin;
    private String type_conge;
    private float txhoraire;
    private String statut;
    
    /**
     * Constructeur de la classe Conge.
     * 
     * @param date_debut La date de début du congé.
     * @param date_fin La date de fin du congé.
     * @param type_conge Le type de congé (exemple : "payé", "non payé", "maladie", etc.).
     * @param txhoraire Le taux horaire applicable pendant le congé.
     * @param statut Le statut du congé (exemple : "Validé", "En attente", "Rejeté").
     */
    public Conge(Date date_debut, Date date_fin, String type_conge, float txhoraire, String statut) {
        this.date_debut = date_debut;
        this.date_fin = date_fin;
        this.type_conge = type_conge;
        this.txhoraire = txhoraire;
        this.statut = statut;
    }
    
    // Vous pouvez ajouter des accesseurs (getters/setters) si nécessaire
}
