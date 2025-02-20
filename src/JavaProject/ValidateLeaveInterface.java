package JavaProject;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

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

        // Charger les données du fichier "conge.csv" via Manager
        Manager.loadCongeData(tableModel, this);

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
                tableModel.setValueAt("Validé", selectedRow, 6);
                Manager.updateCongeStatus(idConge, "Validé", this);
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
                tableModel.setValueAt("Rejeté", selectedRow, 6);
                Manager.updateCongeStatus(idConge, "Rejeté", this);
                JOptionPane.showMessageDialog(this, "Congé rejeté.");
            } else {
                JOptionPane.showMessageDialog(this, "Veuillez sélectionner une demande.");
            }
        });

        // Action de retour
        backButton.addActionListener(e -> {
            if (parent != null) {
                parent.setVisible(true);
            }
            dispose();
        });

        buttonPanel.add(validateButton);
        buttonPanel.add(rejectButton);
        buttonPanel.add(backButton);

        mainPanel.add(buttonPanel, BorderLayout.SOUTH);
        add(mainPanel);

        setLocationRelativeTo(null);
        setVisible(true);
    }

    // Méthode pour créer des boutons arrondis
    private JButton createRoundedButton(String text) {
        JButton button = new JButton(text);
        button.setBackground(new Color(255, 204, 0));
        button.setForeground(Color.BLACK);
        button.setFocusPainted(false);
        // Supposons que RoundBorder est une classe personnalisée pour une bordure arrondie
        button.setBorder(new RoundBorder(15));
        return button;
    }
}
