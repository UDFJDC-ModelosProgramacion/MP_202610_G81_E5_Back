package co.edu.udistrital.mdp.pets.entities;
import java.time.LocalDate;
import jakarta.persistence.Entity;
import lombok.Data;

@Data
@Entity
public class MedicalRecordEntity extends BaseEntity {

    private String vaccinations;
    private LocalDate upcomingDates;
}