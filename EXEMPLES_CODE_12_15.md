# Exemples de code - Étapes 12-15

## ÉTAPE 12 : CRUD Générique

### Interface Crud<T>
```java
public interface Crud<T extends Identifiable> {
    void create(T entite);           // Crée + valide id unique
    T read(Long id);                 // Récupère (null si absent)
    Optional<T> readOpt(Long id);    // Récupère de manière sûre
    void update(T entite);           // Met à jour + valide existence
    void delete(Long id);            // Supprime + valide existence
    List<T> findAll();               // Récupère tout
}
```

### Implémentation InMemoryCrud<T>
```java
public class InMemoryCrud<T extends Identifiable> implements Crud<T> {
    private final Map<Long, T> storage = new HashMap<>();

    @Override
    public void create(T entite) {
        if (storage.containsKey(entite.getId())) {
            throw new IllegalArgumentException("L'id existe déjà");
        }
        storage.put(entite.getId(), entite);
    }

    @Override
    public Optional<T> readOpt(Long id) {
        return Optional.ofNullable(storage.get(id));
    }
    // ... autres méthodes
}
```

### Utilisation (dans Main)
```java
Crud<Vehicule> crudVehicule = new InMemoryCrud<>();
Vehicule v = new Vehicule(1L, "DK-123", "Toyota", 2015, 100000);
crudVehicule.create(v);      // Crée

List<Vehicule> tous = crudVehicule.findAll();  // Lit tout
```

---

## ÉTAPE 13 : Optional pour éviter les nulls

### Pattern 1 : orElse() - Valeur par défaut
```java
String marque = crudVehicule.readOpt(1L)
    .map(Vehicule::getMarque)
    .orElse("Marque inconnue");

System.out.println(marque); // "Toyota" ou "Marque inconnue"
```

### Pattern 2 : ifPresent() - Action si présent
```java
crudVehicule.readOpt(2L)
    .ifPresent(v -> {
        System.out.println("Trouvé : " + v.afficher());
        v.setKilometrage(v.getKilometrage() + 1000);
        crudVehicule.update(v);
    });
```

### Pattern 3 : orElseThrow() - Exception si absent
```java
try {
    Vehicule v = crudVehicule.readOpt(100L)
        .orElseThrow(() -> new IllegalArgumentException("Véhicule 100 inexistant"));
} catch (IllegalArgumentException e) {
    System.err.println(e.getMessage());
}
```

### Comparaison : Avant vs Après

**Avant (avec nulls) - ❌ Dangereux**
```java
Vehicule v = repo.read(id);
if (v != null) {
    // On peut oublier le null check
    System.out.println(v.afficher());
}
```

**Après (avec Optional) - ✅ Sûr**
```java
repo.readOpt(id)
    .ifPresent(v -> System.out.println(v.afficher()));
    // Impossible d'oublier le null check
```

---

## ÉTAPE 14 : Record pour DTO

### Définition du record
```java
public record LigneRapport(
    String immatriculation,
    String marque,
    EtatVehicule etat,
    int kilometrage
) {}
```

**Auto-généré par le record :**
- Constructeur complet
- `getters` publics (sans le "get" !)
- `equals()`, `hashCode()`, `toString()`

### Utilisation
```java
// Création
LigneRapport ligne = new LigneRapport("DK-123", "Toyota", DISPONIBLE, 100000);

// Accès (pas de get!)
System.out.println(ligne.immatriculation());
System.out.println(ligne.etat());
```

### Générer un rapport avec Stream
```java
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

// Utilisation
List<LigneRapport> rapport = service.genererRapport();
rapport.forEach(ligne ->
    System.out.println(ligne.immatriculation() + " | " +
                      ligne.marque() + " | " +
                      ligne.kilometrage() + " km")
);
```

---

## ÉTAPE 15 : Scénario intégral avec java.time

### Locations avec java.time

#### Classe Location (améliorée)
```java
public class Location extends Entite {
    private final Vehicule vehicule;
    private final LocalDate dateDebut;           // ✅ java.time
    private Optional<LocalDate> dateFin;         // ✅ Optional

    public long dureeJours() {
        LocalDate fin = dateFin.orElse(LocalDate.now());
        return ChronoUnit.DAYS.between(dateDebut, fin);
    }

    public void terminer(LocalDate fin) {
        if (fin.isBefore(dateDebut)) {
            throw new IllegalArgumentException("Date fin avant début");
        }
        this.dateFin = Optional.of(fin);
    }
}
```

