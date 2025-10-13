package com.mati.mati_lhala.model;


import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;
import jakarta.persistence.*;
import java.util.*;
@Entity
@Table(name = "utente")
public class Utente {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nome;
    private String celular;
    private String endereco;
    private String login;
    private String senha;

    // 🔥 CORREÇÃO: Evitar loop infinito com @JsonIgnore
    @OneToMany(mappedBy = "utente", fetch = FetchType.LAZY)
    @JsonIgnore
    private List<Consumo> consumos = new ArrayList<>();

    @OneToMany(mappedBy = "utente", fetch = FetchType.LAZY)
    @JsonIgnore
    private List<Factura> facturas = new ArrayList<>();

    @OneToMany(mappedBy = "utente", fetch = FetchType.LAZY)
    @JsonIgnore
    private List<Pagamento> pagamentos = new ArrayList<>();

    // getters e setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }

    public String getCelular() { return celular; }
    public void setCelular(String celular) { this.celular = celular; }

    public String getEndereco() { return endereco; }
    public void setEndereco(String endereco) { this.endereco = endereco; }

    public String getLogin() { return login; }
    public void setLogin(String login) { this.login = login; }

    public String getSenha() { return senha; }
    public void setSenha(String senha) { this.senha = senha; }

    public List<Consumo> getConsumos() { return consumos; }
    public void setConsumos(List<Consumo> consumos) { this.consumos = consumos; }

    public List<Factura> getFacturas() { return facturas; }
    public void setFacturas(List<Factura> facturas) { this.facturas = facturas; }

    public List<Pagamento> getPagamentos() { return pagamentos; }
    public void setPagamentos(List<Pagamento> pagamentos) { this.pagamentos = pagamentos; }
}