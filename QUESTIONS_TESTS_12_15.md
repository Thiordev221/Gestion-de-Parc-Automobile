# Questions de révision et tests - Étapes 12-15

## 📝 Questions de compréhension

### Étape 12 : CRUD Générique

**Q1.1 :** Pourquoi utiliser `Crud<T extends Identifiable>` plutôt que 3 interfaces distinctes `CrudVehicule`, `CrudConducteur`, `CrudEntretien` ?
- A) Réutilisabilité : même code pour tout type d'entité
- B) Type-safety : le compilateur vérifie que T implémente Identifiable
- C) Maintenabilité : un seul endroit à modifier pour tous les types
- **✅ Réponse : A, B, C (tous les trois)**

**Q1.2 :** Qu'est-ce qui se passe si on essaie de créer deux Vehicules avec le même id ?
```java
crud.create(new Vehicule(1L, "DK-123", ...));
crud.create(new Vehicule(1L, "SL-456", ...)); // ❌
```
- A) Silencieusement écrasé le premier
- B) Lance IllegalArgumentException
- **✅ Réponse : B** (validation dans `create()`)

**Q1.3 :** Quelle est la complexité de `findAll()` avec une HashMap ?
- A) O(1)
- B) O(n)
- **✅ Réponse : B** (doit itérer sur tous les éléments)

---

### Étape 13 : Optional

**Q2.1 :** Ordonnez ces patterns par ordre de "sécurité" (du moins sûr au plus sûr) :
```
1. T value = crud.read(id);        // null possible
2. Optional<T> opt = crud.readOpt(id);
   if (opt.isPresent()) { ... }    // gestion explicite
3. crud.readOpt(id)
   .ifPresent(v -> { ... });       // gestion élégante
4. crud.readOpt(id).orElseThrow();  // exception si absent
```
- **✅ Réponse : 1 (moins sûr) → 2 → 3 → 4 (plus sûr)**

**Q2.2 :** Quel est l'avantage de `readOpt()` par rapport à `read()` ?
- A) Plus rapide
- B) Force le développeur à explicitement traiter l'absence
- C) Évite les NPE (NullPointerException)
- **✅ Réponse : B et C**

**Q2.3 :** Code : Quel est le résultat ?
```java
String result = crud.readOpt(999L)
    .map(Vehicule::getMarque)
    .orElse("Marque inconnue");
System.out.println(result);
```
- A) NullPointerException
- B) "Marque inconnue"
- **✅ Réponse : B** (le id 999 n'existe pas)

**Q2.4 :** Différence entre `.ifPresent()` et `.ifPresentOrElse()` ?
- A) Pas de différence
- B) ifPresentOrElse permet une action "else"
- **✅ Réponse : B**

---

### Étape 14 : Record

**Q3.1 :** Qu'est-ce qui est autogénéré by a record ?
```java
public record LigneRapport(String immat, int km) {}
```
- A) Constructor(String immat, int km)
- B) immat() et km() getters
- C) equals(), hashCode(), toString()
- **✅ Réponse : A, B, C (tout)**

**Q3.2 :** Quelles affirmations sont vraies sur les records ?
- A) Ils sont mutables
- B) Ils sont immuables
- C) Les champs sont final
- D) Pas de setters autogénérés
- **✅ Réponse : B, C, D**

**Q3.3 :** Pourquoi préférer un record pour un DTO plutôt qu'une classe ?
```java
// Record
record LigneRapport(String immat, EtatVehicule etat, int km) {}

// vs Classe
public class LigneRapport {
    private final String immat;
    private final EtatVehicule etat;
    private final int km;
    // constructor, getters, equals, hashCode, toString...
}
```
- **✅ Réponse :** Le record évite ~15 lignes de code passe-partout

**Q3.4 :** Accès aux champs d'un record :
```java
LigneRapport ligne = new LigneRapport("DK-123", DISPONIBLE, 100000);
System.out.println(ligne.???); // Comment accéder à DISPONIBLE ?
```
- A) ligne.getEtat()
- B) ligne.etat
- C) ligne.etat()
- **✅ Réponse : C** (getter sans "get")

---

### Étape 15 : Scénario intégral

**Q4.1 :** Qu'est-ce qui se passe lors du `demarrerLocation()` ?
```java
service.demarrerLocation(location);
```
- A) Crée la location
- B) Change state du véhicule à EN_LOCATION
- C) Change state du conducteur
- **✅ Réponse : A et B** (le conducteur ne change pas d'état)

**Q4.2 :** Calcul de durée. Qu'affiche ce code ?
```java
Location loc = new Location(1L, v, c, 
    LocalDate.of(2025, 1, 1), 5000);
loc.terminer(LocalDate.of(2025, 1, 10));
System.out.println(loc.dureeJours());
```
- A) 8
- B) 9
- C) 10
- **✅ Réponse : B** (9 DAYS.between(1er, 10) = 9)

**Q4.3 :** Code à analyser. Qu'est-ce qui manque ?
```java
Location loc = service.terminerLocation(idInexistant, fin);
```
- A) Rien, ça marche
- B) L'exception n'est pas attrapée
- C) La méthode lance orElseThrow()
- **✅ Réponse : B et C** (danger!)

**Q4.4 :** Polymorphisme. Qu'affiche ce code ?
```java
List<Entite> entites = Arrays.asList(
    new Vehicule(...),
    new Location(...),
    new Conducteur(...)
);

entites.forEach(e -> System.out.println(e.afficher()));
```
- A) 3 fois le même affichage
- B) 3 affichages différents (@Override)
- **✅ Réponse : B** (chaque classe a son afficher())

