package sn.abdoulayeThior.l2gl.app.model;

import java.time.LocalDate;
import java.util.Objects;

public class Entretien extends Entite {
    private final Vehicule vehicule;
    private final LocalDate date;
    private final String description;
    private final int cout;

    public Entretien(Long id, Vehicule vehicule, LocalDate date, String description, int cout) {
        super(id);
        this.vehicule = Objects.requireNonNull(vehicule);
        this.date = Objects.requireNonNull(date);
        this.description = Objects.requireNonNull(description);
        if (cout < 0) throw new IllegalArgumentException("Cout >=0");
        this.cout = cout;
    }

    public Vehicule getVehicule() {
        return vehicule;
    }

    public LocalDate getDate() {
        return date;
    }

    public String getDescription() {
        return description;
    }

    public int getCout() {
        return cout;
    }

    @Override
    public String afficher() {
        return "Entretien{" +
                "id=" + getId() +
                ", vehicule=" + vehicule.afficher() +
                ", date=" + date +
                ", description='" + description + '\'' +
                ", cout=" + cout +
                '}';
    }
}