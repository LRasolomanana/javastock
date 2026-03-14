package src;

import javax.swing.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class AnnulerReservationController {

    private VueAnnulerReservation vue;

    public AnnulerReservationController(VueAnnulerReservation vue) {
        this.vue = vue;

        vue.getBtnRetour().addActionListener(e -> vue.dispose());
        vue.getBtnAnnulerResa().addActionListener(e -> annulerEtRestaurerStock());

        chargerReservations();
    }

    private void chargerReservations() {
        vue.viderTableau();
        Connection connexion = ConnexionBDD.getConnexion();
        if (connexion != null) {
            try {
                // CORRECTION : On passe par RESERVER pour lier la réservation à l'article
                String sql = "SELECT r.id, c.nom AS coureur_nom, a.libelle AS article_nom, r.quantite, r.statut " +
                             "FROM Reservation r " +
                             "JOIN Coureur c ON r.id_coureur = c.id " +
                             "JOIN RESERVER res ON r.id = res.id_reservation " +
                             "JOIN Articles a ON res.id_article = a.id " +
                             "ORDER BY r.id ASC";
                             
                PreparedStatement ps = connexion.prepareStatement(sql);
                ResultSet rs = ps.executeQuery();
                
                while (rs.next()) {
                    vue.ajouterLigneTableau(new Object[]{
                        rs.getInt("id"),
                        rs.getString("coureur_nom"),
                        rs.getString("article_nom"),
                        rs.getInt("quantite"),
                        rs.getString("statut")
                    });
                }
            } catch (SQLException ex) {
                System.out.println("Erreur chargement réservations : " + ex.getMessage());
            }
        }
    }

   private void annulerEtRestaurerStock() {
        int ligne = vue.getTable().getSelectedRow();
        if (ligne == -1) {
            JOptionPane.showMessageDialog(vue, "Veuillez d'abord sélectionner une réservation dans le tableau.", "Avertissement", JOptionPane.WARNING_MESSAGE);
            return;
        }

        // --- LA SÉCURITÉ : VÉRIFICATION DU STATUT ---
        String statutActuel = vue.getTable().getValueAt(ligne, 4).toString();
        if (statutActuel.equalsIgnoreCase("Terminée")) {
            JOptionPane.showMessageDialog(vue, "Cette réservation est 'Terminée'. Elle fait partie de l'historique et ne peut plus être supprimée.", "Action impossible", JOptionPane.ERROR_MESSAGE);
            return; // On bloque tout !
        }
        // ---------------------------------------------

        int idReservation = Integer.parseInt(vue.getTable().getValueAt(ligne, 0).toString());
        String nomCoureur = vue.getTable().getValueAt(ligne, 1).toString();
        String nomArticle = vue.getTable().getValueAt(ligne, 2).toString();

        int choix = JOptionPane.showConfirmDialog(vue, 
            "Voulez-vous vraiment supprimer la réservation de " + nomCoureur + " pour l'article : " + nomArticle + " ?\n(La quantité sera remise en stock)", 
            "Confirmation de suppression", 
            JOptionPane.YES_NO_OPTION, 
            JOptionPane.WARNING_MESSAGE);

        if (choix == JOptionPane.YES_OPTION) {
            Connection connexion = ConnexionBDD.getConnexion();
            if (connexion != null) {
                try {
                    // CORRECTION : On va chercher id_article via la table RESERVER
                    String sqlGetInfo = "SELECT res.id_article, r.quantite " +
                                        "FROM Reservation r " +
                                        "JOIN RESERVER res ON r.id = res.id_reservation " +
                                        "WHERE r.id = ?";
                    PreparedStatement psGetInfo = connexion.prepareStatement(sqlGetInfo);
                    psGetInfo.setInt(1, idReservation);
                    ResultSet rsInfo = psGetInfo.executeQuery();
                    
                    if (rsInfo.next()) {
                        int idArticle = rsInfo.getInt("id_article");
                        int quantiteARendre = rsInfo.getInt("quantite");

                        // 1. On recrédite le stock de l'article
                        String sqlRestoreStock = "UPDATE Articles SET quantite = quantite + ? WHERE id = ?";
                        PreparedStatement psRestore = connexion.prepareStatement(sqlRestoreStock);
                        psRestore.setInt(1, quantiteARendre);
                        psRestore.setInt(2, idArticle);
                        psRestore.executeUpdate();

                        // 2. On supprime la réservation (le ON DELETE CASCADE s'occupe de vider la table RESERVER)
                        String sqlDelete = "DELETE FROM Reservation WHERE id = ?";
                        PreparedStatement psDelete = connexion.prepareStatement(sqlDelete);
                        psDelete.setInt(1, idReservation);
                        psDelete.executeUpdate();

                        JOptionPane.showMessageDialog(vue, "Réservation supprimée ! Le stock a été restauré.", "Succès", JOptionPane.INFORMATION_MESSAGE);
                        
                        chargerReservations();
                    }
                } catch (SQLException ex) {
                    JOptionPane.showMessageDialog(vue, "Erreur BDD : " + ex.getMessage(), "Erreur", JOptionPane.ERROR_MESSAGE);
                }
            }
        }
    }
}