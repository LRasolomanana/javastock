package src;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class VueConnexion extends JFrame {

    private JTextField txtIdentifiant;
    private JPasswordField txtMotDePasse;
    private JButton btnConnexion;
    private JButton btnInscription;
    private JButton btnMdpOublie;
    private JToggleButton btnVoirMdp; // Le bouton Œil

    public VueConnexion() {
        setTitle("🔐 Connexion - JavaStocks");
        setSize(400, 360); // Fenêtre légèrement agrandie
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);
        setLayout(new BorderLayout());

        // --- En-tête ---
        JLabel lblTitre = new JLabel("Identification Requise", SwingConstants.CENTER);
        lblTitre.setFont(new Font("Segoe UI", Font.BOLD, 20));
        lblTitre.setBorder(new EmptyBorder(20, 0, 20, 0));
        add(lblTitre, BorderLayout.NORTH);

        // --- Formulaire ---
        JPanel panelForm = new JPanel(new GridLayout(2, 2, 10, 20));
        panelForm.setBorder(new EmptyBorder(10, 40, 10, 40));

        panelForm.add(new JLabel("Identifiant :"));
        txtIdentifiant = new JTextField();
        panelForm.add(txtIdentifiant);

        panelForm.add(new JLabel("Mot de passe :"));
        
        // Sous-panneau pour coller l'œil au champ mot de passe
        JPanel panelMdp = new JPanel(new BorderLayout());
        txtMotDePasse = new JPasswordField();
        panelMdp.add(txtMotDePasse, BorderLayout.CENTER);
        
        btnVoirMdp = new JToggleButton("👁");
        btnVoirMdp.setMargin(new Insets(0, 5, 0, 5));
        btnVoirMdp.setFocusPainted(false);
        btnVoirMdp.setCursor(new Cursor(Cursor.HAND_CURSOR));
        panelMdp.add(btnVoirMdp, BorderLayout.EAST);
        
        panelForm.add(panelMdp);

        add(panelForm, BorderLayout.CENTER);

        // --- Boutons du bas ---
        JPanel panelBas = new JPanel();
        panelBas.setLayout(new BoxLayout(panelBas, BoxLayout.Y_AXIS));
        panelBas.setBorder(new EmptyBorder(10, 0, 20, 0));

        // Bouton principal de connexion
        btnConnexion = new JButton("SE CONNECTER");
        btnConnexion.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btnConnexion.setBackground(new Color(41, 128, 185));
        btnConnexion.setForeground(Color.WHITE);
        btnConnexion.setFocusPainted(false);
        btnConnexion.setOpaque(true);
        btnConnexion.setBorderPainted(false);
        btnConnexion.setPreferredSize(new Dimension(200, 40));
        btnConnexion.setMaximumSize(new Dimension(200, 40)); // Pour le BoxLayout
        btnConnexion.setAlignmentX(Component.CENTER_ALIGNMENT);
        btnConnexion.setCursor(new Cursor(Cursor.HAND_CURSOR));

        // Bouton Mot de passe oublié
        btnMdpOublie = new JButton("Mot de passe oublié ?");
        stylerBoutonLien(btnMdpOublie);

        // Bouton Inscription
        btnInscription = new JButton("Pas de compte ? S'inscrire");
        stylerBoutonLien(btnInscription);

        panelBas.add(btnConnexion);
        panelBas.add(Box.createRigidArea(new Dimension(0, 10))); // Espace
        panelBas.add(btnMdpOublie);
        panelBas.add(btnInscription);

        add(panelBas, BorderLayout.SOUTH);
    }

    // Méthode pour donner l'apparence d'un lien cliquable
    private void stylerBoutonLien(JButton btn) {
        btn.setAlignmentX(Component.CENTER_ALIGNMENT);
        btn.setContentAreaFilled(false);
        btn.setBorderPainted(false);
        btn.setForeground(new Color(41, 128, 185));
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
    }

    // Getters pour le contrôleur
    public String getIdentifiant() { return txtIdentifiant.getText(); }
    public String getMotDePasse() { return new String(txtMotDePasse.getPassword()); }
    public JPasswordField getTxtMotDePasse() { return txtMotDePasse; }
    public JButton getBtnConnexion() { return btnConnexion; }
    public JButton getBtnInscription() { return btnInscription; }
    public JButton getBtnMdpOublie() { return btnMdpOublie; }
    public JToggleButton getBtnVoirMdp() { return btnVoirMdp; }
}