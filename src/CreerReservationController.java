package src;

import javax.swing.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.sql.SQLException;

public class CreerReservationController {

    private VueCreerReservation vue;

    public CreerReservationController(VueCreerReservation vue) {
        this.vue = vue;

        chargerEpreuves(); // On charge d'abord les épreuves
        chargerArticles();

        // LA MAGIE EST ICI : On écoute les changements sur la liste des épreuves
        vue.getComboEpreuve().addActionListener(e -> chargerCoureursParEpreuve());

        // On charge les coureurs pour la toute première épreuve sélectionnée par défaut
        chargerCoureursParEpreuve();

        vue.getBtnAnnuler().addActionListener(e -> vue.dispose());
        vue.getBtnValider().addActionListener(e -> validerReservation());
    }

    // --- 1. CHARGER LES ÉPREUVES ---
    private void chargerEpreuves() {
        vue.getComboEpreuve().removeAllItems();
        Connection connexion = ConnexionBDD.getConnexion();
        if (connexion != null) {
            try {
                // CORRECTION : type_epreuve au lieu de TypeEpreuve, et libelle au lieu de nom
                String sql = "SELECT id, libelle FROM type_epreuve ORDER BY libelle ASC";
                PreparedStatement ps = connexion.prepareStatement(sql);
                ResultSet rs = ps.executeQuery();
                while (rs.next()) {
                    vue.getComboEpreuve().addItem(rs.getInt("id") + " - " + rs.getString("libelle"));
                }
            } catch (SQLException ex) {
                System.out.println("Erreur épreuves : " + ex.getMessage());
            }
        }
    }

    // --- 2. CHARGER LES COUREURS (FILTRÉS) ---
    private void chargerCoureursParEpreuve() {
        vue.getComboCoureur().removeAllItems(); // On vide l'ancienne liste
        
        String epreuveSel = (String) vue.getComboEpreuve().getSelectedItem();
        if (epreuveSel == null) return; // Sécurité si la liste est vide

        int idEpreuve = Integer.parseInt(epreuveSel.split(" - ")[0]);

        Connection connexion = ConnexionBDD.getConnexion();
        if (connexion != null) {
            try {
                // CORRECTION : id_type_epreuve au lieu de id_epreuve
                String sql = "SELECT id, nom, prenom FROM Coureur WHERE id_type_epreuve = ? ORDER BY nom ASC";
                PreparedStatement ps = connexion.prepareStatement(sql);
                ps.setInt(1, idEpreuve);
                ResultSet rs = ps.executeQuery();
                
                boolean aDesCoureurs = false;
                while (rs.next()) {
                    aDesCoureurs = true;
                    String prenom = rs.getString("prenom") != null ? rs.getString("prenom") : "";
                    vue.getComboCoureur().addItem(rs.getInt("id") + " - " + rs.getString("nom") + " " + prenom);
                }

                if (!aDesCoureurs) {
                    vue.getComboCoureur().addItem("Aucun participant pour cette épreuve");
                }

            } catch (SQLException ex) {
                System.out.println("Erreur chargement coureurs : " + ex.getMessage());
            }
        }
    }

    // --- 3. CHARGER LES ARTICLES ---
    private void chargerArticles() {
        Connection connexion = ConnexionBDD.getConnexion();
        if (connexion != null) {
            try {
                String sql = "SELECT id, libelle, quantite FROM Articles WHERE indicateur_sl = FALSE AND quantite > 0 ORDER BY libelle ASC";
                PreparedStatement ps = connexion.prepareStatement(sql);
                ResultSet rs = ps.executeQuery();
                while (rs.next()) {
                    vue.getComboArticle().addItem(rs.getInt("id") + " - " + rs.getString("libelle") + " (Stock: " + rs.getInt("quantite") + ")");
                }
            } catch (SQLException ex) {
                System.out.println("Erreur chargement articles : " + ex.getMessage());
            }
        }
    }

