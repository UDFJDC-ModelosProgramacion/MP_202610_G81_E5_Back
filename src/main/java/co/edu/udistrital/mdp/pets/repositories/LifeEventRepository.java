package co.edu.udistrital.mdp.pets.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import co.edu.udistrital.mdp.pets.entities.LifeEvent;

public interface LifeEventRepository extends JpaRepository<LifeEvent, Long> {
}