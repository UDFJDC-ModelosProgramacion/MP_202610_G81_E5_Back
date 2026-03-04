package co.edu.udistrital.mdp.pets.entities;

import java.time.LocalDate;

import jakarta.persistence.Entity;
import lombok.Data;

@Data
@Entity
public class AdoptionProcess extends BaseEntity {

    private AdoptionRequest request;
    private LocalDate requestDate;
    private String status;

}
