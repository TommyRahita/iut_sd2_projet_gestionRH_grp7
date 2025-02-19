package JavaProject;

import javax.swing.*;
import javax.swing.border.LineBorder;
import java.awt.*;

/**
 * Classe EmployeeInterface.
 * Gère employeeinterface dans le système.
 */
public class EmployeeInterface extends JFrame {
    private Employe employee;

    public EmployeeInterface(Employe employee) {
        this.employee = employee;
        initUI();
    }

    private void initUI() {
        try {
            ImageIcon icon = new ImageIcon("resources/icon.png");
            setIconImage(icon.getImage());
        } catch (Exception e) {
            System.out.println("Erreur lors du chargement de l'icône: " + e.getMessage());
        }

        setTitle("Interface Employee");
        setSize(800, 500);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());
        setResizable(false);

        // Panel du haut avec nom de l'employé
        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        topPanel.setBackground(new Color(43, 60, 70));
        JLabel nameLabel = new JLabel("Employé : " + employee.prenom + " " + employee.nom);
        nameLabel.setFont(new Font("Arial", Font.BOLD, 16));
        nameLabel.setForeground(Color.WHITE);
        nameLabel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        topPanel.add(nameLabel);
        add(topPanel, BorderLayout.NORTH);

        // Panel central avec les boutons
        JPanel buttonPanel = new JPanel(new GridLayout(2, 1, 10, 20)); // 2 lignes
        buttonPanel.setBackground(Color.WHITE);
        buttonPanel.setBorder(new LineBorder(new Color(255, 255, 255), 35, true));

        JButton congeRequestButton = createRoundedButton("Demander des congés");
        congeRequestButton.addActionListener(e -> openCongesRequestInterface());
        buttonPanel.add(congeRequestButton);

        JButton downloadPaySlipButton = createRoundedButton("Télécharger votre fiche de paie");
        downloadPaySlipButton.addActionListener(e -> openPaySlipSelectionDialog());
        buttonPanel.add(downloadPaySlipButton);

        UIManager.put("ComboBox.selectionBackground", new Color(255, 255, 255));
        UIManager.put("ComboBox.selectionForeground", Color.BLACK);

        JPanel centerPanel = new JPanel(new GridBagLayout());
        centerPanel.add(buttonPanel);
        centerPanel.setBackground(new Color(29, 46, 56));
        add(centerPanel, BorderLayout.CENTER);

        // Panel du bas avec les boutons Déconnexion et Quitter
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

    private void openCongesRequestInterface() {
        new CongeRequest(this, employee);
    }

    /**
     * Ouvre une boîte de dialogue pour choisir le mois et l'année avant de télécharger la fiche de paie.
     */
    private void openPaySlipSelectionDialog() {
        new PaySlipSelectionDialog(this, employee);
    }

    private void logoutAction() {
        dispose();
        SwingUtilities.invokeLater(() -> {
            LoginFrame loginFrame = new LoginFrame();
            loginFrame.setVisible(true);
        });
    }
}
