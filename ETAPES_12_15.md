# GesVehicule - Étapes 12-15 : CRUD, Optional, Record, java.time

## 📋 Résumé des étapes implémentées

### Étape 12/15 — CRUD\<T extends Identifiable\>

**Objectif:** Génériques + stockage in-memory.

**Livrable:**
- Interface `Crud<T extends Identifiable>` avec 5 opérations :
  - `create(T)` - Crée avec validation d'id existant
  - `read(Long id)` - Récupère (déconseillé, peut retourner null)
  - `update(T)` - Met à jour avec validation d'id existant
  - `delete(Long id)` - Supprime avec validation
  - `findAll()` - Récupère tout

- Implémentation `InMemoryCrud<T extends Identifiable>` :
  - Stockage via `Map<Long, T>`
  - Validations strictes (id dupliqué, absent, etc.)

**Avantage:** Réutilisabilité du même code pour Vehicule, Conducteur, Entretien, Location.

---

### Étape 13/15 — Optional : version sûre de read()

**Objectif:** Éviter les null.

**Livrable:**
- Ajout de `Optional<T> readOpt(Long id)` à l'interface Crud
- Service avec 3 patterns d'usage :
  
  ```java
  // 1. orElse() : défaut si absent
  String marque = crudVehicule.readOpt(id)
      .map(Vehicule::getMarque)
      .orElse("Inconnue");
  
  // 2. ifPresent() : action si présent
  crudVehicule.readOpt(id)
      .ifPresent(v -> System.out.println(v.afficher()));
  
  // 3. orElseThrow() : exception si absent
  Location loc = crudLocation.readOpt(id)
      .orElseThrow(() -> new IllegalArgumentException("Inexistant"));
  ```

**Avantage:** Force le développeur à traiter l'absence explicitement (meilleur que les nulls).

---

### Étape 14/15 — record : DTO immuable pour rapport

**Objectif:** Produire un DTO moderne pour reporting.

**Livrable:**
```java
record LigneRapport(
    String immatriculation,
    String marque,
    EtatVehicule etat,
    int kilometrage
) {}
```

- Service avec `genererRapport()` utilisant Stream et map() :
  ```java
  return crudVehicule.findAll().stream()
      .map(v -> new LigneRapport(...))
      .collect(Collectors.toList());
  ```

**Avantage:** Immuabilité, concision, pas de getter/setter, idéal pour les DTOs.

---

### Étape 15/15 — java.time + scénario final intégral

**Objectif:** Finaliser Location/durée/états via lambdas.

**Livrable dans Location :**
- `dureeJours()` : Calcule via `ChronoUnit.DAYS.between()`
- `terminer(LocalDate fin)` : Valide que fin >= début
- `dateFin` : `Optional<LocalDate>`

**Livrable dans Service :**
- `demarrerLocation()` : Crée location + change état véhicule
- `terminerLocation()` : Met à jour location + restaure état
- `trouverAReviser()` : Applique une règle lambda
  ```java
  MyPredicate<Vehicule> regle = v -> 
      v.getKilometrage() > 150000 || v.getAnneeFabrication() < 2012;
  
  List<Vehicule> aReviser = service.trouverAReviser(regle);
  ```

**Affichage polymorphe :**
```java
List<Entite> entites = new ArrayList<>();
entites.add(vehicule);
entites.add(conducteur);
entites.add(location);

entites.forEach(e -> System.out.println(e.afficher()));
// Chaque classe implémente afficher() avec @Override
```

---

## ✅ Réponses aux questions pédagogiques

### Q1 : Pourquoi afficher() est abstraite au lieu d'être générique ?

**R:** Parce que chaque entité a sa propre représentation métier distinct :
- `Vehicule` affiche immat, marque, km
- `Conducteur` affiche nom, permis
- `Location` affiche véhicule + conducteur + dates

C'est du **polymorphisme** : on ne peut pas définir une représentation générique.

---

### Q2 : Que permet le CRUD générique qu'un repo spécifique ne permet pas ?

**R:** La **réutilisabilité**. Avec `Crud<T>`, un seul code fonctionne pour :
- `Crud<Vehicule>`
- `Crud<Conducteur>`
- `Crud<Entretien>`
- `Crud<Location>`

Au lieu d'écrire 4 repos différents = **économie de code** + **type-safety**.

---

### Q3 : En quoi Optional "force" le développeur à traiter le cas absent ?

**R:** `Optional<T>` oblige à **gérer explicitement** l'absence :
- Pas de `if (obj != null)` silencieux
- Doit appeler `orElse()`, `ifPresent()`, ou `orElseThrow()`
- **Compile** : force la réflexion sur les cas limites
- Résultat : **moins de nullPointerException** en production

---

### Q4 : Pourquoi un record est adapté à un "objet de rapport" ?

**R:** Un record est :
1. **Immuable** : pas de modification après création (données stables)
2. **Concis** : autogenere equals, hashCode, toString, getters
3. **Lisible** : déclare clairement les champs pertinents
4. **Pas de logique métier** : juste du transport de données

= **DTO parfait** pour du reporting.

---

### Q5 : Quel choix rend le projet le plus "solide" ?

**R:** La **combinaison** de plusieurs concepts :

| Choix | Bénéfice |
|-------|----------|
| **Enums** | États strictement définis (DISPONIBLE, EN_LOCATION, etc.) |
| **Génériques** | Réutilisabilité + type-safety (Crud<T>) |
| **Optional** | Pas de nulls, traitement explicite |
| **Records** | DTOs immuables et concis |
| **Lambdas** | Règles métier flexibles et testables |
| **java.time** | Dates sûres et fiables |

**Exemple concret :** 
```java
// Avant : erreur possible
Location loc = repo.read(id); // null ?
if (loc != null) { ... }

// Après : garanti de traiter l'absence
Location loc = repo.readOpt(id)
    .orElseThrow(); // Impossible d'oublier
```

---

## 🚀 Comment exécuter

```bash
cd "c:\Users\Abdoulaye_Thior\Documents\L2 G3\Java\GesVehicule"
mvn clean compile
java -cp target/classes sn.abdoulayeThior.l2gl.app.app.Main
```

## 📊 Structure finale

```
src/main/java
├── app/
│   ├── Main.java (scénario 12-15)
│   ├── model/
│   │   ├── Entite.java (id final + afficher() abstract)
│   │   ├── Identifiable.java
│   │   ├── Vehicule.java (@Override afficher)
│   │   ├── Conducteur.java (@Override afficher)
│   │   ├── Entretien.java (@Override afficher)
│   │   ├── Location.java (Optional, LocalDate, @Override afficher)
│   │   ├── EtatVehicule.java (enum)
│   │   ├── LigneRapport.java (record)
│   │   └── ...
│   ├── repo/
│   │   ├── Crud.java (interface générique)
│   │   └── InMemoryCrud.java (Map<Long, T>)
│   └── service/
│       └── ParcAutoService.java (CRUD + métier + Optional)
```

---

## 🎓 Conclusion : La solidité vient de la composition

Le projet démontre qu'une architecture **solide** n'est pas un seul choix, mais une **orchestration consciente** :
- Enums pour les états → pas d'erreur de typage
- Génériques pour le CRUD → réutilisable
- Optional pour les lectures → pas de null surprises
- Records pour les DTOs → immuabilité
- Lambdas pour les règles → flexibilité métier
- java.time pour les dates → évite les bugs de date

**Ensemble** = code prévisible, testable, et maintenable. ✅
