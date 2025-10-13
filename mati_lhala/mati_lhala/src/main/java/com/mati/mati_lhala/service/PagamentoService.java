package com.mati.mati_lhala.service;

// PagamentoService.java

import com.mati.mati_lhala.model.Factura;
import com.mati.mati_lhala.model.Pagamento;
import com.mati.mati_lhala.repository.FacturaRepository;
import com.mati.mati_lhala.repository.PagamentoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class PagamentoService {
    private final PagamentoRepository pagamentoRepository;
    private final FacturaRepository facturaRepository;

    public List<Pagamento> findAll() {
        return pagamentoRepository.findAll();
    }

    public Optional<Pagamento> findById(Long id) {
        return pagamentoRepository.findById(id);
    }

    public Pagamento save(Pagamento pagamento) {
        return pagamentoRepository.save(pagamento);
    }

    public void deleteById(Long id) {
        pagamentoRepository.deleteById(id);
    }

    public List<Pagamento> findByUtenteId(Long utenteId) {
        return pagamentoRepository.findByUtenteId(utenteId);
    }

    public Pagamento efectuarPagamento(Pagamento pagamento) {
        pagamento.setData(LocalDate.now());
        pagamento.setEstado("PROCESSADO");

        if (pagamento.getFactura() != null) {
            Factura factura = pagamento.getFactura();
            factura.setPaga(true);
            facturaRepository.save(factura);
        }

        return pagamentoRepository.save(pagamento);
    }

    public boolean validarPagamento(Pagamento pagamento) {
        return
                pagamento.getMetodo() != null &&
                !pagamento.getMetodo().isEmpty();
    }
}