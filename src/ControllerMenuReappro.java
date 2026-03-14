package src;

import javax.swing.JOptionPane;

public class ControllerMenuReappro {
    private VueMenuReappro vue;

    public ControllerMenuReappro(VueMenuReappro vue) {
        this.vue = vue;
        initialiserEcouteurs();
    }

    private void initialiserEcouteurs() {
        // Clic sur "1- Créer un nouveau fournisseur"
        vue.getBtnCreerFournisseur().addActionListener(e -> {
            VueGestionFournisseur vueFournisseur = new VueGestionFournisseur();
            new GestionFournisseurController(vueFournisseur); // On attache le contrôleur
            vueFournisseur.setVisible(true);
        });

        // Placeholder pour les autres boutons (pour l'instant on affiche juste un message)
        vue.getBtnModifierFournisseur().addActionListener(e -> 
            JOptionPane.showMessageDialog(vue, "La modification se fera via la même fenêtre !")
        );
        
        vue.getBtnAfficherFournisseur().addActionListener(e -> 
            JOptionPane.showMessageDialog(vue, "La consultation se fera via la même fenêtre !")
        );

        // Tu pourras ajouter les écouteurs pour les Points de Livraison plus tard ici
    }
}