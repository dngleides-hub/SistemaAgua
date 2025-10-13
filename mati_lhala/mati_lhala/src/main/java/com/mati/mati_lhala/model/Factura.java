package com.mati.mati_lhala.model;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;
import jakarta.persistence.*;

import java.math.BigDecimal;
import java.time.LocalDate;


@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "facturas")
public class Factura {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private LocalDate dataEmissao;
    private BigDecimal consumo;
    private BigDecimal valor;
    private LocalDate prazoPagamento;
    private Boolean paga = false;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "utente_id")
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    private Utente utente;

    // getters e setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public LocalDate getDataEmissao() { return dataEmissao; }
    public void setDataEmissao(LocalDate dataEmissao) { this.dataEmissao = dataEmissao; }

    public BigDecimal getConsumo() { return consumo; }
    public void setConsumo(BigDecimal consumo) { this.consumo = consumo; }

    public BigDecimal getValor() { return valor; }
    public void setValor(BigDecimal valor) { this.valor = valor; }

    public LocalDate getPrazoPagamento() { return prazoPagamento; }
    public void setPrazoPagamento(LocalDate prazoPagamento) { this.prazoPagamento = prazoPagamento; }

    public Boolean getPaga() { return paga; }
    public void setPaga(Boolean paga) { this.paga = paga; }

    public Utente getUtente() { return utente; }
    public void setUtente(Utente utente) { this.utente = utente; }

    // 🔥 ADICIONE ESTE MÉTODO
    public Double calcularMulta() {
        if (this.paga) {
            return 0.0;
        }

        LocalDate hoje = LocalDate.now();
        if (hoje.isAfter(this.prazoPagamento)) {
            // Multa de 10% sobre o valor da factura
            return this.valor.doubleValue() * 0.10;
        }

        return 0.0;
    }

    // 🔥 MÉTODO AUXILIAR: Verificar se está em atraso
    public boolean isEmAtraso() {
        return !this.paga && LocalDate.now().isAfter(this.prazoPagamento);
    }
}