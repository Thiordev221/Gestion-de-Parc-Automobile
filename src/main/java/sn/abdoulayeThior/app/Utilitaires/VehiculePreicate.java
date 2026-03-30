package sn.abdoulayeThior.app.Utilitaires;

import sn.abdoulayeThior.app.model.Vehicule;

@FunctionalInterface
public interface VehiculePreicate {
    boolean test(Vehicule t);
}
