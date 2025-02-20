package JavaProject;

import javax.swing.*;
import javax.swing.border.LineBorder;
import java.awt.*;

/**
 * Classe InterfaceSaisiePaie.
 * Gère l'interface de saisie de la paie dans le système.
 */

/**
 * Classe InterfaceSaisiePaie.
 * Cette classe gère les fonctionnalités liées à interfacesaisiepaie dans le système.
 *
 * @author Groupe 7
 * @version 1.0
 */
public class InterfaceSaisiePaie extends JFrame {
    private JTextField heuresTravailField, primesField, cotisationsField, impotsField;
    private JLabel userInfoLabel, salaireBrutLabel, salaireNetLabel;
    private String selectedUser;
    private Utilisateur utilisateur;
    private double tauxHoraire;
    private JFrame parent; // Référence vers la fenêtre précédente

    /**
     * Constructeur de l'interface de saisie de la paie.
     * @param parent La fenêtre précédente.
     * @param selectedUser L'utilisateur sélectionné pour la saisie de la paie.
     */
    public InterfaceSaisiePaie(JFrame parent, String selectedUser) {
        this.parent = parent;
        this.selectedUser = selectedUser;
        // Récupération des informations de l'utilisateur et du taux horaire via Manager
        this.utilisateur = Manager.getUtilisateurInfo(selectedUser);
        this.tauxHoraire = Manager.getTauxHoraire(utilisateur.poste);

        setTitle("Saisie de la paie pour " + selectedUser);
        setSize(800, 500);
        setLayout(new BorderLayout());
        getContentPane().setBackground(new Color(29, 46, 56));
        setResizable(false);

        JPanel formPanel = new JPanel();
        formPanel.setLayout(new GridLayout(7, 2, 10, 10));
        formPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        formPanel.setBackground(new Color(43, 60, 70));

        userInfoLabel = new JLabel("Employé : " + selectedUser 
                + " | Poste : " + utilisateur.poste 
                + " | Taux horaire : " + tauxHoraire + " €/h");
        userInfoLabel.setForeground(Color.WHITE);
        userInfoLabel.setHorizontalAlignment(SwingConstants.CENTER);
        add(userInfoLabel, BorderLayout.NORTH);

        JLabel heuresTravailLabel = createStyledLabel("Heures travaillées :");
        heuresTravailField = createStyledTextField();
        formPanel.add(heuresTravailLabel);
        formPanel.add(heuresTravailField);

        JLabel primesLabel = createStyledLabel("Primes (€) :");
        primesField = createStyledTextField();
        formPanel.add(primesLabel);
        formPanel.add(primesField);

        JLabel cotisationsLabel = createStyledLabel("Cotisations (€) :");
        cotisationsField = createStyledTextField();
        formPanel.add(cotisationsLabel);
        formPanel.add(cotisationsField);

        JLabel impotsLabel = createStyledLabel("Impôts (€) :");
        impotsField = createStyledTextField();
        formPanel.add(impotsLabel);
        formPanel.add(impotsField);

        salaireBrutLabel = createStyledLabel("Salaire Brut : -");
        formPanel.add(salaireBrutLabel);
        salaireNetLabel = createStyledLabel("Salaire Net : -");
        formPanel.add(salaireNetLabel);

        // Panel contenant les boutons
        JPanel buttonPanel = new JPanel(new GridLayout(2, 1, 10, 10));
        buttonPanel.setBackground(new Color(43, 60, 70));

        // Boutons en haut : "Enregistrer la paie" et "Générer fiche de paie"
        JPanel topButtons = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
        topButtons.setBackground(new Color(43, 60, 70));

        JButton saveButton = createRoundedButton("Enregistrer la paie");
        saveButton.addActionListener(e -> {
            try {
                int heuresTravail = Integer.parseInt(heuresTravailField.getText().trim());
                double primes = Double.parseDouble(primesField.getText().trim().replace(",", "."));
                double cotisations = Double.parseDouble(cotisationsField.getText().trim().replace(",", "."));
                double impots = Double.parseDouble(impotsField.getText().trim().replace(",", "."));

                double salaireBrut = tauxHoraire * heuresTravail;
                double salaireNet = salaireBrut + primes - cotisations - impots;

                salaireBrutLabel.setText("Salaire Brut : " + String.format("%.2f €", salaireBrut));
                salaireNetLabel.setText("Salaire Net : " + String.format("%.2f €", salaireNet));

                // Appel à la méthode de Manager pour enregistrer la paie
                Manager.enregistrerPaie(heuresTravail, primes, cotisations, impots, utilisateur, tauxHoraire, this);

                // Affichage du message de succès
                JOptionPane.showMessageDialog(this, "La paie a été enregistrée avec succès.", "Succès", JOptionPane.INFORMATION_MESSAGE);
                
                // Réinitialisation des champs du formulaire
                heuresTravailField.setText("");
                primesField.setText("");
                cotisationsField.setText("");
                impotsField.setText("");
                salaireBrutLabel.setText("Salaire Brut : -");
                salaireNetLabel.setText("Salaire Net : -");
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Veuillez entrer des valeurs valides.", "Erreur", JOptionPane.ERROR_MESSAGE);
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Erreur lors de l'enregistrement de la paie : " + ex.getMessage(), "Erreur", JOptionPane.ERROR_MESSAGE);
            }
        });
        topButtons.add(saveButton);

        JButton generateButton = createRoundedButton("Générer fiche de paie");
        generateButton.addActionListener(e -> Manager.genererFichePaie(this, selectedUser));
        topButtons.add(generateButton);

        // Bouton "Retour" en bas
        JPanel bottomButton = new JPanel(new FlowLayout(FlowLayout.CENTER));
        bottomButton.setBackground(new Color(43, 60, 70));

        JButton backButton = createRoundedButton("Retour");
        backButton.addActionListener(e -> {
            if (parent != null) {
                parent.setVisible(true);
            }
            dispose();
        });
        bottomButton.add(backButton);

        buttonPanel.add(topButtons);
        buttonPanel.add(bottomButton);

        add(formPanel, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);

        revalidate();
        repaint();

        setLocationRelativeTo(null);
        setVisible(true);
        if (parent != null) parent.setVisible(false);
    }

    // Méthode utilitaire pour créer un label stylisé
    private JLabel createStyledLabel(String text) {
        JLabel label = new JLabel(text);
        label.setFont(new Font("Arial", Font.BOLD, 14));
        label.setForeground(Color.WHITE);
        return label;
    }

    // Méthode utilitaire pour créer un champ de texte stylisé
    private JTextField createStyledTextField() {
        JTextField textField = new JTextField();
        textField.setBackground(Color.LIGHT_GRAY);
        textField.setForeground(Color.BLACK);
        textField.setBorder(new LineBorder(Color.GRAY, 1, true));
        return textField;
    }

    // Méthode utilitaire pour créer un bouton avec un style particulier
    private JButton createRoundedButton(String text) {
        JButton button = new JButton(text);
        button.setBackground(new Color(255, 204, 0));
        button.setForeground(Color.BLACK);
        return button;
    }
}
