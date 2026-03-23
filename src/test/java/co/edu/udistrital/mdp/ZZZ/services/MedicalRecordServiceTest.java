package co.edu.udistrital.mdp.ZZZ.services;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ContextConfiguration;

import co.edu.udistrital.mdp.pets.MainApplication;
import co.edu.udistrital.mdp.pets.entities.MedicalRecordEntity;
import co.edu.udistrital.mdp.pets.entities.PetEntity;
import co.edu.udistrital.mdp.pets.exceptions.IllegalOperationException;
import co.edu.udistrital.mdp.pets.services.MedicalRecordService;
import jakarta.transaction.Transactional;
import uk.co.jemos.podam.api.PodamFactory;
import uk.co.jemos.podam.api.PodamFactoryImpl;

@DataJpaTest
@Transactional
@Import(MedicalRecordService.class)
@ContextConfiguration(classes = MainApplication.class)
class MedicalRecordServiceTest {

    @Autowired
    private MedicalRecordService medicalRecordService;

    @Autowired
    private TestEntityManager entityManager;

    private PodamFactory factory = new PodamFactoryImpl();
    private List<MedicalRecordEntity> recordList = new ArrayList<>();

    @BeforeEach
    void setUp() {
        clearData();
        insertData();
    }

    private void clearData() {
        entityManager.getEntityManager().createQuery("delete from MedicalRecordEntity").executeUpdate();
        entityManager.getEntityManager().createQuery("delete from PetEntity").executeUpdate();
    }

    private void insertData() {
        for (int i = 0; i < 3; i++) {
            MedicalRecordEntity entity = factory.manufacturePojo(MedicalRecordEntity.class);
      
            PetEntity pet = factory.manufacturePojo(PetEntity.class);
            entityManager.persist(pet);
            entity.setPet(pet);
            
            entityManager.persist(entity);
            recordList.add(entity);
        }
    }

    @Test
    void testCreateMedicalRecordNullPet() {
        assertThrows(IllegalOperationException.class, () -> {
            MedicalRecordEntity newEntity = factory.manufacturePojo(MedicalRecordEntity.class);
            newEntity.setPet(null); 
            medicalRecordService.createMedicalRecord(newEntity);
        });
    }

    @Test
    void testUpdateChangePet() {
        assertThrows(IllegalOperationException.class, () -> {
            MedicalRecordEntity existing = recordList.get(0);
   
            PetEntity otherPet = factory.manufacturePojo(PetEntity.class);
            entityManager.persist(otherPet);
            
            MedicalRecordEntity pojo = factory.manufacturePojo(MedicalRecordEntity.class);
            pojo.setPet(otherPet);
            
            medicalRecordService.updateMedicalRecord(existing.getId(), pojo);
        });
    }

    @Test
    void testDeleteRecord() throws Exception {
        MedicalRecordEntity existing = recordList.get(0);
        medicalRecordService.deleteMedicalRecord(existing.getId());
        assertNull(entityManager.find(MedicalRecordEntity.class, existing.getId()));
    }
}