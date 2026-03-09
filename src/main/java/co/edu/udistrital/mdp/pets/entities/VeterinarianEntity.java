package co.edu.udistrital.mdp.pets.entities;
import jakarta.persistence.*;
import uk.co.jemos.podam.common.PodamExclude;
import lombok.Data;

@Data
@Entity

public class VeterinarianEntity extends BaseEntity {
    private String licenseNumber;
    private String specialty;
    private String availability;

    @ManyToOne
    @JoinColumn(name = "shelter_id")
    @PodamExclude
    private ShelterEntity shelter;

    @OneToOne
    @JoinColumn(name = "trialCohabitation_id")
    @PodamExclude
    private TrialCohabitationEntity trialCohabitation;


    @OneToMany(mappedBy = "veterinarian")
    private List<MedicalRecord> medicalRecords;

    @OneToMany(mappedBy = "veterinarian")
    private List<LifeEvent> lifeEvents;

    @OneToMany(mappedBy = "veterinarian")
    private List<AdoptionProcess> adoptionProcesses;
    

}
