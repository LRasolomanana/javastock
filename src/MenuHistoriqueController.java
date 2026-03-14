package src;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import javax.swing.*;

public class MenuHistoriqueController {

    private VueMenuHistorique vue;
    private VueMenuPrincipal menuPrincipal;

    public MenuHistoriqueController(VueMenuHistorique vue, VueMenuPrincipal menuPrincipal) {
        this.vue = vue;
        this.menuPrincipal = menuPrincipal;

        initialiserActions();
    }

    private void initialiserActions() {
        
        // --- Les actions reliées ---
        vue.getBtnNbResaDate().addActionListener(e -> statNombreReservationParDate());
        vue.getBtnNbResaCoureur().addActionListener(e -> statNombreReservationParCoureur());
        vue.getBtnNbResaEpreuve().addActionListener(e -> statNombreReservationParEpreuve());
        vue.getBtnNbResaDateEpreuve().addActionListener(e -> statNombreReservationDateEtEpreuve());
        vue.getBtnQteArticleDate().addActionListener(e -> statQuantiteParArticleADate());
       vue.getBtnQteEpreuve().addActionListener(e -> statQuantiteParEpreuve());
        vue.getBtnQteDateEpreuve().addActionListener(e -> statQuantiteParDateEtEpreuve());

        // 8- Revenir au menu principal
        vue.getBtnRetourMenu().addActionListener(e -> {
            vue.dispose(); 
            menuPrincipal.setVisible(true); 
        });

        // 9- Quitter l'application
        vue.getBtnQuitter().addActionListener(e -> {
            int choix = JOptionPane.showConfirmDialog(vue, "Voulez-vous vraiment quitter l'application ?", "Confirmation", JOptionPane.YES_NO_OPTION);
            if (choix == JOptionPane.YES_OPTION) {
                System.exit(0);
            }
        });
    }

