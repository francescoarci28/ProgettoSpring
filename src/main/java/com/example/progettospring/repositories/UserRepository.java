package com.example.progettospring.repositories;

import com.example.progettospring.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.*;

@Repository
public interface UserRepository extends JpaRepository<User,Integer> {
    User findByUsername(String username);

    Optional<User> findById(Integer id);

    void deleteById(Integer id);

    void deleteByUsername(String username);

    boolean existsByUsername(String username);

}
