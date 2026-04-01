package co.edu.udistrital.mdp.pets.controllers;

import java.util.List;

import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
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


    // Se encuentran todos los procesos de adopción
    @GetMapping
    @ResponseStatus(code = HttpStatus.OK)
    public List<AdoptionProcessDTO> getAllAdoptionProcesses() {

        List<AdoptionProcessEntity> adoptionProcessEntities = adoptionProcessService.getAdoptionProcesses();
        return modelMapper.map(adoptionProcessEntities, new TypeToken<List<AdoptionProcessDTO>>() {
        }.getType());
    }

    // Se encuentra un proceso de adopción por su id
    @GetMapping(value ="/{id}")
    @ResponseStatus(code = HttpStatus.OK)
    public AdoptionProcessDTO findOne(@PathVariable Long id) throws EntityNotFoundException {
        AdoptionProcessEntity adoptionProcessEntity = adoptionProcessService.getAdoptionProcess(id);
        return modelMapper.map(adoptionProcessEntity, AdoptionProcessDTO.class);
    }

    // Se crea un proceso de adopción. Para crearlo la solicitud de adopción debe existir y estar aprobada.
    @PostMapping
    @ResponseStatus(code = HttpStatus.CREATED)
    public AdoptionProcessDTO create(@RequestBody AdoptionProcessDTO adoptionProcessDTO) throws IllegalOperationException, EntityNotFoundException{

        AdoptionProcessEntity adoptionProcessEntity = adoptionProcessService.createAdoptionProcess(modelMapper.map(adoptionProcessDTO, AdoptionProcessEntity.class));
        
        return modelMapper.map(adoptionProcessEntity, AdoptionProcessDTO.class);
    }

    // Se actualiza un proceso de adopción. Para actualizarlo se debe enviar el id del proceso de adopción en la ruta y el id de la solicitud de adopción en el cuerpo de la petición.
    @PutMapping(value ="/{id}")
    @ResponseStatus(code = HttpStatus.OK)
    public AdoptionProcessDTO update(@PathVariable Long id, @RequestBody AdoptionProcessDTO adoptionProcessDTO) 
                    throws EntityNotFoundException, IllegalOperationException {

        AdoptionProcessEntity adoptionProcessEntity = adoptionProcessService.updateAdoptionProcess(id, modelMapper.map(adoptionProcessDTO, AdoptionProcessEntity.class));

        return modelMapper.map(adoptionProcessEntity, AdoptionProcessDTO.class);
    }

    // Se elimina un proceso de adopción por su id
    @DeleteMapping(value ="/{id}")
    @ResponseStatus(code = HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) throws EntityNotFoundException, IllegalOperationException {
        adoptionProcessService.deleteAdoptionProcess(id);
    }
}