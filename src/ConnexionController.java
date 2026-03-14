package src;

import javax.swing.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class ConnexionController {

    private VueConnexion vue;

    public ConnexionController(VueConnexion vue) {
        this.vue = vue;
        
        // 1. Bouton "Se connecter"
        vue.getBtnConnexion().addActionListener(e -> verifierIdentifiants());

        // 2. Bouton "S'inscrire" (On remet la liaison que l'on avait faite avant)
        vue.getBtnInscription().addActionListener(e -> {
            vue.setVisible(false);
            VueInscription vueInscription = new VueInscription();
            new InscriptionController(vueInscription, vue);
            vueInscription.setVisible(true);
        });

        // 3. Bouton "Œil" (Afficher/Masquer le mot de passe)
        // Par défaut, Java utilise un point (•) pour cacher les mots de passe
        char caractereMasqueDefaut = vue.getTxtMotDePasse().getEchoChar();
        
        vue.getBtnVoirMdp().addActionListener(e -> {
            if (vue.getBtnVoirMdp().isSelected()) {
                // Si l'œil est cliqué, on affiche le texte en clair ((char) 0 enlève le masquage)
                vue.getTxtMotDePasse().setEchoChar((char) 0);
            } else {
                // Sinon, on remet le caractère masqué
                vue.getTxtMotDePasse().setEchoChar(caractereMasqueDefaut);
            }
        });

        // 4. Bouton "Mot de passe oublié"
        vue.getBtnMdpOublie().addActionListener(e -> procedureMotDePasseOublie());
    }

    private void verifierIdentifiants() {
        String identifiant = vue.getIdentifiant();
        String motDePasse = vue.getMotDePasse();

        if (identifiant.isEmpty() || motDePasse.isEmpty()) {
            JOptionPane.showMessageDialog(vue, "Veuillez remplir tous les champs.", "Erreur", JOptionPane.WARNING_MESSAGE);
            return;
        }

        // 🔒 LA SÉCURITÉ EST LÀ :
        // On hache le mot de passe tapé par l'utilisateur pour qu'il ait la même forme 
        // que celui stocké dans la base de données.
        String mdpSaisiHache = OutilsSecurite.hacherMotDePasse(motDePasse);

        Connection connexion = ConnexionBDD.getConnexion();
        if (connexion != null) {
            try {
                String sql = "SELECT * FROM Utilisateur WHERE identifiant = ? AND mot_de_passe = ?";
                PreparedStatement ps = connexion.prepareStatement(sql);
                ps.setString(1, identifiant);
                
                // 🔒 On utilise le mot de passe HACHÉ pour la comparaison !
                ps.setString(2, mdpSaisiHache); 

                ResultSet rs = ps.executeQuery();

                if (rs.next()) {
                    JOptionPane.showMessageDialog(vue, "Connexion réussie ! Bienvenue " + identifiant + " !");
                    VueMenuPrincipal menu = new VueMenuPrincipal();
                    menu.setVisible(true);
                    vue.dispose(); 
                } else {
                    JOptionPane.showMessageDialog(vue, "Identifiant ou mot de passe incorrect.", "Accès refusé", JOptionPane.ERROR_MESSAGE);
                }
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(vue, "Erreur BDD : " + ex.getMessage(), "Erreur", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    // --- LA PROCÉDURE DE RÉCUPÉRATION (Version Simulation Email) ---
    private void procedureMotDePasseOublie() {
        // Étape 1 : Demander l'adresse email (au lieu de l'identifiant)
        String email = JOptionPane.showInputDialog(vue, 
            "Veuillez saisir l'adresse email de votre compte :", 
            "Mot de passe oublié", 
            JOptionPane.QUESTION_MESSAGE);
        
        // Si l'utilisateur annule ou ne tape rien
        if (email == null || email.trim().isEmpty()) {
            return; 
        }

        Connection connexion = ConnexionBDD.getConnexion();
        if (connexion != null) {
            try {
                // Étape 2 : Vérifier si cet email existe vraiment dans la base
                String sqlCheck = "SELECT COUNT(*) FROM Utilisateur WHERE email = ?";
                PreparedStatement psCheck = connexion.prepareStatement(sqlCheck);
                psCheck.setString(1, email);
                ResultSet rs = psCheck.executeQuery();
                rs.next();
                
                // Si l'email n'est pas trouvé dans PostgreSQL
                if (rs.getInt(1) == 0) {
                    JOptionPane.showMessageDialog(vue, 
                        "Aucun compte n'est associé à cette adresse email.", 
                        "Erreur", 
                        JOptionPane.ERROR_MESSAGE);
                    return;
                }

                // Étape 3 : La simulation d'envoi d'email !
                // (Dans la vraie vie, c'est ici qu'on utiliserait l'API JavaMail pour envoyer le vrai mail)
                JOptionPane.showMessageDialog(vue, 
                    "Demande de changement de mot de passe envoyée !\n\nVeuillez consulter la boîte de réception de : \n" + email, 
                    "Email envoyé", 
                    JOptionPane.INFORMATION_MESSAGE);

            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(vue, "Erreur BDD : " + ex.getMessage(), "Erreur", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
}