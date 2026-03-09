package co.edu.udistrital.mdp.pets.entities;
import jakarta.persistence.*;
import uk.co.jemos.podam.common.PodamExclude;
import lombok.Data;


public class AdopterEntity {
    private String housingType;
    private Boolean hasOtherPets;
    
    @ManyToOne
    @JoinColumn(name = "adoptionProcess_id")
    @PodamExclude
    private adoptionProcessEntity adoptionProcess;

    @ManyToOne
    @JoinColumn(name = "adoptionRequest_id")
    @PodamExclude
    private adoptionRequestEntity adoptionRequest;

    @ManyToOne
    @JoinColumn(name = "pet_id")
    @PodamExclude
    private PetEntity pet;
}
