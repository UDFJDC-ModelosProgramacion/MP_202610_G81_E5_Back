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

import co.edu.udistrital.mdp.pets.dto.TrialCohabitationDTO;
import co.edu.udistrital.mdp.pets.entities.TrialCohabitationEntity;
import co.edu.udistrital.mdp.pets.exceptions.EntityNotFoundException;
import co.edu.udistrital.mdp.pets.exceptions.IllegalOperationException;
import co.edu.udistrital.mdp.pets.services.TrialCohabitationService;

@RestController
@RequestMapping("/cohabitations-trials")
public class TrialCohabitationController {

    @Autowired

    private TrialCohabitationService trialCohabitationService;

    @Autowired

    private ModelMapper modelMapper;


    // Se encuentran todas las cohabitaciones de prueba
    @GetMapping
    @ResponseStatus(code = HttpStatus.OK)
    public List<TrialCohabitationDTO> getAllTrialCohabitations() {

        List<TrialCohabitationEntity> trialCohabitationEntities = trialCohabitationService.getTrialCohabitations();
        return modelMapper.map(trialCohabitationEntities, new TypeToken<List<TrialCohabitationDTO>>() {
        }.getType());
    }

    // Se encuentra una cohabitación de prueba por su id
    @GetMapping(value ="/{id}")
    @ResponseStatus(code = HttpStatus.OK)
    public TrialCohabitationDTO findOne(@PathVariable Long id) throws EntityNotFoundException {
        TrialCohabitationEntity trialCohabitationEntity = trialCohabitationService.getTrialCohabitation(id);
        return modelMapper.map(trialCohabitationEntity, TrialCohabitationDTO.class);
    }

    // Se crea una cohabitación de prueba. Para crearla se debe enviar el id del proceso de adopción en el cuerpo de la petición.
    @PostMapping
    @ResponseStatus(code = HttpStatus.CREATED)
    public TrialCohabitationDTO create(@RequestBody TrialCohabitationDTO trialCohabitationDTO) throws IllegalOperationException, EntityNotFoundException{

        TrialCohabitationEntity trialCohabitationEntity = trialCohabitationService.createTrialCohabitation(modelMapper.map(trialCohabitationDTO, TrialCohabitationEntity.class));
        
        return modelMapper.map(trialCohabitationEntity, TrialCohabitationDTO.class);
    }

    // Se actualiza una cohabitación de prueba. Para actualizarla se debe enviar el id de la cohabitación de prueba en la ruta y el id del proceso de adopción en el cuerpo de la petición.
    @PutMapping(value ="/{id}")
    @ResponseStatus(code = HttpStatus.OK)
    public TrialCohabitationDTO update(@PathVariable Long id, @RequestBody TrialCohabitationDTO trialCohabitationDTO) 
                    throws EntityNotFoundException, IllegalOperationException {

        TrialCohabitationEntity trialCohabitationEntity = trialCohabitationService.updateTrialCohabitation(id, modelMapper.map(trialCohabitationDTO, TrialCohabitationEntity.class));

        return modelMapper.map(trialCohabitationEntity, TrialCohabitationDTO.class);
    }

    // Se elimina una cohabitación de prueba por su id
    @DeleteMapping(value ="/{id}")
    @ResponseStatus(code = HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) throws EntityNotFoundException, IllegalOperationException {
        trialCohabitationService.deleteTrialCohabitation(id);
    }

}
