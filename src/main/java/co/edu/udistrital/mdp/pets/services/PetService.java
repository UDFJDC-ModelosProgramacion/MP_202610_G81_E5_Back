package co.edu.udistrital.mdp.pets.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import co.edu.udistrital.mdp.pets.entities.PetEntity;
import co.edu.udistrital.mdp.pets.exceptions.EntityNotFoundException;
import co.edu.udistrital.mdp.pets.exceptions.IllegalOperationException;
import co.edu.udistrital.mdp.pets.repositories.AdopterRepository;
import co.edu.udistrital.mdp.pets.repositories.AdoptionProcessRepository;
import co.edu.udistrital.mdp.pets.repositories.PetRepository;
import co.edu.udistrital.mdp.pets.repositories.ShelterRepository;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service

public class PetService {

	@Autowired
	PetRepository petRepository;

    @Autowired
    ShelterRepository shelterRepository;

    @Autowired
    AdopterRepository adopterRepository;

    @Autowired
    AdoptionProcessRepository adoptionProcessRepository;


	@Transactional
    public PetEntity createPet(PetEntity petEntity) throws EntityNotFoundException, IllegalOperationException {

        log.info("Inicia proceso de creación de mascota");

        if (petEntity.getName() == null) {
            throw new IllegalOperationException("Pet name cannot be null");
        }

        if (petEntity.getShelter() == null) {
            throw new IllegalOperationException("Pet shelter cannot be null");
        }

        if (petEntity.getAge() < 0) {
            throw new IllegalOperationException("Pet age must be >= 0");
        }

        shelterRepository.findById(petEntity.getShelter().getId()).orElseThrow(
            () -> new EntityNotFoundException("Pet shelter not found"));

        log.info("Termina proceso de creación de mascota");

        return petRepository.save(petEntity);
    }

    @Transactional
	public List<PetEntity> getPets() {
		log.info("Inicia proceso de consultar todas las mascotas");
		return petRepository.findAll();
	}

    @Transactional
    public PetEntity getPet(Long petId) throws EntityNotFoundException {
        log.info("Inicia proceso de consultar la mascota con id = {0}", petId);


        PetEntity petEntity = petRepository.findById(petId).orElseThrow(
            () -> new EntityNotFoundException("Pet not found"));
 
        log.info("Termina proceso de consultar la mascota con id = {0}", petId);
        return petEntity;
	}

    @Transactional
    public PetEntity updatePet(Long petId, PetEntity pet) throws EntityNotFoundException, IllegalOperationException {

        log.info("Inicia proceso de actualizar la mascota con id = {0}", petId);

        petRepository.findById(petId).orElseThrow(
            () -> new EntityNotFoundException("Pet not found"));
     
        if (pet.getName() == null) {
            throw new IllegalOperationException("Pet name cannot be null");
        }

        if (pet.getAge() < 0) {
            throw new IllegalOperationException("Pet age must be >= 0");
        }
        
        if (pet.getShelter() == null && pet.getAdopter() == null) {
            throw new IllegalOperationException("La mascota debe pertenecer a un refugio o a un adoptante");
        }
        if (pet.getShelter() != null && pet.getAdopter() != null) {
            throw new IllegalOperationException("La mascota no puede pertenecer a un refugio y a un adoptante al mismo tiempo");
        }
        if (pet.getShelter() != null) {
            shelterRepository.findById(pet.getShelter().getId()).orElseThrow(
                () -> new EntityNotFoundException("Pet shelter not found"));
        }
        if (pet.getAdopter() != null) {
            adopterRepository.findById(pet.getAdopter().getId()).orElseThrow(
                () -> new EntityNotFoundException("Pet adopter not found"));
        }
        pet.setId(petId);

        log.info("Termina proceso de actualizar la mascota con id = {0}", petId);

        return petRepository.save(pet);

    }

    @Transactional
    public void deletePet(Long petId) throws EntityNotFoundException, IllegalOperationException {

        log.info("Inicia proceso de borrar la mascota con id = {0}", petId);

        petRepository.findById(petId).orElseThrow(
            () -> new EntityNotFoundException("Pet not found"));

        if (adoptionProcessRepository.existsByPetIdAndStatus(petId, "ACTIVO")) {
            throw new IllegalOperationException(
            "No se puede borrar una mascota con un proceso de adopción activo"
        );
    }
        petRepository.deleteById(petId);

        log.info("Termina proceso de borrar la mascota con id = {0}", petId);

    }

}