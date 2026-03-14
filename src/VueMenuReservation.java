package src;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class VueMenuReservation extends JFrame {

    private JButton btnCreer;
    private JButton btnModifier;
    private JButton btnConsulter;
    private JButton btnAnnuler;
    private JButton btnRetourMenu;
    private JButton btnQuitter;

    public VueMenuReservation() {
        setTitle("📅 Menu Réservation - JavaStocks");
        setSize(450, 550);
        setLocationRelativeTo(null);
        setResizable(false);
        setLayout(new BorderLayout());
        getContentPane().setBackground(new Color(248, 249, 250));

        // --- Titre de la fenêtre ---
        JLabel lblTitre = new JLabel("Menu réservation", SwingConstants.CENTER);
        lblTitre.setFont(new Font("Segoe UI", Font.BOLD, 24));
        lblTitre.setForeground(new Color(44, 62, 80));
        lblTitre.setBorder(new EmptyBorder(25, 0, 25, 0));
        add(lblTitre, BorderLayout.NORTH);

        // --- Panneau central pour les boutons ---
        // GridLayout(6, 1) veut dire 6 lignes, 1 colonne
        JPanel panelBoutons = new JPanel(new GridLayout(6, 1, 0, 15)); 
        panelBoutons.setOpaque(false);
        panelBoutons.setBorder(new EmptyBorder(10, 50, 30, 50));

        // Création des 6 boutons
        btnCreer = new JButton("1- Créer une réservation");
        btnModifier = new JButton("2- Modifier une réservation");
        btnConsulter = new JButton("3- Consulter une réservation");
        btnAnnuler = new JButton("4- Annuler une réservation");
        btnRetourMenu = new JButton("5- Revenir au menu");
        btnQuitter = new JButton("6- Quitter");

        // Application du style
        Color couleurAction = new Color(41, 128, 185); // Bleu pour les actions
        Color couleurDanger = new Color(231, 76, 60);  // Rouge pour annuler/quitter
        Color couleurNeutre = new Color(149, 165, 166); // Gris pour retour

        styleButton(btnCreer, couleurAction);
        styleButton(btnModifier, couleurAction);
        styleButton(btnConsulter, couleurAction);
        styleButton(btnAnnuler, couleurDanger);
        styleButton(btnRetourMenu, couleurNeutre);
        styleButton(btnQuitter, couleurDanger);

        // Ajout au panneau
        panelBoutons.add(btnCreer);
        panelBoutons.add(btnModifier);
        panelBoutons.add(btnConsulter);
        panelBoutons.add(btnAnnuler);
        panelBoutons.add(btnRetourMenu);
        panelBoutons.add(btnQuitter);

        add(panelBoutons, BorderLayout.CENTER);
    }

    // Méthode de style (la même qui marche bien pour enlever le fond blanc de Windows)
    private void styleButton(JButton btn, Color color) {
        btn.setFont(new Font("Segoe UI", Font.BOLD, 16));
        btn.setBackground(color);
        btn.setForeground(Color.WHITE);
        btn.setFocusPainted(false);
        btn.setOpaque(true);
        btn.setBorderPainted(false);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
    }

    // Getters pour que le contrôleur puisse donner vie aux boutons
    public JButton getBtnCreer() { return btnCreer; }
    public JButton getBtnModifier() { return btnModifier; }
    public JButton getBtnConsulter() { return btnConsulter; }
    public JButton getBtnAnnuler() { return btnAnnuler; }
    public JButton getBtnRetourMenu() { return btnRetourMenu; }
    public JButton getBtnQuitter() { return btnQuitter; }
}