    private void statNombreReservationParEpreuve() {
        Connection connexion = ConnexionBDD.getConnexion();
        if (connexion != null) {
            try {
                // CORRECTION : type_epreuve et libelle
                String sqlEpreuves = "SELECT id, libelle FROM type_epreuve ORDER BY libelle ASC";
                PreparedStatement psEpreuves = connexion.prepareStatement(sqlEpreuves);
                ResultSet rsEpreuves = psEpreuves.executeQuery();
                
                ArrayList<String> listeEpreuves = new ArrayList<>();
                while (rsEpreuves.next()) {
                    listeEpreuves.add(rsEpreuves.getInt("id") + " - " + rsEpreuves.getString("libelle"));
                }

                if (listeEpreuves.isEmpty()) {
                    JOptionPane.showMessageDialog(vue, "Aucune épreuve n'existe dans la base.", "Erreur", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                String epreuveChoisie = (String) JOptionPane.showInputDialog(vue, 
                    "Choisissez le type d'épreuve :", 
                    "Statistiques", 
                    JOptionPane.QUESTION_MESSAGE, 
                    null, 
                    listeEpreuves.toArray(), 
                    listeEpreuves.get(0));

                if (epreuveChoisie != null) {
                    int idEpreuve = Integer.parseInt(epreuveChoisie.split(" - ")[0]);
                    String nomEpreuve = epreuveChoisie.split(" - ")[1];

                    // CORRECTION : c.id_type_epreuve
                    String sqlStat = "SELECT COUNT(r.id) AS total_resa " +
                                     "FROM Reservation r " +
                                     "JOIN Coureur c ON r.id_coureur = c.id " +
                                     "WHERE c.id_type_epreuve = ?";
                                     
                    PreparedStatement psStat = connexion.prepareStatement(sqlStat);
                    psStat.setInt(1, idEpreuve);
                    ResultSet rsStat = psStat.executeQuery();

                    if (rsStat.next()) {
                        int total = rsStat.getInt("total_resa");
                        JOptionPane.showMessageDialog(vue, 
                            "📊 RÉSULTAT DE LA STATISTIQUE :\n\n" +
                            "Il y a actuellement " + total + " réservation(s) pour les participants de l'épreuve : " + nomEpreuve, 
                            "Résultat", 
                            JOptionPane.INFORMATION_MESSAGE);
                    }
                }

            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(vue, "Erreur BDD : " + ex.getMessage(), "Erreur", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
    
    private void statNombreReservationParCoureur() {
        Connection connexion = ConnexionBDD.getConnexion();
        if (connexion != null) {
            try {
                String sqlCoureurs = "SELECT id, nom FROM Coureur ORDER BY nom ASC";
                PreparedStatement psCoureurs = connexion.prepareStatement(sqlCoureurs);
                ResultSet rsCoureurs = psCoureurs.executeQuery();
                
                java.util.ArrayList<String> listeCoureurs = new java.util.ArrayList<>();
                while (rsCoureurs.next()) {
                    listeCoureurs.add(rsCoureurs.getInt("id") + " - " + rsCoureurs.getString("nom"));
                }

                if (listeCoureurs.isEmpty()) {
                    JOptionPane.showMessageDialog(vue, "Aucun coureur n'existe.", "Erreur", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                String coureurChoisi = (String) JOptionPane.showInputDialog(vue, 
                    "Choisissez le coureur :", "Statistiques", 
                    JOptionPane.QUESTION_MESSAGE, null, listeCoureurs.toArray(), listeCoureurs.get(0));

                if (coureurChoisi != null) {
                    int idCoureur = Integer.parseInt(coureurChoisi.split(" - ")[0]);
                    String nomCoureur = coureurChoisi.split(" - ")[1];

                    String sqlStat = "SELECT COUNT(id) AS total_resa FROM Reservation WHERE id_coureur = ?";
                    PreparedStatement psStat = connexion.prepareStatement(sqlStat);
                    psStat.setInt(1, idCoureur);
                    ResultSet rsStat = psStat.executeQuery();

                    if (rsStat.next()) {
                        JOptionPane.showMessageDialog(vue, 
                            "📊 RÉSULTAT DE LA STATISTIQUE :\n\n" +
                            "Le coureur " + nomCoureur + " a effectué " + rsStat.getInt("total_resa") + " réservation(s) au total.", 
                            "Résultat", JOptionPane.INFORMATION_MESSAGE);
                    }
                }
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(vue, "Erreur BDD : " + ex.getMessage(), "Erreur", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    // --- STATISTIQUE 1 : PAR DATE DONNÉE ---
    private void statNombreReservationParDate() {
        Connection connexion = ConnexionBDD.getConnexion();
        if (connexion != null) {
            try {
                String sqlDates = "SELECT DISTINCT DATE(date_reservation) AS jour FROM Reservation ORDER BY jour DESC";
                PreparedStatement psDates = connexion.prepareStatement(sqlDates);
                ResultSet rsDates = psDates.executeQuery();
                
                java.util.ArrayList<String> listeDates = new java.util.ArrayList<>();
                java.text.SimpleDateFormat sdfVisual = new java.text.SimpleDateFormat("dd/MM/yyyy");
                java.text.SimpleDateFormat sdfSQL = new java.text.SimpleDateFormat("yyyy-MM-dd");

                while (rsDates.next()) {
                    java.sql.Date dateSQL = rsDates.getDate("jour");
                    if (dateSQL != null) {
                        listeDates.add(sdfVisual.format(dateSQL) + " (SQL:" + sdfSQL.format(dateSQL) + ")");
                    }
                }

                if (listeDates.isEmpty()) {
                    JOptionPane.showMessageDialog(vue, "Il n'y a eu aucune réservation pour l'instant.", "Erreur", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                Object[] choixEsthetiques = listeDates.stream().map(d -> d.split(" \\(SQL:")[0]).toArray();

                String dateChoisieVisuelle = (String) JOptionPane.showInputDialog(vue, 
                    "Choisissez une date d'enregistrement :", "Statistiques", 
                    JOptionPane.QUESTION_MESSAGE, null, choixEsthetiques, choixEsthetiques[0]);

                if (dateChoisieVisuelle != null) {
                    String vraieDateSQL = "";
                    for (String d : listeDates) {
                        if (d.startsWith(dateChoisieVisuelle)) {
                            vraieDateSQL = d.split("\\(SQL:")[1].replace(")", "");
                            break;
                        }
                    }

                    String sqlStat = "SELECT COUNT(id) AS total_resa FROM Reservation WHERE DATE(date_reservation) = ?::date";
                    PreparedStatement psStat = connexion.prepareStatement(sqlStat);
                    psStat.setString(1, vraieDateSQL);
                    ResultSet rsStat = psStat.executeQuery();

                    if (rsStat.next()) {
                        JOptionPane.showMessageDialog(vue, 
                            "📊 RÉSULTAT DE LA STATISTIQUE :\n\n" +
                            "À la date du " + dateChoisieVisuelle + ", il y a eu " + rsStat.getInt("total_resa") + " réservation(s) enregistrée(s).", 
                            "Résultat", JOptionPane.INFORMATION_MESSAGE);
                    }
                }
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(vue, "Erreur BDD : " + ex.getMessage(), "Erreur", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
    
    // --- STATISTIQUE 4 : PAR DATE ET PAR TYPE D'ÉPREUVE ---
    private void statNombreReservationDateEtEpreuve() {
        Connection connexion = ConnexionBDD.getConnexion();
        if (connexion != null) {
            try {
                // 1. Date
                String sqlDates = "SELECT DISTINCT DATE(date_reservation) AS jour FROM Reservation ORDER BY jour DESC";
                PreparedStatement psDates = connexion.prepareStatement(sqlDates);
                ResultSet rsDates = psDates.executeQuery();
                
                java.util.ArrayList<String> listeDates = new java.util.ArrayList<>();
                java.text.SimpleDateFormat sdfVisual = new java.text.SimpleDateFormat("dd/MM/yyyy");
                java.text.SimpleDateFormat sdfSQL = new java.text.SimpleDateFormat("yyyy-MM-dd");

                while (rsDates.next()) {
                    java.sql.Date dateSQL = rsDates.getDate("jour");
                    if (dateSQL != null) {
                        listeDates.add(sdfVisual.format(dateSQL) + " (SQL:" + sdfSQL.format(dateSQL) + ")");
                    }
                }

                if (listeDates.isEmpty()) {
                    JOptionPane.showMessageDialog(vue, "Aucune réservation trouvée dans l'historique.", "Information", JOptionPane.INFORMATION_MESSAGE);
                    return;
                }

                Object[] choixDates = listeDates.stream().map(d -> d.split(" \\(SQL:")[0]).toArray();
                String dateChoisieVisuelle = (String) JOptionPane.showInputDialog(vue, "1/2 - Choisissez la date :", "Statistiques", JOptionPane.QUESTION_MESSAGE, null, choixDates, choixDates[0]);
                if (dateChoisieVisuelle == null) return; 

                String vraieDateSQL = "";
                for (String d : listeDates) {
                    if (d.startsWith(dateChoisieVisuelle)) { vraieDateSQL = d.split("\\(SQL:")[1].replace(")", ""); break; }
                }

                // 2. Epreuve - CORRECTION : type_epreuve et libelle
                String sqlEpreuves = "SELECT id, libelle FROM type_epreuve ORDER BY libelle ASC";
                PreparedStatement psEpreuves = connexion.prepareStatement(sqlEpreuves);
                ResultSet rsEpreuves = psEpreuves.executeQuery();
                
                java.util.ArrayList<String> listeEpreuves = new java.util.ArrayList<>();
                while (rsEpreuves.next()) {
                    listeEpreuves.add(rsEpreuves.getInt("id") + " - " + rsEpreuves.getString("libelle"));
                }

                String epreuveChoisie = (String) JOptionPane.showInputDialog(vue, "2/2 - Choisissez l'épreuve :", "Statistiques", JOptionPane.QUESTION_MESSAGE, null, listeEpreuves.toArray(), listeEpreuves.get(0));
                if (epreuveChoisie == null) return;

                int idEpreuve = Integer.parseInt(epreuveChoisie.split(" - ")[0]);
                String nomEpreuve = epreuveChoisie.split(" - ")[1];

                // 3. LA REQUÊTE DOUBLE CONDITION - CORRECTION : id_type_epreuve
                String sqlStat = "SELECT COUNT(r.id) AS total_resa " +
                                 "FROM Reservation r " +
                                 "JOIN Coureur c ON r.id_coureur = c.id " +
                                 "WHERE DATE(r.date_reservation) = ?::date AND c.id_type_epreuve = ?";
                                 
                PreparedStatement psStat = connexion.prepareStatement(sqlStat);
                psStat.setString(1, vraieDateSQL);
                psStat.setInt(2, idEpreuve);
                ResultSet rsStat = psStat.executeQuery();

                if (rsStat.next()) {
                    JOptionPane.showMessageDialog(vue, 
                        "📊 RÉSULTAT DE LA STATISTIQUE :\n\n" +
                        "Le " + dateChoisieVisuelle + ", il y a eu " + rsStat.getInt("total_resa") + " réservation(s) \n" +
                        "pour l'épreuve : " + nomEpreuve + ".", 
                        "Résultat", JOptionPane.INFORMATION_MESSAGE);
                }

            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(vue, "Erreur BDD : " + ex.getMessage(), "Erreur", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    // --- STATISTIQUE 5 : QUANTITÉS RÉSERVÉES PAR ARTICLE À UNE DATE (SUM & GROUP BY) ---
    private void statQuantiteParArticleADate() {
        Connection connexion = ConnexionBDD.getConnexion();
        if (connexion != null) {
            try {
                // 1. Demander la date
                String sqlDates = "SELECT DISTINCT DATE(date_reservation) AS jour FROM Reservation ORDER BY jour DESC";
                PreparedStatement psDates = connexion.prepareStatement(sqlDates);
                ResultSet rsDates = psDates.executeQuery();
                
                java.util.ArrayList<String> listeDates = new java.util.ArrayList<>();
                java.text.SimpleDateFormat sdfVisual = new java.text.SimpleDateFormat("dd/MM/yyyy");
                java.text.SimpleDateFormat sdfSQL = new java.text.SimpleDateFormat("yyyy-MM-dd");

                while (rsDates.next()) {
                    java.sql.Date dateSQL = rsDates.getDate("jour");
                    if (dateSQL != null) {
                        listeDates.add(sdfVisual.format(dateSQL) + " (SQL:" + sdfSQL.format(dateSQL) + ")");
                    }
                }

                if (listeDates.isEmpty()) {
                    JOptionPane.showMessageDialog(vue, "Aucune réservation trouvée dans l'historique.", "Information", JOptionPane.INFORMATION_MESSAGE);
                    return;
                }

                Object[] choixDates = listeDates.stream().map(d -> d.split(" \\(SQL:")[0]).toArray();
                String dateChoisieVisuelle = (String) JOptionPane.showInputDialog(vue, "Choisissez la date :", "Statistiques - Quantités", JOptionPane.QUESTION_MESSAGE, null, choixDates, choixDates[0]);
                if (dateChoisieVisuelle == null) return;

                String vraieDateSQL = "";
                for (String d : listeDates) {
                    if (d.startsWith(dateChoisieVisuelle)) { vraieDateSQL = d.split("\\(SQL:")[1].replace(")", ""); break; }
                }

                // 2. REQUÊTE AVEC SUM() ET GROUP BY - CORRECTION : JOIN avec RESERVER
                String sqlStat = "SELECT a.libelle, SUM(res.quantite_reservee) AS total_qte " +
                                 "FROM Reservation r " +
                                 "JOIN RESERVER res ON r.id = res.id_reservation " +
                                 "JOIN Articles a ON res.id_article = a.id " +
                                 "WHERE DATE(r.date_reservation) = ?::date " +
                                 "GROUP BY a.libelle " +
                                 "ORDER BY total_qte DESC";
                                 
                PreparedStatement psStat = connexion.prepareStatement(sqlStat);
                psStat.setString(1, vraieDateSQL);
                ResultSet rsStat = psStat.executeQuery();

                // 3. Construction
                StringBuilder texteResultat = new StringBuilder("📦 Bilan des articles réservés le " + dateChoisieVisuelle + " :\n\n");
                boolean aDesResultats = false;

                while (rsStat.next()) {
                    aDesResultats = true;
                    texteResultat.append("• ").append(rsStat.getString("libelle"))
                                 .append(" : ").append(rsStat.getInt("total_qte")).append(" unité(s)\n");
                }

                if (!aDesResultats) {
                    texteResultat.append("Aucun article trouvé pour cette date.");
                }

                JOptionPane.showMessageDialog(vue, texteResultat.toString(), "Résultat des quantités", JOptionPane.INFORMATION_MESSAGE);

            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(vue, "Erreur BDD : " + ex.getMessage(), "Erreur", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
    
    private void statQuantiteParEpreuve() {
        Connection connexion = ConnexionBDD.getConnexion();
        if (connexion != null) {
            try {
                // 1. Demander l'épreuve - CORRECTION : type_epreuve et libelle
                String sqlEpreuves = "SELECT id, libelle FROM type_epreuve ORDER BY libelle ASC";
                PreparedStatement psEpreuves = connexion.prepareStatement(sqlEpreuves);
                ResultSet rsEpreuves = psEpreuves.executeQuery();
                
                java.util.ArrayList<String> listeEpreuves = new java.util.ArrayList<>();
                while (rsEpreuves.next()) {
                    listeEpreuves.add(rsEpreuves.getInt("id") + " - " + rsEpreuves.getString("libelle"));
                }

                if (listeEpreuves.isEmpty()) {
                    JOptionPane.showMessageDialog(vue, "Aucune épreuve n'existe.", "Erreur", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                String epreuveChoisie = (String) JOptionPane.showInputDialog(vue, "Choisissez l'épreuve :", "Statistiques - Quantités", JOptionPane.QUESTION_MESSAGE, null, listeEpreuves.toArray(), listeEpreuves.get(0));
                if (epreuveChoisie == null) return;

                int idEpreuve = Integer.parseInt(epreuveChoisie.split(" - ")[0]);
                String nomEpreuve = epreuveChoisie.split(" - ")[1];

                // 2. REQUÊTE : CORRECTION JOIN RESERVER + c.id_type_epreuve
                String sqlStat = "SELECT a.libelle, SUM(res.quantite_reservee) AS total_qte " +
                                 "FROM Reservation r " +
                                 "JOIN RESERVER res ON r.id = res.id_reservation " +
                                 "JOIN Articles a ON res.id_article = a.id " +
                                 "JOIN Coureur c ON r.id_coureur = c.id " +
                                 "WHERE c.id_type_epreuve = ? " +
                                 "GROUP BY a.libelle " +
                                 "ORDER BY total_qte DESC";
                                 
                PreparedStatement psStat = connexion.prepareStatement(sqlStat);
                psStat.setInt(1, idEpreuve);
                ResultSet rsStat = psStat.executeQuery();

                // 3. Construction
                StringBuilder texteResultat = new StringBuilder("📦 Bilan des articles pour l'épreuve : " + nomEpreuve + "\n\n");
                boolean aDesResultats = false;

                while (rsStat.next()) {
                    aDesResultats = true;
                    texteResultat.append("• ").append(rsStat.getString("libelle"))
                                 .append(" : ").append(rsStat.getInt("total_qte")).append(" unité(s)\n");
                }

                if (!aDesResultats) {
                    texteResultat.append("Aucun article n'a été réservé pour cette épreuve.");
                }

                JOptionPane.showMessageDialog(vue, texteResultat.toString(), "Résultat des quantités", JOptionPane.INFORMATION_MESSAGE);

            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(vue, "Erreur BDD : " + ex.getMessage(), "Erreur", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    // --- STATISTIQUE 7 : QUANTITÉS RÉSERVÉES À UNE DATE ET UNE ÉPREUVE DONNÉES ---
    private void statQuantiteParDateEtEpreuve() {
        Connection connexion = ConnexionBDD.getConnexion();
        if (connexion != null) {
            try {
                // 1. Demander la date
                String sqlDates = "SELECT DISTINCT DATE(date_reservation) AS jour FROM Reservation ORDER BY jour DESC";
                PreparedStatement psDates = connexion.prepareStatement(sqlDates);
                ResultSet rsDates = psDates.executeQuery();
                
                java.util.ArrayList<String> listeDates = new java.util.ArrayList<>();
                java.text.SimpleDateFormat sdfVisual = new java.text.SimpleDateFormat("dd/MM/yyyy");
                java.text.SimpleDateFormat sdfSQL = new java.text.SimpleDateFormat("yyyy-MM-dd");

                while (rsDates.next()) {
                    java.sql.Date dateSQL = rsDates.getDate("jour");
                    if (dateSQL != null) {
                        listeDates.add(sdfVisual.format(dateSQL) + " (SQL:" + sdfSQL.format(dateSQL) + ")");
                    }
                }

                if (listeDates.isEmpty()) {
                    JOptionPane.showMessageDialog(vue, "Aucune réservation trouvée dans l'historique.", "Information", JOptionPane.INFORMATION_MESSAGE);
                    return;
                }

                Object[] choixDates = listeDates.stream().map(d -> d.split(" \\(SQL:")[0]).toArray();
                String dateChoisieVisuelle = (String) JOptionPane.showInputDialog(vue, "1/2 - Choisissez la date :", "Statistiques - Quantités", JOptionPane.QUESTION_MESSAGE, null, choixDates, choixDates[0]);
                if (dateChoisieVisuelle == null) return;

                String vraieDateSQL = "";
                for (String d : listeDates) {
                    if (d.startsWith(dateChoisieVisuelle)) { vraieDateSQL = d.split("\\(SQL:")[1].replace(")", ""); break; }
                }

                // 2. Demander l'épreuve - CORRECTION type_epreuve et libelle
                String sqlEpreuves = "SELECT id, libelle FROM type_epreuve ORDER BY libelle ASC";
                PreparedStatement psEpreuves = connexion.prepareStatement(sqlEpreuves);
                ResultSet rsEpreuves = psEpreuves.executeQuery();
                
                java.util.ArrayList<String> listeEpreuves = new java.util.ArrayList<>();
                while (rsEpreuves.next()) {
                    listeEpreuves.add(rsEpreuves.getInt("id") + " - " + rsEpreuves.getString("libelle"));
                }

                String epreuveChoisie = (String) JOptionPane.showInputDialog(vue, "2/2 - Choisissez l'épreuve :", "Statistiques - Quantités", JOptionPane.QUESTION_MESSAGE, null, listeEpreuves.toArray(), listeEpreuves.get(0));
                if (epreuveChoisie == null) return;

                int idEpreuve = Integer.parseInt(epreuveChoisie.split(" - ")[0]);
                String nomEpreuve = epreuveChoisie.split(" - ")[1];

                // 3. LA REQUÊTE TOTALE - CORRECTION JOIN RESERVER + id_type_epreuve
                String sqlStat = "SELECT a.libelle, SUM(res.quantite_reservee) AS total_qte " +
                                 "FROM Reservation r " +
                                 "JOIN RESERVER res ON r.id = res.id_reservation " +
                                 "JOIN Articles a ON res.id_article = a.id " +
                                 "JOIN Coureur c ON r.id_coureur = c.id " +
                                 "WHERE DATE(r.date_reservation) = ?::date AND c.id_type_epreuve = ? " +
                                 "GROUP BY a.libelle " +
                                 "ORDER BY total_qte DESC";
                                 
                PreparedStatement psStat = connexion.prepareStatement(sqlStat);
                psStat.setString(1, vraieDateSQL);
                psStat.setInt(2, idEpreuve);
                ResultSet rsStat = psStat.executeQuery();

                // 4. Construction du résultat
                StringBuilder texteResultat = new StringBuilder("📦 Bilan du " + dateChoisieVisuelle + " pour l'épreuve : " + nomEpreuve + "\n\n");
                boolean aDesResultats = false;

                while (rsStat.next()) {
                    aDesResultats = true;
                    texteResultat.append("• ").append(rsStat.getString("libelle"))
                                 .append(" : ").append(rsStat.getInt("total_qte")).append(" unité(s)\n");
                }

                if (!aDesResultats) {
                    texteResultat.append("Aucun article trouvé pour ces critères combinés.");
                }

                JOptionPane.showMessageDialog(vue, texteResultat.toString(), "Résultat des quantités", JOptionPane.INFORMATION_MESSAGE);

            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(vue, "Erreur BDD : " + ex.getMessage(), "Erreur", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
}