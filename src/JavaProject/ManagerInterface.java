package JavaProject;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.LineBorder;

import java.awt.*;

public class ManagerInterface extends JFrame {
    public ManagerInterface() {
    	
    	try {
    	    // Charger l'image depuis le dossier 'resources' dans le projet
    		ImageIcon icon = new ImageIcon("resources\\pdpooo.png");
    	    setIconImage(icon.getImage()); // Définir l'icône de la fenêtre
    	} catch (Exception e) {
    	    System.out.println("Erreur lors du chargement de l'icône: " + e.getMessage());
    	}
    	//à regler, le message print d'erreur n'est pas lancé mais l'icone n'est quand meme pas changé

        // Configuration de la fenêtre principale
        setTitle("Interface Manager");
        setSize(800, 500);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout()); // Utilisation de BorderLayout pour organiser les composants
        setResizable(false);
        
        // ---- Panneau supérieur avec le nom du Manager ----
        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT)); // Aligné à droite
        topPanel.setBackground(new Color(43, 60, 70)); // Couleur de fond du haut

        JLabel nameLabel = new JLabel("Manager : *à implémenter*");
        nameLabel.setFont(new Font("Arial", Font.BOLD, 16)); // Style du texte
        nameLabel.setForeground(Color.WHITE); // Texte en blanc pour plus de contraste
        nameLabel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10)); // Marges autour du texte

        topPanel.add(nameLabel);
        add(topPanel, BorderLayout.NORTH);

        // ---- Panneau contenant les boutons principaux ----
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new GridLayout(4, 1, 10, 20)); // Grille pour organiser les boutons
        buttonPanel.setBackground(new Color(255, 255, 255)); // Couleur de fond
        buttonPanel.setBorder(new LineBorder(new Color(255, 255, 255), 35, true));

        // Création et personnalisation des boutons
        JButton addUserButton = createRoundedButton("Ajouter un utilisateur");
        addUserButton.addActionListener(e -> openAddUserInterface());
        //addUserButton.setBorder(new LineBorder(new Color(255, 255, 255), 2, true));
        buttonPanel.add(addUserButton);
        
        JButton deleteUserButton = createRoundedButton("Supprimer un utilisateur");
        deleteUserButton.addActionListener(e -> openDeleteUserInterface());
        //deleteUserButton.setBorder(new LineBorder(new Color(255, 255, 255), 2, true));
        buttonPanel.add(deleteUserButton);

        JButton validateLeaveButton = createRoundedButton("Valider les congés");
        validateLeaveButton.addActionListener(e -> openValidateLeaveInterface());
        //validateLeaveButton.setBorder(new LineBorder(new Color(255, 255, 255), 2, true));
        buttonPanel.add(validateLeaveButton);

        JButton salaryManagementButton = createRoundedButton("Ouvrir le menu de management des salaires");
        salaryManagementButton.addActionListener(e -> openSalaryManagementInterface());
        //salaryManagementButton.setBorder(new LineBorder(new Color(255, 255, 255), 2, true));
        buttonPanel.add(salaryManagementButton);
        
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
    private void openAddUserInterface() {
        new AddUserInterface(this);
    }

    private void openDeleteUserInterface() {
        new DeleteUserInterface(this);
    }

    private void openValidateLeaveInterface() {
        new ValidateLeaveInterface(this);
    }

    private void openSalaryManagementInterface() {
        new SalaryManagementInterface(this);
    }

    // Action pour le bouton déconnexion (à implémenter)
    private void logoutAction() {
        JOptionPane.showMessageDialog(this, "Déconnexion à implémenter");
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(ManagerInterface::new);
    }
}

// Classe pour créer des bordures arrondies
class RoundBorder implements Border {
    private int radius;

    public RoundBorder(int radius) {
        this.radius = radius;
    }

    @Override
    public void paintBorder(Component c, Graphics g, int x, int y, int width, int height) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setColor(c.getBackground());
        g2.drawRoundRect(x, y, width - 1, height - 1, radius, radius);
        g2.dispose();
    }

    @Override
    public Insets getBorderInsets(Component c) {
        return new Insets(4, 4, 4, 4);
    }

    @Override
    public boolean isBorderOpaque() {
        return true;
    }
}
