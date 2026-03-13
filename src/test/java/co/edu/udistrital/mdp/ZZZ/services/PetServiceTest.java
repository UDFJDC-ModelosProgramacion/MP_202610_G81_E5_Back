package co.edu.udistrital.mdp.ZZZ.services;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.List;

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
import co.edu.udistrital.mdp.pets.entities.PetEntity;
import co.edu.udistrital.mdp.pets.entities.ShelterEntity;
import co.edu.udistrital.mdp.pets.exceptions.EntityNotFoundException;
import co.edu.udistrital.mdp.pets.exceptions.IllegalOperationException;
import co.edu.udistrital.mdp.pets.services.PetService;
import uk.co.jemos.podam.api.PodamFactory;
import uk.co.jemos.podam.api.PodamFactoryImpl;


@DataJpaTest
@Transactional
@Import(PetService.class)
@ContextConfiguration(classes = MainApplication.class)
class PetServiceTest {

    @Autowired
    private PetService petService;

    @Autowired
    private TestEntityManager entityManager;

    private PodamFactory factory = new PodamFactoryImpl();

    private List<PetEntity> petList = new ArrayList<>();
    private ShelterEntity shelterEntity;
    private AdopterEntity adopterEntity;

    @BeforeEach
    void setUp() {
        clearData();
        insertData();
    }

    private void clearData() {
        entityManager.getEntityManager().createQuery("delete from AdoptionProcessEntity").executeUpdate();
        entityManager.getEntityManager().createQuery("delete from PetEntity").executeUpdate();
        entityManager.getEntityManager().createQuery("delete from ShelterEntity").executeUpdate();
        entityManager.getEntityManager().createQuery("delete from AdopterEntity").executeUpdate();
    }

    private void insertData() {

        shelterEntity = factory.manufacturePojo(ShelterEntity.class);
        entityManager.persist(shelterEntity);

        adopterEntity = factory.manufacturePojo(AdopterEntity.class);
        entityManager.persist(adopterEntity);

        for (int i = 0; i < 3; i++) {

            PetEntity petEntity = factory.manufacturePojo(PetEntity.class);
            petEntity.setShelter(shelterEntity);
            petEntity.setAdopter(null);
            petEntity.setAge(2);

            entityManager.persist(petEntity);
            petList.add(petEntity);
        }
    }

    /**
     * Crear mascota correctamente
     */
    @Test
    void testCreatePet() throws EntityNotFoundException, IllegalOperationException {

        PetEntity newEntity = factory.manufacturePojo(PetEntity.class);
        newEntity.setShelter(shelterEntity);
        newEntity.setAge(3);

        PetEntity result = petService.createPet(newEntity);

        assertNotNull(result);

        PetEntity entity = entityManager.find(PetEntity.class, result.getId());

        assertEquals(newEntity.getName(), entity.getName());
        assertEquals(newEntity.getAge(), entity.getAge());
    }

    /**
     * Crear mascota con nombre null
     */
    @Test
    void testCreatePetWithNullName() {

        assertThrows(IllegalOperationException.class, () -> {

            PetEntity newEntity = factory.manufacturePojo(PetEntity.class);
            newEntity.setName(null);
            newEntity.setShelter(shelterEntity);

            petService.createPet(newEntity);

        });

    }

    /**
     * Crear mascota con refugio null
     */
    @Test
    void testCreatePetWithNullShelter() {

        assertThrows(IllegalOperationException.class, () -> {

            PetEntity newEntity = factory.manufacturePojo(PetEntity.class);
            newEntity.setShelter(null);

            petService.createPet(newEntity);

        });

    }

    /**
     * Crear mascota con edad negativa
     */
    @Test
    void testCreatePetWithNegativeAge() {

        assertThrows(IllegalOperationException.class, () -> {

            PetEntity newEntity = factory.manufacturePojo(PetEntity.class);
            newEntity.setShelter(shelterEntity);
            newEntity.setAge(-1);

            petService.createPet(newEntity);

        });

    }

    /**
     * Obtener todas las mascotas
     */
    @Test
    void testGetPets() {

        List<PetEntity> list = petService.getPets();

        assertEquals(petList.size(), list.size());

        for (PetEntity entity : list) {

            boolean found = false;

            for (PetEntity stored : petList) {

                if (entity.getId().equals(stored.getId())) {
                    found = true;
                }
            }

            assertTrue(found);
        }
    }

    /**
     * Obtener una mascota
     */
    @Test
    void testGetPet() throws EntityNotFoundException {

        PetEntity entity = petList.get(0);

        PetEntity result = petService.getPet(entity.getId());

        assertNotNull(result);

        assertEquals(entity.getId(), result.getId());
        assertEquals(entity.getName(), result.getName());
        assertEquals(entity.getAge(), result.getAge());

    }

    /**
     * Obtener mascota inválida
     */
    @Test
    void testGetInvalidPet() {

        assertThrows(EntityNotFoundException.class, () -> {

            petService.getPet(0L);

        });

    }

    /**
     * Actualizar mascota correctamente
     */
    @Test
    void testUpdatePet() throws EntityNotFoundException, IllegalOperationException {

        PetEntity entity = petList.get(0);

        PetEntity pojoEntity = factory.manufacturePojo(PetEntity.class);
        pojoEntity.setShelter(shelterEntity);
        pojoEntity.setAdopter(null);
        pojoEntity.setAge(5);

        petService.updatePet(entity.getId(), pojoEntity);

        PetEntity resp = entityManager.find(PetEntity.class, entity.getId());

        assertEquals(pojoEntity.getName(), resp.getName());
        assertEquals(pojoEntity.getAge(), resp.getAge());
    }

    /**
     * Actualizar mascota que no existe
     */
    @Test
    void testUpdateInvalidPet() {

        assertThrows(EntityNotFoundException.class, () -> {

            PetEntity pojoEntity = factory.manufacturePojo(PetEntity.class);

            petService.updatePet(0L, pojoEntity);

        });

    }

    /**
     * Actualizar mascota con nombre null
     */
    @Test
    void testUpdatePetWithNullName() {

        assertThrows(IllegalOperationException.class, () -> {

            PetEntity entity = petList.get(0);

            PetEntity pojoEntity = factory.manufacturePojo(PetEntity.class);
            pojoEntity.setName(null);
            pojoEntity.setShelter(shelterEntity);
            pojoEntity.setAdopter(null);

            petService.updatePet(entity.getId(), pojoEntity);

        });

    }

    /**
     * Eliminar mascota correctamente
     */
    @Test
    void testDeletePet() throws EntityNotFoundException, IllegalOperationException {

        PetEntity entity = petList.get(1);

        petService.deletePet(entity.getId());

        PetEntity deleted = entityManager.find(PetEntity.class, entity.getId());

        assertNull(deleted);
    }

    /**
     * Eliminar mascota inexistente
     */
    @Test
    void testDeleteInvalidPet() {

        assertThrows(EntityNotFoundException.class, () -> {

            petService.deletePet(0L);

        });

    }

}