package co.edu.udistrital.mdp.pets.controllers;

import co.edu.udistrital.mdp.pets.dto.*;
import co.edu.udistrital.mdp.pets.entities.TypeLEEntity;
import co.edu.udistrital.mdp.pets.exceptions.EntityNotFoundException;
import co.edu.udistrital.mdp.pets.exceptions.IllegalOperationException;
import co.edu.udistrital.mdp.pets.services.TypeLEService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/type-life-events")
public class TypeLEController {

    @Autowired
    private TypeLEService typeLEService;

    @GetMapping("/{id}")
    public ResponseEntity<TypeLEDTO> getTypeLE(@PathVariable Long id) throws EntityNotFoundException {
        TypeLEEntity entity = typeLEService.getTypeLE(id);
        return new ResponseEntity<>(convertToDTO(entity), HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<List<TypeLEDTO>> getTypeLEs() {
        List<TypeLEDTO> dtos = typeLEService.getTypeLEs().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
        return new ResponseEntity<>(dtos, HttpStatus.OK);
    }

    @GetMapping("/{id}/life-events")
    public ResponseEntity<List<LifeEventDTO>> getLifeEventsByType(@PathVariable Long id) throws EntityNotFoundException {
        TypeLEEntity entity = typeLEService.getTypeLE(id);
        List<LifeEventDTO> dtos = entity.getLifeEvents().stream()
                .map(e -> convertToLifeEventDTO(e))
                .collect(Collectors.toList());
        return new ResponseEntity<>(dtos, HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<TypeLEDTO> createTypeLE(@RequestBody TypeLEDTO dto) throws EntityNotFoundException, IllegalOperationException {
        TypeLEEntity entity = convertToEntity(dto);
        TypeLEEntity created = typeLEService.createTypeLE(entity);
        return new ResponseEntity<>(convertToDTO(created), HttpStatus.CREATED);
    }

    // Métodos de conversión
    private TypeLEDTO convertToDTO(TypeLEEntity entity) {
        TypeLEDTO dto = new TypeLEDTO();
        dto.setId(entity.getId());
        dto.setName(entity.getName());
        dto.setDescription(entity.getDescription());
        // otros campos si existen
        return dto;
    }

    private TypeLEEntity convertToEntity(TypeLEDTO dto) {
        TypeLEEntity entity = new TypeLEEntity();
        entity.setId(dto.getId());
        entity.setName(dto.getName());
        entity.setDescription(dto.getDescription());
        return entity;
    }

    private LifeEventDTO convertToLifeEventDTO(LifeEventEntity entity) {
        LifeEventDTO dto = new LifeEventDTO();
        dto.setId(entity.getId());
        dto.setDate(entity.getDate());
        dto.setDescription(entity.getDescription());
        // otros campos
        return dto;
    }
}
