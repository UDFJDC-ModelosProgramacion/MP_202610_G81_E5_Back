package co.edu.udistrital.mdp.pets.service;

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

import co.edu.udistrital.mdp.pets.entities.MedicalRecordEntity;
import co.edu.udistrital.mdp.pets.entities.PetEntity;
import co.edu.udistrital.mdp.pets.entities.VeterinarianEntity;
import co.edu.udistrital.mdp.pets.entities.AdoptionProcessEntity;
import co.edu.udistrital.mdp.pets.exceptions.EntityNotFoundException;
import co.edu.udistrital.mdp.pets.exceptions.IllegalOperationException;
import co.edu.udistrital.mdp.pets.services.MedicalRecordService;
import uk.co.jemos.podam.api.PodamFactory;
import uk.co.jemos.podam.api.PodamFactoryImpl;


@DataJpaTest
@Transactional
@Import({ MedicalRecordService.class })
class MedicalRecordServiceTest {

    @Autowired
    private MedicalRecordService medicalRecordService;

    @Autowired
    private TestEntityManager entityManager;

    private PodamFactory factory = new PodamFactoryImpl();

    private List<MedicalRecordEntity> recordList = new ArrayList<>();
    private PetEntity petEntity;
    private VeterinarianEntity vetEntity;

    @BeforeEach
    void setUp() {
        clearData();
        insertData();
    }

    private void clearData() {
        entityManager.getEntityManager().createQuery("delete from MedicalRecordEntity").executeUpdate();
        entityManager.getEntityManager().createQuery("delete from PetEntity").executeUpdate();
        entityManager.getEntityManager().createQuery("delete from VeterinarianEntity").executeUpdate();
        entityManager.getEntityManager().createQuery("delete from AdoptionProcessEntity").executeUpdate();
    }

    private void insertData() {
  
        petEntity = factory.manufacturePojo(PetEntity.class);
        entityManager.persist(petEntity);


        vetEntity = factory.manufacturePojo(VeterinarianEntity.class);
        entityManager.persist(vetEntity);


        for (int i = 0; i  {
            MedicalRecordEntity newRecord = factory.manufacturePojo(MedicalRecordEntity.class);
            medicalRecordService.createMedicalRecord(0L, vetEntity.getId(), newRecord);
        });
    }

    @Test
    void testCreateMedicalRecordInvalidVet() {
        assertThrows(EntityNotFoundException.class, () -> {
            MedicalRecordEntity newRecord = factory.manufacturePojo(MedicalRecordEntity.class);
            medicalRecordService.createMedicalRecord(petEntity.getId(), 0L, newRecord);
        });
    }



    @Test
    void testUpdateMedicalRecord() throws EntityNotFoundException {
        MedicalRecordEntity oldEntity = recordList.get(0);
        MedicalRecordEntity newEntity = factory.manufacturePojo(MedicalRecordEntity.class);

        // Intentamos "hackear" el cambio de mascota/vet enviando otros IDs en el objeto
        PetEntity otraMascota = factory.manufacturePojo(PetEntity.class);
        entityManager.persist(otraMascota);
        newEntity.setPet(otraMascota);

        MedicalRecordEntity updated = medicalRecordService.updateMedicalRecord(oldEntity.getId(), newEntity);

        assertNotNull(updated);
        assertEquals(newEntity.getVaccinations(), updated.getVaccinations());
        assertEquals(newEntity.getUpcomingDates(), updated.getUpcomingDates());

        assertEquals(petEntity.getId(), updated.getPet().getId());
    }

    @Test
    void testUpdateMedicalRecordNotFound() {
        assertThrows(EntityNotFoundException.class, () -> {
            MedicalRecordEntity newEntity = factory.manufacturePojo(MedicalRecordEntity.class);
            medicalRecordService.updateMedicalRecord(0L, newEntity);
        });
    }


    @Test
    void testDeleteMedicalRecord() throws EntityNotFoundException, IllegalOperationException {
        MedicalRecordEntity entity = recordList.get(0);
        medicalRecordService.deleteMedicalRecord(entity.getId());
        MedicalRecordEntity deleted = entityManager.find(MedicalRecordEntity.class, entity.getId());
        assertNull(deleted);
    }

    @Test
    void testDeleteMedicalRecordInvalid() {
        assertThrows(EntityNotFoundException.class, () -> {
            medicalRecordService.deleteMedicalRecord(0L);
        });
    }

    @Test
    void testDeleteMedicalRecordWithActiveAdoption() {
        AdoptionProcessEntity process = factory.manufacturePojo(AdoptionProcessEntity.class);
        process.setPet(petEntity);
        entityManager.persist(process);
        

        petEntity.getAdoptionProcess().add(process);
        entityManager.merge(petEntity);

        MedicalRecordEntity recordParaBorrar = recordList.get(0);

        assertThrows(IllegalOperationException.class, () -> {
            medicalRecordService.deleteMedicalRecord(recordParaBorrar.getId());
        });
    }


    @Test
    void testGetMedicalRecordsByPet() throws EntityNotFoundException {
        List<MedicalRecordEntity> resultList = medicalRecordService.getMedicalRecordsByPet(petEntity.getId());
        assertEquals(recordList.size(), resultList.size());
        for (MedicalRecordEntity m : resultList) {
            assertTrue(recordList.contains(m));
        }
    }
}
