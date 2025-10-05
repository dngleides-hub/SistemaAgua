package com.mati.mati_lhala.model;


import lombok.*;
import jakarta.persistence.*;
import java.util.List;

@Entity
@Table(name = "utentes")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Utente {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String nome;

    @Column(nullable = false, unique = true)
    private String celular;

    private String endereco;

    @Column(nullable = false, unique = true)
    private String login;

    @Column(nullable = false)
    private String senha;

    @OneToMany(mappedBy = "utente", cascade = CascadeType.ALL)
    private List<Consumo> consumos;

    @OneToMany(mappedBy = "utente", cascade = CascadeType.ALL)
    private List<Factura> facturas;

    @OneToMany(mappedBy = "utente", cascade = CascadeType.ALL)
    private List<Pagamento> pagamentos;
}