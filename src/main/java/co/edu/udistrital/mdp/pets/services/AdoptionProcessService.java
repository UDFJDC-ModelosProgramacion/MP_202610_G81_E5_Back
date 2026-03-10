package co.edu.udistrital.mdp.pets.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import co.edu.udistrital.mdp.pets.repositories.AdoptionProcessRepository;
import lombok.extern.slf4j.Slf4j;

@Slf4j

@Service

public class AdoptionProcessService {

    @Autowired
    AdoptionProcessRepository adoptionProcessRepository;
}
