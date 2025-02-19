package JavaProject;

import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class CongeRequest extends JFrame {
    private JFrame parent;

    public CongeRequest(JFrame parent) {
        this.parent = parent;
        if (this.parent != null) {
            this.parent.setVisible(false);
        }

        try {
            // Charger l'image depuis le dossier 'resources' dans le projet
            ImageIcon icon = new ImageIcon("resources\\icon.png");
            setIconImage(icon.getImage()); // Définir l'icône de la fenêtre
        } catch (Exception e) {
            System.out.println("Erreur lors du chargement de l'icône: " + e.getMessage());
        }

        setTitle("Demande de congés");
        setSize(500, 300);
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
            String onrecupidici = "1"; // Déclaration ici, valeur 1

            // Vérification des champs vides
            if (startDate.isEmpty() || endDate.isEmpty()) {
                JOptionPane.showMessageDialog(this, "C'est pas cool ! Veuillez remplir tous les champs de date.", "Erreur", JOptionPane.ERROR_MESSAGE);
                return;
            }

            // Vérification du format de la date
            if (!isValidDate(startDate) || !isValidDate(endDate)) {
                JOptionPane.showMessageDialog(this, "Les dates doivent être valides et au format JJ/MM/AAAA.", "Erreur", JOptionPane.ERROR_MESSAGE);
                return;
            }

            // Vérification si la date de fin est antérieure à la date de début
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
            LocalDate dateDebut = LocalDate.parse(startDate, formatter);
            LocalDate dateFin = LocalDate.parse(endDate, formatter);

            if (dateFin.isBefore(dateDebut)) {
                JOptionPane.showMessageDialog(this, "La date de fin ne peut pas être antérieure à la date de début.", "Erreur", JOptionPane.ERROR_MESSAGE);
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
                try {
                    enregistrerConge(onrecupidici, startDate, endDate);
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

    private void enregistrerConge(String idConge, String dateDebutStr, String dateFinStr) throws IOException {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        LocalDate dateDebut = LocalDate.parse(dateDebutStr, formatter);
        LocalDate dateFin = LocalDate.parse(dateFinStr, formatter);

        String nom = "";
        String prenom = "";

        try (BufferedReader br = new BufferedReader(new FileReader("resources\\Utilisateurs.csv"))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(";");
                if (parts.length > 0 && parts[0].equals(idConge)) {
                    nom = parts[1];
                    prenom = parts[2];
                    break; // On arrête dès qu'on trouve la ligne correspondante
                }
            }
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "Erreur lors de la lecture du fichier Utilisateurs.csv : " + e.getMessage(), "Erreur", JOptionPane.ERROR_MESSAGE);
            return; // On arrête si la lecture du fichier échoue
        }

        long nbJoursOuvres = calculerJoursOuvres(dateDebut, dateFin);

        try (BufferedWriter writer = new BufferedWriter(new FileWriter("resources\\conge.csv", true))) {
            writer.write(String.format("%s;%s;%s;%s;%s;%d;%s\n",
                    idConge, nom, prenom, dateDebutStr, dateFinStr, nbJoursOuvres, "En attente"));
        }
    }

    private long calculerJoursOuvres(LocalDate dateDebut, LocalDate dateFin) {
        long nbJours = 0;

        // Si la date de début est égale à la date de fin, compter un jour ouvré
        if (dateDebut.isEqual(dateFin)) {
            return 1;
        }

        // Sinon, on continue de calculer les jours ouvrés entre les deux dates
        LocalDate dateCourante = dateDebut;
        while (dateCourante.isBefore(dateFin.plusDays(1))) {
            if (dateCourante.getDayOfWeek() != DayOfWeek.SATURDAY && dateCourante.getDayOfWeek() != DayOfWeek.SUNDAY) {
                nbJours++;
            }
            dateCourante = dateCourante.plusDays(1);
        }

        return nbJours;
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
