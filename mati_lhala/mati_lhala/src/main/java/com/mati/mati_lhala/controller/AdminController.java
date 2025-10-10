package com.mati.mati_lhala.controller;

import com.mati.mati_lhala.model.Admin;
import com.mati.mati_lhala.service.AdminService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/admins")
@RequiredArgsConstructor
public class AdminController {
    private final AdminService adminService;

    @GetMapping
    public List<Admin> getAllAdmins() {
        return adminService.findAll();
    }

    @GetMapping("/ativos")
    public List<Admin> getActiveAdmins() {
        return adminService.findAllActive();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Admin> getAdminById(@PathVariable Long id) {
        return adminService.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<?> createAdmin(@RequestBody Admin admin) {
        // Verificar se login já existe
        if (adminService.existsByLogin(admin.getLogin())) {
            return ResponseEntity.badRequest().body("Login já está em uso");
        }

        // Verificar se email já existe
        if (adminService.existsByEmail(admin.getEmail())) {
            return ResponseEntity.badRequest().body("Email já está em uso");
        }

        Admin savedAdmin = adminService.save(admin);
        return ResponseEntity.ok(savedAdmin);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateAdmin(@PathVariable Long id, @RequestBody Admin admin) {
        if (!adminService.findById(id).isPresent()) {
            return ResponseEntity.notFound().build();
        }

        // Verificar se outro admin já usa o login
        Optional<Admin> adminByLogin = adminService.findByLogin(admin.getLogin());
        if (adminByLogin.isPresent() && !adminByLogin.get().getId().equals(id)) {
            return ResponseEntity.badRequest().body("Login já está em uso por outro admin");
        }

        admin.setId(id);
        Admin updatedAdmin = adminService.save(admin);
        return ResponseEntity.ok(updatedAdmin);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAdmin(@PathVariable Long id) {
        if (!adminService.findById(id).isPresent()) {
            return ResponseEntity.notFound().build();
        }
        adminService.deleteById(id);
        return ResponseEntity.ok().build();
    }

    @PatchMapping("/{id}/desativar")
    public ResponseEntity<Admin> deactivateAdmin(@PathVariable Long id) {
        Admin deactivatedAdmin = adminService.deactivateAdmin(id);
        if (deactivatedAdmin == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(deactivatedAdmin);
    }

    @PatchMapping("/{id}/ativar")
    public ResponseEntity<Admin> activateAdmin(@PathVariable Long id) {
        Admin activatedAdmin = adminService.activateAdmin(id);
        if (activatedAdmin == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(activatedAdmin);
    }

    // CORREÇÃO: Mudar para @RequestBody para evitar Bad Request
    @PostMapping("/autenticar")
    public ResponseEntity<Boolean> autenticar(@RequestBody LoginRequest loginRequest) {
        boolean autenticado = adminService.autenticar(loginRequest.getLogin(), loginRequest.getSenha());
        return ResponseEntity.ok(autenticado);
    }

    @GetMapping("/login/{login}")
    public ResponseEntity<Admin> getAdminByLogin(@PathVariable String login) {
        return adminService.findByLogin(login)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // Classe DTO interna para o login (ou crie uma classe separada)
    public static class LoginRequest {
        private String login;
        private String senha;

        // Getters e Setters
        public String getLogin() {
            return login;
        }

        public void setLogin(String login) {
            this.login = login;
        }

        public String getSenha() {
            return senha;
        }

        public void setSenha(String senha) {
            this.senha = senha;
        }
    }
}