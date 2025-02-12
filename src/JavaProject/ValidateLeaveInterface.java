package JavaProject;

import java.awt.*;
import javax.swing.*;
import javax.swing.border.LineBorder;

// Interface de validation des congés
class ValidateLeaveInterface extends JFrame {
	   public ValidateLeaveInterface(JFrame parent) {
	       setTitle("Valider les Congés");
	       setSize(800, 500);
	       setLayout(new BorderLayout());
	       getContentPane().setBackground(new Color(29, 46, 56));
	       
	    	try {
	    	    // Charger l'image depuis le dossier 'resources' dans le projet
	    		ImageIcon icon = new ImageIcon("resources\\icon.png");
	    	    setIconImage(icon.getImage()); // Définir l'icône de la fenêtre
	    	} catch (Exception e) {
	    	    System.out.println("Erreur lors du chargement de l'icône: " + e.getMessage());
	    	}

	       // ---- Panneau principal ----
	       JPanel formPanel = new JPanel();
	       formPanel.setLayout(new GridBagLayout()); // Utilisation de GridBagLayout pour le centrage
	       formPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
	       formPanel.setBackground(new Color(43, 60, 70));
	       setResizable(false);

	       GridBagConstraints gbc = new GridBagConstraints();
	       gbc.gridx = 0;
	       gbc.gridy = 0;
	       gbc.insets = new Insets(10, 10, 10, 10);
	       gbc.anchor = GridBagConstraints.CENTER;

	       // Choix multiples : Sélection d'un utilisateur
	       JLabel choiceLabel1 = createStyledLabel("Veuillez choisir un utilisateur");
	       formPanel.add(choiceLabel1, gbc);

	       gbc.gridy++;
	       JComboBox<String> choiceBox1 = new JComboBox<>(new String[]{"à implémenter", "TEST 1 2 1 2", "Lallalala"});
	       choiceBox1.setPreferredSize(new Dimension(200, 30));
	       choiceBox1.setBackground(new Color(255, 204, 0));
	       choiceBox1.setForeground(Color.BLACK);
	       formPanel.add(choiceBox1, gbc);

	       // ---- Panneau des boutons (Accepter et Refuser) ----
	       JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
	       buttonPanel.setBackground(new Color(43, 60, 70));

	       // Bouton "Accepter"
	       JButton acceptButton = createRoundedButton("Accepter");
	       acceptButton.addActionListener(e -> {
	           JOptionPane.showMessageDialog(this, "Congé accepté.");
	       });
	       buttonPanel.add(acceptButton);

	       // Bouton "Refuser"
	       JButton rejectButton = createRoundedButton("Refuser");
	       rejectButton.addActionListener(e -> {
	           new RefuseLeaveInterface(this); // L'interface de refus
	       });
	       buttonPanel.add(rejectButton);

	       // Ajouter le panneau des boutons sous la liste déroulante
	       gbc.gridy++;
	       formPanel.add(buttonPanel, gbc);

	       // ---- Panneau du bouton "Retour" ----
	       JPanel returnButtonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
	       returnButtonPanel.setBackground(new Color(43, 60, 70));

	       JButton returnButton = createRoundedButton("Retour");
	       returnButton.addActionListener(e -> {
	           parent.setVisible(true); // Rendre l'interface principale visible
	           dispose();
	       });
	       returnButtonPanel.add(returnButton);

	       add(formPanel, BorderLayout.CENTER);
	       add(returnButtonPanel, BorderLayout.SOUTH);
	       setLocationRelativeTo(null);
	       setVisible(true);
	       parent.setVisible(false);
	   }

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
	}

// Interface pour préciser le motif du refus
class RefuseLeaveInterface extends JFrame {
   public RefuseLeaveInterface(JFrame parent) {
       setTitle("Motif du Refus");
       setSize(400, 250);
       setLayout(new BorderLayout());
       getContentPane().setBackground(new Color(29, 46, 56));

       // ---- Panneau principal ----
       JPanel formPanel = new JPanel();
       formPanel.setLayout(new GridLayout(3, 1, 10, 10)); // Grille pour organiser les champs
       formPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
       formPanel.setBackground(new Color(43, 60, 70));

       // Label et champ de texte pour le motif du refus
       JLabel reasonLabel = createStyledLabel("Motif du refus");
       JTextArea reasonTextArea = new JTextArea(5, 20);
       formPanel.add(reasonLabel);
       formPanel.add(reasonTextArea);

       // Bouton "Envoyer"
       JButton sendButton = createRoundedButton("Envoyer");
       sendButton.addActionListener(e -> {
           String reason = reasonTextArea.getText();
           if (!reason.isEmpty()) {
               JOptionPane.showMessageDialog(this, "Motif du refus envoyé :\n" + reason);
               new ValidateLeaveInterface(parent);
               dispose();
           } else {
               JOptionPane.showMessageDialog(this, "Veuillez entrer un motif.");
           }
       });
       formPanel.add(sendButton);

       // ---- Panneau du bouton "Retour" ----
       JPanel returnButtonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
       returnButtonPanel.setBackground(new Color(43, 60, 70));

       JButton returnButton = createRoundedButton("Retour");
       returnButton.addActionListener(e -> {
           parent.setVisible(true); // Rendre l'interface de validation des congés visible
           dispose(); // Fermer cette fenêtre
       });
       returnButtonPanel.add(returnButton);

       // Ajouter les panneaux à la fenêtre principale
       add(formPanel, BorderLayout.CENTER);
       add(returnButtonPanel, BorderLayout.SOUTH);
       setLocationRelativeTo(null);
       setVisible(true);
       parent.setVisible(false);
   }

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
}
