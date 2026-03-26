package co.edu.udistrital.mdp.ZZZ.services;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
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
import co.edu.udistrital.mdp.pets.entities.VeterinarianEntity;
import co.edu.udistrital.mdp.pets.exceptions.EntityNotFoundException;
import co.edu.udistrital.mdp.pets.exceptions.IllegalOperationException;
import co.edu.udistrital.mdp.pets.services.VeterinarianService;
import jakarta.transaction.Transactional;
import uk.co.jemos.podam.api.PodamFactory;
import uk.co.jemos.podam.api.PodamFactoryImpl;

@DataJpaTest
@Transactional
@ContextConfiguration(classes = MainApplication.class)
@Import(VeterinarianService.class)
class VeterinarianServiceTest {

    @Autowired
    private VeterinarianService veterinarianService;

    @Autowired
    private TestEntityManager entityManager;

    private PodamFactory factory = new PodamFactoryImpl();

    private List<VeterinarianEntity> vetList = new ArrayList<>();

    @BeforeEach
    void setUp() {
        clearData();
        insertData();
    }

    private void clearData() {
        entityManager.getEntityManager().createQuery("delete from MedicalRecordEntity").executeUpdate();
        entityManager.getEntityManager().createQuery("delete from VeterinarianEntity").executeUpdate();
    }

    private void insertData() {
        for (int i = 0; i < 3; i++) {
            VeterinarianEntity vet = factory.manufacturePojo(VeterinarianEntity.class);
            entityManager.persist(vet);
            vetList.add(vet);
        }
    }

    @Test
    void testGetVeterinarians() {
        List<VeterinarianEntity> list = veterinarianService.getVeterinarians();
        assertEquals(vetList.size(), list.size());
    }

    @Test
    void testGetVeterinarian() throws EntityNotFoundException {
        VeterinarianEntity vet = vetList.get(0);
        VeterinarianEntity result = veterinarianService.getVeterinarian(vet.getId());
        assertNotNull(result);
        assertEquals(vet.getId(), result.getId());
    }

    @Test
    void testGetInvalidVeterinarian() {
        assertThrows(EntityNotFoundException.class, () -> {
            veterinarianService.getVeterinarian(0L);
        });
    }

    @Test
    void testCreateVeterinarian() throws IllegalOperationException {
        VeterinarianEntity newVet = factory.manufacturePojo(VeterinarianEntity.class);
        newVet.setLicenseNumber("UNIQUE123");
        VeterinarianEntity result = veterinarianService.createVeterinarian(newVet);
        assertNotNull(result);
    }

    @Test
    void testCreateVeterinarianWithSameLicense() {
        VeterinarianEntity vet = vetList.get(0);
        VeterinarianEntity newVet = factory.manufacturePojo(VeterinarianEntity.class);
        newVet.setLicenseNumber(vet.getLicenseNumber());

        assertThrows(IllegalOperationException.class, () -> {
            veterinarianService.createVeterinarian(newVet);
        });
    }

    @Test
    void testDeleteVeterinarianWithMedicalRecords() {
        VeterinarianEntity vet = vetList.get(0);

        MedicalRecordEntity record = factory.manufacturePojo(MedicalRecordEntity.class);
        record.setVeterinarian(vet);
        entityManager.persist(record);
        
        vet.getMedicalRecords().add(record);
        entityManager.merge(vet);

        assertThrows(IllegalOperationException.class, () -> {
            veterinarianService.deleteVeterinarian(vet.getId());
        });
    }
}
