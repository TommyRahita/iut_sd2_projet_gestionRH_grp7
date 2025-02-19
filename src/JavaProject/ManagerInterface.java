package JavaProject;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.LineBorder;
import java.awt.*;

/**
 * Interface graphique pour les managers.
 * Permet la gestion des utilisateurs, la validation des congés et la gestion des salaires.
 */
public class ManagerInterface extends JFrame {
    private Utilisateur manager;

    /**
     * Constructeur de l'interface manager.
     * @param manager L'utilisateur manager connecté.
     */
    public ManagerInterface(Utilisateur manager) {
        this.manager = manager;
        initUI();
    }

    /**
     * Initialise l'interface utilisateur.
     */
    private void initUI() {
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

        JLabel nameLabel = new JLabel("Manager : " + manager.prenom + " " + manager.nom);
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
        addUserButton.addActionListener(e -> openAddUserInterface());
        buttonPanel.add(addUserButton);

        JButton deleteUserButton = createRoundedButton("Supprimer un utilisateur");
        deleteUserButton.addActionListener(e -> openDeleteUserInterface());
        buttonPanel.add(deleteUserButton);

        JButton validateLeaveButton = createRoundedButton("Valider les congés");
        validateLeaveButton.addActionListener(e -> openValidateLeaveInterface());
        buttonPanel.add(validateLeaveButton);

        JButton salaryManagementButton = createRoundedButton("Ouvrir le menu de management des salaires");
        salaryManagementButton.addActionListener(e -> openSalaryManagementInterface());
        buttonPanel.add(salaryManagementButton);

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
        button.setBorder(new RoundBorder(15));
        return button;
    }

    /**
     * Ouvre l'interface d'ajout d'utilisateur.
     */
    private void openAddUserInterface() {
        new AddUserInterface(this);
    }
    
    /**
     * Ouvre l'interface de suppression d'utilisateur.
     */
    private void openDeleteUserInterface() {
        new DeleteUserInterface(this);
    }
    
    /**
     * Ouvre l'interface de validation des congés.
     */
    private void openValidateLeaveInterface() {
        new ValidateLeaveInterface(this);
    }
    
    /**
     * Ouvre l'interface de gestion des salaires.
     */
    private void openSalaryManagementInterface() {
        new SalaryManagementInterface(this);
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