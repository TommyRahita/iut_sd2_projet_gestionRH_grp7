package JavaProject;

/**
 * Classe Salaire.
 * <p>
 * Cette classe gère les fonctionnalités liées au calcul et à la représentation du salaire dans le système.
 * Elle encapsule les informations du salaire brut, des primes, des cotisations, des impôts et du salaire net,
 * ainsi que le mois et l'année de la paie.
 * </p>
 * 
 * @author Groupe 7
 * @version 1.0
 */
public class Salaire {
    private double salaireBrut;
    private double primes;
    private double cotisations;
    private double impots;
    private double salaireNet;
    private int mois;
    private int annee;

    /**
     * Constructeur de la classe Salaire.
     *
     * @param salaireBrut Le salaire brut.
     * @param primes Le montant des primes.
     * @param cotisations Le montant des cotisations sociales.
     * @param impots Le montant des impôts.
     * @param salaireNet Le salaire net à payer.
     * @param mois Le mois de la paie.
     * @param annee L'année de la paie.
     */
    public Salaire(double salaireBrut, double primes, double cotisations, double impots, double salaireNet, int mois, int annee) {
        this.salaireBrut = salaireBrut;
        this.primes = primes;
        this.cotisations = cotisations;
        this.impots = impots;
        this.salaireNet = salaireNet;
        this.mois = mois;
        this.annee = annee;
    }

    /**
     * Retourne le salaire brut.
     *
     * @return Le salaire brut.
     */
    public double getSalaireBrut() {
        return salaireBrut;
    }

    /**
     * Retourne le montant des primes.
     *
     * @return Les primes.
     */
    public double getPrimes() {
        return primes;
    }

    /**
     * Retourne le montant des cotisations sociales.
     *
     * @return Les cotisations sociales.
     */
    public double getCotisations() {
        return cotisations;
    }

    /**
     * Retourne le montant des impôts.
     *
     * @return Les impôts.
     */
    public double getImpots() {
        return impots;
    }

    /**
     * Retourne le salaire net à payer.
     *
     * @return Le salaire net.
     */
    public double getSalaireNet() {
        return salaireNet;
    }

    /**
     * Retourne le mois de la paie.
     *
     * @return Le mois.
     */
    public int getMois() {
        return mois;
    }

    /**
     * Retourne l'année de la paie.
     *
     * @return L'année.
     */
    public int getAnnee() {
        return annee;
    }

    /**
     * Retourne une représentation textuelle des informations de salaire.
     *
     * @return Une chaîne de caractères représentant le salaire.
     */
    @Override
    public String toString() {
        return "Salaire [salaireBrut=" + salaireBrut + ", primes=" + primes 
            + ", cotisations=" + cotisations + ", impots=" + impots 
            + ", salaireNet=" + salaireNet + ", mois=" + mois + ", annee=" + annee + "]";
    }
}
