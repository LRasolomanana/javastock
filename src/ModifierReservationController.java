package src;

import javax.swing.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class ModifierReservationController {

    private VueModifierReservation vue;

    public ModifierReservationController(VueModifierReservation vue) {
        this.vue = vue;

        chargerReservations();

        vue.getBtnRetour().addActionListener(e -> vue.dispose());
        vue.getBtnModifier().addActionListener(e -> modifierReservation());

        // Quand on clique sur le tableau, ça pré-remplit le formulaire
        vue.getTable().getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting() && vue.getTable().getSelectedRow() != -1) {
                int ligne = vue.getTable().getSelectedRow();
                vue.getTxtNouvelleQuantite().setText(vue.getTable().getValueAt(ligne, 3).toString());
                vue.getComboStatut().setSelectedItem(vue.getTable().getValueAt(ligne, 4).toString());
            }
        });
    }

    // --- CORRECTION DU JOIN : On passe par la table RESERVER ---
    private void chargerReservations() {
        vue.viderTableau();
        Connection connexion = ConnexionBDD.getConnexion();
        if (connexion != null) {
            try {
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

   private void modifierReservation() {
        int ligne = vue.getTable().getSelectedRow();
        if (ligne == -1) {
            JOptionPane.showMessageDialog(vue, "Veuillez sélectionner une réservation dans le tableau.", "Avertissement", JOptionPane.WARNING_MESSAGE);
            return;
        }

        // --- LA SÉCURITÉ : VÉRIFICATION DU STATUT ---
        String statutActuel = vue.getTable().getValueAt(ligne, 4).toString();
        if (statutActuel.equalsIgnoreCase("Terminée")) {
            JOptionPane.showMessageDialog(vue, "Cette réservation est 'Terminée'. Elle est verrouillée et ne peut plus être modifiée.", "Action impossible", JOptionPane.ERROR_MESSAGE);
            return; 
        }
        // ---------------------------------------------

        int idReservation = Integer.parseInt(vue.getTable().getValueAt(ligne, 0).toString());
        String qteStr = vue.getTxtNouvelleQuantite().getText();
        String nouveauStatut = (String) vue.getComboStatut().getSelectedItem();

        try {
            int nouvelleQte = Integer.parseInt(qteStr);
            if (nouvelleQte <= 0) {
                JOptionPane.showMessageDialog(vue, "La quantité doit être supérieure à 0.", "Erreur", JOptionPane.ERROR_MESSAGE);
                return;
            }

            Connection connexion = ConnexionBDD.getConnexion();
            if (connexion != null) {
                
                // CORRECTION : On va chercher id_article via la table de jointure RESERVER
                String sqlGetOld = "SELECT res.id_article, r.quantite " +
                                   "FROM Reservation r " +
                                   "JOIN RESERVER res ON r.id = res.id_reservation " +
                                   "WHERE r.id = ?";
                PreparedStatement psOld = connexion.prepareStatement(sqlGetOld);
                psOld.setInt(1, idReservation);
                ResultSet rsOld = psOld.executeQuery();
                
                if (rsOld.next()) {
                    int idArticle = rsOld.getInt("id_article");
                    int ancienneQte = rsOld.getInt("quantite");
                    int difference = nouvelleQte - ancienneQte;

                    // Gestion du stock si la quantité augmente
                    if (difference > 0) {
                        String sqlStock = "SELECT quantite FROM Articles WHERE id = ?";
                        PreparedStatement psStock = connexion.prepareStatement(sqlStock);
                        psStock.setInt(1, idArticle);
                        ResultSet rsStock = psStock.executeQuery();
                        if (rsStock.next()) {
                            if (difference > rsStock.getInt("quantite")) {
                                JOptionPane.showMessageDialog(vue, "Stock insuffisant pour augmenter la réservation ! Il ne reste que " + rsStock.getInt("quantite") + " article(s).", "Erreur de stock", JOptionPane.ERROR_MESSAGE);
                                return;
                            }
                        }
                    }

                    // 1. Mise à jour du stock d'articles (marche aussi pour les retours si diff est négative)
                    String sqlUpdateStock = "UPDATE Articles SET quantite = quantite - ? WHERE id = ?";
                    PreparedStatement psUpdStock = connexion.prepareStatement(sqlUpdateStock);
                    psUpdStock.setInt(1, difference);
                    psUpdStock.setInt(2, idArticle);
                    psUpdStock.executeUpdate();

                    // 2. Mise à jour de la table Reservation
                    String sqlUpdateResa = "UPDATE Reservation SET quantite = ?, statut = ? WHERE id = ?";
                    PreparedStatement psUpdResa = connexion.prepareStatement(sqlUpdateResa);
                    psUpdResa.setInt(1, nouvelleQte);
                    psUpdResa.setString(2, nouveauStatut);
                    psUpdResa.setInt(3, idReservation);
                    psUpdResa.executeUpdate();
                    
                    // 3. CORRECTION : Mise à jour de la table de jointure RESERVER
                    String sqlUpdateReserver = "UPDATE RESERVER SET quantite_reservee = ? WHERE id_reservation = ?";
                    PreparedStatement psUpdReserver = connexion.prepareStatement(sqlUpdateReserver);
                    psUpdReserver.setInt(1, nouvelleQte);
                    psUpdReserver.setInt(2, idReservation);
                    psUpdReserver.executeUpdate();

                    JOptionPane.showMessageDialog(vue, "Réservation et stock mis à jour avec succès !", "Succès", JOptionPane.INFORMATION_MESSAGE);
                    chargerReservations(); 
                }
            }
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(vue, "Quantité invalide.", "Erreur", JOptionPane.ERROR_MESSAGE);
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(vue, "Erreur SQL : " + ex.getMessage(), "Erreur", JOptionPane.ERROR_MESSAGE);
        }
    }
}