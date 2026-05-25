package co.edu.udistrital.mdp.pets.entities;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;

import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import lombok.Data;
import uk.co.jemos.podam.common.PodamExclude;

@Data
@Entity
public class AdopterEntity extends BaseEntity {
    private String housingType;
    private Boolean hasOtherPets;
    private String email;
    private String password;

    @PodamExclude
    @OneToMany(mappedBy = "adopter")
    @JsonIgnore
    private List<AdoptionProcessEntity> adoptionProcess = new ArrayList<>();

    @PodamExclude
    @OneToMany(mappedBy = "adopter")
    @JsonIgnore
    private List<AdoptionRequestEntity> adoptionRequest = new ArrayList<>();

    @PodamExclude
    @OneToMany
    @JsonManagedReference("adopter-pets")
    private List<PetEntity> pet = new ArrayList<>();
}