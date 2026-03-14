package src;

import javax.swing.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.sql.Timestamp;

public class ReservationsAttenteController {

    private VueReservationsAttente vue;

    public ReservationsAttenteController(VueReservationsAttente vue) {
        this.vue = vue;

        // Le bouton Retour
        this.vue.getBtnRetour().addActionListener(e -> vue.dispose());

        // Chargement automatique
        chargerReservationsEnAttente();
    }

    private void chargerReservationsEnAttente() {
        vue.viderTableau();
        Connection connexion = ConnexionBDD.getConnexion();
        
        if (connexion != null) {
            try {
                // CORRECTION : Le fameux JOIN avec la table RESERVER
                // Filtre uniquement sur les statuts 'En cours'
                // Tri par date ASC : les plus anciennes en premier
                String sql = "SELECT r.id, c.nom AS coureur_nom, a.libelle AS article_nom, r.quantite, r.date_reservation, r.statut " +
                             "FROM Reservation r " +
                             "JOIN Coureur c ON r.id_coureur = c.id " +
                             "JOIN RESERVER res ON r.id = res.id_reservation " +
                             "JOIN Articles a ON res.id_article = a.id " +
                             "WHERE r.statut = 'En cours' " +
                             "ORDER BY r.date_reservation ASC";
                             
                PreparedStatement ps = connexion.prepareStatement(sql);
                ResultSet rs = ps.executeQuery();
                
                SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy 'à' HH:mm");
                boolean auMoinsUneResa = false;

                while (rs.next()) {
                    auMoinsUneResa = true;
                    Timestamp dateSQL = rs.getTimestamp("date_reservation");
                    String dateFormatee = (dateSQL != null) ? sdf.format(dateSQL) : "";

                    vue.ajouterLigneTableau(new Object[]{
                        rs.getInt("id"),
                        rs.getString("coureur_nom"),
                        rs.getString("article_nom"),
                        rs.getInt("quantite"),
                        dateFormatee,
                        rs.getString("statut")
                    });
                }

                if (!auMoinsUneResa) {
                    JOptionPane.showMessageDialog(vue, "Il n'y a aucune réservation en attente pour le moment.", "Tout est à jour", JOptionPane.INFORMATION_MESSAGE);
                }

            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(vue, "Erreur BDD : " + ex.getMessage(), "Erreur", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
}