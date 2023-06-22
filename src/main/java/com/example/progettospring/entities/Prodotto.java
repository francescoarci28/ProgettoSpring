package com.example.progettospring.entities;

import javax.persistence.*;

import com.example.progettospring.dto.ProdottoDTO;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;
import java.util.List;

@Getter
@Setter
@EqualsAndHashCode
@ToString
@Entity
@Table(name="prodotto",schema="ecommercedb")
public class Prodotto {

    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    @Column(name="id",nullable = false)
    private int id;

    @Basic
    @Column(name = "immagine", nullable = true, length = 500)
    @ToString.Exclude
    @JsonIgnore
    private String immagine; //url dell'immagine

    @Basic
    @Column(name="nome",unique = true,nullable=false, length=100)
    private String nome;

    @Basic
    @Column(name="descrizione", nullable=true, length=300)
    private String descrizione;

    @Basic
    @Column(name="prezzo",nullable=false,length=100)
    private float prezzo;

    @Basic
    @Column(name="qta", nullable=false,length=100)
    private int qta;

    @Basic
    @Column(name="categoria",nullable=false,length = 100)
    private String categoria;

    @OneToMany(mappedBy = "prodotto",cascade = CascadeType.ALL)
    @JsonIgnore
    private List<ProdottoNelCarrello> prodottiNelCarrello;

    @OneToMany(mappedBy = "prodotto",cascade=CascadeType.ALL)
    @JsonIgnore
  private List<Acquisto> acquisto;

    @Version
    @Column(name="version")
    @JsonIgnore
    private Long version;



    public Prodotto(String nome,String descrizione,float prezzo,int qta,String categoria){
        this.nome=nome;
        this.descrizione=descrizione;
        this.prezzo=prezzo;
        this.qta=qta;
        this.categoria=categoria;
    }
    public Prodotto(){

    }

    public void setByDto(ProdottoDTO prod){
        this.setDescrizione(prod.getDescrizione());
        this.setNome(prod.getNome());
        this.setQta(prod.getQta());
        this.setPrezzo(prod.getPrezzo());
        this.setCategoria(prod.getCategoria());
    }





}
