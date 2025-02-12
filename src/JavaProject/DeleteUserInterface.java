package JavaProject;

import java.awt.*;
import javax.swing.*;
import javax.swing.border.LineBorder;

class DeleteUserInterface extends JFrame {
    public DeleteUserInterface(JFrame parent) {
        setTitle("Supprimer un utilisateur");
        setSize(800, 500);
        setLayout(new BorderLayout());
        getContentPane().setBackground(new Color(29, 46, 56));
        
    	try {
    	    // Charger l'image depuis le dossier 'resources' dans le projet
    		ImageIcon icon = new ImageIcon("resources\\icon.png");
    	    setIconImage(icon.getImage()); // Définir l'icône de la fenêtre
    	} catch (Exception e) {
    	    System.out.println("Erreur lors du chargement de l'icône: " + e.getMessage());
    	}
        
        // ---- Panneau principal ----
        JPanel formPanel = new JPanel();
        formPanel.setLayout(new GridBagLayout());
        formPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        formPanel.setBackground(new Color(43, 60, 70));
        setResizable(false);
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.anchor = GridBagConstraints.CENTER;

        JLabel choiceLabel = createStyledLabel("Veuillez choisir un utilisateur :");
        formPanel.add(choiceLabel, gbc);

        gbc.gridy++;
        JComboBox<String> choiceBox = new JComboBox<>(new String[]{"à implémenter", "Utilisateur 1", "Utilisateur 2"});
        choiceBox.setPreferredSize(new Dimension(200, 30));
        choiceBox.setBackground(new Color(255, 204, 0));
        choiceBox.setForeground(Color.BLACK);
        formPanel.add(choiceBox, gbc);

        // ---- Panneau des boutons ----
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
        buttonPanel.setBackground(new Color(43, 60, 70));

        JButton deleteButton = createRoundedButton("Supprimer");
        deleteButton.addActionListener(e -> JOptionPane.showMessageDialog(this, "Action 'Supprimer' à implémenter."));
        buttonPanel.add(deleteButton);

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

    private JButton createRoundedButton(String text) {
        JButton button = new JButton(text);
        button.setBackground(new Color(255, 204, 0));
        button.setForeground(Color.BLACK);
        button.setFocusPainted(false);
        button.setBorder(new LineBorder(new Color(255, 204, 0), 4, true));
        return button;
    }
}
