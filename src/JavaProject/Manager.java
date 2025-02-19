package JavaProject;

import javax.swing.*;
import java.io.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;

import com.itextpdf.kernel.colors.ColorConstants;
import com.itextpdf.kernel.colors.DeviceRgb;
import com.itextpdf.kernel.pdf.*;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.properties.TextAlignment;
import com.itextpdf.layout.borders.SolidBorder;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.properties.UnitValue;

/**
 * Classe Manager héritant de la classe Utilisateur.
 * Cette classe permet la gestion des utilisateurs et inclut désormais
 * les méthodes pour enregistrer une paie, obtenir le prochain ID de paie,
 * récupérer le taux horaire d’un poste, récupérer les informations d’un utilisateur,
 * ainsi que des méthodes de gestion de la liste des utilisateurs pour l'interface de salaires.
 */
public class Manager extends Utilisateur {

    // ---------------------- CONSTRUCTEURS -----------------------//
    public Manager(int id, String nom, String prenom, String poste, int jours_conge_restants, String mdp, String statut) {
        super(id, nom, prenom, poste, jours_conge_restants, mdp, statut);
    }

    public Manager() {
        super();
    }
    
    // ---------------------- GESTION DES UTILISATEURS -----------------------//
    
    public static void ajouter_utilisateur(String nom, String prenom, String poste, int jours_conge_restants, String mdp, String statut) {
        String ligne;
        int nb_lignes = 0;
        String path_csv = "resources/Utilisateurs.csv";

        try (BufferedReader br = new BufferedReader(new FileReader(path_csv))) {
            br.readLine(); // Ignorer l'en-tête
            while ((ligne = br.readLine()) != null) {
                nb_lignes++;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        try (FileWriter writer = new FileWriter(path_csv, true);
             BufferedWriter bw = new BufferedWriter(writer);
             PrintWriter out = new PrintWriter(bw)) {

            String nouvelleLigne = (nb_lignes + 1) + ";" +
                                    nom + ";" +
                                    prenom + ";" +
                                    poste + ";" +
                                    jours_conge_restants + ";" +
                                    mdp + ";" +
                                    statut;
            out.println(nouvelleLigne);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    /**
     * Retourne un tableau de chaînes contenant la liste des utilisateurs.
     * Cette méthode est utilisée dans DeleteUserInterface pour remplir la JComboBox.
     * @return Un tableau de chaînes (ex: "Prénom Nom").
     */
    public static String[] loadUsersFromCSV() {
        ArrayList<String> usersList = new ArrayList<>();
        String filePath = "resources/Utilisateurs.csv"; // Chemin relatif vers le fichier CSV

        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            boolean isFirstLine = true; // Pour ignorer l'en-tête
            while ((line = br.readLine()) != null) {
                if (isFirstLine) {
                    isFirstLine = false;
                    continue;
                }
                String[] columns = line.split(";");
                if (columns.length >= 3) { // Vérifier que les colonnes existent
                    String fullName = columns[1] + " " + columns[2];
                    usersList.add(fullName);
                }
            }
        } catch (IOException e) {
            System.out.println("Erreur lors de la lecture du fichier CSV : " + e.getMessage());
        }
        if (usersList.isEmpty()) {
            usersList.add("Aucun utilisateur trouvé");
        }
        return usersList.toArray(new String[0]);
    }
    
    /**
     * Supprime un utilisateur du fichier CSV en fonction de son nom complet.
     * @param selectedUser Le nom complet de l'utilisateur à supprimer.
     */
    public static void deleteUserFromCSV(String selectedUser) {
        String filePath = "resources/Utilisateurs.csv"; // Chemin vers le fichier CSV
        ArrayList<String> updatedLines = new ArrayList<>();

        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            boolean isFirstLine = true; // Pour conserver l'en-tête
            while ((line = br.readLine()) != null) {
                if (isFirstLine) {
                    updatedLines.add(line); // Garder l'en-tête
                    isFirstLine = false;
                    continue;
                }
                String[] columns = line.split(";");
                if (columns.length >= 3) {
                    String fullName = columns[1] + " " + columns[2];
                    if (!fullName.equalsIgnoreCase(selectedUser)) {
                        updatedLines.add(line); // Conserver les lignes qui ne correspondent pas
                    }
                }
            }
        } catch (IOException e) {
            System.out.println("Erreur lors de la lecture du fichier CSV : " + e.getMessage());
        }

        // Réécrire le fichier CSV avec les lignes mises à jour
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(filePath))) {
            for (String updatedLine : updatedLines) {
                bw.write(updatedLine);
                bw.newLine();
            }
        } catch (IOException e) {
            System.out.println("Erreur lors de l'écriture du fichier CSV : " + e.getMessage());
        }
    }
    
