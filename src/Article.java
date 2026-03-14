package src;

public class Article {
    // Attributs communs à tous les articles
    protected int id;
    protected String libelle;
    protected int quantite;
    protected boolean indicateurSL; // true si supprimé logiquement

    // Constructeur
    public Article(int id, String libelle, int quantite) {
        this.id = id;
        this.libelle = libelle;
        this.quantite = quantite;
        this.indicateurSL = false;
    }

    // --- Les 4 méthodes génériques demandées (avec exemple de surcharge) ---

    public void creer() {
        System.out.println("Création de l'article en base de données...");
    }

    public void consulter() {
        System.out.println("ID: " + id + " | Libellé: " + libelle + " | Quantité: " + quantite);
    }

    // Surcharge : Modifier sans paramètres spécifiques
    public void modifier() {
        System.out.println("Modification de l'article...");
    }

    // Surcharge : Modifier avec un nouveau libellé
    public void modifier(String nouveauLibelle) {
        this.libelle = nouveauLibelle;
        System.out.println("Le libellé a été modifié en : " + nouveauLibelle);
    }

    public void supprimer() {
        this.indicateurSL = true;
        System.out.println("L'article a été supprimé logiquement (indicateurSL = true).");
    }

    // Getters et Setters (Accesseurs)
    public int getId() { return id; }
    public String getLibelle() { return libelle; }
    public int getQuantite() { return quantite; }
    public void setQuantite(int quantite) { this.quantite = quantite; }
}