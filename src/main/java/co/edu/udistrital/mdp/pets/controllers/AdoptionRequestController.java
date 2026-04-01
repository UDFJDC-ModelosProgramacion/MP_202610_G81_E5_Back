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

import co.edu.udistrital.mdp.pets.dto.AdoptionRequestDTO;
import co.edu.udistrital.mdp.pets.entities.AdoptionRequestEntity;
import co.edu.udistrital.mdp.pets.exceptions.EntityNotFoundException;
import co.edu.udistrital.mdp.pets.exceptions.IllegalOperationException;
import co.edu.udistrital.mdp.pets.services.AdoptionRequestService;

@RestController
@RequestMapping("/adoption-requests")

public class AdoptionRequestController {

    @Autowired

    private AdoptionRequestService adoptionRequestService;

    @Autowired

    private ModelMapper modelMapper;


    // Se encuentran todas las solicitudes de adopción
    @GetMapping
    @ResponseStatus(code = HttpStatus.OK)
    public List<AdoptionRequestDTO> getAllAdoptionRequests() {

        List<AdoptionRequestEntity> adoptionRequestEntities = adoptionRequestService.getAdoptionRequests();
        
        return modelMapper.map(adoptionRequestEntities, new TypeToken<List<AdoptionRequestDTO>>() {
        }.getType());
    }

    // Se encuentra una solicitud de adopción por su id
    @GetMapping(value ="/{id}")
    @ResponseStatus(code = HttpStatus.OK)
    public AdoptionRequestDTO findOne(@PathVariable Long id) throws EntityNotFoundException {
        AdoptionRequestEntity adoptionRequestEntity = adoptionRequestService.getAdoptionRequest(id);
        return modelMapper.map(adoptionRequestEntity, AdoptionRequestDTO.class);
    }

    // Se crea una solicitud de adopción. Para crearla se debe enviar el id de la mascota y el id del adoptante en el cuerpo de la petición.
    @PostMapping
    @ResponseStatus(code = HttpStatus.CREATED)
    public AdoptionRequestDTO create(@RequestBody AdoptionRequestDTO adoptionRequestDTO) throws IllegalOperationException, EntityNotFoundException{

        AdoptionRequestEntity adoptionRequestEntity = adoptionRequestService.createAdoptionRequest(modelMapper.map(adoptionRequestDTO, AdoptionRequestEntity.class));

        return modelMapper.map(adoptionRequestEntity, AdoptionRequestDTO.class);
    }

    // Se actualiza una solicitud de adopción. Para actualizarla se debe enviar el id de la solicitud de adopción en la ruta y el id de la mascota y el id del adoptante en el cuerpo de la petición.
    @PutMapping(value ="/{id}")
    @ResponseStatus(code = HttpStatus.OK)
    public AdoptionRequestDTO update(@PathVariable Long id, @RequestBody AdoptionRequestDTO adoptionRequestDTO) 
                    throws EntityNotFoundException, IllegalOperationException {

        AdoptionRequestEntity adoptionRequestEntity = adoptionRequestService.updateAdoptionRequest(id, modelMapper.map(adoptionRequestDTO, AdoptionRequestEntity.class));

        return modelMapper.map(adoptionRequestEntity, AdoptionRequestDTO.class);
    }

    // Se elimina una solicitud de adopción por su id
    @DeleteMapping(value ="/{id}")
    @ResponseStatus(code = HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) throws EntityNotFoundException, IllegalOperationException {
        adoptionRequestService.deleteAdoptionRequest(id);
    }
}