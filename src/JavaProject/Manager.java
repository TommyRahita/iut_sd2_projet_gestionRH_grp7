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

public class Manager extends Utilisateur {

    // ---------------------- CONSTRUCTEURS -----------------------//
    public Manager(int id, String nom, String prenom, String poste, int jours_conge_restants, String mdp, String statut) {
        super(id, nom, prenom, poste, jours_conge_restants, mdp, statut);
    }

    public Manager() {
        super();
    }

    // ---------------------- CALCULER SALAIRE -----------------------//
    /**
     * Lit le fichier paie.csv et calcule les montants cumulés pour l'utilisateur sélectionné.
     * On suppose que le fichier paie.csv a la structure suivante :
     * idPaie;moisPaie;anneePaie;idUtilisateur;nom;prenom;poste;taux_horaire;nb_jour_travaille;nb_conge;salaire;primes;cotisations;impots
     *
     * @param selectedUser Le nom complet de l'utilisateur (prenom + " " + nom)
     * @return Un tableau de double contenant :
     *         [0] salaireBrut, [1] primes, [2] cotisations, [3] impots, [4] salaireNet,
     *         [5] mois (en valeur numérique), [6] année (en valeur numérique)
     */
    public static double[] calculer_salaire(String selectedUser) {
        String path_paie = "resources/paie.csv";
        double salaireBrut = 0, primes = 0, cotisations = 0, impots = 0;
        String moisPaie = "", anneePaie = "";
        
        try (BufferedReader br = new BufferedReader(new FileReader(path_paie))) {
            String ligne;
            // Lecture ligne par ligne
            while ((ligne = br.readLine()) != null) {
                String[] colonne = ligne.split(";");
                // On vérifie que le nom complet (colonne[5] + " " + colonne[4]) correspond à l'utilisateur sélectionné
                System.out.println(colonne[5] + " " + colonne[4]);
                if ((colonne[5] + " " + colonne[4]).equalsIgnoreCase(selectedUser)) {
                    // Extraction du mois et de l'année (supposés être aux index 1 et 2)
                    moisPaie = colonne[1];
                    anneePaie = colonne[2];
                    System.out.println("Mois de paie : " + moisPaie);
                    System.out.println("Année de paie : " + anneePaie);
                    
                    salaireBrut += Double.parseDouble(colonne[10].replace(",", "."));
                    primes += Double.parseDouble(colonne[11].replace(",", "."));
                    cotisations += Double.parseDouble(colonne[12].replace(",", "."));
                    impots += Double.parseDouble(colonne[13].replace(",", "."));
                    System.out.println("Hello");
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        double salaireNet = salaireBrut + primes - cotisations - impots;
        double mois = Double.parseDouble(moisPaie);
        double annee = Double.parseDouble(anneePaie);
        System.out.println("Salaire net : " + salaireNet);
        
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

        double[] salaireDetails = calculer_salaire(selectedUser);
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

    // ---------------------- Méthodes pour la mise en forme des cellules ----------------------//
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
}
