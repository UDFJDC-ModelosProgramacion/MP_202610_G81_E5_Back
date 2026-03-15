package co.edu.udistrital.mdp.pets.repositories;
import org.springframework.data.jpa.repository.JpaRepository;

import co.edu.udistrital.mdp.pets.entities.AdoptionProcessEntity;
import org.springframework.stereotype.Repository;

@Repository
public interface AdoptionProcessRepository extends JpaRepository<AdoptionProcessEntity, Long> {
    boolean existsByPetIdAndStatus(Long petId, String status);
}
