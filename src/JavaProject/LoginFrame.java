package JavaProject;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Classe LoginFrame.
 * Gère loginframe dans le système.
 */
public class LoginFrame extends JFrame {
    private JTextField txtId;
    private JPasswordField txtPassword;

    /**
     * Constructeur de la classe LoginFrame.
     * Initialise les composants de l'interface graphique.
     */
    public LoginFrame() {
        try {
            ImageIcon icon = new ImageIcon("resources/icon.png");
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

        txtId = new JTextField();
        txtId.setBounds(100, 60, 250, 30);
        panel.add(txtId);

        txtPassword = new JPasswordField();
        txtPassword.setBounds(100, 100, 250, 30);
        panel.add(txtPassword);

        JButton btnLogin = new JButton("Connexion");
        btnLogin.setBounds(100, 140, 250, 35);
        btnLogin.setBackground(new Color(255, 204, 0));
        btnLogin.setForeground(Color.BLACK);
        panel.add(btnLogin);

        btnLogin.addActionListener(new ActionListener() {
            @Override
/**
 * Méthode actionPerformed.
 * Description de la méthode.
 * @param e Description du paramètre.
 */
            public void actionPerformed(ActionEvent e) {
                String id = txtId.getText().trim();
                String password = new String(txtPassword.getPassword()).trim();
                // Utilisation de la méthode validerIdentifiant de la classe Utilisateur
                Utilisateur utilisateur = Utilisateur.validerIdentifiant(id, password);
                if (utilisateur != null) {
                    JOptionPane.showMessageDialog(LoginFrame.this, "Connexion réussie en tant que " + utilisateur.statut);
                    if (utilisateur.statut.equalsIgnoreCase("manager")) {
                        SwingUtilities.invokeLater(() -> new ManagerInterface(utilisateur));
                    } else {
                        // Créer une instance d'Employe à partir de l'objet Utilisateur
                        Employe emp = new Employe(utilisateur.id, utilisateur.nom, utilisateur.prenom,
                                                  utilisateur.poste, utilisateur.jours_conge_restants,
                                                  utilisateur.mdp, utilisateur.statut);
                        SwingUtilities.invokeLater(() -> new EmployeeInterface(emp));
                    }
                    dispose();
                } else {
                    JOptionPane.showMessageDialog(LoginFrame.this, "ID ou mot de passe incorrect", "Erreur", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        add(panel);
    }

    /**
     * Méthode principale pour lancer l'application de connexion.
     * @param args Arguments de la ligne de commande (non utilisés).
     */
/**
 * Méthode main.
 * Description de la méthode.
 * @param args Description du paramètre.
 */
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            LoginFrame loginFrame = new LoginFrame();
            loginFrame.setVisible(true);
        });
    }
}
