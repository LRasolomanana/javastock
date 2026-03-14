package src;

import javax.swing.*;
import java.awt.*;

public class VueGestionFournisseur extends JFrame {

    // J'ajoute la variable ici pour que toute la classe puisse la voir !
    private JTabbedPane onglets;

    // --- Composants de l'onglet CRÉER ---
    private JTextField txtNomCrea, txtRueCrea, txtCpCrea, txtVilleCrea, txtTelCrea, txtEmailCrea;
    private JButton btnCreer;

    // --- Composants de l'onglet MODIFIER ---
    private JComboBox<String> comboFournisseurModif;
    private JTextField txtCodeModif, txtNomModif, txtRueModif, txtCpModif, txtVilleModif, txtTelModif, txtEmailModif;
    private JButton btnModifier;

    // --- Composants de l'onglet CONSULTER ---
    private JComboBox<String> comboFournisseurCons;
    private JLabel lblDetailsCons; 

    public VueGestionFournisseur() {
        setTitle("Gestion des Fournisseurs");
        setSize(500, 400);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        // Création du système d'onglets (On utilise la variable déclarée plus haut)
        onglets = new JTabbedPane();

        onglets.addTab("Créer", creerOngletCreation());
        onglets.addTab("Modifier", creerOngletModification());
        onglets.addTab("Consulter", creerOngletConsultation());

        add(onglets, BorderLayout.CENTER); // On précise que les onglets vont au centre

        // --- Ajout du bouton Retour en bas ---
        JPanel panelBas = new JPanel();
        JButton btnRetour = new JButton("Retour");
        btnRetour.addActionListener(e -> dispose()); // Ferme cette fenêtre pour revenir au menu
        panelBas.add(btnRetour);
        add(panelBas, BorderLayout.SOUTH); // On place le bouton tout en bas
    }
    

    // =========================================================
    // CONSTRUCTION DE L'ONGLET 1 : CRÉATION
    // =========================================================
    private JPanel creerOngletCreation() {
        JPanel panel = new JPanel(new GridLayout(7, 2, 10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        txtNomCrea = new JTextField();
        txtRueCrea = new JTextField();
        txtCpCrea = new JTextField();
        txtVilleCrea = new JTextField();
        txtTelCrea = new JTextField();
        txtEmailCrea = new JTextField();
        btnCreer = new JButton("CRÉER LE FOURNISSEUR");

        panel.add(new JLabel("Nom :")); panel.add(txtNomCrea);
        panel.add(new JLabel("Rue :")); panel.add(txtRueCrea);
        panel.add(new JLabel("CP :")); panel.add(txtCpCrea);
        panel.add(new JLabel("Ville :")); panel.add(txtVilleCrea);
        panel.add(new JLabel("Tel :")); panel.add(txtTelCrea);
        panel.add(new JLabel("Email :")); panel.add(txtEmailCrea);
        panel.add(new JLabel("")); panel.add(btnCreer); 

        return panel;
    }

    // =========================================================
    // CONSTRUCTION DE L'ONGLET 2 : MODIFICATION
    // =========================================================
    private JPanel creerOngletModification() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));

        JPanel panelHaut = new JPanel();
        panelHaut.add(new JLabel("Choisir le fournisseur :"));
        comboFournisseurModif = new JComboBox<>(new String[]{"-- Sélectionner --"}); 
        panelHaut.add(comboFournisseurModif);
        panel.add(panelHaut, BorderLayout.NORTH);

        JPanel panelCentre = new JPanel(new GridLayout(7, 2, 5, 5));
        txtCodeModif = new JTextField(); txtCodeModif.setEditable(false); 
        txtNomModif = new JTextField();
        txtRueModif = new JTextField();
        txtCpModif = new JTextField();
        txtVilleModif = new JTextField();
        txtTelModif = new JTextField();
        txtEmailModif = new JTextField();
        
        panelCentre.add(new JLabel("Code :")); panelCentre.add(txtCodeModif);
        panelCentre.add(new JLabel("Nom :")); panelCentre.add(txtNomModif);
        panelCentre.add(new JLabel("Rue :")); panelCentre.add(txtRueModif);
        panelCentre.add(new JLabel("CP :")); panelCentre.add(txtCpModif);
        panelCentre.add(new JLabel("Ville :")); panelCentre.add(txtVilleModif);
        panelCentre.add(new JLabel("Tel :")); panelCentre.add(txtTelModif);
        panelCentre.add(new JLabel("Email :")); panelCentre.add(txtEmailModif);
        panel.add(panelCentre, BorderLayout.CENTER);

        btnModifier = new JButton("MODIFIER");
        panel.add(btnModifier, BorderLayout.SOUTH);

        return panel;
    }

    // =========================================================
    // CONSTRUCTION DE L'ONGLET 3 : CONSULTATION
    // =========================================================
    private JPanel creerOngletConsultation() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JPanel panelHaut = new JPanel();
        panelHaut.add(new JLabel("Choisir le fournisseur :"));
        comboFournisseurCons = new JComboBox<>(new String[]{"-- Sélectionner --"}); 
        panelHaut.add(comboFournisseurCons);
        panel.add(panelHaut, BorderLayout.NORTH);

        lblDetailsCons = new JLabel("Veuillez sélectionner un fournisseur pour voir ses détails.");
        lblDetailsCons.setVerticalAlignment(SwingConstants.TOP);
        panel.add(lblDetailsCons, BorderLayout.CENTER);

        return panel;
    }

    // =========================================================
    // --- GETTERS POUR LE CONTROLEUR ---
    // =========================================================
    
    // ⭐ LE VOICI ! Le getter qui manquait pour résoudre ton erreur :
    public JTabbedPane getOnglets() { return onglets; }

    public JButton getBtnCreer() { return btnCreer; }
    public JTextField getTxtNomCrea() { return txtNomCrea; }
    public JTextField getTxtRueCrea() { return txtRueCrea; }
    public JTextField getTxtCpCrea() { return txtCpCrea; }
    public JTextField getTxtVilleCrea() { return txtVilleCrea; }
    public JTextField getTxtTelCrea() { return txtTelCrea; }
    public JTextField getTxtEmailCrea() { return txtEmailCrea; }

    public JButton getBtnModifier() { return btnModifier; }
    public JComboBox<String> getComboFournisseurModif() { return comboFournisseurModif; }
    public JTextField getTxtCodeModif() { return txtCodeModif; }
    public JTextField getTxtNomModif() { return txtNomModif; }
    public JTextField getTxtRueModif() { return txtRueModif; }
    public JTextField getTxtCpModif() { return txtCpModif; }
    public JTextField getTxtVilleModif() { return txtVilleModif; }
    public JTextField getTxtTelModif() { return txtTelModif; }
    public JTextField getTxtEmailModif() { return txtEmailModif; }

    public JComboBox<String> getComboFournisseurCons() { return comboFournisseurCons; }
    public JLabel getLblDetailsCons() { return lblDetailsCons; }
}