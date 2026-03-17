package co.edu.udistrital.mdp.pets.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import co.edu.udistrital.mdp.pets.entities.AdopterEntity;
import co.edu.udistrital.mdp.pets.repositories.AdopterRepository;

import java.util.List;
import java.util.Optional;

@Service
public class AdopterService {

    @Autowired
    private AdopterRepository adopterRepository;


    public List<AdopterEntity> getAdopters(){
        return adopterRepository.findAll();
    }

    public AdopterEntity getAdopter(Long id) throws Exception{
        Optional<AdopterEntity> adop = adopterRepository.findById(id);

        if(adop.isEmpty()){
            throw new Exception("Adoptante no encontrado");
        }

        return adop.get();
    }


    public AdopterEntity createAdopter(AdopterEntity adopter){
        return adopterRepository.save(adopter);
    }

    public AdopterEntity updateAdopter(Long id, AdopterEntity adopter) throws Exception{

        Optional<AdopterEntity> existing = adopterRepository.findById(id);

        if(existing.isEmpty()){
            throw new Exception("Adoptante no encontrado");
        }

        adopter.setId(id);
        return adopterRepository.save(adopter);
    }


    public void deleteAdopter(Long id) throws Exception{

        Optional<AdopterEntity> adop = adopterRepository.findById(id);

        if(adop.isEmpty()){
            throw new Exception("Adoptante no existe");
        }

        if(!adop.get().getAdoptionRequest().isEmpty()){
            throw new Exception("No se puede eliminar adoptante con solicitudes");
        }

        adopterRepository.deleteById(id);
    }
}
