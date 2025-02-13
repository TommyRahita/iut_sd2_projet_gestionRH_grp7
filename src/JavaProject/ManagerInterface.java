package JavaProject;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.LineBorder;
import java.awt.*;

public class ManagerInterface extends JFrame {
    public ManagerInterface() {
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

        JLabel nameLabel = new JLabel("Manager : *à implémenter*");
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

    private JButton createRoundedButton(String text) {
        JButton button = new JButton(text);
        button.setBackground(new Color(255, 204, 0));
        button.setForeground(Color.BLACK);
        button.setFocusPainted(false);
        button.setBorder(new RoundBorder(15));
        return button;
    }

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

    private void logoutAction() {
        dispose(); // Ferme l'interface manager
        SwingUtilities.invokeLater(() -> {
            LoginFrame loginFrame = new LoginFrame();
            loginFrame.setVisible(true);
        });
    }
}

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
