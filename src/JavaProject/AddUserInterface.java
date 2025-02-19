package JavaProject;

import java.awt.*;
import java.io.*;
import javax.swing.*;
import javax.swing.border.LineBorder;

/**
 * Classe représentant l'interface graphique pour ajouter un nouvel utilisateur.
 */
class AddUserInterface extends JFrame {
    
    /**
     * Constructeur de la classe AddUserInterface.
     * 
     * @param parent La fenêtre précédente.
     */
    public AddUserInterface(JFrame parent) {
        setTitle("Ajouter un utilisateur");
        setSize(800, 500);
        setLayout(new BorderLayout());
        getContentPane().setBackground(new Color(29, 46, 56));
        setResizable(false);

        try {
            // Charger l'image depuis le dossier 'resources' dans le projet
            ImageIcon icon = new ImageIcon("resources\\icon.png");
            setIconImage(icon.getImage()); // Définir l'icône de la fenêtre
        } catch (Exception e) {
            System.out.println("Erreur lors du chargement de l'icône: " + e.getMessage());
        }

        // Création du panneau principal contenant le formulaire
        JPanel formPanel = new JPanel(new GridLayout(7, 2, 10, 10));
        formPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        formPanel.setBackground(new Color(43, 60, 70));

        // Ajout des champs pour saisir les informations utilisateur
        JLabel nameLabel = createStyledLabel("Nom :");
        JTextField nameField = createStyledTextField();
        formPanel.add(nameLabel);
        formPanel.add(nameField);

        JLabel firstNameLabel = createStyledLabel("Prénom :");
        JTextField firstNameField = createStyledTextField();
        formPanel.add(firstNameLabel);
        formPanel.add(firstNameField);

        JLabel jobLabel = createStyledLabel("Poste :");
        JTextField jobField = createStyledTextField();
        formPanel.add(jobLabel);
        formPanel.add(jobField);

        JLabel leaveDaysLabel = createStyledLabel("Jours de congés restants :");
        JTextField leaveDaysField = createStyledTextField();
        formPanel.add(leaveDaysLabel);
        formPanel.add(leaveDaysField);

        JLabel passwordLabel = createStyledLabel("Mot de passe :");
        JPasswordField passwordField = new JPasswordField();
        stylePasswordField(passwordField);
        formPanel.add(passwordLabel);
        formPanel.add(passwordField);

        JLabel statusLabel = createStyledLabel("Statut :");
        JComboBox<String> statusComboBox = new JComboBox<>(new String[]{"Manager", "Employé"});
        formPanel.add(statusLabel);
        formPanel.add(statusComboBox);

        // Création du panneau contenant les boutons
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
        buttonPanel.setBackground(new Color(43, 60, 70));

        // Bouton pour ajouter l'utilisateur
        JButton addButton = createRoundedButton("Ajouter");
        addButton.addActionListener(e -> {
            // Récupération des valeurs des champs
            String name = nameField.getText();
            String firstName = firstNameField.getText();
            String job = jobField.getText();
            String leaveDaysText = leaveDaysField.getText();
            String password = new String(passwordField.getPassword());
            String status = (String) statusComboBox.getSelectedItem();

            try {
                // Vérification que tous les champs sont remplis
                if (name.isEmpty() || firstName.isEmpty() || job.isEmpty() || leaveDaysText.isEmpty() || password.isEmpty() || status.isEmpty()) {
                    JOptionPane.showMessageDialog(this, "Veuillez remplir tous les champs.", "Erreur", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                int leaveDays = Integer.parseInt(leaveDaysText);

                // Ajout de l'utilisateur via la classe Manager
                Manager.ajouter_utilisateur(name, firstName, job, leaveDays, password, status);
                JOptionPane.showMessageDialog(this, "Utilisateur ajouté avec succès !");

                // Réinitialisation des champs
                nameField.setText("");
                firstNameField.setText("");
                jobField.setText("");
                leaveDaysField.setText("");
                passwordField.setText("");
                statusComboBox.setSelectedIndex(0);
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Le champ 'Jours de congés restants' doit être un nombre.", "Erreur", JOptionPane.ERROR_MESSAGE);
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Erreur lors de l'ajout de l'utilisateur : " + ex.getMessage(), "Erreur", JOptionPane.ERROR_MESSAGE);
            }
        });
        buttonPanel.add(addButton);

        // Bouton de retour à la fenêtre précédente
        JButton backButton = createRoundedButton("Retour");
        backButton.addActionListener(e -> {
            parent.setVisible(true);
            dispose();
        });
        buttonPanel.add(backButton);

        add(formPanel, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);
        setLocationRelativeTo(null);
        setVisible(true);
        parent.setVisible(false);
    }

    /**
     * Crée un label stylisé avec une police et une couleur définies.
     * 
     * @param text Le texte du label.
     * @return Un objet JLabel stylisé.
     */
    private JLabel createStyledLabel(String text) {
        JLabel label = new JLabel(text);
        label.setFont(new Font("Arial", Font.BOLD, 14));
        label.setForeground(Color.WHITE);
        return label;
    }

    /**
     * Crée un champ de texte stylisé avec une couleur de fond et une bordure.
     * 
     * @return Un objet JTextField stylisé.
     */
    private JTextField createStyledTextField() {
        JTextField textField = new JTextField();
        textField.setBackground(Color.LIGHT_GRAY);
        textField.setForeground(Color.BLACK);
        textField.setBorder(new LineBorder(Color.GRAY, 1, true));
        return textField;
    }

    /**
     * Applique un style spécifique à un champ de mot de passe.
     * 
     * @param passwordField Le champ de mot de passe à styliser.
     */
    private void stylePasswordField(JPasswordField passwordField) {
        passwordField.setBackground(Color.LIGHT_GRAY);
        passwordField.setForeground(Color.BLACK);
        passwordField.setBorder(new LineBorder(Color.GRAY, 1, true));
    }

    /**
     * Crée un bouton arrondi avec une couleur de fond personnalisée.
     * 
     * @param text Le texte affiché sur le bouton.
     * @return Un objet JButton stylisé avec des bordures arrondies.
     */
    private JButton createRoundedButton(String text) {
        JButton button = new JButton(text);
        button.setBackground(new Color(255, 204, 0));
        button.setForeground(Color.BLACK);
        button.setFocusPainted(false);
        button.setBorder(new RoundBorder(15));
        return button;
    }
}
