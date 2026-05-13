package co.edu.udistrital.mdp.pets.controllers;

import co.edu.udistrital.mdp.pets.dto.*;
import co.edu.udistrital.mdp.pets.entities.PetEntity;
import co.edu.udistrital.mdp.pets.exceptions.EntityNotFoundException;
import co.edu.udistrital.mdp.pets.exceptions.IllegalOperationException;
import co.edu.udistrital.mdp.pets.services.PetService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/pets") // Unificado a plural según la mayoría de tus colecciones
public class PetController {

    @Autowired
    private PetService petService;

    @Autowired
    private ModelMapper modelMapper;

    @GetMapping("/{id}")
    public PetDetailDTO getPet(@PathVariable Long id) throws EntityNotFoundException {
        PetEntity entity = petService.getPet(id);
        return modelMapper.map(entity, PetDetailDTO.class);
    }

    @GetMapping
    public List<PetDTO> getPets() {
        return petService.getPets().stream()
                .map(p -> modelMapper.map(p, PetDTO.class))
                .collect(Collectors.toList());
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public PetDTO createPet(@RequestBody PetDTO petDTO) throws EntityNotFoundException, IllegalOperationException {
        PetEntity entity = modelMapper.map(petDTO, PetEntity.class);
        return modelMapper.map(petService.createPet(entity), PetDTO.class);
    }

    @PutMapping("/{id}")
    public PetDTO updatePet(@PathVariable Long id, @RequestBody PetDTO petDTO) throws EntityNotFoundException, IllegalOperationException {
        PetEntity entity = modelMapper.map(petDTO, PetEntity.class);
        return modelMapper.map(petService.updatePet(id, entity), PetDTO.class);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deletePet(@PathVariable Long id) throws EntityNotFoundException, IllegalOperationException {
        petService.deletePet(id);
    }

    // --- Endpoints de Sub-recursos (Requeridos por la colección Ricardo) ---

    @GetMapping("/{id}/life-events")
    public List<LifeEventDTO> getPetLifeEvents(@PathVariable Long id) throws EntityNotFoundException {
        PetEntity entity = petService.getPet(id);
        return entity.getLifeEvents().stream()
                .map(e -> modelMapper.map(e, LifeEventDTO.class))
                .collect(Collectors.toList());
    }

    @GetMapping("/{petId}/life-events/{eventId}")
    public LifeEventDTO getPetLifeEvent(@PathVariable Long petId, @PathVariable Long eventId) throws EntityNotFoundException {
        PetEntity pet = petService.getPet(petId);
        // Filtramos el evento específico dentro de la mascota
        return pet.getLifeEvents().stream()
                .filter(e -> e.getId().equals(eventId))
                .findFirst()
                .map(e -> modelMapper.map(e, LifeEventDTO.class))
                .orElseThrow(() -> new EntityNotFoundException("LifeEvent not found for this pet"));
    }
}