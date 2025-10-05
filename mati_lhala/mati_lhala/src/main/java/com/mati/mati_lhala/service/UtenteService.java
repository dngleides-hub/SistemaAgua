package com.mati.mati_lhala.service;



import com.mati.mati_lhala.model.Utente;
import com.mati.mati_lhala.repository.UtenteRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UtenteService {
    private final UtenteRepository utenteRepository;

    public List<Utente> findAll() {
        return utenteRepository.findAll();
    }

    public Optional<Utente> findById(Long id) {
        return utenteRepository.findById(id);
    }

    public Utente save(Utente utente) {
        return utenteRepository.save(utente);
    }

    public void deleteById(Long id) {
        utenteRepository.deleteById(id);
    }

    public Optional<Utente> findByLogin(String login) {
        return utenteRepository.findByLogin(login);
    }

    public boolean autenticar(String login, String senha) {
        return utenteRepository.findByLogin(login)
                .map(utente -> utente.getSenha().equals(senha))
                .orElse(false);
    }
}