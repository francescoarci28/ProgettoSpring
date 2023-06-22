package com.example.progettospring.controllers;

import com.example.progettospring.dto.AcquistoDTO;
import com.example.progettospring.dto.ProdottoDTO;
import com.example.progettospring.entities.Acquisto;
import com.example.progettospring.entities.Carrello;
import com.example.progettospring.entities.Prodotto;
import com.example.progettospring.entities.User;
import support.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import com.example.progettospring.services.AcquistoService;
import com.example.progettospring.services.CarrelloService;
import com.example.progettospring.services.UserService;


import java.util.LinkedList;
import java.util.List;


@RestController
@RequestMapping("/acquisti")
public class AcquistoController {

    @Autowired
    private AcquistoService acquistoService;

    @GetMapping("/byuser")
    public ResponseEntity getAcquisti(@RequestParam(value = "username") String username){
        try{
           List<AcquistoDTO> result = acquistoService.getAllPurchasesByUser(username);
           return new ResponseEntity(result, HttpStatus.OK);
        } catch (AccountAssenteException e) {
            return new ResponseEntity("Account inesistente ",HttpStatus.BAD_REQUEST);
        }catch (RuntimeException er){
            return new ResponseEntity("Errore ",HttpStatus.BAD_REQUEST);
        }
    }
/*
    @PostMapping("/buyproduct")
    public ResponseEntity effettuaAcquisto(@RequestBody ProdottoDTO prodDTO,@RequestParam(value="qta") int qta,@RequestParam(value="username")String username){
        try{
            float prezzo = acquistoService.effettuaAcquisto(prodDTO,qta,username);
            return new ResponseEntity(prezzo,HttpStatus.OK);
        } catch (ProdottoAssenteException e) {
            return new ResponseEntity("Prodotto inesistente", HttpStatus.BAD_REQUEST);
        } catch (QtaNonDisponibileException e) {
            return new ResponseEntity("Quantità non disponibile",HttpStatus.BAD_REQUEST);
        } catch (AccountAssenteException e) {
            return new ResponseEntity(" Account inesistente",HttpStatus.BAD_REQUEST);
        }catch (RuntimeException er){
            return new ResponseEntity("Errore ",HttpStatus.BAD_REQUEST);
        }
    }
    */








}
