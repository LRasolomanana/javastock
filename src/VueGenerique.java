package src;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class VueGenerique extends JFrame {

    // On demande le titre du module et la fenêtre principale pour pouvoir y revenir
    public VueGenerique(String titreModule, JFrame fenetreParente) {
        setTitle("Java Stocks - " + titreModule);
        setSize(800, 500);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE); // Ferme juste cette fenêtre, pas l'appli
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());
        getContentPane().setBackground(new Color(248, 249, 250));


        // --- Bouton de retour en bas ---
        JPanel panelBas = new JPanel();
        panelBas.setOpaque(false);
        panelBas.setBorder(new EmptyBorder(20, 0, 30, 0)); // Marges

        JButton btnRetour = new JButton("RETOUR AU MENU PRINCIPAL");
        btnRetour.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btnRetour.setBackground(new Color(52, 73, 94)); // Bleu ardoise
        btnRetour.setForeground(Color.WHITE);
        btnRetour.setFocusPainted(false);
        btnRetour.setOpaque(true);
        btnRetour.setBorderPainted(false);
        btnRetour.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnRetour.setPreferredSize(new Dimension(250, 40));

        // L'action magique du bouton retour
        btnRetour.addActionListener(e -> {
            this.dispose(); // Détruit la fenêtre actuelle
            fenetreParente.setVisible(true); // Réaffiche le menu principal
        });

        panelBas.add(btnRetour);
        add(panelBas, BorderLayout.SOUTH);

        // Sécurité : Si l'utilisateur clique sur la croix rouge au lieu du bouton
        addWindowListener(new java.awt.event.WindowAdapter() {
            @Override
            public void windowClosed(java.awt.event.WindowEvent windowEvent) {
                fenetreParente.setVisible(true);
            }
        });
    }
}