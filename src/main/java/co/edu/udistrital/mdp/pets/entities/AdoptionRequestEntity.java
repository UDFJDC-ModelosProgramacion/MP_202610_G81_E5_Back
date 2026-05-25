package co.edu.udistrital.mdp.pets.entities;

import java.time.LocalDate;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import lombok.Data;
import uk.co.jemos.podam.common.PodamExclude;

@Data
@Entity
public class AdoptionRequestEntity extends BaseEntity {

    private LocalDate requestDate;

    @PodamExclude
    @ManyToOne
    @JoinColumn(name="adopter_id")
    @JsonBackReference("adopter-requests")
    private AdopterEntity adopter;

    @PodamExclude
    @OneToOne(mappedBy="request")
    @JsonIgnore
    private AdoptionProcessEntity adoptionProcess;

    @PodamExclude
    @ManyToOne
    @JoinColumn(name="pet_id")
    @JsonBackReference("pet-requests")
    private PetEntity pet;

    private String purpose;
    private String papers;
    private String status;
}
