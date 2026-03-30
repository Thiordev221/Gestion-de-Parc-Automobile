package sn.abdoulayeThior.app.Utilitaires;

import sn.abdoulayeThior.app.model.Chauffeur;

@FunctionalInterface
public interface ChauffeurPredicate {
    boolean test(Chauffeur t);
}
