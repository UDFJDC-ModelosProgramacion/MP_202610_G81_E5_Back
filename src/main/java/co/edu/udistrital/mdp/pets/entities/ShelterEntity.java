package co.edu.udistrital.mdp.pets.entities;
import jakarta.persistence.*;
import uk.co.jemos.podam.common.PodamExclude;
import lombok.Data;
import java.util.ArrayList;
import java.util.List;

@Data
@Entity

public class ShelterEntity extends BaseEntity {
    private String shelterName;
    private String city;
    private String location;

    @ElementCollection
    @OneToMany(mappedBy = "shelter", cascade = CascadeType.ALL, orphanRemoval = true) 
    @PodamExclude
    private List<EventEntity> event=new ArrayList <>();

    @OneToMany(mappedBy = "shelter", cascade = CascadeType.ALL, orphanRemoval = true)
    @PodamExclude
    private List<VeterinarianEntity> veterinarian=new ArrayList <>();

    @OneToMany(mappedBy = "shelter", cascade = CascadeType.ALL, orphanRemoval = true)
    @PodamExclude
    private List<PetEntity> pet=new ArrayList <>();
}
 
