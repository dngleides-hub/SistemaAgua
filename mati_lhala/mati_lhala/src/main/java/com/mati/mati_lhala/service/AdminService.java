package com.mati.mati_lhala.service;


import com.mati.mati_lhala.model.Admin;
import com.mati.mati_lhala.repository.AdminRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AdminService {
    private final AdminRepository adminRepository;

    public List<Admin> findAll() {
        return adminRepository.findAll();
    }

    public List<Admin> findAllActive() {
        return adminRepository.findByAtivoTrue();
    }

    public Optional<Admin> findById(Long id) {
        return adminRepository.findById(id);
    }

    public Admin save(Admin admin) {
        return adminRepository.save(admin);
    }

    public void deleteById(Long id) {
        adminRepository.deleteById(id);
    }

    public Admin deactivateAdmin(Long id) {
        Optional<Admin> adminOpt = adminRepository.findById(id);
        if (adminOpt.isPresent()) {
            Admin admin = adminOpt.get();
            admin.setAtivo(false);
            return adminRepository.save(admin);
        }
        return null;
    }

    public Admin activateAdmin(Long id) {
        Optional<Admin> adminOpt = adminRepository.findById(id);
        if (adminOpt.isPresent()) {
            Admin admin = adminOpt.get();
            admin.setAtivo(true);
            return adminRepository.save(admin);
        }
        return null;
    }

    public Optional<Admin> findByLogin(String login) {
        return adminRepository.findByLogin(login);
    }

    public boolean autenticar(String login, String senha) {
        return adminRepository.findByLogin(login)
                .map(admin -> admin.getSenha().equals(senha) && admin.isAtivo())
                .orElse(false);
    }

    public boolean existsByLogin(String login) {
        return adminRepository.existsByLogin(login);
    }

    public boolean existsByEmail(String email) {
        return adminRepository.existsByEmail(email);
    }
}