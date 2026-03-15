package co.edu.udistrital.mdp.ZZZ.services;

import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Comparator;
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
import co.edu.udistrital.mdp.pets.entities.LifeEventEntity;
import co.edu.udistrital.mdp.pets.entities.PetEntity;
import co.edu.udistrital.mdp.pets.entities.TypeLEEntity;
import co.edu.udistrital.mdp.pets.entities.VeterinarianEntity;
import co.edu.udistrital.mdp.pets.exceptions.EntityNotFoundException;
import co.edu.udistrital.mdp.pets.exceptions.IllegalOperationException;
import co.edu.udistrital.mdp.pets.services.LifeEventService;

import uk.co.jemos.podam.api.PodamFactory;
import uk.co.jemos.podam.api.PodamFactoryImpl;

@DataJpaTest
@Transactional
@Import(LifeEventService.class)
@ContextConfiguration(classes = MainApplication.class)
class LifeEventServiceTest {

    @Autowired
    private LifeEventService lifeEventService;

    @Autowired
    private TestEntityManager entityManager;

    private PodamFactory factory = new PodamFactoryImpl();

    private List<LifeEventEntity> lifeEventList = new ArrayList<>();

    private PetEntity petEntity;
    private TypeLEEntity typeEntity;
    private VeterinarianEntity veterinarianEntity;

    @BeforeEach
    void setUp() {
        clearData();
        insertData();
    }

    private void clearData() {
        entityManager.getEntityManager().createQuery("delete from LifeEventEntity").executeUpdate();
        entityManager.getEntityManager().createQuery("delete from PetEntity").executeUpdate();
        entityManager.getEntityManager().createQuery("delete from TypeLEEntity").executeUpdate();
        entityManager.getEntityManager().createQuery("delete from VeterinarianEntity").executeUpdate();
    }

    private void insertData() {

        petEntity = factory.manufacturePojo(PetEntity.class);
        entityManager.persist(petEntity);

        typeEntity = factory.manufacturePojo(TypeLEEntity.class);
        entityManager.persist(typeEntity);

        veterinarianEntity = factory.manufacturePojo(VeterinarianEntity.class);
        entityManager.persist(veterinarianEntity);

        for(int i = 0; i < 3; i++) {

            LifeEventEntity lifeEvent = factory.manufacturePojo(LifeEventEntity.class);

            lifeEvent.setPet(petEntity);
            lifeEvent.setType(typeEntity);
            lifeEvent.setVeterinarian(veterinarianEntity);
            lifeEvent.setDate(LocalDate.now().plusDays(i));

            entityManager.persist(lifeEvent);

            petEntity.getLifeEvents().add(lifeEvent);

            lifeEventList.add(lifeEvent);
        }
    }

    @Test
    void testCreateLifeEvent() throws EntityNotFoundException, IllegalOperationException {

        LifeEventEntity newEntity = factory.manufacturePojo(LifeEventEntity.class);

        newEntity.setPet(petEntity);
        newEntity.setType(typeEntity);
        newEntity.setVeterinarian(veterinarianEntity);
        newEntity.setDate(LocalDate.now());

        LifeEventEntity result = lifeEventService.createLifeEvent(newEntity);

        assertNotNull(result);

        LifeEventEntity entity = entityManager.find(LifeEventEntity.class, result.getId());

        assertEquals(newEntity.getDescription(), entity.getDescription());
        assertEquals(newEntity.getDate(), entity.getDate());
    }

    @Test
    void testCreateLifeEventWithInvalidDescription() {

        assertThrows(IllegalOperationException.class, () -> {

            LifeEventEntity entity = factory.manufacturePojo(LifeEventEntity.class);

            entity.setDescription("");
            entity.setPet(petEntity);
            entity.setType(typeEntity);
            entity.setVeterinarian(veterinarianEntity);
            entity.setDate(LocalDate.now());

            lifeEventService.createLifeEvent(entity);
        });
    }

    @Test
    void testCreateLifeEventWithInvalidDate() {

        assertThrows(IllegalOperationException.class, () -> {

            LifeEventEntity entity = factory.manufacturePojo(LifeEventEntity.class);

            entity.setPet(petEntity);
            entity.setType(typeEntity);
            entity.setVeterinarian(veterinarianEntity);
            entity.setDate(null);

            lifeEventService.createLifeEvent(entity);
        });
    }

    @Test
    void testGetLifeEvents() {

        List<LifeEventEntity> list = lifeEventService.getLifeEvents();

        assertEquals(lifeEventList.size(), list.size());

        for(LifeEventEntity entity : list) {

            boolean found = false;

            for(LifeEventEntity stored : lifeEventList) {

                if(entity.getId().equals(stored.getId())) {
                    found = true;
                }
            }

            assertTrue(found);
        }
    }

    @Test
    void testGetLifeEvent() throws EntityNotFoundException {

        LifeEventEntity entity = lifeEventList.get(0);

        LifeEventEntity result = lifeEventService.getLifeEvent(entity.getId());

        assertNotNull(result);
        assertEquals(entity.getId(), result.getId());
        assertEquals(entity.getDescription(), result.getDescription());
    }

    @Test
    void testGetInvalidLifeEvent() {

        assertThrows(EntityNotFoundException.class, () -> {
            lifeEventService.getLifeEvent(0L);
        });
    }

    @Test
    void testUpdateLifeEvent() throws EntityNotFoundException, IllegalOperationException {

        LifeEventEntity entity = lifeEventList.get(0);

        LifeEventEntity pojoEntity = factory.manufacturePojo(LifeEventEntity.class);

        pojoEntity.setPet(petEntity);
        pojoEntity.setType(typeEntity);
        pojoEntity.setVeterinarian(veterinarianEntity);
        pojoEntity.setDate(LocalDate.now());

        lifeEventService.updateLifeEvent(entity.getId(), pojoEntity);

        LifeEventEntity resp = entityManager.find(LifeEventEntity.class, entity.getId());

        assertEquals(pojoEntity.getDescription(), resp.getDescription());
        assertEquals(pojoEntity.getDate(), resp.getDate());
    }

    @Test
    void testUpdateInvalidLifeEvent() {

        assertThrows(EntityNotFoundException.class, () -> {

            LifeEventEntity entity = factory.manufacturePojo(LifeEventEntity.class);

            lifeEventService.updateLifeEvent(0L, entity);
        });
    }

    @Test
    void testDeleteLifeEvent() throws EntityNotFoundException, IllegalOperationException {

        LifeEventEntity entity = lifeEventList.stream()
                .sorted(Comparator.comparing(LifeEventEntity::getDate).reversed())
                .findFirst()
                .get();

        lifeEventService.deleteLifeEvent(entity.getId());

        LifeEventEntity deleted = entityManager.find(LifeEventEntity.class, entity.getId());

        assertNull(deleted);
    }

    @Test
    void testDeleteFirstLifeEvent() {

        assertThrows(IllegalOperationException.class, () -> {

            LifeEventEntity first = lifeEventList.stream()
                    .sorted(Comparator.comparing(LifeEventEntity::getDate))
                    .findFirst()
                    .get();

            lifeEventService.deleteLifeEvent(first.getId());
        });
    }

    @Test
    void testDeleteInvalidLifeEvent() {

        assertThrows(EntityNotFoundException.class, () -> {

            lifeEventService.deleteLifeEvent(0L);
        });
    }
}
