package co.edu.udistrital.mdp.pets.entities;

import java.time.LocalDate;

import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import lombok.Data;
import uk.co.jemos.podam.common.PodamExclude;

@Data
@Entity
public class TrialCohabitationEntity extends BaseEntity {
    
    @PodamExclude
    @ManyToOne
    @JoinColumn(name = "veterinarian_id")
    private VeterinarianEntity veterinarian;

    @PodamExclude
    @OneToOne
    private AdoptionProcessEntity adoptionProcess;

    private LocalDate starDate;
    private LocalDate endDate;
    private String status;
}
