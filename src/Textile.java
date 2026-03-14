package src;

public class Textile extends Article {
    private String taille;
    private String couleur;

    public Textile(int id, String libelle, int quantite, String taille, String couleur) {
        super(id, libelle, quantite); // Appelle le constructeur de la classe mère (Article)
        this.taille = taille;
        this.couleur = couleur;
    }

    // Surcharge de la méthode consulter pour afficher la taille et la couleur
    @Override
    public void consulter() {
        super.consulter();
        System.out.println("Taille: " + taille + " | Couleur: " + couleur);
    }
}