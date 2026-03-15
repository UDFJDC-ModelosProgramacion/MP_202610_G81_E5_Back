package co.edu.udistrital.mdp.ZZZ.services;

import static org.junit.jupiter.api.Assertions.*;

import java.util.List;
import java.util.ArrayList;
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
import co.edu.udistrital.mdp.pets.entities.AdopterEntity;
import co.edu.udistrital.mdp.pets.entities.PetEntity;
import co.edu.udistrital.mdp.pets.entities.VeterinarianEntity;
import co.edu.udistrital.mdp.pets.exceptions.EntityNotFoundException;
import co.edu.udistrital.mdp.pets.exceptions.IllegalOperationException;
import co.edu.udistrital.mdp.pets.services.AdoptionProcessService;
import co.edu.udistrital.mdp.pets.services.AdoptionRequestService;
import org.springframework.beans.BeanUtils;
import uk.co.jemos.podam.api.PodamFactory;
import uk.co.jemos.podam.api.PodamFactoryImpl;

@DataJpaTest
@Transactional
@Import(AdoptionProcessService.class)
public class AdoptionProcessServiceTest {

    @Autowired
    private AdoptionProcessService adoptionProcessService;

    @Autowired
    private TestEntityManager entityManager;

    private final PodamFactory factory = new PodamFactoryImpl();

    private AdoptionRequestEntity approvedRequest;
    private AdopterEntity adopter;
    private PetEntity pet;
    private VeterinarianEntity veterinarian;

    @BeforeEach
    void setUp() {
        clearData();

        adopter = factory.manufacturePojo(AdopterEntity.class);
        pet = factory.manufacturePojo(PetEntity.class);
        veterinarian = factory.manufacturePojo(VeterinarianEntity.class);

        entityManager.persist(adopter);
        entityManager.persist(pet);
        entityManager.persist(veterinarian);

        approvedRequest = factory.manufacturePojo(AdoptionRequestEntity.class);
        approvedRequest.setAdopter(adopter);
        approvedRequest.setIdPet(String.valueOf(pet.getId()));
        entityManager.persist(approvedRequest);
    }

    public void clearData() {
        entityManager.getEntityManager().createQuery("delete from AdoptionProcessEntity").executeUpdate();
        entityManager.getEntityManager().createQuery("delete from AdoptionRequestEntity").executeUpdate();
        entityManager.getEntityManager().createQuery("delete from VeterinarianEntity").executeUpdate();
        entityManager.getEntityManager().createQuery("delete from AdopterEntity").executeUpdate();
        entityManager.getEntityManager().createQuery("delete from PetEntity").executeUpdate();
    }

    @Test
    void testCreateAdoptionProcessWithUsedVeterinarianThrows() {
        AdoptionProcessEntity existing = factory.manufacturePojo(AdoptionProcessEntity.class);
        existing.setAdopter(adopter);
        existing.setPet(pet);
        existing.setRequest(approvedRequest);
        existing.setVeterinarian(veterinarian);
        existing.setStatus("cerrado");
        entityManager.persist(existing);

        AdoptionProcessEntity newProcess = factory.manufacturePojo(AdoptionProcessEntity.class);
        newProcess.setAdopter(adopter);
        newProcess.setPet(pet);
        newProcess.setRequest(approvedRequest);
        newProcess.setVeterinarian(veterinarian);
        newProcess.setStatus("aprobado");

        IllegalOperationException exception = assertThrows(IllegalOperationException.class, () -> {
            adoptionProcessService.createAdoptionProcess(newProcess);
        });

        assertTrue(exception.getMessage().contains("veterinario asignado ya tiene otro proceso"));
    }

    @Test
    void testUpdateAdoptionProcessStatusMustChange() throws Exception {
        AdoptionProcessEntity saved = factory.manufacturePojo(AdoptionProcessEntity.class);
        saved.setAdopter(adopter);
        saved.setPet(pet);
        saved.setRequest(approvedRequest);
        saved.setVeterinarian(veterinarian);
        saved.setStatus("aprobado");
        entityManager.persist(saved);

        AdoptionProcessEntity update = factory.manufacturePojo(AdoptionProcessEntity.class);
        update.setAdopter(adopter);
        update.setPet(pet);
        update.setRequest(approvedRequest);
        update.setVeterinarian(veterinarian);
        update.setStatus("aprobado");

        IllegalOperationException exception = assertThrows(IllegalOperationException.class, () -> {
            adoptionProcessService.updateAdoptionProcess(saved.getId(), update);
        });

        assertTrue(exception.getMessage().contains("nuevo estado debe ser diferente"));
    }

    @Test
    void testCreateAdoptionProcessSuccess() throws IllegalOperationException {
        AdoptionProcessEntity newProcess = factory.manufacturePojo(AdoptionProcessEntity.class);
        newProcess.setAdopter(adopter);
        newProcess.setPet(pet);
        newProcess.setVeterinarian(veterinarian);
        newProcess.setRequest(approvedRequest);
        newProcess.setStatus("aprobado");

        AdoptionProcessEntity result = adoptionProcessService.createAdoptionProcess(newProcess);

        assertNotNull(result);
        assertNotNull(result.getId());
        assertEquals("aprobado", result.getStatus().toLowerCase());
    }
}

