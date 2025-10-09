package com.mati.mati_lhala.repository;

import com.mati.mati_lhala.model.Admin;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AdminRepository extends JpaRepository<Admin, Long> {
    Optional<Admin> findByLogin(String login);
    Optional<Admin> findByEmail(String email);
    boolean existsByLogin(String login);
    boolean existsByEmail(String email);
    List<Admin> findByAtivoTrue();
}