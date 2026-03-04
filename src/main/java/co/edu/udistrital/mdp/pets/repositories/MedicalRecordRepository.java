package co.edu.udistrital.mdp.pets.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import co.edu.udistrital.mdp.pets.entities.MedicalRecordEntity;

public interface MedicalRecordRepository extends JpaRepository<MedicalRecordEntity, Long> {
}