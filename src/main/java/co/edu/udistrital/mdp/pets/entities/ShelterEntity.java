package co.edu.udistrital.mdp.pets.entities;
import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import lombok.Data;
import uk.co.jemos.podam.common.PodamExclude;

@Data
@Entity
public class ShelterEntity extends BaseEntity {
    private String shelterName;
    private String city;
    private String location;

    @ElementCollection
    @OneToMany(mappedBy = "shelter", cascade = CascadeType.ALL, orphanRemoval = true) 
    @PodamExclude
    private List<EventEntity> event = new ArrayList<>();

    @OneToMany(mappedBy = "shelter", cascade = CascadeType.ALL, orphanRemoval = true)
    @PodamExclude
    private List<VeterinarianEntity> veterinarian = new ArrayList<>();

    @OneToMany(mappedBy = "shelter", cascade = CascadeType.ALL, orphanRemoval = true)
    @PodamExclude
    private List<PetEntity> pets = new ArrayList<>();
}