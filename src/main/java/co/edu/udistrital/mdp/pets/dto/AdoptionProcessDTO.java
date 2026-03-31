package co.edu.udistrital.mdp.pets.dto;

import java.time.LocalDate;

import lombok.Data;

@Data

public class AdoptionProcessDTO {

    private Long id;

    private LocalDate requestDate;

    private String status; // en proceso, aprobado, rechazado, cerrado

    private AdopterDTO adopter;

    private PetDTO pet;

    private AdoptionRequestDTO request;

    private VeterinarianDTO veterinarian;

}