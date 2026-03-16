package co.edu.udistrital.mdp.pets.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import co.edu.udistrital.mdp.pets.entities.AdoptionProcessEntity;
import co.edu.udistrital.mdp.pets.entities.AdoptionRequestEntity;
import co.edu.udistrital.mdp.pets.exceptions.EntityNotFoundException;
import co.edu.udistrital.mdp.pets.exceptions.IllegalOperationException;
import co.edu.udistrital.mdp.pets.repositories.AdoptionProcessRepository;
import co.edu.udistrital.mdp.pets.repositories.AdoptionRequestRepository;
import co.edu.udistrital.mdp.pets.repositories.TrialCohabitationRepository;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class AdoptionProcessService {

    @Autowired
    private AdoptionProcessRepository adoptionProcessRepository;

    @Autowired
    private AdoptionRequestRepository adoptionRequestRepository;

    @Autowired
    private TrialCohabitationRepository trialCohabitationRepository;

    /**
     * Crea un proceso de adopción.
     * Para crearlo la solicitud de adopción debe existir y estar aprobada.
     */
    public AdoptionProcessEntity createAdoptionProcess(AdoptionProcessEntity adoptionProcess)
            throws IllegalOperationException {

        log.info("Creando proceso de adopción");

        if (adoptionProcess == null) {
            throw new IllegalOperationException("El proceso de adopción no puede ser nulo");
        }

        if (adoptionProcess.getAdopter() == null) {
            throw new IllegalOperationException("El adoptante no puede ser nulo");
        }

        if (adoptionProcess.getPet() == null) {
            throw new IllegalOperationException("La mascota no puede ser nula");
        }

        if (adoptionProcess.getVeterinarian() == null) {
            throw new IllegalOperationException("El veterinario no puede ser nulo");
        }

        if (adoptionProcess.getRequest() == null || adoptionProcess.getRequest().getId() == null) {
            throw new IllegalOperationException("La solicitud de adopción no puede ser nula");
        }

        // Buscar la request real en la base de datos
        AdoptionRequestEntity request = adoptionRequestRepository
                .findById(adoptionProcess.getRequest().getId())
                .orElseThrow(() -> new IllegalOperationException("La solicitud de adopción no existe"));

        // Validar que esté aprobada
        if (request.getStatus() == null ||
            !"aprobado".equalsIgnoreCase(request.getStatus().trim())) {
            throw new IllegalOperationException(
                    "La solicitud debe estar aprobada para iniciar el proceso de adopción");
        }

        // Verificar que el veterinario no tenga otro proceso activo
        if (adoptionProcessRepository.existsByVeterinarian(adoptionProcess.getVeterinarian())) {
            throw new IllegalOperationException(
                    "El veterinario ya tiene otro proceso de adopción asignado");
        }

        if (adoptionProcess.getStatus() == null || adoptionProcess.getStatus().trim().isEmpty()) {
            throw new IllegalOperationException("El estado del proceso no puede ser nulo o vacío");
        }

        adoptionProcess.setRequest(request);

        return adoptionProcessRepository.save(adoptionProcess);
    }

    /**
     * Retorna un proceso de adopción por id
     */
    public AdoptionProcessEntity getAdoptionProcess(Long id) throws EntityNotFoundException {

        log.info("Buscando proceso de adopción con id = {}", id);

        return adoptionProcessRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(
                        "El proceso de adopción con id = " + id + " no existe"));
    }

    /**
     * Retorna todos los procesos de adopción
     */
    public List<AdoptionProcessEntity> getAdoptionProcesses() {

        log.info("Buscando todos los procesos de adopción");

        return adoptionProcessRepository.findAll();
    }

    /**
     * Actualiza un proceso de adopción
     */
    @Transactional
    public AdoptionProcessEntity updateAdoptionProcess(Long id, AdoptionProcessEntity adoptionProcess)
            throws EntityNotFoundException, IllegalOperationException {

        log.info("Actualizando proceso de adopción con id = {}", id);

        AdoptionProcessEntity existing = adoptionProcessRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(
                        "El proceso de adopción con id = " + id + " no existe"));

        if (adoptionProcess == null) {
            throw new IllegalOperationException("Los datos de actualización no pueden ser nulos");
        }

        if (adoptionProcess.getStatus() == null || adoptionProcess.getStatus().trim().isEmpty()) {
            throw new IllegalOperationException("El nuevo estado no puede ser nulo ni vacío");
        }

        if (existing.getStatus() != null &&
                existing.getStatus().equalsIgnoreCase(adoptionProcess.getStatus().trim())) {
            throw new IllegalOperationException(
                    "El nuevo estado debe ser diferente al anterior");
        }

        existing.setAdopter(adoptionProcess.getAdopter());
        existing.setPet(adoptionProcess.getPet());
        existing.setVeterinarian(adoptionProcess.getVeterinarian());
        existing.setRequestDate(adoptionProcess.getRequestDate());
        existing.setStatus(adoptionProcess.getStatus());

        return adoptionProcessRepository.save(existing);
    }

    /**
     * Elimina un proceso de adopción
     */
    @Transactional
    public void deleteAdoptionProcess(Long id)
            throws EntityNotFoundException, IllegalOperationException {

        log.info("Eliminando proceso de adopción con id = {}", id);

        AdoptionProcessEntity existing = adoptionProcessRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(
                        "El proceso de adopción con id = " + id + " no existe"));

        if (existing.getStatus() == null ||
                !"cerrado".equalsIgnoreCase(existing.getStatus().trim())) {
            throw new IllegalOperationException(
                    "Solo se puede eliminar un proceso de adopción con estado cerrado");
        }

        if (!trialCohabitationRepository.findByAdoptionProcess(existing).isEmpty()) {
            throw new IllegalOperationException(
                    "No se puede eliminar porque existe una prueba de cohabitación asociada");
        }

        adoptionProcessRepository.deleteById(id);
    }
}