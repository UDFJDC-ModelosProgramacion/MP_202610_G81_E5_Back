package co.edu.udistrital.mdp.pets.entities;

import java.util.List;

import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import lombok.Data;

@Data
@Entity
public class TypeLEEntity extends BaseEntity {

    private String name;

    @OneToMany(mappedBy = "type")
    private List<LifeEventEntity> lifeEvents;
}
