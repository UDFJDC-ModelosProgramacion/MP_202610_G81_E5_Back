package co.edu.udistrital.mdp.pets.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import co.edu.udistrital.mdp.pets.entities.VeterinarianEntity;
import co.edu.udistrital.mdp.pets.repositories.VeterinarianRepository;
import co.edu.udistrital.mdp.pets.exceptions.EntityNotFoundException;
import co.edu.udistrital.mdp.pets.exceptions.IllegalOperationException;

import java.util.List;
import java.util.Optional;

@Service
public class VeterinarianService {

    @Autowired
    private VeterinarianRepository veterinarianRepository;

    
    public List<VeterinarianEntity> getVeterinarians() {
        return veterinarianRepository.findAll();
    }


    public VeterinarianEntity getVeterinarian(Long id) throws EntityNotFoundException {
        Optional<VeterinarianEntity> vet = veterinarianRepository.findById(id);

        if (vet.isEmpty()) {
            throw new EntityNotFoundException("Veterinario no encontrado");
        }

        return vet.get();
    }

    public VeterinarianEntity createVeterinarian(VeterinarianEntity veterinarian)
            throws IllegalOperationException {

        if (veterinarianRepository.existsByLicenseNumber(veterinarian.getLicenseNumber())) {
            throw new IllegalOperationException("La licencia ya existe");
        }

        return veterinarianRepository.save(veterinarian);
    }


    public VeterinarianEntity updateVeterinarian(Long id, VeterinarianEntity veterinarian)
            throws EntityNotFoundException, IllegalOperationException {

        Optional<VeterinarianEntity> existing = veterinarianRepository.findById(id);

        if (existing.isEmpty()) {
            throw new EntityNotFoundException("Veterinario no existe");
        }

        if (veterinarianRepository.existsByLicenseNumber(veterinarian.getLicenseNumber())
                && !existing.get().getLicenseNumber().equals(veterinarian.getLicenseNumber())) {
            throw new IllegalOperationException("La licencia ya está en uso");
        }

        veterinarian.setId(id);
        return veterinarianRepository.save(veterinarian);
    }


    public void deleteVeterinarian(Long id)
            throws EntityNotFoundException, IllegalOperationException {

        Optional<VeterinarianEntity> vet = veterinarianRepository.findById(id);

        if (vet.isEmpty()) {
            throw new EntityNotFoundException("Veterinario no existe");
        }

        if (!vet.get().getMedicalRecords().isEmpty()) {
            throw new IllegalOperationException("No se puede eliminar, tiene historial médico");
        }

        veterinarianRepository.deleteById(id);
    }
}
