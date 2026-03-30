package sn.abdoulayeThior.app.model;

import java.util.Calendar;
import java.util.Date;

public class Vehicule {
    private String immatriculation;
    private String marque;
    private TypeVehicule type;
    private Disponibilite etat;
    private Calendar dateFabrication;

    public Vehicule(String immatriculation, String marque, TypeVehicule type, Disponibilite etat, Calendar dateFabrication) {
        this.immatriculation = immatriculation;
        this.marque = marque;
        this.type = type;
        this.etat = etat;
        this.dateFabrication = dateFabrication;
    }

    public Calendar getDateFabrication() {
        return dateFabrication;
    }

    public Disponibilite getEtat() {
        return etat;
    }

    public TypeVehicule getType() {
        return type;
    }

    public String getMarque() {
        return marque;
    }

    public String getImmatriculation() {
        return immatriculation;
    }

    public void setImmatriculation(String immatriculation) {
        this.immatriculation = immatriculation;
    }

    public void setMarque(String marque) {
        this.marque = marque;
    }

    public void setType(TypeVehicule type) {
        this.type = type;
    }

    public void setEtat(Disponibilite etat) {
        this.etat = etat;
    }

    public void setDateFabrication(Calendar dateFabrication) {
        this.dateFabrication = dateFabrication;
    }
}
