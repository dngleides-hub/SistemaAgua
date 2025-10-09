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

        // Verificar se outro admin já usa o email
//        Optional<Admin> adminByEmail = adminService.findByEmail(admin.getEmail());
//        if (adminByEmail.isPresent() && !adminByEmail.get().getId().equals(id)) {
//            return ResponseEntity.badRequest().body("Email já está em uso por outro admin");
//        }

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

    @PostMapping("/autenticar")
    public ResponseEntity<Boolean> autenticar(@RequestParam String login, @RequestParam String senha) {
        boolean autenticado = adminService.autenticar(login, senha);
        return ResponseEntity.ok(autenticado);
    }

    @GetMapping("/login/{login}")
    public ResponseEntity<Admin> getAdminByLogin(@PathVariable String login) {
        return adminService.findByLogin(login)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
}