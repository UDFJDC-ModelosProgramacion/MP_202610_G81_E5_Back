package co.edu.udistrital.mdp.pets.controllers;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import co.edu.udistrital.mdp.pets.entities.AdopterEntity;
import co.edu.udistrital.mdp.pets.entities.ShelterEntity;
import co.edu.udistrital.mdp.pets.entities.VeterinarianEntity;
import co.edu.udistrital.mdp.pets.repositories.AdopterRepository;
import co.edu.udistrital.mdp.pets.repositories.ShelterRepository;
import co.edu.udistrital.mdp.pets.repositories.VeterinarianRepository;

@RestController
@RequestMapping("/users")
public class UserController {

    @Autowired
    private AdopterRepository adopterRepository;

    @Autowired
    private ShelterRepository shelterRepository;

    @Autowired
    private VeterinarianRepository veterinarianRepository;

    @PostMapping("/register")
    public ResponseEntity<Map<String, Object>> register(@RequestBody Map<String, String> body) {
        String email = body.get("email");
        String password = body.get("password");
        String role = body.get("role"); // "adopter", "shelter", "veterinarian"

        if (email == null || password == null || role == null) {
            Map<String, Object> err = new HashMap<>();
            err.put("message", "Email, contraseña y rol son requeridos");
            return new ResponseEntity<>(err, HttpStatus.BAD_REQUEST);
        }

        Map<String, Object> response = new HashMap<>();

        switch (role.toLowerCase()) {
            case "adopter":
                if (adopterRepository.findByEmail(email).isPresent()) {
                    return emailConflictResponse();
                }
                AdopterEntity adopter = new AdopterEntity();
                adopter.setEmail(email);
                adopter.setPassword(password);
                adopter.setHousingType(body.getOrDefault("housingType", "Sin especificar"));
                adopter.setHasOtherPets(Boolean.parseBoolean(body.getOrDefault("hasOtherPets", "false")));
                AdopterEntity savedAdopter = adopterRepository.save(adopter);
                response.put("id", savedAdopter.getId());
                break;

            case "shelter":
                if (shelterRepository.findByEmail(email).isPresent()) {
                    return emailConflictResponse();
                }
                ShelterEntity shelter = new ShelterEntity();
                shelter.setEmail(email);
                shelter.setPassword(password);
                shelter.setName(body.getOrDefault("name", "Nuevo Refugio"));
                shelter.setCity(body.getOrDefault("city", ""));
                shelter.setLocation(body.getOrDefault("location", ""));
                ShelterEntity savedShelter = shelterRepository.save(shelter);
                response.put("id", savedShelter.getId());
                break;

            case "veterinarian":
                if (veterinarianRepository.findByEmail(email).isPresent()) {
                    return emailConflictResponse();
                }
                VeterinarianEntity vet = new VeterinarianEntity();
                vet.setEmail(email);
                vet.setPassword(password);
                
                vet.setLicenseNumber(body.get("licenseNumber"));
                vet.setSpecialty(body.get("specialty"));
                vet.setAvailability(body.get("availability"));
                
                String shelterIdStr = body.get("shelterId");
                if (shelterIdStr != null) {
                    ShelterEntity shelter1 = shelterRepository.findById(Long.parseLong(shelterIdStr))
                                                            .orElseThrow(() -> new RuntimeException("Refugio no encontrado"));
                    vet.setShelter(shelter1);
                }
                
                veterinarianRepository.save(vet);
                response.put("id", vet.getId());
                break;

            default:
                Map<String, Object> err = new HashMap<>();
                err.put("message", "Rol no válido");
                return new ResponseEntity<>(err, HttpStatus.BAD_REQUEST);
        }

        response.put("email", email);
        response.put("role", role);
        response.put("message", "Usuario registrado correctamente con rol: " + role);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @PostMapping("/login")
    public ResponseEntity<Map<String, Object>> login(@RequestBody Map<String, String> body) {
        String email = body.get("email");
        String password = body.get("password");
        String role = body.get("role"); // El front debe decir qué tipo de usuario intenta ingresar

        if (email == null || password == null || role == null) {
            Map<String, Object> err = new HashMap<>();
            err.put("message", "Email, contraseña y rol son requeridos");
            return new ResponseEntity<>(err, HttpStatus.BAD_REQUEST);
        }

        Map<String, Object> response = new HashMap<>();
        boolean authenticated = false;
        Long id = null;

        switch (role.toLowerCase()) {
            case "adopter":
                Optional<AdopterEntity> adopter = adopterRepository.findByEmail(email);
                if (adopter.isPresent() && adopter.get().getPassword().equals(password)) {
                    authenticated = true;
                    id = adopter.get().getId();
                }
                break;
            case "shelter":
                Optional<ShelterEntity> shelter = shelterRepository.findByEmail(email);
                if (shelter.isPresent() && shelter.get().getPassword().equals(password)) {
                    authenticated = true;
                    id = shelter.get().getId();
                }
                break;
            case "veterinarian":
                Optional<VeterinarianEntity> vet = veterinarianRepository.findByEmail(email);
                if (vet.isPresent() && vet.get().getPassword().equals(password)) {
                    authenticated = true;
                    id = vet.get().getId();
                }
                break;
        }

        if (!authenticated) {
            Map<String, Object> err = new HashMap<>();
            err.put("message", "Credenciales inválidas para el rol especificado");
            return new ResponseEntity<>(err, HttpStatus.UNAUTHORIZED);
        }

        response.put("id", id);
        response.put("email", email);
        response.put("role", role);
        response.put("message", "Login exitoso");
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Map<String, Object>> getUser(@PathVariable Long id, @RequestParam String role) {
        Map<String, Object> r = new HashMap<>();
        
        switch (role.toLowerCase()) {
            case "adopter":
                return adopterRepository.findById(id)
                    .map(a -> {
                        r.put("id", a.getId());
                        r.put("email", a.getEmail());
                        r.put("role", "adopter");
                        r.put("housingType", a.getHousingType());
                        return new ResponseEntity<>(r, HttpStatus.OK);
                    }).orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
            
            case "shelter":
                return shelterRepository.findById(id)
                    .map(s -> {
                        r.put("id", s.getId());
                        r.put("email", s.getEmail());
                        r.put("role", "shelter");
                        r.put("name", s.getName());
                        return new ResponseEntity<>(r, HttpStatus.OK);
                    }).orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));

            case "veterinarian":
                return veterinarianRepository.findById(id)
                    .map(v -> {
                        r.put("id", v.getId());
                        r.put("email", v.getEmail());
                        r.put("role", "veterinarian");
                        r.put("specialty", v.getSpecialty());
                        return new ResponseEntity<>(r, HttpStatus.OK);
                    }).orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
            
            default:
                return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    private ResponseEntity<Map<String, Object>> emailConflictResponse() {
        Map<String, Object> err = new HashMap<>();
        err.put("message", "Ya existe una cuenta con ese email");
        return new ResponseEntity<>(err, HttpStatus.CONFLICT);
    }
}