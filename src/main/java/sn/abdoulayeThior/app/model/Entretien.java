package sn.abdoulayeThior.app.model;

import java.util.Objects;

public class Entretien {
    private Long id;
    private Long cout;

    public Entretien(Long id, Long cout) {
        this.id = id;
        this.cout = cout;
    }

    public Long getCout() {
        return cout;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof Entretien entretien)) return false;
        return Objects.equals(id, entretien.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }
}
