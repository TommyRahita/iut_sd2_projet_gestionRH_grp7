package JavaProject;

import javax.swing.*;
import java.awt.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;

public class CongeRequest extends JFrame {
    private JFrame parent; // Référence à la fenêtre parent

    public CongeRequest(JFrame parent) {
        this.parent = parent; // Assigner la fenêtre parent
        if (this.parent != null) {
            this.parent.setVisible(false); // Cacher la fenêtre parent
        }

        setTitle("Demande de congés");
        setSize(500, 300); // Taille ajustée après suppression du champ motif
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout());
        setResizable(false);

        // Panneau central
        JPanel mainPanel = new JPanel(new GridBagLayout());
        mainPanel.setBackground(new Color(29, 46, 56));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.WEST;

        // Création d'une taille uniforme pour les labels
        Dimension labelSize = new Dimension(180, 20);

        // Label et champ pour la date de début
        JLabel startDateLabel = new JLabel("Date de début (JJ/MM/AAAA) :");
        startDateLabel.setPreferredSize(labelSize);
        startDateLabel.setForeground(Color.WHITE);
        mainPanel.add(startDateLabel, gbc);

        gbc.gridx = 1;
        JTextField startDateField = new JTextField(15);
        mainPanel.add(startDateField, gbc);

        // Label et champ pour la date de fin
        gbc.gridx = 0;
        gbc.gridy++;
        JLabel endDateLabel = new JLabel("Date de fin (JJ/MM/AAAA) :");
        endDateLabel.setPreferredSize(labelSize);
        endDateLabel.setForeground(Color.WHITE);
        mainPanel.add(endDateLabel, gbc);

        gbc.gridx = 1;
        JTextField endDateField = new JTextField(15);
        mainPanel.add(endDateField, gbc);

        add(mainPanel, BorderLayout.CENTER);

        // Panneau inférieur avec boutons
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 10));
        buttonPanel.setBackground(new Color(29, 46, 56));

        JButton requestButton = createRoundedButton("Demander");
        JButton backButton = createRoundedButton("Retour");
        backButton.addActionListener(e -> {
            if (parent != null) {
                parent.setVisible(true); // Rendre la fenêtre parent visible
            }
            dispose(); // Fermer la fenêtre actuelle
        });

        requestButton.addActionListener(e -> {
            String startDate = startDateField.getText();
            String endDate = endDateField.getText();

            // Vérification des champs vides
            if (startDate.isEmpty() || endDate.isEmpty()) {
                JOptionPane.showMessageDialog(this, "C'est pas cool ! Veuillez remplir tous les champs de date.", "Erreur", JOptionPane.ERROR_MESSAGE);
                return;
            }

            // Vérification du format de la date
            if (!isValidDate(startDate) || !isValidDate(endDate)) {
                JOptionPane.showMessageDialog(this, "Les dates doivent être valide et au format JJ/MM/AAAA.", "Erreur", JOptionPane.ERROR_MESSAGE);
                return;
            }

            // Vérification et confirmation
            int response = JOptionPane.showConfirmDialog(
                this,
                "Confirmez-vous votre demande ?\n\n" +
                        "Date de début : " + startDate + "\n" +
                        "Date de fin : " + endDate,
                "Confirmation",
                JOptionPane.YES_NO_OPTION
            );

            if (response == JOptionPane.YES_OPTION) {
                JOptionPane.showMessageDialog(this, "Demande envoyée avec succès !");
                if (parent != null) {
                    parent.setVisible(true); // Retour à la fenêtre parent
                }
                dispose();
            }
        });

        buttonPanel.add(requestButton);
        buttonPanel.add(backButton);

        add(buttonPanel, BorderLayout.SOUTH);

        setLocationRelativeTo(null);
        setVisible(true);
    }

    // Méthode pour créer des boutons arrondis avec une taille uniforme
    private JButton createRoundedButton(String text) {
        JButton button = new JButton(text);
        button.setBackground(new Color(255, 204, 0)); // Couleur jaune comme dans EmployeeInterface
        button.setForeground(Color.BLACK);
        button.setFocusPainted(false);
        button.setBorder(new RoundBorder(15)); // Bordure arrondie avec un rayon de 15 pixels
        return button;
    }

    // Méthode pour vérifier si une date est valide (format JJ/MM/AAAA)
    private boolean isValidDate(String dateStr) {
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        sdf.setLenient(false); // Désactive le mode tolérant pour les dates
        try {
            sdf.parse(dateStr); // Essaye de parser la date
            return true;
        } catch (ParseException e) {
            return false;
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new CongeRequest(null)); // Test sans parent
    }
}
