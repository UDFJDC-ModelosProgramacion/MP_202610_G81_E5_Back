package co.edu.udistrital.mdp.pets.entities;

import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import lombok.Data;
import uk.co.jemos.podam.common.PodamExclude;

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
    
    @PodamExclude
    @OneToMany(mappedBy = "pet")
    private List<MedicalRecordEntity> medicalRecords=new ArrayList<>();

    @PodamExclude
    @OneToMany(mappedBy = "pet")
    private List<LifeEventEntity> lifeEvents=new ArrayList<>();
    
    @PodamExclude
    @OneToMany
    private List<AdoptionProcessEntity> adoptionProcess=new ArrayList<>();

    @PodamExclude
    @ManyToOne
    @JoinColumn(name = "shelter_id")
    private ShelterEntity shelter;

    @PodamExclude
    @ManyToOne
    @JoinColumn(name = "adopter_id")
    private AdopterEntity adopter;

}
