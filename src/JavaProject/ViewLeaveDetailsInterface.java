package JavaProject;

import javax.swing.*;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.awt.event.*;

public class ViewLeaveDetailsInterface extends JFrame {
    private static final long serialVersionUID = 1L;

    // Déclaration des variables
    private String statut;
    private String motifRefus;

    // Constructeur
    public ViewLeaveDetailsInterface(JFrame parent, String id, String nom, String prenom, String dateDebut, String dateFin, String nbPrisConge, String statut, String motifRefus) {
        this.statut = statut;
        this.motifRefus = motifRefus;

        setTitle("Détails du Congé");
        setSize(800, 500);
        setLayout(new BorderLayout());
        getContentPane().setBackground(new Color(29, 46, 56));

        // Titre
        JLabel titleLabel = new JLabel("Détails du Congé");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 18));
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);

        // Informations du congé
        JPanel infoPanel = new JPanel();
        infoPanel.setLayout(new GridLayout(9, 2));
        infoPanel.setBackground(new Color(43, 60, 70));

        infoPanel.add(createStyledLabel("ID :"));
        infoPanel.add(new JLabel(id));

        infoPanel.add(createStyledLabel("Nom :"));
        infoPanel.add(new JLabel(nom));

        infoPanel.add(createStyledLabel("Prénom :"));
        infoPanel.add(new JLabel(prenom));

        infoPanel.add(createStyledLabel("Date Début :"));
        infoPanel.add(new JLabel(dateDebut));

        infoPanel.add(createStyledLabel("Date Fin :"));
        infoPanel.add(new JLabel(dateFin));

        infoPanel.add(createStyledLabel("Nombre de Jours Pris :"));
        infoPanel.add(new JLabel(nbPrisConge));

        infoPanel.add(createStyledLabel("Statut :"));
        infoPanel.add(new JLabel(statut));

        infoPanel.add(createStyledLabel("Motif de Refus :"));
        JTextField motifField = new JTextField(motifRefus);
        infoPanel.add(motifField);

        // Ajouter le panneau d'informations et le titre
        add(titleLabel, BorderLayout.NORTH);
        add(infoPanel, BorderLayout.CENTER);

        // Panneau des boutons (Accepter et Refuser)
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 20, 10));
        buttonPanel.setBackground(new Color(43, 60, 70));

        JButton acceptButton = createRoundedButton("Accepter");
        acceptButton.addActionListener(e -> {
            // Mettre à jour le statut en "Accepte"
            setStatut("Accepte"); // Utilisation du setter pour modifier statut
            JOptionPane.showMessageDialog(this, "Congé accepté.");
        });

        JButton rejectButton = createRoundedButton("Refuser");
        rejectButton.addActionListener(e -> {
            // Mettre à jour le statut en "Refusé" et modifier le motif de refus
            setStatut("Refusé"); // Utilisation du setter pour modifier statut
            String motif = motifField.getText();
            if (motif.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Un motif de refus est obligatoire.");
                return;
            }
            setMotifRefus(motif); // Utilisation du setter pour modifier motifRefus
            JOptionPane.showMessageDialog(this, "Congé refusé avec motif : " + motif);
        });

        buttonPanel.add(acceptButton);
        buttonPanel.add(rejectButton);

        // Ajouter les boutons en bas
        add(buttonPanel, BorderLayout.SOUTH);

        setLocationRelativeTo(null);
        setVisible(true);
    }

    // Méthode pour créer des labels stylisés
    private JLabel createStyledLabel(String text) {
        JLabel label = new JLabel(text);
        label.setFont(new Font("Arial", Font.BOLD, 14));
        label.setForeground(Color.WHITE);
        return label;
    }

    // Méthode pour créer des boutons arrondis
    private JButton createRoundedButton(String text) {
        JButton button = new JButton(text);
        button.setBackground(new Color(255, 204, 0));
        button.setForeground(Color.BLACK);
        button.setFocusPainted(false);
        button.setBorder(new LineBorder(new Color(255, 204, 0), 4, true));
        return button;
    }

    // Getter pour statut
    public String getStatut() {
        return statut;
    }

    // Setter pour statut
    public void setStatut(String statut) {
        this.statut = statut;
    }

    // Getter pour motifRefus
    public String getMotifRefus() {
        return motifRefus;
    }

    // Setter pour motifRefus
    public void setMotifRefus(String motifRefus) {
        this.motifRefus = motifRefus;
    }

    public static void main(String[] args) {
        new ViewLeaveDetailsInterface(null, "123", "Doe", "John", "01/01/2025", "07/01/2025", "5", "En attente", "Motif refus");
    }
}
