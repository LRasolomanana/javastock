package src;

public class MenuReapproController {
    private VueMenuReappro vue;
    private VueMenuPrincipal vuePrincipale;

    public MenuReapproController(VueMenuReappro vue, VueMenuPrincipal vuePrincipale) {
        this.vue = vue;
        this.vuePrincipale = vuePrincipale;
        initialiserEcouteurs();
    }

    private void initialiserEcouteurs() {
        // =========================================================
        // BOUTONS GESTION FOURNISSEUR
        // =========================================================
        
        // Clic sur "1- Créer un nouveau fournisseur" (Onglet 0)
        vue.getBtnCreerFournisseur().addActionListener(e -> {
            VueGestionFournisseur vueFournisseur = new VueGestionFournisseur();
            new GestionFournisseurController(vueFournisseur); 
            vueFournisseur.getOnglets().setSelectedIndex(0); // Force l'onglet Créer
            vueFournisseur.setVisible(true);
        });

        // Clic sur "2- Modifier un fournisseur" (Onglet 1)
        vue.getBtnModifierFournisseur().addActionListener(e -> {
            VueGestionFournisseur vueFournisseur = new VueGestionFournisseur();
            new GestionFournisseurController(vueFournisseur);
            vueFournisseur.getOnglets().setSelectedIndex(1); // Force l'onglet Modifier
            vueFournisseur.setVisible(true);
        });
        
        // Clic sur "3- Afficher un fournisseur" (Onglet 2)
        vue.getBtnAfficherFournisseur().addActionListener(e -> {
            VueGestionFournisseur vueFournisseur = new VueGestionFournisseur();
            new GestionFournisseurController(vueFournisseur);
            vueFournisseur.getOnglets().setSelectedIndex(2); // Force l'onglet Consulter
            vueFournisseur.setVisible(true);
        });

        // =========================================================
        // BOUTONS GESTION POINT DE LIVRAISON
        // =========================================================
        
        // (Même logique, il faudra juste ajouter "private JTabbedPane onglets;" 
        // dans ta VueGestionPointLivraison !)
        
        vue.getBtnCreerPointLivraison().addActionListener(e -> {
            VueGestionPointLivraison vueLivraison = new VueGestionPointLivraison();
            new GestionPointLivraisonController(vueLivraison); 
            // vueLivraison.getOnglets().setSelectedIndex(0);
            vueLivraison.setVisible(true);
        });
        
        vue.getBtnModifierPointLivraison().addActionListener(e -> {
            VueGestionPointLivraison vueLivraison = new VueGestionPointLivraison();
            new GestionPointLivraisonController(vueLivraison); 
            // vueLivraison.getOnglets().setSelectedIndex(1);
            vueLivraison.setVisible(true);
        });
        
        vue.getBtnAfficherPointLivraison().addActionListener(e -> {
            VueGestionPointLivraison vueLivraison = new VueGestionPointLivraison();
            new GestionPointLivraisonController(vueLivraison); 
            // vueLivraison.getOnglets().setSelectedIndex(2);
            vueLivraison.setVisible(true);
        });
    
    // =========================================================
        // BOUTONS GESTION DEMANDE RÉAPPROVISIONNEMENT
        // =========================================================
        
        vue.getBtnCreerDemande().addActionListener(e -> {
            VueGestionDemandeReappro vueDemande = new VueGestionDemandeReappro();
            new GestionDemandeReapproController(vueDemande); 
            vueDemande.getOnglets().setSelectedIndex(0); // Force l'onglet Créer
            vueDemande.setVisible(true);
        });
        
        vue.getBtnModifierDemande().addActionListener(e -> {
            VueGestionDemandeReappro vueDemande = new VueGestionDemandeReappro();
            new GestionDemandeReapproController(vueDemande); 
            vueDemande.getOnglets().setSelectedIndex(1); // Force l'onglet Modifier
            vueDemande.setVisible(true);
        });
        
        vue.getBtnAfficherDemande().addActionListener(e -> {
            VueGestionDemandeReappro vueDemande = new VueGestionDemandeReappro();
            new GestionDemandeReapproController(vueDemande); 
            vueDemande.getOnglets().setSelectedIndex(2); // Force l'onglet Consulter
            vueDemande.setVisible(true);
        });
    }
}