package JavaProject;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.LineBorder;
import java.awt.*;

/**
 * Interface graphique pour les gestionnaires.
 * Permet la gestion des utilisateurs, la validation des congés et la gestion des salaires.
 */
/**
 * Classe InterfaceManager.
 * Gère gestionnaireinterface dans le système.
 */
public class InterfaceManager extends JFrame {
    private Utilisateur gestionnaire;

    /**
     * Constructeur de l'interface gestionnaire.
     * @param gestionnaire L'utilisateur gestionnaire connecté.
     */
    public InterfaceManager(Utilisateur gestionnaire) {
        this.gestionnaire = gestionnaire;
        initialiserUI();
    }

    /**
     * Initialise l'interface utilisateur.
     */
    private void initialiserUI() {
        try {
            ImageIcon icon = new ImageIcon("resources\\icon.png");
            setIconImage(icon.getImage());
        } catch (Exception e) {
            System.out.println("Erreur lors du chargement de l'icône: " + e.getMessage());
        }

        setTitle("Interface Manager");
        setSize(800, 500);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());
        setResizable(false);

        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        topPanel.setBackground(new Color(43, 60, 70));

        JLabel nameLabel = new JLabel("Manager : " + gestionnaire.prenom + " " + gestionnaire.nom);
        nameLabel.setFont(new Font("Arial", Font.BOLD, 16));
        nameLabel.setForeground(Color.WHITE);
        nameLabel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        topPanel.add(nameLabel);
        add(topPanel, BorderLayout.NORTH);

        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new GridLayout(4, 1, 10, 20));
        buttonPanel.setBackground(Color.WHITE);
        buttonPanel.setBorder(new LineBorder(Color.WHITE, 35, true));

        JButton addUserButton = createRoundedButton("Ajouter un utilisateur");
        addUserButton.addActionListener(e -> openInterfaceAjouterUtilisateur());
        buttonPanel.add(addUserButton);

        JButton deleteUserButton = createRoundedButton("Supprimer un utilisateur");
        deleteUserButton.addActionListener(e -> openSupprimerUtilisateurInterface());
        buttonPanel.add(deleteUserButton);

        JButton validerCongeButton = createRoundedButton("Valider les congés");
        validerCongeButton.addActionListener(e -> openInterfaceValidationConge());
        buttonPanel.add(validerCongeButton);

        JButton salaireManagementButton = createRoundedButton("Ouvrir le menu de management des salaires");
        salaireManagementButton.addActionListener(e -> openInterfaceGestionSalaire());
        buttonPanel.add(salaireManagementButton);

        UIManager.put("ComboBox.selectionBackground", Color.WHITE);
        UIManager.put("ComboBox.selectionForeground", Color.BLACK);

        JPanel centerPanel = new JPanel(new GridBagLayout());
        centerPanel.add(buttonPanel);
        centerPanel.setBackground(new Color(29, 46, 56));

        add(centerPanel, BorderLayout.CENTER);

        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 15, 10));
        bottomPanel.setBackground(new Color(43, 60, 70));

        JButton logoutButton = createRoundedButton("Déconnexion");
        logoutButton.addActionListener(e -> logoutAction());
        bottomPanel.add(logoutButton);

        JButton quitButton = createRoundedButton("Quitter");
        quitButton.addActionListener(e -> System.exit(0));
        bottomPanel.add(quitButton);

        add(bottomPanel, BorderLayout.SOUTH);

        setLocationRelativeTo(null);
        setVisible(true);
    }

    /**
     * Crée un bouton stylisé avec des bords arrondis.
     * @param text Texte du bouton.
     * @return Bouton stylisé.
     */
    private JButton createRoundedButton(String text) {
        JButton button = new JButton(text);
        button.setBackground(new Color(255, 204, 0));
        button.setForeground(Color.BLACK);
        button.setFocusPainted(false);
        button.setBorder(new BordureArrondie(15));
        return button;
    }

    /**
     * Ouvre l'interface d'ajout d'utilisateur.
     */
    private void openInterfaceAjouterUtilisateur() {
        new InterfaceAjouterUtilisateur(this);
    }
    
    /**
     * Ouvre l'interface de suppression d'utilisateur.
     */
    private void openSupprimerUtilisateurInterface() {
        new SupprimerUtilisateurInterface(this);
    }
    
    /**
     * Ouvre l'interface de validation des congés.
     */
    private void openInterfaceValidationConge() {
        new InterfaceValidationConge(this);
    }
    
    /**
     * Ouvre l'interface de gestion des salaires.
     */
    private void openInterfaceGestionSalaire() {
        new InterfaceGestionSalaire(this);
    }

    /**
     * Action de déconnexion : ferme la fenêtre et ouvre l'écran de connexion.
     */
    private void logoutAction() {
        dispose();
        SwingUtilities.invokeLater(() -> {
            LoginFrame loginFrame = new LoginFrame();
            loginFrame.setVisible(true);
        });
    }
}