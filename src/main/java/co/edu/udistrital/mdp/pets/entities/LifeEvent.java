package co.edu.udistrital.mdp.pets.entities;
import jakarta.persistence.Entity;
import lombok.Data;
import java.time.LocalDate;

@Data
@Entity
public class LifeEvent extends BaseEntity {

    private String description;
    private LocalDate date;
}