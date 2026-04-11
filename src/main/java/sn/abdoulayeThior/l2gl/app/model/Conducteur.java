package sn.abdoulayeThior.l2gl.app.model;

import java.util.Objects;

public class Conducteur extends Entite {
    private final String nom;
    private final String permis;

    public Conducteur(Long id, String nom, String permis) {
        super(id);
        this.nom = Objects.requireNonNull(nom);
        this.permis = Objects.requireNonNull(permis);
    }

    public String getNom() {
        return nom;
    }

    public String getPermis() {
        return permis;
    }

    @Override
    public String afficher() {
        return "Conducteur{" +
                "id=" + getId() +
                ", nom='" + nom + '\'' +
                ", permis='" + permis + '\'' +
                '}';
    }
}