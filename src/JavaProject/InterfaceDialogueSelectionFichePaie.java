package JavaProject;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;

/**
 * Classe InterfaceDialogueSelectionFichePaie.
 * <p>
 * Cette classe affiche une boîte de dialogue permettant de choisir le mois et l'année d'une fiche de paie.
 * Elle utilise l'employé connecté pour récupérer et télécharger la fiche de paie correspondante.
 * </p>
 * 
 * @author Groupe 7
 * @version 1.0
 */
class InterfaceDialogueSelectionFichePaie extends JDialog {
    private JComboBox<Integer> monthComboBox, yearComboBox;
    private Employe employe;
    private JFrame parent;

    /**
     * Constructeur de la classe InterfaceDialogueSelectionFichePaie.
     * <p>
     * Crée une boîte de dialogue modale pour permettre à l'utilisateur de sélectionner le mois et l'année
     * d'une fiche de paie, et de lancer le téléchargement de la fiche correspondante.
     * </p>
     *
     * @param parent  La fenêtre parente à laquelle cette boîte de dialogue est rattachée.
     * @param employe L'employé connecté dont la fiche de paie doit être récupérée.
     */
    public InterfaceDialogueSelectionFichePaie(JFrame parent, Employe employe) {
        super(parent, "Sélectionner une fiche de paie", true);
        this.parent = parent;
        this.employe = employe;

        setSize(300, 200);
        setLayout(new GridLayout(3, 2, 10, 10));
        setLocationRelativeTo(parent);

        add(new JLabel("Mois :"));
        Integer[] months = {1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12};
        monthComboBox = new JComboBox<>(months);
        add(monthComboBox);

        add(new JLabel("Année :"));
        Integer[] years = {2023, 2024, 2025, 2026, 2027};
        yearComboBox = new JComboBox<>(years);
        add(yearComboBox);

        JButton confirmButton = new JButton("Télécharger");
        confirmButton.addActionListener(e -> downloadPaySlip());
        add(confirmButton);

        JButton cancelButton = new JButton("Annuler");
        cancelButton.addActionListener(e -> dispose());
        add(cancelButton);

        setVisible(true);
    }

    /**
     * Télécharge la fiche de paie pour le mois et l'année sélectionnés.
     * <p>
     * Cette méthode récupère les valeurs sélectionnées pour le mois et l'année,
     * puis appelle la méthode downloadPaySlip() de l'employé connecté afin de procéder au téléchargement.
     * </p>
     */
    private void downloadPaySlip() {
        int selectedMonth = (int) monthComboBox.getSelectedItem();
        int selectedYear = (int) yearComboBox.getSelectedItem();
        // Appelle la méthode downloadPaySlip() de la classe Employe
        employe.downloadPaySlip(this, selectedMonth, selectedYear);
    }
}
