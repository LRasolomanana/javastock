package src;

public class Reservation {
    private int id;
    private String date;
    private Coureur coureur;
    private TypeEpreuve typeEpreuve;
    
    public Reservation(int id, String date, Coureur coureur, TypeEpreuve typeEpreuve) {
        this.id = id;
        this.date = date;
        this.coureur = coureur;
        this.typeEpreuve = typeEpreuve;
    }
    // TODO: Ajouter méthodes CRER, MODIFIER, CONSULTER, SUPPRIMER et logique de vérification de stock
}