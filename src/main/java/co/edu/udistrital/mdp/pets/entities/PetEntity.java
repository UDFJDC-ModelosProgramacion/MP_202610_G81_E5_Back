package co.edu.udistrital.mdp.pets.entities;
 
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;

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
    private String specie;
    private String breed;
    private Integer age;
    private String status;
    private String temperament;
    private Boolean compKids;
    private Boolean compOtherPets;
 
    @PodamExclude
    @OneToMany(mappedBy = "pet")
    @JsonManagedReference("pet-medicalrecords")
    private List<MedicalRecordEntity> medicalRecords = new ArrayList<>();
 
    @PodamExclude
    @OneToMany(mappedBy = "pet")
    @JsonManagedReference("pet-lifeevents")
    private List<LifeEventEntity> lifeEvents = new ArrayList<>();
 
    @PodamExclude
    @OneToMany(mappedBy = "pet")
    @JsonIgnore
    private List<AdoptionProcessEntity> adoptionProcess = new ArrayList<>();
 
    @PodamExclude
    @OneToMany(mappedBy = "pet")
    @JsonIgnore
    private List<AdoptionRequestEntity> adoptionRequest = new ArrayList<>();
 
    @PodamExclude
    @ManyToOne
    @JoinColumn(name = "shelter_id")
    @JsonBackReference("shelter-pets")
    private ShelterEntity shelter;
 
    @PodamExclude
    @ManyToOne
    @JoinColumn(name = "adopter_id")
    @JsonBackReference("adopter-pets")
    private AdopterEntity adopter;
}
