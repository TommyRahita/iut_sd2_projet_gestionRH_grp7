package JavaProject;

import javax.swing.*;
import javax.swing.border.LineBorder;
import java.awt.*;

public class EmployeeInterface extends JFrame {
    public EmployeeInterface() {
        try {
            ImageIcon icon = new ImageIcon("resources\\icon.png");
            setIconImage(icon.getImage());
        } catch (Exception e) {
            System.out.println("Erreur lors du chargement de l'icône: " + e.getMessage());
        }

        setTitle("Interface Employee");
        setSize(800, 500);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());
        setResizable(false);

        // Panneau supérieur avec le nom de l'employé
        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        topPanel.setBackground(new Color(43, 60, 70));
        JLabel nameLabel = new JLabel("Employee : *à implémenter*");
        nameLabel.setFont(new Font("Arial", Font.BOLD, 16));
        nameLabel.setForeground(Color.WHITE);
        nameLabel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        topPanel.add(nameLabel);
        add(topPanel, BorderLayout.NORTH);

        // Panneau central contenant les boutons
        JPanel buttonPanel = new JPanel(new GridLayout(3, 1, 10, 20));
        buttonPanel.setBackground(Color.WHITE);
        buttonPanel.setBorder(new LineBorder(new Color(255, 255, 255), 35, true));

        // Bouton 1 : Saisir le nombre d'heures travaillées
        JButton enterWorkedHours = createRoundedButton("Saisissez votre nombre d'heures travaillées ce mois-ci");
        enterWorkedHours.addActionListener(e -> openEnterWorkedHours());
        buttonPanel.add(enterWorkedHours);

        // Bouton 2 : Demander des congés
        JButton congeRequestButton = createRoundedButton("Demander des congés");
        congeRequestButton.addActionListener(e -> openCongesRequestInterface());
        buttonPanel.add(congeRequestButton);

        // Bouton 3 : Télécharger la fiche de paie
        JButton downloadPaySlipButton = createRoundedButton("Téléchargez votre fiche de paie");
        downloadPaySlipButton.addActionListener(e -> downloadPaySlip());
        buttonPanel.add(downloadPaySlipButton);

        // Configuration des UIManager pour les ComboBox (provenant de la version B)
        UIManager.put("ComboBox.selectionBackground", new Color(255, 255, 255));
        UIManager.put("ComboBox.selectionForeground", Color.BLACK);

        JPanel centerPanel = new JPanel(new GridBagLayout());
        centerPanel.add(buttonPanel);
        centerPanel.setBackground(new Color(29, 46, 56));
        add(centerPanel, BorderLayout.CENTER);

        // Panneau inférieur avec les boutons Déconnexion et Quitter
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

    private JButton createRoundedButton(String text) {
        JButton button = new JButton(text);
        button.setBackground(new Color(255, 204, 0));
        button.setForeground(Color.BLACK);
        button.setFocusPainted(false);
        button.setBorder(new RoundBorder(15));
        return button;
    }

    // Ouvre la fenêtre pour saisir le nombre d'heures travaillées (fonctionnalité de la version A)
    private void openEnterWorkedHours() {
        new EnterWorkedHours(this);
    }
   
    // Ouvre l'interface de demande de congés (fonctionnalité ajoutée dans la version B)
    private void openCongesRequestInterface() {
        new CongeRequest(this);
    }
   
    // Affiche un message pour le téléchargement de la fiche de paie (stub)
    private void downloadPaySlip() {
        JOptionPane.showMessageDialog(this, "Téléchargement à implémenter");
    }

    // Déconnexion : ferme l'interface et ouvre la fenêtre de login (fonctionnalité de la version A)
    private void logoutAction() {
        dispose();
        SwingUtilities.invokeLater(() -> {
            LoginFrame loginFrame = new LoginFrame();
            loginFrame.setVisible(true);
        });
    }
   
    public static void main(String[] args) {
        SwingUtilities.invokeLater(EmployeeInterface::new);
    }
}