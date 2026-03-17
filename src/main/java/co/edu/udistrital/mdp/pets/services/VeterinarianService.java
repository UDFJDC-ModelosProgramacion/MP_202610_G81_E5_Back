package co.edu.udistrital.mdp.pets.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import co.edu.udistrital.mdp.pets.entities.VeterinarianEntity;
import co.edu.udistrital.mdp.pets.repositories.VeterinarianRepository;

import java.util.List;
import java.util.Optional;

@Service
public class VeterinarianService {

    @Autowired
    private VeterinarianRepository veterinarianRepository;


    public List<VeterinarianEntity> getVeterinarians() {
        return veterinarianRepository.findAll();
    }

   
    public VeterinarianEntity getVeterinarian(Long id) throws Exception {
        Optional<VeterinarianEntity> vet = veterinarianRepository.findById(id);

        if(vet.isEmpty()){
            throw new Exception("Veterinario no encontrado");
        }

        return vet.get();
    }


    public VeterinarianEntity createVeterinarian(VeterinarianEntity veterinarian) throws Exception {

        if(veterinarianRepository.existsByLicenseNumber(veterinarian.getLicenseNumber())){
            throw new Exception("La licencia ya existe");
        }

        return veterinarianRepository.save(veterinarian);
    }

  
    public VeterinarianEntity updateVeterinarian(Long id, VeterinarianEntity veterinarian) throws Exception {

        Optional<VeterinarianEntity> existing = veterinarianRepository.findById(id);

        if(existing.isEmpty()){
            throw new Exception("Veterinario no existe");
        }


        if(veterinarianRepository.existsByLicenseNumber(veterinarian.getLicenseNumber())
            && !existing.get().getLicenseNumber().equals(veterinarian.getLicenseNumber())){
            throw new Exception("La licencia ya está en uso");
        }

        veterinarian.setId(id);
        return veterinarianRepository.save(veterinarian);
    }

    public void deleteVeterinarian(Long id) throws Exception {

        Optional<VeterinarianEntity> vet = veterinarianRepository.findById(id);

        if(vet.isEmpty()){
            throw new Exception("Veterinario no existe");
        }

        if(!vet.get().getMedicalRecords().isEmpty()){
            throw new Exception("No se puede eliminar, tiene historial médico");
        }

        veterinarianRepository.deleteById(id);
    }
}
