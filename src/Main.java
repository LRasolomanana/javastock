package src;

import javax.swing.UIManager;
import com.formdev.flatlaf.FlatLightLaf;

public class Main {
    
    public static void main(String[] args) {

        try {
            javax.swing.UIManager.setLookAndFeel(new FlatLightLaf()); 
        } catch (Exception ex) {
            System.err.println("Erreur thème : " + ex.getMessage());
        }
        

        // 2. Création de la fenêtre de connexion (La Vue)
        VueConnexion vueConnexion = new VueConnexion();

        // 3. Création du contrôleur (Le Cerveau)
        // On relie le contrôleur à la vue pour qu'il puisse "écouter" le bouton "Se connecter"
        ConnexionController controleur = new ConnexionController(vueConnexion);

        // 4. On affiche UNIQUEMENT la fenêtre de connexion au démarrage
        vueConnexion.setVisible(true);
    }
}