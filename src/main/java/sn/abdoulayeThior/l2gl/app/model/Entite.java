package sn.abdoulayeThior.l2gl.app.model;

import java.util.Objects;

public abstract class Entite implements Identifiable {
    private final Long id;

    protected Entite(Long id) {
        this.id = Objects.requireNonNull(id);
    }

    @Override
    public final Long getId() {
        return id;
    }

    public abstract String afficher();
}