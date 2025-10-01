package com.petfriendly.backend.repository;

import com.petfriendly.backend.entity.Pet;
import com.petfriendly.backend.entity.PetSpecies;
import com.petfriendly.backend.entity.PetStatus;
import com.petfriendly.backend.entity.PetSize;
import com.petfriendly.backend.entity.PetGender;
import com.petfriendly.backend.entity.Foundation;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

/**
 * Repository interface for Pet entity operations
 */
@Repository
public interface PetRepository extends JpaRepository<Pet, UUID> {

    /**
     * Find all pets by status
     * @param status the pet status to filter by
     * @return list of pets with the specified status
     */
    /**
     * Find available pets for adoption
     * @return list of available pets
     */
    List<Pet> findByStatus(PetStatus status);

    /**
     * Find available pets for adoption with pagination
     * @param status the pet status
     * @param pageable pagination information
     * @return page of available pets
     */
    Page<Pet> findByStatus(PetStatus status, Pageable pageable);

    /**
     * Find pets by foundation and status
     * @param foundation the foundation
     * @param status the pet status
     * @return list of pets belonging to the foundation with the specified status
     */
    List<Pet> findAllByFoundationAndStatus(Foundation foundation, PetStatus status);

    /**
     * Find all pets by species
     * @param species the pet species to filter by
     * @return list of pets of the specified species
     */
    List<Pet> findBySpecies(PetSpecies species);

    /**
     * Find all pets by species with pagination
     * @param species the pet species to filter by
     * @param pageable pagination information
     * @return page of pets of the specified species
     */
    Page<Pet> findBySpecies(PetSpecies species, Pageable pageable);

    /**
     * Find all pets by breed
     * @param breed the breed to filter by
     * @return list of pets of the specified breed
     */
    List<Pet> findByBreed(String breed);

    /**
     * Find all pets by breed with pagination
     * @param breed the breed to filter by
     * @param pageable pagination information
     * @return page of pets of the specified breed
     */
    Page<Pet> findByBreed(String breed, Pageable pageable);

    /**
     * Find all pets by foundation
     * @param foundation the foundation to filter by
     * @return list of pets belonging to the specified foundation
     */
    List<Pet> findByFoundation(Foundation foundation);

    /**
     * Find all pets by foundation ID
     * @param foundationId the foundation ID to filter by
     * @return list of pets belonging to the specified foundation
     */
    List<Pet> findByFoundationId(UUID foundationId);

    /**
     * Find all pets by foundation with pagination
     * @param foundation the foundation to filter by
     * @param pageable pagination information
     * @return page of pets belonging to the specified foundation
     */
    Page<Pet> findByFoundation(Foundation foundation, Pageable pageable);

    /**
     * Find pets by foundation and status with pagination
     * @param foundation the foundation
     * @param status the pet status
     * @param pageable pagination information
     * @return page of pets belonging to the foundation with the specified status
     */
    Page<Pet> findByFoundationAndStatus(Foundation foundation, PetStatus status, Pageable pageable);

    /**
     * Find pets by status and species
     * @param status the pet status
     * @param species the pet species
     * @return list of pets matching both criteria
     */
    List<Pet> findByStatusAndSpecies(PetStatus status, PetSpecies species);

    /**
     * Find available pets by species with pagination
     * @param species the pet species
     * @param pageable pagination information
     * @return page of available pets of the specified species
     */
    Page<Pet> findByStatusAndSpecies(PetStatus status, PetSpecies species, Pageable pageable);

    /**
     * Find pets by name containing the given text (case-insensitive)
     * @param name the name to search for
     * @return list of pets whose name contains the search text
     */
    @Query("SELECT p FROM Pet p WHERE LOWER(p.name) LIKE LOWER(CONCAT('%', :name, '%'))")
    List<Pet> findByNameContainingIgnoreCase(@Param("name") String name);

    /**
     * Find pets by name containing the given text (case-insensitive) with pagination
     * @param name the name to search for
     * @param pageable pagination information
     * @return page of pets whose name contains the search text
     */
    @Query("SELECT p FROM Pet p WHERE LOWER(p.name) LIKE LOWER(CONCAT('%', :name, '%'))")
    Page<Pet> findByNameContainingIgnoreCase(@Param("name") String name, Pageable pageable);

    /**
     * Find available pets in a specific city
     * @param city the city to search in
     * @return list of available pets in the specified city
     */
    @Query("SELECT p FROM Pet p JOIN p.foundation f WHERE p.status = 'AVAILABLE' AND f.city = :city")
    List<Pet> findAvailablePetsByCity(@Param("city") String city);

    /**
     * Find available pets by multiple criteria
     * @param species the pet species (optional)
     * @param size the pet size (optional)
     * @param gender the pet gender (optional)
     * @param city the city (optional)
     * @param pageable pagination information
     * @return page of pets matching the criteria
     */
    @Query("SELECT p FROM Pet p JOIN p.foundation f WHERE " +
           "p.status = 'AVAILABLE' AND " +
           "(:species IS NULL OR p.species = :species) AND " +
           "(:size IS NULL OR p.size = :size) AND " +
           "(:gender IS NULL OR p.gender = :gender) AND " +
           "(:city IS NULL OR f.city = :city)")
    Page<Pet> findAvailablePetsWithFilters(@Param("species") PetSpecies species,
                                          @Param("size") PetSize size,
                                          @Param("gender") PetGender gender,
                                          @Param("city") String city, 
                                          Pageable pageable);

    /**
     * Count pets by status
     * @param status the pet status
     * @return number of pets with the specified status
     */
    long countByStatus(PetStatus status);

    /**
     * Count pets by foundation
     * @param foundation the foundation
     * @return number of pets belonging to the foundation
     */
    long countByFoundation(Foundation foundation);

    /**
     * Count pets by foundation and status
     * @param foundation the foundation
     * @param status the pet status
     * @return number of pets belonging to the foundation with the specified status
     */
    long countByFoundationAndStatus(Foundation foundation, PetStatus status);

    /**
     * Count pets by species
     * @param species the pet species as string
     * @return number of pets of the specified species
     */
    @Query("SELECT COUNT(p) FROM Pet p WHERE UPPER(p.species) = UPPER(:species)")
    long countBySpecies(@Param("species") String species);

    /**
     * Find pets with images
     * @return list of pets that have at least one image
     */
    @Query("SELECT DISTINCT p FROM Pet p JOIN p.images i")
    List<Pet> findPetsWithImages();

    /**
     * Find recently added pets (available status only)
     * @param pageable pagination information
     * @return page of recently added available pets
     */
    @Query("SELECT p FROM Pet p WHERE p.status = 'AVAILABLE' ORDER BY p.createdAt DESC")
    Page<Pet> findRecentlyAddedAvailablePets(Pageable pageable);

    /**
     * Find pets by age range
     * @param minAge minimum age
     * @param maxAge maximum age
     * @return list of pets within the age range
     */
    List<Pet> findByAgeBetween(Integer minAge, Integer maxAge);

    /**
     * Find pets by age range with pagination
     * @param minAge minimum age
     * @param maxAge maximum age
     * @param pageable pagination information
     * @return page of pets within the age range
     */
    Page<Pet> findByAgeBetween(Integer minAge, Integer maxAge, Pageable pageable);
}