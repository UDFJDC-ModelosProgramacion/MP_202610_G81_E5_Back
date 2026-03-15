package co.edu.udistrital.mdp.ZZZ.services;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertNotNull;
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
import co.edu.udistrital.mdp.pets.entities.AdoptionProcessEntity;
import co.edu.udistrital.mdp.pets.entities.AdoptionRequestEntity;
import co.edu.udistrital.mdp.pets.exceptions.EntityNotFoundException;
import co.edu.udistrital.mdp.pets.exceptions.IllegalOperationException;
import co.edu.udistrital.mdp.pets.services.AdoptionRequestService;
import uk.co.jemos.podam.api.PodamFactory;
import uk.co.jemos.podam.api.PodamFactoryImpl;


@DataJpaTest
@Transactional
@ContextConfiguration(classes = MainApplication.class)
@Import(AdoptionRequestService.class)
public class AdoptionRequestServiceTest {
    @Autowired
    private AdoptionRequestService adoptionRequestService;

    @Autowired
    private TestEntityManager entityManager;

    final private PodamFactory factory = new PodamFactoryImpl();
    final private List<AdoptionRequestEntity> adoptionRequestList = new ArrayList<>();
    final private List<AdoptionProcessEntity> adoptionProcessList = new ArrayList<>();   

    @BeforeEach

    void setUp() {

        clearData();

        insertData();

    }

    public void clearData() {
        entityManager.getEntityManager().createQuery("delete from AdoptionRequestEntity").executeUpdate();
        entityManager.getEntityManager().createQuery("delete from AdoptionProcessEntity").executeUpdate();
    }

    public void insertData() {
        for (int i = 0; i < 3; i++) {
            AdoptionProcessEntity adoptionProcessEntity = factory.manufacturePojo(AdoptionProcessEntity.class);
            entityManager.persist(adoptionProcessEntity);
            adoptionProcessList.add(adoptionProcessEntity);
        }
        for (int i = 0; i < 3; i++) {
            AdoptionRequestEntity adoptionRequestEntity = factory.manufacturePojo(AdoptionRequestEntity.class);
            adoptionRequestEntity.setAdoptionProcess(adoptionProcessList.get(0));
            entityManager.persist(adoptionRequestEntity);
            adoptionRequestList.add(adoptionRequestEntity);
        }
    }

    @Test
    void testCreateAdoptionRequestSuccess() throws EntityNotFoundException, IllegalOperationException {
        AdoptionRequestEntity newEntity = factory.manufacturePojo(AdoptionRequestEntity.class);
        newEntity.setAdoptionProcess(adoptionProcessList.get(0));
        newEntity.setAdopter(adoptionRequestList.get(0).getAdopter());
        newEntity.setIdPet("1");
        newEntity.setPurpose("Test purpose");
        newEntity.setPapers("Test papers");

        AdoptionRequestEntity result = adoptionRequestService.createAdoptionRequest(newEntity);
        assertNotNull(result);
        assertNotNull(result.getId());
    }

    @Test
    void testCreateAdoptionRequestThrowsIfProcessFinalized() {
        AdoptionProcessEntity finalizedProcess = factory.manufacturePojo(AdoptionProcessEntity.class);
        finalizedProcess.setStatus("Finalizado");
        entityManager.persist(finalizedProcess);

        AdoptionRequestEntity newEntity = factory.manufacturePojo(AdoptionRequestEntity.class);
        newEntity.setAdoptionProcess(finalizedProcess);
        newEntity.setAdopter(adoptionRequestList.get(0).getAdopter());
        newEntity.setIdPet("1");
        newEntity.setPurpose("Test purpose");
        newEntity.setPapers("Test papers");

        IllegalOperationException exception = assertThrows(IllegalOperationException.class, () -> {
            adoptionRequestService.createAdoptionRequest(newEntity);
        });

        assertTrue(exception.getMessage().contains("No se pueden crear solicitudes de adopción para procesos de adopción finalizados"));
    }
}

