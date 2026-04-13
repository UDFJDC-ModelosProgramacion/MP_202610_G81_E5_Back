package co.edu.udistrital.mdp.pets.controllers;
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

import co.edu.udistrital.mdp.pets.dto.EventDTO;
import co.edu.udistrital.mdp.pets.entities.EventEntity;
import co.edu.udistrital.mdp.pets.entities.ShelterEntity;
import co.edu.udistrital.mdp.pets.exceptions.EntityNotFoundException;
import co.edu.udistrital.mdp.pets.exceptions.IllegalOperationException;
import co.edu.udistrital.mdp.pets.services.EventService;

@RestController
@RequestMapping("/api/events")
public class EventController {

    @Autowired
    private EventService eventService;

    @GetMapping("/{id}")
    public ResponseEntity<EventDTO> getEvent(@PathVariable Long id) throws EntityNotFoundException { 
        EventEntity entity = eventService.getEvent(id);
        return new ResponseEntity<>(convertToDTO(entity), HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<EventDTO> createEvent(@RequestBody EventDTO dto) throws IllegalOperationException { 
        EventEntity entity = convertToEntity(dto);
        EventEntity created = eventService.createEvent(entity);
        return new ResponseEntity<>(convertToDTO(created), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<EventDTO> updateEvent(@PathVariable Long id, @RequestBody EventDTO dto) throws EntityNotFoundException, IllegalOperationException { 
        EventEntity entity = convertToEntity(dto);
        EventEntity updated = eventService.updateEvent(id, entity);
        return new ResponseEntity<>(convertToDTO(updated), HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteEvent(@PathVariable Long id) throws EntityNotFoundException, IllegalOperationException { // Validar que el evento existe y no tiene restricciones para eliminar   
        eventService.deleteEvent(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    private EventDTO convertToDTO(EventEntity entity) {
        EventDTO dto = new EventDTO();
        dto.setId(entity.getId());
        dto.setName(entity.getName());
        dto.setDate(entity.getDate());
        dto.setType(entity.getType());
        if (entity.getShelter() != null) {
            dto.setShelterId(entity.getShelter().getId());
        }
        return dto;
    }

    private EventEntity convertToEntity(EventDTO dto) {
        EventEntity entity = new EventEntity();
        entity.setId(dto.getId());
        entity.setName(dto.getName());
        entity.setDate(dto.getDate());
        entity.setType(dto.getType());
        
        if (dto.getShelterId() != null) {
            ShelterEntity shelter = new ShelterEntity();
            shelter.setId(dto.getShelterId());
            entity.setShelter(shelter);
        }
        return entity;
    }
}