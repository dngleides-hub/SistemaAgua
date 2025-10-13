package com.mati.mati_lhala.controller;


import com.mati.mati_lhala.dto.LoginRequest;
import com.mati.mati_lhala.dto.UtenteDTO;
import com.mati.mati_lhala.model.Utente;
import com.mati.mati_lhala.service.UtenteService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/utentes")
@RequiredArgsConstructor
public class UtenteController {
    private final UtenteService utenteService;

    @GetMapping
    public List<Utente> getAllUtentes() {
        return utenteService.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Utente> getUtenteById(@PathVariable Long id) {
        return utenteService.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public Utente createUtente(@RequestBody Utente utente) {
        return utenteService.save(utente);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Utente> updateUtente(@PathVariable Long id, @RequestBody Utente utente) {
        if (!utenteService.findById(id).isPresent()) {
            return ResponseEntity.notFound().build();
        }
        utente.setId(id);
        return ResponseEntity.ok(utenteService.save(utente));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUtente(@PathVariable Long id) {
        if (!utenteService.findById(id).isPresent()) {
            return ResponseEntity.notFound().build();
        }
        utenteService.deleteById(id);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/autenticar")
    public ResponseEntity<Boolean> autenticar(@RequestBody LoginRequest loginRequest) {
        System.out.println("Tentativa de login: " + loginRequest.getLogin());
        boolean autenticado = utenteService.autenticar(loginRequest.getLogin(), loginRequest.getSenha());
        return ResponseEntity.ok(autenticado);
    }

    @GetMapping("/login/{login}")
    public ResponseEntity<UtenteDTO> getUtentePorLogin(@PathVariable String login) {
        System.out.println("Buscando utente por login: " + login);
        return utenteService.findByLogin(login)
                .map(UtenteDTO::new) // Converte para DTO
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
}