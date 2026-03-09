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
    @JoinColumn(name = "event_id")
    @PodamExclude
    private EventEntity event;


    @ManyToOne
    @JoinColumn(name = "vet_id")
    @PodamExclude
    private VetEntity vet;
}
 