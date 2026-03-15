package co.edu.udistrital.mdp.pets.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import co.edu.udistrital.mdp.pets.entities.TypeLEEntity;
import co.edu.udistrital.mdp.pets.exceptions.EntityNotFoundException;
import co.edu.udistrital.mdp.pets.exceptions.IllegalOperationException;
import co.edu.udistrital.mdp.pets.repositories.LifeEventRepository;
import co.edu.udistrital.mdp.pets.repositories.TypeLERepository;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class TypeLEService {

    @Autowired
    TypeLERepository typeLERepository;

    @Autowired
    LifeEventRepository lifeEventRepository;

    @Transactional
    public TypeLEEntity createTypeLE(TypeLEEntity typeLEEntity) throws EntityNotFoundException, IllegalOperationException  {
        log.info("Inicia proceso de creación de tipo de evento de vida");

        if (typeLERepository.existsByName(typeLEEntity.getName())) {
            throw new IllegalOperationException("Ya existe un tipo de evento con ese nombre");
        }
        return typeLERepository.save(typeLEEntity);
        }

    @Transactional
	public List<TypeLEEntity> getTypeLEs() {
		log.info("Inicia proceso de consultar todos los tipos de eventos de vida");
		return typeLERepository.findAll();
	}


    @Transactional
    public TypeLEEntity getTypeLE(Long typeLEId) throws EntityNotFoundException {

        log.info("Inicia proceso de consulta del tipo de evento de vida con id = {0}", typeLEId);

        TypeLEEntity typeLEEntity = typeLERepository.findById(typeLEId).orElseThrow(
            () -> new EntityNotFoundException("Type of life event not found"));

        log.info("Termina proceso de consulta del tipo de evento de vida con id = {0}", typeLEId);

        return typeLEEntity;
    }

    @Transactional
    public TypeLEEntity updateTypeLE(Long typeLEId, TypeLEEntity typeLE) throws EntityNotFoundException, IllegalOperationException {

        log.info("Inicia proceso de actualizar el tipo de evento de vida con id = {0}", typeLEId);

        typeLERepository.findById(typeLEId).orElseThrow(
            () -> new EntityNotFoundException("Type of life event not found"));

        if (typeLERepository.existsByName(typeLE.getName())) {
            throw new IllegalOperationException("Ya existe un tipo de evento con ese nombre");
        }

        typeLE.setId(typeLEId);

        log.info("Termina proceso de actualizar el tipo de evento de vida con id = {0}", typeLEId);

        return typeLERepository.save(typeLE);

    }

    @Transactional
    public void deleteTypeLE(Long typeLEId) throws EntityNotFoundException, IllegalOperationException {

        log.info("Inicia proceso de borrar el tipo de evento de vida con id = {0}", typeLEId);

        typeLERepository.findById(typeLEId).orElseThrow(
            () -> new EntityNotFoundException("Type of life event not found"));

        if (lifeEventRepository.existsByTypeId(typeLEId)) {
            throw new IllegalOperationException(
                "No se puede borrar el tipo de evento de vida porque tiene eventos de vida asociados"
            );
        }

        typeLERepository.deleteById(typeLEId);

        log.info("Termina proceso de borrar el tipo de evento de vida con id = {0}", typeLEId);
    }
}
