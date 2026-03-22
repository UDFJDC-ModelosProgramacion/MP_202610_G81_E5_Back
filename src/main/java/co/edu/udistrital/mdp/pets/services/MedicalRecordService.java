package co.edu.udistrital.mdp.pets.services;

import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import co.edu.udistrital.mdp.pets.entities.MedicalRecordEntity;
import co.edu.udistrital.mdp.pets.entities.PetEntity;
import co.edu.udistrital.mdp.pets.entities.VeterinarianEntity;
import co.edu.udistrital.mdp.pets.exceptions.EntityNotFoundException;
import co.edu.udistrital.mdp.pets.exceptions.ErrorMessage;
import co.edu.udistrital.mdp.pets.exceptions.IllegalOperationException;
import co.edu.udistrital.mdp.pets.repositories.MedicalRecordRepository;
import co.edu.udistrital.mdp.pets.repositories.PetRepository;
import co.edu.udistrital.mdp.pets.repositories.VeterinarianRepository;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class MedicalRecordService {

    @Autowired
    private MedicalRecordRepository medicalRecordRepository;

    @Autowired
    private PetRepository petRepository;

    @Autowired
    private VeterinarianRepository veterinarianRepository;

    /**
     * REGLA: Debe existir una mascota registrada y un veterinario válido.
     */
    @Transactional
    public MedicalRecordEntity createMedicalRecord(Long petId, Long veterinarianId, MedicalRecordEntity medicalRecord) throws EntityNotFoundException {
        log.info("Inicia creación de historial médico para mascota id = {}", petId);
        
        PetEntity pet = petRepository.findById(petId)
                .orElseThrow(() -> new EntityNotFoundException(ErrorMessage.PET_NOT_FOUND));

        VeterinarianEntity vet = veterinarianRepository.findById(veterinarianId)
                .orElseThrow(() -> new EntityNotFoundException(ErrorMessage.VETERINARIAN_NOT_FOUND));

        medicalRecord.setPet(pet);
        medicalRecord.setVeterinarian(vet);
        
        return medicalRecordRepository.save(medicalRecord);
    }

    /**
     * REGLA: Solo se pueden modificar diagnóstico y tratamiento 
     */
    @Transactional
    public MedicalRecordEntity updateMedicalRecord(Long recordId, MedicalRecordEntity newInfo) throws EntityNotFoundException {
        log.info("Inicia actualización del registro médico id = {}", recordId);
        
        MedicalRecordEntity currentRecord = medicalRecordRepository.findById(recordId)
                .orElseThrow(() -> new EntityNotFoundException(ErrorMessage.MEDICAL_RECORD_NOT_FOUND));


        currentRecord.setVaccinations(newInfo.getVaccinations());
        currentRecord.setUpcomingDates(newInfo.getUpcomingDates());
        

        log.info("Finaliza actualización del registro médico id = {}", recordId);
        return medicalRecordRepository.save(currentRecord);
    }

    /**
     * REGLA: No se puede eliminar si está asociado a un proceso de adopción activo.
     */
    @Transactional
    public void deleteMedicalRecord(Long recordId) throws EntityNotFoundException, IllegalOperationException {
        log.info("Inicia eliminación del registro médico id = {}", recordId);
        
        MedicalRecordEntity record = medicalRecordRepository.findById(recordId)
                .orElseThrow(() -> new EntityNotFoundException(ErrorMessage.MEDICAL_RECORD_NOT_FOUND));

        PetEntity pet = record.getPet();
        if (pet.getAdoptionProcess() != null && !pet.getAdoptionProcess().isEmpty()) {
          
            throw new IllegalOperationException("No se puede eliminar: La mascota tiene un proceso de adopción en curso.");
        }

        medicalRecordRepository.delete(record);
        log.info("Finaliza eliminación del registro médico id = {}", recordId);
    }

    @Transactional(readOnly = true)
    public List<MedicalRecordEntity> getMedicalRecordsByPet(Long petId) throws EntityNotFoundException {
        return petRepository.findById(petId)
                .map(PetEntity::getMedicalRecords)
                .orElseThrow(() -> new EntityNotFoundException(ErrorMessage.PET_NOT_FOUND));
    }
}
