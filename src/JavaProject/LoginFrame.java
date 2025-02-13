package JavaProject;

import javax.swing.*;
import java.awt.*;

public class LoginFrame extends JFrame {
//  Rajoutez le faite que si l'user + mdp sont bon dans le csv alors rentrer dans l'interface utilisateur ou manager
	
    public LoginFrame() {
    	try {
            // Charger l'image depuis le dossier 'resources' dans le projet
            ImageIcon icon = new ImageIcon("resources\\icon.png");
            setIconImage(icon.getImage()); // Définir l'icône de la fenêtre
        } catch (Exception e) {
            System.out.println("Erreur lors du chargement de l'icône: " + e.getMessage());
        }
        setTitle("Logiciel de gestion");
        setSize(450, 300);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);

        // Création du panneau principal
        JPanel panel = new JPanel();
        panel.setLayout(null);
        panel.setBackground(new Color(40, 55, 71));

        // Label de connexion
        JLabel lblTitle = new JLabel("Connectez-vous à votre compte", SwingConstants.CENTER);
        lblTitle.setForeground(Color.LIGHT_GRAY);
        lblTitle.setFont(new Font("Arial", Font.BOLD, 14));

        // Centrage dynamique du label
        int panelWidth = getWidth();
        int labelWidth = 250;
        int x = (panelWidth - labelWidth) / 2;

        lblTitle.setBounds(x, 20, labelWidth, 20);
        panel.add(lblTitle);

        // Champ Email
        JTextField txtEmail = new JTextField();
        txtEmail.setBounds(100, 60, 250, 30);
        panel.add(txtEmail);

        // Champ Mot de passe
        JPasswordField txtPassword = new JPasswordField();
        txtPassword.setBounds(100, 100, 250, 30);
        panel.add(txtPassword);

        // Bouton Connexion
        JButton btnLogin = new JButton("Connexion");
        btnLogin.setBounds(100, 140, 250, 35);
        btnLogin.setBackground(new Color(255, 204, 0));
        btnLogin.setForeground(Color.BLACK);
        panel.add(btnLogin);

        // Ajout du panel à la fenêtre
        add(panel);
    }

}
