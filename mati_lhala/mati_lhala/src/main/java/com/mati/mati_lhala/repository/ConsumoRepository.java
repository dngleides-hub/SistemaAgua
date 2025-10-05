package com.mati.mati_lhala.repository;

import com.mati.mati_lhala.model.Consumo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import java.time.LocalDate;
import java.util.List;

@Repository
public interface ConsumoRepository extends JpaRepository<Consumo, Long> {
    List<Consumo> findByUtenteId(Long utenteId);
    List<Consumo> findByDataBetween(LocalDate startDate, LocalDate endDate);

    @Query("SELECT SUM(c.quantidade) FROM Consumo c WHERE c.utente.id = :utenteId AND c.data BETWEEN :startDate AND :endDate")
    Double findTotalConsumoByUtenteAndPeriod(Long utenteId, LocalDate startDate, LocalDate endDate);
}
