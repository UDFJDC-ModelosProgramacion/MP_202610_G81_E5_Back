package co.edu.udistrital.mdp.pets.entities;
import java.time.LocalDate;

import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Data;
import uk.co.jemos.podam.common.PodamExclude;

@Data
@Entity
public class MedicalRecordEntity extends BaseEntity {

    private String vaccinations;
    private LocalDate upcomingDates;

    @PodamExclude
    @ManyToOne
    @JoinColumn(name = "pet_id")
    private PetEntity pet;

    @PodamExclude
    @ManyToOne
    @JoinColumn(name = "veterinarian_id")
    private VeterinarianEntity veterinarian;
}