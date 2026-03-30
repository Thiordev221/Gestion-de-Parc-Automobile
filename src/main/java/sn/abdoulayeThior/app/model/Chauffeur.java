package sn.abdoulayeThior.app.model;

import java.util.Calendar;

public class Chauffeur {
    private Integer id;
    private String nom;
    private String prenom;
    private Calendar dateNaissance;
    private TypePermis permis;
    private Disponibilite etat;

    public Chauffeur(Integer id, String nom, String prenom, Calendar dateNaissance, TypePermis permis, Disponibilite etat) {
        this.id = id;
        this.nom = nom;
        this.prenom = prenom;
        this.dateNaissance = dateNaissance;
        this.permis = permis;
        this.etat = etat;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
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

    public Calendar getDateNaissance() {
        return dateNaissance;
    }

    public void setDateNaissance(Calendar dateNaissance) {
        this.dateNaissance = dateNaissance;
    }

    public TypePermis getPermis() {
        return permis;
    }

    public void setPermis(TypePermis permis) {
        this.permis = permis;
    }

    public Disponibilite getEtat() {
        return etat;
    }

    public void setEtat(Disponibilite etat) {
        this.etat = etat;
    }
}
