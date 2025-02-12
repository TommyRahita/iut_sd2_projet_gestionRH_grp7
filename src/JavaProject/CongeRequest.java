package JavaProject;

import javax.swing.*;
import java.awt.*;

public class CongeRequest extends JFrame {
    public CongeRequest() {
        setTitle("Demande de congés");
        setSize(400, 300);
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

        // Label et champ pour la date de début
        JLabel startDateLabel = new JLabel("Date de début (JJ/MM/AAAA) :");
        startDateLabel.setForeground(Color.WHITE);
        mainPanel.add(startDateLabel, gbc);

        gbc.gridx = 1;
        JTextField startDateField = new JTextField(10);
        mainPanel.add(startDateField, gbc);

        // Label et champ pour la date de fin
        gbc.gridx = 0;
        gbc.gridy++;
        JLabel endDateLabel = new JLabel("Date de fin (JJ/MM/AAAA) :");
        endDateLabel.setForeground(Color.WHITE);
        mainPanel.add(endDateLabel, gbc);

        gbc.gridx = 1;
        JTextField endDateField = new JTextField(10);
        mainPanel.add(endDateField, gbc);

        add(mainPanel, BorderLayout.CENTER);

        // Panneau inférieur avec boutons
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 10));
        buttonPanel.setBackground(new Color(29, 46, 56));

        JButton requestButton = new JButton("Demander");
        JButton backButton = new JButton("Retour");
        backButton.addActionListener(e -> dispose()); // Ferme cette fenêtre

        buttonPanel.add(requestButton);
        buttonPanel.add(backButton);

        add(buttonPanel, BorderLayout.SOUTH);

        setLocationRelativeTo(null);
        setVisible(true);
    }
}
