package co.edu.udistrital.mdp.pets.services;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import co.edu.udistrital.mdp.pets.entities.AdoptionProcessEntity;
import co.edu.udistrital.mdp.pets.entities.AdoptionRequestEntity;
import co.edu.udistrital.mdp.pets.entities.TrialCohabitationEntity;
import co.edu.udistrital.mdp.pets.exceptions.EntityNotFoundException;
import co.edu.udistrital.mdp.pets.exceptions.IllegalOperationException;
import co.edu.udistrital.mdp.pets.repositories.AdopterRepository;
import co.edu.udistrital.mdp.pets.repositories.AdoptionRequestRepository;
import co.edu.udistrital.mdp.pets.repositories.PetRepository;
import co.edu.udistrital.mdp.pets.repositories.TrialCohabitationRepository;
import lombok.extern.slf4j.Slf4j;

@Slf4j

@Service

public class AdoptionRequestService {

    @Autowired
    AdoptionRequestRepository adoptionRequestRepository;

    @Autowired
    TrialCohabitationRepository trialCohabitationRepository;

    @Autowired
    PetRepository petRepository;

    @Autowired
    AdopterRepository adopterRepository;

    /**
     * Crea una nueva solicitud de adopción con validación de reglas de negocio.
     * 
     * Valida que:
     * - El adoptante exista y no sea nulo
     * - La mascota exista y su ID sea válido
     * - El adoptante no tenga otra solicitud activa
     * - El proceso de adopción no sea nulo y no esté finalizado
     * 
     * @param adoptionRequestEntity la solicitud de adopción a crear con los datos del adoptante, mascota y proceso
     * @return la solicitud de adopción creada y persistida en la base de datos
     * @throws EntityNotFoundException si el adoptante o mascota no existen
     * @throws IllegalOperationException si incumple las reglas de negocio establecidas
     */
    
    @Transactional
    public AdoptionRequestEntity createAdoptionRequest(AdoptionRequestEntity adoptionRequestEntity) 
    throws EntityNotFoundException, IllegalOperationException{

        log.info("Inicia proceso de creación de una solicitud de adopción");

        if(adoptionRequestEntity.getAdopter() == null)
            throw new IllegalOperationException("La solicitud de adopción debe estar asociada a un adoptante");

        if(!adopterRepository.existsById(Objects.requireNonNull(adoptionRequestEntity.getAdopter().getId(), "El ID del adoptante no puede ser nulo")))
            throw new EntityNotFoundException("El adoptante con id = " + adoptionRequestEntity.getAdopter().getId() + " no existe");

        if(adoptionRequestEntity.getIdPet() == null || adoptionRequestEntity.getIdPet().trim().isEmpty())
            throw new IllegalOperationException("La solicitud de adopción debe incluir una mascota válida");

        Long petId;
        try {
            petId = Long.parseLong(adoptionRequestEntity.getIdPet());
        } catch (NumberFormatException e) {
            throw new IllegalOperationException("El id de mascota debe ser un número válido");
        }

        if(!petRepository.existsById(Objects.requireNonNull(petId, "El ID de la mascota no puede ser nulo")))
            throw new EntityNotFoundException("La mascota con id = " + petId + " no existe");

        if(!adoptionRequestRepository.findByAdopterId(adoptionRequestEntity.getAdopter().getId()).isEmpty())
            throw new IllegalOperationException("Ya existe una solicitud de adopción para el adoptante con id = " + adoptionRequestEntity.getAdopter().getId());

        if(adoptionRequestEntity.getAdoptionProcess() == null)
            throw new IllegalOperationException("La solicitud de adopción debe estar asociada a un proceso de adopción");

        if(adoptionRequestEntity.getAdoptionProcess().getStatus().equals("Finalizado"))
            throw new IllegalOperationException("No se pueden crear solicitudes de adopción para procesos de adopción finalizados");

        log.info("Termina proceso de creación de solicitud de adopción");
        
        return adoptionRequestRepository.save(adoptionRequestEntity);
    }

    /**
     * Obtiene una solicitud de adopción por su identificador.
     * 
     * @param adoptionRequestId el identificador de la solicitud de adopción a obtener
     * @return la solicitud de adopción encontrada
     * @throws EntityNotFoundException si la solicitud de adopción no existe
     */
    public AdoptionRequestEntity getAdoptionRequest(Long adoptionRequestId) 
    throws EntityNotFoundException {

        log.info("Inicia búsqueda de solicitud de adopción con id = {}", adoptionRequestId);

        Optional<AdoptionRequestEntity> adoptionRequest = adoptionRequestRepository.findById(
            Objects.requireNonNull(adoptionRequestId, "El ID de la solicitud no puede ser nulo"));

        if(!adoptionRequest.isPresent())
            throw new EntityNotFoundException("La solicitud de adopción con id = " + adoptionRequestId + " no existe");

        log.info("Termina búsqueda de solicitud de adopción con id = {}", adoptionRequestId);

        return adoptionRequest.get();
    }

    /**
     * Obtiene todas las solicitudes de adopción registradas en el sistema.
     * 
     * @return lista de todas las solicitudes de adopción
     */
    public List<AdoptionRequestEntity> getAdoptionRequests() {

        log.info("Inicia búsqueda de todas las solicitudes de adopción");

        List<AdoptionRequestEntity> adoptionRequests = adoptionRequestRepository.findAll();

        log.info("Termina búsqueda de todas las solicitudes de adopción. Total encontradas: {}", adoptionRequests.size());

        return adoptionRequests;
    }

