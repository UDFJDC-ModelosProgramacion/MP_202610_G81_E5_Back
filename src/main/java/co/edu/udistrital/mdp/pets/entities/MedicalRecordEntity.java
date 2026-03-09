package co.edu.udistrital.mdp.pets.entities;
import java.time.LocalDate;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Data;

@Data
@Entity
public class MedicalRecordEntity extends BaseEntity {

    private String vaccinations;
    private LocalDate upcomingDates;

    @ManyToOne
    @JoinColumn(name = "pet_id")
    private PetEntity pet;

}