package co.edu.udistrital.mdp.pets.repositories;
import org.springframework.data.jpa.repository.JpaRepository;

import co.edu.udistrital.mdp.pets.entities.AdoptionRequestEntity;

public interface AdoptionRequestRepository extends JpaRepository<AdoptionRequestEntity, Long> {

}
