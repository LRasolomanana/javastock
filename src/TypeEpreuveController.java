package src;

import javax.swing.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class TypeEpreuveController {

    private VueGestionTypeEpreuve vue;

    public TypeEpreuveController(VueGestionTypeEpreuve vue) {
        this.vue = vue;
        
        // Les actions des boutons
        this.vue.getBtnAjouter().addActionListener(e -> ajouterEpreuve());
        this.vue.getBtnRetour().addActionListener(e -> vue.dispose()); // Ferme la fenêtre
        
        // On charge les données au démarrage
        chargerEpreuvesDepuisBDD();
    }

    private void chargerEpreuvesDepuisBDD() {
        vue.viderTableau(); 

        Connection connexion = ConnexionBDD.getConnexion();
        if (connexion != null) {
            try {
                String sql = "SELECT * FROM type_epreuve ORDER BY id ASC";
                PreparedStatement requete = connexion.prepareStatement(sql);
                ResultSet resultat = requete.executeQuery();

                while (resultat.next()) {
                    int id = resultat.getInt("id");
                    String nom = resultat.getString("libelle");
                    vue.ajouterLigneTableau(id, nom);
                }

            } catch (SQLException ex) {
                System.out.println("Erreur SQL (Lecture) : " + ex.getMessage());
            }
        }
    }

    private void ajouterEpreuve() {
        String nomEpreuve = JOptionPane.showInputDialog(vue, 
            "Veuillez entrer le nom du nouveau type d'épreuve :", 
            "Ajout d'épreuve", 
            JOptionPane.QUESTION_MESSAGE);

        if (nomEpreuve != null && !nomEpreuve.trim().isEmpty()) {
            
            Connection connexion = ConnexionBDD.getConnexion();
            if (connexion != null) {
                try {
                    String sql = "INSERT INTO type_epreuve (libelle) VALUES (?)";
                    PreparedStatement requete = connexion.prepareStatement(sql);
                    requete.setString(1, nomEpreuve.trim());

                    int lignesModifiees = requete.executeUpdate();

                    if (lignesModifiees > 0) {
                        chargerEpreuvesDepuisBDD();
                        JOptionPane.showMessageDialog(vue, "Le type d'épreuve a été ajouté avec succès !", "Succès", JOptionPane.INFORMATION_MESSAGE);
                    }

                } catch (SQLException ex) {
                    JOptionPane.showMessageDialog(vue, "Erreur BDD lors de l'ajout : " + ex.getMessage(), "Erreur", JOptionPane.ERROR_MESSAGE);
                }
            }
        }
    }
}