package JavaProject;

import javax.swing.*;
import java.awt.Component;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

/**
 * Classe Employe.
 * <p>
 * Cette classe gère les fonctionnalités liées aux employés dans le système.
 * Elle étend la classe Utilisateur et fournit des méthodes pour télécharger la fiche de paie
 * et enregistrer les demandes de congés.
 * </p>
 *
 * @author Groupe 7
 * @version 1.0
 */
public class Employe extends Utilisateur {
    
    /**
     * Constructeur complet de la classe Employe.
     *
     * @param id                  L'identifiant de l'employé.
     * @param nom                 Le nom de l'employé.
     * @param prenom              Le prénom de l'employé.
     * @param poste               Le poste occupé par l'employé.
     * @param jours_conge_restants Le nombre de jours de congé restants pour l'employé.
     * @param mdp                 Le mot de passe de l'employé.
     * @param statut              Le statut de l'employé.
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
     * Recherche la fiche de paie correspondant à un mois et une année donnés.
     *
     * @param month Le mois recherché.
     * @param year  L'année recherchée.
     * @return Le fichier correspondant à la fiche de paie si trouvé, sinon null.
     */
    public File findPaySlipFile(int month, int year) {
        File dir = new File("resources/fiches_paie");
        if (!dir.exists() || !dir.isDirectory()) {
            return null;
        }
        String expectedFileName = this.prenom.toLowerCase() + "_" + this.nom.toLowerCase() + "_" +
                String.format("%02d", month) + "-" + year + "_fiche_paie.pdf";
        File[] matchingFiles = dir.listFiles((d, filename) -> filename.equalsIgnoreCase(expectedFileName));
        return (matchingFiles != null && matchingFiles.length > 0) ? matchingFiles[0] : null;
    }
    
    /**
     * Télécharge la fiche de paie pour le mois et l'année spécifiés.
     * <p>
     * Cette méthode recherche la fiche de paie correspondante et, si elle est trouvée,
     * ouvre un dialogue permettant à l'utilisateur de choisir l'emplacement de sauvegarde.
     * </p>
     *
     * @param parent Le composant parent pour le dialogue.
     * @param month  Le mois de la fiche de paie.
     * @param year   L'année de la fiche de paie.
     */
    public void downloadPaySlip(Component parent, int month, int year) {
        File fichePaieFile = findPaySlipFile(month, year);
        if (fichePaieFile == null) {
            JOptionPane.showMessageDialog(parent,
                    "Aucune fiche de paie trouvée pour " + this.prenom + " " + this.nom +
                            " en " + month + "/" + year,
                    "Erreur", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setSelectedFile(new File(this.prenom + "_" + this.nom + "_" +
                month + "_" + year + "_fiche_paie.pdf"));
        
        int userChoice = fileChooser.showSaveDialog(parent);
        if (userChoice == JFileChooser.APPROVE_OPTION) {
            File destinationFile = fileChooser.getSelectedFile();
            try {
                Files.copy(fichePaieFile.toPath(), destinationFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
                JOptionPane.showMessageDialog(parent,
                        "Fiche de paie téléchargée avec succès.\n" + destinationFile.getAbsolutePath(),
                        "Succès", JOptionPane.INFORMATION_MESSAGE);
            } catch (IOException ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(parent,
                        "Erreur lors du téléchargement de la fiche de paie : " + ex.getMessage(),
                        "Erreur", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
    
    /**
     * Enregistre une demande de congé en ajoutant une nouvelle ligne dans le fichier "conge.csv".
     * <p>
     * La méthode convertit les dates de début et de fin à partir des chaînes fournies, calcule
     * le nombre de jours ouvrés entre ces dates, et écrit la demande dans le fichier.
     * </p>
     *
     * @param parent       Le composant parent pour afficher les messages.
     * @param dateDebutStr La date de début du congé au format "dd/MM/yyyy".
     * @param dateFinStr   La date de fin du congé au format "dd/MM/yyyy".
     * @throws IOException Si une erreur d'entrée/sortie se produit lors de la lecture ou de l'écriture du fichier.
     */
    public void enregistrerConge(Component parent, String dateDebutStr, String dateFinStr) throws IOException {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        LocalDate dateDebut = LocalDate.parse(dateDebutStr, formatter);
        LocalDate dateFin = LocalDate.parse(dateFinStr, formatter);

        String nom = "";
        String prenom = "";
        String idUtilisateur = "";

        String onrecupidici = this.nom + " " + this.prenom;

        try (BufferedReader br = new BufferedReader(new FileReader("resources\\Utilisateurs.csv"))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(";");
                if (parts.length > 2 && (parts[1] + " " + parts[2]).equals(onrecupidici)) {
                    idUtilisateur = parts[0];
                    nom = parts[1];
                    prenom = parts[2];
                    break;
                }
            }
        } catch (IOException e) {
            JOptionPane.showMessageDialog(parent, "Erreur lors de la lecture du fichier Utilisateurs.csv : " + e.getMessage(), "Erreur", JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (idUtilisateur.isEmpty()) {
            JOptionPane.showMessageDialog(parent, "Utilisateur non trouvé dans le fichier Utilisateurs.csv.", "Erreur", JOptionPane.ERROR_MESSAGE);
            return;
        }

        long nbJoursOuvres = calculerJoursOuvres(dateDebut, dateFin);

        try (BufferedWriter writer = new BufferedWriter(new FileWriter("resources\\conge.csv", true))) {
            writer.write(String.format("%s;%s;%s;%s;%s;%d;%s;%s;%s;\n",
                    idUtilisateur, nom, prenom, dateDebutStr, dateFinStr, nbJoursOuvres, "En attente", "", "N"));
        }
    }
    
    /**
     * Calcule le nombre de jours ouvrés entre deux dates.
     * <p>
     * Les jours ouvrés sont considérés comme tous les jours sauf le samedi et le dimanche.
     * </p>
     *
     * @param dateDebut La date de début.
     * @param dateFin   La date de fin.
     * @return Le nombre de jours ouvrés entre la date de début et la date de fin.
     */
    private long calculerJoursOuvres(LocalDate dateDebut, LocalDate dateFin) {
        long nbJours = 0;

        if (dateDebut.isEqual(dateFin)) {
            return 1;
        }

        LocalDate dateCourante = dateDebut;
        while (dateCourante.isBefore(dateFin.plusDays(1))) {
            if (dateCourante.getDayOfWeek() != DayOfWeek.SATURDAY && dateCourante.getDayOfWeek() != DayOfWeek.SUNDAY) {
                nbJours++;
            }
            dateCourante = dateCourante.plusDays(1);
        }

        return nbJours;
    }
}
