package co.edu.udistrital.mdp.pets.entities;
import jakarta.persistence.*;
import uk.co.jemos.podam.common.PodamExclude;
import lombok.Data;


public class AdopterEntity extends BaseEntity {
    private String housingType;
    private Boolean hasOtherPets;
    
    @OneToMany
    private List<AdoptionProcess> adoptionProcess;

    @OneToMany
    private List<AdoptionRequest> adoptionRequest;

    @OneToMany
    private List<Pet> pet;
}
