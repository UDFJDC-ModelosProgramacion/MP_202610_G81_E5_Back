package co.edu.udistrital.mdp.pets.entities;
import jakarta.persistence.*;
import uk.co.jemos.podam.common.PodamExclude;
import lombok.Data;

@Data
@Entity

public class ShelterEntity extends BaseEntity {
    private String shelterName;
    private String city;
    private String location;

    @ElementCollection
    @OneToMany(mappedBy = "shelter", cascade = CascadeType.ALL, orphanRemoval = true) 
    @PodamExclude
    private EventEntity event;

    @OneToMany(mappedBy = "shelter", cascade = CascadeType.ALL, orphanRemoval = true)
    @PodamExclude
    private VeterinarianEntity veterinarian;

    @OneToMany(mappedBy = "shelter", cascade = CascadeType.ALL, orphanRemoval = true)
    @PodamExclude
    private PetEntity pet;
}
 
