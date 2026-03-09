package co.edu.udistrital.mdp.pets.entities;

import java.time.LocalDate;

import jakarta.persistence.Entity;
import jakarta.persistence.OneToOne;
import lombok.Data;
import uk.co.jemos.podam.common.PodamExclude;

@Data
@Entity
public class TrialCohabitationEntity extends BaseEntity {
    
    @PodamExclude
    @OneToOne
    private VeterinarianEntity veterinarian;
    private LocalDate trialStarDate;
    private LocalDate trialEndDate;
    private String status;
}
