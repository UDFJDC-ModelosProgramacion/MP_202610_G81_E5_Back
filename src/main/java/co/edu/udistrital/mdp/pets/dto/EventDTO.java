package co.edu.udistrital.mdp.pets.dto;

import java.util.Date;

import lombok.Data;

@Data
public class EventDTO {
    private Long id; 
    private String name;
    private Date date;
    private String type;
    
    
    private Long shelterId;
}