package co.edu.udistrital.mdp.pets.entities;
import jakarta.persistence.*;
import uk.co.jemos.podam.common.PodamExclude;
import lombok.Data;
import java.util.ArrayList;
import java.util.List;


public class AdopterEntity extends BaseEntity {
    private String housingType;
    private Boolean hasOtherPets;
    
    @OneToMany
    private List<AdoptionProcessEntity> adoptionProcess=new ArrayList <>();

    @OneToMany
    private List<AdoptionRequestEntity> adoptionRequest=new ArrayList <>();
    @OneToMany
    private List<PetEntity> pet= new ArrayList <>();
}
