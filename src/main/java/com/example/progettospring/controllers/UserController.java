package com.example.progettospring.controllers;

import com.example.progettospring.entities.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.example.progettospring.services.UserService;
import support.AccountAssenteException;
import support.AccountEsistenteException;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping(path="/user")
public class UserController {//testato



    @Autowired
    private UserService userService;

    @PostMapping("/register")
    public ResponseEntity registerUser(@RequestParam(value="username") String username) {
        try {
            User nuovo = userService.registerUser(username);
            return new ResponseEntity(nuovo, HttpStatus.OK);
        } catch (AccountEsistenteException e) {
            return new ResponseEntity("account già esistente",HttpStatus.BAD_REQUEST);
        }catch(RuntimeException er){
            return new ResponseEntity("Errore",HttpStatus.BAD_REQUEST);
        }
    }


    @DeleteMapping("/delete")
    public ResponseEntity deleteUser(@RequestParam(value="username") String username){
        try{
            User result=userService.deleteUser(username);
            return new ResponseEntity(result,HttpStatus.OK);
        } catch (AccountAssenteException e) {
            return new ResponseEntity("Account inesistente",HttpStatus.BAD_REQUEST);
        }catch (RuntimeException er){
            return new ResponseEntity("Errore",HttpStatus.BAD_REQUEST);
        }
    }
    @GetMapping("/getUsers")
    public ResponseEntity getUsers(){
        try{
            List<User> result=userService.getAll();
            return new ResponseEntity(result,HttpStatus.OK);
        } catch (RuntimeException e) {
            return new ResponseEntity("Errore",HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/getUser")
    public ResponseEntity getUser(@RequestParam(value="username")String username){
        try{
            User user = userService.findOne(username);
            return new ResponseEntity(user,HttpStatus.BAD_REQUEST);
        } catch (AccountAssenteException e) {
            return new ResponseEntity("Account assente",HttpStatus.BAD_REQUEST);
        }catch(RuntimeException er){
            return new ResponseEntity("Errore",HttpStatus.BAD_REQUEST);
        }
    }


}
