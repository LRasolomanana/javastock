package src;

public class DenreeSeche extends Article {
    private String poids;

    public DenreeSeche(int id, String libelle, int quantite, String poids) {
        super(id, libelle, quantite);
        this.poids = poids;
    }

    @Override
    public void consulter() {
        super.consulter();
        System.out.println("Poids: " + poids);
    }
}