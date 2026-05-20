package co.edu.udistrital.mdp.pets.controllers;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import co.edu.udistrital.mdp.pets.entities.AdopterEntity;
import co.edu.udistrital.mdp.pets.repositories.AdopterRepository;

@RestController
@RequestMapping("/users")
public class UserController {

    @Autowired
    private AdopterRepository adopterRepository;

    @PostMapping("/register")
    public ResponseEntity<Map<String, Object>> register(@RequestBody Map<String, String> body) {
        String email = body.get("email");
        String password = body.get("password");

        if (email == null || password == null) {
            Map<String, Object> err = new HashMap<>();
            err.put("message", "Email y contraseña son requeridos");
            return new ResponseEntity<>(err, HttpStatus.BAD_REQUEST);
        }

        // Verificar si ya existe un usuario con ese email
        Optional<AdopterEntity> existing = adopterRepository.findByEmail(email);
        if (existing.isPresent()) {
            Map<String, Object> err = new HashMap<>();
            err.put("message", "Ya existe una cuenta con ese email");
            return new ResponseEntity<>(err, HttpStatus.CONFLICT);
        }

        AdopterEntity adopter = new AdopterEntity();
        adopter.setEmail(email);
        adopter.setPassword(password);
        adopter.setHousingType("Sin especificar");
        adopter.setHasOtherPets(false);

        AdopterEntity saved = adopterRepository.save(adopter);

        Map<String, Object> response = new HashMap<>();
        response.put("id", saved.getId());
        response.put("email", email);
        response.put("message", "Usuario registrado correctamente");

        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @PostMapping("/login")
    public ResponseEntity<Map<String, Object>> login(@RequestBody Map<String, String> body) {
        String email = body.get("email");
        String password = body.get("password");

        if (email == null || password == null) {
            Map<String, Object> err = new HashMap<>();
            err.put("message", "Email y contraseña son requeridos");
            return new ResponseEntity<>(err, HttpStatus.BAD_REQUEST);
        }

        Optional<AdopterEntity> adopter = adopterRepository.findByEmail(email);

        if (adopter.isEmpty() || !adopter.get().getPassword().equals(password)) {
            Map<String, Object> err = new HashMap<>();
            err.put("message", "Credenciales inválidas");
            return new ResponseEntity<>(err, HttpStatus.UNAUTHORIZED);
        }

        Map<String, Object> response = new HashMap<>();
        response.put("id", adopter.get().getId());
        response.put("email", email);
        response.put("message", "Login exitoso");

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Map<String, Object>> getUser(@PathVariable Long id) {
        return adopterRepository.findById(id)
            .map(a -> {
                Map<String, Object> r = new HashMap<>();
                r.put("id", a.getId());
                r.put("email", a.getEmail());
                r.put("housingType", a.getHousingType());
                return new ResponseEntity<>(r, HttpStatus.OK);
            })
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }
}
