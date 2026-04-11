package sn.abdoulayeThior.l2gl.app.app;

import java.time.LocalDate;
import java.util.List;

import sn.abdoulayeThior.l2gl.app.model.Conducteur;
import sn.abdoulayeThior.l2gl.app.model.Entite;
import sn.abdoulayeThior.l2gl.app.model.Entretien;
import sn.abdoulayeThior.l2gl.app.model.LigneRapport;
import sn.abdoulayeThior.l2gl.app.model.Location;
import sn.abdoulayeThior.l2gl.app.model.MyPredicate;
import sn.abdoulayeThior.l2gl.app.model.Vehicule;
import sn.abdoulayeThior.l2gl.app.repo.Crud;
import sn.abdoulayeThior.l2gl.app.repo.InMemoryCrud;
import sn.abdoulayeThior.l2gl.app.service.ParcAutoService;

/**
 * ÉTAPE 15/15 - Scénario final intégral
 * Démontre :
 * - Étape 12: CRUD générique + InMemoryCrud
 * - Étape 13: Optional (readOpt, orElse, orElseThrow, ifPresent)
 * - Étape 14: Record LigneRapport + Stream
 * - Étape 15: java.time + Location + Règles lambda + Affichage polymorphe
 */
public class Main {
    public static void main(String[] args) {
        System.out.println("===================================");
        System.out.println("  GESTIONNAIRE DE VÉHICULES - V2");
        System.out.println("  Étapes 12-15 : CRUD + Optional + Record");
        System.out.println("===================================\n");

        // ========== INITIALISATION ==========
        ParcAutoService service = new ParcAutoService();

        // Étape 12: Création des CRUDs génériques
        Crud<Vehicule> crudVehicule = new InMemoryCrud<>();
        Crud<Conducteur> crudConducteur = new InMemoryCrud<>();
        Crud<Entretien> crudEntretien = new InMemoryCrud<>();
        Crud<Location> crudLocation = new InMemoryCrud<>();

        service.setCrudVehicule(crudVehicule);
        service.setCrudConducteur(crudConducteur);
        service.setCrudEntretien(crudEntretien);
        service.setCrudLocation(crudLocation);

        // ========== CRÉATION DE DONNÉES ==========
        System.out.println("--- CRÉATION DE DONNÉES (à via CRUD) ---\n");

        // Véhicules
        Vehicule v1 = new Vehicule(1L, "DK-123-AA", "Toyota", 2015, 120000);
        Vehicule v2 = new Vehicule(2L, "SL-456-BB", "Peugeot", 2020, 45000);
        Vehicule v3 = new Vehicule(3L, "TH-789-CC", "Mercedes", 2010, 250000);
        Vehicule v4 = new Vehicule(4L, "LG-001-DD", "Renault", 2022, 5000);

        crudVehicule.create(v1);
        crudVehicule.create(v2);
        crudVehicule.create(v3);
        crudVehicule.create(v4);

        System.out.println("✓ 4 véhicules créés");

        // Conducteurs
        Conducteur c1 = new Conducteur(1L, "Abdoulaye Thior", "001");
        Conducteur c2 = new Conducteur(2L, "Marie Diallo", "002");
        crudConducteur.create(c1);
        crudConducteur.create(c2);

        System.out.println("✓ 2 conducteurs créés");

        // ========== ÉTAPE 13 : OPTIONAL - LECTURE SÛRE ==========
        System.out.println("\n--- ÉTAPE 13 : OPTIONAL (Lecture sûre) ---\n");

        // 1. readOpt() + orElse()
        String marqueDefaut = crudVehicule.readOpt(1L)
            .map(Vehicule::getMarque)
            .orElse("Marque inconnue");
        System.out.println("Marque du véhicule 1 : " + marqueDefaut);

        // 2. readOpt() + ifPresent()
        System.out.println("Affichage via ifPresent() :");
        crudVehicule.readOpt(2L)
            .ifPresent(v -> System.out.println("  " + v.afficher()));

        // 3. readOpt() + orElseThrow()
        try {
            Vehicule vehicule = crudVehicule.readOpt(100L)
                .orElseThrow(() -> new IllegalArgumentException("Véhicule 100 : inexistant"));
        } catch (IllegalArgumentException e) {
            System.out.println("Erreur capturée : " + e.getMessage());
        }

        // ========== OPÉRATIONS CRUD ==========
        System.out.println("\n--- OPÉRATIONS CRUD ---\n");

        // READ : afficher tous les véhicules
        System.out.println("Tous les véhicules :");
        crudVehicule.findAll().forEach(v -> System.out.println("  " + v.afficher()));

        // UPDATE : modifier le kilométrage
        v1.setKilometrage(125000);
        crudVehicule.update(v1);
        System.out.println("\n✓ Kilométrage v1 mis à jour : " + v1.getKilometrage() + " km");

        // ========== ÉTAPE 15 : LOCATIONS ==========
        System.out.println("\n--- ÉTAPE 15 : LOCATIONS (Démarre + Termine) ---\n");

        // Création et démarrage d'une location
        Location loc1 = new Location(1L, v1, c1, LocalDate.of(2025, 1, 1), 5000);
        service.demarrerLocation(loc1);
        System.out.println("✓ Location 1 démarrée");
        System.out.println("  État du vehicule : " + v1.getEtat());

        // Autre location
        Location loc2 = new Location(2L, v2, c2, LocalDate.of(2025, 1, 5), 4000);
        service.demarrerLocation(loc2);

        // Terminer la première location
        service.terminerLocation(1L, LocalDate.of(2025, 1, 10));
        System.out.println("\n✓ Location 1 terminée");
        System.out.println("  État du vehicule : " + v1.getEtat());
        System.out.println("  Durée : " + loc1.dureeJours() + " jours");

        // ========== ÉTAPE 14 : RECORD + RAPPORT ==========
        System.out.println("\n--- ÉTAPE 14 : RAPPORT (Record LigneRapport) ---\n");

        List<LigneRapport> rapport = service.genererRapport();
        System.out.println("Rapport des véhicules :");
        rapport.forEach(ligne ->
            System.out.println("  " + ligne.immatriculation() + " | " +
                              ligne.marque() + " | " +
                              ligne.etat() + " | " +
                              ligne.kilometrage() + " km")
        );

        // ========== AFFICHAGE POLYMORPHE ==========
        System.out.println("\n--- AFFICHAGE POLYMORPHE (afficher() @Override) ---\n");

        List<Entite> entites = new java.util.ArrayList<>();
        entites.add(v1);
        entites.add(c1);
        entites.add(loc1);

        System.out.println("Affichage polymorphe via afficher() :");
        entites.forEach(e -> System.out.println("  " + e.afficher()));

        // ========== RÈGLES LAMBDA ET STATISTIQUES ==========
        System.out.println("\n--- RÈGLES LAMBDA : Véhicules à réviser ---\n");

        // Règle 1 : km > 150000 OU année < 2012
        MyPredicate<Vehicule> aReviser = v ->
            v.getKilometrage() > 150000 || v.getAnneeFabrication() < 2012;

        List<Vehicule> aReviserList = service.trouverAReviser(aReviser);
        System.out.println("Véhicules à réviser (km > 150k OU année < 2012) :");
        aReviserList.forEach(v -> System.out.println("  " + v.getImmatriculation() +
                                                     " : " + v.getKilometrage() + " km, " +
                                                     v.getAnneeFabrication()));

        // Statistiques
        service.afficherStatistiques();

        // ========== CONCLUSION ==========
        System.out.println("\n===================================");
        System.out.println("  RÉPONSES AUX QUESTIONS");
        System.out.println("===================================\n");

        System.out.println("Q1: Pourquoi afficher() est abstraite au lieu d'être générique ?");
        System.out.println("R: Chaque entité a sa propre représentation (polymorphisme).\n");

        System.out.println("Q2: Que permet le CRUD générique ?");
        System.out.println("R: Réutilisabilité : même logique pour Vehicule, Conducteur, etc.\n");

        System.out.println("Q3: Comment Optional force le développeur ?");
        System.out.println("R: Oblige à traiter le cas absent explicitement (orElse, ifPresent, etc).\n");

        System.out.println("Q4: Pourquoi un record pour un rapport ?");
        System.out.println("R: Immuabilité, concision, idéal pour les DTO sans logique.\n");

        System.out.println("Q5: Quel choix rend le code plus solide ?");
        System.out.println("R: Combinaison :");
        System.out.println("   - Enums (états strictement définis)");
        System.out.println("   - Génériques (réutilisabilité + type-safe)");
        System.out.println("   - Optional (pas de nulls)");
        System.out.println("   - Records (DTO immuables)");
        System.out.println("   - Lambdas (règles métier flexibles)");
        System.out.println("Exemple : Une Location typée par le CRUD, garantit l'absence de null\n");
    }
}