package src;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

public class ReservationHebergement {

    // ==========================================
    // ÉNUMÉRATION INTÉGRÉE
    // ==========================================
    public enum TypeHebergement {
        STUDIO,
        F1,
        F2
    }

    // ==========================================
    // ATTRIBUTS
    // ==========================================
    private int idReservation;
    private Coureur coureur; 
    private TypeHebergement type;
    private LocalDate dateDebut;
    private LocalDate dateFin;
    private int nombreNuites;

    // ==========================================
    // CONSTRUCTEURS
    // ==========================================

    /**
     * Constructeur complet
     */
    public ReservationHebergement(int idReservation, Coureur coureur, TypeHebergement type, LocalDate dateDebut, LocalDate dateFin) {
        this.idReservation = idReservation;
        this.coureur = coureur;
        this.type = type;
        this.dateDebut = dateDebut;
        this.dateFin = dateFin;
        
        // Calcul automatique à l'instanciation
        this.nombreNuites = calculerNuites(dateDebut, dateFin);
    }

    /**
     * Constructeur vide
     */
    public ReservationHebergement() {
    }

    // ==========================================
    // LOGIQUE MÉTIER
    // ==========================================

    /**
     * Calcule le nombre de jours entre deux dates.
     * Retourne 0 si les dates sont invalides (ex: départ avant l'arrivée).
     */
    private int calculerNuites(LocalDate debut, LocalDate fin) {
        if (debut != null && fin != null && !fin.isBefore(debut)) {
            return (int) ChronoUnit.DAYS.between(debut, fin);
        }
        return 0; 
    }

    // ==========================================
    // GETTERS & SETTERS
    // ==========================================

    public int getIdReservation() {
        return idReservation;
    }

    public void setIdReservation(int idReservation) {
        this.idReservation = idReservation;
    }

    public Coureur getCoureur() {
        return coureur;
    }

    public void setCoureur(Coureur coureur) {
        this.coureur = coureur;
    }

    public TypeHebergement getType() {
        return type;
    }

    public void setType(TypeHebergement type) {
        this.type = type;
    }

    public LocalDate getDateDebut() {
        return dateDebut;
    }

    public void setDateDebut(LocalDate dateDebut) {
        this.dateDebut = dateDebut;
        this.nombreNuites = calculerNuites(this.dateDebut, this.dateFin);
    }

    public LocalDate getDateFin() {
        return dateFin;
    }

    public void setDateFin(LocalDate dateFin) {
        this.dateFin = dateFin;
        this.nombreNuites = calculerNuites(this.dateDebut, this.dateFin);
    }

    public int getNombreNuites() {
        return nombreNuites;
    }

    // ==========================================
    // MÉTHODES UTILITAIRES
    // ==========================================

    @Override
    public String toString() {
        String infoCoureur = (coureur != null) ? coureur.getPrenom() + " " + coureur.getNom().toUpperCase() : "Inconnu";
        
        return "=== RÉCAPITULATIF DE RÉSERVATION ===\n" +
               "Numéro de dossier : " + idReservation + "\n" +
               "Coureur           : " + infoCoureur + "\n" +
               "Type de logement  : " + type + "\n" +
               "Date d'arrivée    : " + dateDebut + "\n" +
               "Date de départ    : " + dateFin + "\n" +
               "Durée du séjour   : " + nombreNuites + " nuit(s)\n" +
               "====================================";
    }
}