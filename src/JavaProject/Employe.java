package JavaProject;

import javax.swing.*;
import java.awt.Component;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class Employe extends Utilisateur {
    
    public Employe(int id, String nom, String prenom, String poste, int jours_conge_restants, String mdp, String statut) {
        super(id, nom, prenom, poste, jours_conge_restants, mdp, statut);
    }

    public Employe() {
        super();
    }
    
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
    
    public void downloadPaySlip(Component parent, int month, int year) {
        File paySlipFile = findPaySlipFile(month, year);
        if (paySlipFile == null) {
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
                Files.copy(paySlipFile.toPath(), destinationFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
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
    
    // Nouvelle méthode enregistrerConge déplacée depuis CongeRequest
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
    
    // Méthode privée pour calculer les jours ouvrés entre deux dates
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
