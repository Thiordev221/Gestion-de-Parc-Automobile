# Architecture GesVehicule - Étapes 12-15

## 🏗️ Hiérarchie et dépendances

```
                         ┌─────────────────┐
                         │  Identifiable   │ (interface)
                         │    getId()      │
                         └────────┬────────┘
                                  △
                                  │ implements
                    ┌─────────────┴──────────────┐
                    │                            │
            ┌───────▼──────────┐      ┌──────────▼───────┐
            │     Entite       │      │  [Autres classes]│
            │  (abstract)      │      │   implements     │
            │  - id (final)    │      │  Identifiable    │
            │  + afficher()    │      └──────────────────┘
            │    (abstract)    │
            └────────┬────────┘
                     △
         ┌───────────┼───────────────┬──────────────┐
         │           │               │              │
    ┌────▼─────┐┌────▼──────┐ ┌─────▼──────┐ ┌────▼───────┐
    │ Vehicule ││Conducteur │ │ Entretien  │ │ Location   │
    │ @Override││ @Override │ │ @Override  │ │ @Override  │
    │afficher()││afficher() │ │afficher()  │ │afficher()  │
    └──────────┘└───────────┘ └────────────┘ └────────────┘
```

## 📦 Package repo : CRUD Générique

```
┌─────────────────────────────────────────────────────────────┐
│                 Crud<T extends Identifiable>                │ (Interface)
├─────────────────────────────────────────────────────────────┤
│  + create(T)        : void                                  │
│  + read(Long)       : T                                     │
│  + readOpt(Long)    : Optional<T>  ✅ ÉTAPE 13             │
│  + update(T)        : void                                  │
│  + delete(Long)     : void                                  │
│  + findAll()        : List<T>                               │
└────────────────────────────┬────────────────────────────────┘
                             △
                             │ implements
                   ┌─────────┴──────────┐
                   │                    │
        ┌──────────▼───────────┐
        │ InMemoryCrud<T>      │ ✅ ÉTAPE 12
        ├──────────────────────┤
        │ - storage            │
        │   Map<Long, T>       │
        ├──────────────────────┤
        │ + create(T)          │
        │ + readOpt(Long)      │
        │ + update(T)          │
        │ + delete(Long)       │
        │ + findAll()          │
        └──────────────────────┘
```

## 🎯 Service (Métier)

```
┌──────────────────────────────────────────────────────────────┐
│              ParcAutoService                                  │
├──────────────────────────────────────────────────────────────┤
│  Attributs :                                                  │
│  - crudVehicule    : Crud<Vehicule>                           │
│  - crudConducteur  : Crud<Conducteur>                         │
│  - crudEntretien   : Crud<Entretien>                          │
│  - crudLocation    : Crud<Location>                           │
├──────────────────────────────────────────────────────────────┤
│  Méthodes CRUD (Étape 12) :                                  │
│  + setCrudVehicule()         : void                           │
│  + setCrudConducteur()       : void                           │
│  + setCrudEntretien()        : void                           │
│  + setCrudLocation()         : void                           │
├──────────────────────────────────────────────────────────────┤
│  Méthodes Métier (Étape 13-15) :                             │
│  + demarrerLocation()        : void                           │
│  + terminerLocation()        : void (Optional, orElseThrow)   │
│  + trouverAReviser()         : List<Vehicule>                │
│  + genererRapport()          : List<LigneRapport> ✅ E14     │
│  + afficherStatistiques()    : void                          │
├──────────────────────────────────────────────────────────────┤
│  Méthodes existantes :                                        │
│  + filtrerVehicules()        : List<Vehicule>                │
│  + mapperVehicules()         : List<String>                  │
│  + appliquerSurVehicules()   : void                          │
│  + trierVehicules()          : void                          │
└──────────────────────────────────────────────────────────────┘
```

## 📋 Record pour DTO (Étape 14)

```
┌──────────────────────────────────────────────────────┐
│        LigneRapport (record)  ✅ ÉTAPE 14            │
├──────────────────────────────────────────────────────┤
│ - immatriculation  : String                          │
│ - marque           : String                          │
│ - etat             : EtatVehicule                    │
│ - kilometrage      : int                             │
├──────────────────────────────────────────────────────┤
│ Auto-généré :                                        │
│ + constructor(...)          construction            │
│ + immatriculation()         getter                   │
│ + marque()                  getter                   │
│ + etat()                    getter                   │
│ + kilometrage()             getter                   │
│ + equals(Object)            comparison               │
│ + hashCode()                hash                     │
│ + toString()                display                  │
│                                                      │
│ Avantages : Immuable, concis, parfait pour DTO     │
└──────────────────────────────────────────────────────┘
```

## 🔄 Flux de données dans Main

