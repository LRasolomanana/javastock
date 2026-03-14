package src;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class VueInscription extends JFrame {

    private JTextField txtPrenom;
    private JTextField txtNom;
    private JTextField txtEmail; 
    private JTextField txtIdentifiant;
    private JPasswordField txtMotDePasse;
    private JPasswordField txtConfirmation;
    private JButton btnValiderInscription;
    private JButton btnRetour;
    
    // Les boutons Oeil
    private JToggleButton btnVoirMdp;
    private JToggleButton btnVoirConfirmation;

    // Les labels pour les conditions de sécurité
    private JLabel lblConditionLongueur;
    private JLabel lblConditionMajMin;
    private JLabel lblConditionChiffre;
    private JLabel lblConditionSpecial;

    public final Color COULEUR_VALIDEE = new Color(39, 174, 96);
    public final Color COULEUR_NON_VALIDEE = Color.GRAY;

    public VueInscription() {
        setTitle("📝 Créer un compte sécurisé - JavaStocks");
        setSize(500, 600); // 📏 Fenêtre agrandie pour accueillir les 6 champs proprement
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);
        setLayout(new BorderLayout());
        getContentPane().setBackground(new Color(248, 249, 250));

        // --- En-tête ---
        JLabel lblTitre = new JLabel("Nouvel Utilisateur", SwingConstants.CENTER);
        lblTitre.setFont(new Font("Segoe UI", Font.BOLD, 22));
        lblTitre.setForeground(new Color(44, 62, 80));
        lblTitre.setBorder(new EmptyBorder(20, 0, 20, 0));
        add(lblTitre, BorderLayout.NORTH);

        // --- Formulaire ---
        JPanel panelGlobalForm = new JPanel(new BorderLayout());
        panelGlobalForm.setOpaque(false);
        panelGlobalForm.setBorder(new EmptyBorder(0, 40, 10, 40));

        // 🛠️ LA CORRECTION EST ICI : 6 lignes, 2 colonnes (Labels à gauche, Champs à droite)
        JPanel panelChamps = new JPanel(new GridLayout(6, 2, 10, 15));
        panelChamps.setOpaque(false);

        // 1. Le Prénom
        panelChamps.add(new JLabel("Prénom :"));
        txtPrenom = new JTextField();
        panelChamps.add(txtPrenom);

        // 2. Le Nom
        panelChamps.add(new JLabel("Nom :"));
        txtNom = new JTextField();
        panelChamps.add(txtNom);

        // 3. L'email
        panelChamps.add(new JLabel("Adresse Email :"));
        txtEmail = new JTextField();
        panelChamps.add(txtEmail);

        // 4. L'identifiant
        panelChamps.add(new JLabel("Identifiant souhaité :"));
        txtIdentifiant = new JTextField();
        panelChamps.add(txtIdentifiant);

        // 5. Mot de passe + Oeil
        panelChamps.add(new JLabel("Mot de passe :"));
        JPanel panelMdp = new JPanel(new BorderLayout());
        txtMotDePasse = new JPasswordField();
        panelMdp.add(txtMotDePasse, BorderLayout.CENTER);
        
        btnVoirMdp = new JToggleButton("👁");
        btnVoirMdp.setMargin(new Insets(0, 5, 0, 5));
        btnVoirMdp.setFocusPainted(false);
        btnVoirMdp.setCursor(new Cursor(Cursor.HAND_CURSOR));
        panelMdp.add(btnVoirMdp, BorderLayout.EAST);
        panelChamps.add(panelMdp);

        // 6. Confirmation + Oeil
        panelChamps.add(new JLabel("Confirmer le mot de passe :"));
        JPanel panelConf = new JPanel(new BorderLayout());
        txtConfirmation = new JPasswordField();
        panelConf.add(txtConfirmation, BorderLayout.CENTER);
        
        btnVoirConfirmation = new JToggleButton("👁");
        btnVoirConfirmation.setMargin(new Insets(0, 5, 0, 5));
        btnVoirConfirmation.setFocusPainted(false);
        btnVoirConfirmation.setCursor(new Cursor(Cursor.HAND_CURSOR));
        panelConf.add(btnVoirConfirmation, BorderLayout.EAST);
        panelChamps.add(panelConf);

        panelGlobalForm.add(panelChamps, BorderLayout.NORTH);

        // --- Conditions de sécurité ---
        JPanel panelConditions = new JPanel(new GridLayout(5, 1, 0, 5));
        panelConditions.setOpaque(false);
        panelConditions.setBorder(new EmptyBorder(15, 0, 0, 0));

        JLabel lblTitreConditions = new JLabel("Le mot de passe doit contenir :");
        lblTitreConditions.setFont(new Font("Segoe UI", Font.BOLD, 12));
        panelConditions.add(lblTitreConditions);

        lblConditionLongueur = creerLabelCondition("- Au moins 8 caractères");
        lblConditionMajMin = creerLabelCondition("- 1 majuscule et 1 minuscule");
        lblConditionChiffre = creerLabelCondition("- 1 chiffre");
        lblConditionSpecial = creerLabelCondition("- 1 caractère spécial (@#$%^&+=!)");

        panelConditions.add(lblConditionLongueur);
        panelConditions.add(lblConditionMajMin);
        panelConditions.add(lblConditionChiffre);
        panelConditions.add(lblConditionSpecial);

        panelGlobalForm.add(panelConditions, BorderLayout.CENTER);
        add(panelGlobalForm, BorderLayout.CENTER);

        // --- Boutons ---
        JPanel panelBas = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 20));
        panelBas.setOpaque(false);

        btnRetour = new JButton("RETOUR");
        styleButton(btnRetour, new Color(149, 165, 166));

        btnValiderInscription = new JButton("S'INSCRIRE");
        styleButton(btnValiderInscription, new Color(39, 174, 96));

        panelBas.add(btnRetour);
        panelBas.add(btnValiderInscription);
        add(panelBas, BorderLayout.SOUTH);
    }

    private JLabel creerLabelCondition(String texte) {
        JLabel lbl = new JLabel(texte);
        lbl.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        lbl.setForeground(COULEUR_NON_VALIDEE);
        return lbl;
    }

    private void styleButton(JButton btn, Color color) {
        btn.setBackground(color);
        btn.setForeground(Color.WHITE);
        btn.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btn.setFocusPainted(false);
        btn.setOpaque(true);
        btn.setBorderPainted(false);
        btn.setPreferredSize(new Dimension(130, 40));
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
    }

    // Getters
    public String getPrenom() { return txtPrenom.getText(); }
    public String getNom() { return txtNom.getText(); }
    public String getEmail() { return txtEmail.getText(); }
    public String getIdentifiant() { return txtIdentifiant.getText(); }
    public String getMotDePasse() { return new String(txtMotDePasse.getPassword()); }
    public String getConfirmation() { return new String(txtConfirmation.getPassword()); }
    
    public JPasswordField getTxtMotDePasse() { return txtMotDePasse; }
    public JPasswordField getTxtConfirmation() { return txtConfirmation; }
    
    public JToggleButton getBtnVoirMdp() { return btnVoirMdp; }
    public JToggleButton getBtnVoirConfirmation() { return btnVoirConfirmation; }
    
    public JButton getBtnValiderInscription() { return btnValiderInscription; }
    public JButton getBtnRetour() { return btnRetour; }
    
    public JLabel getLblConditionLongueur() { return lblConditionLongueur; }
    public JLabel getLblConditionMajMin() { return lblConditionMajMin; }
    public JLabel getLblConditionChiffre() { return lblConditionChiffre; }
    public JLabel getLblConditionSpecial() { return lblConditionSpecial; }
}