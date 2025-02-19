package JavaProject;

import javax.swing.*;
import java.io.*;
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
 * Cette classe permet la gestion des utilisateurs, notamment l'ajout d'un nouvel utilisateur.
 */
public class Manager extends Utilisateur {

    // ---------------------- CONSTRUCTEURS -----------------------//
    /**
     * Constructeur avec paramètres pour créer un manager.
     * 
     * @param id Identifiant de l'utilisateur
     * @param nom Nom de l'utilisateur
     * @param prenom Prénom de l'utilisateur
     * @param poste Poste occupé par l'utilisateur
     * @param jours_conge_restants Nombre de jours de congés restants
     * @param mdp Mot de passe de l'utilisateur
     * @param statut Statut de l'utilisateur (Manager ou Employé)
     */
    public Manager(int id, String nom, String prenom, String poste, int jours_conge_restants, String mdp, String statut) {
        super(id, nom, prenom, poste, jours_conge_restants, mdp, statut);
    }

    /**
     * Constructeur sans paramètres pour initialiser un manager par défaut.
     */
    public Manager() {
        super();
    }
    
    /**
     * Ajoute un nouvel utilisateur au fichier CSV contenant la liste des utilisateurs.
     * 
     * @param nom Nom du nouvel utilisateur
     * @param prenom Prénom du nouvel utilisateur
     * @param poste Poste du nouvel utilisateur
     * @param jours_conge_restants Nombre de jours de congé restants
     * @param mdp Mot de passe du nouvel utilisateur
     * @param statut Statut du nouvel utilisateur (Manager ou Employé)
     */
    public static void ajouter_utilisateur(String nom, String prenom, String poste, int jours_conge_restants, String mdp, String statut) {
        String ligne;
        int nb_lignes = 0;
        String path_csv = "resources/Utilisateurs.csv";

        // Compter le nombre de lignes pour générer un ID utilisateur unique
        try (BufferedReader br = new BufferedReader(new FileReader(path_csv))) {
            // Lire et ignorer la première ligne (en-tête)
            br.readLine();

            // Lire chaque ligne du fichier et compter le nombre d'entrées
            while ((ligne = br.readLine()) != null) {
                nb_lignes++;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Ajouter le nouvel utilisateur au fichier CSV
        try (FileWriter writer = new FileWriter(path_csv, true);
             BufferedWriter bw = new BufferedWriter(writer);
             PrintWriter out = new PrintWriter(bw)) {

            // Construire la ligne utilisateur avec un nouvel ID incrémenté
            String nouvelleLigne = (nb_lignes + 1) + ";" +
                                    nom + ";" +
                                    prenom + ";" +
                                    poste + ";" +
                                    jours_conge_restants + ";" +
                                    mdp + ";" +
                                    statut;

            out.println(nouvelleLigne); // Ajout direct avec un saut de ligne
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // ---------------------- CALCULER SALAIRE -----------------------//
    /**
     * Lit le fichier paie.csv et calcule les montants cumulés pour l'utilisateur sélectionné.
     * On suppose que le fichier paie.csv a la structure suivante :
     * idPaie;moisPaie;anneePaie;idUtilisateur;nom;prenom;poste;taux_horaire;nb_jour_travaille;nb_conge;salaire;primes;cotisations;impots
     *
     * @param parent       La fenêtre parent pour afficher un message si aucune paie n'est trouvée.
     * @param selectedUser Le nom complet de l'utilisateur (prenom + " " + nom)
     * @return Un tableau de double contenant les informations salariales ou null si aucune paie trouvée.
     */
    public static double[] calculer_salaire(JFrame parent, String selectedUser) {
        String path_paie = "resources/paie.csv";
        double salaireBrut = 0, primes = 0, cotisations = 0, impots = 0;
        String moisPaie = "0", anneePaie = "0";
        boolean found = false; // Pour vérifier si une paie existe

        try (BufferedReader br = new BufferedReader(new FileReader(path_paie))) {
            String ligne;
            while ((ligne = br.readLine()) != null) {
                String[] colonne = ligne.split(";");
                if (colonne.length < 14) continue; // Ignore les lignes incomplètes

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
            return null; // Retourne `null` pour éviter une erreur lors du calcul du salaire net
        }

        double salaireNet = salaireBrut + primes - cotisations - impots;
        double mois = safeParseDouble(moisPaie);
        double annee = safeParseDouble(anneePaie);

        return new double[]{salaireBrut, primes, cotisations, impots, salaireNet, mois, annee};
    }


    // ---------------------- GÉNÉRER FICHE DE PAIE -----------------------//
    /**
     * Génère un PDF de la fiche de paie pour l'utilisateur sélectionné.
     * Le PDF contient le titre, le mois/année, le nom de l'utilisateur et un tableau détaillant le salaire.
     *
     * @param parent       La fenêtre parent pour l'affichage des messages.
     * @param selectedUser Le nom complet de l'utilisateur (prenom + " " + nom)
     */
    public static void genererFichePaie(JFrame parent, String selectedUser) {
        if (selectedUser == null || selectedUser.isEmpty()) {
            JOptionPane.showMessageDialog(parent, "Veuillez sélectionner un utilisateur.", "Erreur", JOptionPane.ERROR_MESSAGE);
            return;
        }

        double[] salaireDetails = calculer_salaire(parent, selectedUser);
        if (salaireDetails == null) {
            return; // Arrête la fonction si aucune paie n'est trouvée
        }

        double salaireBrut = salaireDetails[0];
        double primes = salaireDetails[1];
        double cotisations = salaireDetails[2];
        double impots = salaireDetails[3];
        double salaireNet = salaireDetails[4];
        int moisPaie = (int) salaireDetails[5];
        int anneePaie = (int) salaireDetails[6];

        String nomFichier = String.format("resources/fiches_paie/%s_%02d-%d_fiche_paie.pdf", selectedUser.replace(" ", "_"), moisPaie, anneePaie);

        try {
            PdfWriter writer = new PdfWriter(nomFichier);
            PdfDocument pdf = new PdfDocument(writer);
            Document document = new Document(pdf);

            // Couleurs personnalisées pour le thème (similaire à l'interface)
            DeviceRgb accentColor = new DeviceRgb(255, 204, 0);       // Couleur d'accent (jaune)
            DeviceRgb headerBgColor = new DeviceRgb(43, 60, 70);        // Couleur d'arrière-plan des entêtes (similaire à l'interface)
            DeviceRgb headerTextColor = new DeviceRgb(255, 255, 255);     // Texte en blanc pour les entêtes

            // Titre principal avec couleur d'accent
            document.add(new Paragraph("BULLETIN DE PAIE")
                    .setFontSize(18)
                    .setBold()
                    .setTextAlignment(TextAlignment.CENTER)
                    .setFontColor(accentColor));

            // Informations de paie
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

            // Création du tableau récapitulatif avec 2 colonnes (proportions 4:3)
            Table table = new Table(UnitValue.createPercentArray(new float[]{4, 3}))
                    .useAllAvailableWidth()
                    .setBorder(new SolidBorder(ColorConstants.GRAY, 1));

            // Entêtes du tableau avec fond headerBgColor et texte en blanc
            table.addCell(createHeaderCell("Description", headerBgColor, headerTextColor));
            table.addCell(createHeaderCell("Montant (€)", headerBgColor, headerTextColor));

            // Détail des montants
            table.addCell(createBodyCell("Salaire Brut"));
            table.addCell(createBodyCell(String.format("%.2f", salaireBrut)));

            table.addCell(createBodyCell("Primes"));
            table.addCell(createBodyCell(String.format("%.2f", primes)));

            table.addCell(createBodyCell("Cotisations Sociales"));
            table.addCell(createBodyCell(String.format("-%.2f", cotisations)));

            table.addCell(createBodyCell("Impôts"));
            table.addCell(createBodyCell(String.format("-%.2f", impots)));

            // Total
            table.addCell(createFooterCell("Salaire Net à Payer"));
            table.addCell(createFooterCell(String.format("%.2f", salaireNet)));

            document.add(table);
            document.close();

            JOptionPane.showMessageDialog(parent, "Fiche de paie générée avec succès : " + nomFichier, "Succès", JOptionPane.INFORMATION_MESSAGE);

        } catch (IOException e) {
            JOptionPane.showMessageDialog(parent, "Erreur lors de la génération du PDF.", "Erreur", JOptionPane.ERROR_MESSAGE);
        }
    }
    /**
     * Crée une cellule d'en-tête avec un texte centré, un fond coloré et une bordure.
     *
     * @param text      Le texte à afficher dans la cellule.
     * @param bgColor   La couleur de fond de la cellule.
     * @param textColor La couleur du texte.
     * @return Une cellule formatée pour l'en-tête du tableau.
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
     * Crée une cellule pour le corps du tableau avec un texte aligné à droite.
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
     * Crée une cellule pour le pied de page du tableau avec un texte en gras et aligné à droite.
     *
     * @param text Le texte à afficher dans la cellule.
     * @return Une cellule formatée pour le pied de page du tableau.
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
     * Convertit une chaîne de caractères en double de manière sécurisée.
     * Si la conversion échoue ou si la valeur est null/vide, retourne 0.0.
     * Remplace les virgules par des points pour éviter les erreurs de formatage.
     *
     * @param value La chaîne à convertir en double.
     * @return La valeur convertie en double, ou 0.0 en cas d'erreur.
     */
    private static double safeParseDouble(String value) {
        try {
            return value == null || value.isEmpty() ? 0.0 : Double.parseDouble(value.replace(",", "."));
        } catch (NumberFormatException e) {
            System.err.println("Erreur de conversion en double : " + value);
            return 0.0; // Retourne 0 pour éviter les crashs
        }
    }


}
