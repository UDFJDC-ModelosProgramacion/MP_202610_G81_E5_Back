package co.edu.udistrital.mdp.pets.controllers;

import java.util.List;

import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import co.edu.udistrital.mdp.pets.dto.AdoptionRequestDTO;
import co.edu.udistrital.mdp.pets.entities.AdoptionRequestEntity;
import co.edu.udistrital.mdp.pets.services.AdoptionRequestService;

@RestController
@RequestMapping("/adoption-requests")

public class AdoptionRequestController {

    @Autowired

    private AdoptionRequestService adoptionRequestService;

    @Autowired

    private ModelMapper modelMapper;


    @GetMapping
    @ResponseStatus(code = HttpStatus.OK)
    public List<AdoptionRequestDTO> getAllAdoptionRequests() {

        List<AdoptionRequestEntity> adoptionRequestEntities = adoptionRequestService.getAdoptionRequests();
        
        return modelMapper.map(adoptionRequestEntities, new TypeToken<List<AdoptionRequestDTO>>() {
        }.getType());
    }

}
