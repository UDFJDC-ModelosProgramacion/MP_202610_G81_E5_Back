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
import org.springframework.transaction.annotation.Transactional;

import co.edu.udistrital.mdp.pets.entities.AdoptionProcessEntity;
import co.edu.udistrital.mdp.pets.entities.AdoptionRequestEntity;
import co.edu.udistrital.mdp.pets.exceptions.EntityNotFoundException;
import co.edu.udistrital.mdp.pets.exceptions.IllegalOperationException;
import co.edu.udistrital.mdp.pets.services.AdoptionRequestService;
import uk.co.jemos.podam.api.PodamFactory;
import uk.co.jemos.podam.api.PodamFactoryImpl;


@DataJpaTest
@Transactional
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

    void testCreateAdoptionRequest() throws EntityNotFoundException, IllegalOperationException {
        
        AdoptionRequestEntity newEntity = factory.manufacturePojo(AdoptionRequestEntity.class);

        newEntity.setAdoptionProcess(adoptionProcessList.get(0));

        newEntity.setAdopter(adoptionRequestList.get(0).getAdopter());

        newEntity.setPurpose("Placeholder for test run");

        newEntity.setPapers("Placeholder for test run");

        AdoptionRequestEntity result = adoptionRequestService.createAdoptionRequest(newEntity);

        assertNotNull(result);

        AdoptionRequestEntity entity = entityManager.find(AdoptionRequestEntity.class, result.getId());


        assertEquals(newEntity.getId(), entity.getId());

        assertEquals(newEntity.getAdopter().getId(), entity.getAdopter().getId());

        assertEquals(newEntity.getAdoptionProcess().getId(), entity.getAdoptionProcess().getId());

    }

}
