package JavaProject;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Interface graphique pour la gestion des salaires.
 * Permet de sélectionner un utilisateur, calculer son salaire et gérer la paie.
 */
public class SalaryManagementInterface extends JFrame {
    private Manager manager;
    private JComboBox<String> choiceBox1;
    private DefaultComboBoxModel<String> comboBoxModel;
    private JTextField searchField;
    private JLabel salaryLabel;
    private List<String> utilisateurs;

    /**
     * Constructeur de l'interface de gestion des salaires.
     * @param parent La fenêtre parente qui a ouvert cette interface.
     */
    public SalaryManagementInterface(JFrame parent) {
        this.manager = new Manager();
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

        chargerUtilisateurs();

        choiceBox1.addActionListener(e -> {
            String selectedUser = (String) choiceBox1.getSelectedItem();
            if (selectedUser != null) {
                System.out.println("Utilisateur sélectionné : " + selectedUser);
            }
        });

        searchField.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                filtrerUtilisateurs(searchField.getText().trim());
            }
        });

        gbc.gridy++;
        JPanel salaryPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        salaryPanel.setBackground(new Color(43, 60, 70));

        salaryLabel = new JLabel("Salaire: Non calculé");
        salaryLabel.setFont(new Font("Arial", Font.BOLD, 16));
        salaryLabel.setForeground(Color.WHITE);
        salaryLabel.setOpaque(true);
        salaryLabel.setBackground(new Color(29, 46, 56));
        salaryLabel.setHorizontalAlignment(SwingConstants.CENTER);
        salaryLabel.setPreferredSize(new Dimension(300, 40));

        salaryPanel.add(salaryLabel);
        formPanel.add(salaryPanel, gbc);

        gbc.gridy++;
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
        buttonPanel.setBackground(new Color(43, 60, 70));

        JButton calculateSalaryButton = createStyledButton("Calculer le salaire");
        calculateSalaryButton.addActionListener(e -> calculerSalaire());

        JButton generatePayslipButton = createStyledButton("Gérer la paie");
        generatePayslipButton.addActionListener(e -> ouvrirInterfaceSaisie());

        buttonPanel.add(calculateSalaryButton);
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
     * Charge tous les utilisateurs au démarrage.
     */
    private void chargerUtilisateurs() {
        utilisateurs = Utilisateur.func_recup_data("resources/Utilisateurs.csv")
                .stream()
                .map(user -> user.prenom + " " + user.nom)
                .collect(Collectors.toList());

        mettreAJourListe(utilisateurs);
    }

    /**
     * Filtre la liste des utilisateurs en fonction du texte saisi.
     * @param recherche Texte de recherche.
     */
    private void filtrerUtilisateurs(String recherche) {
        List<String> resultats = utilisateurs.stream()
                .filter(nom -> nom.toLowerCase().contains(recherche.toLowerCase()))
                .limit(5)
                .collect(Collectors.toList());

        mettreAJourListe(resultats);
    }

    /**
     * Met à jour la liste déroulante avec les utilisateurs filtrés.
     * @param utilisateursFiltrés Liste des utilisateurs filtrés.
     */
    private void mettreAJourListe(List<String> utilisateursFiltrés) {
        comboBoxModel.removeAllElements();
        utilisateursFiltrés.forEach(comboBoxModel::addElement);
    }

    /**
     * Calcule et affiche le salaire de l'utilisateur sélectionné.
     */
    private void calculerSalaire() {
        String selectedUser = (String) choiceBox1.getSelectedItem();

        if (selectedUser == null || selectedUser.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Veuillez sélectionner un utilisateur.", "Erreur", JOptionPane.ERROR_MESSAGE);
            return;
        }

        double[] salaireDetails = manager.calculer_salaire(this, selectedUser);
        if (salaireDetails == null) {
            return;
        }

        salaryLabel.setText("Salaire Net : " + String.format("%.2f €", salaireDetails[4]));
        salaryLabel.repaint();
        salaryLabel.revalidate();
    }

    /**
     * Ouvre l'interface de gestion de la paie pour l'utilisateur sélectionné.
     */
    private void ouvrirInterfaceSaisie() {
        String selectedUser = (String) choiceBox1.getSelectedItem();

        if (selectedUser == null || selectedUser.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Veuillez sélectionner un utilisateur.", "Erreur", JOptionPane.ERROR_MESSAGE);
            return;
        }

        new PayrollEntryInterface(this, selectedUser);
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
