package src;

public class Boisson extends Article {
    private String volume;

    public Boisson(int id, String libelle, int quantite, String volume) {
        super(id, libelle, quantite);
        this.volume = volume;
    }

    @Override
    public void consulter() {
        super.consulter();
        System.out.println("Volume: " + volume);
    }
}