package co.edu.udistrital.mdp.pets.dto;

import java.time.LocalDate;

import lombok.Data;

@Data

public class AdoptionProcessDTO {

    private Long id;

    private Long requestId;

    private Long adopterId;

    private Long veterinarianId;

    private Long petId;

    private LocalDate requestDate;

    private String status; // en proceso, aprobado, rechazado, cerrado

    private AdoptionRequestDTO request;

}
