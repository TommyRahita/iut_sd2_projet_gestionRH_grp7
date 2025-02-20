/**
 * Classe InterfaceDemandeConge.
 * Cette classe est responsable de la gestion de demandeconge.
 *
 * @author Équipe Projet Gestion RH
 * @version 1.0
 */
package JavaProject;

import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class InterfaceDemandeConge extends JFrame {
    private JFrame parent;
    private Utilisateur utilisateur;
    
    public InterfaceDemandeConge(JFrame parent, Utilisateur utilisateur) {
        this.parent = parent;
        this.utilisateur = utilisateur;

        if (this.parent != null) {
            this.parent.setVisible(false);
        }

        try {
            ImageIcon icon = new ImageIcon("resources\\icon.png");
            setIconImage(icon.getImage());
        } catch (Exception e) {
            System.out.println("Erreur lors du chargement de l'icône: " + e.getMessage());
        }

        setTitle("Demande de congés");
        setSize(500, 300);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout());
        setResizable(false);

        JPanel mainPanel = new JPanel(new GridBagLayout());
        mainPanel.setBackground(new Color(29, 46, 56));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.WEST;

        Dimension labelSize = new Dimension(180, 20);

        JLabel startDateLabel = new JLabel("Date de début (JJ/MM/AAAA) :");
        startDateLabel.setPreferredSize(labelSize);
        startDateLabel.setForeground(Color.WHITE);
        mainPanel.add(startDateLabel, gbc);

        gbc.gridx = 1;
        JTextField startDateField = new JTextField(15);
        mainPanel.add(startDateField, gbc);

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

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 10));
        buttonPanel.setBackground(new Color(29, 46, 56));

        JButton requestButton = createRoundedButton("Demander");
        JButton backButton = createRoundedButton("Retour");
        backButton.addActionListener(e -> {
            if (parent != null) {
                parent.setVisible(true);
            }
            dispose();
        });

        requestButton.addActionListener(e -> {
            String startDate = startDateField.getText();
            String endDate = endDateField.getText();

            if (startDate.isEmpty() || endDate.isEmpty()) {
                JOptionPane.showMessageDialog(this, "C'est pas cool ! Veuillez remplir tous les champs de date.", "Erreur", JOptionPane.ERROR_MESSAGE);
                return;
            }

            if (!isValidDate(startDate) || !isValidDate(endDate)) {
                JOptionPane.showMessageDialog(this, "Les dates doivent être valides et au format JJ/MM/AAAA.", "Erreur", JOptionPane.ERROR_MESSAGE);
                return;
            }

            LocalDate today = LocalDate.now();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
            LocalDate dateDebut = LocalDate.parse(startDate, formatter);
            LocalDate dateFin = LocalDate.parse(endDate, formatter);

            if (!dateDebut.isAfter(today) || !dateFin.isAfter(today)) {
                JOptionPane.showMessageDialog(this, "Les dates doivent être postérieures à la date d'aujourd'hui.", "Erreur", JOptionPane.ERROR_MESSAGE);
                return;
            }

            if (dateFin.isBefore(dateDebut)) {
                JOptionPane.showMessageDialog(this, "La date de fin ne peut pas être antérieure à la date de début.", "Erreur", JOptionPane.ERROR_MESSAGE);
                return;
            }

            int response = JOptionPane.showConfirmDialog(
                this,
                "Confirmez-vous votre demande ?\n\n" +
                        "Date de début : " + startDate + "\n" +
                        "Date de fin : " + endDate,
                "Confirmation",
                JOptionPane.YES_NO_OPTION
            );
            
            if (response == JOptionPane.YES_OPTION) {
                try {
                    // Appel de la méthode enregistrerConge de l'Employe (après cast)
                    ((Employe) utilisateur).enregistrerConge(this, startDate, endDate);
                    JOptionPane.showMessageDialog(this, "Demande enregistrée avec succès !");
                    if (parent != null) {
                        parent.setVisible(true);
                    }
                    dispose();
                } catch (IOException ex) {
                    JOptionPane.showMessageDialog(this, "Erreur lors de l'enregistrement de la demande: " + ex.getMessage(), "Erreur", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        buttonPanel.add(requestButton);
        buttonPanel.add(backButton);

        add(buttonPanel, BorderLayout.SOUTH);

        setLocationRelativeTo(null);
        setVisible(true);
    }

    private JButton createRoundedButton(String text) {
        JButton button = new JButton(text);
        button.setBackground(new Color(255, 204, 0));
        button.setForeground(Color.BLACK);
        button.setFocusPainted(false);
        button.setBorder(new BordureArrondie(15));
        return button;
    }

    private boolean isValidDate(String dateStr) {
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        sdf.setLenient(false);
        try {
            sdf.parse(dateStr);
            return true;
        } catch (ParseException e) {
            return false;
        }
    }
}
