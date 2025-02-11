package JavaProject;

import java.awt.*;
import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.LineBorder;

class AddUserInterface extends JFrame {
    public AddUserInterface(JFrame parent) {
        setTitle("Ajouter un utilisateur");
        setSize(800, 500);
        setLayout(new BorderLayout());
        getContentPane().setBackground(new Color(29, 46, 56));
        setResizable(false);
        
    	try {
    	    // Charger l'image depuis le dossier 'resources' dans le projet
    		ImageIcon icon = new ImageIcon("resources\\pdpooo.png");
    	    setIconImage(icon.getImage()); // Définir l'icône de la fenêtre
    	} catch (Exception e) {
    	    System.out.println("Erreur lors du chargement de l'icône: " + e.getMessage());
    	}
    	
        // ---- Panneau principal ----
        JPanel formPanel = new JPanel();
        formPanel.setLayout(new GridLayout(7, 2, 10, 10));
        formPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        formPanel.setBackground(new Color(43, 60, 70));

        // Champs pour les informations utilisateur
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

        // ---- Panneau des boutons ----
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
        buttonPanel.setBackground(new Color(43, 60, 70));

        JButton addButton = createRoundedButton("Ajouter");
        addButton.addActionListener(e -> {
            String name = nameField.getText();
            String firstName = firstNameField.getText();
            String job = jobField.getText();
            String leaveDays = leaveDaysField.getText();
            String password = new String(passwordField.getPassword());
            String status = (String) statusComboBox.getSelectedItem();

            JOptionPane.showMessageDialog(this,
                    "Utilisateur ajouté :\nNom : " + name +
                            "\nPrénom : " + firstName +
                            "\nPoste : " + job +
                            "\nJours de congés restants : " + leaveDays +
                            "\nStatut : " + status);
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

    // Méthodes pour styliser les composants
    private JLabel createStyledLabel(String text) {
        JLabel label = new JLabel(text);
        label.setFont(new Font("Arial", Font.BOLD, 14));
        label.setForeground(Color.WHITE);
        return label;
    }

    private JTextField createStyledTextField() {
        JTextField textField = new JTextField();
        textField.setBackground(Color.LIGHT_GRAY);
        textField.setForeground(Color.BLACK);
        textField.setBorder(new LineBorder(Color.GRAY, 1, true));
        return textField;
    }

    private void stylePasswordField(JPasswordField passwordField) {
        passwordField.setBackground(Color.LIGHT_GRAY);
        passwordField.setForeground(Color.BLACK);
        passwordField.setBorder(new LineBorder(Color.GRAY, 1, true));
    }

    private JButton createRoundedButton(String text) {
        JButton button = new JButton(text);
        button.setBackground(new Color(255, 204, 0));
        button.setForeground(Color.BLACK);
        button.setFocusPainted(false);
        button.setBorder(new RoundBorder(15));
        return button;
    }
}
