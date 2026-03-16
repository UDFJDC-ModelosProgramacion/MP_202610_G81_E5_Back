package co.edu.udistrital.mdp.pets.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import co.edu.udistrital.mdp.pets.entities.EventEntity;
import co.edu.udistrital.mdp.pets.exceptions.EntityNotFoundException;
import co.edu.udistrital.mdp.pets.exceptions.IllegalOperationException;
import co.edu.udistrital.mdp.pets.repositories.EventRepository;

@Service
public class EventService {

    @Autowired
    private EventRepository repository;

    @Transactional
    public List<EventEntity> getEvents() {
        return repository.findAll();
    }

    @Transactional
    public EventEntity getEvent(Long id) throws EntityNotFoundException {
        return repository.findById(id).orElseThrow(() -> 
            new EntityNotFoundException("El evento con el id dado no fue encontrado"));
    }

    @Transactional
    public EventEntity createEvent(EventEntity entity) throws IllegalOperationException {

        if (entity.getName() == null || entity.getDescription() == null) {
            throw new IllegalOperationException("Los atributos no deben ser nulos");
        }
        return repository.save(entity);
    }

    @Transactional
    public EventEntity updateEvent(Long id, EventEntity entity) 
            throws EntityNotFoundException, IllegalOperationException {
        
        EventEntity existing = getEvent(id);

        if (existing.getEnCurso() != null && existing.getEnCurso()) {
            throw new IllegalOperationException("No se puede actualizar un evento si está en curso");
        }

        existing.setName(entity.getName());
        existing.setDescription(entity.getDescription());
        
        return repository.save(existing);
    }

    @Transactional
    public void deleteEvent(Long id) throws EntityNotFoundException, IllegalOperationException {
        EventEntity entity = getEvent(id);

        if (entity.getEnCurso() != null && entity.getEnCurso()) {
            throw new IllegalOperationException("No se puede eliminar un evento en curso");
        }

        repository.delete(entity);
    }
}
