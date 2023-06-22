package com.example.progettospring.services;

import com.example.progettospring.entities.Carrello;
import com.example.progettospring.entities.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import com.example.progettospring.repositories.CarrelloRepository;
import com.example.progettospring.repositories.UserRepository;
import support.AccountAssenteException;
import support.AccountEsistenteException;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CarrelloRepository carrelloRepository;

    @Transactional(readOnly = false,propagation = Propagation.REQUIRED,isolation = Isolation.READ_COMMITTED)
    public User registerUser(String username) throws AccountEsistenteException {
        if(userRepository.existsByUsername(username)) return userRepository.findByUsername(username);
        User user = new User(username);
        userRepository.save(user);
        Carrello c = new Carrello();
        c.setPrezzoTotale(0);
        c.setUser(user);
        carrelloRepository.save(c);
        return user;
    }

    @Transactional(readOnly = false,propagation = Propagation.REQUIRED,isolation = Isolation.READ_COMMITTED)
    public User registerUser(User user) throws AccountEsistenteException {
        if(userRepository.existsByUsername(user.getUsername())) throw new AccountEsistenteException();
        userRepository.save(user);
        Carrello c = new Carrello();
        c.setPrezzoTotale(0);
        c.setUser(user);
        carrelloRepository.save(c);
        return user;
    }

    @Transactional(readOnly = true,isolation =Isolation.READ_COMMITTED)
    public List<User> getAll(){
        return userRepository.findAll();
    }

    @Transactional(readOnly = false,propagation = Propagation.REQUIRES_NEW,isolation = Isolation.READ_COMMITTED)
    public User deleteUser(String username) throws AccountAssenteException {
        if(!(userRepository.existsByUsername(username))) throw new AccountAssenteException();
        //Carrello c = carrelloRepository.findByUser(user);
        //carrelloRepository.delete(c);
        User user = userRepository.findByUsername(username);
        userRepository.deleteById(user.getId());
        return user;
    }


    @Transactional(readOnly = true,propagation = Propagation.REQUIRED)
    public User getUser(Integer id) throws AccountAssenteException {
        Optional<User> user =userRepository.findById(id);
        if(user.isEmpty()) throw new AccountAssenteException();
        User u = user.get();
        return u;
    }

    @Transactional(readOnly = true)
    public User findOne(String username) throws AccountAssenteException{
        if(!userRepository.existsByUsername(username)) throw new AccountAssenteException();
        return userRepository.findByUsername(username);
    }
}
