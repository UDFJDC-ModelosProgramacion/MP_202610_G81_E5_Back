package co.edu.udistrital.mdp.pets.entities;
import jakarta.persistence.*;
import uk.co.jemos.podam.common.PodamExclude;
import lombok.Data;


public class AdopterEntity extends BaseEntity {
    private String housingType;
    private Boolean hasOtherPets;
    
    @OneToMany
    @PodamExclude
    private adoptionProcessEntity adoptionProcess;

    @OneToMany
    @PodamExclude
    private adoptionRequestEntity adoptionRequest;

    @OneToMany
    @PodamExclude
    private PetEntity pet;
}
