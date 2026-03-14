package src;

import java.security.MessageDigest;
import java.nio.charset.StandardCharsets;

public class OutilsSecurite {

    // Méthode qui transforme un texte en clair en une empreinte SHA-256
    public static String hacherMotDePasse(String motDePasseEnClair) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(motDePasseEnClair.getBytes(StandardCharsets.UTF_8));
            
            // On convertit les octets en texte hexadécimal lisible
            StringBuilder hexString = new StringBuilder();
            for (byte b : hash) {
                String hex = Integer.toHexString(0xff & b);
                if (hex.length() == 1) {
                    hexString.append('0');
                }
                hexString.append(hex);
            }
            return hexString.toString();
            
        } catch (Exception ex) {
            throw new RuntimeException("Erreur lors du hachage du mot de passe", ex);
        }
    }
}