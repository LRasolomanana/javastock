package src;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class VueCreerReservation extends JFrame {

    private JComboBox<String> comboEpreuve; // NOUVEAU
    private JComboBox<String> comboCoureur;
    private JComboBox<String> comboArticle;
    private JTextField txtQuantite;
    private JButton btnValider;
    private JButton btnAnnuler;

    public VueCreerReservation() {
        setTitle("➕ Créer une réservation");
        setSize(550, 400); // Un peu plus grand pour la nouvelle ligne
        setLocationRelativeTo(null);
        setResizable(false);
        setLayout(new BorderLayout());
        getContentPane().setBackground(new Color(248, 249, 250));

        JLabel lblTitre = new JLabel("Nouvelle Réservation", SwingConstants.CENTER);
        lblTitre.setFont(new Font("Segoe UI", Font.BOLD, 22));
        lblTitre.setBorder(new EmptyBorder(20, 0, 20, 0));
        add(lblTitre, BorderLayout.NORTH);

        // --- Formulaire (4 lignes maintenant) ---
        JPanel panelForm = new JPanel(new GridLayout(4, 2, 10, 20));
        panelForm.setBorder(new EmptyBorder(10, 30, 10, 30));
        panelForm.setOpaque(false);

        panelForm.add(new JLabel("1. Choisir l'épreuve :"));
        comboEpreuve = new JComboBox<>();
        panelForm.add(comboEpreuve);

        panelForm.add(new JLabel("2. Choisir le coureur :"));
        comboCoureur = new JComboBox<>();
        panelForm.add(comboCoureur);

        panelForm.add(new JLabel("3. Choisir l'article :"));
        comboArticle = new JComboBox<>();
        panelForm.add(comboArticle);

        panelForm.add(new JLabel("4. Quantité réservée :"));
        txtQuantite = new JTextField();
        panelForm.add(txtQuantite);

        add(panelForm, BorderLayout.CENTER);

        // --- Boutons ---
        JPanel panelBas = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 15));
        panelBas.setOpaque(false);

        btnAnnuler = new JButton("ANNULER");
        styleButton(btnAnnuler, new Color(149, 165, 166));

        btnValider = new JButton("VALIDER LA RÉSERVATION");
        styleButton(btnValider, new Color(39, 174, 96));

        panelBas.add(btnAnnuler);
        panelBas.add(btnValider);
        add(panelBas, BorderLayout.SOUTH);
    }

    private void styleButton(JButton btn, Color color) {
        btn.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btn.setBackground(color);
        btn.setForeground(Color.WHITE);
        btn.setFocusPainted(false);
        btn.setOpaque(true);
        btn.setBorderPainted(false);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
    }

    public JComboBox<String> getComboEpreuve() { return comboEpreuve; }
    public JComboBox<String> getComboCoureur() { return comboCoureur; }
    public JComboBox<String> getComboArticle() { return comboArticle; }
    public String getQuantite() { return txtQuantite.getText(); }
    public JButton getBtnValider() { return btnValider; }
    public JButton getBtnAnnuler() { return btnAnnuler; }
}