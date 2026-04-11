package sn.abdoulayeThior.l2gl.app.model;

import java.util.Objects;

public class Vehicule extends Entite {
    private final String immatriculation;
    private final String marque;
    private int kilometrage;
    private EtatVehicule etat;
    private final int annee;

    public Vehicule(Long id, String immatriculation, String marque, int annee, int kilometrage) {
        super(id);
        this.immatriculation = Objects.requireNonNull(immatriculation);
        this.marque = Objects.requireNonNull(marque);
        if (annee < 1990) throw new IllegalArgumentException("Annee >= 1990");
        this.annee = annee;
        if (kilometrage < 0) throw new IllegalArgumentException("Km >=0");
        this.kilometrage = kilometrage;
        this.etat = EtatVehicule.DISPONIBLE;
    }

    public String getImmatriculation() {
        return immatriculation;
    }

    public String getMarque() {
        return marque;
    }

    public int getKilometrage() {
        return kilometrage;
    }

    public EtatVehicule getEtat() {
        return etat;
    }

    public int getAnneeFabrication() {
        return annee;
    }

    public void setKilometrage(int kilometrage) {
        if (kilometrage < 0) throw new IllegalArgumentException("Km >=0");
        this.kilometrage = kilometrage;
    }

    public void setEtat(EtatVehicule etat) {
        this.etat = Objects.requireNonNull(etat);
    }

    public boolean isEnPanne() {
        return etat == EtatVehicule.EN_PANNE;
    }

    public boolean isDisponible() {
        return etat == EtatVehicule.DISPONIBLE;
    }

    public void setDisponible(boolean disponible) {
        this.etat = disponible ? EtatVehicule.DISPONIBLE : EtatVehicule.EN_REVISION;
    }

    @Override
    public String afficher() {
        return "Vehicule{" +
                "id=" + getId() +
                ", immatriculation='" + immatriculation + '\'' +
                ", marque='" + marque + '\'' +
                ", kilometrage=" + kilometrage +
                ", etat=" + etat +
                ", annee=" + annee +
                '}';
    }
}