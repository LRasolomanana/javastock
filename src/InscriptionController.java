package src;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class InscriptionController {

    private VueInscription vue;
    private VueConnexion vueConnexionPrecedente;

    public InscriptionController(VueInscription vue, VueConnexion vueConnexionPrecedente) {
        this.vue = vue;
        this.vueConnexionPrecedente = vueConnexionPrecedente;

        vue.getBtnValiderInscription().addActionListener(e -> inscrireUtilisateur());
        
        vue.getBtnRetour().addActionListener(e -> {
            vue.dispose();
            vueConnexionPrecedente.setVisible(true);
        });

        // --- GESTION DES YEUX (Afficher/Masquer MDP) ---
        char caractereMasqueDefaut = vue.getTxtMotDePasse().getEchoChar();
        
        vue.getBtnVoirMdp().addActionListener(e -> {
            if (vue.getBtnVoirMdp().isSelected()) vue.getTxtMotDePasse().setEchoChar((char) 0);
            else vue.getTxtMotDePasse().setEchoChar(caractereMasqueDefaut);
        });

        vue.getBtnVoirConfirmation().addActionListener(e -> {
            if (vue.getBtnVoirConfirmation().isSelected()) vue.getTxtConfirmation().setEchoChar((char) 0);
            else vue.getTxtConfirmation().setEchoChar(caractereMasqueDefaut);
        });

        // --- GESTION EN TEMPS RÉEL DES COULEURS ---
        vue.getTxtMotDePasse().getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) { verifierConditionsTempsReel(); }
            @Override
            public void removeUpdate(DocumentEvent e) { verifierConditionsTempsReel(); }
            @Override
            public void changedUpdate(DocumentEvent e) { verifierConditionsTempsReel(); }
        });
    }

    private void verifierConditionsTempsReel() {
        String mdp = vue.getMotDePasse();

        changerCouleurLabel(vue.getLblConditionLongueur(), mdp.length() >= 8);
        changerCouleurLabel(vue.getLblConditionMajMin(), !mdp.equals(mdp.toLowerCase()) && !mdp.equals(mdp.toUpperCase()));
        changerCouleurLabel(vue.getLblConditionChiffre(), mdp.matches(".*[0-9].*"));
        changerCouleurLabel(vue.getLblConditionSpecial(), mdp.matches(".*[@#$%^&+=!].*"));
    }

    private void changerCouleurLabel(JLabel label, boolean conditionRemplie) {
        if (conditionRemplie) {
            label.setForeground(vue.COULEUR_VALIDEE);
            if (!label.getText().startsWith("✓")) label.setText("✓ " + label.getText().substring(2));
        } else {
            label.setForeground(vue.COULEUR_NON_VALIDEE);
            if (label.getText().startsWith("✓")) label.setText("- " + label.getText().substring(2));
        }
    }

    private void inscrireUtilisateur() {
        // 1. ON RÉCUPÈRE ABSOLUMENT TOUT (y compris nom et prénom)
        String prenom = vue.getPrenom().trim();
        String nom = vue.getNom().trim();
        String email = vue.getEmail().trim();
        String identifiant = vue.getIdentifiant().trim();
        String mdp = vue.getMotDePasse();
        String confirmation = vue.getConfirmation();

        // 2. Vérifier les champs vides
        if (prenom.isEmpty() || nom.isEmpty() || email.isEmpty() || identifiant.isEmpty() || mdp.isEmpty() || confirmation.isEmpty()) {
            JOptionPane.showMessageDialog(vue, "Tous les champs sont obligatoires.", "Erreur", JOptionPane.WARNING_MESSAGE);
            return;
        }

        // 3. Vérifier le format de l'email
        String regexEmail = "^[A-Za-z0-9+_.-]+@(.+)$";
        if (!email.matches(regexEmail)) {
            JOptionPane.showMessageDialog(vue, "L'adresse email n'est pas valide.", "Erreur de saisie", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // 4. Vérifier la sécurité du mot de passe
        String regexMdp = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=!]).{8,}$";
        if (!mdp.matches(regexMdp)) {
            JOptionPane.showMessageDialog(vue, "Le mot de passe ne respecte pas toutes les conditions.", "Sécurité insuffisante", JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (!mdp.equals(confirmation)) {
            JOptionPane.showMessageDialog(vue, "Les mots de passe ne correspondent pas !", "Erreur", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // 5. Inscription en BDD
        Connection connexion = ConnexionBDD.getConnexion();
        if (connexion != null) {
            try {
                // Vérifier si l'identifiant OU l'email existe déjà
                String sqlCheck = "SELECT COUNT(*) FROM Utilisateur WHERE identifiant = ? OR email = ?";
                PreparedStatement psCheck = connexion.prepareStatement(sqlCheck);
                psCheck.setString(1, identifiant);
                psCheck.setString(2, email);
                ResultSet rs = psCheck.executeQuery();
                rs.next();
                
                if (rs.getInt(1) > 0) {
                    JOptionPane.showMessageDialog(vue, "Cet identifiant ou cet email est déjà utilisé.", "Erreur", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                // --- INSERTION DE L'UTILISATEUR SÉCURISÉ ---
                String sqlInsert = "INSERT INTO Utilisateur (email, identifiant, mot_de_passe) VALUES (?, ?, ?)";
                PreparedStatement psInsert = connexion.prepareStatement(sqlInsert);
                psInsert.setString(1, email);
                psInsert.setString(2, identifiant);
                
                // 🔒 On hache le mot de passe !
                String mdpHache = OutilsSecurite.hacherMotDePasse(mdp);
                psInsert.setString(3, mdpHache);

                int lignesModifiees = psInsert.executeUpdate();

                if (lignesModifiees > 0) {
                    
                    // --- LA MAGIE : DOUBLE INSERTION (Avec une épreuve par défaut pour éviter les bugs d'affichage) ---
                    try {
                        String sqlCoureur = "INSERT INTO Coureur (nom, prenom, id_epreuve) VALUES (?, ?, (SELECT MIN(id) FROM TypeEpreuve))";
                        PreparedStatement psCoureur = connexion.prepareStatement(sqlCoureur);
                        psCoureur.setString(1, nom);    
                        psCoureur.setString(2, prenom); 
                        psCoureur.executeUpdate();
                    } catch (SQLException ex) {
                        System.out.println("Erreur lors de la création automatique du coureur : " + ex.getMessage());
                    }
                    // ------------------------------------

                    JOptionPane.showMessageDialog(vue, "Compte créé et sécurisé avec succès !", "Succès", JOptionPane.INFORMATION_MESSAGE);
                    vue.dispose();
                    vueConnexionPrecedente.setVisible(true);
                }

            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(vue, "Erreur BDD : " + ex.getMessage(), "Erreur", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
}