package com.mati.mati_lhala.controller;


import com.mati.mati_lhala.model.Factura;
import com.mati.mati_lhala.service.FacturaService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/facturas")
@RequiredArgsConstructor
public class FacturaController {
    private final FacturaService facturaService;

    @GetMapping
    public List<Factura> getAllFacturas() {
        return facturaService.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Factura> getFacturaById(@PathVariable Long id) {
        return facturaService.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping("/gerar")
    public Factura gerarFactura(@RequestBody Factura factura) {
        return facturaService.gerarFactura(factura);
    }

    @GetMapping("/utente/{utenteId}")
    public List<Factura> getFacturasByUtente(@PathVariable Long utenteId) {
        return facturaService.findByUtenteId(utenteId);
    }

    @GetMapping("/atraso")
    public List<Factura> getFacturasEmAtraso() {
        return facturaService.findFacturasEmAtraso();
    }

    @GetMapping("/{id}/multa")
    public ResponseEntity<Double> calcularMulta(@PathVariable Long id) {
        return facturaService.findById(id)
                .map(factura -> ResponseEntity.ok(factura.calcularMulta()))
                .orElse(ResponseEntity.notFound().build());
    }
}