**Q4.5 :** Règles lambda. Quelle règle trouve les véhicules avec km > 200000 ?
```java
MyPredicate<Vehicule> regle = v -> ???;
```
- A) `v -> v.getKilometrage() > 200000`
- B) `v -> { return v.getKilometrage() > 200000; }`
- C) Les deux
- **✅ Réponse : A (syntaxe préférée)**

---

## 🧪 Exercices pratiques

### Exercice 1 : Étendre le CRUD

**Énoncé :** Ajouter une méthode `exists(Long id)` à l'interface Crud et implémenter dans InMemoryCrud.

```java
// Interface
public interface Crud<T extends Identifiable> {
    boolean exists(Long id);
}

// Implémentation
@Override
public boolean exists(Long id) {
    return storage.containsKey(id);
}
```

**Test :**
```java
crud.exists(1L);   // true si créé
crud.exists(999L); // false
```

---

### Exercice 2 : Ajouter un filtre au rapport

**Énoncé :** Créer une méthode `genererRapport(EtatVehicule etat)` qui filtre par état.

```java
public List<LigneRapport> genererRapport(EtatVehicule etat) {
    return crudVehicule.findAll().stream()
        .filter(v -> v.getEtat() == etat)
        .map(v -> new LigneRapport(...))
        .collect(Collectors.toList());
}
```

**Test :**
```java
List<LigneRapport> disponibles = service.genererRapport(DISPONIBLE);
List<LigneRapport> enlocation = service.genererRapport(EN_LOCATION);
```

---

### Exercice 3 : Gérer le cas où on termine une location inexistante

**Énoncé :** Adapter le code pour retourner `Optional<Location>` au lieu de lever une exception.

```java
public Optional<Location> terminerLocation(Long locationId, LocalDate fin) {
    return crudLocation.readOpt(locationId)
        .map(loc -> {
            loc.terminer(fin);
            crudLocation.update(loc);
            return loc;
        });
}

// Utilisation
service.terminerLocation(idInexistant, fin)
    .ifPresentOrElse(
        loc -> System.out.println("Terminée"),
        () -> System.err.println("Location introuvable")
    );
```

---

### Exercice 4 : Créer un record pour statistiques

**Énoncé :** Créer un record `StatVehicule(int total, int kmTotal, long enLocation)`.

```java
public record StatVehicule(int total, int kmTotal, long enLocation) {}

public StatVehicule calculerStatistiques() {
    List<Vehicule> v = crudVehicule.findAll();
    int total = v.size();
    int kmTotal = v.stream().mapToInt(Vehicule::getKilometrage).sum();
    long enLocation = v.stream().filter(x -> x.getEtat() == EN_LOCATION).count();
    return new StatVehicule(total, kmTotal, enLocation);
}
```

**Test :**
```java
StatVehicule stats = service.calculerStatistiques();
System.out.println("Total: " + stats.total() + ", km: " + stats.kmTotal());
```

---

## 🔍 Cas limites (Edge cases)

**Q5.1 :** Qu'se passe-t-il si on crée une Location avec un conducteur inexistant ?
```java
Vehicule v = ...; // existe
Conducteur c = ...; // n'existe nulle part dans la BD
Location loc = new Location(1L, v, c, LocalDate.now(), 5000);
```
- **✅ Réponse :** Le code compile et crée la Location (pas de validation). À améliorer dans une vraie application !

**Q5.2 :** Qu'est-ce qu'une "durée négative" ?
```java
Location loc = new Location(..., LocalDate.of(2025, 1, 10), ...);
loc.terminer(LocalDate.of(2025, 1, 1)); // fin < début
```
- **✅ Réponse :** `terminer()` lance une exception (voir le code)

**Q5.3 :** Si on delete un vehicule en location ?
```java
crud.delete(vehiculeId); // Il est EN_LOCATION
```
- **✅ Réponse :** C'est supprimé ! À améliorer : ajouter une validation.

---

## 📊 Résumé : Matrice de validation

| Concept | Répondu ? | Implanté ? | Testé ? |
|---------|-----------|-----------|---------|
| CRUD<T> generic | ✅ | ✅ | ✅ |
| create() validation | ✅ | ✅ | ✅ |
| Optional.readOpt() | ✅ | ✅ | ✅ |
| 3+ patterns Optional | ✅ | ✅ | ✅ |
| record LigneRapport | ✅ | ✅ | ✅ |
| genererRapport() | ✅ | ✅ | ✅ |
| Location.dureeJours() | ✅ | ✅ | ✅ |
| demarrerLocation() | ✅ | ✅ | ✅ |
| afficher() polymorphe | ✅ | ✅ | ✅ |
| Règles lambda | ✅ | ✅ | ✅ |

---

## 🎯 Prochains défis (optionnels)

1. **Validation de contraintes :** Ajouter @Validated pour refuser les conducteurs inexistants
2. **Tri avancé :** Trouver les 3 véhicules avec le plus de km
3. **Rapports groupés :** Grouper le rapport par EtatVehicule
4. **API REST :** Exposer les CRUD via des endpoints /api/vehicules
5. **Tests unitaires :** JUnit pour chaque CRUD et Optional

---

## ✅ Auto-évaluation

- [ ] Je comprends pourquoi Crud<T> est générique
- [ ] Je peux expliquer orElse, ifPresent, orElseThrow
- [ ] Je sais créer un record et l'utiliser
- [ ] Je peux écrire un Stream avec map et filter
- [ ] Je comprends le polymorphisme via afficher()
- [ ] Je peux écrire une règle lambda
- [ ] Je peux dépanner un code qui utilise Optional
- [ ] Je sais comment validation un CRUD

**Si toutes les cases sont cochées : BRAVO ! 🎉**
