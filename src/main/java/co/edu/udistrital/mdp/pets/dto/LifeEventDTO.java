package co.edu.udistrital.mdp.pets.dto;
import java.time.LocalDate;
import lombok.Data;

@Data

public class LifeEventDTO {
    private Long id;
    private String description;
    private LocalDate date;
    private PetDTO pet;
    private TypeLEDTO type;
    private VeterinarianDTO veterinarian;
}