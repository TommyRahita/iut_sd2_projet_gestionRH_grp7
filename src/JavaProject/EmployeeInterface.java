package JavaProject;

import javax.swing.*;
import javax.swing.border.LineBorder;
import java.awt.*;

public class EmployeeInterface extends JFrame {
    public EmployeeInterface() {
        try {
            // Charger l'image depuis le dossier 'resources' dans le projet
            ImageIcon icon = new ImageIcon("resources\\pdpooo.png");
            setIconImage(icon.getImage()); // Définir l'icône de la fenêtre
        } catch (Exception e) {
            System.out.println("Erreur lors du chargement de l'icône: " + e.getMessage());
        }
        // À régler, le message print d'erreur n'est pas lancé mais l'icône n'est quand même pas changée

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
        JPanel buttonPanel = new JPanel(new GridLayout(2, 1, 10, 20)); // Grille avec 2 lignes pour 2 boutons
        buttonPanel.setBackground(Color.WHITE); // Fond blanc pour le panneau central
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(20, 40, 20, 40)); // Ajout de marges pour l'aération

        // Création et personnalisation des boutons
        JButton enterWorkedHours = createRoundedButton("Saisissez votre nombre d'heures travaillées ce mois-ci");
        enterWorkedHours.addActionListener(e -> openEnterWorkedHours());
        buttonPanel.add(enterWorkedHours);

        JButton deleteUserButton = createRoundedButton("Téléchargez votre fiche de paie");
        deleteUserButton.addActionListener(e -> downloadBulPai());
        buttonPanel.add(deleteUserButton);

        // ---- Panel contenant buttonPanel pour centrer et ajuster sa taille ----
        JPanel containerPanel = new JPanel(new GridBagLayout()); // Centrage du panneau
        containerPanel.setBackground(new Color(29, 46, 56)); // Même couleur que le fond principal
        containerPanel.add(buttonPanel); // Ajout du panneau blanc contenant les boutons

        add(containerPanel, BorderLayout.CENTER);

        // ---- Panneau inférieur avec Déconnexion & Quitter ----
        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 15, 10)); // Aligné à droite
        bottomPanel.setBackground(new Color(43, 60, 70)); // Couleur de fond

        JButton logoutButton = createRoundedButton("Déconnexion");
        logoutButton.addActionListener(e -> logoutAction());
        bottomPanel.add(logoutButton);

        JButton quitButton = createRoundedButton("Quitter");
        quitButton.addActionListener(e -> System.exit(0));
        bottomPanel.add(quitButton);

        add(bottomPanel, BorderLayout.SOUTH);

        // Rendre la fenêtre visible
        setLocationRelativeTo(null); // Centrer la fenêtre
        setVisible(true);
    }

    // Méthode pour créer des boutons arrondis
    private JButton createRoundedButton(String text) {
        JButton button = new JButton(text);
        button.setBackground(new Color(255, 204, 0));
        button.setForeground(Color.BLACK);
        button.setFocusPainted(false);
        button.setBorder(new RoundBorder(15)); // Bordure arrondie avec un rayon de 15 pixels
        return button;
    }

    // Méthodes pour ouvrir les interfaces secondaires
    private void openEnterWorkedHours() {
        new EnterWorkedHours(this);
    }
    
    private void downloadBulPai() {
    	JOptionPane.showMessageDialog(this, "Téléchargement à implémenter");
    }

    // Action pour le bouton déconnexion (à implémenter)
    private void logoutAction() {
        JOptionPane.showMessageDialog(this, "Déconnexion à implémenter");
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(EmployeeInterface::new);
    }
}
