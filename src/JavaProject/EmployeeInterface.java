package JavaProject;

import javax.swing.*;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;

public class EmployeeInterface extends JFrame {
    private Utilisateur employee;

    public EmployeeInterface(Utilisateur employee) {
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
        JPanel buttonPanel = new JPanel(new GridLayout(2, 1, 10, 20)); // On passe de 3 à 2 lignes
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


/**
 * Classe pour afficher une boîte de dialogue permettant de choisir le mois et l'année d'une fiche de paie.
 */
class PaySlipSelectionDialog extends JDialog {
    private JComboBox<Integer> monthComboBox, yearComboBox;
    private Utilisateur employee;
    private JFrame parent;

    public PaySlipSelectionDialog(JFrame parent, Utilisateur employee) {
        super(parent, "Sélectionner une fiche de paie", true);
        this.parent = parent;
        this.employee = employee;

        setSize(300, 200);
        setLayout(new GridLayout(3, 2, 10, 10));
        setLocationRelativeTo(parent);

        add(new JLabel("Mois :"));
        Integer[] months = {1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12};
        monthComboBox = new JComboBox<>(months);
        add(monthComboBox);

        add(new JLabel("Année :"));
        Integer[] years = {2023, 2024, 2025, 2026, 2027};
        yearComboBox = new JComboBox<>(years);
        add(yearComboBox);

        JButton confirmButton = new JButton("Télécharger");
        confirmButton.addActionListener(e -> downloadPaySlip());
        add(confirmButton);

        JButton cancelButton = new JButton("Annuler");
        cancelButton.addActionListener(e -> dispose());
        add(cancelButton);

        setVisible(true);
    }

    /**
     * Télécharge la fiche de paie en fonction du mois et de l'année sélectionnés.
     */
    private void downloadPaySlip() {
        int selectedMonth = (int) monthComboBox.getSelectedItem();
        int selectedYear = (int) yearComboBox.getSelectedItem();

        File paySlipFile = findPaySlipFile(employee.prenom, employee.nom, selectedMonth, selectedYear);
        if (paySlipFile == null) {
            JOptionPane.showMessageDialog(this,
                    "Aucune fiche de paie trouvée pour " + employee.prenom + " " + employee.nom +
                            " en " + selectedMonth + "/" + selectedYear,
                    "Erreur", JOptionPane.ERROR_MESSAGE);
            return;
        }

        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setSelectedFile(new File(employee.prenom + "_" + employee.nom + "_" +
                selectedMonth + "_" + selectedYear + "_fiche_paie.pdf"));

        int userChoice = fileChooser.showSaveDialog(this);
        if (userChoice == JFileChooser.APPROVE_OPTION) {
            File destinationFile = fileChooser.getSelectedFile();
            try {
                Files.copy(paySlipFile.toPath(), destinationFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
                JOptionPane.showMessageDialog(this,
                        "Fiche de paie téléchargée avec succès.\n" + destinationFile.getAbsolutePath(),
                        "Succès", JOptionPane.INFORMATION_MESSAGE);
            } catch (IOException ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(this,
                        "Erreur lors du téléchargement de la fiche de paie : " + ex.getMessage(),
                        "Erreur", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    /**
     * Recherche une fiche de paie spécifique en fonction du prénom, nom, mois et année.
     */
    private File findPaySlipFile(String prenom, String nom, int month, int year) {
        File dir = new File("resources/fiches_paie");
        if (!dir.exists() || !dir.isDirectory()) {
            return null;
        }

        String expectedFileName = prenom.toLowerCase() + "_" + nom.toLowerCase() +
                "_" + String.format("%02d", month) + "-" + year + "_fiche_paie.pdf";

        File[] matchingFiles = dir.listFiles((d, filename) -> filename.equalsIgnoreCase(expectedFileName));

        return (matchingFiles != null && matchingFiles.length > 0) ? matchingFiles[0] : null;
    }
}
