package com.mati.mati_lhala.repository;


import com.mati.mati_lhala.model.Pagamento;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface PagamentoRepository extends JpaRepository<Pagamento, Long> {
    List<Pagamento> findByUtenteId(Long utenteId);
    List<Pagamento> findByFacturaId(Long facturaId);
}