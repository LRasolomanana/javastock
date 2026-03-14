package src;

import javax.swing.*;
import java.awt.*;

public class VueGestionDemandeReappro extends JFrame {

    private JTabbedPane onglets;

    // --- CRÉER ---
    private JComboBox<String> comboPLCrea, comboFournisseurCrea;
    private JTextField txtMotifCrea, txtDateCrea, txtCodeArtCrea, txtLibelleCrea, txtTailleCrea, txtCouleurCrea, txtContCrea, txtPoidsCrea, txtQteCrea;
    private JButton btnCreer;

    // --- MODIFIER ---
    private JComboBox<String> comboDemandeModif, comboPLModif, comboFournisseurModif;
    private JTextField txtNumCmdModif, txtMotifModif, txtDateModif, txtCodeArtModif, txtLibelleModif, txtTailleModif, txtCouleurModif, txtContModif, txtPoidsModif, txtQteModif;
    private JButton btnModifier;

    // --- CONSULTER ---
    private JComboBox<String> comboDemandeCons;
    private JLabel lblDetailsCons;

    public VueGestionDemandeReappro() {
        setTitle("Gestion des Demandes de Réapprovisionnement");
        setSize(750, 600); // Fenêtre plus grande pour tous ces champs !
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        onglets = new JTabbedPane();
        onglets.addTab("Créer", creerOngletCreation());
        onglets.addTab("Modifier", creerOngletModification());
        onglets.addTab("Consulter", creerOngletConsultation());

        add(onglets, BorderLayout.CENTER);

        JPanel panelBas = new JPanel();
        JButton btnRetour = new JButton("Retour");
        btnRetour.addActionListener(e -> dispose());
        panelBas.add(btnRetour);
        add(panelBas, BorderLayout.SOUTH);
    }

    private JPanel creerOngletCreation() {
        JPanel panel = new JPanel(new GridLayout(12, 2, 5, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));

        comboPLCrea = new JComboBox<>(new String[]{"-- Sélectionner un point de livraison --"});
        comboFournisseurCrea = new JComboBox<>(new String[]{"-- Sélectionner un fournisseur --"});
        txtMotifCrea = new JTextField("R réapprovisionnement"); // Valeur par défaut comme sur l'image
        txtDateCrea = new JTextField();
        txtCodeArtCrea = new JTextField(); txtLibelleCrea = new JTextField();
        txtTailleCrea = new JTextField(); txtCouleurCrea = new JTextField();
        txtContCrea = new JTextField(); txtPoidsCrea = new JTextField(); txtQteCrea = new JTextField();
        btnCreer = new JButton("VALIDER LA DEMANDE");

        panel.add(new JLabel("Point de livraison :")); panel.add(comboPLCrea);
        panel.add(new JLabel("Fournisseur :")); panel.add(comboFournisseurCrea);
        panel.add(new JLabel("Motif :")); panel.add(txtMotifCrea);
        panel.add(new JLabel("Date (JJ/MM/AAAA) :")); panel.add(txtDateCrea);
        panel.add(new JLabel("--- DÉTAIL ARTICLE ---")); panel.add(new JLabel("")); // Séparateur
        panel.add(new JLabel("Code Article :")); panel.add(txtCodeArtCrea);
        panel.add(new JLabel("Libellé :")); panel.add(txtLibelleCrea);
        panel.add(new JLabel("Taille / Couleur :")); 
        JPanel pnlTC = new JPanel(new GridLayout(1,2)); pnlTC.add(txtTailleCrea); pnlTC.add(txtCouleurCrea); panel.add(pnlTC);
        panel.add(new JLabel("Contenance / Poids :")); 
        JPanel pnlCP = new JPanel(new GridLayout(1,2)); pnlCP.add(txtContCrea); pnlCP.add(txtPoidsCrea); panel.add(pnlCP);
        panel.add(new JLabel("Quantité :")); panel.add(txtQteCrea);
        panel.add(new JLabel("")); panel.add(btnCreer);

        return panel;
    }

