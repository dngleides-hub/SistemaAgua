package com.mati.mati_lhala.controller;

// ConsumoController.java

import com.mati.mati_lhala.model.Consumo;
import com.mati.mati_lhala.service.ConsumoService;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/consumos")
@RequiredArgsConstructor
public class ConsumoController {
    private final ConsumoService consumoService;

    @GetMapping
    public List<Consumo> getAllConsumos() {
        return consumoService.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Consumo> getConsumoById(@PathVariable Long id) {
        return consumoService.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public Consumo createConsumo(@RequestBody Consumo consumo) {
        return consumoService.save(consumo);
    }

    @GetMapping("/utente/{utenteId}")
    public List<Consumo> getConsumosByUtente(@PathVariable Long utenteId) {
        return consumoService.findByUtenteId(utenteId);
    }

    @GetMapping("/total")
    public ResponseEntity<Double> getTotalConsumoPeriodo(
            @RequestParam Long utenteId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate inicio,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fim) {

        Double total = consumoService.calcularTotalConsumoPeriodo(utenteId, inicio, fim);
        return ResponseEntity.ok(total != null ? total : 0.0);
    }
}