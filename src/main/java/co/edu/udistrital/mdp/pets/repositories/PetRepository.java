package co.edu.udistrital.mdp.pets.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import co.edu.udistrital.mdp.pets.entities.Pet;

public interface PetRepository extends JpaRepository<Pet, Long> {
    
}
