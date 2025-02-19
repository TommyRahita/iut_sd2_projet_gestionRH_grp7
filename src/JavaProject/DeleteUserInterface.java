package JavaProject;

import java.awt.*;
import javax.swing.*;
import javax.swing.border.LineBorder;
import java.io.*;
import java.util.ArrayList;

class DeleteUserInterface extends JFrame {
    public DeleteUserInterface(JFrame parent) {
        setTitle("Supprimer un utilisateur");
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
        formPanel.setLayout(new GridBagLayout());
        formPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        formPanel.setBackground(new Color(43, 60, 70));
        setResizable(false);
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.anchor = GridBagConstraints.CENTER;

        JLabel choiceLabel = createStyledLabel("Veuillez choisir un utilisateur :");
        formPanel.add(choiceLabel, gbc);

        // Charger les utilisateurs depuis le fichier CSV
        String[] utilisateurs = loadUsersFromCSV();

        gbc.gridy++;
        JComboBox<String> choiceBox = new JComboBox<>(utilisateurs);
        choiceBox.setPreferredSize(new Dimension(200, 30));
        choiceBox.setBackground(new Color(255, 204, 0));
        choiceBox.setForeground(Color.BLACK);
        formPanel.add(choiceBox, gbc);

        // ---- Panneau des boutons ----
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
        buttonPanel.setBackground(new Color(43, 60, 70));

        JButton deleteButton = createRoundedButton("Supprimer");
        deleteButton.addActionListener(e -> {
            String selectedUser = (String) choiceBox.getSelectedItem();
            if (selectedUser != null && !selectedUser.equals("Aucun utilisateur trouvé")) {
                int confirmation = JOptionPane.showConfirmDialog(
                    this,
                    "Êtes-vous sûr de vouloir supprimer l'utilisateur : " + selectedUser + " ?",
                    "Confirmation de suppression",
                    JOptionPane.YES_NO_OPTION
                );
                if (confirmation == JOptionPane.YES_OPTION) {
                    deleteUserFromCSV(selectedUser); // Appeler la méthode pour supprimer l'utilisateur
                    JOptionPane.showMessageDialog(this, "Utilisateur supprimé avec succès !");
                    // Recharger la liste déroulante
                    choiceBox.setModel(new DefaultComboBoxModel<>(loadUsersFromCSV()));
                }
            } else {
                JOptionPane.showMessageDialog(this, "Veuillez sélectionner un utilisateur valide.", "Erreur", JOptionPane.ERROR_MESSAGE);
            }
        });

        buttonPanel.add(deleteButton);

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

 // Méthode pour charger les utilisateurs depuis un fichier CSV
    private String[] loadUsersFromCSV() {
        ArrayList<String> usersList = new ArrayList<>();
        String filePath = "resources/Utilisateurs.csv"; // Chemin relatif vers le fichier CSV dans le dossier resources

        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            boolean isFirstLine = true; // Indicateur pour ignorer l'entête
            while ((line = br.readLine()) != null) {
                if (isFirstLine) {
                    isFirstLine = false; // Ignorer la première ligne
                    continue;
                }
                String[] columns = line.split(";");
                if (columns.length >= 3) { // Vérifier que les colonnes existent
                    // Afficher les colonnes 2 et 3 (index 1 et 2 dans un tableau)
                    String fullName = columns[1] + " " + columns[2];
                    usersList.add(fullName);
                }
            }
        } catch (IOException e) {
            System.out.println("Erreur lors de la lecture du fichier CSV : " + e.getMessage());
        }

        // Convertir la liste en tableau
        if (usersList.isEmpty()) {
            usersList.add("Aucun utilisateur trouvé");
        }
        return usersList.toArray(new String[0]);
    }
    
 // Méthode pour supprimer un utilisateur du fichier CSV
    private void deleteUserFromCSV(String selectedUser) {
        String filePath = "resources/Utilisateurs.csv"; // Chemin vers le fichier CSV
        ArrayList<String> updatedLines = new ArrayList<>();

        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            boolean isFirstLine = true; // Pour conserver l'entête
            while ((line = br.readLine()) != null) {
                if (isFirstLine) {
                    updatedLines.add(line); // Garder l'entête
                    isFirstLine = false;
                    continue;
                }
                String[] columns = line.split(";");
                if (columns.length >= 3) {
                    // Vérifier si la ligne correspond à l'utilisateur sélectionné
                    String fullName = columns[1] + " " + columns[2];
                    if (!fullName.equals(selectedUser)) {
                        updatedLines.add(line); // Conserver les lignes ne correspondant pas
                    }
                }
            }
        } catch (IOException e) {
            System.out.println("Erreur lors de la lecture du fichier CSV : " + e.getMessage());
        }

        // Réécrire le fichier CSV avec les lignes mises à jour
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(filePath))) {
            for (String updatedLine : updatedLines) {
                bw.write(updatedLine);
                bw.newLine();
            }
        } catch (IOException e) {
            System.out.println("Erreur lors de l'écriture du fichier CSV : " + e.getMessage());
        }
    }


}
