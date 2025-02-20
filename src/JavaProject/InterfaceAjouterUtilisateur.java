package JavaProject;

import javax.swing.*;
import javax.swing.border.LineBorder;
import java.awt.*;

/**
 * Classe InterfaceAjouterUtilisateur.
 * <p>
 * Cette classe gère les fonctionnalités liées à l'interface d'ajout d'un utilisateur dans le système.
 * Elle permet de saisir les informations d'un nouvel utilisateur et de les transmettre pour leur enregistrement.
 * </p>
 *
 * @author Groupe 7
 * @version 1.0
 */
public class InterfaceAjouterUtilisateur extends JFrame {
    
    /**
     * Constructeur de l'interface d'ajout d'utilisateur.
     *
     * @param parent La fenêtre parente à masquer lors de l'affichage de cette interface.
     */
    public InterfaceAjouterUtilisateur(JFrame parent) {
        setTitle("Ajouter un utilisateur");
        setSize(800, 500);
        setLayout(new BorderLayout());
        getContentPane().setBackground(new Color(29, 46, 56));
        setResizable(false);

        try {
            // Charger l'image depuis le dossier 'resources'
            ImageIcon icon = new ImageIcon("resources\\icon.png");
            setIconImage(icon.getImage());
        } catch (Exception e) {
            System.out.println("Erreur lors du chargement de l'icône: " + e.getMessage());
        }

        // Panneau principal pour les champs de saisie
        JPanel formPanel = new JPanel(new GridLayout(7, 2, 10, 10));
        formPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        formPanel.setBackground(new Color(43, 60, 70));

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
        JComboBox<String> statusComboBox = new JComboBox<>(new String[]{"manager", "employe"});
        formPanel.add(statusLabel);
        formPanel.add(statusComboBox);

        // Panneau des boutons
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
        buttonPanel.setBackground(new Color(43, 60, 70));

        JButton addButton = createRoundedButton("Ajouter");
        addButton.addActionListener(e -> {
            String name = nameField.getText().trim();
            String firstName = firstNameField.getText().trim();
            String job = jobField.getText().trim();
            String leaveDaysText = leaveDaysField.getText().trim();
            String password = new String(passwordField.getPassword()).trim();
            String status = (String) statusComboBox.getSelectedItem();

            // Vérification des champs
            if (name.isEmpty() || firstName.isEmpty() || job.isEmpty() ||
                leaveDaysText.isEmpty() || password.isEmpty() || status.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Veuillez remplir tous les champs.", "Erreur", JOptionPane.ERROR_MESSAGE);
                return;
            }

            try {
                int leaveDays = Integer.parseInt(leaveDaysText);
                // Créer un nouvel utilisateur et l'ajouter via Manager
                Utilisateur newUser = new Utilisateur(name, firstName, job, leaveDays, password, status);
                Manager.ajouter_utilisateur(newUser.nom, newUser.prenom, newUser.poste, newUser.jours_conge_restants, newUser.mdp, newUser.statut);
                JOptionPane.showMessageDialog(this, "Utilisateur ajouté avec succès !");
                // Réinitialiser les champs
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
     * Crée un JLabel stylisé avec le texte spécifié.
     *
     * @param text Le texte à afficher.
     * @return Un JLabel stylisé.
     */
    private JLabel createStyledLabel(String text) {
        JLabel label = new JLabel(text);
        label.setFont(new Font("Arial", Font.BOLD, 14));
        label.setForeground(Color.WHITE);
        return label;
    }

    /**
     * Crée un JTextField stylisé.
     *
     * @return Un JTextField stylisé.
     */
    private JTextField createStyledTextField() {
        JTextField textField = new JTextField();
        textField.setBackground(Color.LIGHT_GRAY);
        textField.setForeground(Color.BLACK);
        textField.setBorder(new LineBorder(Color.GRAY, 1, true));
        return textField;
    }

    /**
     * Applique un style spécifique à un JPasswordField.
     *
     * @param passwordField Le JPasswordField auquel appliquer le style.
     */
    private void stylePasswordField(JPasswordField passwordField) {
        passwordField.setBackground(Color.LIGHT_GRAY);
        passwordField.setForeground(Color.BLACK);
        passwordField.setBorder(new LineBorder(Color.GRAY, 1, true));
    }

    /**
     * Crée un JButton avec un design arrondi et le texte spécifié.
     *
     * @param text Le texte à afficher sur le bouton.
     * @return Un JButton stylisé.
     */
    private JButton createRoundedButton(String text) {
        JButton button = new JButton(text);
        button.setBackground(new Color(255, 204, 0));
        button.setForeground(Color.BLACK);
        button.setFocusPainted(false);
        button.setBorder(new BordureArrondie(15));
        return button;
    }
}
