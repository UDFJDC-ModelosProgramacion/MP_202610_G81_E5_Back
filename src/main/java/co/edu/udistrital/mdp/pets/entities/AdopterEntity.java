package co.edu.udistrital.mdp.pets.entities;
import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import lombok.Data;
import uk.co.jemos.podam.common.PodamExclude;

@Data
@Entity

public class AdopterEntity extends BaseEntity {
    private String housingType;
    private Boolean hasOtherPets;
    @PodamExclude
    @OneToMany
    private List<AdoptionProcessEntity> adoptionProcess=new ArrayList <>();

    @PodamExclude
    @OneToMany
    private List<AdoptionRequestEntity> adoptionRequest=new ArrayList <>();

    @PodamExclude
    @OneToMany
    private List<PetEntity> pet= new ArrayList <>();
}
