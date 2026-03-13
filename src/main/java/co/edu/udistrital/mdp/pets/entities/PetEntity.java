package co.edu.udistrital.mdp.pets.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
@Entity

public class PetEntity extends BaseEntity {

    private String name;
    private String species;
    private String breed;
    private Integer age;
    private String status;
    private String temperament;
    private Boolean compKids;
    private Boolean compOtherDogs;
    
    @OneToMany(mappedBy = "pet")
    private List<MedicalRecordEntity> medicalRecords=new ArrayList<>();

    @OneToMany(mappedBy = "pet")
    private List<LifeEventEntity> lifeEvents=new ArrayList<>();
    
    @OneToMany(mappedBy = "pet")
    private List<AdoptionProcessEntity> adoptionProcess=new ArrayList<>();

    @ManyToOne
    @JoinColumn(name = "shelter_id")
    private ShelterEntity shelter;

    @ManyToOne
    @JoinColumn(name = "adopter_id")
    private AdopterEntity adopter;

}
