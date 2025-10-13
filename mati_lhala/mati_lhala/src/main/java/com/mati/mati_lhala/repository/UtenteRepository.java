package com.mati.mati_lhala.repository;



import com.mati.mati_lhala.model.Utente;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;


@Repository
public interface UtenteRepository extends JpaRepository<Utente, Long> {
    Optional<Utente> findByLogin(String login);
    Optional<Utente> findByCelular(String celular);

    boolean existsByLogin(String login);
}
