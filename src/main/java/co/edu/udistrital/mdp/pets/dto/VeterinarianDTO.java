package co.edu.udistrital.mdp.pets.dto;

import lombok.Data;

@Data
public class VeterinarianDTO {
    private Long id; 
    private String licenseNumber;
    private String specialty;
    private String availability;
    
    private Long shelterId;
}