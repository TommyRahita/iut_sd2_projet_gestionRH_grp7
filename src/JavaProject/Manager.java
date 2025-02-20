package JavaProject;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
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
 * Classe Manager.
 * <p>
 * Cette classe gère l'ensemble des fonctionnalités relatives à la gestion des utilisateurs, de la paie et des congés
 * dans le système. Elle étend la classe Utilisateur et fournit des méthodes pour ajouter des utilisateurs, calculer
 * le taux horaire en fonction du poste, générer des fiches de paie, et gérer les congés ainsi que les opérations
 * associées (lecture/écriture sur fichiers CSV, génération de PDF, etc.).
 * </p>
 * 
 * @author Groupe 7
 * @version 1.0
 */
public class Manager extends Utilisateur {

    // ---------------------- CONSTRUCTEURS -----------------------//

    /**
     * Constructeur complet de la classe Manager.
     *
     * @param id                  L'identifiant du manager.
     * @param nom                 Le nom du manager.
     * @param prenom              Le prénom du manager.
     * @param poste               Le poste occupé.
     * @param jours_conge_restants Le nombre de jours de congé restants.
     * @param mdp                 Le mot de passe.
     * @param statut              Le statut (par exemple "gestionnaire" ou "employé").
     */
    public Manager(int id, String nom, String prenom, String poste, int jours_conge_restants, String mdp, String statut) {
        super(id, nom, prenom, poste, jours_conge_restants, mdp, statut);
    }

    /**
     * Constructeur par défaut de la classe Manager.
     */
    public Manager() {
        super();
    }
    
    // ---------------------- MÉTHODES AJOUTER UTILISATEUR -----------------------//

