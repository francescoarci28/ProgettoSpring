package com.example.progettospring.dto;

import com.example.progettospring.entities.Acquisto;
import com.example.progettospring.entities.Prodotto;
import com.example.progettospring.entities.User;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Data
public class AcquistoDTO implements Serializable {

    private int id;

    private int quantita;

    private float prezzo;

    private String nome;

    private String username;

    private Date data;
    public AcquistoDTO(Acquisto a){
        this.id=a.getId();
        this.prezzo=a.getPrezzo();
        this.nome=a.getProdotto().getNome();
        this.username=a.getBuyer().getUsername();
        this.data = a.getData();
        this.quantita=a.getQuantita();
    }

    public AcquistoDTO(){}


}
