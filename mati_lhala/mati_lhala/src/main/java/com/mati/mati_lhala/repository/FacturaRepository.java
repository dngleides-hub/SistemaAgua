package com.mati.mati_lhala.repository;



import com.mati.mati_lhala.model.Factura;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface FacturaRepository extends JpaRepository<Factura, Long> {
    List<Factura> findByUtenteId(Long utenteId);
    List<Factura> findByPagaFalse();
    Optional<Factura> findByIdAndUtenteId(Long id, Long utenteId);
}