package co.edu.udistrital.mdp.pets.entities;
import java.time.LocalDate;
import jakarta.persistence.Entity;
import lombok.Data;

@Data
@Entity
public class MedicalRecord extends BaseEntity {

    private String vaccinations;
    private LocalDate upcomingDates;
}