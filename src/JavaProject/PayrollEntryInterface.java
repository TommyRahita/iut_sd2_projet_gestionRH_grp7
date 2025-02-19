package JavaProject;

import javax.swing.*;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.io.*;
import java.time.LocalDate;
import java.util.List;

/**
 * Classe représentant l'interface de saisie de la paie d'un employé.
 */
public class PayrollEntryInterface extends JFrame {
    private JTextField heuresTravailField, primesField, cotisationsField, impotsField;
    private JLabel userInfoLabel, salaireBrutLabel, salaireNetLabel;
    private String selectedUser;
    private Utilisateur utilisateur;
    private double tauxHoraire;
    private JFrame parent; // Stocke la fenêtre précédente

    /**
     * Constructeur de l'interface de saisie de la paie.
     * @param parent La fenêtre précédente.
     * @param selectedUser L'utilisateur sélectionné pour la saisie de la paie.
     */
    public PayrollEntryInterface(JFrame parent, String selectedUser) {
        this.parent = parent;
        this.selectedUser = selectedUser;
        this.utilisateur = getUtilisateurInfo(selectedUser);
        this.tauxHoraire = getTauxHoraire(utilisateur.poste);

        setTitle("Saisie de la paie pour " + selectedUser);
        setSize(800, 500);
        setLayout(new BorderLayout());
        getContentPane().setBackground(new Color(29, 46, 56));
        setResizable(false);

        JPanel formPanel = new JPanel();
        formPanel.setLayout(new GridLayout(7, 2, 10, 10));
        formPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        formPanel.setBackground(new Color(43, 60, 70));

        userInfoLabel = new JLabel("Employé : " + selectedUser + " | Poste : " + utilisateur.poste + " | Taux horaire : " + tauxHoraire + " €/h");
        userInfoLabel.setForeground(Color.WHITE);
        userInfoLabel.setHorizontalAlignment(SwingConstants.CENTER);
        add(userInfoLabel, BorderLayout.NORTH);

        JLabel heuresTravailLabel = createStyledLabel("Heures travaillées :");
        heuresTravailField = createStyledTextField();
        formPanel.add(heuresTravailLabel);
        formPanel.add(heuresTravailField);

        JLabel primesLabel = createStyledLabel("Primes (€) :");
        primesField = createStyledTextField();
        formPanel.add(primesLabel);
        formPanel.add(primesField);

        JLabel cotisationsLabel = createStyledLabel("Cotisations (€) :");
        cotisationsField = createStyledTextField();
        formPanel.add(cotisationsLabel);
        formPanel.add(cotisationsField);

        JLabel impotsLabel = createStyledLabel("Impôts (€) :");
        impotsField = createStyledTextField();
        formPanel.add(impotsLabel);
        formPanel.add(impotsField);

        salaireBrutLabel = createStyledLabel("Salaire Brut : -");
        formPanel.add(salaireBrutLabel);
        salaireNetLabel = createStyledLabel("Salaire Net : -");
        formPanel.add(salaireNetLabel);

        JPanel buttonPanel = new JPanel(new GridLayout(2, 1, 10, 10)); // 2 lignes pour centrer le bouton "Retour"
        buttonPanel.setBackground(new Color(43, 60, 70));

        // Panel contenant les boutons "Enregistrer" et "Générer fiche de paie"
        JPanel topButtons = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
        topButtons.setBackground(new Color(43, 60, 70));

        JButton saveButton = createRoundedButton("Enregistrer la paie");
        saveButton.addActionListener(e -> enregistrerPaie());
        topButtons.add(saveButton);

        JButton generateButton = createRoundedButton("Générer fiche de paie");
        generateButton.addActionListener(e -> {
            Manager.genererFichePaie(this, selectedUser);
        });
        topButtons.add(generateButton);

        // Bouton retour centré en bas
        JPanel bottomButton = new JPanel(new FlowLayout(FlowLayout.CENTER));
        bottomButton.setBackground(new Color(43, 60, 70));

        JButton backButton = createRoundedButton("Retour");
        backButton.addActionListener(e -> {
            if (parent != null) {
                parent.setVisible(true);
            }
            dispose();
        });
        bottomButton.add(backButton);

        // Ajout des boutons au panel principal
        buttonPanel.add(topButtons);
        buttonPanel.add(bottomButton);

        add(formPanel, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);

        // Forcer l'affichage correct
        revalidate();
        repaint();

        setLocationRelativeTo(null);
        setVisible(true);
        if (parent != null) parent.setVisible(false);
    }

