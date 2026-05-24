package co.edu.udistrital.mdp.pets.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import co.edu.udistrital.mdp.pets.entities.LifeEventEntity;
import org.springframework.stereotype.Repository;

@Repository
public interface LifeEventRepository extends JpaRepository<LifeEventEntity, Long> {
    boolean existsByTypeId(Long typeId);
    List<LifeEventEntity> findByPetShelterId(Long shelterId);
}