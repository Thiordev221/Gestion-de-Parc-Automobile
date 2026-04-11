package sn.abdoulayeThior.l2gl.app.model;

/**
 * Record immuable pour rapport de véhicules.
 * Étape 14/15 : DTO moderne adapté au reporting.
 * 
 * @param immatriculation Immatriculation du véhicule
 * @param marque Marque du véhicule
 * @param etat État actuel du véhicule
 * @param kilometrage Kilométrage actuel
 */
public record LigneRapport(
    String immatriculation,
    String marque,
    EtatVehicule etat,
    int kilometrage
) {}
