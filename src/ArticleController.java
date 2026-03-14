package src;

import javax.swing.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class ArticleController {

    private VueGestionArticles vue;
    private int compteurId = 1;

    public ArticleController(VueGestionArticles vue) {
        this.vue = vue;
        initialiserControleur();
        
        // C'est ici qu'on appelle la méthode au démarrage !
        chargerArticlesDepuisBDD(); 
    }

    private void initialiserControleur() {
        // Branchement des 3 boutons
        vue.getBtnAjouter().addActionListener(e -> ajouterArticle());
        vue.getBtnModifier().addActionListener(e -> modifierArticle());
        vue.getBtnSupprimer().addActionListener(e -> supprimerArticle());

        // Branchement du clic sur le tableau pour pré-remplir les champs
        vue.getTable().getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting() && vue.getTable().getSelectedRow() != -1) {
                chargerDetailsArticleSelectionne();
            }
        });
    }

    // =========================================================================
    // 1. MÉTHODE DE LECTURE (READ) - Celle qui te manquait !
    // =========================================================================
    private void chargerArticlesDepuisBDD() {
        vue.viderTableau(); // On nettoie le tableau visuel
        Connection connexion = ConnexionBDD.getConnexion();

        if (connexion != null) {
            try {
                // On récupère uniquement les articles non supprimés (indicateur_sl = FALSE)
                String sql = "SELECT * FROM Articles WHERE indicateur_sl = FALSE ORDER BY id ASC";
                PreparedStatement requete = connexion.prepareStatement(sql);
                ResultSet resultat = requete.executeQuery();

                while (resultat.next()) {
                    int id = resultat.getInt("id");
                    String libelle = resultat.getString("libelle");
                    String codeCat = resultat.getString("categorie");
                    int quantite = resultat.getInt("quantite");

                    String nomCategorie = "";
                    String details = "";

                    if (codeCat.equals("T")) {
                        nomCategorie = "Textile";
                        details = "Taille: " + resultat.getString("taille") + " | Couleur: " + resultat.getString("couleur");
                    } else if (codeCat.equals("B")) {
                        nomCategorie = "Boisson";
                        details = "Volume: " + resultat.getString("volume");
                    } else if (codeCat.equals("DS")) {
                        nomCategorie = "Denrée Sèche";
                        details = "Poids: " + resultat.getString("poids");
                    }

                    vue.ajouterLigneTableau(id, nomCategorie, libelle, quantite, details);
                    
                    if (id >= compteurId) {
                        compteurId = id + 1;
                    }
                }
            } catch (SQLException ex) {
                System.out.println("❌ Erreur lors du chargement des articles : " + ex.getMessage());
            }
        }
    }

    // =========================================================================
    // 2. MÉTHODE DE CRÉATION (CREATE)
    // =========================================================================
    private void ajouterArticle() {
        String categorie = vue.getCategorieSelectionnee();
        String libelle = vue.getLibelle();
        String quantiteStr = vue.getQuantiteTexte();
        String spec1 = vue.getSpec1();
        String spec2 = vue.getSpec2();

        if (libelle.isEmpty() || quantiteStr.isEmpty()) {
            JOptionPane.showMessageDialog(vue, "Le libellé et la quantité sont obligatoires !", "Erreur", JOptionPane.ERROR_MESSAGE);
            return;
        }

        try {
            int quantite = Integer.parseInt(quantiteStr);
            String codeCategorie = categorie.equals("Textile") ? "T" : (categorie.equals("Boisson") ? "B" : "DS");

            Connection connexion = ConnexionBDD.getConnexion();

            if (connexion != null) {
                String sql = "INSERT INTO Articles (libelle, categorie, quantite, taille, couleur, volume, poids, indicateur_sl) VALUES (?, ?, ?, ?, ?, ?, ?, FALSE)";
                PreparedStatement requete = connexion.prepareStatement(sql);

                requete.setString(1, libelle);
                requete.setString(2, codeCategorie);
                requete.setInt(3, quantite);

                if (codeCategorie.equals("T")) {
                    requete.setString(4, spec1); requete.setString(5, spec2);
                    requete.setNull(6, java.sql.Types.VARCHAR); requete.setNull(7, java.sql.Types.VARCHAR);
                } else if (codeCategorie.equals("B")) {
                    requete.setNull(4, java.sql.Types.VARCHAR); requete.setNull(5, java.sql.Types.VARCHAR);
                    requete.setString(6, spec1); requete.setNull(7, java.sql.Types.VARCHAR);
                } else if (codeCategorie.equals("DS")) {
                    requete.setNull(4, java.sql.Types.VARCHAR); requete.setNull(5, java.sql.Types.VARCHAR);
                    requete.setNull(6, java.sql.Types.VARCHAR); requete.setString(7, spec1);
                }

                int lignesModifiees = requete.executeUpdate();

                if (lignesModifiees > 0) {
                    chargerArticlesDepuisBDD(); // Recharge le tableau complet
                    vue.reinitialiserChamps();
                    JOptionPane.showMessageDialog(vue, "Article sauvegardé dans la base de données !", "Succès", JOptionPane.INFORMATION_MESSAGE);
                }
            }
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(vue, "La quantité doit être un nombre valide.", "Erreur", JOptionPane.ERROR_MESSAGE);
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(vue, "Erreur SQL : " + ex.getMessage(), "Erreur", JOptionPane.ERROR_MESSAGE);
        }
    }

    // =========================================================================
    // 3. MÉTHODES DE MODIFICATION (UPDATE)
    // =========================================================================
    private void chargerDetailsArticleSelectionne() {
        int id = vue.getIdLigneSelectionnee();
        if (id == -1) return;

        Connection connexion = ConnexionBDD.getConnexion();
        if (connexion != null) {
            try {
                PreparedStatement ps = connexion.prepareStatement("SELECT * FROM Articles WHERE id = ?");
                ps.setInt(1, id);
                ResultSet rs = ps.executeQuery();
                if (rs.next()) {
                    vue.setValeursFormulaire(
                        rs.getString("categorie"),
                        rs.getString("libelle"),
                        String.valueOf(rs.getInt("quantite")),
                        rs.getString("taille"),
                        rs.getString("couleur"),
                        rs.getString("volume"),
                        rs.getString("poids")
                    );
                }
            } catch (SQLException ex) {
                System.out.println("Erreur de chargement des détails : " + ex.getMessage());
            }
        }
    }

    private void modifierArticle() {
        int idSelectionne = vue.getIdLigneSelectionnee();
        
        if (idSelectionne == -1) {
            JOptionPane.showMessageDialog(vue, "Veuillez sélectionner un article dans le tableau à modifier.", "Avertissement", JOptionPane.WARNING_MESSAGE);
            return;
        }

        String categorie = vue.getCategorieSelectionnee();
        String libelle = vue.getLibelle();
        String quantiteStr = vue.getQuantiteTexte();
        String spec1 = vue.getSpec1();
        String spec2 = vue.getSpec2();

        if (libelle.isEmpty() || quantiteStr.isEmpty()) {
            JOptionPane.showMessageDialog(vue, "Le libellé et la quantité sont obligatoires !", "Erreur", JOptionPane.ERROR_MESSAGE);
            return;
        }

        try {
            int quantite = Integer.parseInt(quantiteStr);
            String codeCategorie = categorie.equals("Textile") ? "T" : (categorie.equals("Boisson") ? "B" : "DS");

            Connection connexion = ConnexionBDD.getConnexion();
            if (connexion != null) {
                String sql = "UPDATE Articles SET libelle=?, categorie=?, quantite=?, taille=?, couleur=?, volume=?, poids=? WHERE id=?";
                PreparedStatement ps = connexion.prepareStatement(sql);
                
                ps.setString(1, libelle);
                ps.setString(2, codeCategorie);
                ps.setInt(3, quantite);
                
                if (codeCategorie.equals("T")) {
                    ps.setString(4, spec1); ps.setString(5, spec2);
                    ps.setNull(6, java.sql.Types.VARCHAR); ps.setNull(7, java.sql.Types.VARCHAR);
                } else if (codeCategorie.equals("B")) {
                    ps.setNull(4, java.sql.Types.VARCHAR); ps.setNull(5, java.sql.Types.VARCHAR);
                    ps.setString(6, spec1); ps.setNull(7, java.sql.Types.VARCHAR);
                } else {
                    ps.setNull(4, java.sql.Types.VARCHAR); ps.setNull(5, java.sql.Types.VARCHAR);
                    ps.setNull(6, java.sql.Types.VARCHAR); ps.setString(7, spec1);
                }
                ps.setInt(8, idSelectionne);

                int lignesModifiees = ps.executeUpdate();

                if (lignesModifiees > 0) {
                    chargerArticlesDepuisBDD(); // Met à jour le tableau visuellement
                    vue.reinitialiserChamps();
                    JOptionPane.showMessageDialog(vue, "Article modifié avec succès !", "Succès", JOptionPane.INFORMATION_MESSAGE);
                }
            }
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(vue, "La quantité doit être un nombre valide.", "Erreur", JOptionPane.ERROR_MESSAGE);
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(vue, "Erreur SQL : " + ex.getMessage(), "Erreur", JOptionPane.ERROR_MESSAGE);
        }
    }

    // =========================================================================
    // 4. MÉTHODE DE SUPPRESSION LOGIQUE (DELETE)
    // =========================================================================
    private void supprimerArticle() {
        int idSelectionne = vue.getIdLigneSelectionnee();
        
        if (idSelectionne == -1) {
            JOptionPane.showMessageDialog(vue, "Veuillez d'abord cliquer sur un article dans le tableau pour le supprimer.", "Avertissement", JOptionPane.WARNING_MESSAGE);
            return; 
        } 

        int choix = JOptionPane.showConfirmDialog(vue, 
            "Voulez-vous vraiment supprimer cet article (ID: " + idSelectionne + ") ?\nIl sera retiré du catalogue mais conservé dans l'historique.", 
            "Confirmation de suppression", 
            JOptionPane.YES_NO_OPTION,
            JOptionPane.WARNING_MESSAGE);
        
        if (choix == JOptionPane.YES_OPTION) {
            Connection connexion = ConnexionBDD.getConnexion();
            
            if (connexion != null) {
                try {
                    String sql = "UPDATE Articles SET indicateur_sl = TRUE WHERE id = ?";
                    PreparedStatement requete = connexion.prepareStatement(sql);
                    requete.setInt(1, idSelectionne); 

                    int lignesModifiees = requete.executeUpdate();

                    if (lignesModifiees > 0) {
                        chargerArticlesDepuisBDD(); // Recharge le tableau sans l'article supprimé
                        vue.reinitialiserChamps();
                        JOptionPane.showMessageDialog(vue, "L'article a été supprimé avec succès !", "Succès", JOptionPane.INFORMATION_MESSAGE);
                    }
                    
                } catch (SQLException ex) {
                    JOptionPane.showMessageDialog(vue, "Erreur SQL : " + ex.getMessage(), "Erreur fatale", JOptionPane.ERROR_MESSAGE);
                }
            }
        }
    }
}