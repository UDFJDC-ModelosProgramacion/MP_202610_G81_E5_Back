package co.edu.udistrital.mdp.pets.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import lombok.Data;
import java.util.List;

import org.hibernate.annotations.ManyToAny;

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
    private List<MedicalRecordEntity> medicalRecords;

    @OneToMany(mappedBy = "pet")
    private List<LifeEventEntity> lifeEvents;

    @ManyToOne
    @JoinColumn(name = "shelter_id")
    private ShelterEntity shelter;

    @ManyToOne
    @JoinColumn(name = "adopter_id")
    private AdopterEntity adopter;

    @ManyToOne
    @JoinColumn(name = "process_id")
    private AdoptionProcessEntity adoptionProcess;
}
