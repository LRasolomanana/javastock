package src;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class VueGestionHebergement extends JFrame {

    private final Font policeTitre = new Font("Segoe UI", Font.BOLD, 24);
    private final Font policeLabel = new Font("Segoe UI", Font.PLAIN, 14);

    // Champs de formulaire accessibles par le contrôleur
    private JComboBox<Coureur> cbCoureurs;
    private JComboBox<String> cbTypeHebergement;
    private JTextField txtDateDebut;
    private JTextField txtDateFin;
    private JTextArea zoneResultat;
    private JButton btnValider; // Déclaré ici pour le getter du contrôleur

    public VueGestionHebergement() {
        this.setTitle("Java Stocks - Gestion des Hébergements");
        this.setSize(600, 500);
        this.setLocationRelativeTo(null); 
        this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE); 
        this.setLayout(new BorderLayout());
        this.getContentPane().setBackground(new Color(248, 249, 250));

        // --- EN-TÊTE ---
        JPanel headerPanel = new JPanel();
        headerPanel.setBackground(new Color(108, 122, 137));
        headerPanel.setBorder(new EmptyBorder(15, 15, 15, 15));
        JLabel lblTitre = new JLabel("Nouvelle Réservation d'Hébergement");
        lblTitre.setFont(policeTitre);
        lblTitre.setForeground(Color.WHITE);
        headerPanel.add(lblTitre);
        this.add(headerPanel, BorderLayout.NORTH);

        // --- FORMULAIRE CENTRAL ---
        JPanel formPanel = new JPanel(new GridLayout(4, 2, 15, 15));
        formPanel.setBorder(new EmptyBorder(30, 40, 30, 40));
        formPanel.setOpaque(false);

        // 1. Sélection du Coureur (depuis la BDD)
        formPanel.add(creerLabel("Sélectionnez le coureur :"));
        Coureur[] listeCoureurs = getCoureursDepuisBaseDeDonnees(); 
        cbCoureurs = new JComboBox<>(listeCoureurs);
        formPanel.add(cbCoureurs);

        // 2. Type d'hébergement
        formPanel.add(creerLabel("Type d'hébergement :"));
        String[] types = {"STUDIO", "F1", "F2"};
        cbTypeHebergement = new JComboBox<>(types);
        formPanel.add(cbTypeHebergement);

        // 3. Date d'arrivée
        formPanel.add(creerLabel("Date d'arrivée (AAAA-MM-JJ) :"));
        txtDateDebut = new JTextField();
        formPanel.add(txtDateDebut);

        // 4. Date de départ
        formPanel.add(creerLabel("Date de départ (AAAA-MM-JJ) :"));
        txtDateFin = new JTextField();
        formPanel.add(txtDateFin);

        this.add(formPanel, BorderLayout.CENTER);

        // --- ZONE DE RÉSULTAT ET BOUTONS (SUD) ---
        JPanel southPanel = new JPanel(new BorderLayout());
        southPanel.setOpaque(false);
        southPanel.setBorder(new EmptyBorder(0, 40, 20, 40));

        zoneResultat = new JTextArea(6, 40); 
        zoneResultat.setEditable(false);
        zoneResultat.setBorder(BorderFactory.createTitledBorder("Résultat de l'opération"));
        southPanel.add(new JScrollPane(zoneResultat), BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        buttonPanel.setOpaque(false);

        // Initialisation du bouton Valider (le contrôleur lui donnera son action)
        btnValider = new JButton("Enregistrer la réservation");
        btnValider.setBackground(new Color(46, 204, 113));
        btnValider.setForeground(Color.WHITE);
        btnValider.setFocusPainted(false);

        JButton btnFermer = new JButton("Fermer");
        
        buttonPanel.add(btnFermer);
        buttonPanel.add(btnValider);
        southPanel.add(buttonPanel, BorderLayout.SOUTH);

        this.add(southPanel, BorderLayout.SOUTH);

        // L'action du bouton fermer reste gérée par la vue car c'est purement graphique
        btnFermer.addActionListener(e -> this.dispose()); 
    }

    private JLabel creerLabel(String texte) {
        JLabel label = new JLabel(texte);
        label.setFont(policeLabel);
        return label;
    }

    /**
     * Récupère la liste réelle des coureurs depuis la base de données.
     */
    private Coureur[] getCoureursDepuisBaseDeDonnees() {
        List<Coureur> listeCoureurs = new ArrayList<>();
        String requeteSQL = "SELECT id, nom, prenom FROM COUREUR ORDER BY nom ASC";

        // On sort la connexion des parenthèses pour qu'elle ne soit pas auto-fermée
        Connection conn = ConnexionBDD.getConnexion();
        try (PreparedStatement stmt = conn.prepareStatement(requeteSQL);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                int id = rs.getInt("id");
                String nom = rs.getString("nom");
                String prenom = rs.getString("prenom");
                listeCoureurs.add(new Coureur(id, nom, prenom));
            }

        } catch (SQLException e) {
            System.err.println("Erreur SQL : " + e.getMessage());
            afficherErreur("Impossible de charger les coureurs depuis la base de données.");
        }

        // Sécurité si la base est vide pour le test du gestionnaire
        if (listeCoureurs.isEmpty()) {
            listeCoureurs.add(new Coureur(999, "VOTRE_NOM", "VOTRE_PRENOM"));
        }

        return listeCoureurs.toArray(new Coureur[0]);
    }

    // ==========================================
    // GETTERS POUR LE CONTRÔLEUR
    // ==========================================

    public JButton getBtnValider() {
        return btnValider;
    }

    public Coureur getCoureurSelectionne() {
        return (Coureur) cbCoureurs.getSelectedItem();
    }

    public String getTypeHebergementSelectionne() {
        return (String) cbTypeHebergement.getSelectedItem();
    }

    public String getDateDebut() {
        return txtDateDebut.getText();
    }

    public String getDateFin() {
        return txtDateFin.getText();
    }

    // ==========================================
    // MÉTHODES D'AFFICHAGE POUR LE CONTRÔLEUR
    // ==========================================

    public void afficherErreur(String message) {
        JOptionPane.showMessageDialog(this, message, "Erreur", JOptionPane.ERROR_MESSAGE);
    }

    public void afficherSucces(String message) {
        zoneResultat.setText(message);
        zoneResultat.setForeground(new Color(39, 174, 96)); // Texte en vert
    }
}