package com.example.progettospring.repositories;

import com.example.progettospring.entities.Carrello;
import com.example.progettospring.entities.ProdottoNelCarrello;
import com.example.progettospring.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CarrelloRepository extends JpaRepository<Carrello,Integer> {

    @Query("select c.prodottiNelCarrello from Carrello c where c.user=?1")
    List<ProdottoNelCarrello> findProductByUser(User user);

    Carrello findByUser(User user);

    Optional<Carrello> findById(Integer id);

    @Modifying(flushAutomatically = true, clearAutomatically = true)
    @Query("update Carrello c set c.prezzoTotale =?2  where c.id = ?1")
    int updatePrice(int id, float prezzo);

    boolean existsById(Integer id);

    boolean existsByUser(User user);






}
