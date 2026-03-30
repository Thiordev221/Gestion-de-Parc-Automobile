package sn.abdoulayeThior.app;

import sn.abdoulayeThior.app.Utilitaires.ChauffeurPredicate;
import sn.abdoulayeThior.app.Utilitaires.DateUtils;
import sn.abdoulayeThior.app.Utilitaires.VehiculeChauffeurPrediacte;
import sn.abdoulayeThior.app.Utilitaires.VehiculePreicate;
import sn.abdoulayeThior.app.model.Disponibilite;
import sn.abdoulayeThior.app.model.TypePermis;
import sn.abdoulayeThior.app.model.TypeVehicule;

import java.time.LocalDateTime;
import java.util.Calendar;
import java.util.Date;

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class Main {
    static void main() {
        //Vérifier si un conducteur et un vehicule son disponible
        VehiculeChauffeurPrediacte verifierDisponibilite = (vehicule, chauffeur) ->
                vehicule.getEtat() == Disponibilite.DISPONIBLE && chauffeur.getEtat() == Disponibilite.DISPONIBLE;

        //Vérifier si un Conducteur peut conduire un vehicule
        VehiculeChauffeurPrediacte verifierSiPeutConduire = (vehicule, chauffeur) -> {
                if (vehicule.getType() == TypeVehicule.LOURD && chauffeur.getPermis() == TypePermis.A) {
                    return true;
                } else if (vehicule.getType() == TypeVehicule.LEGER && chauffeur.getPermis() == TypePermis.A) {
                    return true;

                } else return vehicule.getType() == TypeVehicule.LEGER && chauffeur.getPermis() == TypePermis.B;
        };

        //Vérifier si amortis
        VehiculePreicate verifierSiAmortis = (vehicule) -> DateUtils.getAge(vehicule.getDateFabrication()) > 5;

        ChauffeurPredicate verifierSiALaRetraite = (chauffeur) -> DateUtils.getAge(chauffeur.getDateNaissance()) > 60;

    }
}
