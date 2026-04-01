package co.edu.udistrital.mdp.pets.dto;
import java.util.ArrayList;
import java.util.List;

import lombok.Data;

@Data
public class TypeLEDetailDTO {
    private List<LifeEventDTO> lifeEvents = new ArrayList<>();
}
