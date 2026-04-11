package sn.abdoulayeThior.l2gl.app.model;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.Objects;
import java.util.Optional;

public class Location extends Entite {
    private final Vehicule vehicule;
    private final Conducteur conducteur;
    private final LocalDate dateDebut;
    private Optional<LocalDate> dateFin;
    private final int prixJour;

    public Location(Long id, Vehicule vehicule, Conducteur conducteur, LocalDate dateDebut, int prixJour) {
        super(id);
        this.vehicule = Objects.requireNonNull(vehicule);
        this.conducteur = Objects.requireNonNull(conducteur);
        this.dateDebut = Objects.requireNonNull(dateDebut);
        this.dateFin = Optional.empty();
        if (prixJour < 0) throw new IllegalArgumentException("Prix >=0");
        this.prixJour = prixJour;
    }

    public Vehicule getVehicule() {
        return vehicule;
    }

    public Conducteur getConducteur() {
        return conducteur;
    }

    public LocalDate getDateDebut() {
        return dateDebut;
    }

    public Optional<LocalDate> getDateFin() {
        return dateFin;
    }

    public int getPrixJour() {
        return prixJour;
    }

    public void terminer(LocalDate fin) {
        if (fin.isBefore(dateDebut)) {
            throw new IllegalArgumentException("Date fin before date debut");
        }
        this.dateFin = Optional.of(fin);
    }

    public long dureeJours() {
        LocalDate fin = dateFin.orElse(LocalDate.now());
        return ChronoUnit.DAYS.between(dateDebut, fin);
    }

    @Override
    public String afficher() {
        return "Location{" +
                "id=" + getId() +
                ", vehicule=" + vehicule.afficher() +
                ", conducteur=" + conducteur.afficher() +
                ", dateDebut=" + dateDebut +
                ", dateFin=" + dateFin +
                ", prixJour=" + prixJour +
                '}';
    }
}