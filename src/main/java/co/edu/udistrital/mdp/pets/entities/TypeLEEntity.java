package co.edu.udistrital.mdp.pets.entities;

import jakarta.persistence.Entity;
import lombok.Data;

@Data
@Entity
public class TypeLEEntity extends BaseEntity {

    private String name;
}
