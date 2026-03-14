package src;

import javax.swing.*;
import java.awt.*;

public class VueMenuReappro extends JFrame {

    // Boutons Fournisseur
    private JButton btnCreerFournisseur, btnModifierFournisseur, btnAfficherFournisseur;
    // Boutons Livraison
    private JButton btnCreerPointLivraison, btnModifierPointLivraison, btnAfficherPointLivraison;
    // NOUVEAU : Boutons Demande
    private JButton btnCreerDemande, btnModifierDemande, btnAfficherDemande;

    public VueMenuReappro() {
        setTitle("Gestion des Réapprovisionnements");
        setSize(900, 450); // J'ai élargi un peu la fenêtre pour la 3ème colonne !
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout(10, 10));

        JLabel titrePrincipal = new JLabel("Menu des Réapprovisionnements", SwingConstants.CENTER);
        titrePrincipal.setFont(new Font("Arial", Font.BOLD, 22));
        titrePrincipal.setBorder(BorderFactory.createEmptyBorder(20, 0, 20, 0));
        add(titrePrincipal, BorderLayout.NORTH);

        // NOUVEAU : Grille avec 1 ligne et 3 colonnes
        JPanel panelCentral = new JPanel(new GridLayout(1, 3, 20, 0));
        panelCentral.setBorder(BorderFactory.createEmptyBorder(10, 20, 20, 20));

        // --- 1. Gestion Fournisseur ---
        JPanel panelFournisseur = new JPanel(new GridLayout(3, 1, 10, 20));
        panelFournisseur.setBorder(BorderFactory.createTitledBorder("Gestion Fournisseur"));
        btnCreerFournisseur = new JButton("1- Créer un fournisseur");
        btnModifierFournisseur = new JButton("2- Modifier un fournisseur");
        btnAfficherFournisseur = new JButton("3- Afficher un fournisseur");
        panelFournisseur.add(btnCreerFournisseur);
        panelFournisseur.add(btnModifierFournisseur);
        panelFournisseur.add(btnAfficherFournisseur);

        // --- 2. Gestion Point de Livraison ---
        JPanel panelLivraison = new JPanel(new GridLayout(3, 1, 10, 20));
        panelLivraison.setBorder(BorderFactory.createTitledBorder("Point de livraison"));
        btnCreerPointLivraison = new JButton("1- Créer un point de livraison");
        btnModifierPointLivraison = new JButton("2- Modifier un point de livraison");
        btnAfficherPointLivraison = new JButton("3- Afficher un point de livraison");
        panelLivraison.add(btnCreerPointLivraison);
        panelLivraison.add(btnModifierPointLivraison);
        panelLivraison.add(btnAfficherPointLivraison);

        // --- 3. Gestion Demande Réappro (NOUVEAU) ---
        JPanel panelDemande = new JPanel(new GridLayout(3, 1, 10, 20));
        panelDemande.setBorder(BorderFactory.createTitledBorder("Demande de réapprovisionnement"));
        btnCreerDemande = new JButton("1- Créer une demande");
        btnModifierDemande = new JButton("2- Modifier une demande");
        btnAfficherDemande = new JButton("3- Afficher une demande");
        panelDemande.add(btnCreerDemande);
        panelDemande.add(btnModifierDemande);
        panelDemande.add(btnAfficherDemande);

        panelCentral.add(panelFournisseur);
        panelCentral.add(panelLivraison);
        panelCentral.add(panelDemande);
        add(panelCentral, BorderLayout.CENTER);

        JPanel panelBas = new JPanel();
        JButton btnRetour = new JButton("Retour à l'accueil");
        btnRetour.addActionListener(e -> dispose());
        panelBas.add(btnRetour);
        add(panelBas, BorderLayout.SOUTH);
    }

    // Getters Fournisseur
    public JButton getBtnCreerFournisseur() { return btnCreerFournisseur; }
    public JButton getBtnModifierFournisseur() { return btnModifierFournisseur; }
    public JButton getBtnAfficherFournisseur() { return btnAfficherFournisseur; }
    // Getters Livraison
    public JButton getBtnCreerPointLivraison() { return btnCreerPointLivraison; }
    public JButton getBtnModifierPointLivraison() { return btnModifierPointLivraison; }
    public JButton getBtnAfficherPointLivraison() { return btnAfficherPointLivraison; }
    // Getters Demandes
    public JButton getBtnCreerDemande() { return btnCreerDemande; }
    public JButton getBtnModifierDemande() { return btnModifierDemande; }
    public JButton getBtnAfficherDemande() { return btnAfficherDemande; }
}