    /**
     * Ajoute un nouvel utilisateur dans le fichier CSV en utilisant les informations fournies.
     * <p>
     * Cette méthode lit d'abord le fichier CSV pour déterminer le nombre de lignes existantes, puis
     * ajoute une nouvelle ligne correspondant aux informations de l'utilisateur.
     * </p>
     *
     * @param nom                 Le nom de l'utilisateur.
     * @param prenom              Le prénom de l'utilisateur.
     * @param poste               Le poste de l'utilisateur.
     * @param jours_conge_restants Le nombre de jours de congé restants pour l'utilisateur.
     * @param mdp                 Le mot de passe de l'utilisateur.
     * @param statut              Le statut de l'utilisateur (ex : "gestionnaire", "employé").
     */
    public static void ajouter_utilisateur(String nom, String prenom, String poste, int jours_conge_restants, String mdp, String statut) {
        String path_csv = "resources/Utilisateurs.csv";
        int nb_lignes = 0;
        try (BufferedReader br = new BufferedReader(new FileReader(path_csv))) {
            br.readLine(); // Ignorer l'en-tête
            while (br.readLine() != null) {
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
    
    // ---------------------- MÉTHODE CALCULER SALAIRE -----------------------//

    /**
     * Calcule et retourne le taux horaire en fonction du poste de l'utilisateur identifié par son nom et prénom.
     * <p>
     * La méthode parcourt le fichier CSV des utilisateurs afin d'identifier l'utilisateur concerné et lit
     * ensuite le fichier CSV des taux horaires pour trouver le taux correspondant au poste de cet utilisateur.
     * </p>
     *
     * @param nom    Le nom de l'utilisateur.
     * @param prenom Le prénom de l'utilisateur.
     * @return Le taux horaire en float, ou -1 en cas d'erreur ou si le poste n'est pas trouvé.
     */
    public static float calculer_salaire(String nom, String prenom) {
        String path_th = "Ressources/taux_horraire_poste.csv";
        String path_utilisateurs = "Ressources/Utilisateurs.csv";
        String ligne;
        try (BufferedReader br_utilisateurs = new BufferedReader(new FileReader(path_utilisateurs))) {
            br_utilisateurs.readLine(); // Ignorer l'en-tête
            while ((ligne = br_utilisateurs.readLine()) != null) {
                String[] colonne_utilisateur = ligne.split(";");
                String nom_utilisateur = colonne_utilisateur[1];
                String prenom_utilisateur = colonne_utilisateur[2];
                String poste_utilisateur = colonne_utilisateur[3];
                if (nom_utilisateur.equals(nom) && prenom_utilisateur.equals(prenom)) {
                    try (BufferedReader br_th = new BufferedReader(new FileReader(path_th))) {
                        br_th.readLine(); // Ignorer l'en-tête
                        while ((ligne = br_th.readLine()) != null) {
                            String[] colonne_th = ligne.split(";");
                            String poste_th = colonne_th[0];
                            float taux_horraire = Float.parseFloat(colonne_th[1]);
                            if (poste_utilisateur.equals(poste_th)) {
                                return taux_horraire;
                            }
                        }
                    }
                    break;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return -1;
    }
    
    // ---------------------- AUTRES MÉTHODES DE GESTION DES UTILISATEURS -----------------------//

    /**
     * Récupère la liste des utilisateurs à partir du fichier CSV.
     *
     * @return Une liste de chaînes représentant les noms complets des utilisateurs.
     */
    public static List<String> chargerUtilisateurs() {
        return Utilisateur.func_recup_data("resources/Utilisateurs.csv")
                .stream()
                .map(u -> u.prenom + " " + u.nom)
                .collect(Collectors.toList());
    }

    /**
     * Charge les utilisateurs depuis le fichier CSV sous forme de tableau de chaînes.
     *
     * @return Un tableau contenant les noms complets des utilisateurs.
     */
    public static String[] loadUsersFromCSV() {
        List<String> list = chargerUtilisateurs();
        return list.toArray(new String[0]);
    }
    
    /**
     * Supprime l'utilisateur sélectionné du fichier CSV.
     * <p>
     * La méthode lit le fichier CSV, exclut la ligne correspondant à l'utilisateur sélectionné, et réécrit le fichier.
     * </p>
     *
     * @param selectedUser Le nom complet de l'utilisateur à supprimer.
     */
    public static void deleteUserFromCSV(String selectedUser) {
        String filePath = "resources/Utilisateurs.csv";
        ArrayList<String> updatedLines = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            boolean isFirstLine = true;
            while ((line = br.readLine()) != null) {
                if (isFirstLine) {
                    updatedLines.add(line);
                    isFirstLine = false;
                    continue;
                }
                String[] columns = line.split(";");
                if (columns.length >= 3) {
                    String fullName = columns[1] + " " + columns[2];
                    if (!fullName.equalsIgnoreCase(selectedUser)) {
                        updatedLines.add(line);
                    }
                }
            }
        } catch (IOException e) {
            System.out.println("Erreur lors de la lecture du fichier CSV : " + e.getMessage());
        }
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(filePath))) {
            for (String updatedLine : updatedLines) {
                bw.write(updatedLine);
                bw.newLine();
            }
        } catch (IOException e) {
            System.out.println("Erreur lors de l'écriture du fichier CSV : " + e.getMessage());
        }
    }
    
    // ---------------------- CALCUL DU SALAIRE ET GÉNÉRATION DE FICHE DE PAIE -----------------------//

    /**
     * Calcule les informations de paie pour un utilisateur sélectionné en fonction de son nom complet.
     * <p>
     * La méthode lit le fichier CSV de paie pour accumuler les valeurs de salaire brut, primes, cotisations et impôts.
     * Si l'utilisateur est trouvé, elle retourne un objet Salaire contenant ces valeurs ainsi que la date de paie.
     * </p>
     *
     * @param parent       La fenêtre parente pour l'affichage des messages.
     * @param selectedUser Le nom complet de l'utilisateur.
     * @return Un objet Salaire contenant les détails de paie ou null si aucune paie n'est trouvée.
     */
    public static Salaire calculer_salaire(JFrame parent, String selectedUser) {
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
        int mois = (int) safeParseDouble(moisPaie);
        int annee = (int) safeParseDouble(anneePaie);
        return new Salaire(salaireBrut, primes, cotisations, impots, salaireNet, mois, annee);
    }

    /**
     * Génère la fiche de paie en format PDF pour l'utilisateur sélectionné.
     * <p>
     * La méthode récupère les détails de paie via {@code calculer_salaire}, construit le document PDF
     * avec un tableau récapitulatif et l'enregistre dans le répertoire prévu. Un message de confirmation est affiché.
     * </p>
     *
     * @param parent       La fenêtre parente pour l'affichage des messages.
     * @param selectedUser Le nom complet de l'utilisateur.
     */
    public static void genererFichePaie(JFrame parent, String selectedUser) {
        if (selectedUser == null || selectedUser.isEmpty()) {
            JOptionPane.showMessageDialog(parent, "Veuillez sélectionner un utilisateur.", "Erreur", JOptionPane.ERROR_MESSAGE);
            return;
        }
        Salaire salaireDetails = calculer_salaire(parent, selectedUser);
        if (salaireDetails == null) {
            return;
        }
        double salaireBrut = salaireDetails.getSalaireBrut();
        double primes = salaireDetails.getPrimes();
        double cotisations = salaireDetails.getCotisations();
        double impots = salaireDetails.getImpots();
        double salaireNet = salaireDetails.getSalaireNet();
        int moisPaie = salaireDetails.getMois();
        int anneePaie = salaireDetails.getAnnee();
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
    
    // ---------------- MÉTHODES UTILITAIRES POUR LA GÉNÉRATION DE PDF ---------------- //

    /**
     * Crée une cellule d'en-tête pour le tableau de la fiche de paie.
     *
     * @param text      Le texte de l'en-tête.
     * @param bgColor   La couleur de fond.
     * @param textColor La couleur du texte.
     * @return Une cellule formatée pour l'en-tête.
     */
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

    /**
     * Crée une cellule de corps pour le tableau de la fiche de paie.
     *
     * @param text Le texte à afficher dans la cellule.
     * @return Une cellule formatée pour le corps du tableau.
     */
    private static Cell createBodyCell(String text) {
        return new Cell()
                .add(new Paragraph(text))
                .setFontSize(12)
                .setTextAlignment(TextAlignment.RIGHT)
                .setBorder(new SolidBorder(1));
    }

    /**
     * Crée une cellule de pied de tableau pour le tableau de la fiche de paie.
     *
     * @param text Le texte à afficher dans la cellule.
     * @return Une cellule formatée pour le pied du tableau.
     */
    private static Cell createFooterCell(String text) {
        return new Cell()
                .add(new Paragraph(text))
                .setFontSize(12)
                .setBold()
                .setTextAlignment(TextAlignment.RIGHT)
                .setBorder(new SolidBorder(1));
    }

    /**
     * Convertit une chaîne en double en gérant les valeurs nulles ou vides.
     *
     * @param value La chaîne à convertir.
     * @return La valeur double correspondante, ou 0.0 si la chaîne est nulle ou vide.
     */
    private static double safeParseDouble(String value) {
        try {
            return value == null || value.isEmpty() ? 0.0 : Double.parseDouble(value.replace(",", "."));
        } catch (NumberFormatException e) {
            System.err.println("Erreur de conversion en double : " + value);
            return 0.0;
        }
    }
    
    // ---------------- MÉTHODES DE GESTION DES UTILISATEURS POUR L'INTERFACE ---------------- //

    /**
     * Filtre la liste des utilisateurs en fonction d'une chaîne de recherche.
     *
     * @param utilisateurs La liste complète des utilisateurs.
     * @param recherche    La chaîne à rechercher (non sensible à la casse).
     * @return Une liste contenant jusqu'à 5 utilisateurs correspondant à la recherche.
     */
    public static List<String> filtrerUtilisateurs(List<String> utilisateurs, String recherche) {
        return utilisateurs.stream()
                .filter(nom -> nom.toLowerCase().contains(recherche.toLowerCase()))
                .limit(5)
                .collect(Collectors.toList());
    }
    
    /**
     * Met à jour le modèle de la JComboBox avec la liste filtrée d'utilisateurs.
     *
     * @param comboBoxModel         Le modèle de la JComboBox.
     * @param utilisateursFiltrés   La liste des utilisateurs filtrés.
     */
    public static void mettreAJourListe(DefaultComboBoxModel<String> comboBoxModel, List<String> utilisateursFiltrés) {
        comboBoxModel.removeAllElements();
        utilisateursFiltrés.forEach(comboBoxModel::addElement);
    }
    
    /**
     * Calcule le salaire net et met à jour l'interface utilisateur pour l'afficher.
     *
     * @param parent         La fenêtre parente pour l'affichage des messages.
     * @param gestionnaire   L'instance Manager utilisée pour le calcul.
     * @param selectedUser   Le nom complet de l'utilisateur sélectionné.
     * @param salaireLabel   Le JLabel où le salaire net sera affiché.
     */
    public static void calculerSalaireUI(JFrame parent, Manager gestionnaire, String selectedUser, JLabel salaireLabel) {
        if (selectedUser == null || selectedUser.isEmpty()) {
            JOptionPane.showMessageDialog(parent, "Veuillez sélectionner un utilisateur.", "Erreur", JOptionPane.ERROR_MESSAGE);
            return;
        }
        Salaire salaireDetails = gestionnaire.calculer_salaire(parent, selectedUser);
        if (salaireDetails == null) {
            return;
        }
        salaireLabel.setText("Salaire Net : " + String.format("%.2f €", salaireDetails.getSalaireNet()));
        salaireLabel.repaint();
        salaireLabel.revalidate();
    }
    
    // ---------------------- GESTION DE LA PAIE ----------------------- //

    /**
     * Récupère l'ID suivant pour la fiche de paie en parcourant le fichier CSV.
     *
     * @return L'ID suivant pour la fiche de paie.
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
     * Enregistre une fiche de paie dans le fichier CSV en utilisant les paramètres fournis.
     *
     * @param heuresTravail Le nombre d'heures travaillées.
     * @param primes        Le montant des primes.
     * @param cotisations   Le montant des cotisations sociales.
     * @param impots        Le montant des impôts.
     * @param utilisateur   L'utilisateur concerné.
     * @param tauxHoraire   Le taux horaire du poste de l'utilisateur.
     * @param frame         La fenêtre parente pour l'affichage des messages.
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
     * Récupère le taux horaire pour un poste donné à partir du fichier CSV.
     *
     * @param poste Le poste pour lequel obtenir le taux horaire.
     * @return Le taux horaire correspondant, ou 0.0 en cas d'erreur.
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
     * Récupère les informations complètes d'un utilisateur à partir de son nom complet.
     *
     * @param selectedUser Le nom complet de l'utilisateur.
     * @return L'objet Utilisateur correspondant, ou un utilisateur par défaut si non trouvé.
     */
    public static Utilisateur getUtilisateurInfo(String selectedUser) {
        List<Utilisateur> users = Utilisateur.func_recup_data("resources/Utilisateurs.csv");
        return users.stream()
                .filter(u -> (u.prenom + " " + u.nom).equalsIgnoreCase(selectedUser))
                .findFirst()
                .orElse(new Utilisateur(0, "Inconnu", "Inconnu", "Aucun", 0, "", ""));
    }
    
    // ---------------------- GESTION DES CONGÉS ---------------------- //

    /**
     * Charge les données de congé à partir du fichier CSV et les ajoute dans le modèle de la JTable.
     *
     * @param tableModel Le modèle de la JTable dans lequel ajouter les données.
     * @param parent     Le composant parent pour l'affichage des messages en cas d'erreur.
     */
    public static void loadCongeData(DefaultTableModel tableModel, Component parent) {
        try (BufferedReader br = new BufferedReader(new FileReader("resources\\conge.csv"))) {
            String line;
            // Ignorer l'en-tête
            if ((line = br.readLine()) != null) {
                // En-tête ignoré
            }
            while ((line = br.readLine()) != null) {
                String[] data = line.split(";");
                if (data.length >= 9 && !data[6].equalsIgnoreCase("Validé") && !data[6].equalsIgnoreCase("Rejeté")) {
                    Object[] row = {
                        data[0],
                        data[1],
                        data[2],
                        data[3],
                        data[4],
                        data[5],
                        data[6],
                        createCongeActionButton(data[0], parent)
                    };
                    tableModel.addRow(row);
                }
            }
        } catch (IOException e) {
            JOptionPane.showMessageDialog(parent, "Erreur lors de la lecture du fichier conge.csv: " + e.getMessage(), "Erreur", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    /**
     * Crée un bouton d'action pour la gestion d'un congé.
     * <p>
     * Le bouton permet de choisir entre la validation ou le rejet d'une demande de congé.
     * </p>
     *
     * @param idConge Le numéro d'identification du congé.
     * @param parent  Le composant parent pour l'affichage des dialogues.
     * @return Un JButton configuré pour l'action sur le congé.
     */
    public static JButton createCongeActionButton(String idConge, Component parent) {
        JButton button = new JButton("Action");
        button.addActionListener(e -> {
            String[] options = {"Valider", "Rejeter"};
            int choice = JOptionPane.showOptionDialog(
                    parent,
                    "Choisir l'action à effectuer",
                    "Action",
                    JOptionPane.DEFAULT_OPTION,
                    JOptionPane.INFORMATION_MESSAGE,
                    null,
                    options,
                    options[0]
            );
            if (choice == 0) {
                updateCongeStatus(idConge, "Validé", parent);
                JOptionPane.showMessageDialog(parent, "Congé validé.");
            } else if (choice == 1) {
                updateCongeStatus(idConge, "Rejeté", parent);
                JOptionPane.showMessageDialog(parent, "Congé rejeté.");
            }
        });
        return button;
    }
    
    /**
     * Met à jour le statut d'un congé dans le fichier CSV.
     * <p>
     * La méthode lit le fichier CSV, met à jour le statut pour le congé correspondant à l'identifiant fourni,
     * et réécrit le fichier. En cas d'échec, un message d'erreur est affiché.
     * </p>
     *
     * @param idConge Le numéro d'identification du congé.
     * @param statut  Le nouveau statut à appliquer (par exemple "Validé" ou "Rejeté").
     * @param parent  Le composant parent pour l'affichage des messages en cas d'erreur.
     */
    public static void updateCongeStatus(String idConge, String statut, Component parent) {
        try {
            File inputFile = new File("resources\\conge.csv");
            File tempFile = new File("resources\\conge_temp.csv");
            try (BufferedReader reader = new BufferedReader(new FileReader(inputFile));
                 BufferedWriter writer = new BufferedWriter(new FileWriter(tempFile))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    String[] data = line.split(";");
                    if (data[0].equals(idConge)) {
                        data[6] = statut;
                    }
                    writer.write(String.join(";", data));
                    writer.newLine();
                }
            }
            if (inputFile.delete()) {
                if (!tempFile.renameTo(inputFile)) {
                    JOptionPane.showMessageDialog(parent, "Erreur lors de la mise à jour du fichier des congés.", "Erreur", JOptionPane.ERROR_MESSAGE);
                }
            }
        } catch (IOException e) {
            JOptionPane.showMessageDialog(parent, "Erreur lors de la mise à jour du statut du congé : " + e.getMessage(), "Erreur", JOptionPane.ERROR_MESSAGE);
        }
    }
}
