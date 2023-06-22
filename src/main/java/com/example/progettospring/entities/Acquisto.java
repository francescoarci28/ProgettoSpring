package com.example.progettospring.entities;

import javax.persistence.*;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.CreationTimestamp;

import java.io.Serializable;
import java.util.Date;

@Getter
@Setter
@EqualsAndHashCode
@ToString
@Entity
@Table(name="acquisto",schema = "ecommercedb")
public class Acquisto {

    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    @Column(name="id",nullable=false)
    private int id;


    @Version
    @Column(name="version")
    private Long version;

    @Basic
    @CreationTimestamp
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "data")
    private Date data;

    @Basic
    @Column(name="prezzo")
    private float prezzo;

    @Basic
    @Column(name = "quantita")
    private int quantita;

    @ManyToOne
    @JoinColumn(name="buyer",referencedColumnName = "id")
    private User buyer;

    @ManyToOne
    @JoinColumn(name="prodotto",referencedColumnName = "id")
    @JsonIgnore
    private Prodotto prodotto;


}
