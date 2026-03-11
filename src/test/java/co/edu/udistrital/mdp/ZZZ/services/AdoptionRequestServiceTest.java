package co.edu.udistrital.mdp.ZZZ.services;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.context.annotation.Import;
import org.springframework.transaction.annotation.Transactional;

import co.edu.udistrital.mdp.pets.entities.AdoptionProcessEntity;
import co.edu.udistrital.mdp.pets.entities.AdoptionRequestEntity;
import co.edu.udistrital.mdp.pets.services.AdoptionRequestService;
import lombok.extern.slf4j.Slf4j;
import uk.co.jemos.podam.api.PodamFactory;
import uk.co.jemos.podam.api.PodamFactoryImpl;

@Slf4j
@DataJpaTest
@Transactional
@Import(AdoptionRequestService.class)
public class AdoptionRequestServiceTest {
    @Autowired
    private AdoptionRequestService adoptionRequestService;

    @Autowired
    private TestEntityManager entityManager;

    private PodamFactory factory = new PodamFactoryImpl();
    private List<AdoptionRequestEntity> adoptionRequestList = new ArrayList<>();
    private List<AdoptionProcessEntity> adoptionProcessList = new ArrayList<>();   

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

    

}
