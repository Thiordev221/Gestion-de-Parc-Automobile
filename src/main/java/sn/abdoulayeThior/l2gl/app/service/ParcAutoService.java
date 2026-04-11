package sn.abdoulayeThior.l2gl.app.service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import sn.abdoulayeThior.l2gl.app.model.Conducteur;
import sn.abdoulayeThior.l2gl.app.model.Entretien;
import sn.abdoulayeThior.l2gl.app.model.EtatVehicule;
import sn.abdoulayeThior.l2gl.app.model.LigneRapport;
import sn.abdoulayeThior.l2gl.app.model.Location;
import sn.abdoulayeThior.l2gl.app.model.MyComparator;
import sn.abdoulayeThior.l2gl.app.model.MyConsumer;
import sn.abdoulayeThior.l2gl.app.model.MyFunction;
import sn.abdoulayeThior.l2gl.app.model.MyPredicate;
import sn.abdoulayeThior.l2gl.app.model.Vehicule;
import sn.abdoulayeThior.l2gl.app.repo.Crud;

public class ParcAutoService {
    
    // CRUDs pour chaque type d'entité (Étape 12/15)
    private Crud<Vehicule> crudVehicule;
    private Crud<Conducteur> crudConducteur;
    private Crud<Entretien> crudEntretien;
    private Crud<Location> crudLocation;

    // Constructeur pour injection de dépendances
    public void setCrudVehicule(Crud<Vehicule> crud) {
        this.crudVehicule = crud;
    }

    public void setCrudConducteur(Crud<Conducteur> crud) {
        this.crudConducteur = crud;
    }

    public void setCrudEntretien(Crud<Entretien> crud) {
        this.crudEntretien = crud;
    }

    public void setCrudLocation(Crud<Location> crud) {
        this.crudLocation = crud;
    }
    //1. Filter
    //On utilise un Predicate de type Vehicule
    public List<Vehicule> filtrerVehicules(List<Vehicule> src, MyPredicate<Vehicule> regle){
        List<Vehicule> resultat = new ArrayList<>();
        for (Vehicule vehicule : src) {
            if (regle.test(vehicule)) resultat.add(vehicule);
        }
        return resultat;
    }

    //2. Mapper
    //On utilise ici une interface de type MyFunction
    public List<String> mapperVehicules(List<Vehicule> src, MyFunction<Vehicule, String> f){
        List<String> resultat = new ArrayList<>();
        for (Vehicule vehicule : src) {
            resultat.add(f.apply(vehicule));
        }
        return resultat;
    }

    //3. Action
    //On utilise une interface de type MyConsumer
    public void appliquerSurVehicules(List<Vehicule> src, MyConsumer<Vehicule> action){
        for (Vehicule vehicule : src) {
            action.accept(vehicule);
        }
    }

    //4. Trie
    //On utilise une interface de type Comparator
    public void trierVehicules(List<Vehicule> src, MyComparator<Vehicule> cmp) {
        // Utilisation d'un tri classique (Bulle ou autre) utilisant notre interface
        for (int i = 0; i < src.size(); i++) {
            for (int j = i + 1; j < src.size(); j++) {
                if (cmp.compare(src.get(i), src.get(j)) > 0) {
                    Vehicule temp = src.get(i);
                    src.set(i, src.get(j));
                    src.set(j, temp);
                }
            }
        }
    }

    // ========== ÉTAPE 14/15 : RAPPORT ET STATISTIQUES ==========

    /**
     * Génère un rapport à partir des véhicules.
     * Utilise Stream et map pour transformer Vehicule -> LigneRapport.
     */
    public List<LigneRapport> genererRapport() {
        return crudVehicule.findAll().stream()
            .map(v -> new LigneRapport(
                v.getImmatriculation(),
                v.getMarque(),
                v.getEtat(),
                v.getKilometrage()
            ))
            .collect(Collectors.toList());
    }

    // ========== ÉTAPE 15/15 : OPÉRATIONS LOCATION ==========

    /**
     * Démarre une location : crée la location et change l'état du véhicule.
     * Utilise readOpt() pour éviter les nulls.
     */
    public void demarrerLocation(Location location) {
        crudLocation.create(location);
        
        // Récupère le véhicule de manière sûre avec Optional
        crudVehicule.readOpt(location.getVehicule().getId())
            .ifPresent(v -> {
                v.setEtat(EtatVehicule.EN_LOCATION);
                crudVehicule.update(v);
            });
    }

    /**
     * Termine une location avec validations.
     * Utilise readOpt() et orElseThrow pour gestion sûre.
     */
    public void terminerLocation(Long locationId, LocalDate fin) {
        Location location = crudLocation.readOpt(locationId)
            .orElseThrow(() -> new IllegalArgumentException("Location " + locationId + " introuvable"));
        
        location.terminer(fin);
        crudLocation.update(location);
        
        // Change l'état du véhicule
        crudVehicule.readOpt(location.getVehicule().getId())
            .ifPresent(v -> {
                v.setEtat(EtatVehicule.DISPONIBLE);
                crudVehicule.update(v);
            });
    }

    /**
     * Trouve les véhicules à réviser selon une règle (lambda).
     * Étape 15/15 : Règle combinant km, âge, et durée location.
     */
    public List<Vehicule> trouverAReviser(MyPredicate<Vehicule> regle) {
        return filtrerVehicules(crudVehicule.findAll(), regle);
    }

    /**
     * Crée une location et la sauvegarde.
     * Version sûre avec Optional.
     */
    public Optional<Location> creerLocation(Long locationId, Vehicule vehicule, Conducteur conducteur,
                                           LocalDate dateDebut, int prixJour) {
        try {
            Location location = new Location(locationId, vehicule, conducteur, dateDebut, prixJour);
            crudLocation.create(location);
            return Optional.of(location);
        } catch (IllegalArgumentException e) {
            return Optional.empty();
        }
    }

    /**
     * Obtient les statistiques de base.
     */
    public void afficherStatistiques() {
        List<Vehicule> vehicules = crudVehicule.findAll();
        
        System.out.println("\n=== STATISTIQUES ===");
        System.out.println("Nombre total de véhicules : " + vehicules.size());
        
        int kmTotal = vehicules.stream()
            .mapToInt(Vehicule::getKilometrage)
            .sum();
        System.out.println("Kilométrage total : " + kmTotal + " km");
        
        long disponibles = vehicules.stream()
            .filter(v -> v.isDisponible())
            .count();
        System.out.println("Véhicules disponibles : " + disponibles);
        
        long enPanne = vehicules.stream()
            .filter(Vehicule::isEnPanne)
            .count();
        System.out.println("Véhicules en panne : " + enPanne);
    }
}