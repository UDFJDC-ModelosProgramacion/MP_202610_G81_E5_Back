package co.edu.udistrital.mdp.pets.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import co.edu.udistrital.mdp.pets.entities.TypeLEEntity;
import org.springframework.stereotype.Repository;

@Repository
public interface TypeLERepository extends JpaRepository<TypeLEEntity, Long> {
}