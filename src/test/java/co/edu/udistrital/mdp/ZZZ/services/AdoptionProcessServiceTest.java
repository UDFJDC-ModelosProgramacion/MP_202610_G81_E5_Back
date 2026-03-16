package co.edu.udistrital.mdp.ZZZ.services;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.List;

import jakarta.transaction.Transactional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ContextConfiguration;

import co.edu.udistrital.mdp.pets.MainApplication;
import co.edu.udistrital.mdp.pets.entities.AdopterEntity;
import co.edu.udistrital.mdp.pets.entities.AdoptionProcessEntity;
import co.edu.udistrital.mdp.pets.entities.AdoptionRequestEntity;
import co.edu.udistrital.mdp.pets.entities.PetEntity;
import co.edu.udistrital.mdp.pets.entities.VeterinarianEntity;
import co.edu.udistrital.mdp.pets.exceptions.EntityNotFoundException;
import co.edu.udistrital.mdp.pets.exceptions.IllegalOperationException;
import co.edu.udistrital.mdp.pets.services.AdoptionProcessService;

import uk.co.jemos.podam.api.PodamFactory;
import uk.co.jemos.podam.api.PodamFactoryImpl;


@DataJpaTest
@Transactional
@ContextConfiguration(classes = MainApplication.class)
@Import(AdoptionProcessService.class)
class AdoptionProcessServiceTest {

    @Autowired
    private AdoptionProcessService adoptionProcessService;

    @Autowired
    private TestEntityManager entityManager;

    private PodamFactory factory = new PodamFactoryImpl();

    private List<AdoptionProcessEntity> processList = new ArrayList<>();

    private List<AdoptionRequestEntity> requestList = new ArrayList<>();

    private AdopterEntity adopter;
    private PetEntity pet;
    private VeterinarianEntity vet;

    @BeforeEach
    void setUp() {
        clearData();
        insertData();
    }

    private void clearData() {
        entityManager.getEntityManager().createQuery("delete from AdoptionProcessEntity").executeUpdate();
        entityManager.getEntityManager().createQuery("delete from AdoptionRequestEntity").executeUpdate();
        entityManager.getEntityManager().createQuery("delete from AdopterEntity").executeUpdate();
        entityManager.getEntityManager().createQuery("delete from PetEntity").executeUpdate();
        entityManager.getEntityManager().createQuery("delete from VeterinarianEntity").executeUpdate();
    }

    private void insertData() {

        adopter = factory.manufacturePojo(AdopterEntity.class);
        entityManager.persist(adopter);

        pet = factory.manufacturePojo(PetEntity.class);
        entityManager.persist(pet);

        vet = factory.manufacturePojo(VeterinarianEntity.class);
        entityManager.persist(vet);

        for (int i = 0; i < 3; i++) {

            AdoptionRequestEntity request = factory.manufacturePojo(AdoptionRequestEntity.class);
            request.setAdopter(adopter);
            request.setStatus("aprobado");

            entityManager.persist(request);
            requestList.add(request);

            AdoptionProcessEntity process = factory.manufacturePojo(AdoptionProcessEntity.class);
            process.setAdopter(adopter);
            process.setPet(pet);
            process.setVeterinarian(vet);
            process.setRequest(request);
            process.setStatus("activo");

            entityManager.persist(process);
            processList.add(process);
        }
    }

    /**
     * Prueba para crear un proceso de adopción
     */
    @Test
    void testCreateAdoptionProcess() throws IllegalOperationException {
        AdoptionProcessEntity newEntity = factory.manufacturePojo(AdoptionProcessEntity.class);

        // crear veterinario nuevo
        VeterinarianEntity vet = factory.manufacturePojo(VeterinarianEntity.class);
        entityManager.persist(vet);

        // crear adoptante
        AdopterEntity adopter = factory.manufacturePojo(AdopterEntity.class);
        entityManager.persist(adopter);

        // crear mascota
        PetEntity pet = factory.manufacturePojo(PetEntity.class);
        entityManager.persist(pet);

        // crear request aprobada
        AdoptionRequestEntity request = factory.manufacturePojo(AdoptionRequestEntity.class);
        request.setAdopter(adopter);
        request.setStatus("aprobado");
        entityManager.persist(request);

        newEntity.setVeterinarian(vet);
        newEntity.setAdopter(adopter);
        newEntity.setPet(pet);
        newEntity.setRequest(request);

        AdoptionProcessEntity result = adoptionProcessService.createAdoptionProcess(newEntity);

        assertNotNull(result);

        AdoptionProcessEntity entity = entityManager.find(AdoptionProcessEntity.class, result.getId());

        assertEquals(newEntity.getStatus(), entity.getStatus());
        assertEquals(vet.getId(), entity.getVeterinarian().getId());
        assertEquals(request.getId(), entity.getRequest().getId());
    }
    /**
     * Prueba para consultar todos los procesos
     */
    @Test
    void testGetAdoptionProcesses() {

        List<AdoptionProcessEntity> list = adoptionProcessService.getAdoptionProcesses();
        assertEquals(processList.size(), list.size());
    }

    /**
     * Prueba para consultar un proceso
     */
    @Test
    void testGetAdoptionProcess() throws EntityNotFoundException {

        AdoptionProcessEntity entity = processList.get(0);

        AdoptionProcessEntity result = adoptionProcessService.getAdoptionProcess(entity.getId());

        assertNotNull(result);
        assertEquals(entity.getId(), result.getId());
    }

    /**
     * Prueba para consultar un proceso que no existe
     */
    @Test
    void testGetInvalidAdoptionProcess() {

        assertThrows(EntityNotFoundException.class, () -> {
            adoptionProcessService.getAdoptionProcess(0L);
        });
    }

    /**
     * Prueba para actualizar un proceso
     */
    @Test
    void testUpdateAdoptionProcess() throws EntityNotFoundException, IllegalOperationException {

        AdoptionProcessEntity entity = processList.get(0);

        AdoptionProcessEntity pojo = factory.manufacturePojo(AdoptionProcessEntity.class);
        pojo.setId(entity.getId());
        pojo.setAdopter(adopter);
        pojo.setPet(pet);
        pojo.setVeterinarian(vet);
        pojo.setRequest(entity.getRequest());
        pojo.setStatus("cerrado");

        adoptionProcessService.updateAdoptionProcess(entity.getId(), pojo);

        AdoptionProcessEntity response = entityManager.find(AdoptionProcessEntity.class, entity.getId());

        assertEquals(pojo.getStatus(), response.getStatus());
    }

    /**
     * Prueba para eliminar un proceso
     */
    @Test
    void testDeleteAdoptionProcess() throws EntityNotFoundException, IllegalOperationException {

        AdoptionProcessEntity entity = processList.get(0);
        entity.setStatus("cerrado");

        entityManager.persist(entity);

        adoptionProcessService.deleteAdoptionProcess(entity.getId());

        AdoptionProcessEntity deleted = entityManager.find(AdoptionProcessEntity.class, entity.getId());

        assertNull(deleted);
    }
}