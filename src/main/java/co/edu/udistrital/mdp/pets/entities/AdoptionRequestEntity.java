package co.edu.udistrital.mdp.pets.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.OneToOne;
import lombok.Data;
import uk.co.jemos.podam.common.PodamExclude;

@Data
@Entity
public class AdoptionRequestEntity extends BaseEntity {
    
    @PodamExclude
    @OneToOne(mappedBy="Adopter")
    
    private String idPet;
    private String idAdopter;
    private String purpose;
    private String papers;

}
