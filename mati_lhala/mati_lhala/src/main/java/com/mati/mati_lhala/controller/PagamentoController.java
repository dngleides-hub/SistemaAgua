package com.mati.mati_lhala.controller;


import com.mati.mati_lhala.model.Factura;
import com.mati.mati_lhala.model.Pagamento;
import com.mati.mati_lhala.service.FacturaService;
import com.mati.mati_lhala.service.PagamentoService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import java.util.List;

@RestController
@RequestMapping("/api/pagamentos")
@RequiredArgsConstructor
public class PagamentoController {
    private final PagamentoService pagamentoService;

    @GetMapping
    public List<Pagamento> getAllPagamentos() {
        return pagamentoService.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Pagamento> getPagamentoById(@PathVariable Long id) {
        return pagamentoService.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping("/efectuar")
    public ResponseEntity<Pagamento> efectuarPagamento(@RequestBody Pagamento pagamento) {
        if (!pagamentoService.validarPagamento(pagamento)) {
            return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.ok(pagamentoService.efectuarPagamento(pagamento));
    }

    @GetMapping("/utente/{utenteId}")
    public List<Pagamento> getPagamentosByUtente(@PathVariable Long utenteId) {
        return pagamentoService.findByUtenteId(utenteId);
    }
}