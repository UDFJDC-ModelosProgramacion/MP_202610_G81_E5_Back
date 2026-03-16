package co.edu.udistrital.mdp.pets.repositories;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import co.edu.udistrital.mdp.pets.entities.AdoptionProcessEntity;
import co.edu.udistrital.mdp.pets.entities.VeterinarianEntity;

@Repository
public interface AdoptionProcessRepository extends JpaRepository<AdoptionProcessEntity, Long> {

    Optional<AdoptionProcessEntity> findByVeterinarianId(Long veterinarianId);
    boolean existsByVeterinarian(VeterinarianEntity veterinarian);
}
