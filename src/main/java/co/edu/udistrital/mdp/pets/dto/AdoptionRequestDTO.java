package co.edu.udistrital.mdp.pets.dto;

import java.time.LocalDate;

import lombok.Data;

@Data

public class AdoptionRequestDTO {

    private Long id;

    private LocalDate requestDate;

    private String purpose;

    private String papers;

    private String status;

    private AdopterDTO adopter;

    private AdoptionProcessDTO adoptionProcess;

    private PetDTO pet;

}
