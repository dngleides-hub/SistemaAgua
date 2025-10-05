package com.mati.mati_lhala.model;


import lombok.*;
import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "facturas")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Factura {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private LocalDate dataEmissao;

    @Column(nullable = false)
    private Double consumo;

    @Column(nullable = false)
    private Double valor;

    @Column(nullable = false)
    private LocalDate prazoPagamento;

    private Boolean paga = false;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "utente_id", nullable = false)
    private Utente utente;

    public Double calcularMulta() {
        if (LocalDate.now().isAfter(prazoPagamento) && !paga) {
            return valor * 0.10; // 10% de multa
        }
        return 0.0;
    }
}