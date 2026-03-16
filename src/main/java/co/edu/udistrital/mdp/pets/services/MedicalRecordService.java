package co.edu.udistrital.mdp.pets.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import co.edu.udistrital.mdp.pets.entities.MedicalRecordEntity;
import co.edu.udistrital.mdp.pets.exceptions.EntityNotFoundException;
import co.edu.udistrital.mdp.pets.exceptions.IllegalOperationException;
import co.edu.udistrital.mdp.pets.repositories.MedicalRecordRepository;

@Service
public class MedicalRecordService {

    @Autowired
    private MedicalRecordRepository repository;

    @Transactional
    public List<MedicalRecordEntity> getMedicalRecords() {
        return repository.findAll();
    }

    @Transactional
    public MedicalRecordEntity getMedicalRecord(Long id) throws EntityNotFoundException {
        return repository.findById(id).orElseThrow(() -> 
            new EntityNotFoundException("El registro médico no fue encontrado"));
    }

    @Transactional
    public MedicalRecordEntity createMedicalRecord(MedicalRecordEntity entity) throws IllegalOperationException {
        if (entity.getDescription() == null || entity.getDiagnosis() == null || entity.getPet() == null) {
            throw new IllegalOperationException("Los atributos obligatorios no deben ser nulos");
        }
        return repository.save(entity);
    }

    @Transactional
    public MedicalRecordEntity updateMedicalRecord(Long id, MedicalRecordEntity entity) 
            throws EntityNotFoundException, IllegalOperationException {
        
        MedicalRecordEntity existing = getMedicalRecord(id);

        if (!existing.getPet().getId().equals(entity.getPet().getId())) {
            throw new IllegalOperationException("No se puede modificar la mascota asociada al historial médico");
        }

        existing.setDescription(entity.getDescription());
        existing.setDiagnosis(entity.getDiagnosis());
        existing.setTreatment(entity.getTreatment());
        
        return repository.save(existing);
    }

    @Transactional
    public void deleteMedicalRecord(Long id) throws EntityNotFoundException, IllegalOperationException {
        MedicalRecordEntity entity = getMedicalRecord(id);

        if (entity.getPet() != null) {
            throw new IllegalOperationException("No se puede eliminar el registro porque tiene una mascota asociada");
        }

        repository.delete(entity);
    }
}
