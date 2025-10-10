package com.mati.mati_lhala.controller;

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

    @PutMapping("/{id}")
    public ResponseEntity<Consumo> updateConsumo(@PathVariable Long id, @RequestBody Consumo consumo) {
        if (!consumoService.findById(id).isPresent()) {
            return ResponseEntity.notFound().build();
        }
        consumo.setId(id);
        return ResponseEntity.ok(consumoService.save(consumo));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteConsumo(@PathVariable Long id) {
        if (!consumoService.findById(id).isPresent()) {
            return ResponseEntity.notFound().build();
        }
        consumoService.deleteById(id);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/utente/{utenteId}")
    public List<Consumo> getConsumosByUtente(@PathVariable Long utenteId) {
        return consumoService.findByUtenteId(utenteId);
    }

    // CORREÇÃO: Mudar para @RequestBody para evitar Bad Request com múltiplos parâmetros
    @PostMapping("/total")
    public ResponseEntity<Double> getTotalConsumoPeriodo(@RequestBody TotalConsumoRequest request) {
        Double total = consumoService.calcularTotalConsumoPeriodo(
                request.getUtenteId(),
                request.getInicio(),
                request.getFim()
        );
        return ResponseEntity.ok(total != null ? total : 0.0);
    }

    // CORREÇÃO: Manter versão com @RequestParam mas com valores padrão para evitar Bad Request
    @GetMapping("/total-periodo")
    public ResponseEntity<Double> getTotalConsumoPeriodoParam(
            @RequestParam(required = false) Long utenteId,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate inicio,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fim) {

        // Valores padrão para evitar null
        LocalDate dataInicio = inicio != null ? inicio : LocalDate.now().minusMonths(1);
        LocalDate dataFim = fim != null ? fim : LocalDate.now();

        if (utenteId == null) {
            return ResponseEntity.badRequest().body(0.0);
        }

        Double total = consumoService.calcularTotalConsumoPeriodo(utenteId, dataInicio, dataFim);
        return ResponseEntity.ok(total != null ? total : 0.0);
    }

    // Classe DTO para a requisição de total de consumo
    public static class TotalConsumoRequest {
        private Long utenteId;
        private LocalDate inicio;
        private LocalDate fim;

        // Getters e Setters
        public Long getUtenteId() {
            return utenteId;
        }

        public void setUtenteId(Long utenteId) {
            this.utenteId = utenteId;
        }

        public LocalDate getInicio() {
            return inicio;
        }

        public void setInicio(LocalDate inicio) {
            this.inicio = inicio;
        }

        public LocalDate getFim() {
            return fim;
        }

        public void setFim(LocalDate fim) {
            this.fim = fim;
        }
    }
}