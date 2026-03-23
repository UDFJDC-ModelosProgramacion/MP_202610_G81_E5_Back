package co.edu.udistrital.mdp.ZZZ.services;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertThrows;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ContextConfiguration;

import co.edu.udistrital.mdp.pets.MainApplication;
import co.edu.udistrital.mdp.pets.entities.PetEntity;
import co.edu.udistrital.mdp.pets.entities.ShelterEntity;
import co.edu.udistrital.mdp.pets.exceptions.IllegalOperationException;
import co.edu.udistrital.mdp.pets.services.ShelterService;
import jakarta.transaction.Transactional;
import uk.co.jemos.podam.api.PodamFactory;
import uk.co.jemos.podam.api.PodamFactoryImpl;

@DataJpaTest
@Transactional
@Import(ShelterService.class)
@ContextConfiguration(classes = MainApplication.class)
class ShelterServiceTest {

    @Autowired
    private ShelterService shelterService;

    @Autowired
    private TestEntityManager entityManager;

    private PodamFactory factory = new PodamFactoryImpl();
    private List<ShelterEntity> shelterList = new ArrayList<>();

    @BeforeEach
    void setUp() {
        clearData();
        insertData();
    }

    private void clearData() {
        entityManager.getEntityManager().createQuery("delete from PetEntity").executeUpdate();
        entityManager.getEntityManager().createQuery("delete from ShelterEntity").executeUpdate();
    }

    private void insertData() {
        for (int i = 0; i < 3; i++) {
            ShelterEntity entity = factory.manufacturePojo(ShelterEntity.class);
            entityManager.persist(entity);
            shelterList.add(entity);
        }
    }


    @Test
    void testCreateShelterWithNullAttributes() {
        assertThrows(IllegalOperationException.class, () -> {
            ShelterEntity newEntity = new ShelterEntity();
            newEntity.setShelterName(null);
            shelterService.createShelter(newEntity);
        });
    }

    @Test
    void testUpdateLocationWithPets() {
        assertThrows(IllegalOperationException.class, () -> {
            ShelterEntity shelter = shelterList.get(0);
            
            PetEntity pet = factory.manufacturePojo(PetEntity.class);
            pet.setShelter(shelter);
            entityManager.persist(pet);
            
            if(shelter.getPets() == null) shelter.setPets(new ArrayList<>());
            shelter.getPets().add(pet);

            ShelterEntity pojo = factory.manufacturePojo(ShelterEntity.class);
            pojo.setLocation("Calle Falsa 123");
            
            shelterService.updateShelter(shelter.getId(), pojo);
        });
    }

    @Test
    void testDeleteShelterWithPets() {
        assertThrows(IllegalOperationException.class, () -> {
            ShelterEntity shelter = shelterList.get(0);
            
            PetEntity pet = factory.manufacturePojo(PetEntity.class);
            pet.setShelter(shelter);
            entityManager.persist(pet);
            
            if(shelter.getPets() == null) shelter.setPets(new ArrayList<>());
            shelter.getPets().add(pet);

            shelterService.deleteShelter(shelter.getId());
        });
    }
}