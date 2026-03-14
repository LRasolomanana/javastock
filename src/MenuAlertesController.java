package src;

import javax.swing.*;

public class MenuAlertesController {

    private VueMenuAlertes vue;
    private VueMenuPrincipal menuPrincipal;

    public MenuAlertesController(VueMenuAlertes vue, VueMenuPrincipal menuPrincipal) {
        this.vue = vue;
        this.menuPrincipal = menuPrincipal;

        initialiserActions();
    }

    private void initialiserActions() {
        
        // 1- Produits en rupture
        vue.getBtnProduitsRupture().addActionListener(e -> {
            VueProduitsRupture vueRupture = new VueProduitsRupture();
            new ProduitsRuptureController(vueRupture);
            vueRupture.setVisible(true);
        });

        // 2- Réservations en attente
        vue.getBtnReservationsAttente().addActionListener(e -> {
            VueReservationsAttente vueAttente = new VueReservationsAttente();
            new ReservationsAttenteController(vueAttente);
            vueAttente.setVisible(true);
        });

        // 3- Revenir au menu principal
        vue.getBtnRetourMenu().addActionListener(e -> {
            vue.dispose(); 
            menuPrincipal.setVisible(true); 
        });

        // 4- Quitter l'application
        vue.getBtnQuitter().addActionListener(e -> {
            int choix = JOptionPane.showConfirmDialog(vue, 
                "Voulez-vous vraiment quitter l'application ?", 
                "Confirmation", 
                JOptionPane.YES_NO_OPTION);
                
            if (choix == JOptionPane.YES_OPTION) {
                System.exit(0);
            }
        });
    }
}