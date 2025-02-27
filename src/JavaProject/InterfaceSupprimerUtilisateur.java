package JavaProject;

import javax.swing.*;
import javax.swing.border.LineBorder;
import java.awt.*;

/**
 * Classe SupprimerUtilisateurInterface.
 * <p>
 * Cette classe gère les fonctionnalités liées à l'interface de suppression d'un utilisateur dans le système.
 * Elle permet de sélectionner un utilisateur depuis une liste déroulante et de le supprimer après confirmation.
 * </p>
 * 
 * @author Groupe 7
 * @version 1.0
 */
class SupprimerUtilisateurInterface extends JFrame {
    
    /**
     * Constructeur de l'interface de suppression d'utilisateur.
     *
     * @param parent La fenêtre parente qui a ouvert cette interface.
     */
    public SupprimerUtilisateurInterface(JFrame parent) {
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
        JPanel formPanel = new JPanel(new GridBagLayout());
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

        // Charger les utilisateurs depuis le fichier CSV via Manager.loadUsersFromCSV()
        String[] utilisateurs = Manager.loadUsersFromCSV();

        gbc.gridy++;
        JComboBox<String> choiceBox = new JComboBox<>(utilisateurs);
        choiceBox.setPreferredSize(new Dimension(200, 30));
        choiceBox.setBackground(new Color(255, 204, 0));
        choiceBox.setForeground(Color.BLACK);
        formPanel.add(choiceBox, gbc);

        // ---- Panneau des boutons ----
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
        buttonPanel.setBackground(new Color(43, 60, 70));

        JButton deleteButton = createRoundedButton("Supprimer");
        deleteButton.addActionListener(e -> {
            String selectedUser = (String) choiceBox.getSelectedItem();
            if (selectedUser != null && !selectedUser.equals("Aucun utilisateur trouvé")) {
                int confirmation = JOptionPane.showConfirmDialog(
                    this,
                    "Êtes-vous sûr de vouloir supprimer l'utilisateur : " + selectedUser + " ?",
                    "Confirmation de suppression",
                    JOptionPane.YES_NO_OPTION
                );
                if (confirmation == JOptionPane.YES_OPTION) {
                    // Appeler la méthode pour supprimer l'utilisateur via Manager
                    Manager.deleteUserFromCSV(selectedUser);
                    JOptionPane.showMessageDialog(this, "Utilisateur supprimé avec succès !");
                    // Recharger la liste déroulante
                    choiceBox.setModel(new DefaultComboBoxModel<>(Manager.loadUsersFromCSV()));
                }
            } else {
                JOptionPane.showMessageDialog(this, "Veuillez sélectionner un utilisateur valide.", "Erreur", JOptionPane.ERROR_MESSAGE);
            }
        });

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
     * Crée un bouton arrondi avec le texte spécifié.
     *
     * @param text Le texte du bouton.
     * @return Un JButton stylisé.
     */
    private JButton createRoundedButton(String text) {
        JButton button = new JButton(text);
        button.setBackground(new Color(255, 204, 0));
        button.setForeground(Color.BLACK);
        button.setFocusPainted(false);
        button.setBorder(new LineBorder(new Color(255, 204, 0), 4, true));
        return button;
    }
}
