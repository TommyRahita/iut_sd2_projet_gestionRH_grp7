package JavaProject;

import javax.swing.*;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.List;
import java.util.stream.Collectors;

public class SalaryManagementInterface extends JFrame {
    private Manager manager;
    private JComboBox<String> choiceBox1;
    private DefaultComboBoxModel<String> comboBoxModel;
    private JTextField searchField;
    private JLabel salaryLabel;

    public SalaryManagementInterface(JFrame parent) {
        this.manager = new Manager();
        setTitle("Menu de Management des Salaires");
        setSize(800, 500);
        setLayout(new BorderLayout());
        getContentPane().setBackground(new Color(29, 46, 56));

        setupUI();

        setLocationRelativeTo(null);
        setVisible(true);
    }

    private void setupUI() {
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

        chargerUtilisateurs(); // Charge les 5 premiers utilisateurs au lancement

        // Ajout de la recherche dynamique
        searchField.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                filtrerUtilisateurs(searchField.getText().trim());
            }
        });

        // PANEL POUR LE SALAIRE
        gbc.gridy++;
        JPanel salaryPanel = new JPanel();
        salaryPanel.setLayout(new FlowLayout(FlowLayout.CENTER));
        salaryPanel.setBackground(new Color(43, 60, 70));

        salaryLabel = new JLabel("Salaire: Non calculé");
        salaryLabel.setFont(new Font("Arial", Font.BOLD, 16));
        salaryLabel.setForeground(Color.WHITE);
        salaryLabel.setOpaque(true);
        salaryLabel.setBackground(new Color(29, 46, 56)); // Assure la visibilité
        salaryLabel.setHorizontalAlignment(SwingConstants.CENTER);
        salaryLabel.setPreferredSize(new Dimension(300, 40)); // Taille plus grande pour éviter la coupure

        salaryPanel.add(salaryLabel);
        formPanel.add(salaryPanel, gbc);

        // PANEL POUR LES BOUTONS
        gbc.gridy++;
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        buttonPanel.setBackground(new Color(43, 60, 70));

        JButton calculateSalaryButton = new JButton("Calculer le salaire");
        calculateSalaryButton.addActionListener(e -> {
            String selectedUser = (String) choiceBox1.getSelectedItem();

            if (selectedUser == null || selectedUser.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Veuillez sélectionner un utilisateur.", "Erreur", JOptionPane.ERROR_MESSAGE);
                return;
            }

            double salaireNet = manager.calculer_salaire(selectedUser);

            // Mise à jour du label du salaire avec un bon format
            salaryLabel.setText(String.format("Salaire Net : %.2f €", salaireNet));
            salaryLabel.repaint();
            salaryLabel.revalidate();
        });

        JButton generatePayslipButton = new JButton("Générer fiche de paie");
        generatePayslipButton.addActionListener(e -> {
            String selectedUser = (String) choiceBox1.getSelectedItem();
            manager.genererFichePaie(this, selectedUser);
        });

        buttonPanel.add(calculateSalaryButton);
        buttonPanel.add(generatePayslipButton);
        formPanel.add(buttonPanel, gbc);
        add(formPanel, BorderLayout.CENTER);
    }

    // Charge les 5 premiers employés au démarrage
    private void chargerUtilisateurs() {
        List<String> utilisateurs = Utilisateur.func_recup_data("resources/Utilisateurs.csv")
                .stream()
                .map(user -> user.prenom + " " + user.nom)
                .limit(5)
                .collect(Collectors.toList());

        comboBoxModel.removeAllElements();
        utilisateurs.forEach(comboBoxModel::addElement);
    }

    // Filtrer la liste des employés en fonction de la recherche
    private void filtrerUtilisateurs(String recherche) {
        List<String> utilisateurs = Utilisateur.func_recup_data("resources/Utilisateurs.csv")
                .stream()
                .map(user -> user.prenom + " " + user.nom)
                .filter(nom -> nom.toLowerCase().contains(recherche.toLowerCase()))
                .limit(5)
                .collect(Collectors.toList());

        comboBoxModel.removeAllElements();
        utilisateurs.forEach(comboBoxModel::addElement);
    }
}
