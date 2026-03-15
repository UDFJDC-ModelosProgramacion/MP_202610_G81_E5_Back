package co.edu.udistrital.mdp.pets.repositories;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import co.edu.udistrital.mdp.pets.entities.AdopterEntity;
import co.edu.udistrital.mdp.pets.entities.AdoptionRequestEntity;

@Repository
public interface AdoptionRequestRepository extends JpaRepository<AdoptionRequestEntity, Long> {

    List<AdopterEntity> findByAdopterId(Long adopterid);
    boolean existsByAdoptionProcessId(Long adoptionProcessId);
}
