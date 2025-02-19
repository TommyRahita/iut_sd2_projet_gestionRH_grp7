package JavaProject;

import javax.swing.*;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.io.*;
import java.nio.file.*;
import java.util.*;
import java.util.List;

/**
 * Interface graphique pour les employés.
 * Permet de demander des congés et de télécharger les fiches de paie.
 */
public class EmployeeInterface extends JFrame {
    private Utilisateur employee;

    /**
     * Constructeur de la classe EmployeeInterface.
     * @param employee L'utilisateur connecté à l'interface.
     */
    public EmployeeInterface(Utilisateur employee) {
        this.employee = employee;
        initUI();
    }

    /**
     * Initialise l'interface employé.
     */
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

        // Vérifier les congés de l'employé
        checkEmployeeLeaveStatus();

        // Panel central avec les boutons
        JPanel buttonPanel = new JPanel(new GridLayout(2, 1, 10, 20));
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

    /**
     * Crée un bouton arrondi personnalisé.
     * @param text Le texte du bouton.
     * @return Un bouton personnalisé.
     */
    private JButton createRoundedButton(String text) {
        JButton button = new JButton(text);
        button.setBackground(new Color(255, 204, 0));
        button.setForeground(Color.BLACK);
        button.setFocusPainted(false);
        button.setBorder(new RoundBorder(15));
        return button;
    }

    /**
     * Ouvre l'interface de demande de congés.
     */
    private void openCongesRequestInterface() {
        new CongeRequest(this, employee);
    }

    private void openPaySlipSelectionDialog() {
        new PaySlipSelectionDialog(this, employee);
    }

    /**
     * Action de déconnexion de l'utilisateur.
     */
    private void logoutAction() {
        dispose();
        SwingUtilities.invokeLater(() -> {
            LoginFrame loginFrame = new LoginFrame();
            loginFrame.setVisible(true);
        });
    }

    private void checkEmployeeLeaveStatus() {
        File congeFile = new File("resources/conge.csv");
        List<String> lines = new ArrayList<>(); // Store all lines of the file

        try (BufferedReader br = new BufferedReader(new FileReader(congeFile))) {
            String line;
            while ((line = br.readLine()) != null) {
                lines.add(line); // Add each line to the list
                String[] columns = line.split(";");
                if (columns.length < 9) {
                    System.out.println("Ligne mal formatée : " + line);
                    continue;
                }

                String nom = columns[1];
                String prenom = columns[2];
                String statut = columns[6];
                String received = columns[8];

                if (prenom.equalsIgnoreCase(employee.prenom) && nom.equalsIgnoreCase(employee.nom)) {
                    if ("N".equals(received) && !"En attente".equals(statut)) {
                        String message = statut.equals("Refusé") ?
                                "Votre demande de congé a été refusée." :
                                "Votre demande de congé a été acceptée.";
                        int option = JOptionPane.showOptionDialog(this,
                                message,
                                "Statut de congé",
                                JOptionPane.DEFAULT_OPTION,
                                JOptionPane.INFORMATION_MESSAGE,
                                null,
                                new Object[]{"OK"},
                                "OK");

                        if (option == JOptionPane.OK_OPTION) {
                            columns[8] = "O";
                            // Update the line in the list:
                            int index = lines.indexOf(line); // Find the line to update
                            if (index != -1) {
                               lines.set(index, String.join(";", columns));
                            }


<<<<<<< HEAD

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
=======
                        }
                        break; // Employee found, exit loop.
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
>>>>>>> dfc319b08284a8da8f9316d52cb76ed32d7b2eb4
        }


        // Now update the file *outside* the reading block:
        if (!lines.isEmpty()) { // Only if there are lines to update
            try (BufferedWriter writer = new BufferedWriter(new FileWriter(congeFile))) { // Open ONLY ONCE
                for (String updatedLine : lines) {
                    writer.write(updatedLine);
                    writer.newLine();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void updateLeaveFile(File file, String oldLine, String newLine) {
        File tempFile = new File("resources/temp_conge.csv");

        try (BufferedReader reader = new BufferedReader(new FileReader(file));
             BufferedWriter writer = new BufferedWriter(new FileWriter(tempFile))) {

            String line;
            while ((line = reader.readLine()) != null) {
                if (line.equals(oldLine)) {
                    writer.write(newLine);
                } else {
                    writer.write(line);
                }
                writer.newLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            Files.move(tempFile.toPath(), file.toPath(), StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Classe interne pour la sélection et le téléchargement des fiches de paie
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
}
