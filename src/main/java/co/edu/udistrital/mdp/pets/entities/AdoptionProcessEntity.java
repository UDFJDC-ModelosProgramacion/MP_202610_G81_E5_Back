package co.edu.udistrital.mdp.pets.entities;

import jakarta.persistence.Entity;

@Data
@Entity
public class AdoptionProcessEntity extends BaseEntity {
    
    private AdoptionRequest request;
    private LocalDate requestDate;
    private String status;


}