    /**
     * Actualiza una solicitud de adopción existente con nuevos datos.
     * 
     * Valida que:
     * - La solicitud exista en la base de datos
     * - El adoptante exista y no sea nulo
     * - La mascota exista y su ID sea válido
     * - El proceso de adopción no sea nulo y no esté finalizado
     * 
     * @param adoptionRequestId el identificador de la solicitud de adopción a actualizar
     * @param adoptionRequestEntity los nuevos datos de la solicitud de adopción
     * @return la solicitud de adopción actualizada
     * @throws EntityNotFoundException si la solicitud, adoptante o mascota no existen
     * @throws IllegalOperationException si incumple las reglas de negocio establecidas
     */
    @Transactional
    public AdoptionRequestEntity updateAdoptionRequest(Long adoptionRequestId, AdoptionRequestEntity adoptionRequestEntity) 
    throws EntityNotFoundException, IllegalOperationException {

        log.info("Inicia proceso de actualización de solicitud de adopción con id = {}", adoptionRequestId);

        Optional<AdoptionRequestEntity> existingAdoptionRequest = adoptionRequestRepository.findById(
            Objects.requireNonNull(adoptionRequestId, "El ID de la solicitud no puede ser nulo"));

        if(!existingAdoptionRequest.isPresent())
            throw new EntityNotFoundException("La solicitud de adopción con id = " + adoptionRequestId + " no existe");

        AdoptionRequestEntity adoptionRequest = existingAdoptionRequest.get();

        if(adoptionRequestEntity.getAdopter() == null)
            throw new IllegalOperationException("La solicitud de adopción debe estar asociada a un adoptante");

        if(!adopterRepository.existsById(Objects.requireNonNull(adoptionRequestEntity.getAdopter().getId(), "El ID del adoptante no puede ser nulo")))
            throw new EntityNotFoundException("El adoptante con id = " + adoptionRequestEntity.getAdopter().getId() + " no existe");

        if(adoptionRequestEntity.getIdPet() == null || adoptionRequestEntity.getIdPet().trim().isEmpty())
            throw new IllegalOperationException("La solicitud de adopción debe incluir una mascota válida");

        Long petId;
        try {
            petId = Long.parseLong(adoptionRequestEntity.getIdPet());
        } catch (NumberFormatException e) {
            throw new IllegalOperationException("El id de mascota debe ser un número válido");
        }

        if(!petRepository.existsById(Objects.requireNonNull(petId, "El ID de la mascota no puede ser nulo")))
            throw new EntityNotFoundException("La mascota con id = " + petId + " no existe");

        if(adoptionRequestEntity.getAdoptionProcess() == null)
            throw new IllegalOperationException("La solicitud de adopción debe estar asociada a un proceso de adopción");

        if(adoptionRequestEntity.getAdoptionProcess().getStatus().equals("Finalizado"))
            throw new IllegalOperationException("No se pueden actualizar solicitudes de adopción para procesos de adopción finalizados");

        adoptionRequest.setAdopter(adoptionRequestEntity.getAdopter());
        adoptionRequest.setIdPet(adoptionRequestEntity.getIdPet());
        adoptionRequest.setAdoptionProcess(adoptionRequestEntity.getAdoptionProcess());
        adoptionRequest.setPurpose(adoptionRequestEntity.getPurpose());
        adoptionRequest.setPapers(adoptionRequestEntity.getPapers());

        log.info("Termina proceso de actualización de solicitud de adopción con id = {}", adoptionRequestId);

        return adoptionRequestRepository.save(adoptionRequest);
    }

    /**
     * Elimina una solicitud de adopción existente con validaciones.
     * 
     * Valida que:
     * - La solicitud exista en la base de datos
     * - La solicitud tenga un proceso de adopción asociado
     * - No exista una prueba de cohabitación vinculada al proceso
     * 
     * @param adoptionRequestId el identificador de la solicitud de adopción a eliminar
     * @throws EntityNotFoundException si la solicitud de adopción no existe
     * @throws IllegalOperationException si la solicitud no tiene proceso asociado o existe prueba de cohabitación vinculada
     */

    @Transactional
    public void deleteAdoptionRequest(Long adoptionRequestId) 
    throws EntityNotFoundException, IllegalOperationException{

        log.info("Inicia proceso de eliminación de solicitud de adopción con id = {}", adoptionRequestId);

        Optional<AdoptionRequestEntity> adoptionRequest = adoptionRequestRepository.findById(
            Objects.requireNonNull(adoptionRequestId, "El ID de la solicitud no puede ser nulo"));

        if(!adoptionRequest.isPresent())
            throw new EntityNotFoundException("La solicitud de adopción con id = " + adoptionRequestId + " no existe");

        AdoptionRequestEntity adoptionRequestEntity = adoptionRequest.get();
        AdoptionProcessEntity adoptionProcess = adoptionRequestEntity.getAdoptionProcess();

        if(adoptionProcess == null)
            throw new IllegalOperationException("La solicitud de adopción no tiene un proceso de adopción asociado");

        Optional<TrialCohabitationEntity> trialCohabitation = trialCohabitationRepository.findByAdoptionProcess(adoptionProcess);

        if(trialCohabitation.isPresent()){
            throw new IllegalOperationException("No se puede eliminar la solicitud de adopción porque existe una prueba de cohabitación vinculada al proceso de adopción");
        }

        adoptionRequestRepository.deleteById(adoptionRequestId);

        log.info("Termina proceso de eliminación de solicitud de adopción con id = {}", adoptionRequestId);
    }
}
