package src;

import javax.swing.*;
import java.awt.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class CoureurController {

    private VueGestionCoureurs vue;

    public CoureurController(VueGestionCoureurs vue) {
        this.vue = vue;
        
        this.vue.getBtnAjouter().addActionListener(e -> ajouterCoureur());
        this.vue.getBtnRetour().addActionListener(e -> vue.dispose());
        
        chargerCoureursDepuisBDD();
    }

    // --- LECTURE (Avec Jointure pour avoir le nom de l'épreuve) ---
    private void chargerCoureursDepuisBDD() {
        vue.viderTableau(); 

        Connection connexion = ConnexionBDD.getConnexion();
        if (connexion != null) {
            try {
                // CORRECTION : e.nom devient e.libelle, TypeEpreuve devient type_epreuve, id_epreuve devient id_type_epreuve
                String sql = "SELECT c.id, c.nom, c.prenom, e.libelle AS nom_epreuve " +
                             "FROM Coureur c " +
                             "JOIN type_epreuve e ON c.id_type_epreuve = e.id " +
                             "ORDER BY c.id ASC";
                PreparedStatement requete = connexion.prepareStatement(sql);
                ResultSet resultat = requete.executeQuery();

                while (resultat.next()) {
                    int id = resultat.getInt("id");
                    String nom = resultat.getString("nom");
                    String prenom = resultat.getString("prenom") != null ? resultat.getString("prenom") : "";
                    String affichageNom = nom + " " + prenom;
                    String nomEpreuve = resultat.getString("nom_epreuve");

                    vue.ajouterLigneTableau(id, affichageNom, nomEpreuve);
                }

            } catch (SQLException ex) {
                System.out.println("Erreur SQL (Lecture) : " + ex.getMessage());
            }
        }
    }

    // --- CRÉATION (Nouveau formulaire) ---
    private void ajouterCoureur() {
        Connection connexion = ConnexionBDD.getConnexion();
        if (connexion == null) return;

        try {
            // CORRECTION : 'nom' remplacé par 'libelle'
            String sqlEpreuves = "SELECT id, libelle FROM type_epreuve ORDER BY libelle ASC";
            PreparedStatement psEpreuves = connexion.prepareStatement(sqlEpreuves);
            ResultSet rsEpreuves = psEpreuves.executeQuery();
            
            ArrayList<String> listeEpreuves = new ArrayList<>();
            while (rsEpreuves.next()) {
                // CORRECTION : 'nom' remplacé par 'libelle'
                listeEpreuves.add(rsEpreuves.getInt("id") + " - " + rsEpreuves.getString("libelle"));
            }

            if (listeEpreuves.isEmpty()) {
                JOptionPane.showMessageDialog(vue, "Vous devez d'abord créer au moins une Épreuve dans le menu !", "Erreur", JOptionPane.ERROR_MESSAGE);
                return;
            }

            // 2. Création d'un formulaire personnalisé pour la pop-up
            JTextField txtNom = new JTextField(15);
            JComboBox<String> comboEpreuve = new JComboBox<>(listeEpreuves.toArray(new String[0]));

            JPanel panelFormulaire = new JPanel(new GridLayout(2, 2, 10, 10));
            panelFormulaire.add(new JLabel("Nom du coureur :"));
            panelFormulaire.add(txtNom);
            panelFormulaire.add(new JLabel("Épreuve choisie :"));
            panelFormulaire.add(comboEpreuve);

            // 3. Affichage de la pop-up avec le formulaire
            int resultat = JOptionPane.showConfirmDialog(vue, panelFormulaire, "Inscription d'un nouveau coureur", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);

            // 4. Si l'utilisateur a cliqué sur OK
            if (resultat == JOptionPane.OK_OPTION) {
                String nomSaisi = txtNom.getText().trim();
                String epreuveChoisie = (String) comboEpreuve.getSelectedItem();

                if (!nomSaisi.isEmpty() && epreuveChoisie != null) {
                    // On extrait l'ID de l'épreuve
                    int idEpreuve = Integer.parseInt(epreuveChoisie.split(" - ")[0]);

                    // CORRECTION : id_epreuve devient id_type_epreuve
                    String sqlInsert = "INSERT INTO Coureur (nom, prenom, id_type_epreuve) VALUES (?, ?, ?)";
                    PreparedStatement requete = connexion.prepareStatement(sqlInsert);
                    requete.setString(1, nomSaisi);
                    requete.setString(2, ""); // Prénom laissé vide
                    requete.setInt(3, idEpreuve);

                    int lignesModifiees = requete.executeUpdate();

                    if (lignesModifiees > 0) {
                        chargerCoureursDepuisBDD(); // Recharge le tableau
                        JOptionPane.showMessageDialog(vue, "Coureur inscrit avec succès !", "Succès", JOptionPane.INFORMATION_MESSAGE);
                    }
                } else {
                    JOptionPane.showMessageDialog(vue, "Le nom ne peut pas être vide.", "Erreur", JOptionPane.WARNING_MESSAGE);
                }
            }

        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(vue, "Erreur BDD lors de l'ajout : " + ex.getMessage(), "Erreur", JOptionPane.ERROR_MESSAGE);
        }
    }
}