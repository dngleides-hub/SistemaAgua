package com.mati.mati_lhala.service;


import com.mati.mati_lhala.model.Factura;
import com.mati.mati_lhala.repository.FacturaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class FacturaService {
    private final FacturaRepository facturaRepository;
    private final UtenteService utenteService;

    public List<Factura> findAll() {
        return facturaRepository.findAll();
    }

    public Optional<Factura> findById(Long id) {
        return facturaRepository.findById(id);
    }

    public Factura save(Factura factura) {
        return facturaRepository.save(factura);
    }

    public void deleteById(Long id) {
        facturaRepository.deleteById(id);
    }

    public List<Factura> findByUtenteId(Long utenteId) {
        return facturaRepository.findByUtenteId(utenteId);
    }

    public Factura gerarFactura(Factura factura) {
        factura.setDataEmissao(LocalDate.now());
        factura.setPaga(false);
        return facturaRepository.save(factura);
    }

    public List<Factura> findFacturasEmAtraso() {
        return facturaRepository.findByPagaFalse().stream()
                .filter(f -> LocalDate.now().isAfter(f.getPrazoPagamento()))
                .toList();
    }
}