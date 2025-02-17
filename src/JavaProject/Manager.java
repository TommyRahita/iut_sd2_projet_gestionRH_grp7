package JavaProject;

import javax.swing.*;
import java.io.*;
import com.itextpdf.kernel.pdf.*;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Paragraph;

public class Manager extends Utilisateur {

	// ---------------------- CONSTRUCTEURS -----------------------//
		// Constructeur 1
		public Manager(int id, String nom, String prenom, String poste,	int jours_conge_restants, String mdp, String statut) {
			 super(id, nom, prenom, poste, jours_conge_restants, mdp, statut);
		}
		
		// Constructeur vide
		public Manager() {
			super();
		}
		
    // ---------------------- CALCULER SALAIRE ----------------------//
    public static double calculer_salaire(String selectedUser) {
        String path_paie = "resources/paie.csv";
        double salaireBrut = 0, primes = 0, cotisations = 0, impots = 0;

        try (BufferedReader br = new BufferedReader(new FileReader(path_paie))) {
            String ligne;
            while ((ligne = br.readLine()) != null) {
                String[] colonne = ligne.split(";");
                if ((colonne[5] + " " + colonne[4]).equalsIgnoreCase(selectedUser)) {
                    salaireBrut += Double.parseDouble(colonne[10].replace(",", "."));
                    primes += Double.parseDouble(colonne[11].replace(",", "."));
                    cotisations += Double.parseDouble(colonne[12].replace(",", "."));
                    impots += Double.parseDouble(colonne[13].replace(",", "."));
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return salaireBrut + primes - cotisations - impots;
    }

    // ---------------------- GÉNÉRER FICHE DE PAIE ----------------------//
    public static void genererFichePaie(JFrame parent, String selectedUser) {
        if (selectedUser == null || selectedUser.isEmpty()) {
            JOptionPane.showMessageDialog(parent, "Veuillez sélectionner un utilisateur.", "Erreur", JOptionPane.ERROR_MESSAGE);
            return;
        }

        double salaireNet = calculer_salaire(selectedUser);
        String nomFichier = "resources/fiches_paie/" + selectedUser.replace(" ", "_") + "_fiche_paie.pdf";

        try {
            PdfWriter writer = new PdfWriter(nomFichier);
            PdfDocument pdf = new PdfDocument(writer);
            Document document = new Document(pdf);

            document.add(new Paragraph("BULLETIN DE PAIE").setBold().setFontSize(18));
            document.add(new Paragraph("Employé : " + selectedUser));
            document.add(new Paragraph("Salaire Net : " + salaireNet + " €"));
            document.close();

            JOptionPane.showMessageDialog(parent, "Fiche de paie générée avec succès.", "Succès", JOptionPane.INFORMATION_MESSAGE);
        } catch (IOException e) {
            JOptionPane.showMessageDialog(parent, "Erreur lors de la génération du PDF.", "Erreur", JOptionPane.ERROR_MESSAGE);
        }
    }
}
