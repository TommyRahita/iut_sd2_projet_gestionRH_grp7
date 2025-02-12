package JavaProject;

import java.awt.*;
import javax.swing.*;
import javax.swing.border.LineBorder;

class SalaryManagementInterface extends JFrame {
   public SalaryManagementInterface(JFrame parent) {
       setTitle("Menu de Management des Salaires");
       setSize(800, 500);
       setLayout(new BorderLayout());
       getContentPane().setBackground(new Color(29, 46, 56)); // Couleur de fond similaire
       
   	try {
	    // Charger l'image depuis le dossier 'resources' dans le projet
		ImageIcon icon = new ImageIcon("resources\\icon.png");
	    setIconImage(icon.getImage()); // Définir l'icône de la fenêtre
	} catch (Exception e) {
	    System.out.println("Erreur lors du chargement de l'icône: " + e.getMessage());
	}

       // Panneau principal
       JPanel formPanel = new JPanel();
       formPanel.setLayout(new GridBagLayout()); // Utilisation de GridBagLayout pour centrer
       formPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20)); // Marges autour du formulaire
       formPanel.setBackground(new Color(43, 60, 70)); // Couleur de fond du panneau principal

       GridBagConstraints gbc = new GridBagConstraints();
       gbc.gridx = 0;
       gbc.gridy = 0;
       gbc.insets = new Insets(10, 10, 10, 10);
       gbc.anchor = GridBagConstraints.CENTER;

       // Choix multiples : Sélection d'un utilisateur
       JLabel choiceLabel1 = createStyledLabel("Veuillez choisir un utilisateur");
       formPanel.add(choiceLabel1, gbc);

       gbc.gridy++;
       JComboBox<String> choiceBox1 = new JComboBox<>(new String[]{"à implémenter", "Prout", "Lallalala"});
       choiceBox1.setPreferredSize(new Dimension(200, 30));
       choiceBox1.setBackground(new Color(255, 204, 0)); // Bouton jaune
       choiceBox1.setForeground(Color.BLACK);
       formPanel.add(choiceBox1, gbc);

       // Panneau pour les boutons
       JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
       buttonPanel.setBackground(new Color(43, 60, 70)); // Couleur de fond du panneau de boutons

       // Bouton "Calculer le salaire"
       JButton calculateSalaryButton = createRoundedButton("Calculer le salaire");
       buttonPanel.add(calculateSalaryButton);

       // Bouton "Générer fiche de paie"
       JButton generatePayslipButton = createRoundedButton("Générer fiche de paie");
       buttonPanel.add(generatePayslipButton);

       // Ajouter le panneau des boutons sous la liste déroulante
       gbc.gridy++;
       formPanel.add(buttonPanel, gbc);

       // Ajouter le formulaire à la fenêtre principale
       add(formPanel, BorderLayout.CENTER);

       // Panneau pour le bouton "Retour" tout en bas
       JPanel returnButtonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
       returnButtonPanel.setBackground(new Color(43, 60, 70));

       JButton returnButton = createRoundedButton("Retour");
       returnButton.addActionListener(e -> {
           parent.setVisible(true); // Rendre l'interface principale visible
           dispose(); // Fermer cette fenêtre
       });
       returnButtonPanel.add(returnButton);

       add(returnButtonPanel, BorderLayout.SOUTH); // Placer le bouton "Retour" en bas

       setLocationRelativeTo(null); // Centrer la fenêtre sur l'écran
       setVisible(true);

       // Masquer l'interface principale pendant que cette fenêtre est ouverte
       parent.setVisible(false);
   }

   // Méthodes pour styliser les composants
   private JLabel createStyledLabel(String text) {
       JLabel label = new JLabel(text);
       label.setFont(new Font("Arial", Font.BOLD, 14));
       label.setForeground(Color.WHITE); // Texte blanc
       return label;
   }

   private JButton createRoundedButton(String text) {
       JButton button = new JButton(text);
       button.setBackground(new Color(255, 204, 0)); // Bouton jaune
       button.setForeground(Color.BLACK);
       button.setFocusPainted(false);
       button.setBorder(new LineBorder(new Color(255, 204, 0), 4, true)); // Bordure arrondie
       return button;
   }
}
