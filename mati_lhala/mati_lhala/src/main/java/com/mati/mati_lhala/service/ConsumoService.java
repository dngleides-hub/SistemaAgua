package com.mati.mati_lhala.service;


import com.mati.mati_lhala.model.Consumo;
import com.mati.mati_lhala.repository.ConsumoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ConsumoService {
    private final ConsumoRepository consumoRepository;
    private final UtenteService utenteService;

    public List<Consumo> findAll() {
        return consumoRepository.findAll();
    }

    public Optional<Consumo> findById(Long id) {
        return consumoRepository.findById(id);
    }

    public Consumo save(Consumo consumo) {
        return consumoRepository.save(consumo);
    }

    public void deleteById(Long id) {
        consumoRepository.deleteById(id);
    }

    public List<Consumo> findByUtenteId(Long utenteId) {
        return consumoRepository.findByUtenteId(utenteId);
    }

    public Double calcularTotalConsumoPeriodo(Long utenteId, LocalDate inicio, LocalDate fim) {
        return consumoRepository.findTotalConsumoByUtenteAndPeriod(utenteId, inicio, fim);
    }

    public List<Consumo> registrarConsumo(Consumo consumo) {
        consumoRepository.save(consumo);
        return consumoRepository.findByUtenteId(consumo.getUtente().getId());
    }
}