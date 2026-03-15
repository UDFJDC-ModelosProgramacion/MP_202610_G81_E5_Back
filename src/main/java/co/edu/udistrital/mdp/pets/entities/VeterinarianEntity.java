package co.edu.udistrital.mdp.pets.entities;
import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import lombok.Data;
import uk.co.jemos.podam.common.PodamExclude;

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

    @PodamExclude
    @OneToMany(mappedBy = "veterinarian")
    private List<MedicalRecordEntity> medicalRecords=new ArrayList <>();

    @PodamExclude
    @OneToMany(mappedBy = "veterinarian")
    private List<LifeEventEntity> lifeEvents=new ArrayList <>();

    @PodamExclude
    @OneToMany(mappedBy = "veterinarian")
    private List<AdoptionProcessEntity> adoptionProcesses=new ArrayList <>();
}
