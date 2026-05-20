package co.edu.udistrital.mdp.pets.controllers;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import co.edu.udistrital.mdp.pets.dto.MedicalRecordDTO;
import co.edu.udistrital.mdp.pets.entities.MedicalRecordEntity;
import co.edu.udistrital.mdp.pets.entities.PetEntity;
import co.edu.udistrital.mdp.pets.entities.VeterinarianEntity;
import co.edu.udistrital.mdp.pets.exceptions.EntityNotFoundException;
import co.edu.udistrital.mdp.pets.exceptions.IllegalOperationException;
import co.edu.udistrital.mdp.pets.services.MedicalRecordService;

@RestController
@RequestMapping("/medical-records")
public class MedicalRecordController {

    @Autowired
    private MedicalRecordService medicalRecordService;

    @GetMapping
    public ResponseEntity<List<MedicalRecordDTO>> getMedicalRecords() {
        List<MedicalRecordDTO> records = medicalRecordService.getMedicalRecords().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
        return new ResponseEntity<>(records, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<MedicalRecordDTO> getMedicalRecord(@PathVariable Long id) throws EntityNotFoundException {
        MedicalRecordEntity entity = medicalRecordService.getMedicalRecord(id);
        return new ResponseEntity<>(convertToDTO(entity), HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<MedicalRecordDTO> createMedicalRecord(@RequestBody MedicalRecordDTO dto) throws IllegalOperationException {
        MedicalRecordEntity entity = convertToEntity(dto);
        MedicalRecordEntity created = medicalRecordService.createMedicalRecord(entity);
        return new ResponseEntity<>(convertToDTO(created), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<MedicalRecordDTO> updateMedicalRecord(@PathVariable Long id, @RequestBody MedicalRecordDTO dto) 
            throws EntityNotFoundException, IllegalOperationException {
        MedicalRecordEntity entity = convertToEntity(dto);
        MedicalRecordEntity updated = medicalRecordService.updateMedicalRecord(id, entity);
        return new ResponseEntity<>(convertToDTO(updated), HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteMedicalRecord(@PathVariable Long id) 
            throws EntityNotFoundException, IllegalOperationException {
        medicalRecordService.deleteMedicalRecord(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    private MedicalRecordDTO convertToDTO(MedicalRecordEntity entity) {
        MedicalRecordDTO dto = new MedicalRecordDTO();
        dto.setId(entity.getId());
        dto.setVaccinations(entity.getVaccinations());
        dto.setUpcomingDates(entity.getUpcomingDates());
        if (entity.getPet() != null) dto.setPetId(entity.getPet().getId());
        if (entity.getVeterinarian() != null) dto.setVeterinarianId(entity.getVeterinarian().getId());
        return dto;
    }

    private MedicalRecordEntity convertToEntity(MedicalRecordDTO dto) {
        MedicalRecordEntity entity = new MedicalRecordEntity();
        entity.setId(dto.getId());
        entity.setVaccinations(dto.getVaccinations());
        entity.setUpcomingDates(dto.getUpcomingDates());
        if (dto.getPetId() != null) {
            PetEntity pet = new PetEntity();
            pet.setId(dto.getPetId());
            entity.setPet(pet);
        }
        if (dto.getVeterinarianId() != null) {
            VeterinarianEntity vet = new VeterinarianEntity();
            vet.setId(dto.getVeterinarianId());
            entity.setVeterinarian(vet);
        }
        return entity;
    }
}