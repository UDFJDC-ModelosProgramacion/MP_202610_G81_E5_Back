package co.edu.udistrital.mdp.pets.entities;

import java.time.LocalDate;

import jakarta.persistence.Entity;
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
    private AdoptionRequestEntity request;
    private LocalDate requestDate;
    private String status;


}
