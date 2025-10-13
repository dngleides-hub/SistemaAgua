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
@Table(name = "consumos")
public class Consumo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private LocalDate data;
    private BigDecimal quantidade;
    private String tipoConsumo;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "utente_id")
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"}) // 🔥 CORREÇÃO
    private Utente utente;

    // getters e setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public LocalDate getData() { return data; }
    public void setData(LocalDate data) { this.data = data; }

    public BigDecimal getQuantidade() { return quantidade; }
    public void setQuantidade(BigDecimal quantidade) { this.quantidade = quantidade; }

    public String getTipoConsumo() { return tipoConsumo; }
    public void setTipoConsumo(String tipoConsumo) { this.tipoConsumo = tipoConsumo; }

    public Utente getUtente() { return utente; }
    public void setUtente(Utente utente) { this.utente = utente; }
}