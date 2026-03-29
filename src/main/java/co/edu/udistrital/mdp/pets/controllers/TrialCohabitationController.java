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

import co.edu.udistrital.mdp.pets.dto.TrialCohabitationDTO;
import co.edu.udistrital.mdp.pets.entities.TrialCohabitationEntity;
import co.edu.udistrital.mdp.pets.services.TrialCohabitationService;

@RestController
@RequestMapping("/cohabitations-trials")
public class TrialCohabitationController {

    @Autowired

    private TrialCohabitationService trialCohabitationService;

    @Autowired

    private ModelMapper modelMapper;

    @GetMapping
    @ResponseStatus(code = HttpStatus.OK)
    public List<TrialCohabitationDTO> getAllTrialCohabitations() {

        List<TrialCohabitationEntity> trialCohabitationEntities = trialCohabitationService.getTrialCohabitations();
        return modelMapper.map(trialCohabitationEntities, new TypeToken<List<TrialCohabitationDTO>>() {
        }.getType());
    }
}
