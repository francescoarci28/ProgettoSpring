package com.example.progettospring.dto;

import com.example.progettospring.entities.Carrello;
import com.example.progettospring.entities.Prodotto;
import com.example.progettospring.entities.ProdottoNelCarrello;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.ToString;

import javax.persistence.*;
import java.io.Serializable;

@Data
public class ProdottoNelCarrelloDTO implements Serializable {

    private int id;

    private Integer quantitaRichiesta;

    private Float prezzo;

    private String nome;

    private String username;

    public ProdottoNelCarrelloDTO(ProdottoNelCarrello pNC){
        this.id=pNC.getId();
        this.quantitaRichiesta=pNC.getQuantita();
        this.prezzo=pNC.getPrezzo();
        this.nome=pNC.getProdotto().getNome();
        this.username=pNC.getCarrello().getUser().getUsername();
    }

    public ProdottoNelCarrelloDTO(){}





}
