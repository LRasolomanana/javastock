package src;

import javax.swing.JOptionPane;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class GestionFournisseurController {
    private VueGestionFournisseur vue;

    public GestionFournisseurController(VueGestionFournisseur vue) {
        this.vue = vue;
        initialiserEcouteurs();
        chargerFournisseurs();
    }

    // =========================================================
    // MÉTHODE POUR REMPLIR LES LISTES DÉROULANTES
    // =========================================================
    private void chargerFournisseurs() {
        vue.getComboFournisseurModif().removeAllItems();
        vue.getComboFournisseurModif().addItem("-- Sélectionner --");
        vue.getComboFournisseurCons().removeAllItems();
        vue.getComboFournisseurCons().addItem("-- Sélectionner --");

        String requete = "SELECT id_fournisseur, nom FROM Fournisseur ORDER BY nom ASC";
        
        // CORRECTION : Utilisation de ConnexionBDD et connexion en dehors du "try"
        Connection conn = ConnexionBDD.getConnexion(); 
        
        try (PreparedStatement ps = conn.prepareStatement(requete);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                String ligneItem = rs.getInt("id_fournisseur") + " - " + rs.getString("nom");
                vue.getComboFournisseurModif().addItem(ligneItem);
                vue.getComboFournisseurCons().addItem(ligneItem);
            }

        } catch (SQLException ex) {
            System.out.println("Erreur lors du chargement des fournisseurs : " + ex.getMessage());
        }
    }

    private void initialiserEcouteurs() {
        // =========================================================
        // --- Action CRÉER ---
        // =========================================================
        vue.getBtnCreer().addActionListener(e -> {
            String nom = vue.getTxtNomCrea().getText();
            String rue = vue.getTxtRueCrea().getText();
            String cp = vue.getTxtCpCrea().getText();
            String ville = vue.getTxtVilleCrea().getText();
            String tel = vue.getTxtTelCrea().getText();
            String email = vue.getTxtEmailCrea().getText();

            if (nom.isEmpty()) {
                JOptionPane.showMessageDialog(vue, "Le nom du fournisseur est obligatoire !", "Erreur", JOptionPane.ERROR_MESSAGE);
                return;
            }

            String requete = "INSERT INTO Fournisseur (nom, rue, cp, ville, tel, email) VALUES (?, ?, ?, ?, ?, ?)";
            Connection conn = ConnexionBDD.getConnexion(); // CORRECTION

            try (PreparedStatement ps = conn.prepareStatement(requete)) {
                 
                ps.setString(1, nom);
                ps.setString(2, rue);
                ps.setString(3, cp);
                ps.setString(4, ville);
                ps.setString(5, tel);
                ps.setString(6, email);

                int lignesModifiees = ps.executeUpdate();

                if (lignesModifiees > 0) {
                    JOptionPane.showMessageDialog(vue, "Le fournisseur a été créé avec succès !");
                    vue.getTxtNomCrea().setText("");
                    vue.getTxtRueCrea().setText("");
                    vue.getTxtCpCrea().setText("");
                    vue.getTxtVilleCrea().setText("");
                    vue.getTxtTelCrea().setText("");
                    vue.getTxtEmailCrea().setText("");
                    
                    chargerFournisseurs(); 
                }
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(vue, "Erreur SQL : \n" + ex.getMessage(), "Erreur", JOptionPane.ERROR_MESSAGE);
            }
        });

        // =========================================================
        // --- Action MODIFIER : CHOIX DANS LA LISTE ---
        // =========================================================
        vue.getComboFournisseurModif().addActionListener(e -> {
            String selection = (String) vue.getComboFournisseurModif().getSelectedItem();
            
            if (selection != null && !selection.equals("-- Sélectionner --")) {
                String[] parties = selection.split(" - ");
                int idFournisseur = Integer.parseInt(parties[0]);

                String requete = "SELECT * FROM Fournisseur WHERE id_fournisseur = ?";
                Connection conn = ConnexionBDD.getConnexion(); // CORRECTION
                
                try (PreparedStatement ps = conn.prepareStatement(requete)) {
                    ps.setInt(1, idFournisseur);
                    try (ResultSet rs = ps.executeQuery()) {
                        if (rs.next()) {
                            vue.getTxtCodeModif().setText(String.valueOf(rs.getInt("id_fournisseur")));
                            vue.getTxtNomModif().setText(rs.getString("nom"));
                            vue.getTxtRueModif().setText(rs.getString("rue") != null ? rs.getString("rue") : "");
                            vue.getTxtCpModif().setText(rs.getString("cp") != null ? rs.getString("cp") : "");
                            vue.getTxtVilleModif().setText(rs.getString("ville") != null ? rs.getString("ville") : "");
                            vue.getTxtTelModif().setText(rs.getString("tel") != null ? rs.getString("tel") : "");
                            vue.getTxtEmailModif().setText(rs.getString("email") != null ? rs.getString("email") : "");
                        }
                    }
                } catch (SQLException ex) {
                    System.out.println("Erreur SELECT modif : " + ex.getMessage());
                }
            } else {
                vue.getTxtCodeModif().setText("");
                vue.getTxtNomModif().setText("");
                vue.getTxtRueModif().setText("");
                vue.getTxtCpModif().setText("");
                vue.getTxtVilleModif().setText("");
                vue.getTxtTelModif().setText("");
                vue.getTxtEmailModif().setText("");
            }
        });

        // =========================================================
        // --- Action MODIFIER : SAUVEGARDE EN BDD ---
        // =========================================================
        vue.getBtnModifier().addActionListener(e -> {
            if (vue.getTxtCodeModif().getText().isEmpty()) {
                JOptionPane.showMessageDialog(vue, "Veuillez d'abord sélectionner un fournisseur à modifier !", "Attention", JOptionPane.WARNING_MESSAGE);
                return;
            }

            int id = Integer.parseInt(vue.getTxtCodeModif().getText());
            String nom = vue.getTxtNomModif().getText();
            String rue = vue.getTxtRueModif().getText();
            String cp = vue.getTxtCpModif().getText();
            String ville = vue.getTxtVilleModif().getText();
            String tel = vue.getTxtTelModif().getText();
            String email = vue.getTxtEmailModif().getText();

            if (nom.isEmpty()) {
                JOptionPane.showMessageDialog(vue, "Le nom ne peut pas être vide !");
                return;
            }

            String requete = "UPDATE Fournisseur SET nom=?, rue=?, cp=?, ville=?, tel=?, email=? WHERE id_fournisseur=?";
            Connection conn = ConnexionBDD.getConnexion(); // CORRECTION

            try (PreparedStatement ps = conn.prepareStatement(requete)) {
                ps.setString(1, nom);
                ps.setString(2, rue);
                ps.setString(3, cp);
                ps.setString(4, ville);
                ps.setString(5, tel);
                ps.setString(6, email);
                ps.setInt(7, id);

                int modif = ps.executeUpdate();
                if (modif > 0) {
                    JOptionPane.showMessageDialog(vue, "Modification confirmée pour le fournisseur N°" + id);
                    chargerFournisseurs(); 
                }
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(vue, "Erreur lors de la modification : \n" + ex.getMessage(), "Erreur", JOptionPane.ERROR_MESSAGE);
            }
        });

        // =========================================================
        // --- Action CONSULTER : CHOIX DANS LA LISTE ---
        // =========================================================
        vue.getComboFournisseurCons().addActionListener(e -> {
            String selection = (String) vue.getComboFournisseurCons().getSelectedItem();
            
            if (selection != null && !selection.equals("-- Sélectionner --")) {
                String[] parties = selection.split(" - ");
                int idFournisseur = Integer.parseInt(parties[0]);

                String requete = "SELECT * FROM Fournisseur WHERE id_fournisseur = ?";
                Connection conn = ConnexionBDD.getConnexion(); // CORRECTION
                
                try (PreparedStatement ps = conn.prepareStatement(requete)) {
                    ps.setInt(1, idFournisseur);
                    try (ResultSet rs = ps.executeQuery()) {
                        if (rs.next()) {
                            String details = "<html><body style='font-size:12px; font-family:Arial;'>"
                                    + "<b>Code :</b> " + rs.getInt("id_fournisseur") + "<br><br>"
                                    + "<b>Nom :</b> " + rs.getString("nom") + "<br><br>"
                                    + "<b>Rue :</b> " + (rs.getString("rue") != null ? rs.getString("rue") : "<i>Non renseignée</i>") + "<br><br>"
                                    + "<b>Code Postal :</b> " + (rs.getString("cp") != null ? rs.getString("cp") : "<i>Non renseigné</i>") + "<br><br>"
                                    + "<b>Ville :</b> " + (rs.getString("ville") != null ? rs.getString("ville") : "<i>Non renseignée</i>") + "<br><br>"
                                    + "<b>Téléphone :</b> " + (rs.getString("tel") != null ? rs.getString("tel") : "<i>Non renseigné</i>") + "<br><br>"
                                    + "<b>Email :</b> " + (rs.getString("email") != null ? rs.getString("email") : "<i>Non renseigné</i>")
                                    + "</body></html>";
                            
                            vue.getLblDetailsCons().setText(details);
                        }
                    }
                } catch (SQLException ex) {
                    System.out.println("Erreur SELECT consultation : " + ex.getMessage());
                }
            } else {
                vue.getLblDetailsCons().setText("Veuillez sélectionner un fournisseur pour voir ses détails.");
            }
        });
    }
}