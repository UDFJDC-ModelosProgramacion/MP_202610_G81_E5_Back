package co.edu.udistrital.mdp.pets.entities;

import java.time.LocalDate;

import com.fasterxml.jackson.annotation.JsonManagedReference;

import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import lombok.Data;
import uk.co.jemos.podam.common.PodamExclude;

@Data
@Entity
public class AdoptionProcessEntity extends BaseEntity {
    
    private LocalDate requestDate;
    private String status;

    @PodamExclude
    @ManyToOne 
    @JoinColumn(name = "adopter_id")
    @JsonManagedReference("adopter-processes")
    private AdopterEntity adopter;

    @PodamExclude
    @ManyToOne
    @JoinColumn(name = "pet_id")
    @JsonManagedReference("pet-processes")
    private PetEntity pet;

    @PodamExclude
    @OneToOne
    @JoinColumn(name = "request_id")
    @JsonManagedReference("request-process")
    private AdoptionRequestEntity request;

    @PodamExclude
    @ManyToOne
    @JoinColumn(name = "veterinarian_id")
    private VeterinarianEntity veterinarian;

    @PodamExclude
    @OneToOne(mappedBy = "adoptionProcess")
    @JsonManagedReference("process-trial")
    private TrialCohabitationEntity trialCohabitation;
}