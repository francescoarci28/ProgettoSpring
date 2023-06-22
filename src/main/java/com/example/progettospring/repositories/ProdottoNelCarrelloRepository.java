package com.example.progettospring.repositories;

import com.example.progettospring.entities.Carrello;
import com.example.progettospring.entities.Prodotto;
import com.example.progettospring.entities.ProdottoNelCarrello;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProdottoNelCarrelloRepository extends JpaRepository<ProdottoNelCarrello,Integer> {

    List<ProdottoNelCarrello> findAllByCarrello(Carrello carr);

    @Modifying(flushAutomatically = true, clearAutomatically = true)
    @Query("update ProdottoNelCarrello pNC set pNC.prezzo=?2 where pNC.id = ?1")
    int updatePrezzo(int id, float prezzo);

    @Modifying(flushAutomatically = true, clearAutomatically = true)
    @Query("update ProdottoNelCarrello pNC set pNC.quantita = ?2 where pNC.id =?1")
    int updateQuantita(int id, int qta);


     void deleteAllByCarrello(Carrello carrello);

    void deleteById(Integer id);

    ProdottoNelCarrello findAllByCarrelloAndProdotto(Carrello carr,Prodotto prod);

    Prodotto findByProdotto(Prodotto p);



    boolean existsByCarrelloAndProdotto(Carrello carr,Prodotto prod);

    @Override
    Optional<ProdottoNelCarrello> findById(Integer integer);

    @Modifying(flushAutomatically = true, clearAutomatically = true)
    @Query("update ProdottoNelCarrello p set p.prezzo = ?2 where p.prodotto = ?1")
    int updatePrice(Integer id, float prezzo);
}
