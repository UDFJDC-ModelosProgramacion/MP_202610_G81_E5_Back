package co.edu.udistrital.mdp.pets.controllers;

import co.edu.udistrital.mdp.pets.dto.LifeEventDTO;
import co.edu.udistrital.mdp.pets.dto.TypeLEDTO;
import co.edu.udistrital.mdp.pets.entities.TypeLEEntity;
import co.edu.udistrital.mdp.pets.exceptions.EntityNotFoundException;
import co.edu.udistrital.mdp.pets.exceptions.IllegalOperationException;
import co.edu.udistrital.mdp.pets.services.TypeLEService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;

import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/type-life-events")
public class TypeLEController {

    @Autowired
    private TypeLEService typeLEService;

    @Autowired
    private ModelMapper modelMapper;

    @GetMapping("/{id}")
    public TypeLEDTO getTypeLE(@PathVariable Long id) throws EntityNotFoundException {
        return modelMapper.map(typeLEService.getTypeLE(id), TypeLEDTO.class);

    }

    @GetMapping
    public List<TypeLEDTO> getTypeLEs() {
        return typeLEService.getTypeLEs().stream()
                .map(t -> modelMapper.map(t, TypeLEDTO.class))
                .collect(Collectors.toList());

    }

    @GetMapping("/{id}/life-events")
    public List<LifeEventDTO> getLifeEventsByType(@PathVariable Long id) throws EntityNotFoundException {
        TypeLEEntity entity = typeLEService.getTypeLE(id);
        return entity.getLifeEvents().stream()
                .map(e -> modelMapper.map(e, LifeEventDTO.class))
                .collect(Collectors.toList());

    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public TypeLEDTO createTypeLE(@RequestBody TypeLEDTO dto) throws EntityNotFoundException, IllegalOperationException {
        TypeLEEntity entity = modelMapper.map(dto, TypeLEEntity.class);
        return modelMapper.map(typeLEService.createTypeLE(entity), TypeLEDTO.class);
    }
}
