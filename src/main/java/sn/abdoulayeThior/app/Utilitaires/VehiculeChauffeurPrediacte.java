package sn.abdoulayeThior.app.Utilitaires;

import sn.abdoulayeThior.app.model.Chauffeur;
import  sn.abdoulayeThior.app.model.Vehicule;

@FunctionalInterface
public interface VehiculeChauffeurPrediacte {
    boolean test(Vehicule vehicule,  Chauffeur chauffeur);
}
