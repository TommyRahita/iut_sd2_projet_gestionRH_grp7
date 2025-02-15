package JavaProject;

import javax.swing.*;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.io.*;
import java.util.List;
import java.util.stream.Collectors;
import com.itextpdf.kernel.pdf.*;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Paragraph;

public class SalaryManagementInterface extends JFrame {
    private JFrame parent;
    private JComboBox<String> choiceBox1;
    private DefaultComboBoxModel<String> comboBoxModel;
    private JTextField searchField;
    private JLabel salaryLabel;

    public SalaryManagementInterface(JFrame parent) {
        this.parent = parent;
        setTitle("Menu de Management des Salaires");
        setSize(800, 500);
        setLayout(new BorderLayout());
        getContentPane().setBackground(new Color(29, 46, 56));

        try {
            ImageIcon icon = new ImageIcon("resources\\icon.png");
            setIconImage(icon.getImage());
        } catch (Exception e) {
            System.out.println("Erreur lors du chargement de l'icône: " + e.getMessage());
        }

        JPanel formPanel = new JPanel();
        formPanel.setLayout(new GridBagLayout());
        formPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        formPanel.setBackground(new Color(43, 60, 70));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.anchor = GridBagConstraints.CENTER;

        JLabel choiceLabel1 = createStyledLabel("Veuillez choisir un utilisateur");
        formPanel.add(choiceLabel1, gbc);

        gbc.gridy++;
        searchField = new JTextField(20);
        formPanel.add(searchField, gbc);

        gbc.gridy++;
        comboBoxModel = new DefaultComboBoxModel<>();
        choiceBox1 = new JComboBox<>(comboBoxModel);
        choiceBox1.setPreferredSize(new Dimension(200, 30));
        formPanel.add(choiceBox1, gbc);

        chargerUtilisateurs();

        searchField.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                filtrerUtilisateurs(searchField.getText().trim());
            }
        });

        gbc.gridy++;
        salaryLabel = createStyledLabel("Salaire: Non calculé");
        formPanel.add(salaryLabel, gbc);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
        JButton calculateSalaryButton = createRoundedButton("Calculer le salaire");
        calculateSalaryButton.addActionListener(e -> calculerSalaire());

        JButton generatePayslipButton = createRoundedButton("Générer fiche de paie");
        generatePayslipButton.addActionListener(e -> genererFichePaie());

        buttonPanel.add(calculateSalaryButton);
        buttonPanel.add(generatePayslipButton);

        gbc.gridy++;
        formPanel.add(buttonPanel, gbc);

        add(formPanel, BorderLayout.CENTER);

        JPanel returnButtonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        JButton returnButton = createRoundedButton("Retour");
        returnButton.addActionListener(e -> {
            parent.setVisible(true);
            dispose();
        });
        returnButtonPanel.add(returnButton);
        add(returnButtonPanel, BorderLayout.SOUTH);

        setLocationRelativeTo(null);
        setVisible(true);
        parent.setVisible(false);
    }

    // Méthodes pour styliser les composants
    private JLabel createStyledLabel(String text) {
        JLabel label = new JLabel(text);
        label.setFont(new Font("Arial", Font.BOLD, 14));
        label.setForeground(Color.WHITE);
        return label;
    }

    private JButton createRoundedButton(String text) {
        JButton button = new JButton(text);
        button.setBackground(new Color(255, 204, 0));
        button.setForeground(Color.BLACK);
        button.setFocusPainted(false);
        button.setBorder(new LineBorder(new Color(255, 204, 0), 4, true));
        return button;
    }

    private void chargerUtilisateurs() {
        List<Utilisateur> utilisateurs = Utilisateur.func_recup_data("resources/Utilisateurs.csv");

        List<String> nomsUtilisateurs = utilisateurs.stream()
                .map(user -> user.prenom + " " + user.nom)
                .limit(5)
                .collect(Collectors.toList());

        comboBoxModel.removeAllElements();
        for (String nom : nomsUtilisateurs) {
            comboBoxModel.addElement(nom);
        }
    }

    private void filtrerUtilisateurs(String recherche) {
        List<Utilisateur> utilisateurs = Utilisateur.func_recup_data("resources/Utilisateurs.csv");

        List<String> resultats = utilisateurs.stream()
                .map(user -> user.prenom + " " + user.nom)
                .filter(nom -> nom.toLowerCase().contains(recherche.toLowerCase()))
                .limit(5)
                .collect(Collectors.toList());

        comboBoxModel.removeAllElements();
        for (String nom : resultats) {
            comboBoxModel.addElement(nom);
        }
    }

    private void calculerSalaire() {
        String selectedUser = (String) choiceBox1.getSelectedItem();
        if (selectedUser == null || selectedUser.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Veuillez sélectionner un utilisateur.", "Erreur", JOptionPane.ERROR_MESSAGE);
            return;
        }

        double totalSalaire = 0.0;
        double totalPrimes = 0.0;
        double totalCotisations = 0.0;
        double totalImpots = 0.0;

        try (BufferedReader br = new BufferedReader(new FileReader("resources/paie.csv"))) {
            String ligne;
            boolean userFound = false;
            
            while ((ligne = br.readLine()) != null) {
                String[] colonne = ligne.split(";");
                
                // Vérifier que la ligne contient toutes les données nécessaires
                if (colonne.length < 14) {
                    System.out.println("Ligne ignorée : " + ligne);
                    continue;
                }

                String nom = colonne[4].trim();
                String prenom = colonne[5].trim();
                String salaireStr = colonne[10].trim().replace(",", ".");
                String primesStr = colonne[11].trim().replace(",", ".");
                String cotisationsStr = colonne[12].trim().replace(",", ".");
                String impotsStr = colonne[13].trim().replace(",", ".");

                if ((prenom + " " + nom).equalsIgnoreCase(selectedUser)) {
                    userFound = true;

                    try {
                        double salaire = Double.parseDouble(salaireStr);
                        double primes = Double.parseDouble(primesStr);
                        double cotisations = Double.parseDouble(cotisationsStr);
                        double impots = Double.parseDouble(impotsStr);

                        totalSalaire += salaire;
                        totalPrimes += primes;
                        totalCotisations += cotisations;
                        totalImpots += impots;

                    } catch (NumberFormatException e) {
                        System.out.println("Erreur parsing salaire/primes/cotisations/impots : " + ligne);
                    }
                }
            }

            if (!userFound) {
                JOptionPane.showMessageDialog(this, "Aucune paie trouvée pour cet utilisateur.", "Erreur", JOptionPane.ERROR_MESSAGE);
                return;
            }

            double netAPayer = totalSalaire + totalPrimes - totalCotisations - totalImpots;
            salaryLabel.setText(String.format("Salaire Net: %.2f €", netAPayer));
            System.out.println("Salaire Net pour " + selectedUser + " : " + netAPayer);

        } catch (IOException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Erreur lors de la lecture du fichier de paie.", "Erreur", JOptionPane.ERROR_MESSAGE);
        }
    }


    private void genererFichePaie() {
        String selectedUser = (String) choiceBox1.getSelectedItem();
        if (selectedUser == null || selectedUser.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Veuillez sélectionner un utilisateur.", "Erreur", JOptionPane.ERROR_MESSAGE);
            return;
        }

        String moisPaie = "";
        String anneePaie = "";
        double salaireBrut = 0.0, primes = 0.0, cotisations = 0.0, impots = 0.0;

        try (BufferedReader br = new BufferedReader(new FileReader("resources/paie.csv"))) {
            String ligne;
            boolean userFound = false;
            
            while ((ligne = br.readLine()) != null) {
                String[] colonne = ligne.split(";");
                
                if (colonne.length < 14) continue;

                String nom = colonne[4].trim();
                String prenom = colonne[5].trim();
                
                if ((prenom + " " + nom).equalsIgnoreCase(selectedUser)) {
                    userFound = true;
                    
                    moisPaie = colonne[1].trim();
                    anneePaie = colonne[2].trim();
                    salaireBrut += Double.parseDouble(colonne[10].replace(",", "."));
                    primes += Double.parseDouble(colonne[11].replace(",", "."));
                    cotisations += Double.parseDouble(colonne[12].replace(",", "."));
                    impots += Double.parseDouble(colonne[13].replace(",", "."));
                }
            }

            if (!userFound) {
                JOptionPane.showMessageDialog(this, "Aucune paie trouvée pour cet utilisateur.", "Erreur", JOptionPane.ERROR_MESSAGE);
                return;
            }

        } catch (IOException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Erreur lors de la lecture du fichier de paie.", "Erreur", JOptionPane.ERROR_MESSAGE);
            return;
        }

        double salaireNet = salaireBrut + primes - cotisations - impots;
        String nomFichier = String.format("resources/fiches_paie/%s_%s_%s_fiche_paie.pdf", selectedUser.replace(" ", "_"), moisPaie, anneePaie);

        try {
            PdfWriter writer = new PdfWriter(nomFichier);
            PdfDocument pdf = new PdfDocument(writer);
            Document document = new Document(pdf);

            document.add(new Paragraph("BULLETIN DE PAIE").setBold().setFontSize(18));
            document.add(new Paragraph(String.format("Employé : %s", selectedUser)).setFontSize(14));
            document.add(new Paragraph(String.format("Mois de paie : %s %s", moisPaie, anneePaie)).setFontSize(14));
            document.add(new Paragraph("------------------------------------------------------"));
            document.add(new Paragraph(String.format("Salaire Brut : %.2f €", salaireBrut)).setFontSize(12));
            document.add(new Paragraph(String.format("Primes : %.2f €", primes)).setFontSize(12));
            document.add(new Paragraph(String.format("Cotisations Sociales : -%.2f €", cotisations)).setFontSize(12));
            document.add(new Paragraph(String.format("Impôt sur le revenu : -%.2f €", impots)).setFontSize(12));
            document.add(new Paragraph("------------------------------------------------------"));
            document.add(new Paragraph(String.format("Salaire Net à Payer : %.2f €", salaireNet)).setFontSize(14).setBold());

            document.close();
            JOptionPane.showMessageDialog(this, "Fiche de paie générée pour " + selectedUser, "Succès", JOptionPane.INFORMATION_MESSAGE);

        } catch (IOException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Erreur lors de la génération du PDF.", "Erreur", JOptionPane.ERROR_MESSAGE);
        }
    }


}
