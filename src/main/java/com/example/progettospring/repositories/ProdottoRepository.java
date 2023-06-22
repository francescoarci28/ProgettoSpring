package com.example.progettospring.repositories;

import com.example.progettospring.entities.Prodotto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;


@Repository
public interface ProdottoRepository extends JpaRepository<Prodotto,Integer> {

    @Modifying(flushAutomatically = true, clearAutomatically = true)
    @Query("update Prodotto p set p.qta = ?2 where p.nome = ?1")
    int updateQuantity(String nome, int qta);

    @Modifying(flushAutomatically = true, clearAutomatically = true)
    @Query("update Prodotto p set p.prezzo = ?2 where p.nome = ?1")
    int updatePrice(String nome, float prezzo);


    Optional<Prodotto> findById(Integer integer);

    Prodotto findByNome(String nome);

    List<Prodotto> findByPrezzoGreaterThan(float prezzo);

    List<Prodotto> findByPrezzoLessThan(float prezzo);

    List<Prodotto> findByPrezzoBetween(float prezzo1,float prezzo2);

    void deleteById(Integer id);

    void deleteByNome(String nome);

    @Query("select p from Prodotto  p")
    List<Prodotto> findAll();

    List<Prodotto> findByNomeContaining(String nome);


    boolean existsByNome(String nome);

    List<Prodotto> findByCategoria(String categoria);


    @Query("SELECT p " +
            "FROM Prodotto p "+
             "WHERE (p.nome LIKE ?1 OR ?1 IS NULL) AND (p.qta > ?2 OR ?2 IS NULL ) AND (p.prezzo = ?3 OR ?3 IS NUll )" )
    List<Prodotto> advancedSearch(String nome,Integer qta, float prezzo);


}
