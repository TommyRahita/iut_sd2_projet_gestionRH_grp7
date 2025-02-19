package JavaProject;

import javax.swing.*;
import java.awt.Component;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
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
        for (Employe employe : liste_employe) {
            if (id_saisi.equals(employe.id)) {
                return true;
            }
        }
        return false;
    }
    
    // ---------------- Méthodes de téléchargement de fiche de paie ---------------- //
    
    /**
     * Recherche une fiche de paie spécifique pour cet employé en fonction du mois et de l'année.
     * @param month Mois de la fiche de paie.
     * @param year Année de la fiche de paie.
     * @return Un objet File correspondant à la fiche de paie si trouvé, sinon null.
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
     * Télécharge la fiche de paie de cet employé pour le mois et l'année spécifiés.
     * Affiche une boîte de dialogue pour sélectionner l'emplacement de sauvegarde.
     * @param parent Composant parent pour les boîtes de dialogue.
     * @param month Mois de la fiche de paie.
     * @param year Année de la fiche de paie.
     */
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
}
