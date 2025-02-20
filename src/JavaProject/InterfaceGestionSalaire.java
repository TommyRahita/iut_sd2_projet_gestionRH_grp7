package JavaProject;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Interface graphique pour la gestion des salaires.
 * Permet de sélectionner un utilisateur, calculer son salaire et gérer la paie.
 */
/**
 * Classe InterfaceGestionSalaire.
 * Gère salairemanagementinterface dans le système.
 */
public class InterfaceGestionSalaire extends JFrame {
    private Manager gestionnaire;
    private JComboBox<String> choiceBox1;
    private DefaultComboBoxModel<String> comboBoxModel;
    private JTextField searchField;
    private JLabel salaireLabel;
    private List<String> utilisateurs;

    /**
     * Constructeur de l'interface de gestion des salaires.
     * @param parent La fenêtre parente qui a ouvert cette interface.
     */
    public InterfaceGestionSalaire(JFrame parent) {
        this.gestionnaire = new Manager();
        setTitle("Menu de Management des Salaires");
        setSize(800, 500);
        setLayout(new BorderLayout());
        getContentPane().setBackground(new Color(29, 46, 56));

        setupUI(parent);
        setLocationRelativeTo(null);
        setVisible(true);
    }

    /**
     * Initialise l'interface utilisateur.
     * @param parent La fenêtre parente.
     */
    private void setupUI(JFrame parent) {
        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        formPanel.setBackground(new Color(43, 60, 70));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.insets = new Insets(10, 10, 10, 10);

        JLabel choiceLabel1 = new JLabel("Veuillez choisir un utilisateur");
        choiceLabel1.setForeground(Color.WHITE);
        formPanel.add(choiceLabel1, gbc);

        gbc.gridy++;
        searchField = new JTextField(20);
        formPanel.add(searchField, gbc);

        gbc.gridy++;
        comboBoxModel = new DefaultComboBoxModel<>();
        choiceBox1 = new JComboBox<>(comboBoxModel);
        formPanel.add(choiceBox1, gbc);

        // Charger tous les utilisateurs via Manager
        utilisateurs = Manager.chargerUtilisateurs();
        Manager.mettreAJourListe(comboBoxModel, utilisateurs);

        searchField.addKeyListener(new KeyAdapter() {
            @Override
/**
 * Méthode keyReleased.
 * Description de la méthode.
 * @param e Description du paramètre.
 */
            public void keyReleased(KeyEvent e) {
                List<String> resultats = Manager.filtrerUtilisateurs(utilisateurs, searchField.getText().trim());
                Manager.mettreAJourListe(comboBoxModel, resultats);
            }
        });

        gbc.gridy++;
        JPanel salairePanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        salairePanel.setBackground(new Color(43, 60, 70));

        salaireLabel = new JLabel("Salaire: Non calculé");
        salaireLabel.setFont(new Font("Arial", Font.BOLD, 16));
        salaireLabel.setForeground(Color.WHITE);
        salaireLabel.setOpaque(true);
        salaireLabel.setBackground(new Color(29, 46, 56));
        salaireLabel.setHorizontalAlignment(SwingConstants.CENTER);
        salaireLabel.setPreferredSize(new Dimension(300, 40));
        salairePanel.add(salaireLabel);
        formPanel.add(salairePanel, gbc);

        gbc.gridy++;
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
        buttonPanel.setBackground(new Color(43, 60, 70));

        JButton calculerSalaireButton = createStyledButton("Calculer le salaire");
        calculerSalaireButton.addActionListener(e -> 
                Manager.calculerSalaireUI(this, gestionnaire, (String) choiceBox1.getSelectedItem(), salaireLabel));
        JButton generatePayslipButton = createStyledButton("Gérer la paie");
        generatePayslipButton.addActionListener(e -> ouvrirInterfaceSaisie());

        buttonPanel.add(calculerSalaireButton);
        buttonPanel.add(generatePayslipButton);
        formPanel.add(buttonPanel, gbc);

        gbc.gridy++;
        JPanel backButtonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        backButtonPanel.setBackground(new Color(43, 60, 70));

        JButton backButton = createStyledButton("Retour");
        backButton.addActionListener(e -> {
            parent.setVisible(true);
            dispose();
        });
        backButtonPanel.add(backButton);
        formPanel.add(backButtonPanel, gbc);

        add(formPanel, BorderLayout.CENTER);
    }

    /**
     * Ouvre l'interface de saisie de la paie pour l'utilisateur sélectionné.
     */
    private void ouvrirInterfaceSaisie() {
        String selectedUser = (String) choiceBox1.getSelectedItem();
        if (selectedUser == null || selectedUser.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Veuillez sélectionner un utilisateur.", "Erreur", JOptionPane.ERROR_MESSAGE);
            return;
        }
        new InterfaceSaisiePaie(this, selectedUser);
    }

    /**
     * Crée un bouton stylisé.
     * @param text Texte du bouton.
     * @return Bouton stylisé.
     */
    private JButton createStyledButton(String text) {
        JButton button = new JButton(text);
        button.setBackground(new Color(255, 204, 0));
        button.setForeground(Color.BLACK);
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createEmptyBorder(10, 15, 10, 15));
        return button;
    }
}
