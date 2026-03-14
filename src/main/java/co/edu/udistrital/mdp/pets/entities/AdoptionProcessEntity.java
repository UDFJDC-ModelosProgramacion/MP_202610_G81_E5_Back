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
public class AdoptionProcessEntity extends BaseEntity {
    
    @PodamExclude
    @ManyToOne 
    @JoinColumn(name = "adopter_id")
    private AdopterEntity adopter;

    @PodamExclude
    @ManyToOne
    @JoinColumn(name = "pet_id")
    private PetEntity pet;

    @PodamExclude
    @OneToOne
    private AdoptionRequestEntity request;

    @PodamExclude
    @ManyToOne
    @JoinColumn(name = "veterinarian_id")
    private VeterinarianEntity veterinarian;

    private LocalDate requestDate;
    private String status;


}
