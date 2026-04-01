package co.edu.udistrital.mdp.pets.dto;
import java.util.ArrayList;
import java.util.List;

import lombok.Data;

@Data
public class PetDetailDTO extends PetDTO {
    private List<LifeEventDTO> lifeEvents = new ArrayList<>();
    private List<MedicalRecordDTO> medicalRecords = new ArrayList<>();
    private List<AdoptionProcessDTO> adoptionProcesses = new ArrayList<>();
    private List<AdoptionRequestDTO> adoptionRequests = new ArrayList<>();

}
