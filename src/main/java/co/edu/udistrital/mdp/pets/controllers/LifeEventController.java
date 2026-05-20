package co.edu.udistrital.mdp.pets.controllers;

import co.edu.udistrital.mdp.pets.dto.LifeEventDTO;
import co.edu.udistrital.mdp.pets.entities.LifeEventEntity;
import co.edu.udistrital.mdp.pets.exceptions.EntityNotFoundException;
import co.edu.udistrital.mdp.pets.exceptions.IllegalOperationException;
import co.edu.udistrital.mdp.pets.services.LifeEventService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/life-events")
public class LifeEventController {

    @Autowired
    private LifeEventService lifeEventService;

    @GetMapping("/{id}")
    public ResponseEntity<LifeEventDTO> getLifeEvent(@PathVariable Long id) throws EntityNotFoundException {
        LifeEventEntity entity = lifeEventService.getLifeEvent(id);
        return new ResponseEntity<>(convertToDTO(entity), HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<List<LifeEventDTO>> getLifeEvents() {
        List<LifeEventDTO> dtos = lifeEventService.getLifeEvents().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
        return new ResponseEntity<>(dtos, HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<LifeEventDTO> createLifeEvent(@RequestBody LifeEventDTO dto) throws EntityNotFoundException, IllegalOperationException {
        LifeEventEntity entity = convertToEntity(dto);
        LifeEventEntity created = lifeEventService.createLifeEvent(entity);
        return new ResponseEntity<>(convertToDTO(created), HttpStatus.CREATED);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteLifeEvent(@PathVariable Long id) throws EntityNotFoundException, IllegalOperationException {
        lifeEventService.deleteLifeEvent(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    // Métodos de conversión
    private LifeEventDTO convertToDTO(LifeEventEntity entity) {
        LifeEventDTO dto = new LifeEventDTO();
        dto.setId(entity.getId());
        dto.setDate(entity.getDate());
        dto.setDescription(entity.getDescription());
        dto.setType(entity.getType() != null ? entity.getType().getId() : null); // Ejemplo
        dto.setPet(entity.getPet() != null ? entity.getPet().getId() : null);
        return dto;
    }

    private LifeEventEntity convertToEntity(LifeEventDTO dto) {
        LifeEventEntity entity = new LifeEventEntity();
        entity.setId(dto.getId());
        entity.setDate(dto.getDate());
        entity.setDescription(dto.getDescription());
        // Las relaciones se deben resolver con servicios aparte o mediante IDs
        return entity;
    }
}
