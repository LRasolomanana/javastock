package src;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConnexionBDD {
    // Paramètres de connexion (À adapter selon ta configuration PostgreSQL)
    // Si ta base est sur un serveur spécifique (comme le srvjava mentionné dans ton projet), remplace localhost par l'IP.
    private static final String URL = "jdbc:postgresql://localhost:5432/javastocks";
    private static final String UTILISATEUR = "postgres"; // Ton nom d'utilisateur PostgreSQL
    private static final String MOT_DE_PASSE = "Yukichien86"; // TON mot de passe PostgreSQL

    private static Connection connexion = null;

    // Constructeur privé pour empêcher de créer plusieurs connexions (Singleton)
    private ConnexionBDD() {
    }

    /**
     * Méthode pour obtenir la connexion à la base de données.
     */
    public static Connection getConnexion() {
        if (connexion == null) {
            try {
                // Établissement de la connexion
                connexion = DriverManager.getConnection(URL, UTILISATEUR, MOT_DE_PASSE);
                System.out.println("✅ Connexion à PostgreSQL réussie !");
            } catch (SQLException e) {
                System.out.println("❌ Erreur de connexion à la base de données : " + e.getMessage());
            }
        }
        return connexion;
    }

// --- PETIT TEST DIRECT ---
    public static void main(String[] args) {
        System.out.println("Lancement du test de connexion...");
        getConnexion();
    }
}