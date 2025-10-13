package com.mati.mati_lhala.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;
import jakarta.persistence.*;

import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "pagamentos")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Pagamento {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private LocalDate data;
    private BigDecimal valor;
    private String metodo;
    private String estado;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "utente_id")
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"}) // 🔥 CORREÇÃO
    private Utente utente;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "factura_id")
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"}) // 🔥 CORREÇÃO
    private Factura factura;

    // getters e setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public LocalDate getData() { return data; }
    public void setData(LocalDate data) { this.data = data; }

    public BigDecimal getValor() { return valor; }
    public void setValor(BigDecimal valor) { this.valor = valor; }

    public String getMetodo() { return metodo; }
    public void setMetodo(String metodo) { this.metodo = metodo; }

    public String getEstado() { return estado; }
    public void setEstado(String estado) { this.estado = estado; }

    public Utente getUtente() { return utente; }
    public void setUtente(Utente utente) { this.utente = utente; }

    public Factura getFactura() { return factura; }
    public void setFactura(Factura factura) { this.factura = factura; }
}