#### Gestion des locations dans le service
```java
public void demarrerLocation(Location location) {
    crudLocation.create(location);
    
    // Récupère le véhicule de manière sûre
    crudVehicule.readOpt(location.getVehicule().getId())
        .ifPresent(v -> {
            v.setEtat(EtatVehicule.EN_LOCATION);
            crudVehicule.update(v);
        });
}

public void terminerLocation(Long locationId, LocalDate fin) {
    Location location = crudLocation.readOpt(locationId)
        .orElseThrow(() -> new IllegalArgumentException("Inexistant"));
    
    location.terminer(fin);
    crudLocation.update(location);
    
    // Restaure l'état du véhicule
    crudVehicule.readOpt(location.getVehicule().getId())
        .ifPresent(v -> {
            v.setEtat(EtatVehicule.DISPONIBLE);
            crudVehicule.update(v);
        });
}
```

### Affichage polymorphe
```java
List<Entite> entites = new ArrayList<>();
entites.add(vehicule);
entites.add(conducteur);
entites.add(location);

// Chaque classe implémente afficher() à sa manière
entites.forEach(e -> System.out.println(e.afficher()));
```

### Règles métier avec lambdas
```java
// Règle : km > 150k OU année < 2012
MyPredicate<Vehicule> aReviser = v ->
    v.getKilometrage() > 150000 || v.getAnneeFabrication() < 2012;

List<Vehicule> veheAReviser = service.trouverAReviser(aReviser);

// Autre règle : en panne ET anciens
MyPredicate<Vehicule> critiquepanne = v ->
    v.isEnPanne() && v.getAnneeFabrication() < 2010;

List<Vehicule> critique = service.trouverAReviser(critiquepanne);
```

### Scénario complet dans Main
```java
public static void main(String[] args) {
    // 1. Init CRUD
    Crud<Vehicule> crud = new InMemoryCrud<>();
    
    // 2. Création de données
    Vehicule v = new Vehicule(1L, "DK-123", "Toyota", 2015, 100000);
    crud.create(v);
    
    // 3. Lecture sûre (Optional)
    crud.readOpt(1L)
        .ifPresent(vehicle -> System.out.println(vehicle.afficher()));
    
    // 4. Location
    Conducteur c = new Conducteur(1L, "Jean", "001");
    Location loc = new Location(1L, v, c, LocalDate.now(), 5000);
    service.demarrerLocation(loc);
    
    // 5. Terminer
    service.terminerLocation(1L, LocalDate.now().plusDays(5));
    System.out.println("Durée : " + loc.dureeJours() + " jours");
    
    // 6. Rapport (record)
    List<LigneRapport> rapport = service.genererRapport();
    rapport.forEach(System.out::println);
    
    // 7. Statistiques
    long kmTotal = crud.findAll().stream()
        .mapToLong(Vehicule::getKilometrage)
        .sum();
    System.out.println("KM total : " + kmTotal);
}
```

---

## Résumé des patterns

| Étape | Pattern | Bénéfice |
|-------|---------|----------|
| 12 | CRUD<T> + InMemoryCrud | Réutilisabilité, type-safety |
| 13 | Optional + orElse/ifPresent | Évite les nulls |
| 14 | record + Stream.map() | DTOs immuables |
| 15 | java.time + lambdas | Dates sûres, règles flexibles |

---

## Tests possibles

```java
// Test CRUD
@Test
public void testCreateVoiture() {
    Crud<Vehicule> crud = new InMemoryCrud<>();
    Vehicule v = new Vehicule(1L, "DK", "Toyota", 2015, 100000);
    crud.create(v);
    assertEquals(1, crud.findAll().size());
}

// Test Optional
@Test
public void testReadOptAbsent() {
    Crud<Vehicule> crud = new InMemoryCrud<>();
    assertTrue(crud.readOpt(999L).isEmpty());
}

// Test Location
@Test
public void testDureeJours() {
    Location loc = new Location(1L, vehicule, conducteur,
        LocalDate.of(2025, 1, 1), 5000);
    loc.terminer(LocalDate.of(2025, 1, 10));
    assertEquals(9, loc.dureeJours());
}
```
