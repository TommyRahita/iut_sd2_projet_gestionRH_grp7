package JavaProject;

/**
 * Classe Salaire.
 * Gère le salaire dans le système.
 */
public class Salaire {
    private double salaireBrut;
    private double primes;
    private double cotisations;
    private double impots;
    private double salaireNet;
    private int mois;
    private int annee;

    // Constructeur avec paramètres
    public Salaire(double salaireBrut, double primes, double cotisations, double impots, double salaireNet, int mois, int annee) {
        this.salaireBrut = salaireBrut;
        this.primes = primes;
        this.cotisations = cotisations;
        this.impots = impots;
        this.salaireNet = salaireNet;
        this.mois = mois;
        this.annee = annee;
    }

    // Getters
    public double getSalaireBrut() {
        return salaireBrut;
    }

    public double getPrimes() {
        return primes;
    }

    public double getCotisations() {
        return cotisations;
    }

    public double getImpots() {
        return impots;
    }

    public double getSalaireNet() {
        return salaireNet;
    }

    public int getMois() {
        return mois;
    }

    public int getAnnee() {
        return annee;
    }

    @Override
    public String toString() {
        return "Salaire [salaireBrut=" + salaireBrut + ", primes=" + primes 
            + ", cotisations=" + cotisations + ", impots=" + impots 
            + ", salaireNet=" + salaireNet + ", mois=" + mois + ", annee=" + annee + "]";
    }
}
