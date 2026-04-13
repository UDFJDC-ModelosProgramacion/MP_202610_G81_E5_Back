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

import co.edu.udistrital.mdp.pets.dto.ShelterDTO;
import co.edu.udistrital.mdp.pets.entities.ShelterEntity;
import co.edu.udistrital.mdp.pets.exceptions.EntityNotFoundException;
import co.edu.udistrital.mdp.pets.exceptions.IllegalOperationException;
import co.edu.udistrital.mdp.pets.services.ShelterService;

@RestController
@RequestMapping("/api/shelters")
public class ShelterController {

    @Autowired
    private ShelterService shelterService;

    @GetMapping
    public ResponseEntity<List<ShelterDTO>> getShelters() {
        List<ShelterDTO> shelters = shelterService.getShelters().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
        return new ResponseEntity<>(shelters, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ShelterDTO> getShelter(@PathVariable Long id) throws EntityNotFoundException {
        ShelterEntity entity = shelterService.getShelter(id);
        return new ResponseEntity<>(convertToDTO(entity), HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<ShelterDTO> createShelter(@RequestBody ShelterDTO dto) throws IllegalOperationException {
        ShelterEntity entity = convertToEntity(dto);
        ShelterEntity created = shelterService.createShelter(entity);
        return new ResponseEntity<>(convertToDTO(created), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ShelterDTO> updateShelter(@PathVariable Long id, @RequestBody ShelterDTO dto) 
            throws EntityNotFoundException, IllegalOperationException {
        ShelterEntity entity = convertToEntity(dto);
        ShelterEntity updated = shelterService.updateShelter(id, entity);
        return new ResponseEntity<>(convertToDTO(updated), HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteShelter(@PathVariable Long id) 
            throws EntityNotFoundException, IllegalOperationException {
        shelterService.deleteShelter(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    private ShelterDTO convertToDTO(ShelterEntity entity) {
        ShelterDTO dto = new ShelterDTO();
        dto.setId(entity.getId());
        dto.setName(entity.getName());
        dto.setCity(entity.getCity());
        dto.setLocation(entity.getLocation());
        return dto;
    }

    private ShelterEntity convertToEntity(ShelterDTO dto) {
        ShelterEntity entity = new ShelterEntity();
        entity.setId(dto.getId());
        entity.setName(dto.getName());
        entity.setCity(dto.getCity());
        entity.setLocation(dto.getLocation());
        return entity;
    }
}