package co.edu.udistrital.mdp.pets.controllers;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import co.edu.udistrital.mdp.pets.dto.AdopterDTO;
import co.edu.udistrital.mdp.pets.entities.AdopterEntity;
import co.edu.udistrital.mdp.pets.exceptions.EntityNotFoundException;
import co.edu.udistrital.mdp.pets.exceptions.IllegalOperationException;
import co.edu.udistrital.mdp.pets.services.AdopterService;

@RestController
@RequestMapping("/api/adopters")
public class AdopterController {

    @Autowired
    private AdopterService adopterService;

    @GetMapping
    public ResponseEntity<List<AdopterDTO>> getAdopters() {
        List<AdopterDTO> adopters = adopterService.getAdopters().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
        return new ResponseEntity<>(adopters, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<AdopterDTO> getAdopter(@PathVariable Long id) throws EntityNotFoundException {
        AdopterEntity entity = adopterService.getAdopter(id);
        return new ResponseEntity<>(convertToDTO(entity), HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<AdopterDTO> createAdopter(@RequestBody AdopterDTO dto) throws IllegalOperationException {
        AdopterEntity entity = convertToEntity(dto);
        AdopterEntity created = adopterService.createAdopter(entity);
        return new ResponseEntity<>(convertToDTO(created), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<AdopterDTO> updateAdopter(@PathVariable Long id, @RequestBody AdopterDTO dto) 
            throws EntityNotFoundException, IllegalOperationException {
        AdopterEntity entity = convertToEntity(dto);
        AdopterEntity updated = adopterService.updateAdopter(id, entity);
        return new ResponseEntity<>(convertToDTO(updated), HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAdopter(@PathVariable Long id) 
            throws EntityNotFoundException, IllegalOperationException {
        adopterService.deleteAdopter(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    private AdopterDTO convertToDTO(AdopterEntity entity) {
        AdopterDTO dto = new AdopterDTO();
        dto.setId(entity.getId());
        dto.setHousingType(entity.getHousingType());
        dto.setHasOtherPets(entity.getHasOtherPets());
        return dto;
    }

    private AdopterEntity convertToEntity(AdopterDTO dto) {
        AdopterEntity entity = new AdopterEntity();
        entity.setId(dto.getId());
        entity.setHousingType(dto.getHousingType());
        entity.setHasOtherPets(dto.getHasOtherPets());
        return entity;
    }
}