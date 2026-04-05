package sn.abdoulayeThior.app.service;

import sn.abdoulayeThior.app.model.Disponibilite;
import sn.abdoulayeThior.app.model.Entretien;
import sn.abdoulayeThior.app.model.Vehicule;

import java.util.*;
import java.util.stream.Collectors;

public class ParcAutoService {

    private static List<Vehicule> vehicules = new ArrayList<>();
    private static Map<String, Vehicule> indexParImmat = new HashMap<>();
    private static Map<Long, List<Entretien>> entretiensParVehiculeId = new HashMap<>();

    public static void ajouterVehicule(Vehicule v){
        vehicules.add(v);
        indexParImmat.put(v.getImmatriculation(), v);
    }

    public static void supprimerVehicule(String immat){
        vehicules.remove(indexParImmat.get(immat));
        indexParImmat.remove(immat);
    }

    public static Vehicule rechercher(String immat){
        return indexParImmat.get(immat);
    }

    public static Set<Vehicule> vehiculesUniques(){
        return new HashSet<>(vehicules);
    }

    public static void ajouterEntretien(Entretien e, Long id){
        List<Entretien> entretiens = new ArrayList<>();
        entretiens.add(e);
        if(!entretiensParVehiculeId.containsKey(id)){
            entretiensParVehiculeId.put(id, entretiens);
        }else{
            entretiensParVehiculeId.get(id).add(e);
        }
    }

    public static List<Entretien> getEntretiens(Long vehiculeId){
        return entretiensParVehiculeId.get(vehiculeId);
    }

    public static List<Vehicule> vehiculesDisponibles(){
        return vehicules.stream()
                .filter(v-> v.getEtat().equals(Disponibilite.DISPONIBLE))
                .toList();
    }

    public static List<String> immatriculationsTriees(){
        return vehicules.stream()
                .map(Vehicule::getImmatriculation)
                .sorted(String::compareTo)
                .toList();
    }

    public static List<Vehicule> plusGrangKilometrage(){
        return vehicules.stream()
                .sorted(Comparator.comparing(Vehicule::getKilometrage))
                .limit(3)
                .toList();
    }

    public static Double kilometrageMoyen(){
        return vehicules.stream()
                .mapToDouble(Vehicule::getKilometrage) // Remplacez par le nom exact de votre getter
                .average()
                .orElse(0.0);
    }

    public static Map<Disponibilite, Long> nombreVehiculesParEtat(){
        return vehicules.stream()
                .collect(Collectors.groupingBy(
                        Vehicule::getEtat, Collectors.counting()
                ));
    }

    public static Map<Vehicule, Integer> totalCoutsEntretienParVehicule() {
        return vehicules.stream()
                .collect(Collectors.groupingBy(
                        vehicule -> vehicule, // La clé est l'objet véhicule lui-même
                        Collectors.summingInt(Vehicule::getCoutEntretien) // On somme les coûts
                ));
    }
}
