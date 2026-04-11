package sn.abdoulayeThior.l2gl.app.repo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

import sn.abdoulayeThior.l2gl.app.model.Identifiable;

/**
 * Implémentation générique du CRUD avec stockage in-memory (Map).
 * Étape 12/15 : Stockage par clé id, validations strictes.
 * 
 * @param <T> Type d'entité géré
 */
public class InMemoryCrud<T extends Identifiable> implements Crud<T> {

    private final Map<Long, T> storage = new HashMap<>();

    @Override
    public void create(T entite) {
        Objects.requireNonNull(entite, "L'entité ne peut pas être null");
        Long id = entite.getId();
        
        if (storage.containsKey(id)) {
            throw new IllegalArgumentException("L'id " + id + " existe déjà");
        }
        
        storage.put(id, entite);
    }

    @Override
    public T read(Long id) {
        return storage.get(id);
    }

    @Override
    public Optional<T> readOpt(Long id) {
        return Optional.ofNullable(storage.get(id));
    }

    @Override
    public void update(T entite) {
        Objects.requireNonNull(entite, "L'entité ne peut pas être null");
        Long id = entite.getId();
        
        if (!storage.containsKey(id)) {
            throw new IllegalArgumentException("L'id " + id + " n'existe pas");
        }
        
        storage.put(id, entite);
    }

    @Override
    public void delete(Long id) {
        if (!storage.containsKey(id)) {
            throw new IllegalArgumentException("L'id " + id + " n'existe pas");
        }
        
        storage.remove(id);
    }

    @Override
    public List<T> findAll() {
        return new ArrayList<>(storage.values());
    }
}
