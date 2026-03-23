package co.edu.udistrital.mdp.pets.repositories;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import co.edu.udistrital.mdp.pets.entities.AdoptionProcessEntity;
import co.edu.udistrital.mdp.pets.entities.TrialCohabitationEntity;
import org.springframework.stereotype.Repository;

@Repository
public interface TrialCohabitationRepository extends JpaRepository<TrialCohabitationEntity, Long> {

    Optional<TrialCohabitationEntity> findByAdoptionProcess(AdoptionProcessEntity adoptionProcess);
}