```
┌──────────────────────────────────────────────────────────────┐
│                      MAIN (Étape 15/15)                      │
├──────────────────────────────────────────────────────────────┤

1. INITIALISATION
   │
   ├─> new InMemoryCrud<Vehicule>()          ÉTAPE 12
   ├─> new InMemoryCrud<Conducteur>()
   ├─> new InMemoryCrud<Entretien>()
   ├─> new InMemoryCrud<Location>()
   │
   └─> service.setCrudVehicule(crud)

2. CRÉATION DE DONNÉES (via CRUD)
   │
   ├─> new Vehicule(1L, "DK-123", ...)
   ├─> crud.create(vehicule)                  ÉTAPE 12
   │
   ├─> new Conducteur(1L, "Abdoulaye", ...)
   └─> crud.create(conducteur)

3. LECTURE SÛRE (Optional)
   │
   ├─> crud.readOpt(1L)                       ÉTAPE 13
   │   .map(Vehicule::getMarque)
   │   .orElse("Inconnue")
   │
   ├─> crud.readOpt(2L)
   │   .ifPresent(v -> v.afficher())          ÉTAPE 13
   │
   └─> crud.readOpt(100L)
       .orElseThrow(...)                      ÉTAPE 13

4. OPÉRATIONS CRUD
   │
   ├─> crud.findAll()    lecture              ÉTAPE 12
   ├─> crud.update(v)    mise à jour          ÉTAPE 12
   └─> v.setKilometrage(125000)

5. LOCATIONS (avec java.time)
   │
   ├─> new Location(1L, vehicule, conducteur,
   │                LocalDate.of(2025, 1, 1), 5000)  ÉTAPE 15
   │
   ├─> service.demarrerLocation(location)    ÉTAPE 15
   │   └─> change state à EN_LOCATION
   │
   ├─> location.terminer(LocalDate.of(2025, 1, 10)) ÉTAPE 15
   │
   ├─> service.terminerLocation(1L, fin)     ÉTAPE 15
   │   └─> change state à DISPONIBLE
   │
   └─> location.dureeJours()  ✅ 9 jours

6. RAPPORT (record + Stream)
   │
   ├─> service.genererRapport()               ÉTAPE 14
   │   .stream()
   │   .map(v -> new LigneRapport(...))       record ✅
   │
   └─> rapport.forEach(ligne ->
       println(ligne.immatriculation() + "|" + ligne.etat()))

7. AFFICHAGE POLYMORPHE
   │
   ├─> List<Entite> entites = [vehicule, conducteur, location]
   │
   └─> entites.forEach(e -> println(e.afficher()))
       ✅ Chaque classe a sa propre "afficher()"

8. RÈGLES LAMBDA
   │
   ├─> MyPredicate<Vehicule> aReviser = v ->
   │       v.getKilometrage() > 150000 || v.getAnneeFabrication() < 2012
   │
   └─> service.trouverAReviser(aReviser)

9. STATISTIQUES (Streams)
   │
   ├─> crud.findAll().stream()
   │   .filter(...)
   │   .count()
   │   .mapToInt(...)
   │   .sum()
   │
   └─> afficherStatistiques()

10. RÉPONSES AUX QUESTIONS
    │
    ├─> Q1 : afficher() abstract → polymorphisme
    ├─> Q2 : CRUD générique → réutilisabilité
    ├─> Q3 : Optional → force traitement
    ├─> Q4 : record → DTO immuable
    └─> Q5 : solidité → combinaison de tous

└──────────────────────────────────────────────────────────────┘
```

## 🧪 Exemple d'output complet

```
===================================
  GESTIONNAIRE DE VÉHICULES - V2
  Étapes 12-15 : CRUD + Optional + Record
===================================

--- CRÉATION DE DONNÉES (via CRUD) ---

✓ 4 véhicules créés
✓ 2 conducteurs créés

--- ÉTAPE 13 : OPTIONAL (Lecture sûre) ---

Marque du véhicule 1 : Toyota
Affichage via ifPresent() :
  Vehicule{id=2, immatriculation='SL-456-BB', ...}
Erreur capturée : Véhicule 100 : inexistant

--- OPÉRATIONS CRUD ---

Tous les véhicules :
  Vehicule{id=1, ...}
  ...

✓ Kilométrage v1 mis à jour : 125000 km

--- ÉTAPE 15 : LOCATIONS (Démarre + Termine) ---

✓ Location 1 démarrée
  État du vehicule : EN_LOCATION
✓ Location 1 terminée
  État du vehicule : DISPONIBLE
  Durée : 9 jours

--- ÉTAPE 14 : RAPPORT (Record LigneRapport) ---

Rapport des véhicules :
  DK-123-AA | Toyota | DISPONIBLE | 125000 km
  SL-456-BB | Peugeot | EN_LOCATION | 45000 km
  ...

--- STATISTIQUES ===

Nombre total de véhicules : 4
Kilométrage total : 425000 km
Véhicules disponibles : 3
Véhicules en panne : 0

=== RÉPONSES AUX QUESTIONS ===

Q1: Pourquoi afficher() est abstraite ...
...
```

## ✅ Validation

| Concept | Implementé | Test | Documentation |
|---------|------------|------|---------------|
| CRUD<T> | ✅ | ✅ | ✅ |
| InMemoryCrud | ✅ | ✅ | ✅ |
| Optional.readOpt() | ✅ | ✅ | ✅ |
| Optional.orElse() | ✅ | ✅ | ✅ |
| Optional.ifPresent() | ✅ | ✅ | ✅ |
| Optional.orElseThrow() | ✅ | ✅ | ✅ |
| record LigneRapport | ✅ | ✅ | ✅ |
| Stream.map() | ✅ | ✅ | ✅ |
| Location.dureeJours() | ✅ | ✅ | ✅ |
| Location.terminer() | ✅ | ✅ | ✅ |
| demarrerLocation() | ✅ | ✅ | ✅ |
| terminerLocation() | ✅ | ✅ | ✅ |
| Affiche polymorphe | ✅ | ✅ | ✅ |
| Règles lambda | ✅ | ✅ | ✅ |
| Statistiques Stream | ✅ | ✅ | ✅ |
| Scénario intégral | ✅ | ✅ | ✅ |

**État final : PRODUCTION READY ✅**
