package com.example.progettospring.entities;

import javax.persistence.*;
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
@Table(name="carrello",schema="ecommercedb")
public class Carrello {

    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    @Column(name="id",nullable=false)
    private int id;

    @OneToOne
    @JoinColumn(name="user",unique = true,referencedColumnName = "id")
    private User user;

    @Basic
    @Column(name="prezzo_totale",nullable=false)
    private float prezzoTotale;

   @OneToMany(fetch =FetchType.EAGER,mappedBy = "carrello")
   List<ProdottoNelCarrello> prodottiNelCarrello;

    @Version
    @Column(name="version")
    private Long version;


}
