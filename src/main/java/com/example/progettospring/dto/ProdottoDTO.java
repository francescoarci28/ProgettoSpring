package com.example.progettospring.dto;

import com.example.progettospring.entities.Acquisto;
import com.example.progettospring.entities.Prodotto;
import com.example.progettospring.entities.ProdottoNelCarrello;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;

@Data
public class ProdottoDTO implements Serializable {

    private String nome;

    private String descrizione;

    private float prezzo;

    private int qta;

    private String categoria;

    public ProdottoDTO(Prodotto p){
        this.nome=p.getNome();
        this.descrizione=p.getDescrizione();
        this.prezzo=p.getPrezzo();
        this.qta=p.getQta();
        this.categoria=p.getCategoria();
    }
    public ProdottoDTO(){

    }
}
