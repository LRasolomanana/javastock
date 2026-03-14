package src;

import javax.swing.JOptionPane;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class GestionDemandeReapproController {
    private VueGestionDemandeReappro vue;

    public GestionDemandeReapproController(VueGestionDemandeReappro vue) {
        this.vue = vue;
        chargerListesDeroulantes();
        chargerDemandes();
        initialiserEcouteurs();
    }

    private void chargerListesDeroulantes() {
        Connection conn = ConnexionBDD.getConnexion(); 
        if (conn == null) return;

        // 1. Charger les Points de Livraison (Correction table POINT_LIVRAISON)
        try (PreparedStatement ps = conn.prepareStatement("SELECT id_point_livraison, nom FROM POINT_LIVRAISON ORDER BY nom ASC");
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                String item = rs.getInt("id_point_livraison") + " - " + rs.getString("nom");
                vue.getComboPLCrea().addItem(item);
                vue.getComboPLModif().addItem(item);
            }
        } catch (SQLException ex) { ex.printStackTrace(); }

        // 2. Charger les Fournisseurs (Correction table FOURNISSEUR)
        try (PreparedStatement ps = conn.prepareStatement("SELECT id_fournisseur, nom FROM FOURNISSEUR ORDER BY nom ASC");
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                String item = rs.getInt("id_fournisseur") + " - " + rs.getString("nom");
                vue.getComboFournisseurCrea().addItem(item);
                vue.getComboFournisseurModif().addItem(item);
            }
        } catch (SQLException ex) { ex.printStackTrace(); }
    }

    private void chargerDemandes() {
        vue.getComboDemandeModif().removeAllItems();
        vue.getComboDemandeModif().addItem("-- Sélectionner --");
        vue.getComboDemandeCons().removeAllItems();
        vue.getComboDemandeCons().addItem("-- Sélectionner --");

        Connection conn = ConnexionBDD.getConnexion();
        // CORRECTION : Table DEMANDE
        try (PreparedStatement ps = conn.prepareStatement("SELECT numero_commande, date_demande FROM DEMANDE ORDER BY date_demande DESC");
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                String item = rs.getString("numero_commande") + " - " + rs.getString("date_demande");
                vue.getComboDemandeModif().addItem(item);
                vue.getComboDemandeCons().addItem(item);
            }
        } catch (SQLException ex) { ex.printStackTrace(); }
    }

    private int extraireId(String selection) {
        if (selection == null || selection.startsWith("--")) return -1;
        return Integer.parseInt(selection.split(" - ")[0]);
    }

    private void initialiserEcouteurs() {
        // --- Action CRÉER ---
        vue.getBtnCreer().addActionListener(e -> {
            int idPl = extraireId((String) vue.getComboPLCrea().getSelectedItem());
            int idFourn = extraireId((String) vue.getComboFournisseurCrea().getSelectedItem());
            String numCmd = vue.getTxtCodeArtCrea().getText(); // On utilise le code art comme num de commande pour l'exemple

            if (idPl == -1 || idFourn == -1 || numCmd.isEmpty()) {
                JOptionPane.showMessageDialog(vue, "Veuillez remplir les champs obligatoires (PL, Fournisseur, N° Commande) !");
                return;
            }

            // CORRECTION : La table DEMANDE lie id_fournisseur, id_point_livraison et id_article
            // Note : Il faudrait idéalement une liste déroulante pour choisir l'ID de l'article existant
            String sql = "INSERT INTO DEMANDE (id_fournisseur, id_point_livraison, id_article, numero_commande, motif, date_demande, quantite) VALUES (?, ?, ?, ?, ?, CURRENT_DATE, ?)";
            
            Connection conn = ConnexionBDD.getConnexion();
            try (PreparedStatement ps = conn.prepareStatement(sql)) {
                ps.setInt(1, idFourn);
                ps.setInt(2, idPl);
                ps.setInt(3, 1); // ID Article par défaut (à adapter selon ton besoin)
                ps.setString(4, numCmd);
                ps.setString(5, vue.getTxtMotifCrea().getText());
                
                int qte = vue.getTxtQteCrea().getText().isEmpty() ? 0 : Integer.parseInt(vue.getTxtQteCrea().getText());
                ps.setInt(6, qte);

                if (ps.executeUpdate() > 0) {
                    JOptionPane.showMessageDialog(vue, "Demande de réapprovisionnement créée !");
                    chargerDemandes();
                }
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(vue, "Erreur SQL : " + ex.getMessage());
            }
        });

        // --- Action CONSULTER ---
        vue.getComboDemandeCons().addActionListener(e -> {
            String selection = (String) vue.getComboDemandeCons().getSelectedItem();
            if (selection == null || selection.startsWith("--")) return;
            
            String idCmd = selection.split(" - ")[0];

            String sql = "SELECT d.*, p.nom as p_nom, p.ville as p_ville, f.nom as f_nom " +
                         "FROM DEMANDE d " +
                         "JOIN POINT_LIVRAISON p ON d.id_point_livraison = p.id_point_livraison " +
                         "JOIN FOURNISSEUR f ON d.id_fournisseur = f.id_fournisseur " +
                         "WHERE d.numero_commande = ?";

            Connection conn = ConnexionBDD.getConnexion();
            try (PreparedStatement ps = conn.prepareStatement(sql)) {
                ps.setString(1, idCmd);
                try (ResultSet rs = ps.executeQuery()) {
                    if (rs.next()) {
                        String html = "<html><body style='padding:10px;'>"
                                + "<b>Réception :</b> " + rs.getString("p_nom") + " (" + rs.getString("p_ville") + ")<br>"
                                + "<b>Expéditeur :</b> " + rs.getString("f_nom") + "<br><br>"
                                + "<b>Commande N° :</b> " + rs.getString("numero_commande") + "<br>"
                                + "<b>Motif :</b> " + rs.getString("motif") + "<br>"
                                + "<b>Quantité :</b> " + rs.getInt("quantite")
                                + "</body></html>";
                        
                        vue.getLblDetailsCons().setText(html);
                    }
                }
            } catch (SQLException ex) { ex.printStackTrace(); }
        });
    }
}