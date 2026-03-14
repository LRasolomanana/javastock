package src;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class VueMenuHistorique extends JFrame {

    private JButton btnNbResaDate;
    private JButton btnNbResaCoureur;
    private JButton btnNbResaEpreuve;
    private JButton btnNbResaDateEpreuve;
    private JButton btnQteArticleDate;
    private JButton btnQteEpreuve;
    private JButton btnQteDateEpreuve;
    private JButton btnRetourMenu;
    private JButton btnQuitter;

    public VueMenuHistorique() {
        setTitle("📊 Menu Historique - JavaStocks");
        setSize(550, 650); // Fenêtre plus haute pour les 9 boutons
        setLocationRelativeTo(null);
        setResizable(false);
        setLayout(new BorderLayout());
        getContentPane().setBackground(new Color(248, 249, 250));

        // --- Titre ---
        JLabel lblTitre = new JLabel("Menu Historique des Statistiques", SwingConstants.CENTER);
        lblTitre.setFont(new Font("Segoe UI", Font.BOLD, 22));
        lblTitre.setForeground(new Color(44, 62, 80));
        lblTitre.setBorder(new EmptyBorder(20, 0, 15, 0));
        add(lblTitre, BorderLayout.NORTH);

        // --- Panneau central pour les boutons ---
        // GridLayout(9, 1) = 9 lignes, 1 colonne, avec un espacement de 10px
        JPanel panelBoutons = new JPanel(new GridLayout(9, 1, 0, 10)); 
        panelBoutons.setOpaque(false);
        panelBoutons.setBorder(new EmptyBorder(0, 30, 20, 30));

        // Création des 9 boutons (en reprenant exactement les textes de ton cahier des charges)
        btnNbResaDate = new JButton("1- Nombre de réservations à une date donnée");
        btnNbResaCoureur = new JButton("2- Nombre de réservations pour un coureur donné");
        btnNbResaEpreuve = new JButton("3- Nombre de réservations pour un type d'épreuve");
        btnNbResaDateEpreuve = new JButton("4- Nombre de résa. (Date + Type d'épreuve)");
        btnQteArticleDate = new JButton("5- Quantités réservées par article à une date");
        btnQteEpreuve = new JButton("6- Quantités réservées pour un type d'épreuve");
        btnQteDateEpreuve = new JButton("7- Quantités réservées (Date + Type d'épreuve)");
        btnRetourMenu = new JButton("8- Revenir au menu");
        btnQuitter = new JButton("9- Quitter");

        // Application du style
        Color couleurAction = new Color(52, 152, 219); // Bleu clair pour les stats
        Color couleurDanger = new Color(231, 76, 60);  // Rouge
        Color couleurNeutre = new Color(149, 165, 166); // Gris

        styleButton(btnNbResaDate, couleurAction);
        styleButton(btnNbResaCoureur, couleurAction);
        styleButton(btnNbResaEpreuve, couleurAction);
        styleButton(btnNbResaDateEpreuve, couleurAction);
        styleButton(btnQteArticleDate, couleurAction);
        styleButton(btnQteEpreuve, couleurAction);
        styleButton(btnQteDateEpreuve, couleurAction);
        styleButton(btnRetourMenu, couleurNeutre);
        styleButton(btnQuitter, couleurDanger);

        // Ajout au panneau
        panelBoutons.add(btnNbResaDate);
        panelBoutons.add(btnNbResaCoureur);
        panelBoutons.add(btnNbResaEpreuve);
        panelBoutons.add(btnNbResaDateEpreuve);
        panelBoutons.add(btnQteArticleDate);
        panelBoutons.add(btnQteEpreuve);
        panelBoutons.add(btnQteDateEpreuve);
        panelBoutons.add(btnRetourMenu);
        panelBoutons.add(btnQuitter);

        add(panelBoutons, BorderLayout.CENTER);
    }

    private void styleButton(JButton btn, Color color) {
        btn.setFont(new Font("Segoe UI", Font.BOLD, 13)); // Police un peu plus petite pour faire rentrer le texte
        btn.setBackground(color);
        btn.setForeground(Color.WHITE);
        btn.setFocusPainted(false);
        btn.setOpaque(true);
        btn.setBorderPainted(false);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
    }

    // Getters pour le contrôleur
    public JButton getBtnNbResaDate() { return btnNbResaDate; }
    public JButton getBtnNbResaCoureur() { return btnNbResaCoureur; }
    public JButton getBtnNbResaEpreuve() { return btnNbResaEpreuve; }
    public JButton getBtnNbResaDateEpreuve() { return btnNbResaDateEpreuve; }
    public JButton getBtnQteArticleDate() { return btnQteArticleDate; }
    public JButton getBtnQteEpreuve() { return btnQteEpreuve; }
    public JButton getBtnQteDateEpreuve() { return btnQteDateEpreuve; }
    public JButton getBtnRetourMenu() { return btnRetourMenu; }
    public JButton getBtnQuitter() { return btnQuitter; }
}