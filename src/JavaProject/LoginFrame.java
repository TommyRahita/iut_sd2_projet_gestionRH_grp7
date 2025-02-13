package JavaProject;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

public class LoginFrame extends JFrame {
    private JTextField txtEmail;
    private JPasswordField txtPassword;

    public LoginFrame() {
        try {
            ImageIcon icon = new ImageIcon("resources\\icon.png");
            setIconImage(icon.getImage());
        } catch (Exception e) {
            System.out.println("Erreur lors du chargement de l'icône: " + e.getMessage());
        }
        setTitle("Logiciel de gestion");
        setSize(450, 300);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);

        JPanel panel = new JPanel();
        panel.setLayout(null);
        panel.setBackground(new Color(40, 55, 71));

        JLabel lblTitle = new JLabel("Connectez-vous à votre compte", SwingConstants.CENTER);
        lblTitle.setForeground(Color.LIGHT_GRAY);
        lblTitle.setFont(new Font("Arial", Font.BOLD, 14));
        lblTitle.setBounds(100, 20, 250, 20);
        panel.add(lblTitle);

        txtEmail = new JTextField();
        txtEmail.setBounds(100, 60, 250, 30);
        panel.add(txtEmail);

        txtPassword = new JPasswordField();
        txtPassword.setBounds(100, 100, 250, 30);
        panel.add(txtPassword);

        JButton btnLogin = new JButton("Connexion");
        btnLogin.setBounds(100, 140, 250, 35);
        btnLogin.setBackground(new Color(255, 204, 0));
        btnLogin.setForeground(Color.BLACK);
        panel.add(btnLogin);

        // Action lors du clic sur Connexion
        btnLogin.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String email = txtEmail.getText().trim();
                String password = new String(txtPassword.getPassword()).trim();
                verifierIdentifiants(email, password);
            }
        });

        add(panel);
    }

    private void verifierIdentifiants(String email, String password) {
        List<Utilisateur> utilisateurs = App.func_recup_data("resources/Utilisateurs.csv");

        for (Utilisateur utilisateur : utilisateurs) {
            if (utilisateur.nom.equalsIgnoreCase(email) && utilisateur.mdp.equals(password)) {
                JOptionPane.showMessageDialog(this, "Connexion réussie en tant que " + utilisateur.statut);
                if (utilisateur.statut.equalsIgnoreCase("manager")) {
                	SwingUtilities.invokeLater(ManagerInterface::new);
                } else {
                	SwingUtilities.invokeLater(EmployeeInterface::new);
                }
                dispose(); // Fermer la fenêtre de connexion après authentification
                return;
            }
        }
        JOptionPane.showMessageDialog(this, "Identifiants incorrects", "Erreur", JOptionPane.ERROR_MESSAGE);
    }
}
