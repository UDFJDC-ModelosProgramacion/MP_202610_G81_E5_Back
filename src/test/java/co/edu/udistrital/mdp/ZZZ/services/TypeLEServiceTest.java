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
import co.edu.udistrital.mdp.pets.entities.LifeEventEntity;
import co.edu.udistrital.mdp.pets.entities.PetEntity;
import co.edu.udistrital.mdp.pets.entities.TypeLEEntity;
import co.edu.udistrital.mdp.pets.entities.VeterinarianEntity;
import co.edu.udistrital.mdp.pets.exceptions.EntityNotFoundException;
import co.edu.udistrital.mdp.pets.exceptions.IllegalOperationException;
import co.edu.udistrital.mdp.pets.services.TypeLEService;

import uk.co.jemos.podam.api.PodamFactory;
import uk.co.jemos.podam.api.PodamFactoryImpl;

@DataJpaTest
@Transactional
@Import(TypeLEService.class)
@ContextConfiguration(classes = MainApplication.class)
class TypeLEServiceTest {

    @Autowired
    private TypeLEService typeLEService;

    @Autowired
    private TestEntityManager entityManager;

    private PodamFactory factory = new PodamFactoryImpl();

    private List<TypeLEEntity> typeLEList = new ArrayList<>();

    private PetEntity petEntity;
    private VeterinarianEntity veterinarianEntity;

    @BeforeEach
    void setUp() {
        clearData();
        insertData();
    }

    private void clearData() {
        entityManager.getEntityManager().createQuery("delete from LifeEventEntity").executeUpdate();
        entityManager.getEntityManager().createQuery("delete from TypeLEEntity").executeUpdate();
        entityManager.getEntityManager().createQuery("delete from PetEntity").executeUpdate();
        entityManager.getEntityManager().createQuery("delete from VeterinarianEntity").executeUpdate();
    }

    private void insertData() {

        petEntity = factory.manufacturePojo(PetEntity.class);
        entityManager.persist(petEntity);

        veterinarianEntity = factory.manufacturePojo(VeterinarianEntity.class);
        entityManager.persist(veterinarianEntity);

        for (int i = 0; i < 3; i++) {

            TypeLEEntity type = factory.manufacturePojo(TypeLEEntity.class);
            entityManager.persist(type);

            typeLEList.add(type);
        }

        LifeEventEntity lifeEvent = factory.manufacturePojo(LifeEventEntity.class);

        lifeEvent.setPet(petEntity);
        lifeEvent.setVeterinarian(veterinarianEntity);
        lifeEvent.setType(typeLEList.get(0));

        entityManager.persist(lifeEvent);
    }

    @Test
    void testCreateTypeLE() throws EntityNotFoundException, IllegalOperationException {

        TypeLEEntity newEntity = factory.manufacturePojo(TypeLEEntity.class);

        TypeLEEntity result = typeLEService.createTypeLE(newEntity);

        assertNotNull(result);

        TypeLEEntity entity = entityManager.find(TypeLEEntity.class, result.getId());

        assertEquals(newEntity.getName(), entity.getName());
    }

    @Test
    void testCreateTypeLEWithDuplicateName() {

        assertThrows(IllegalOperationException.class, () -> {

            TypeLEEntity newEntity = factory.manufacturePojo(TypeLEEntity.class);

            newEntity.setName(typeLEList.get(0).getName());

            typeLEService.createTypeLE(newEntity);
        });
    }

    @Test
    void testGetTypeLEs() {

        List<TypeLEEntity> list = typeLEService.getTypeLEs();

        assertEquals(typeLEList.size(), list.size());

        for (TypeLEEntity entity : list) {

            boolean found = false;

            for (TypeLEEntity storedEntity : typeLEList) {

                if (entity.getId().equals(storedEntity.getId())) {
                    found = true;
                }
            }

            assertTrue(found);
        }
    }

    @Test
    void testGetTypeLE() throws EntityNotFoundException {

        TypeLEEntity entity = typeLEList.get(0);

        TypeLEEntity resultEntity = typeLEService.getTypeLE(entity.getId());

        assertNotNull(resultEntity);
        assertEquals(entity.getId(), resultEntity.getId());
        assertEquals(entity.getName(), resultEntity.getName());
    }

    @Test
    void testGetInvalidTypeLE() {

        assertThrows(EntityNotFoundException.class, () -> {
            typeLEService.getTypeLE(0L);
        });
    }

    @Test
    void testUpdateTypeLE() throws EntityNotFoundException, IllegalOperationException {

        TypeLEEntity entity = typeLEList.get(1);

        TypeLEEntity pojoEntity = factory.manufacturePojo(TypeLEEntity.class);

        pojoEntity.setId(entity.getId());

        typeLEService.updateTypeLE(entity.getId(), pojoEntity);

        TypeLEEntity resp = entityManager.find(TypeLEEntity.class, entity.getId());

        assertEquals(pojoEntity.getName(), resp.getName());
    }

    @Test
    void testUpdateInvalidTypeLE() {

        assertThrows(EntityNotFoundException.class, () -> {

            TypeLEEntity entity = factory.manufacturePojo(TypeLEEntity.class);

            typeLEService.updateTypeLE(0L, entity);
        });
    }

    @Test
    void testUpdateTypeLEWithDuplicateName() {

        assertThrows(IllegalOperationException.class, () -> {

            TypeLEEntity entity = typeLEList.get(1);

            TypeLEEntity pojoEntity = factory.manufacturePojo(TypeLEEntity.class);

            pojoEntity.setName(typeLEList.get(0).getName());

            typeLEService.updateTypeLE(entity.getId(), pojoEntity);
        });
    }

    @Test
    void testDeleteTypeLE() throws EntityNotFoundException, IllegalOperationException {

        TypeLEEntity entity = typeLEList.get(1);

        typeLEService.deleteTypeLE(entity.getId());

        TypeLEEntity deleted = entityManager.find(TypeLEEntity.class, entity.getId());

        assertNull(deleted);
    }

    @Test
    void testDeleteInvalidTypeLE() {

        assertThrows(EntityNotFoundException.class, () -> {
            typeLEService.deleteTypeLE(0L);
        });
    }

    @Test
    void testDeleteTypeLEWithLifeEvents() {

        assertThrows(IllegalOperationException.class, () -> {

            TypeLEEntity entity = typeLEList.get(0);

            typeLEService.deleteTypeLE(entity.getId());
        });
    }
}