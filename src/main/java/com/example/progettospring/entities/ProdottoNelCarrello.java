package com.example.progettospring.entities;

import javax.persistence.*;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;

@Getter
@Setter
@EqualsAndHashCode
@ToString
@Entity
@Table(name="prodottonelcarrello",schema="ecommercedb")
public class ProdottoNelCarrello {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="id")
    private int id;

    @ManyToOne
    @JoinColumn(name="carrello",referencedColumnName = "id")
    private Carrello carrello;

    @Basic
    @Column(name="quantita")
    private Integer quantita;

    @Basic
    @Column(name="prezzo")
    private Float prezzo;

    @Version
    @Column(name = "version")
    @JsonIgnore
    private Long version;

    @ManyToOne
    @JoinColumn(name="prodotto",unique = true,referencedColumnName = "id")
    private Prodotto prodotto;
}
