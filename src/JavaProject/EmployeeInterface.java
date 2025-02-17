package JavaProject;

import javax.swing.*;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.Arrays;
import java.util.Comparator;

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

        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        topPanel.setBackground(new Color(43, 60, 70));
        JLabel nameLabel = new JLabel("Employee : " + employee.prenom + " " + employee.nom);
        nameLabel.setFont(new Font("Arial", Font.BOLD, 16));
        nameLabel.setForeground(Color.WHITE);
        nameLabel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        topPanel.add(nameLabel);
        add(topPanel, BorderLayout.NORTH);

        JPanel buttonPanel = new JPanel(new GridLayout(3, 1, 10, 20));
        buttonPanel.setBackground(Color.WHITE);
        buttonPanel.setBorder(new LineBorder(new Color(255, 255, 255), 35, true));

        JButton enterWorkedHours = createRoundedButton("Saisissez votre nombre d'heures travaillées ce mois-ci");
        enterWorkedHours.addActionListener(e -> openEnterWorkedHours());
        buttonPanel.add(enterWorkedHours);

        JButton congeRequestButton = createRoundedButton("Demander des congés");
        congeRequestButton.addActionListener(e -> openCongesRequestInterface());
        buttonPanel.add(congeRequestButton);

        JButton downloadPaySlipButton = createRoundedButton("Téléchargez votre fiche de paie");
        downloadPaySlipButton.addActionListener(e -> downloadPaySlip());
        buttonPanel.add(downloadPaySlipButton);

        UIManager.put("ComboBox.selectionBackground", new Color(255, 255, 255));
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

    private void openEnterWorkedHours() {
        new EnterWorkedHours(this, employee);
    }
    
    private void openCongesRequestInterface() {
        new CongeRequest(this, employee);
    }
    
    /**
     * Télécharge la dernière fiche de paie PDF de l'utilisateur.
     */
    private void downloadPaySlip() {
        // 1) Récupère le fichier PDF le plus récent correspondant à l'employé
        File lastPaySlip = findLastPaySlipFile(employee.prenom, employee.nom);
        if (lastPaySlip == null) {
            JOptionPane.showMessageDialog(this,
                    "Aucune fiche de paie trouvée pour " + employee.prenom + " " + employee.nom,
                    "Erreur", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // 2) Proposer à l'utilisateur de l'enregistrer
        JFileChooser fileChooser = new JFileChooser();
        // Nom par défaut du fichier
        fileChooser.setSelectedFile(new File(employee.prenom + "_" + employee.nom + "_fiche_paie.pdf"));

        int userChoice = fileChooser.showSaveDialog(this);
        if (userChoice == JFileChooser.APPROVE_OPTION) {
            File destinationFile = fileChooser.getSelectedFile();
            try {
                // 3) Copier la fiche de paie vers l'emplacement choisi
                Files.copy(lastPaySlip.toPath(), destinationFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
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
     * Recherche la dernière fiche de paie PDF correspondant au prénom + nom de l'utilisateur.
     * @param prenom Le prénom de l'utilisateur
     * @param nom Le nom de l'utilisateur
     * @return Le fichier le plus récent ou null si aucun fichier trouvé
     */
    private File findLastPaySlipFile(String prenom, String nom) {
        File dir = new File("resources/fiches_paie");
        if (!dir.exists() || !dir.isDirectory()) {
            return null;
        }

        // On suppose que le fichier PDF contient prenom_nom dans son nom, et se termine par "_fiche_paie.pdf"
        File[] matchingFiles = dir.listFiles((d, filename) -> {
            String lowerFile = filename.toLowerCase();
            String lowerPrenom = prenom.toLowerCase();
            String lowerNom = nom.toLowerCase();
            // Condition : le fichier contient "prenom_nom" et finit par "_fiche_paie.pdf"
            return lowerFile.contains(lowerPrenom + "_" + lowerNom) && lowerFile.endsWith("_fiche_paie.pdf");
        });

        if (matchingFiles == null || matchingFiles.length == 0) {
            return null;
        }

        // Trier par date de modification décroissante (le plus récent en premier)
        Arrays.sort(matchingFiles, Comparator.comparingLong(File::lastModified).reversed());

        // matchingFiles[0] est donc le plus récent
        return matchingFiles[0];
    }

    private void logoutAction() {
        dispose();
        SwingUtilities.invokeLater(() -> {
            LoginFrame loginFrame = new LoginFrame();
            loginFrame.setVisible(true);
        });
    }
}
