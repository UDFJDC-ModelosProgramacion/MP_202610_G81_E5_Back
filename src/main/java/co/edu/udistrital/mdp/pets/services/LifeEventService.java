package co.edu.udistrital.mdp.pets.services;


import java.time.LocalDate;
import java.util.Comparator;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import co.edu.udistrital.mdp.pets.entities.LifeEventEntity;
import co.edu.udistrital.mdp.pets.entities.PetEntity;
import co.edu.udistrital.mdp.pets.entities.TypeLEEntity;
import co.edu.udistrital.mdp.pets.entities.VeterinarianEntity;
import co.edu.udistrital.mdp.pets.exceptions.EntityNotFoundException;
import co.edu.udistrital.mdp.pets.exceptions.IllegalOperationException;
import co.edu.udistrital.mdp.pets.repositories.LifeEventRepository;
import co.edu.udistrital.mdp.pets.repositories.PetRepository;
import co.edu.udistrital.mdp.pets.repositories.TypeLERepository;
import co.edu.udistrital.mdp.pets.repositories.VeterinarianRepository;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class LifeEventService {

    @Autowired
    LifeEventRepository lifeEventRepository;

    @Autowired
    PetRepository petRepository;

    @Autowired
    TypeLERepository typeLERepository;

    @Autowired
    VeterinarianRepository veterinarianRepository;

    @Transactional
    public LifeEventEntity createLifeEvent(LifeEventEntity lifeEventEntity) throws EntityNotFoundException, IllegalOperationException  {
        log.info("Inicia proceso de creación de evento de vida");

        validateLifeEvent(
            lifeEventEntity.getDescription(),
            lifeEventEntity.getDate(),
            lifeEventEntity.getPet(),
            lifeEventEntity.getType(),
            lifeEventEntity.getVeterinarian()
        );

        petRepository.findById(lifeEventEntity.getPet().getId()).orElseThrow(
            () -> new EntityNotFoundException("Pet not found"));        
        typeLERepository.findById(lifeEventEntity.getType().getId()).orElseThrow(
            () -> new EntityNotFoundException("Type of life event not found"));
        veterinarianRepository.findById(lifeEventEntity.getVeterinarian().getId()).orElseThrow(
            () -> new EntityNotFoundException("Veterinarian not found"));
        

        log.info("Termina proceso de creación de evento de vida");
        return lifeEventRepository.save(lifeEventEntity);
    }

    @Transactional
	public List<LifeEventEntity> getLifeEvents() {
		log.info("Inicia proceso de consultar todos los eventos de vida");
		return lifeEventRepository.findAll();
	}

    @Transactional
    public LifeEventEntity getLifeEvent(Long lifeEventId) throws EntityNotFoundException {
        log.info("Inicia proceso de consultar el evento de vida con id = {0}", lifeEventId);


        LifeEventEntity lifeEventEntity = lifeEventRepository.findById(lifeEventId).orElseThrow(
            () -> new EntityNotFoundException("Life event not found"));
 
        log.info("Termina proceso de consultar el evento de vida con id = {0}", lifeEventId);
        return lifeEventEntity;
	}

    @Transactional
    public LifeEventEntity updateLifeEvent(Long lifeEventId, LifeEventEntity lifeEvent) throws EntityNotFoundException, IllegalOperationException {

        log.info("Inicia proceso de actualizar el evento de vida con id = {0}", lifeEventId);

        lifeEventRepository.findById(lifeEventId).orElseThrow(
            () -> new EntityNotFoundException("Life event not found"));
     
        validateLifeEvent(
            lifeEvent.getDescription(),
            lifeEvent.getDate(),
            lifeEvent.getPet(),
            lifeEvent.getType(),
            lifeEvent.getVeterinarian()
        );
        
        petRepository.findById(lifeEvent.getPet().getId()).orElseThrow(
            () -> new EntityNotFoundException("Pet not found"));        
        typeLERepository.findById(lifeEvent.getType().getId()).orElseThrow(
            () -> new EntityNotFoundException("Type of life event not found"));
        veterinarianRepository.findById(lifeEvent.getVeterinarian().getId()).orElseThrow(
            () -> new EntityNotFoundException("Veterinarian not found"));

        lifeEvent.setId(lifeEventId);

        log.info("Termina proceso de actualizar el evento de vida con id = {0}", lifeEventId);

        return lifeEventRepository.save(lifeEvent);

    }

    @Transactional
    public void deleteLifeEvent(Long lifeEventId) throws EntityNotFoundException, IllegalOperationException {

        log.info("Inicia proceso de borrar el evento de vida con id = {0}", lifeEventId);

        LifeEventEntity lifeEventEntity = lifeEventRepository.findById(lifeEventId).orElseThrow(
            () -> new EntityNotFoundException("Life event not found"));

        List<LifeEventEntity> events = lifeEventEntity.getPet().getLifeEvents();

        LifeEventEntity firstEvent = events.stream()
                .sorted(Comparator.comparing(LifeEventEntity::getDate))
                .findFirst()
                .get();

        if (firstEvent.getId().equals(lifeEventId)) {
            throw new IllegalOperationException("No se puede borrar el primer LifeEvent de la mascota");
        }

        lifeEventRepository.deleteById(lifeEventId);

        log.info("Termina proceso de borrar el evento de vida con id = {0}", lifeEventId);

    }

    private void validateLifeEvent(String description, LocalDate date, PetEntity pet, TypeLEEntity type, VeterinarianEntity veterinarian) throws IllegalOperationException {    
		
        if(description == null || description.isEmpty()) {
            throw new IllegalOperationException("Life event description is not valid");
        }
        if(date == null) {
            throw new IllegalOperationException("Life event date is not valid");
        }
        if(pet == null) {
            throw new IllegalOperationException("Life event pet is not valid");
        }
        if(type == null) {
            throw new IllegalOperationException("Life event type is not valid");
        }
        if(veterinarian == null) {
            throw new IllegalOperationException("Life event veterinarian is not valid");
        }
	}



}