    // ---------------------- CALCUL DU SALAIRE ET FICHE DE PAIE -----------------------//

    public static double[] calculer_salaire(JFrame parent, String selectedUser) {
        String path_paie = "resources/paie.csv";
        double salaireBrut = 0, primes = 0, cotisations = 0, impots = 0;
        String moisPaie = "0", anneePaie = "0";
        boolean found = false;

        try (BufferedReader br = new BufferedReader(new FileReader(path_paie))) {
            String ligne;
            while ((ligne = br.readLine()) != null) {
                String[] colonne = ligne.split(";");
                if (colonne.length < 14) continue;
                // On suppose que colonne[4] correspond au nom et colonne[5] au prénom
                if ((colonne[5] + " " + colonne[4]).equalsIgnoreCase(selectedUser)) {
                    found = true;
                    moisPaie = colonne[1];
                    anneePaie = colonne[2];
                    salaireBrut += safeParseDouble(colonne[10]);
                    primes += safeParseDouble(colonne[11]);
                    cotisations += safeParseDouble(colonne[12]);
                    impots += safeParseDouble(colonne[13]);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        if (!found) {
            JOptionPane.showMessageDialog(parent, 
                "Il n'y a pas de paie pour le mois en cours pour " + selectedUser, 
                "Alerte", JOptionPane.WARNING_MESSAGE);
            return null;
        }

        double salaireNet = salaireBrut + primes - cotisations - impots;
        double mois = safeParseDouble(moisPaie);
        double annee = safeParseDouble(anneePaie);

        return new double[]{salaireBrut, primes, cotisations, impots, salaireNet, mois, annee};
    }

    public static void genererFichePaie(JFrame parent, String selectedUser) {
        if (selectedUser == null || selectedUser.isEmpty()) {
            JOptionPane.showMessageDialog(parent, "Veuillez sélectionner un utilisateur.", "Erreur", JOptionPane.ERROR_MESSAGE);
            return;
        }

        double[] salaireDetails = calculer_salaire(parent, selectedUser);
        if (salaireDetails == null) {
            return;
        }

        double salaireBrut = salaireDetails[0];
        double primes = salaireDetails[1];
        double cotisations = salaireDetails[2];
        double impots = salaireDetails[3];
        double salaireNet = salaireDetails[4];
        int moisPaie = (int) salaireDetails[5];
        int anneePaie = (int) salaireDetails[6];

        String nomFichier = String.format("resources/fiches_paie/%s_%02d-%d_fiche_paie.pdf", 
                selectedUser.replace(" ", "_"), moisPaie, anneePaie);

        try {
            PdfWriter writer = new PdfWriter(nomFichier);
            PdfDocument pdf = new PdfDocument(writer);
            Document document = new Document(pdf);

            DeviceRgb accentColor = new DeviceRgb(255, 204, 0);
            DeviceRgb headerBgColor = new DeviceRgb(43, 60, 70);
            DeviceRgb headerTextColor = new DeviceRgb(255, 255, 255);

            document.add(new Paragraph("BULLETIN DE PAIE")
                    .setFontSize(18)
                    .setBold()
                    .setTextAlignment(TextAlignment.CENTER)
                    .setFontColor(accentColor));

            document.add(new Paragraph(String.format("Mois: %02d/%d", moisPaie, anneePaie))
                    .setFontSize(12)
                    .setTextAlignment(TextAlignment.CENTER)
                    .setFontColor(ColorConstants.DARK_GRAY));

            document.add(new Paragraph("Employé : " + selectedUser)
                    .setFontSize(14)
                    .setBold()
                    .setTextAlignment(TextAlignment.CENTER)
                    .setFontColor(ColorConstants.BLACK));

            document.add(new Paragraph("\n"));

            Table table = new Table(UnitValue.createPercentArray(new float[]{4, 3}))
                    .useAllAvailableWidth()
                    .setBorder(new SolidBorder(ColorConstants.GRAY, 1));

            table.addCell(createHeaderCell("Description", headerBgColor, headerTextColor));
            table.addCell(createHeaderCell("Montant (€)", headerBgColor, headerTextColor));

            table.addCell(createBodyCell("Salaire Brut"));
            table.addCell(createBodyCell(String.format("%.2f", salaireBrut)));

            table.addCell(createBodyCell("Primes"));
            table.addCell(createBodyCell(String.format("%.2f", primes)));

            table.addCell(createBodyCell("Cotisations Sociales"));
            table.addCell(createBodyCell(String.format("-%.2f", cotisations)));

            table.addCell(createBodyCell("Impôts"));
            table.addCell(createBodyCell(String.format("-%.2f", impots)));

            table.addCell(createFooterCell("Salaire Net à Payer"));
            table.addCell(createFooterCell(String.format("%.2f", salaireNet)));

            document.add(table);
            document.close();

            JOptionPane.showMessageDialog(parent, "Fiche de paie générée avec succès : " + nomFichier, 
                    "Succès", JOptionPane.INFORMATION_MESSAGE);

        } catch (IOException e) {
            JOptionPane.showMessageDialog(parent, "Erreur lors de la génération du PDF.", "Erreur", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    // ---------------- Méthodes utilitaires pour la génération de PDF ---------------- //

    private static Cell createHeaderCell(String text, DeviceRgb bgColor, DeviceRgb textColor) {
        return new Cell()
                .add(new Paragraph(text))
                .setBold()
                .setFontSize(12)
                .setTextAlignment(TextAlignment.CENTER)
                .setBackgroundColor(bgColor)
                .setFontColor(textColor)
                .setBorder(new SolidBorder(1));
    }

    private static Cell createBodyCell(String text) {
        return new Cell()
                .add(new Paragraph(text))
                .setFontSize(12)
                .setTextAlignment(TextAlignment.RIGHT)
                .setBorder(new SolidBorder(1));
    }

    private static Cell createFooterCell(String text) {
        return new Cell()
                .add(new Paragraph(text))
                .setFontSize(12)
                .setBold()
                .setTextAlignment(TextAlignment.RIGHT)
                .setBorder(new SolidBorder(1));
    }

    private static double safeParseDouble(String value) {
        try {
            return value == null || value.isEmpty() ? 0.0 : Double.parseDouble(value.replace(",", "."));
        } catch (NumberFormatException e) {
            System.err.println("Erreur de conversion en double : " + value);
            return 0.0;
        }
    }
    
    // ---------------- Méthodes de gestion des utilisateurs pour l'interface ---------------- //
    
    /**
     * Charge tous les utilisateurs en lisant le fichier CSV et en retournant une liste de noms complets.
     * @return La liste des utilisateurs (format "Prénom Nom").
     */
    public static List<String> chargerUtilisateurs() {
        return Utilisateur.func_recup_data("resources/Utilisateurs.csv")
                .stream()
                .map(u -> u.prenom + " " + u.nom)
                .collect(Collectors.toList());
    }
    
    /**
     * Filtre la liste des utilisateurs en fonction d'un texte de recherche.
     * @param utilisateurs La liste complète des utilisateurs.
     * @param recherche Le texte de recherche.
     * @return Une liste filtrée d'utilisateurs contenant la chaîne recherchée.
     */
    public static List<String> filtrerUtilisateurs(List<String> utilisateurs, String recherche) {
        return utilisateurs.stream()
                .filter(nom -> nom.toLowerCase().contains(recherche.toLowerCase()))
                .limit(5)
                .collect(Collectors.toList());
    }
    
    /**
     * Met à jour le modèle de la liste déroulante avec une liste d'utilisateurs.
     * @param comboBoxModel Le modèle du JComboBox.
     * @param utilisateursFiltrés La liste d'utilisateurs à afficher.
     */
    public static void mettreAJourListe(DefaultComboBoxModel<String> comboBoxModel, List<String> utilisateursFiltrés) {
        comboBoxModel.removeAllElements();
        utilisateursFiltrés.forEach(comboBoxModel::addElement);
    }
    
    /**
     * Calcule et affiche le salaire de l'utilisateur sélectionné en mettant à jour le label correspondant.
     * @param parent La fenêtre parente.
     * @param manager L'instance de Manager utilisée pour le calcul.
     * @param selectedUser L'utilisateur sélectionné (nom complet).
     * @param salaryLabel Le label à mettre à jour avec le salaire net.
     */
    public static void calculerSalaireUI(JFrame parent, Manager manager, String selectedUser, JLabel salaryLabel) {
        if (selectedUser == null || selectedUser.isEmpty()) {
            JOptionPane.showMessageDialog(parent, "Veuillez sélectionner un utilisateur.", "Erreur", JOptionPane.ERROR_MESSAGE);
            return;
        }
        double[] salaireDetails = manager.calculer_salaire(parent, selectedUser);
        if (salaireDetails == null) {
            return;
        }
        salaryLabel.setText("Salaire Net : " + String.format("%.2f €", salaireDetails[4]));
        salaryLabel.repaint();
        salaryLabel.revalidate();
    }
    
    // ---------------------- GESTION DE LA PAIE ----------------------- //
    
    /**
     * Retourne le prochain ID de paie à utiliser en scannant le fichier "paie.csv".
     * @return Le prochain ID de paie disponible.
     */
    public static int getNextPaieId() {
        int maxId = 0;
        File file = new File("resources/paie.csv");

        if (!file.exists() || file.length() == 0) {
            return 1;
        }

        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line;
            br.readLine(); // Ignorer l'en-tête
            while ((line = br.readLine()) != null) {
                String[] columns = line.split(";");
                if (columns.length > 0 && !columns[0].trim().isEmpty()) {
                    try {
                        int currentId = Integer.parseInt(columns[0].trim());
                        if (currentId > maxId) {
                            maxId = currentId;
                        }
                    } catch (NumberFormatException e) {
                        System.err.println("Erreur conversion ID paie : " + columns[0]);
                    }
                }
            }
        } catch (IOException e) {
            System.err.println("Impossible de lire le fichier paie.csv : " + e.getMessage());
        }

        return maxId + 1;
    }
    
    /**
     * Enregistre la paie en écrivant une nouvelle ligne dans le fichier "paie.csv".
     * Après enregistrement, un message de succès est affiché et la fenêtre passée en paramètre est fermée.
     *
     * @param heuresTravail Nombre d'heures travaillées
     * @param primes        Montant des primes
     * @param cotisations   Montant des cotisations
     * @param impots        Montant des impôts
     * @param utilisateur   L'utilisateur concerné
     * @param tauxHoraire   Le taux horaire de l'utilisateur
     * @param frame         La fenêtre à fermer après enregistrement
     */
    public static void enregistrerPaie(int heuresTravail, double primes, double cotisations, double impots,
                                       Utilisateur utilisateur, double tauxHoraire, JFrame frame) {
        try {
            double salaireBrut = tauxHoraire * heuresTravail;
            double salaireNet = salaireBrut + primes - cotisations - impots;
            int nextId = getNextPaieId();
            LocalDate currentDate = LocalDate.now();

            String newPaie = String.format("%d;%d;%d;%d;%s;%s;%s;%.2f;%d;%d;%.2f;%.2f;%.2f;%.2f",
                    nextId, currentDate.getMonthValue(), currentDate.getYear(),
                    utilisateur.id, utilisateur.nom, utilisateur.prenom, utilisateur.poste,
                    tauxHoraire, heuresTravail, utilisateur.jours_conge_restants,
                    salaireBrut, primes, cotisations, impots);

            File file = new File("resources/paie.csv");
            boolean isEmpty = file.length() == 0;

            try (FileWriter fw = new FileWriter(file, true);
                 BufferedWriter bw = new BufferedWriter(fw);
                 PrintWriter out = new PrintWriter(bw)) {

                if (isEmpty) {
                    out.println("idPaie;moisPaie;anneePaie;idUtilisateur;nom;prenom;poste;taux_horaire;nb_heure_travaille;nb_conge;salaire;primes;cotisations;impots");
                }
                out.println(newPaie);
            }

            JOptionPane.showMessageDialog(frame, "Fiche de paie enregistrée avec succès !");
            frame.dispose();
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(frame, "Veuillez entrer des valeurs valides.", "Erreur", JOptionPane.ERROR_MESSAGE);
        } catch (IOException ex) {
            JOptionPane.showMessageDialog(frame, "Erreur lors de l'enregistrement de la paie.", "Erreur", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    /**
     * Récupère le taux horaire correspondant à un poste donné à partir du fichier "taux_horraire_poste.csv".
     * @param poste Le poste de l'utilisateur.
     * @return Le taux horaire en euros ou 0.0 en cas d'erreur.
     */
    public static double getTauxHoraire(String poste) {
        String path = "resources/taux_horraire_poste.csv";
        try (BufferedReader br = new BufferedReader(new FileReader(path))) {
            br.readLine(); // Ignorer l'en-tête
            String ligne;
            while ((ligne = br.readLine()) != null) {
                String[] parts = ligne.split(";");
                if (parts.length == 2 && parts[0].trim().equalsIgnoreCase(poste.trim())) {
                    return Double.parseDouble(parts[1].trim().replace(",", "."));
                }
            }
        } catch (IOException | NumberFormatException e) {
            System.err.println("Erreur lecture fichier taux_horraire_poste.csv : " + e.getMessage());
        }
        return 0.0;
    }
    
    /**
     * Récupère les informations d'un utilisateur à partir de son nom complet,
     * en lisant le fichier "Utilisateurs.csv".
     * @param selectedUser Le nom complet de l'utilisateur (prenom + " " + nom)
     * @return Un objet Utilisateur contenant ses informations ou un utilisateur par défaut en cas d'absence.
     */
    public static Utilisateur getUtilisateurInfo(String selectedUser) {
        List<Utilisateur> users = Utilisateur.func_recup_data("resources/Utilisateurs.csv");
        return users.stream()
                .filter(u -> (u.prenom + " " + u.nom).equalsIgnoreCase(selectedUser))
                .findFirst()
                .orElse(new Utilisateur(0, "Inconnu", "Inconnu", "Aucun", 0, "", ""));
    }
}
