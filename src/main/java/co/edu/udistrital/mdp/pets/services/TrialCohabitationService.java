package co.edu.udistrital.mdp.pets.services;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import co.edu.udistrital.mdp.pets.entities.AdoptionProcessEntity;
import co.edu.udistrital.mdp.pets.entities.TrialCohabitationEntity;
import co.edu.udistrital.mdp.pets.exceptions.EntityNotFoundException;
import co.edu.udistrital.mdp.pets.exceptions.IllegalOperationException;
import co.edu.udistrital.mdp.pets.repositories.TrialCohabitationRepository;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class TrialCohabitationService {

    @Autowired
    TrialCohabitationRepository trialCohabitationRepository;

    /**
     * Crea una prueba de cohabitación validada.
     *
     * @param trialCohabitation entidad de prueba de cohabitación con datos de fechas, estado y proceso de adopción
     * @return la prueba de cohabitación guardada en la base de datos
     * @throws IllegalOperationException si la entidad o sus datos no cumplen reglas de negocio
     */
    public TrialCohabitationEntity createTrialCohabitation(TrialCohabitationEntity trialCohabitation) 
            throws IllegalOperationException {
        log.info("Creando prueba de cohabitación");

        if (trialCohabitation == null) {
            throw new IllegalOperationException("La prueba de cohabitación no puede ser nula");
        }

        AdoptionProcessEntity adoptionProcess = trialCohabitation.getAdoptionProcess();
        if (adoptionProcess == null) {
            throw new IllegalOperationException("La prueba de cohabitación debe tener un proceso de adopción");
        }

        String status = adoptionProcess.getStatus();
        if (status == null || !"aprobado".equalsIgnoreCase(status.trim())) {
            throw new IllegalOperationException("El proceso de adopción debe estar aprobado para crear la prueba de cohabitación");
        }

        if (trialCohabitation.getStartDate() == null || trialCohabitation.getEndDate() == null) {
            throw new IllegalOperationException("Las fechas de inicio y fin de la prueba de cohabitación no pueden ser nulas");
        }

        if (trialCohabitation.getStartDate().isAfter(trialCohabitation.getEndDate())) {
            throw new IllegalOperationException("La fecha de inicio no puede ser posterior a la fecha de fin");
        }

        if (trialCohabitation.getStatus() == null || trialCohabitation.getStatus().trim().isEmpty()) {
            throw new IllegalOperationException("El estado de la prueba de cohabitación es obligatorio");
        }

        return trialCohabitationRepository.save(trialCohabitation);
    }

    /**
     * Obtiene una prueba de cohabitación por su id.
     *
     * @param id identificador de la prueba de cohabitación
     * @return la entidad encontrada
     * @throws EntityNotFoundException si no existe la entidad con el id dado
     */
    public TrialCohabitationEntity getTrialCohabitation(Long id) throws EntityNotFoundException {
        log.info("Buscando prueba de cohabitación con id = {}", id);
        Optional<TrialCohabitationEntity> optional = trialCohabitationRepository.findById(id);
        if (optional.isEmpty()) {
            throw new EntityNotFoundException("La prueba de cohabitación con id = " + id + " no existe");
        }
        return optional.get();
    }

    /**
     * Obtiene todas las pruebas de cohabitación registradas.
     *
     * @return lista de entidades de prueba de cohabitación
     */
    public List<TrialCohabitationEntity> getTrialCohabitations() {
        log.info("Buscando todas las pruebas de cohabitación");
        return trialCohabitationRepository.findAll();
    }

    /**
     * Actualiza una prueba de cohabitación existente.
     *
     * @param id identificador de la prueba a actualizar
     * @param trialCohabitation datos nuevos para la prueba de cohabitación
     * @return la entidad actualizada
     * @throws EntityNotFoundException si la prueba no existe
     * @throws IllegalOperationException si la nueva fecha final es igual a la actual o está nula
     */
    @Transactional
    public TrialCohabitationEntity updateTrialCohabitation(Long id, TrialCohabitationEntity trialCohabitation) 
            throws EntityNotFoundException, IllegalOperationException {
        log.info("Actualizando prueba de cohabitación con id = {}", id);
        Optional<TrialCohabitationEntity> optional = trialCohabitationRepository.findById(id);
        if (optional.isEmpty()) {
            throw new EntityNotFoundException("La prueba de cohabitación con id = " + id + " no existe");
        }

        TrialCohabitationEntity existing = optional.get();
        if (trialCohabitation.getEndDate() == null) {
            throw new IllegalOperationException("La fecha final no puede ser nula");
        }

        if (trialCohabitation.getEndDate().equals(existing.getEndDate())) {
            throw new IllegalOperationException("La fecha final debe ser diferente a la actual");
        }

        existing.setAdoptionProcess(trialCohabitation.getAdoptionProcess());
        existing.setVeterinarian(trialCohabitation.getVeterinarian());
        existing.setStartDate(trialCohabitation.getStartDate());
        existing.setEndDate(trialCohabitation.getEndDate());
        existing.setStatus(trialCohabitation.getStatus());

        return trialCohabitationRepository.save(existing);
    }

    /**
     * Elimina una prueba de cohabitación solo si está en estado cerrado.
     *
     * @param id identificador de la prueba de cohabitación
     * @throws EntityNotFoundException si la prueba no existe
     * @throws IllegalOperationException si la prueba no está cerrada
     */
    @Transactional
    public void deleteTrialCohabitation(Long id) throws EntityNotFoundException, IllegalOperationException {
        log.info("Eliminando prueba de cohabitación con id = {}", id);
        if (id == null) {
            throw new EntityNotFoundException("El id no puede ser nulo");
        }

        Optional<TrialCohabitationEntity> optional = trialCohabitationRepository.findById(id);
        if (optional.isEmpty()) {
            throw new EntityNotFoundException("La prueba de cohabitación con id = " + id + " no existe");
        }

        TrialCohabitationEntity existing = optional.get();
        if (existing.getStatus() == null || !"cerrado".equalsIgnoreCase(existing.getStatus().trim())) {
            throw new IllegalOperationException("Solo se puede eliminar una prueba de cohabitación con estado cerrado");
        }

        trialCohabitationRepository.deleteById(id);
    }
}