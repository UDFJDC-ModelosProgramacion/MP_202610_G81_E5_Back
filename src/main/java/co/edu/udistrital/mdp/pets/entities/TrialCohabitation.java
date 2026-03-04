package co.edu.udistrital.mdp.pets.entities;

import java.time.LocalDate;

import jakarta.persistence.Entity;
import lombok.Data;

@Data
@Entity
public class TrialCohabitation extends BaseEntity {

    private LocalDate trialStarDate;
    private LocalDate trialEndDate;
    private String status;


}
