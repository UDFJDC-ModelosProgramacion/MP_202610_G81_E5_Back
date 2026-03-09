package co.edu.udistrital.mdp.pets.entities;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Data;
import java.time.LocalDate;

@Data
@Entity
public class LifeEventEntity extends BaseEntity {

    private String description;
    private LocalDate date;

    @ManyToOne
    @JoinColumn(name = "pet_id")
    private PetEntity pet;

    @ManyToOne
    @JoinColumn(name = "type_id")
    private TypeLEEntity type;

    @ManyToOne
    @JoinColumn(name = "veterinarian_id")
    private VeterinarianEntity veterinarian;
}