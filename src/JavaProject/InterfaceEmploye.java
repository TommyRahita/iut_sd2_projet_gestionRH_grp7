package JavaProject;

import javax.swing.*;
import javax.swing.border.LineBorder;
import java.awt.*;

/**
 * Classe InterfaceEmploye.
 * <p>
 * Cette classe gère les fonctionnalités liées à l'interface employé dans le système.
 * Elle affiche les informations de l'employé connecté ainsi que des options pour demander des congés,
 * télécharger la fiche de paie, se déconnecter ou quitter l'application.
 * </p>
 *
 * @author Groupe 7
 * @version 1.0
 */
public class InterfaceEmploye extends JFrame {
    private Employe employe;

    /**
     * Constructeur de l'interface employé.
     *
     * @param employe L'employé connecté dont les informations sont affichées.
     */
    public InterfaceEmploye(Employe employe) {
        this.employe = employe;
        initialiserUI();
    }

    /**
     * Initialise l'interface utilisateur.
     */
    private void initialiserUI() {
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

        // Panel du haut affichant le nom de l'employé
        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        topPanel.setBackground(new Color(43, 60, 70));
        JLabel nameLabel = new JLabel("Employé : " + employe.prenom + " " + employe.nom);
        nameLabel.setFont(new Font("Arial", Font.BOLD, 16));
        nameLabel.setForeground(Color.WHITE);
        nameLabel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        topPanel.add(nameLabel);
        add(topPanel, BorderLayout.NORTH);

        // Panel central avec les boutons pour demander congés et télécharger la fiche de paie
        JPanel buttonPanel = new JPanel(new GridLayout(2, 1, 10, 20)); // 2 lignes
        buttonPanel.setBackground(Color.WHITE);
        buttonPanel.setBorder(new LineBorder(new Color(255, 255, 255), 35, true));

        JButton congeRequestButton = createRoundedButton("Demander des congés");
        congeRequestButton.addActionListener(e -> openCongesRequestInterface());
        buttonPanel.add(congeRequestButton);

        JButton downloadPaySlipButton = createRoundedButton("Télécharger votre fiche de paie");
        downloadPaySlipButton.addActionListener(e -> openDialogueSelectionFichePaie());
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

    /**
     * Crée un bouton avec un design arrondi.
     *
     * @param text Le texte à afficher sur le bouton.
     * @return Un JButton stylisé.
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
     * Ouvre l'interface de demande de congés.
     */
    private void openCongesRequestInterface() {
        new InterfaceDemandeConge(this, employe);
    }

    /**
     * Ouvre la boîte de dialogue pour sélectionner le mois et l'année de la fiche de paie à télécharger.
     */
    private void openDialogueSelectionFichePaie() {
        new InterfaceDialogueSelectionFichePaie(this, employe);
    }

    /**
     * Effectue l'action de déconnexion en fermant cette interface et en affichant l'écran de connexion.
     */
    private void logoutAction() {
        dispose();
        SwingUtilities.invokeLater(() -> {
            InterfaceConnexion loginFrame = new InterfaceConnexion();
            loginFrame.setVisible(true);
        });
    }
}
