package src;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class VueMenuPrincipal extends JFrame {

    private final Font policeTitre = new Font("Segoe UI", Font.BOLD, 28);
    private final Font policeTuile = new Font("Segoe UI", Font.PLAIN, 18);
    private final Font policeBouton = new Font("Segoe UI", Font.PLAIN, 14);

    public VueMenuPrincipal() {
        this.setTitle("Java Stocks - Interface de Gestion");
        this.setSize(950, 650);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setLocationRelativeTo(null);
        this.setLayout(new BorderLayout());
        
        this.getContentPane().setBackground(new Color(248, 249, 250));

        // --- EN-TÊTE ---
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(new Color(255, 255, 255));
        headerPanel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createMatteBorder(0, 0, 1, 0, new Color(220, 220, 220)), 
                new EmptyBorder(20, 30, 20, 30)
        ));

        JLabel lblTitreApp = new JLabel("Menu Principal");
        lblTitreApp.setFont(policeTitre);
        lblTitreApp.setForeground(new Color(44, 62, 80));
        headerPanel.add(lblTitreApp, BorderLayout.WEST);

        JButton btnConnexion = new JButton("DÉCONNEXION");
        btnConnexion.setFont(policeBouton);
        btnConnexion.setCursor(new Cursor(Cursor.HAND_CURSOR));
        headerPanel.add(btnConnexion, BorderLayout.EAST);

        this.add(headerPanel, BorderLayout.NORTH);

        // Action Déconnexion
        btnConnexion.addActionListener(e -> {
            int choix = JOptionPane.showConfirmDialog(this, "Voulez-vous vous déconnecter ?", "Déconnexion", JOptionPane.YES_NO_OPTION);
            if (choix == JOptionPane.YES_OPTION) {
                this.dispose(); 
                VueConnexion vueConnexion = new VueConnexion();
                new ConnexionController(vueConnexion);
                vueConnexion.setVisible(true); 
            }
        });

        // --- TABLEAU DE BORD ---
        // MODIFICATION : Utilisation de 0 pour les lignes afin d'en ajouter dynamiquement selon le nombre de tuiles
        JPanel dashboardPanel = new JPanel(new GridLayout(0, 3, 25, 25));
        dashboardPanel.setBorder(new EmptyBorder(40, 40, 40, 40));
        dashboardPanel.setOpaque(false);

        dashboardPanel.add(creerTuileMenu("Gestion des articles", new Color(44, 62, 80)));
        dashboardPanel.add(creerTuileMenu("Gestion des coureurs", new Color(52, 73, 94)));
        dashboardPanel.add(creerTuileMenu("Gestion types d'épreuve", new Color(70, 90, 101)));
        dashboardPanel.add(creerTuileMenu("Gestion des réservations", new Color(84, 110, 122)));
        dashboardPanel.add(creerTuileMenu("Alertes rupture / attente", new Color(96, 125, 139)));
        dashboardPanel.add(creerTuileMenu("Consulter l'historique", new Color(120, 144, 156)));
        dashboardPanel.add(creerTuileMenu("Gestion du réapprovisionnement", new Color(44, 62, 80)));
        
        // MODIFICATION : Ajout de la nouvelle tuile pour les hébergements
        dashboardPanel.add(creerTuileMenu("Gestion des hébergements", new Color(108, 122, 137)));

        this.add(dashboardPanel, BorderLayout.CENTER);
    }

    private JPanel creerTuileMenu(String titre, Color couleurFond) {
        JPanel tuile = new JPanel(new GridBagLayout());
        tuile.setBackground(couleurFond);
        tuile.setBorder(new EmptyBorder(20, 20, 20, 20));

        JLabel lblTitre = new JLabel("<html><center>" + titre.toUpperCase() + "</center></html>");
        lblTitre.setFont(policeTuile);
        lblTitre.setForeground(Color.WHITE); 
        tuile.add(lblTitre);

        tuile.setCursor(new Cursor(Cursor.HAND_CURSOR));
        tuile.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                // --- LOGIQUE DE NAVIGATION ---
                
                if (titre.equalsIgnoreCase("Gestion des articles")) {
                    VueMenuPrincipal.this.setVisible(false);
                    VueGestionArticles vueArticles = new VueGestionArticles();
                    new ArticleController(vueArticles);
                    vueArticles.setVisible(true);
                    
                    vueArticles.addWindowListener(new java.awt.event.WindowAdapter() {
                        @Override
                        public void windowClosed(java.awt.event.WindowEvent e) {
                            VueMenuPrincipal.this.setVisible(true);
                        }
                    });

               } else if (titre.equalsIgnoreCase("Gestion des coureurs")) {
                    VueMenuPrincipal.this.setVisible(false);
                    VueGestionCoureurs vueCoureurs = new VueGestionCoureurs();
                    new CoureurController(vueCoureurs); 
                    vueCoureurs.setVisible(true);

                    vueCoureurs.addWindowListener(new java.awt.event.WindowAdapter() {
                        @Override
                        public void windowClosed(java.awt.event.WindowEvent e) {
                            VueMenuPrincipal.this.setVisible(true);
                        }
                    });
                } else if (titre.equalsIgnoreCase("Gestion types d'épreuve")) {
                    VueMenuPrincipal.this.setVisible(false);
                    VueGestionTypeEpreuve vueEpreuves = new VueGestionTypeEpreuve();
                    new TypeEpreuveController(vueEpreuves);
                    vueEpreuves.setVisible(true);

                    vueEpreuves.addWindowListener(new java.awt.event.WindowAdapter() {
                        @Override
                        public void windowClosed(java.awt.event.WindowEvent e) {
                            VueMenuPrincipal.this.setVisible(true);
                        }
                    });
                } else if (titre.equalsIgnoreCase("Gestion des réservations")) {
                    VueMenuPrincipal.this.setVisible(false);
                    VueMenuReservation vueReservation = new VueMenuReservation();
                    new MenuReservationController(vueReservation, VueMenuPrincipal.this); 
                    vueReservation.setVisible(true);

                    vueReservation.addWindowListener(new java.awt.event.WindowAdapter() {
                        @Override
                        public void windowClosed(java.awt.event.WindowEvent e) {
                            VueMenuPrincipal.this.setVisible(true);
                        }
                    });
                } else if (titre.equalsIgnoreCase("Alertes rupture / attente")) {
                    VueMenuPrincipal.this.setVisible(false);
                    VueMenuAlertes vueAlertes = new VueMenuAlertes();
                    new MenuAlertesController(vueAlertes, VueMenuPrincipal.this);
                    vueAlertes.setVisible(true);

                    vueAlertes.addWindowListener(new java.awt.event.WindowAdapter() {
                        @Override
                        public void windowClosed(java.awt.event.WindowEvent e) {
                            VueMenuPrincipal.this.setVisible(true);
                        }
                    });
                } else if (titre.equalsIgnoreCase("Consulter l'historique")) {
                    VueMenuPrincipal.this.setVisible(false);
                    VueMenuHistorique vueHisto = new VueMenuHistorique();
                    new MenuHistoriqueController(vueHisto, VueMenuPrincipal.this);
                    vueHisto.setVisible(true);

                    vueHisto.addWindowListener(new java.awt.event.WindowAdapter() {
                        @Override
                        public void windowClosed(java.awt.event.WindowEvent e) {
                            VueMenuPrincipal.this.setVisible(true);
                        }
                    });
                } else if (titre.equalsIgnoreCase("Gestion du réapprovisionnement")) {
                    VueMenuPrincipal.this.setVisible(false);
                    VueMenuReappro vueReappro = new VueMenuReappro();
                    new MenuReapproController(vueReappro, VueMenuPrincipal.this);
                    vueReappro.setVisible(true);

                    vueReappro.addWindowListener(new java.awt.event.WindowAdapter() {
                        @Override
                        public void windowClosed(java.awt.event.WindowEvent e) {
                            VueMenuPrincipal.this.setVisible(true);
                        }
                    });
                    
                // MODIFICATION : Nouveau bloc pour gérer le clic sur les hébergements
                // MODIFICATION : Nouveau bloc pour gérer le clic sur les hébergements
                } else if (titre.equalsIgnoreCase("Gestion des hébergements")) {
                    VueMenuPrincipal.this.setVisible(false);
                    
                    try {
                        VueGestionHebergement vueHebergement = new VueGestionHebergement();
                        
                        // ON DÉCOMMENTE ET ON ACTIVE LE CONTRÔLEUR ICI !
                        new HebergementController(vueHebergement); 
                        
                        vueHebergement.setVisible(true);

                        vueHebergement.addWindowListener(new java.awt.event.WindowAdapter() {
                            @Override
                            public void windowClosed(java.awt.event.WindowEvent e) {
                                VueMenuPrincipal.this.setVisible(true);
                            }
                        });
                    } catch (Exception ex) {
                        // Secours si la classe n'est pas encore créée
                        VueGenerique vueAutre = new VueGenerique(titre, VueMenuPrincipal.this);
                        vueAutre.setVisible(true);
                    }

                } else {
                    // Pour les autres cases pas encore codées
                    VueMenuPrincipal.this.setVisible(false);
                    VueGenerique vueAutre = new VueGenerique(titre, VueMenuPrincipal.this);
                    vueAutre.setVisible(true);
                }
            }
            
            @Override
            public void mouseEntered(MouseEvent e) { tuile.setBackground(couleurFond.brighter()); }
            @Override
            public void mouseExited(MouseEvent e) { tuile.setBackground(couleurFond); }
        });
        
        return tuile;
    }

    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception ignored) {}

        SwingUtilities.invokeLater(() -> {
            new VueMenuPrincipal().setVisible(true);
        });
    }
}