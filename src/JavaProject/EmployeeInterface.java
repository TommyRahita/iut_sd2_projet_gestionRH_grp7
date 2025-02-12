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
        JPanel buttonPanel = new JPanel(new GridBagLayout()); // Utilisation de GridBagLayout pour centrer
        buttonPanel.setBackground(Color.WHITE); // Fond blanc pour le panneau central
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(20, 40, 20, 40)); // Ajout de marges pour l'aération

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0; // Une seule colonne
        gbc.gridy = 0; // Ligne initiale
        gbc.insets = new Insets(10, 0, 10, 0); // Espacement vertical entre les boutons
        gbc.anchor = GridBagConstraints.CENTER; // Centrage horizontal

        // Ajout des boutons avec les contraintes
        JButton enterWorkedHours = createRoundedButton("Saisissez votre nombre d'heures travaillées ce mois-ci");
        buttonPanel.add(enterWorkedHours, gbc);

        gbc.gridy++; // Ligne suivante
        JButton congerequest = createRoundedButton("Demander des congés");
        congerequest.addActionListener(e -> openCongesRequestInterface());
        buttonPanel.add(congerequest, gbc);

        gbc.gridy++; // Ligne suivante
        JButton deleteUserButton = createRoundedButton("Téléchargez votre fiche de paie");
        buttonPanel.add(deleteUserButton, gbc);

        // ---- Panel contenant buttonPanel pour centrer et ajuster sa taille ----
        JPanel containerPanel = new JPanel(new GridBagLayout()); // Centrage du panneau
        containerPanel.setBackground(new Color(29, 46, 56)); // Même couleur que le fond principal
        containerPanel.add(buttonPanel); // Ajout du panneau blanc contenant les boutons

        add(containerPanel, BorderLayout.CENTER);

        // ---- Panneau inférieur avec Déconnexion & Quitter ----
        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 15, 10)); // Aligné à droite
        bottomPanel.setBackground(new Color(43, 60, 70)); // Couleur de fond

        JButton logoutButton = createRoundedButton("Déconnexion");
        bottomPanel.add(logoutButton);

        JButton quitButton = createRoundedButton("Quitter");
        quitButton.addActionListener(e -> System.exit(0));
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
        
        // Taille préférée uniforme
        Dimension buttonSize = new Dimension(400, 22); // Par exemple, largeur 400px, hauteur 40px
        button.setPreferredSize(buttonSize);

        return button;
    }

    private void openCongesRequestInterface() {
        new CongeRequest();
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(EmployeeInterface::new);
    }
}
