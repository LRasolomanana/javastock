package src;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;
import java.awt.*;

public class VueGestionArticles extends JFrame {

    // Composants pour la saisie (Modèle MVC)
    private JTextField txtLibelle, txtQuantite, txtSpec1, txtSpec2;
    private JComboBox<String> comboCategorie;
    private JButton btnAjouter, btnSupprimer, btnModifier;
    
    // Composants pour l'affichage (JTable)
    private JTable table;
    private DefaultTableModel tableModel;

    public VueGestionArticles() {
        // Configuration de la fenêtre [cite: 12]
        setTitle("📦 Gestionnaire de Stock Professionnel - JavaStock v3");
        setSize(950, 650);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE); // Ferme uniquement cette vue
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(10, 10));

        // Palette de couleurs professionnelles [cite: 112]
        Color primaryColor = new Color(41, 128, 185); // Bleu
        Color dangerColor = new Color(192, 57, 43);  // Rouge
        Color backgroundColor = new Color(236, 240, 241); // Gris clair

        // --- 1. PANNEAU NORD : FORMULAIRE DE SAISIE ---
        JPanel panelForm = new JPanel(new GridBagLayout());
        panelForm.setBackground(Color.WHITE);
        panelForm.setBorder(BorderFactory.createCompoundBorder(
            new EmptyBorder(15, 15, 15, 15),
            new TitledBorder(BorderFactory.createLineBorder(primaryColor), "Gestion des articles")
        ));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 8, 8, 8);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Initialisation des champs [cite: 316, 319, 321, 323, 324]
        comboCategorie = new JComboBox<>(new String[]{"Textile", "Boisson", "Denrée Sèche"});
        txtLibelle = new JTextField(15);
        txtQuantite = new JTextField(15);
        txtSpec1 = new JTextField(15); // Taille / Volume / Poids
        txtSpec2 = new JTextField(15); // Couleur
        
       btnAjouter = new JButton("CRÉER L'ARTICLE");
        btnModifier = new JButton("MODIFIER SÉLECTION"); // Le nouveau bouton
        btnSupprimer = new JButton("SUPPRIMER SÉLECTION");
        
        styleButton(btnAjouter, primaryColor);
        styleButton(btnModifier, new Color(243, 156, 18)); // Couleur Orange/Moutarde
        styleButton(btnSupprimer, dangerColor);

        // Placement dans la grille
        gbc.gridx = 0; gbc.gridy = 0; panelForm.add(new JLabel("Catégorie :"), gbc);
        gbc.gridx = 1; panelForm.add(comboCategorie, gbc);
        gbc.gridx = 2; panelForm.add(new JLabel("Détail 1 (Taille/Vol/Poids) :"), gbc);
        gbc.gridx = 3; panelForm.add(txtSpec1, gbc);

        gbc.gridx = 0; gbc.gridy = 1; panelForm.add(new JLabel("Libellé :"), gbc);
        gbc.gridx = 1; panelForm.add(txtLibelle, gbc);
        gbc.gridx = 2; panelForm.add(new JLabel("Détail 2 (Couleur) :"), gbc);
        gbc.gridx = 3; panelForm.add(txtSpec2, gbc);

        gbc.gridx = 0; gbc.gridy = 2; panelForm.add(new JLabel("Quantité :"), gbc);
        gbc.gridx = 1; panelForm.add(txtQuantite, gbc);
        
        // Création d'un sous-panneau pour aligner les 3 boutons proprement en bas
        JPanel panelBoutons = new JPanel(new GridLayout(1, 3, 10, 0));
        panelBoutons.setBackground(Color.WHITE);
        panelBoutons.add(btnAjouter);
        panelBoutons.add(btnModifier);
        panelBoutons.add(btnSupprimer);

        // On place ce panneau de boutons sur une nouvelle ligne (gridy = 3) qui prend toute la largeur
        gbc.gridx = 0; gbc.gridy = 3; gbc.gridwidth = 4;
        gbc.insets = new Insets(15, 8, 8, 8); // Un peu plus d'espace au-dessus
        panelForm.add(panelBoutons, gbc);

        add(panelForm, BorderLayout.NORTH);

        // --- 2. PANNEAU CENTRAL : TABLEAU DES ARTICLES ---
        tableModel = new DefaultTableModel(new Object[]{"ID", "Catégorie", "Libellé", "Quantité", "Spécificités"}, 0) {
            @Override
            public boolean isCellEditable(int row, int column) { return false; }
        };
        
        table = new JTable(tableModel);
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        table.setRowHeight(25);
        
        JTableHeader header = table.getTableHeader();
        header.setFont(new Font("Segoe UI", Font.BOLD, 14));
        // Création d'un rendu personnalisé pour forcer les couleurs, même sur Windows
        header.setDefaultRenderer(new javax.swing.table.DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
                Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                c.setBackground(primaryColor); // Force le fond bleu
                c.setForeground(Color.WHITE);  // Force le texte blanc
                ((JLabel) c).setHorizontalAlignment(SwingConstants.CENTER); // Centre le texte pour faire plus joli
                ((JLabel) c).setBorder(BorderFactory.createMatteBorder(0, 0, 1, 1, new Color(220, 220, 220))); // Petite bordure
                return c;
            }
        });

        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBorder(new EmptyBorder(0, 15, 15, 15));
        scrollPane.getViewport().setBackground(Color.WHITE);

        add(scrollPane, BorderLayout.CENTER);

        // --- 3. PANNEAU SUD : BOUTON RETOUR ---
        JPanel panelSud = new JPanel();
        panelSud.setBackground(backgroundColor);
        panelSud.setBorder(new EmptyBorder(10, 0, 20, 0));

        JButton btnRetour = new JButton("RETOUR AU MENU PRINCIPAL");
        styleButton(btnRetour, new Color(44, 62, 80)); // Couleur sombre professionnelle
        btnRetour.setPreferredSize(new Dimension(300, 45));

        // Action : Ferme la fenêtre (le Menu Principal se réaffichera via son WindowListener)
        btnRetour.addActionListener(e -> this.dispose());

        panelSud.add(btnRetour);
        add(panelSud, BorderLayout.SOUTH);

        getContentPane().setBackground(backgroundColor);
    }

    /**
     * Applique un style plat et professionnel aux boutons
     */
    private void styleButton(JButton btn, Color color) {
        btn.setBackground(color);
        btn.setForeground(Color.WHITE);
        btn.setFont(new Font("Segoe UI", Font.BOLD, 12));
        btn.setFocusPainted(false);
        btn.setOpaque(true);
        btn.setBorderPainted(false);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
    }

    // --- GETTERS POUR LE CONTROLEUR [cite: 18, 41] ---
    public String getCategorieSelectionnee() { return (String) comboCategorie.getSelectedItem(); }
    public String getLibelle() { return txtLibelle.getText(); }
    public String getSpec1() { return txtSpec1.getText(); }
    public String getSpec2() { return txtSpec2.getText(); }
    public String getQuantiteTexte() { return txtQuantite.getText().trim(); }
    public JButton getBtnAjouter() { return btnAjouter; }
    public JButton getBtnSupprimer() { return btnSupprimer; }

    // --- ACTIONS SUR LE TABLEAU ---
    public int getIdLigneSelectionnee() {
        int row = table.getSelectedRow();
        return (row != -1) ? (int) tableModel.getValueAt(row, 0) : -1;
    }

    public void ajouterLigneTableau(int id, String cat, String lib, int qte, String details) {
        tableModel.addRow(new Object[]{id, cat, lib, qte, details});
    }

    public void supprimerLigneTableau() {
        int row = table.getSelectedRow();
        if (row != -1) tableModel.removeRow(row);
    }

    public void reinitialiserChamps() {
        txtLibelle.setText("");
        txtQuantite.setText("");
        txtSpec1.setText("");
        txtSpec2.setText("");
        comboCategorie.setSelectedIndex(0);
    }
    // Méthode pour vider le tableau avant de le recharger
    public void viderTableau() {
        tableModel.setRowCount(0);
    }
    public JButton getBtnModifier() { return btnModifier; }
    public JTable getTable() { return table; }

    // Méthode pour remplir automatiquement les champs quand on clique sur le tableau
    public void setValeursFormulaire(String codeCat, String lib, String qte, String taille, String couleur, String volume, String poids) {
        txtLibelle.setText(lib);
        txtQuantite.setText(qte);
        
        if ("T".equals(codeCat)) {
            comboCategorie.setSelectedItem("Textile");
            txtSpec1.setText(taille != null ? taille : "");
            txtSpec2.setText(couleur != null ? couleur : "");
        } else if ("B".equals(codeCat)) {
            comboCategorie.setSelectedItem("Boisson");
            txtSpec1.setText(volume != null ? volume : "");
            txtSpec2.setText("");
        } else if ("DS".equals(codeCat)) {
            comboCategorie.setSelectedItem("Denrée Sèche");
            txtSpec1.setText(poids != null ? poids : "");
            txtSpec2.setText("");
        }
    }
}