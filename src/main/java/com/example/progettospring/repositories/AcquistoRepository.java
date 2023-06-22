package com.example.progettospring.repositories;

import com.example.progettospring.entities.Acquisto;
import com.example.progettospring.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Repository
public interface AcquistoRepository extends JpaRepository<Acquisto,Integer> {

    List<Acquisto> findAllByBuyer(User buyer);

    @Query("select a from Acquisto a where a.data=?1 and a.buyer=?2")
    List<Acquisto> findByData(Date data, User user);


    @Query("select p from Acquisto p where p.data > ?1 and p.data < ?2 and p.buyer = ?3")
    List<Acquisto> findByBuyerInPeriod(Date startDate, Date endDate, User user);

    boolean existsById(Integer id);

    void deleteById(Integer id);

    Optional<Acquisto> findById(Integer id);





}
