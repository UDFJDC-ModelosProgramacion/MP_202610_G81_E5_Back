package co.edu.udistrital.mdp.pets.dto;

import lombok.Data;

@Data
public class PetDTO {
    private Long id;
    private String name;
    private String specie;
    private String breed;
    private Integer age;
    private String status;
    private String temperament;
    private Boolean compKids;
    private Boolean compOtherPets;
    private ShelterDTO shelter;
    private AdopterDTO adopter;
}
