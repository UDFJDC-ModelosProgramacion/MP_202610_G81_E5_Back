package co.edu.udistrital.mdp.pets.dto;

import java.time.LocalDate;

import lombok.Data;

@Data

public class AdoptionRequestDTO {

    private Long id;

    private Long adopterId;

    private Long petId;

    private LocalDate requestDate;

    private String purpose;

    private String papers;

    private String status;

}
