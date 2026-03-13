package co.edu.udistrital.mdp.pets.entities;

import java.util.List;
import java.util.ArrayList;

import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import lombok.Data;
import uk.co.jemos.podam.common.PodamExclude;

@Data
@Entity
public class TypeLEEntity extends BaseEntity {

    private String name;

    @PodamExclude
    @OneToMany(mappedBy = "type")
    private List<LifeEventEntity> lifeEvents = new ArrayList<>();
}
