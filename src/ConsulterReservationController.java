package src;

import javax.swing.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.sql.Timestamp;

public class ConsulterReservationController {

    private VueConsulterReservation vue;

    public ConsulterReservationController(VueConsulterReservation vue) {
        this.vue = vue;

        // Le bouton Retour ferme la fenêtre
        this.vue.getBtnRetour().addActionListener(e -> vue.dispose());

        // On charge les données dès l'ouverture
        chargerHistorique();
    }

    private void chargerHistorique() {
        vue.viderTableau();
        Connection connexion = ConnexionBDD.getConnexion();
        
        if (connexion != null) {
            try {
                // CORRECTION : Ajout du JOIN avec la table RESERVER (res)
                String sql = "SELECT r.id, c.nom AS coureur_nom, a.libelle AS article_nom, r.quantite, r.date_reservation, r.statut " +
                             "FROM Reservation r " +
                             "JOIN Coureur c ON r.id_coureur = c.id " +
                             "JOIN RESERVER res ON r.id = res.id_reservation " +
                             "JOIN Articles a ON res.id_article = a.id " +
                             "ORDER BY r.date_reservation DESC";
                             
                PreparedStatement ps = connexion.prepareStatement(sql);
                ResultSet rs = ps.executeQuery();
                
                // Un outil pour formater la date proprement (ex: "25/10/2023 à 14:30")
                SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy 'à' HH:mm");
                
                while (rs.next()) {
                    // On récupère la date au format SQL
                    Timestamp dateSQL = rs.getTimestamp("date_reservation");
                    String dateFormatee = "";
                    if (dateSQL != null) {
                        dateFormatee = sdf.format(dateSQL); // On la transforme en texte joli
                    }

                    // On ajoute la ligne au tableau visuel
                    vue.ajouterLigneTableau(new Object[]{
                        rs.getInt("id"),
                        rs.getString("coureur_nom"),
                        rs.getString("article_nom"),
                        rs.getInt("quantite"),
                        dateFormatee,
                        rs.getString("statut")
                    });
                }
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(vue, "Erreur BDD : " + ex.getMessage(), "Erreur", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
}