package src;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.time.temporal.ChronoUnit;

public class HebergementController {

    private VueGestionHebergement vue;

    public HebergementController(VueGestionHebergement vue) {
        this.vue = vue;
        initController();
    }

    private void initController() {
        vue.getBtnValider().addActionListener(e -> enregistrerReservationEnBase());
    }

    private void enregistrerReservationEnBase() {
        try {
            // 1. Récupération des données depuis l'interface graphique
            Coureur coureur = vue.getCoureurSelectionne();
            String typeHebergement = vue.getTypeHebergementSelectionne();
            LocalDate debut = LocalDate.parse(vue.getDateDebut());
            LocalDate fin = LocalDate.parse(vue.getDateFin());

            // 2. Validation métier (Les dates)
            if (fin.isBefore(debut) || fin.isEqual(debut)) {
                vue.afficherErreur("La date de départ doit être après la date d'arrivée.");
                return;
            }
            int nuites = (int) ChronoUnit.DAYS.between(debut, fin);

            // 3. Récupération de la connexion (hors du try-with-resources pour gérer la transaction manuellement)
            Connection conn = ConnexionBDD.getConnexion();
            
            if (conn == null || conn.isClosed()) {
                vue.afficherErreur("Impossible de se connecter à la base de données.");
                return;
            }

            // --- DEBUT DE LA TRANSACTION ---
            // On désactive l'autocommit pour s'assurer que les deux requêtes (reservation + hebergement)
            // s'exécutent toutes les deux ou aucune (en cas d'erreur).
            conn.setAutoCommit(false); 

            try {
                // ETAPE A : Insérer dans la table `reservation`
                // On utilise RETURN_GENERATED_KEYS pour récupérer l'ID créé par la séquence (SERIAL)
                String sqlReservation = "INSERT INTO reservation (date_reservation, statut, quantite, id_coureur) VALUES (?, ?, ?, ?)";
                int newReservationId = -1;

                try (PreparedStatement stmtResa = conn.prepareStatement(sqlReservation, Statement.RETURN_GENERATED_KEYS)) {
                    stmtResa.setDate(1, java.sql.Date.valueOf(LocalDate.now())); // Date d'aujourd'hui pour la création du dossier
                    stmtResa.setString(2, "Hébergement"); // Statut ou type pour identifier la réservation
                    stmtResa.setInt(3, 1); // Quantité standard (1 logement)
                    stmtResa.setInt(4, coureur.getId()); // On lie la réservation au coureur sélectionné

                    stmtResa.executeUpdate();

                    // Récupération de l'ID généré pour la réservation
                    try (ResultSet generatedKeys = stmtResa.getGeneratedKeys()) {
                        if (generatedKeys.next()) {
                            newReservationId = generatedKeys.getInt(1);
                        } else {
                            throw new SQLException("Échec de la création de la réservation, aucun ID obtenu.");
                        }
                    }
                }

                // ETAPE B : Insérer dans la table `reserver_hebergement` en utilisant le newReservationId
                String sqlHebergement = "INSERT INTO reserver_hebergement (id_reservation, id_hebergement, date_debut, date_fin, nombre_nuites) " +
                                        "VALUES (?, (SELECT id FROM hebergement WHERE type_logement = ?), ?, ?, ?)";
                
                try (PreparedStatement stmtHeberg = conn.prepareStatement(sqlHebergement)) {
                    stmtHeberg.setInt(1, newReservationId);
                    stmtHeberg.setString(2, typeHebergement);
                    stmtHeberg.setDate(3, java.sql.Date.valueOf(debut));
                    stmtHeberg.setDate(4, java.sql.Date.valueOf(fin));
                    stmtHeberg.setInt(5, nuites);

                    stmtHeberg.executeUpdate();
                }

                // Si les deux requêtes sont passées avec succès, on valide la transaction (Commit)
                conn.commit();

                // 4. Affichage du succès
                String recap = "=== SUCCÈS DE L'ENREGISTREMENT ===\n" +
                               "Dossier Réservation N° " + newReservationId + " créé.\n" +
                               "Coureur : " + coureur.getPrenom() + " " + coureur.getNom().toUpperCase() + "\n" +
                               "Logement : " + typeHebergement + " (" + nuites + " nuits)\n" +
                               "Séjour : Du " + debut + " au " + fin + "\n" +
                               "---------------------------------------------------\n" +
                               "✅ Test validé avec succès !";
                vue.afficherSucces(recap);

            } catch (SQLException ex) {
                // En cas d'erreur (ex: problème de clé étrangère), on annule tout (Rollback)
                conn.rollback();
                System.err.println("Transaction annulée (Rollback) suite à une erreur SQL : " + ex.getMessage());
                vue.afficherErreur("Erreur lors de l'enregistrement en base (Transaction annulée) : \n" + ex.getMessage());
            } finally {
                // On rétablit l'autocommit par défaut pour ne pas perturber les autres parties de l'application
                conn.setAutoCommit(true);
                // Note : On ne ferme PAS la connexion ici car elle est gérée par le Singleton (ConnexionBDD)
            }

        } catch (DateTimeParseException ex) {
            vue.afficherErreur("Format de date incorrect. Veuillez utiliser AAAA-MM-JJ (ex: 2026-03-20).");
        } catch (Exception ex) {
            vue.afficherErreur("Une erreur inattendue est survenue : " + ex.getMessage());
            ex.printStackTrace(); // Utile pour déboguer dans la console
        }
    }
}