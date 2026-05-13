package co.edu.udistrital.mdp.pets.controllers;

import co.edu.udistrital.mdp.pets.dto.LifeEventDTO;
import co.edu.udistrital.mdp.pets.entities.LifeEventEntity;
import co.edu.udistrital.mdp.pets.exceptions.EntityNotFoundException;
import co.edu.udistrital.mdp.pets.exceptions.IllegalOperationException;
import co.edu.udistrital.mdp.pets.services.LifeEventService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/life-events")
public class LifeEventController {

    @Autowired
    private LifeEventService lifeEventService;

    @Autowired
    private ModelMapper modelMapper;

    @GetMapping("/{id}")
    public LifeEventDTO getLifeEvent(@PathVariable Long id) throws EntityNotFoundException {
        return modelMapper.map(lifeEventService.getLifeEvent(id), LifeEventDTO.class);
    }

    @GetMapping
    public List<LifeEventDTO> getLifeEvents() {
        return lifeEventService.getLifeEvents().stream()
                .map(e -> modelMapper.map(e, LifeEventDTO.class))
                .collect(Collectors.toList());
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public LifeEventDTO createLifeEvent(@RequestBody LifeEventDTO dto) throws EntityNotFoundException, IllegalOperationException {
        LifeEventEntity entity = modelMapper.map(dto, LifeEventEntity.class);
        return modelMapper.map(lifeEventService.createLifeEvent(entity), LifeEventDTO.class);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteLifeEvent(@PathVariable Long id) throws EntityNotFoundException, IllegalOperationException {
        lifeEventService.deleteLifeEvent(id);
    }
}