package co.edu.udistrital.mdp.ZZZ.services;

import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.context.annotation.Import;
import org.springframework.transaction.annotation.Transactional;

import co.edu.udistrital.mdp.pets.entities.AdoptionProcessEntity;
import co.edu.udistrital.mdp.pets.entities.AdoptionRequestEntity;
import co.edu.udistrital.mdp.pets.entities.PetEntity;
import co.edu.udistrital.mdp.pets.entities.AdopterEntity;
import co.edu.udistrital.mdp.pets.entities.VeterinarianEntity;
import co.edu.udistrital.mdp.pets.entities.TrialCohabitationEntity;
import co.edu.udistrital.mdp.pets.exceptions.EntityNotFoundException;
import co.edu.udistrital.mdp.pets.exceptions.IllegalOperationException;
import co.edu.udistrital.mdp.pets.services.TrialCohabitationService;
import uk.co.jemos.podam.api.PodamFactory;
import uk.co.jemos.podam.api.PodamFactoryImpl;

@DataJpaTest
@Transactional
@Import(TrialCohabitationService.class)
public class TrialCohabitationServiceTest {

    @Autowired
    private TrialCohabitationService trialCohabitationService;

    @Autowired
    private TestEntityManager entityManager;

    private final PodamFactory factory = new PodamFactoryImpl();

    private AdoptionProcessEntity approvedProcess;

    @BeforeEach
    void setUp() {
        clearData();

        AdopterEntity adopter = factory.manufacturePojo(AdopterEntity.class);
        PetEntity pet = factory.manufacturePojo(PetEntity.class);
        VeterinarianEntity veterinarian = factory.manufacturePojo(VeterinarianEntity.class);

        entityManager.persist(adopter);
        entityManager.persist(pet);
        entityManager.persist(veterinarian);

        approvedProcess = factory.manufacturePojo(AdoptionProcessEntity.class);
        approvedProcess.setAdopter(adopter);
        approvedProcess.setPet(pet);
        approvedProcess.setVeterinarian(veterinarian);
        approvedProcess.setStatus("aprobado");
        entityManager.persist(approvedProcess);
    }

    void clearData() {
        entityManager.getEntityManager().createQuery("delete from TrialCohabitationEntity").executeUpdate();
        entityManager.getEntityManager().createQuery("delete from AdoptionProcessEntity").executeUpdate();
        entityManager.getEntityManager().createQuery("delete from VeterinarianEntity").executeUpdate();
        entityManager.getEntityManager().createQuery("delete from AdopterEntity").executeUpdate();
        entityManager.getEntityManager().createQuery("delete from PetEntity").executeUpdate();
    }

    @Test
    void testCreateTrialCohabitationRejectsUnapprovedProcess() {
        AdoptionProcessEntity process = factory.manufacturePojo(AdoptionProcessEntity.class);
        process.setAdopter(approvedProcess.getAdopter());
        process.setPet(approvedProcess.getPet());
        process.setStatus("pendiente");
        entityManager.persist(process);

        TrialCohabitationEntity trial = factory.manufacturePojo(TrialCohabitationEntity.class);
        trial.setAdoptionProcess(process);
        trial.setVeterinarian(approvedProcess.getVeterinarian());
        trial.setTrialStarDate(LocalDate.now());
        trial.setTrialEndDate(LocalDate.now().plusDays(7));
        trial.setStatus("abierta");

        IllegalOperationException exception = assertThrows(IllegalOperationException.class, () -> {
            trialCohabitationService.createTrialCohabitation(trial);
        });

        assertTrue(exception.getMessage().contains("debe estar aprobado"));
    }

    @Test
    void testUpdateTrialCohabitationRejectsSameEndDate() throws Exception {
        TrialCohabitationEntity trial = factory.manufacturePojo(TrialCohabitationEntity.class);
        trial.setAdoptionProcess(approvedProcess);
        trial.setVeterinarian(approvedProcess.getVeterinarian());
        trial.setTrialStarDate(LocalDate.now());
        trial.setTrialEndDate(LocalDate.now().plusDays(7));
        trial.setStatus("abierta");
        entityManager.persist(trial);

        TrialCohabitationEntity toUpdate = factory.manufacturePojo(TrialCohabitationEntity.class);
        toUpdate.setAdoptionProcess(approvedProcess);
        toUpdate.setVeterinarian(approvedProcess.getVeterinarian());
        toUpdate.setTrialStarDate(LocalDate.now().plusDays(1));
        toUpdate.setTrialEndDate(trial.getTrialEndDate());
        toUpdate.setStatus("abierta");

        IllegalOperationException exception = assertThrows(IllegalOperationException.class, () -> {
            trialCohabitationService.updateTrialCohabitation(trial.getId(), toUpdate);
        });

        assertTrue(exception.getMessage().contains("fecha final debe ser diferente"));
    }

    @Test
    void testCreateTrialCohabitationSuccess() throws IllegalOperationException {
        TrialCohabitationEntity trial = factory.manufacturePojo(TrialCohabitationEntity.class);
        trial.setAdoptionProcess(approvedProcess);
        trial.setVeterinarian(approvedProcess.getVeterinarian());
        trial.setTrialStarDate(LocalDate.now());
        trial.setTrialEndDate(LocalDate.now().plusDays(7));
        trial.setStatus("abierta");

        TrialCohabitationEntity result = trialCohabitationService.createTrialCohabitation(trial);

        assertNotNull(result);
        assertNotNull(result.getId());
        assertEquals("abierta", result.getStatus().toLowerCase());
    }
}

