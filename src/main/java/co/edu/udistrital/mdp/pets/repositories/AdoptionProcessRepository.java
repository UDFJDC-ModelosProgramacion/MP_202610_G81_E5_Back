package co.edu.udistrital.mdp.pets.repositories;
import org.springframework.data.jpa.repository.JpaRepository;

import co.edu.udistrital.mdp.pets.entities.AdoptionProcessEntity;

public interface AdoptionProcessRepository extends JpaRepository<AdoptionProcessEntity, Long> {

}
