package co.edu.udistrital.mdp.pets.repositories;
import org.springframework.data.jpa.repository.JpaRepository;
import co.edu.udistrital.mdp.pets.entities.AdopterEntity;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface AdopterRepository extends JpaRepository<AdopterEntity, Long> {
    Optional<AdopterEntity> findByEmail(String email);
}
