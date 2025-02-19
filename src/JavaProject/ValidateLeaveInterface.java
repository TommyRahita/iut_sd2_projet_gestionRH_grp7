package JavaProject;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.io.*;
import java.util.*;
import java.awt.event.*;

public class ValidateLeaveInterface extends JFrame {
    private JFrame parent;

    public ValidateLeaveInterface(JFrame parent) {
        this.parent = parent;

        if (this.parent != null) {
            this.parent.setVisible(false);
        }

        setTitle("Validation des demandes de congés");
        setSize(600, 400);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout());
        setResizable(false);

        // Panneau principal
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(new Color(29, 46, 56));

        // Créer un modèle de table
        DefaultTableModel tableModel = new DefaultTableModel();
        tableModel.addColumn("ID");
        tableModel.addColumn("Nom");
        tableModel.addColumn("Prénom");
        tableModel.addColumn("Date de début");
        tableModel.addColumn("Date de fin");
        tableModel.addColumn("Jours ouvrés");
        tableModel.addColumn("Statut");
        tableModel.addColumn("Action");

        JTable table = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(table);
        mainPanel.add(scrollPane, BorderLayout.CENTER);

        // Charger les données depuis le fichier CSV
        loadCongeData(tableModel);

        // Panneau des boutons
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 10));
        buttonPanel.setBackground(new Color(29, 46, 56));

        JButton validateButton = createRoundedButton("Valider");
        JButton rejectButton = createRoundedButton("Rejeter");
        JButton backButton = createRoundedButton("Retour");

        // Action de validation
        validateButton.addActionListener(e -> {
            int selectedRow = table.getSelectedRow();
            if (selectedRow != -1) {
                String idConge = (String) tableModel.getValueAt(selectedRow, 0);
                String statut = "Validé";
                tableModel.setValueAt(statut, selectedRow, 6); // Mettre à jour le statut
                updateCongeStatus(idConge, statut);
                JOptionPane.showMessageDialog(this, "Congé validé avec succès.");
            } else {
                JOptionPane.showMessageDialog(this, "Veuillez sélectionner une demande.");
            }
        });

        // Action de rejet
        rejectButton.addActionListener(e -> {
            int selectedRow = table.getSelectedRow();
            if (selectedRow != -1) {
                String idConge = (String) tableModel.getValueAt(selectedRow, 0);
                String statut = "Rejeté";
                tableModel.setValueAt(statut, selectedRow, 6); // Mettre à jour le statut
                updateCongeStatus(idConge, statut);
                JOptionPane.showMessageDialog(this, "Congé rejeté.");
            } else {
                JOptionPane.showMessageDialog(this, "Veuillez sélectionner une demande.");
            }
        });

        // Action de retour
        backButton.addActionListener(e -> {
            if (parent != null) {
                parent.setVisible(true); // Rendre la fenêtre parent visible
            }
            dispose(); // Fermer la fenêtre actuelle
        });

        buttonPanel.add(validateButton);
        buttonPanel.add(rejectButton);
        buttonPanel.add(backButton);

        mainPanel.add(buttonPanel, BorderLayout.SOUTH);

        add(mainPanel);

        setLocationRelativeTo(null);
        setVisible(true);
    }

    // Charger les données du fichier conge.csv
 // Charger les données du fichier conge.csv
    private void loadCongeData(DefaultTableModel tableModel) {
        try (BufferedReader br = new BufferedReader(new FileReader("resources\\conge.csv"))) {
            String line;

            // Ignorer la première ligne (en-têtes)
            if ((line = br.readLine()) != null) {
                // La première ligne est l'en-tête, donc on ne fait rien ici
            }

            // Lire les lignes restantes
            while ((line = br.readLine()) != null) {
                String[] data = line.split(";");
                if (data.length >= 9 && !data[6].equals("Validé") && !data[6].equals("Rejeté")) {  
                    // Ajouter une nouvelle ligne dans le modèle de la table
                    Object[] row = {
                        data[0], // ID
                        data[1], // Nom
                        data[2], // Prénom
                        data[3], // Date de début
                        data[4], // Date de fin
                        data[5], // Jours ouvrés
                        data[6], // Statut
                        createActionButton(data[0]) // Bouton pour valider ou rejeter
                    };
                    tableModel.addRow(row);
                }
            }
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "Erreur lors de la lecture du fichier conge.csv: " + e.getMessage(), "Erreur", JOptionPane.ERROR_MESSAGE);
        }
    }

    // Méthode pour créer un bouton d'action (pour valider ou rejeter un congé)
    private JButton createActionButton(String idConge) {
        JButton button = new JButton("Action");
        button.addActionListener(e -> {
            String[] options = {"Valider", "Rejeter"};
            int choice = JOptionPane.showOptionDialog(
                    this,
                    "Choisir l'action à effectuer",
                    "Action",
                    JOptionPane.DEFAULT_OPTION,
                    JOptionPane.INFORMATION_MESSAGE,
                    null,
                    options,
                    options[0]
            );
            if (choice == 0) {
                updateCongeStatus(idConge, "Validé");
                JOptionPane.showMessageDialog(this, "Congé validé.");
            } else if (choice == 1) {
                updateCongeStatus(idConge, "Rejeté");
                JOptionPane.showMessageDialog(this, "Congé rejeté.");
            }
        });
        return button;
    }

    // Mise à jour du statut du congé dans le fichier conge.csv
    private void updateCongeStatus(String idConge, String statut) {
        try {
            File inputFile = new File("resources\\conge.csv");
            File tempFile = new File("resources\\conge_temp.csv");

            try (BufferedReader reader = new BufferedReader(new FileReader(inputFile));
                 BufferedWriter writer = new BufferedWriter(new FileWriter(tempFile))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    String[] data = line.split(";");
                    if (data[0].equals(idConge)) {
                        data[6] = statut; // Mettre à jour le statut
                    }
                    writer.write(String.join(";", data));
                    writer.newLine();
                }
            }

            // Remplacer le fichier original par le fichier temporaire
            if (inputFile.delete()) {
                if (!tempFile.renameTo(inputFile)) {
                    JOptionPane.showMessageDialog(this, "Erreur lors de la mise à jour du fichier des congés.", "Erreur", JOptionPane.ERROR_MESSAGE);
                }
            }
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "Erreur lors de la mise à jour du statut du congé : " + e.getMessage(), "Erreur", JOptionPane.ERROR_MESSAGE);
        }
    }

    // Méthode pour créer des boutons arrondis
    private JButton createRoundedButton(String text) {
        JButton button = new JButton(text);
        button.setBackground(new Color(255, 204, 0)); // Couleur jaune
        button.setForeground(Color.BLACK);
        button.setFocusPainted(false);
        button.setBorder(new RoundBorder(15)); // Bordure arrondie
        return button;
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new ValidateLeaveInterface(null)); // Test sans parent
    }
}
