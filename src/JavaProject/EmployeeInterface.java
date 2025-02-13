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

        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        topPanel.setBackground(new Color(43, 60, 70));

        JLabel nameLabel = new JLabel("Employee : *à implémenter*");
        nameLabel.setFont(new Font("Arial", Font.BOLD, 16));
        nameLabel.setForeground(Color.WHITE);
        nameLabel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        topPanel.add(nameLabel);
        add(topPanel, BorderLayout.NORTH);

        JPanel buttonPanel = new JPanel(new GridLayout(2, 1, 10, 20));
        buttonPanel.setBackground(Color.WHITE);
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(20, 40, 20, 40));

        JButton enterWorkedHours = createRoundedButton("Saisissez votre nombre d'heures travaillées ce mois-ci");
        enterWorkedHours.addActionListener(e -> openEnterWorkedHours());
        buttonPanel.add(enterWorkedHours);

        JButton deleteUserButton = createRoundedButton("Téléchargez votre fiche de paie");
        deleteUserButton.addActionListener(e -> downloadBulPai());
        buttonPanel.add(deleteUserButton);

        JPanel containerPanel = new JPanel(new GridBagLayout());
        containerPanel.setBackground(new Color(29, 46, 56));
        containerPanel.add(buttonPanel);

        add(containerPanel, BorderLayout.CENTER);

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

    private void openEnterWorkedHours() {
        new EnterWorkedHours(this);
    }
    
    private void downloadBulPai() {
        JOptionPane.showMessageDialog(this, "Téléchargement à implémenter");
    }

    private void logoutAction() {
        dispose(); // Ferme l'interface employee
        SwingUtilities.invokeLater(() -> {
            LoginFrame loginFrame = new LoginFrame();
            loginFrame.setVisible(true);
        });
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(EmployeeInterface::new);
    }
}