    /**
     * Enregistre les données de paie saisies par l'utilisateur.
     */
    private void enregistrerPaie() {
        try {
            int heuresTravail = Integer.parseInt(heuresTravailField.getText().trim());
            double primes = Double.parseDouble(primesField.getText().trim().replace(",", "."));
            double cotisations = Double.parseDouble(cotisationsField.getText().trim().replace(",", "."));
            double impots = Double.parseDouble(impotsField.getText().trim().replace(",", "."));

            double salaireBrut = tauxHoraire * heuresTravail;
            double salaireNet = salaireBrut + primes - cotisations - impots;

            salaireBrutLabel.setText("Salaire Brut : " + String.format("%.2f €", salaireBrut));
            salaireNetLabel.setText("Salaire Net : " + String.format("%.2f €", salaireNet));

            int nextId = getNextPaieId();
            LocalDate currentDate = LocalDate.now();

            String newPaie = String.format("%d;%d;%d;%d;%s;%s;%s;%.2f;%d;%d;%.2f;%.2f;%.2f;%.2f",
                    nextId, currentDate.getMonthValue(), currentDate.getYear(),
                    utilisateur.id, utilisateur.nom, utilisateur.prenom, utilisateur.poste,
                    tauxHoraire, heuresTravail, utilisateur.jours_conge_restants,
                    salaireBrut, primes, cotisations, impots);

            File file = new File("resources/paie.csv");
            boolean isEmpty = file.length() == 0;

            try (FileWriter fw = new FileWriter(file, true);
                 BufferedWriter bw = new BufferedWriter(fw);
                 PrintWriter out = new PrintWriter(bw)) {

                if (isEmpty) {
                    out.println("idPaie;moisPaie;anneePaie;idUtilisateur;nom;prenom;poste;taux_horaire;nb_heure_travaille;nb_conge;salaire;primes;cotisations;impots");
                }

                out.println(newPaie);
            }

            JOptionPane.showMessageDialog(this, "Fiche de paie enregistrée avec succès !");
            dispose();
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Veuillez entrer des valeurs valides.", "Erreur", JOptionPane.ERROR_MESSAGE);
        } catch (IOException ex) {
            JOptionPane.showMessageDialog(this, "Erreur lors de l'enregistrement de la paie.", "Erreur", JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * Récupère le prochain ID de paie à enregistrer.
     * @return Le prochain ID de paie disponible.
     */
    private int getNextPaieId() {
        int maxId = 0;
        File file = new File("resources/paie.csv");

        if (!file.exists() || file.length() == 0) {
            return 1;
        }

        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line;
            br.readLine();
            while ((line = br.readLine()) != null) {
                String[] columns = line.split(";");
                if (columns.length > 0 && !columns[0].trim().isEmpty()) {
                    try {
                        int currentId = Integer.parseInt(columns[0].trim());
                        if (currentId > maxId) {
                            maxId = currentId;
                        }
                    } catch (NumberFormatException e) {
                        System.err.println("Erreur conversion ID paie : " + columns[0]);
                    }
                }
            }
        } catch (IOException e) {
            System.err.println("Impossible de lire le fichier paie.csv : " + e.getMessage());
        }

        return maxId + 1;
    }
    
    /**
     * Récupère le taux horaire en fonction du poste depuis le fichier CSV
     */
    private double getTauxHoraire(String poste) {
        String path = "resources/taux_horraire_poste.csv";
        try (BufferedReader br = new BufferedReader(new FileReader(path))) {
            br.readLine(); // Ignorer l'en-tête
            String ligne;
            while ((ligne = br.readLine()) != null) {
                String[] parts = ligne.split(";");
                if (parts.length == 2 && parts[0].trim().equalsIgnoreCase(poste.trim())) {
                    return Double.parseDouble(parts[1].trim().replace(",", "."));
                }
            }
        } catch (IOException | NumberFormatException e) {
            System.err.println("Erreur lecture fichier taux_horraire_poste.csv : " + e.getMessage());
        }
        return 0.0; // Valeur par défaut en cas d'erreur
    }
    
    /**
     * Récupère le taux horaire correspondant à un poste donné.
     * @param poste Le poste de l'utilisateur.
     * @return Le taux horaire en euros.
     */
    private Utilisateur getUtilisateurInfo(String selectedUser) {
        List<Utilisateur> users = Utilisateur.func_recup_data("resources/Utilisateurs.csv");
        return users.stream()
                .filter(u -> (u.prenom + " " + u.nom).equalsIgnoreCase(selectedUser))
                .findFirst()
                .orElse(new Utilisateur(0, "Inconnu", "Inconnu", "Aucun", 0, "", ""));
    }
    
    /**
     * Récupère les informations d'un utilisateur à partir de son nom.
     * @param selectedUser Le nom de l'utilisateur sélectionné.
     * @return Un objet Utilisateur contenant ses informations.
     */
    private JButton createRoundedButton(String text) {
        JButton button = new JButton(text);
        button.setBackground(new Color(255, 204, 0));
        button.setForeground(Color.BLACK);
        return button;
    }
    
    
    /**
     * Crée un label stylisé avec une police et une couleur définies.
     * @param text Le texte du label.
     * @return Un objet JLabel stylisé.
     */
    private JLabel createStyledLabel(String text) {
        JLabel label = new JLabel(text);
        label.setFont(new Font("Arial", Font.BOLD, 14));
        label.setForeground(Color.WHITE);
        return label;
    }

    /**
     * Crée un champ de texte stylisé avec une couleur de fond et une bordure.
     * @return Un objet JTextField stylisé.
     */
    private JTextField createStyledTextField() {
        JTextField textField = new JTextField();
        textField.setBackground(Color.LIGHT_GRAY);
        textField.setForeground(Color.BLACK);
        textField.setBorder(new LineBorder(Color.GRAY, 1, true));
        return textField;
    }

}
