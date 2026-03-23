package co.edu.udistrital.mdp.ZZZ.services;
import static org.junit.jupiter.api.Assertions.*;
import co.edu.udistrital.mdp.pets.services.EventService;
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
import co.edu.udistrital.mdp.pets.entities.EventEntity;
import co.edu.udistrital.mdp.pets.exceptions.IllegalOperationException;
import uk.co.jemos.podam.api.PodamFactory;
import uk.co.jemos.podam.api.PodamFactoryImpl;

@DataJpaTest
@Transactional
@Import(EventService.class)
@ContextConfiguration(classes = MainApplication.class)
class EventServiceTest {

    @Autowired
    private EventService eventService;

    @Autowired
    private TestEntityManager entityManager;

    private PodamFactory factory = new PodamFactoryImpl();

    private List<EventEntity> eventList = new ArrayList<>();

    @BeforeEach
    void setUp() {
        clearData();
        insertData();
    }

    private void clearData() {
        entityManager.getEntityManager().createQuery("delete from EventEntity").executeUpdate();
    }

    private void insertData() {
        for (int i = 0; i < 3; i++) {
            EventEntity entity = factory.manufacturePojo(EventEntity.class);
            entityManager.persist(entity);
            eventList.add(entity);
        }
    }

    @Test
    void testCreateEvent() throws IllegalOperationException {
        EventEntity newEntity = factory.manufacturePojo(EventEntity.class);
        newEntity.setName("Evento de Prueba");
        newEntity.setType("Tipo válido");

        EventEntity result = eventService.createEvent(newEntity);

        assertNotNull(result);
        EventEntity entity = entityManager.find(EventEntity.class, result.getId());
        assertEquals(newEntity.getName(), entity.getName());
    }

    @Test
    void testCreateEventWithNullAttribute() {
        assertThrows(IllegalOperationException.class, () -> {
            EventEntity newEntity = new EventEntity();
            newEntity.setName(null); // Atributo nulo gatilla la regla
            eventService.createEvent(newEntity);
        });
    }

    @Test
    void testGetEvents() {
        List<EventEntity> list = eventService.getEvents();
        assertEquals(eventList.size(), list.size());
    }
}