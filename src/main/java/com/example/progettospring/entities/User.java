package com.example.progettospring.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

@Getter
@Setter
@EqualsAndHashCode
@ToString
@Entity
@Table(name = "user",schema ="ecommercedb")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="id",nullable=false)
    private int id;

   @OneToOne(mappedBy = "user",cascade = CascadeType.ALL)
    @JsonIgnore
    private Carrello carrello;


    @Basic
    @Column(name="username",unique = true,length=30)
    private String username;

    @OneToMany(mappedBy = "buyer",cascade = CascadeType.ALL)
    @JsonIgnore
    private List<Acquisto> listaAcquisti;


    public User(String username){
        this.username=username;

    }


    public User() {

    }
}
