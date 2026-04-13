package co.edu.udistrital.mdp.pets.dto;

import java.time.LocalDate;

import lombok.Data;

@Data
public class MedicalRecordDTO {
    private Long id; 
    private String vaccinations;
    private LocalDate upcomingDates;
    
    
    private Long petId;
    private Long veterinarianId;
}
