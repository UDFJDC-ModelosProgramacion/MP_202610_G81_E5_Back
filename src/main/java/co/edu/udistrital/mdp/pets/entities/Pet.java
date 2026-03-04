package co.edu.udistrital.mdp.pets.entities;

import jakarta.persistence.Entity;
import lombok.Data;

@Data
@Entity

public class Pet extends BaseEntity {

    private String name;
    private String species;
    private String breed;
    private Integer age;
    private String status;
    private String temperament;
    private Boolean compKids;
    private Boolean compOtherDogs;
    
}
