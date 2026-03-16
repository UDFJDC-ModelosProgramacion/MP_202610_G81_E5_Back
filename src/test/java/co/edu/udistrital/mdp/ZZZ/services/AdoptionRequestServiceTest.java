package co.edu.udistrital.mdp.ZZZ.services;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.transaction.annotation.Transactional;

import co.edu.udistrital.mdp.pets.MainApplication;
import co.edu.udistrital.mdp.pets.entities.AdopterEntity;
import co.edu.udistrital.mdp.pets.entities.AdoptionRequestEntity;
import co.edu.udistrital.mdp.pets.entities.PetEntity;
import co.edu.udistrital.mdp.pets.exceptions.EntityNotFoundException;
import co.edu.udistrital.mdp.pets.exceptions.IllegalOperationException;
import co.edu.udistrital.mdp.pets.services.AdoptionRequestService;
import uk.co.jemos.podam.api.PodamFactory;
import uk.co.jemos.podam.api.PodamFactoryImpl;


@DataJpaTest
@Transactional
@ContextConfiguration(classes = MainApplication.class)
@Import(AdoptionRequestService.class)
class AdoptionRequestServiceTest {

    @Autowired
    private AdoptionRequestService adoptionRequestService;

    @Autowired
    private TestEntityManager entityManager;

    private PodamFactory factory = new PodamFactoryImpl();

    private List<AdoptionRequestEntity> requestList = new ArrayList<>();
    private List<AdopterEntity> adopterList = new ArrayList<>();
    private List<PetEntity> petList = new ArrayList<>();


    @BeforeEach
    void setUp() {
        clearData();
        insertData();
    }

    private void clearData() {
        entityManager.getEntityManager().createQuery("delete from AdoptionRequestEntity").executeUpdate();
        entityManager.getEntityManager().createQuery("delete from PetEntity").executeUpdate();
        entityManager.getEntityManager().createQuery("delete from AdopterEntity").executeUpdate();
    }

    private void insertData() {

        for (int i = 0; i < 3; i++) {

            AdopterEntity adopter = factory.manufacturePojo(AdopterEntity.class);
            entityManager.persist(adopter);
            adopterList.add(adopter);

            PetEntity pet = factory.manufacturePojo(PetEntity.class);
            entityManager.persist(pet);
            petList.add(pet);

            AdoptionRequestEntity request = factory.manufacturePojo(AdoptionRequestEntity.class);

            request.setAdopter(adopter);
            request.setPet(pet);
            request.setStatus("pendiente");

            entityManager.persist(request);
            requestList.add(request);
        }
    }

    /**
     * Crear solicitud
     */
    @Test
    void testCreateAdoptionRequest() throws EntityNotFoundException, IllegalOperationException {

        AdoptionRequestEntity newEntity = factory.manufacturePojo(AdoptionRequestEntity.class);

        AdopterEntity adopter = factory.manufacturePojo(AdopterEntity.class);
        entityManager.persist(adopter);

        PetEntity pet = petList.get(0);

        newEntity.setAdopter(adopter);
        newEntity.setPet(pet);

        AdoptionRequestEntity result = adoptionRequestService.createAdoptionRequest(newEntity);

        assertNotNull(result);

        AdoptionRequestEntity entity = entityManager.find(AdoptionRequestEntity.class, result.getId());

        assertEquals(adopter.getId(), entity.getAdopter().getId());
        assertEquals(pet.getId(), entity.getPet().getId());
    }

    /**
     * Crear solicitud con adoptante inválido
     */
    @Test
    void testCreateAdoptionRequestInvalidAdopter() {

        assertThrows(EntityNotFoundException.class, () -> {

            AdoptionRequestEntity entity = factory.manufacturePojo(AdoptionRequestEntity.class);

            AdopterEntity adopter = factory.manufacturePojo(AdopterEntity.class);
            adopter.setId(999L);

            entity.setAdopter(adopter);
            entity.setPet(petList.get(0));

            adoptionRequestService.createAdoptionRequest(entity);
        });
    }

    /**
     * Obtener todas las solicitudes
     */
    @Test
    void testGetAdoptionRequests() {

        List<AdoptionRequestEntity> list = adoptionRequestService.getAdoptionRequests();

        assertEquals(requestList.size(), list.size());

        for (AdoptionRequestEntity entity : list) {

            boolean found = false;

            for (AdoptionRequestEntity stored : requestList) {
                if (entity.getId().equals(stored.getId())) {
                    found = true;
                }
            }

            assertTrue(found);
        }
    }

    /**
     * Obtener solicitud por id
     */
    @Test
    void testGetAdoptionRequest() throws EntityNotFoundException {

        AdoptionRequestEntity entity = requestList.get(0);

        AdoptionRequestEntity result = adoptionRequestService.getAdoptionRequest(entity.getId());

        assertNotNull(result);
        assertEquals(entity.getId(), result.getId());
        assertEquals(entity.getStatus(), result.getStatus());
    }

    /**
     * Obtener solicitud inexistente
     */
    @Test
    void testGetInvalidAdoptionRequest() {

        assertThrows(EntityNotFoundException.class, () -> {
            adoptionRequestService.getAdoptionRequest(0L);
        });
    }

    /**
     * Actualizar solicitud
     */
    @Test
    void testUpdateAdoptionRequest() throws EntityNotFoundException {

        AdoptionRequestEntity request = requestList.get(0);

        AdoptionRequestEntity newData = factory.manufacturePojo(AdoptionRequestEntity.class);
        newData.setStatus("aprobado");

        adoptionRequestService.updateAdoptionRequest(request.getId(), newData);

        AdoptionRequestEntity response = entityManager.find(AdoptionRequestEntity.class, request.getId());

        assertEquals("aprobado", response.getStatus());
    }

    /**
     * Eliminar solicitud
     */
    @Test
    void testDeleteAdoptionRequest() throws EntityNotFoundException, IllegalOperationException {

        AdoptionRequestEntity entity = requestList.get(0);

        adoptionRequestService.deleteAdoptionRequest(entity.getId());

        AdoptionRequestEntity deleted = entityManager.find(AdoptionRequestEntity.class, entity.getId());

        assertNull(deleted);
    }

    /**
     * Eliminar solicitud inexistente
     */
    @Test
    void testDeleteInvalidAdoptionRequest() {

        assertThrows(EntityNotFoundException.class, () -> {
            adoptionRequestService.deleteAdoptionRequest(0L);
        });
    }
}