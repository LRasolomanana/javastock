package src;

import javax.swing.*;

public class MenuReservationController {

    private VueMenuReservation vue;
    private VueMenuPrincipal menuPrincipal; // On garde le menu principal en mémoire pour pouvoir y retourner

    public MenuReservationController(VueMenuReservation vue, VueMenuPrincipal menuPrincipal) {
        this.vue = vue;
        this.menuPrincipal = menuPrincipal;

        initialiserActions();
    }

    private void initialiserActions() {
        
        // 1- Créer
        vue.getBtnCreer().addActionListener(e -> {
            VueCreerReservation vueCreation = new VueCreerReservation();
            new CreerReservationController(vueCreation);
            vueCreation.setVisible(true);
        });

        // 2- Modifier
        vue.getBtnModifier().addActionListener(e -> {
            VueModifierReservation vueModif = new VueModifierReservation();
            new ModifierReservationController(vueModif);
            vueModif.setVisible(true);
        });

        // 3- Consulter
        vue.getBtnConsulter().addActionListener(e -> {
            VueConsulterReservation vueConsult = new VueConsulterReservation();
            new ConsulterReservationController(vueConsult);
            vueConsult.setVisible(true);
        });

        // 4- Annuler
        vue.getBtnAnnuler().addActionListener(e -> {
            VueAnnulerReservation vueAnnuler = new VueAnnulerReservation();
            new AnnulerReservationController(vueAnnuler);
            vueAnnuler.setVisible(true);
        });

        // 5- Revenir au menu principal
        vue.getBtnRetourMenu().addActionListener(e -> {
            vue.dispose(); // Ferme le sous-menu de réservation
            menuPrincipal.setVisible(true); // Réaffiche le grand menu
        });

        // 6- Quitter l'application
        vue.getBtnQuitter().addActionListener(e -> {
            int choix = JOptionPane.showConfirmDialog(vue, 
                "Voulez-vous vraiment quitter l'application complète ?", 
                "Confirmation", 
                JOptionPane.YES_NO_OPTION);
                
            if (choix == JOptionPane.YES_OPTION) {
                System.exit(0); // Ferme tout le programme Java instantanément
            }
        });
    }
}