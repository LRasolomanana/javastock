package src;

public class Coureur {
    private int id;
    private String nom;
    private String prenom;

    // Constructeur complet (utilisé quand on récupère les données de la base)
    public Coureur(int id, String nom, String prenom) {
        this.id = id;
        this.nom = nom;
        this.prenom = prenom;
    }

    // Constructeur sans ID (utile lors de la création d'un nouveau coureur)
    public Coureur(String nom, String prenom) {
        this.nom = nom;
        this.prenom = prenom;
    }

    // ==========================================
    // GETTERS & SETTERS
    // ==========================================

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getPrenom() {
        return prenom;
    }

    public void setPrenom(String prenom) {
        this.prenom = prenom;
    }

    // ==========================================
    // MÉTHODES UTILITAIRES
    // ==========================================

    @Override
    public String toString() {
        // Formatage propre : Prénom NOM (ID: X)
        return prenom + " " + nom.toUpperCase() + " (ID: " + id + ")";
    }
}