    private JPanel creerOngletModification() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));

        JPanel panelHaut = new JPanel();
        panelHaut.add(new JLabel("Choisir la commande n° :"));
        comboDemandeModif = new JComboBox<>(new String[]{"-- Sélectionner --"});
        panelHaut.add(comboDemandeModif);
        panel.add(panelHaut, BorderLayout.NORTH);

        JPanel panelCentre = new JPanel(new GridLayout(12, 2, 5, 5));
        txtNumCmdModif = new JTextField(); txtNumCmdModif.setEditable(false);
        comboPLModif = new JComboBox<>(new String[]{"-- Sélectionner --"});
        comboFournisseurModif = new JComboBox<>(new String[]{"-- Sélectionner --"});
        txtMotifModif = new JTextField(); txtDateModif = new JTextField();
        txtCodeArtModif = new JTextField(); txtLibelleModif = new JTextField();
        txtTailleModif = new JTextField(); txtCouleurModif = new JTextField();
        txtContModif = new JTextField(); txtPoidsModif = new JTextField(); txtQteModif = new JTextField();

        panelCentre.add(new JLabel("Numéro Commande :")); panelCentre.add(txtNumCmdModif);
        panelCentre.add(new JLabel("Point de livraison :")); panelCentre.add(comboPLModif);
        panelCentre.add(new JLabel("Fournisseur :")); panelCentre.add(comboFournisseurModif);
        panelCentre.add(new JLabel("Motif :")); panelCentre.add(txtMotifModif);
        panelCentre.add(new JLabel("Date :")); panelCentre.add(txtDateModif);
        panelCentre.add(new JLabel("Code Article :")); panelCentre.add(txtCodeArtModif);
        panelCentre.add(new JLabel("Libellé :")); panelCentre.add(txtLibelleModif);
        panelCentre.add(new JLabel("Taille :")); panelCentre.add(txtTailleModif);
        panelCentre.add(new JLabel("Couleur :")); panelCentre.add(txtCouleurModif);
        panelCentre.add(new JLabel("Contenance :")); panelCentre.add(txtContModif);
        panelCentre.add(new JLabel("Poids :")); panelCentre.add(txtPoidsModif);
        panelCentre.add(new JLabel("Quantité :")); panelCentre.add(txtQteModif);
        panel.add(panelCentre, BorderLayout.CENTER);

        btnModifier = new JButton("MODIFIER LA DEMANDE");
        panel.add(btnModifier, BorderLayout.SOUTH);

        return panel;
    }

    private JPanel creerOngletConsultation() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JPanel panelHaut = new JPanel();
        panelHaut.add(new JLabel("Choisir la demande :"));
        comboDemandeCons = new JComboBox<>(new String[]{"-- Sélectionner --"});
        panelHaut.add(comboDemandeCons);
        panel.add(panelHaut, BorderLayout.NORTH);

        lblDetailsCons = new JLabel("Sélectionnez une commande pour générer la fiche.");
        lblDetailsCons.setVerticalAlignment(SwingConstants.TOP);
        // Ajout d'un scroll au cas où la fiche est très grande
        JScrollPane scroll = new JScrollPane(lblDetailsCons);
        scroll.setBorder(BorderFactory.createEmptyBorder());
        panel.add(scroll, BorderLayout.CENTER);

        return panel;
    }

    // --- GETTERS ---
    public JTabbedPane getOnglets() { return onglets; }
    
    // Créer
    public JButton getBtnCreer() { return btnCreer; }
    public JComboBox<String> getComboPLCrea() { return comboPLCrea; }
    public JComboBox<String> getComboFournisseurCrea() { return comboFournisseurCrea; }
    public JTextField getTxtMotifCrea() { return txtMotifCrea; }
    public JTextField getTxtDateCrea() { return txtDateCrea; }
    public JTextField getTxtCodeArtCrea() { return txtCodeArtCrea; }
    public JTextField getTxtLibelleCrea() { return txtLibelleCrea; }
    public JTextField getTxtTailleCrea() { return txtTailleCrea; }
    public JTextField getTxtCouleurCrea() { return txtCouleurCrea; }
    public JTextField getTxtContCrea() { return txtContCrea; }
    public JTextField getTxtPoidsCrea() { return txtPoidsCrea; }
    public JTextField getTxtQteCrea() { return txtQteCrea; }

    // Modifier
    public JButton getBtnModifier() { return btnModifier; }
    public JComboBox<String> getComboDemandeModif() { return comboDemandeModif; }
    public JTextField getTxtNumCmdModif() { return txtNumCmdModif; }
    public JComboBox<String> getComboPLModif() { return comboPLModif; }
    public JComboBox<String> getComboFournisseurModif() { return comboFournisseurModif; }
    public JTextField getTxtMotifModif() { return txtMotifModif; }
    public JTextField getTxtDateModif() { return txtDateModif; }
    public JTextField getTxtCodeArtModif() { return txtCodeArtModif; }
    public JTextField getTxtLibelleModif() { return txtLibelleModif; }
    public JTextField getTxtTailleModif() { return txtTailleModif; }
    public JTextField getTxtCouleurModif() { return txtCouleurModif; }
    public JTextField getTxtContModif() { return txtContModif; }
    public JTextField getTxtPoidsModif() { return txtPoidsModif; }
    public JTextField getTxtQteModif() { return txtQteModif; }

    // Consulter
    public JComboBox<String> getComboDemandeCons() { return comboDemandeCons; }
    public JLabel getLblDetailsCons() { return lblDetailsCons; }
}