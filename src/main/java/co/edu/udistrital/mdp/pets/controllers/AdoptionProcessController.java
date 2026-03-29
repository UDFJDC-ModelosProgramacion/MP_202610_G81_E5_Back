package co.edu.udistrital.mdp.pets.controllers;

import java.util.List;

import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import co.edu.udistrital.mdp.pets.dto.AdoptionProcessDTO;
import co.edu.udistrital.mdp.pets.entities.AdoptionProcessEntity;
import co.edu.udistrital.mdp.pets.exceptions.EntityNotFoundException;
import co.edu.udistrital.mdp.pets.exceptions.IllegalOperationException;
import co.edu.udistrital.mdp.pets.services.AdoptionProcessService;

@RestController
@RequestMapping("/adoption-processes")

public class AdoptionProcessController {

    @Autowired
    
    private AdoptionProcessService adoptionProcessService;

    @Autowired

    private ModelMapper modelMapper;

    @GetMapping
    @ResponseStatus(code = HttpStatus.OK)
    public List<AdoptionProcessDTO> getAllAdoptionProcesses() {

        List<AdoptionProcessEntity> adoptionProcessEntities = adoptionProcessService.getAdoptionProcesses();
        return modelMapper.map(adoptionProcessEntities, new TypeToken<List<AdoptionProcessDTO>>() {
        }.getType());
    }

    @GetMapping(value ="/{id}")
    @ResponseStatus(code = HttpStatus.OK)
    public AdoptionProcessDTO findOne(@PathVariable Long id) throws EntityNotFoundException {
        AdoptionProcessEntity adoptionProcessEntity = adoptionProcessService.getAdoptionProcess(id);
        return modelMapper.map(adoptionProcessEntity, AdoptionProcessDTO.class);
    }

    @PostMapping
    @ResponseStatus(code = HttpStatus.CREATED)
    public AdoptionProcessDTO create(@RequestBody AdoptionProcessDTO adoptionProcessDTO) throws IllegalOperationException, EntityNotFoundException{

        AdoptionProcessEntity adoptionProcessEntity = adoptionProcessService.createAdoptionProcess(modelMapper.map(adoptionProcessDTO, AdoptionProcessEntity.class));
        
        return modelMapper.map(adoptionProcessEntity, AdoptionProcessDTO.class);
    }

}