    // --- 4. VALIDATION ---
    private void validerReservation() {
        String coureurSel = (String) vue.getComboCoureur().getSelectedItem();
        String articleSel = (String) vue.getComboArticle().getSelectedItem();
        String quantiteStr = vue.getQuantite();

        // Sécurité si on essaie de valider alors qu'il n'y a pas de coureur dans l'épreuve
        if (coureurSel == null || coureurSel.equals("Aucun participant pour cette épreuve") || articleSel == null || quantiteStr.isEmpty()) {
            JOptionPane.showMessageDialog(vue, "Veuillez remplir correctement tous les champs.", "Erreur", JOptionPane.WARNING_MESSAGE);
            return;
        }

        try {
            int quantiteDemandee = Integer.parseInt(quantiteStr);
            if (quantiteDemandee <= 0) {
                JOptionPane.showMessageDialog(vue, "La quantité doit être supérieure à 0.", "Erreur", JOptionPane.ERROR_MESSAGE);
                return;
            }

            int idCoureur = Integer.parseInt(coureurSel.split(" - ")[0]);
            int idArticle = Integer.parseInt(articleSel.split(" - ")[0]);

            Connection connexion = ConnexionBDD.getConnexion();
            if (connexion != null) {
                // 1. Vérification du stock
                String sqlCheckStock = "SELECT quantite FROM Articles WHERE id = ?";
                PreparedStatement psCheck = connexion.prepareStatement(sqlCheckStock);
                psCheck.setInt(1, idArticle);
                ResultSet rsStock = psCheck.executeQuery();
                
                if (rsStock.next()) {
                    int stockActuel = rsStock.getInt("quantite");
                    if (quantiteDemandee > stockActuel) {
                        JOptionPane.showMessageDialog(vue, "Stock insuffisant ! Il ne reste que " + stockActuel + " exemplaire(s).", "Rupture", JOptionPane.ERROR_MESSAGE);
                        return;
                    }
                }

                // 2. Début de la transaction (sécurité)
                connexion.setAutoCommit(false); 

                try {
                    // 3. Création de la réservation (sans l'article)
                    String sqlInsertResa = "INSERT INTO Reservation (date_reservation, statut, quantite, id_coureur) VALUES (CURRENT_DATE, 'En attente', ?, ?)";
                    // On demande à récupérer l'ID généré automatiquement
                    PreparedStatement psInsertResa = connexion.prepareStatement(sqlInsertResa, Statement.RETURN_GENERATED_KEYS);
                    psInsertResa.setInt(1, quantiteDemandee);
                    psInsertResa.setInt(2, idCoureur);
                    psInsertResa.executeUpdate();

                    // 4. Récupération de l'ID de la nouvelle réservation
                    ResultSet rsKeys = psInsertResa.getGeneratedKeys();
                    int idNouvelleReservation = -1;
                    if (rsKeys.next()) {
                        idNouvelleReservation = rsKeys.getInt(1);
                    }

                    // 5. Ajout dans la table de liaison RESERVER
                    String sqlInsertReserver = "INSERT INTO RESERVER (id_reservation, id_article, quantite_reservee) VALUES (?, ?, ?)";
                    PreparedStatement psInsertReserver = connexion.prepareStatement(sqlInsertReserver);
                    psInsertReserver.setInt(1, idNouvelleReservation);
                    psInsertReserver.setInt(2, idArticle);
                    psInsertReserver.setInt(3, quantiteDemandee);
                    psInsertReserver.executeUpdate();

                    // 6. Mise à jour du stock de l'article
                    String sqlUpdateStock = "UPDATE Articles SET quantite = quantite - ? WHERE id = ?";
                    PreparedStatement psUpdate = connexion.prepareStatement(sqlUpdateStock);
                    psUpdate.setInt(1, quantiteDemandee);
                    psUpdate.setInt(2, idArticle);
                    psUpdate.executeUpdate();

                    // 7. On valide définitivement toutes les opérations
                    connexion.commit(); 

                    JOptionPane.showMessageDialog(vue, "Réservation effectuée avec succès ! Le stock a été mis à jour.", "Succès", JOptionPane.INFORMATION_MESSAGE);
                    vue.dispose();

                } catch (SQLException e) {
                    // S'il y a eu la moindre erreur, on annule tout (le stock, la réservation, etc.)
                    connexion.rollback(); 
                    throw e; 
                } finally {
                    // On remet la connexion en mode normal
                    connexion.setAutoCommit(true); 
                }
            }

        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(vue, "La quantité doit être un nombre entier valide.", "Erreur", JOptionPane.ERROR_MESSAGE);
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(vue, "Erreur BDD : " + ex.getMessage(), "Erreur", JOptionPane.ERROR_MESSAGE);
        }
    }
}