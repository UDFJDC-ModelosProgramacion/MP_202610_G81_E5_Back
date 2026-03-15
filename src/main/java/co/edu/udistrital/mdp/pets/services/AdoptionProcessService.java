package co.edu.udistrital.mdp.pets.services;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import co.edu.udistrital.mdp.pets.entities.AdoptionProcessEntity;
import co.edu.udistrital.mdp.pets.exceptions.EntityNotFoundException;
import co.edu.udistrital.mdp.pets.exceptions.IllegalOperationException;
import co.edu.udistrital.mdp.pets.repositories.AdoptionProcessRepository;
import co.edu.udistrital.mdp.pets.repositories.TrialCohabitationRepository;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class AdoptionProcessService {

    @Autowired
    AdoptionProcessRepository adoptionProcessRepository;

    @Autowired
    TrialCohabitationRepository trialCohabitationRepository;

    /**
     * Crea un proceso de adopción validando que no tenga valores nulos
     * y que la request asociada exista y esté aprobada.
     *
     * @param adoptionProcess proceso de adopción a crear
     * @return proceso creado persistido
     * @throws IllegalOperationException si algún valor requerido es nulo o la request no es aprobada
     */
    public AdoptionProcessEntity createAdoptionProcess(AdoptionProcessEntity adoptionProcess) throws IllegalOperationException {
        log.info("Creando proceso de adopción");

        if (adoptionProcess == null) {
            throw new IllegalOperationException("El proceso de adopción no puede ser nulo");
        }
        if (adoptionProcess.getAdopter() == null) {
            throw new IllegalOperationException("El adoptante del proceso no puede ser nulo");
        }
        if (adoptionProcess.getPet() == null) {
            throw new IllegalOperationException("La mascota del proceso no puede ser nula");
        }
        if (adoptionProcess.getRequest() == null) {
            throw new IllegalOperationException("La request del proceso no puede ser nula");
        }

        if (adoptionProcess.getVeterinarian() == null) {
            throw new IllegalOperationException("El veterinario asignado no puede ser nulo");
        }
        if (adoptionProcessRepository.findByVeterinarianId(adoptionProcess.getVeterinarian().getId()).isPresent()) {
            throw new IllegalOperationException("El veterinario asignado ya tiene otro proceso de adopción");
        }
        if (adoptionProcess.getStatus() == null || adoptionProcess.getStatus().trim().isEmpty()) {
            throw new IllegalOperationException("El estado del proceso no puede ser nulo o vacío");
        }

        return adoptionProcessRepository.save(adoptionProcess);
    }

    public AdoptionProcessEntity getAdoptionProcess(Long id) throws EntityNotFoundException {
        log.info("Buscando proceso de adopción con id = {}", id);
        Optional<AdoptionProcessEntity> optional = adoptionProcessRepository.findById(id);
        if (optional.isEmpty()) {
            throw new EntityNotFoundException("El proceso de adopción con id = " + id + " no existe");
        }
        return optional.get();
    }

    public List<AdoptionProcessEntity> getAdoptionProcesses() {
        log.info("Buscando todos los procesos de adopción");
        return adoptionProcessRepository.findAll();
    }

    @Transactional
    public AdoptionProcessEntity updateAdoptionProcess(Long id, AdoptionProcessEntity adoptionProcess)
            throws EntityNotFoundException, IllegalOperationException {
        log.info("Actualizando proceso de adopción con id = {}", id);
        Optional<AdoptionProcessEntity> optional = adoptionProcessRepository.findById(id);
        if (optional.isEmpty()) {
            throw new EntityNotFoundException("El proceso de adopción con id = " + id + " no existe");
        }

        AdoptionProcessEntity existing = optional.get();

        if (adoptionProcess == null) {
            throw new IllegalOperationException("Los datos de actualización no pueden ser nulos");
        }
        if (adoptionProcess.getAdopter() == null) {
            throw new IllegalOperationException("El adoptante del proceso no puede ser nulo");
        }
        if (adoptionProcess.getPet() == null) {
            throw new IllegalOperationException("La mascota del proceso no puede ser nula");
        }
        if (adoptionProcess.getRequest() == null) {
            throw new IllegalOperationException("La request del proceso no puede ser nula");
        }
        if (adoptionProcess.getStatus() == null || adoptionProcess.getStatus().trim().isEmpty()) {
            throw new IllegalOperationException("El nuevo estado no puede ser nulo ni vacío");
        }

        if (existing.getStatus() != null && existing.getStatus().equalsIgnoreCase(adoptionProcess.getStatus().trim())) {
            throw new IllegalOperationException("El nuevo estado debe ser diferente al antiguo");
        }

        existing.setAdopter(adoptionProcess.getAdopter());
        existing.setPet(adoptionProcess.getPet());
        existing.setRequest(adoptionProcess.getRequest());
        existing.setVeterinarian(adoptionProcess.getVeterinarian());
        existing.setRequestDate(adoptionProcess.getRequestDate());
        existing.setStatus(adoptionProcess.getStatus());

        return adoptionProcessRepository.save(existing);
    }

    @Transactional
    public void deleteAdoptionProcess(Long id) throws EntityNotFoundException, IllegalOperationException {
        log.info("Eliminando proceso de adopción con id = {}", id);
        if (id == null) {
            throw new EntityNotFoundException("El proceso de adopción no puede ser nulo");
        }

        Optional<AdoptionProcessEntity> optional = adoptionProcessRepository.findById(id);
        if (optional.isEmpty()) {
            throw new EntityNotFoundException("El proceso de adopción con id = " + id + " no existe");
        }

        AdoptionProcessEntity existing = optional.get();
        if (existing.getStatus() == null || !"cerrado".equalsIgnoreCase(existing.getStatus().trim())) {
            throw new IllegalOperationException("Solo se puede eliminar un proceso de adopción con estado cerrado");
        }

        if (!trialCohabitationRepository.findByAdoptionProcess(existing).isEmpty()) {
            throw new IllegalOperationException("No se puede eliminar el proceso de adopción porque hay una prueba de cohabitación asociada");
        }

        adoptionProcessRepository.deleteById(id);
    }
}