package com.mati.mati_lhala.dto;

import com.mati.mati_lhala.model.Utente;

public class UtenteDTO {
    private Long id;
    private String nome;
    private String celular;
    private String endereco;
    private String login;

    // Construtor a partir da entidade
    public UtenteDTO(Utente utente) {
        this.id = utente.getId();
        this.nome = utente.getNome();
        this.celular = utente.getCelular();
        this.endereco = utente.getEndereco();
        this.login = utente.getLogin();
    }

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
}