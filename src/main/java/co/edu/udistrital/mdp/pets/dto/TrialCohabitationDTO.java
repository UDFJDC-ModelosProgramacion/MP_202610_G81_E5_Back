package co.edu.udistrital.mdp.pets.dto;

import java.time.LocalDate;

import lombok.Data;

@Data
public class TrialCohabitationDTO {

    private Long id;

    private LocalDate startDate;

    private LocalDate endDate;

    private String status; // en proceso, aprobado, rechazado, cerrado

    private AdoptionProcessDTO adoptionProcess;

    private VeterinarianDTO veterinarian;

}
