package sn.abdoulayeThior.l2gl.app.repo;

import java.util.List;
import java.util.Optional;

import sn.abdoulayeThior.l2gl.app.model.Identifiable;

/**
 * Interface générique pour les opérations CRUD.
 * Étape 12/15 : Définit les contrats de stockage et recherche.
 * 
 * @param <T> Type d'entité géré, doit implémenter Identifiable
 */
public interface Crud<T extends Identifiable> {

    /**
     * Crée une nouvelle entité si l'id n'existe pas déjà.
     * 
     * @param entite Entité à créer
     * @throws IllegalArgumentException si l'id existe déjà
     */
    void create(T entite);

    /**
     * Récupère une entité par son id (null si inexistant).
     * Éviter ce mode - préférer readOpt().
     * 
     * @param id Identifiant unique
     * @return L'entité ou null
     */
    T read(Long id);

    /**
     * Récupère une entité par son id de manière sûre.
     * Étape 13/15 : Évite les nulls et force le traitement de l'absence.
     * 
     * @param id Identifiant unique
     * @return Optional contenant l'entité si elle existe
     */
    Optional<T> readOpt(Long id);

    /**
     * Met à jour une entité existante.
     * 
     * @param entite Entité avec l'id existant
     * @throws IllegalArgumentException si l'id n'existe pas
     */
    void update(T entite);

    /**
     * Supprime une entité par son id.
     * 
     * @param id Identifiant à supprimer
     * @throws IllegalArgumentException si l'id n'existe pas
     */
    void delete(Long id);

    /**
     * Récupère toutes les entités.
     * 
     * @return Liste immuable des entités
     */
    List<T> findAll();
}
