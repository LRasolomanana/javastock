package src;

import javax.swing.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class ProduitsRuptureController {

    private VueProduitsRupture vue;

    public ProduitsRuptureController(VueProduitsRupture vue) {
        this.vue = vue;

        // Le bouton Retour ferme la fenêtre
        this.vue.getBtnRetour().addActionListener(e -> vue.dispose());

        // On charge les données dès l'ouverture
        chargerProduitsEnRupture();
    }

    private void chargerProduitsEnRupture() {
        vue.viderTableau();
        Connection connexion = ConnexionBDD.getConnexion();
        
        if (connexion != null) {
            try {
                // La magie est ici : on cherche uniquement les articles où quantité <= 0 
                // et qui n'ont pas été supprimés logiquement (indicateur_sl = FALSE)
                String sql = "SELECT id, libelle, quantite FROM Articles WHERE quantite <= 0 AND indicateur_sl = FALSE ORDER BY libelle ASC";
                             
                PreparedStatement ps = connexion.prepareStatement(sql);
                ResultSet rs = ps.executeQuery();
                
                boolean auMoinsUnProduit = false;

                while (rs.next()) {
                    auMoinsUnProduit = true;
                    // On ajoute la ligne au tableau visuel
                    vue.ajouterLigneTableau(new Object[]{
                        rs.getInt("id"),
                        rs.getString("libelle"),
                        rs.getInt("quantite")
                    });
                }

                // Petit bonus pro : si tout va bien, on affiche un message rassurant !
                if (!auMoinsUnProduit) {
                    JOptionPane.showMessageDialog(vue, "Excellente nouvelle : aucun produit n'est en rupture de stock !", "Stock optimal", JOptionPane.INFORMATION_MESSAGE);
                }

            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(vue, "Erreur BDD : " + ex.getMessage(), "Erreur", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
}