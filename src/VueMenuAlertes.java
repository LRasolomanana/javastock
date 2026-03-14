package src;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class VueMenuAlertes extends JFrame {

    private JButton btnProduitsRupture;
    private JButton btnReservationsAttente;
    private JButton btnRetourMenu;
    private JButton btnQuitter;

    public VueMenuAlertes() {
        setTitle("⚠️ Menu Alertes - JavaStocks");
        setSize(450, 400); // Fenêtre un peu moins haute car il y a moins de boutons
        setLocationRelativeTo(null);
        setResizable(false);
        setLayout(new BorderLayout());
        getContentPane().setBackground(new Color(248, 249, 250));

        // --- Titre ---
        JLabel lblTitre = new JLabel("<html><center>Menu Article en rupture /<br>réservation en attente</center></html>", SwingConstants.CENTER);
        lblTitre.setFont(new Font("Segoe UI", Font.BOLD, 20));
        lblTitre.setForeground(new Color(44, 62, 80));
        lblTitre.setBorder(new EmptyBorder(20, 0, 20, 0));
        add(lblTitre, BorderLayout.NORTH);

        // --- Panneau central pour les boutons ---
        JPanel panelBoutons = new JPanel(new GridLayout(4, 1, 0, 15)); 
        panelBoutons.setOpaque(false);
        panelBoutons.setBorder(new EmptyBorder(10, 30, 30, 30));

        // Création des 4 boutons selon ton image
        btnProduitsRupture = new JButton("1- Consulter les produits en rupture");
        btnReservationsAttente = new JButton("2- Consulter les réservations en attente");
        btnRetourMenu = new JButton("3- Revenir au menu");
        btnQuitter = new JButton("4- Quitter");

        // Style des boutons
        Color couleurAction = new Color(243, 156, 18); // Orange pour les alertes
        Color couleurDanger = new Color(231, 76, 60);  // Rouge pour quitter
        Color couleurNeutre = new Color(149, 165, 166); // Gris pour le retour

        styleButton(btnProduitsRupture, couleurAction);
        styleButton(btnReservationsAttente, couleurAction);
        styleButton(btnRetourMenu, couleurNeutre);
        styleButton(btnQuitter, couleurDanger);

        panelBoutons.add(btnProduitsRupture);
        panelBoutons.add(btnReservationsAttente);
        panelBoutons.add(btnRetourMenu);
        panelBoutons.add(btnQuitter);

        add(panelBoutons, BorderLayout.CENTER);
    }

    private void styleButton(JButton btn, Color color) {
        btn.setFont(new Font("Segoe UI", Font.BOLD, 15));
        btn.setBackground(color);
        btn.setForeground(Color.WHITE);
        btn.setFocusPainted(false);
        btn.setOpaque(true);
        btn.setBorderPainted(false);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
    }

    public JButton getBtnProduitsRupture() { return btnProduitsRupture; }
    public JButton getBtnReservationsAttente() { return btnReservationsAttente; }
    public JButton getBtnRetourMenu() { return btnRetourMenu; }
    public JButton getBtnQuitter() { return btnQuitter; }
}