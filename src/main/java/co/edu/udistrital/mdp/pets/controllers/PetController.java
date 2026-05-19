package co.edu.udistrital.mdp.pets.controllers;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import co.edu.udistrital.mdp.pets.dto.PetDTO;
import co.edu.udistrital.mdp.pets.entities.AdopterEntity;
import co.edu.udistrital.mdp.pets.entities.PetEntity;
import co.edu.udistrital.mdp.pets.entities.ShelterEntity;
import co.edu.udistrital.mdp.pets.exceptions.EntityNotFoundException;
import co.edu.udistrital.mdp.pets.exceptions.IllegalOperationException;
import co.edu.udistrital.mdp.pets.services.PetService;

@RestController
@RequestMapping("/pets")
public class PetController {

    @Autowired
    private PetService petService;

    @GetMapping
    public ResponseEntity<List<PetDTO>> getPets() {
        List<PetDTO> pets = petService.getPets().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
        return new ResponseEntity<>(pets, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<PetDTO> getPet(@PathVariable Long id) throws EntityNotFoundException {
        PetEntity entity = petService.getPet(id);
        return new ResponseEntity<>(convertToDTO(entity), HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<PetDTO> createPet(@RequestBody PetDTO dto) 
            throws EntityNotFoundException, IllegalOperationException {
        PetEntity entity = convertToEntity(dto);
        PetEntity created = petService.createPet(entity);
        return new ResponseEntity<>(convertToDTO(created), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<PetDTO> updatePet(@PathVariable Long id, @RequestBody PetDTO dto)
            throws EntityNotFoundException, IllegalOperationException {
        PetEntity entity = convertToEntity(dto);
        PetEntity updated = petService.updatePet(id, entity);
        return new ResponseEntity<>(convertToDTO(updated), HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePet(@PathVariable Long id)
            throws EntityNotFoundException, IllegalOperationException {
        petService.deletePet(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @GetMapping("/{id}/life-events")
    public ResponseEntity<List<?>> getLifeEvents(@PathVariable Long id) throws EntityNotFoundException {
        PetEntity pet = petService.getPet(id);
        return new ResponseEntity<>(pet.getLifeEvents(), HttpStatus.OK);
    }

    private PetDTO convertToDTO(PetEntity entity) {
        PetDTO dto = new PetDTO();
        dto.setId(entity.getId());
        dto.setName(entity.getName());
        dto.setSpecie(entity.getSpecie());
        dto.setBreed(entity.getBreed());
        dto.setAge(entity.getAge());
        dto.setStatus(entity.getStatus());
        dto.setTemperament(entity.getTemperament());
        dto.setCompKids(entity.getCompKids());
        dto.setCompOtherPets(entity.getCompOtherPets());
        return dto;
    }

    private PetEntity convertToEntity(PetDTO dto) {
        PetEntity entity = new PetEntity();
        entity.setId(dto.getId());
        entity.setName(dto.getName());
        entity.setSpecie(dto.getSpecie());
        entity.setBreed(dto.getBreed());
        entity.setAge(dto.getAge() != null ? dto.getAge() : 0);
        entity.setStatus(dto.getStatus());
        entity.setTemperament(dto.getTemperament());
        entity.setCompKids(dto.getCompKids());
        entity.setCompOtherPets(dto.getCompOtherPets());

        if (dto.getShelter() != null && dto.getShelter().getId() != null) {
            ShelterEntity shelter = new ShelterEntity();
            shelter.setId(dto.getShelter().getId());
            entity.setShelter(shelter);
        }

        if (dto.getAdopter() != null && dto.getAdopter().getId() != null) {
            AdopterEntity adopter = new AdopterEntity();
            adopter.setId(dto.getAdopter().getId());
            entity.setAdopter(adopter);
        }

        return entity;
    }
}