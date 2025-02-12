package JavaProject;

import javax.swing.*;
import javax.swing.border.LineBorder;

import java.awt.*;

public class EmployeeInterface extends JFrame {
    public EmployeeInterface() {
        try {
            // Charger l'image depuis le dossier 'resources' dans le projet
            ImageIcon icon = new ImageIcon("resources\\icon.png");
            setIconImage(icon.getImage()); // Définir l'icône de la fenêtre
        } catch (Exception e) {
            System.out.println("Erreur lors du chargement de l'icône: " + e.getMessage());
        }

        // Configuration de la fenêtre principale
        setTitle("Interface Employee");
        setSize(800, 500);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout()); // Utilisation de BorderLayout pour organiser les composants
        setResizable(false);

        // ---- Panneau supérieur avec le nom de l'employé ----
        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT)); // Aligné à droite
        topPanel.setBackground(new Color(43, 60, 70)); // Couleur de fond du haut

        JLabel nameLabel = new JLabel("Employee : *à implémenter*");
        nameLabel.setFont(new Font("Arial", Font.BOLD, 16)); // Style du texte
        nameLabel.setForeground(Color.WHITE); // Texte en blanc pour plus de contraste
        nameLabel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10)); // Marges autour du texte

        topPanel.add(nameLabel);
        add(topPanel, BorderLayout.NORTH);

        // ---- Panneau contenant les boutons principaux ----
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new GridLayout(3, 1, 10, 20)); // Grille pour organiser les boutons
        buttonPanel.setBackground(new Color(255, 255, 255)); // Couleur de fond
        buttonPanel.setBorder(new LineBorder(new Color(255, 255, 255), 35, true));

        // Ajout des boutons avec les contraintes
        JButton enterWorkedHours = createRoundedButton("Saisissez votre nombre d'heures travaillées ce mois-ci");
        enterWorkedHours.addActionListener(e -> openEnterWorkedHours());
        buttonPanel.add(enterWorkedHours);

        JButton congerequest = createRoundedButton("Demander des congés");
        congerequest.addActionListener(e -> openCongesRequestInterface());
        buttonPanel.add(congerequest);

        JButton deleteUserButton = createRoundedButton("Téléchargez votre fiche de paie");
        deleteUserButton.addActionListener(e -> openDeleteUserButton());
        buttonPanel.add(deleteUserButton);
        
        UIManager.put("ComboBox.selectionBackground", new Color(255, 255, 255)); // Jaune foncé/orange pour la sélection
        UIManager.put("ComboBox.selectionForeground", Color.BLACK);
        
        // ---- Ajout du panneau centré pour les boutons ----
        JPanel centerPanel = new JPanel(new GridBagLayout());
        centerPanel.add(buttonPanel);
        centerPanel.setBackground(new Color(29, 46, 56));
        
        add(centerPanel, BorderLayout.CENTER);

        // ---- Panneau inférieur avec Déconnexion & Quitter ----
        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 15, 10)); // Aligné à droite
        bottomPanel.setBackground(new Color(43, 60, 70)); // Couleur de fond

        JButton logoutButton = createRoundedButton("Déconnexion");
        logoutButton.addActionListener(e -> logoutAction());
        //logoutButton.setBorder(new LineBorder(new Color(255, 255, 255), 2, true));
        bottomPanel.add(logoutButton);

        JButton quitButton = createRoundedButton("Quitter");
        quitButton.addActionListener(e -> System.exit(0));
        //quitButton.setBorder(new LineBorder(new Color(255, 255, 255), 2, true));
        bottomPanel.add(quitButton);

        add(bottomPanel, BorderLayout.SOUTH);

        // Rendre la fenêtre visible
        setLocationRelativeTo(null); // Centrer la fenêtre
        setVisible(true);
    }

    // Méthode pour créer des boutons arrondis avec une taille uniforme
    private JButton createRoundedButton(String text) {
        JButton button = new JButton(text);
        button.setBackground(new Color(255, 204, 0));
        button.setForeground(Color.BLACK);
        button.setFocusPainted(false);
        button.setBorder(new RoundBorder(15)); // Bordure arrondie avec un rayon de 15 pixels
        return button;
    }

    private void openEnterWorkedHours() {
    	JOptionPane.showMessageDialog(this, "à implémenter");
    }
    
    private void openCongesRequestInterface() {
        new CongeRequest();
    }
    
    private void openDeleteUserButton() {
    	JOptionPane.showMessageDialog(this, "à implémenter");
    }

    // Action pour le bouton déconnexion (à implémenter)
    private void logoutAction() {
        JOptionPane.showMessageDialog(this, "Déconnexion à implémenter");
    }
    
    public static void main(String[] args) {
        SwingUtilities.invokeLater(EmployeeInterface::new);
    }
}

