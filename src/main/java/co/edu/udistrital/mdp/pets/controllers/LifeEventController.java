package co.edu.udistrital.mdp.pets.controllers;

import java.util.List;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import co.edu.udistrital.mdp.pets.dto.LifeEventDTO;
import co.edu.udistrital.mdp.pets.dto.PetDTO;
import co.edu.udistrital.mdp.pets.dto.TypeLEDTO;
import co.edu.udistrital.mdp.pets.dto.VeterinarianDTO;
import co.edu.udistrital.mdp.pets.entities.LifeEventEntity;
import co.edu.udistrital.mdp.pets.entities.PetEntity;
import co.edu.udistrital.mdp.pets.entities.TypeLEEntity;
import co.edu.udistrital.mdp.pets.entities.VeterinarianEntity;
import co.edu.udistrital.mdp.pets.exceptions.EntityNotFoundException;
import co.edu.udistrital.mdp.pets.exceptions.IllegalOperationException;
import co.edu.udistrital.mdp.pets.services.LifeEventService;

@RestController
@RequestMapping("/life-events")
public class LifeEventController {

    @Autowired
    private LifeEventService lifeEventService;

    @GetMapping
    public ResponseEntity<List<LifeEventDTO>> getLifeEvents() {
        List<LifeEventDTO> events = lifeEventService.getLifeEvents().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
        return new ResponseEntity<>(events, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<LifeEventDTO> getLifeEvent(@PathVariable Long id) throws EntityNotFoundException {
        LifeEventEntity entity = lifeEventService.getLifeEvent(id);
        return new ResponseEntity<>(convertToDTO(entity), HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<LifeEventDTO> createLifeEvent(@RequestBody LifeEventDTO dto) 
            throws EntityNotFoundException, IllegalOperationException {
        // Usamos tu enfoque seguro de conversión manual
        LifeEventEntity entity = convertToEntity(dto);
        LifeEventEntity created = lifeEventService.createLifeEvent(entity);
        return new ResponseEntity<>(convertToDTO(created), HttpStatus.CREATED);
    }

    // --- ¡RESOLVEMOS TAMBIÉN EL 404 DE CONSULTAR EVENTOS AQUÍ! ---
    @GetMapping("/pet/{petId}")
    public ResponseEntity<List<LifeEventDTO>> getEventsByPet(@PathVariable Long petId) {
        List<LifeEventDTO> events = lifeEventService.getLifeEvents().stream()
                .filter(evento -> evento.getPet() != null && evento.getPet().getId().equals(petId))
                .map(this::convertToDTO)
                .collect(Collectors.toList());
        return new ResponseEntity<>(events, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteLifeEvent(@PathVariable Long id) 
            throws EntityNotFoundException, IllegalOperationException {
        lifeEventService.deleteLifeEvent(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    // ==========================================
    // MÉTODOS DE CONVERSIÓN MANUAL (Tu Patrón)
    // ==========================================
    
    private LifeEventDTO convertToDTO(LifeEventEntity entity) {
        LifeEventDTO dto = new LifeEventDTO();
        dto.setId(entity.getId());
        dto.setDescription(entity.getDescription());
        dto.setDate(entity.getDate());

        if (entity.getPet() != null) {
            PetDTO petDTO = new PetDTO();
            petDTO.setId(entity.getPet().getId());
            petDTO.setName(entity.getPet().getName());
            dto.setPet(petDTO);
        }

        if (entity.getType() != null) {
            TypeLEDTO typeDTO = new TypeLEDTO();
            typeDTO.setId(entity.getType().getId());
            typeDTO.setName(entity.getType().getName());
            dto.setType(typeDTO);
        }

        if (entity.getVeterinarian() != null) {
            VeterinarianDTO vetDTO = new VeterinarianDTO();
            vetDTO.setId(entity.getVeterinarian().getId());
            vetDTO.setLicenseNumber(entity.getVeterinarian().getLicenseNumber());
            vetDTO.setSpecialty(entity.getVeterinarian().getSpecialty());
            dto.setVeterinarian(vetDTO);
        }

        return dto;
    }

    private LifeEventEntity convertToEntity(LifeEventDTO dto) {
        LifeEventEntity entity = new LifeEventEntity();
        entity.setId(dto.getId());
        entity.setDescription(dto.getDescription());
        entity.setDate(dto.getDate());

        // Mapeo seguro y explícito de Relaciones para pasar las validaciones del Service
        if (dto.getPet() != null && dto.getPet().getId() != null) {
            PetEntity pet = new PetEntity();
            pet.setId(dto.getPet().getId());
            entity.setPet(pet);
        }

        if (dto.getType() != null && dto.getType().getId() != null) {
            TypeLEEntity type = new TypeLEEntity();
            type.setId(dto.getType().getId());
            entity.setType(type);
        }

        if (dto.getVeterinarian() != null && dto.getVeterinarian().getId() != null) {
            VeterinarianEntity vet = new VeterinarianEntity();
            vet.setId(dto.getVeterinarian().getId());
            entity.setVeterinarian(vet);
        }

        return entity;
    }
}