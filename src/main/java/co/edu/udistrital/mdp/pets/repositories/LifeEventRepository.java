package co.edu.udistrital.mdp.pets.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import co.edu.udistrital.mdp.pets.entities.LifeEventEntity;

public interface LifeEventRepository extends JpaRepository<LifeEventEntity, Long> {
}