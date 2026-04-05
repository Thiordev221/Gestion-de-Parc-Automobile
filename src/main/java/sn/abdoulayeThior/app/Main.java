package sn.abdoulayeThior.app;

import sn.abdoulayeThior.app.Utilitaires.ChauffeurPredicate;
import sn.abdoulayeThior.app.Utilitaires.DateUtils;
import sn.abdoulayeThior.app.Utilitaires.VehiculeChauffeurPrediacte;
import sn.abdoulayeThior.app.Utilitaires.VehiculePreicate;
import sn.abdoulayeThior.app.model.*;
import sn.abdoulayeThior.app.service.ParcAutoService;

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


        Vehicule v1 = new Vehicule("lokd4", "Toyota", TypeVehicule.LEGER,70000, Disponibilite.DISPONIBLE, 15000000L, DateUtils.createCalendar(1, 1, 2022));
        Vehicule v2 = new Vehicule("lokd4", "Toyota", TypeVehicule.LEGER,25000, Disponibilite.DISPONIBLE, 25000000L, DateUtils.createCalendar(1, 1, 2022));
        ParcAutoService.ajouterVehicule(v1);
        ParcAutoService.ajouterVehicule(v2);

        ParcAutoService.vehiculesUniques().forEach(System.out::println);

        Entretien E1 = new Entretien(1L, 50000L);
        Entretien E2 = new Entretien(2L, 35000L);

        ParcAutoService.ajouterEntretien(E1, 1L);
        ParcAutoService.ajouterEntretien(E2, 1L);
        ParcAutoService.getEntretiens(1L).forEach(System.out::println);

    }

}
