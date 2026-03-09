package co.edu.udistrital.mdp.pets.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.OneToOne;
import lombok.Data;
import uk.co.jemos.podam.common.PodamExclude;

@Data
@Entity
public class AdoptionRequestEntity extends BaseEntity {
    
    @PodamExclude
    @ManyToOne
    @JoinColumn(name = "adopter_id")
    private AdopterEntity adopter;

    private String idPet;
    private String idAdopter;
    private String purpose;
    private String papers;

}
