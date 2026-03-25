package co.edu.udistrital.mdp.ZZZ.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import co.edu.udistrital.mdp.pets.entities.AdopterEntity;
import co.edu.udistrital.mdp.pets.repositories.AdopterRepository;
import co.edu.udistrital.mdp.pets.exceptions.EntityNotFoundException;
import co.edu.udistrital.mdp.pets.exceptions.IllegalOperationException;

import java.util.List;
import java.util.Optional;

@Service
public class AdopterService {

    @Autowired
    private AdopterRepository adopterRepository;

    public List<AdopterEntity> getAdopters() {
        return adopterRepository.findAll();
    }

    public AdopterEntity getAdopter(Long id) throws EntityNotFoundException {
        Optional<AdopterEntity> adop = adopterRepository.findById(id);

        if (adop.isEmpty()) {
            throw new EntityNotFoundException("Adoptante no encontrado");
        }

        return adop.get();
    }

    public AdopterEntity createAdopter(AdopterEntity adopter) throws IllegalOperationException {
        return adopterRepository.save(adopter);
    }

    public AdopterEntity updateAdopter(Long id, AdopterEntity adopter)
            throws EntityNotFoundException {

        Optional<AdopterEntity> existing = adopterRepository.findById(id);

        if (existing.isEmpty()) {
            throw new EntityNotFoundException("Adoptante no encontrado");
        }

        adopter.setId(id);
        return adopterRepository.save(adopter);
    }

    public void deleteAdopter(Long id)
            throws EntityNotFoundException, IllegalOperationException {

        Optional<AdopterEntity> adop = adopterRepository.findById(id);

        if (adop.isEmpty()) {
            throw new EntityNotFoundException("Adoptante no existe");
        }


        if (!adop.get().getAdoptionRequest().isEmpty()) {
            throw new IllegalOperationException("No se puede eliminar adoptante con solicitudes");
        }

        adopterRepository.deleteById(id);
    }
}
