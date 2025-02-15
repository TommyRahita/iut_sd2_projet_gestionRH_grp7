package JavaProject;

import java.awt.*;
import javax.swing.*;
import javax.swing.border.LineBorder;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

class EnterWorkedHours extends JFrame {
    private JFrame parent;
    private Utilisateur utilisateur;

    public EnterWorkedHours(JFrame parent, Utilisateur utilisateur) {
        this.parent = parent;
        this.utilisateur = utilisateur;
        setTitle("Saisie des heures travaillées");
        setSize(600, 300);
        setLayout(new BorderLayout());
        getContentPane().setBackground(new Color(29, 46, 56));
        setResizable(false);
        
        try {
            ImageIcon icon = new ImageIcon("resources\\icon.png");
            setIconImage(icon.getImage());
        } catch (Exception e) {
            System.out.println("Erreur lors du chargement de l'icône: " + e.getMessage());
        }

        JPanel formPanel = new JPanel(new GridLayout(2, 1, 10, 10));
        formPanel.setBorder(BorderFactory.createEmptyBorder(30, 30, 30, 30));
        formPanel.setBackground(new Color(43, 60, 70));

        JLabel hoursLabel = createStyledLabel("Entrez vos heures travaillées ce mois-ci :");
        JTextField hoursField = createStyledTextField();

        hoursField.addKeyListener(new KeyAdapter() {
            @Override
            public void keyTyped(KeyEvent e) {
                char c = e.getKeyChar();
                if (!Character.isDigit(c)) {
                    e.consume();
                }
            }
        });

        formPanel.add(hoursLabel);
        formPanel.add(hoursField);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
        buttonPanel.setBackground(new Color(43, 60, 70));

        JButton validateButton = createRoundedButton("Valider");
        validateButton.addActionListener(e -> {
            String workedHours = hoursField.getText();
            if (workedHours.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Veuillez entrer un nombre d'heures valide.", "Erreur", JOptionPane.ERROR_MESSAGE);
            } else {
                enregistrerPaie(Integer.parseInt(workedHours));
                JOptionPane.showMessageDialog(this, "Nombre d'heures enregistrées : " + workedHours);
                parent.setVisible(true);
                dispose();
            }
        });
        buttonPanel.add(validateButton);

        JButton backButton = createRoundedButton("Retour");
        backButton.addActionListener(e -> {
            parent.setVisible(true);
            dispose();
        });
        buttonPanel.add(backButton);

        add(formPanel, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);
        setLocationRelativeTo(null);
        setVisible(true);
        parent.setVisible(false);
        System.out.println("Test : " + utilisateur.id);
    }

    private void enregistrerPaie(int workedHours) {
        try {
            List<String> paieLines = Files.readAllLines(Paths.get("resources\\paie.csv"));
            int idPaie = paieLines.size();
            LocalDate today = LocalDate.now();
            int moisPaie = today.getMonthValue();
            int anneePaie = today.getYear();

            List<String> tauxLines = Files.readAllLines(Paths.get("resources\\taux_horraire_poste.csv"));
            double tauxHoraire = tauxLines.stream()
                .map(line -> line.split(";"))
                .filter(parts -> parts[0].equalsIgnoreCase(utilisateur.poste))
                .mapToDouble(parts -> Double.parseDouble(parts[1].replace(",", ".")))
                .findFirst()
                .orElse(0.0);
            
            List<String> congeLines = Files.readAllLines(Paths.get("resources\\conge.csv"));
            int nbConges = (int) congeLines.stream()
                .map(line -> line.split(";"))
                .filter(parts -> parts[1].equalsIgnoreCase(utilisateur.nom) && parts[2].equalsIgnoreCase(utilisateur.prenom))
                .filter(parts -> {
                    LocalDate debut = LocalDate.parse(parts[3], DateTimeFormatter.ofPattern("dd/MM/yyyy"));
                    LocalDate fin = LocalDate.parse(parts[4], DateTimeFormatter.ofPattern("dd/MM/yyyy"));
                    return (debut.getMonthValue() == moisPaie && debut.getYear() == anneePaie) ||
                           (fin.getMonthValue() == moisPaie && fin.getYear() == anneePaie);
                })
                .mapToInt(parts -> Integer.parseInt(parts[5]))
                .sum();
            
            try (BufferedWriter writer = new BufferedWriter(new FileWriter("resources\\paie.csv", true))) {
                writer.write(String.format("%d;%d;%d;%d;%s;%s;%s;%.2f;%d;%d;%.2f\n",
                    idPaie, moisPaie, anneePaie, utilisateur.id, utilisateur.nom, utilisateur.prenom,
                    utilisateur.poste, tauxHoraire, workedHours, nbConges, tauxHoraire * (workedHours - nbConges)));
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    private JLabel createStyledLabel(String text) {
        JLabel label = new JLabel(text);
        label.setFont(new Font("Arial", Font.BOLD, 14));
        label.setForeground(Color.WHITE);
        return label;
    }

    private JTextField createStyledTextField() {
        JTextField textField = new JTextField();
        textField.setBackground(Color.LIGHT_GRAY);
        textField.setForeground(Color.BLACK);
        textField.setBorder(new LineBorder(Color.GRAY, 1, true));
        return textField;
    }

    private JButton createRoundedButton(String text) {
        JButton button = new JButton(text);
        button.setBackground(new Color(255, 204, 0));
        button.setForeground(Color.BLACK);
        button.setFocusPainted(false);
        button.setBorder(new RoundBorder(15));
        return button;
    }
}
