package co.edu.udistrital.mdp.pets.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import co.edu.udistrital.mdp.pets.entities.ShelterEntity;
import co.edu.udistrital.mdp.pets.exceptions.EntityNotFoundException;
import co.edu.udistrital.mdp.pets.exceptions.IllegalOperationException;
import co.edu.udistrital.mdp.pets.repositories.ShelterRepository;

@Service
public class ShelterService {

    @Autowired
    private ShelterRepository repository;

    @Transactional
    public List<ShelterEntity> getShelters() {
        return repository.findAll();
    }

    @Transactional
    public ShelterEntity getShelter(Long id) throws EntityNotFoundException {
        return repository.findById(id).orElseThrow(() -> 
            new EntityNotFoundException("Shelter no encontrado"));
    }

    @Transactional
    public ShelterEntity createShelter(ShelterEntity entity) throws IllegalOperationException {
   
        if (entity.getName() == null || entity.getLocation() == null) {
            throw new IllegalOperationException("Los atributos no deben ser nulos");
        }
        
        return repository.save(entity);
    }

    @Transactional
    public ShelterEntity updateShelter(Long id, ShelterEntity entity) 
            throws EntityNotFoundException, IllegalOperationException {
        
        ShelterEntity existing = getShelter(id);

        if (!existing.getLocation().equals(entity.getLocation())) {
 
            if (existing.getPets() != null && !existing.getPets().isEmpty()) {
                throw new IllegalOperationException("No se puede modificar la ubicación si el refugio tiene mascotas asignadas");
            }
        }

        existing.setName(entity.getName());
        existing.setCity(entity.getCity());
        existing.setLocation(entity.getLocation());
        
        return repository.save(existing);
    }

    @Transactional
    public void deleteShelter(Long id) throws EntityNotFoundException, IllegalOperationException {
        ShelterEntity entity = getShelter(id);

        if (entity.getPets() != null && !entity.getPets().isEmpty()) {
            throw new IllegalOperationException("No se puede eliminar un refugio con mascotas asignadas");
        }

        repository.delete(entity);
    }
}
