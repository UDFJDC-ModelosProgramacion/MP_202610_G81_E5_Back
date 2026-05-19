package co.edu.udistrital.mdp.pets.controllers;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import co.edu.udistrital.mdp.pets.entities.AdopterEntity;
import co.edu.udistrital.mdp.pets.repositories.AdopterRepository;

@RestController
@RequestMapping("/users")
public class UserController {

    @Autowired
    private AdopterRepository adopterRepository;

    @PostMapping("/register")
    public ResponseEntity<Map<String, Object>> register(@RequestBody Map<String, String> body) {
        AdopterEntity adopter = new AdopterEntity();
        adopter.setHousingType("Sin especificar");
        adopter.setHasOtherPets(false);

        AdopterEntity saved = adopterRepository.save(adopter);

        Map<String, Object> response = new HashMap<>();
        response.put("id", saved.getId());
        response.put("name", body.get("name"));
        response.put("email", body.get("email"));
        response.put("message", "Usuario registrado correctamente");

        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @PostMapping("/login")
    public ResponseEntity<Map<String, Object>> login(@RequestBody Map<String, String> body) {
        // Login simulado — reemplazar con lógica real cuando haya entidad User
        String email = body.get("email");
        String password = body.get("password");

        if (email == null || password == null || password.length() < 4) {
            Map<String, Object> err = new HashMap<>();
            err.put("message", "Credenciales inválidas");
            return new ResponseEntity<>(err, HttpStatus.UNAUTHORIZED);
        }

        // Buscar si hay algún adoptante (placeholder hasta tener auth real)
        Optional<AdopterEntity> adopter = adopterRepository.findAll()
                .stream().findFirst();

        Map<String, Object> response = new HashMap<>();
        response.put("email", email);
        response.put("id", adopter.map(a -> a.getId()).orElse(1L));
        response.put("message", "Login exitoso");

        return new ResponseEntity<>(response, HttpStatus.OK);
    }
    

    @GetMapping("/{id}")
    public ResponseEntity<Map<String, Object>> getUser(@PathVariable Long id) {
        return adopterRepository.findById(id)
            .map(a -> {
                Map<String, Object> r = new HashMap<>();
                r.put("id", a.getId());
                r.put("housingType", a.getHousingType());
                return new ResponseEntity<>(r, HttpStatus.OK);
            })
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }
    
}