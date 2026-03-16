package co.edu.udistrital.mdp.pets.services;

import java.util.List;
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
    private AdoptionRequestRepository adoptionRequestRepository;

    @Autowired
    private PetRepository petRepository;

    @Autowired
    private AdopterRepository adopterRepository;

    @Autowired
    private TrialCohabitationRepository trialCohabitationRepository;

    @Transactional
    public AdoptionRequestEntity createAdoptionRequest(AdoptionRequestEntity adoptionRequestEntity)
            throws EntityNotFoundException, IllegalOperationException {

        log.info("Inicia proceso de creación de solicitud de adopción");

        if (adoptionRequestEntity.getAdopter() == null)
            throw new IllegalOperationException("La solicitud debe tener un adoptante");

        Long adopterId = adoptionRequestEntity.getAdopter().getId();

        if (adopterId == null || !adopterRepository.existsById(adopterId))
            throw new EntityNotFoundException("El adoptante no existe");

        if (adoptionRequestEntity.getPet() == null || adoptionRequestEntity.getPet().getId() == null)
            throw new IllegalOperationException("La solicitud debe incluir una mascota");

        Long petId = adoptionRequestEntity.getPet().getId();

        try {
            petId = Long.parseLong(adoptionRequestEntity.getPet().getId().toString());
        } catch (NumberFormatException e) {
            throw new IllegalOperationException("El id de mascota debe ser numérico");
        }

        if (!petRepository.existsById(petId))
            throw new EntityNotFoundException("La mascota no existe");

        // Validar que no tenga otra solicitud pendiente
        List<AdoptionRequestEntity> requests = adoptionRequestRepository.findByAdopterId(adopterId);

        for (AdoptionRequestEntity r : requests) {
            if ("pendiente".equalsIgnoreCase(r.getStatus()))
                throw new IllegalOperationException("El adoptante ya tiene una solicitud pendiente");
        }

        adoptionRequestEntity.setStatus("pendiente");

        log.info("Termina proceso de creación de solicitud de adopción");

        return adoptionRequestRepository.save(adoptionRequestEntity);
    }

    public AdoptionRequestEntity getAdoptionRequest(Long adoptionRequestId)
            throws EntityNotFoundException {

        log.info("Buscando solicitud de adopción con id={}", adoptionRequestId);

        return adoptionRequestRepository.findById(adoptionRequestId)
                .orElseThrow(() ->
                        new EntityNotFoundException("La solicitud de adopción no existe"));
    }

    public List<AdoptionRequestEntity> getAdoptionRequests() {
        log.info("Consultando todas las solicitudes de adopción");
        return adoptionRequestRepository.findAll();
    }

    @Transactional
    public AdoptionRequestEntity updateAdoptionRequest(Long adoptionRequestId,
                                                       AdoptionRequestEntity adoptionRequestEntity)
            throws EntityNotFoundException {

        log.info("Actualizando solicitud de adopción con id={}", adoptionRequestId);

        AdoptionRequestEntity request = adoptionRequestRepository.findById(adoptionRequestId)
                .orElseThrow(() ->
                        new EntityNotFoundException("La solicitud de adopción no existe"));

        request.setPurpose(adoptionRequestEntity.getPurpose());
        request.setPapers(adoptionRequestEntity.getPapers());
        request.setStatus(adoptionRequestEntity.getStatus());

        return adoptionRequestRepository.save(request);
    }

    @Transactional
    public void deleteAdoptionRequest(Long adoptionRequestId)
            throws EntityNotFoundException, IllegalOperationException {

        log.info("Eliminando solicitud de adopción con id={}", adoptionRequestId);

        AdoptionRequestEntity request = adoptionRequestRepository.findById(adoptionRequestId)
                .orElseThrow(() ->
                        new EntityNotFoundException("La solicitud no existe"));

        AdoptionProcessEntity process = request.getAdoptionProcess();

        if (process != null) {

            Optional<TrialCohabitationEntity> trial =
                    trialCohabitationRepository.findByAdoptionProcess(process);

            if (trial.isPresent())
                throw new IllegalOperationException(
                        "No se puede eliminar la solicitud porque existe una prueba de cohabitación");
        }

        adoptionRequestRepository.deleteById(adoptionRequestId);

        log.info("Solicitud eliminada correctamente");
    }
}