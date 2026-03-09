package co.edu.udistrital.mdp.pets.entities;
import jakarta.persistence.*;
import uk.co.jemos.podam.common.PodamExclude;
import lombok.Data;
import java.util.Date;

@Data
@Entity

public class EventEntity extends BaseEntity {
    private String name;
    private Date date;
    private String type;

    @ManyToOne
    @JoinColumn(name = "shelter_id")
    @PodamExclude
    private ShelterEntity shelter;
}
