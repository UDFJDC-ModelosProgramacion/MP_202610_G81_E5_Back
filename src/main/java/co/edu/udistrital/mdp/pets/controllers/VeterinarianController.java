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

import co.edu.udistrital.mdp.pets.dto.VeterinarianDTO;
import co.edu.udistrital.mdp.pets.entities.ShelterEntity;
import co.edu.udistrital.mdp.pets.entities.VeterinarianEntity;
import co.edu.udistrital.mdp.pets.exceptions.EntityNotFoundException;
import co.edu.udistrital.mdp.pets.exceptions.IllegalOperationException;
import co.edu.udistrital.mdp.pets.services.VeterinarianService;

@RestController
@RequestMapping("/veterinarians")
public class VeterinarianController {

    @Autowired
    private VeterinarianService veterinarianService;

    @GetMapping
    public ResponseEntity<List<VeterinarianDTO>> getVeterinarians() {
        List<VeterinarianDTO> vets = veterinarianService.getVeterinarians().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
        return new ResponseEntity<>(vets, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<VeterinarianDTO> getVeterinarian(@PathVariable Long id) throws EntityNotFoundException {
        VeterinarianEntity entity = veterinarianService.getVeterinarian(id);
        return new ResponseEntity<>(convertToDTO(entity), HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<VeterinarianDTO> createVeterinarian(@RequestBody VeterinarianDTO dto) throws IllegalOperationException {
        VeterinarianEntity entity = convertToEntity(dto);
        VeterinarianEntity created = veterinarianService.createVeterinarian(entity);
        return new ResponseEntity<>(convertToDTO(created), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<VeterinarianDTO> updateVeterinarian(@PathVariable Long id, @RequestBody VeterinarianDTO dto) 
            throws EntityNotFoundException, IllegalOperationException {
        VeterinarianEntity entity = convertToEntity(dto);
        VeterinarianEntity updated = veterinarianService.updateVeterinarian(id, entity);
        return new ResponseEntity<>(convertToDTO(updated), HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteVeterinarian(@PathVariable Long id) 
            throws EntityNotFoundException, IllegalOperationException {
        veterinarianService.deleteVeterinarian(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    private VeterinarianDTO convertToDTO(VeterinarianEntity entity) {
        VeterinarianDTO dto = new VeterinarianDTO();
        dto.setId(entity.getId());
        dto.setLicenseNumber(entity.getLicenseNumber());
        dto.setSpecialty(entity.getSpecialty());
        dto.setAvailability(entity.getAvailability());
        if (entity.getShelter() != null) dto.setShelterId(entity.getShelter().getId());
        return dto;
    }

    private VeterinarianEntity convertToEntity(VeterinarianDTO dto) {
        VeterinarianEntity entity = new VeterinarianEntity();
        entity.setId(dto.getId());
        entity.setLicenseNumber(dto.getLicenseNumber());
        entity.setSpecialty(dto.getSpecialty());
        entity.setAvailability(dto.getAvailability());
        if (dto.getShelterId() != null) {
            ShelterEntity shelter = new ShelterEntity();
            shelter.setId(dto.getShelterId());
            entity.setShelter(shelter);
        }
        return entity;
    }
}