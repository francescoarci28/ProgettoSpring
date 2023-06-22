package com.example.progettospring.controllers;

import com.example.progettospring.dto.ProdottoNelCarrelloDTO;
import com.example.progettospring.entities.Carrello;
import com.example.progettospring.entities.Prodotto;
import com.example.progettospring.entities.ProdottoNelCarrello;
import com.example.progettospring.entities.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.example.progettospring.services.CarrelloService;
import com.example.progettospring.services.ProdottoService;
import com.example.progettospring.services.UserService;
import support.*;

import javax.validation.Valid;
import java.util.LinkedList;
import java.util.List;

@RestController
@RequestMapping("/carrello")
public class CarrelloController {

    @Autowired
    private CarrelloService carrelloService;

    @Autowired
    private UserService userService;

    @GetMapping()
    public ResponseEntity getProdotti(@RequestParam(value="id") Integer id ){
        try{
           List<ProdottoNelCarrelloDTO> result =  carrelloService.getCarr(id);
           return new ResponseEntity(result,HttpStatus.OK);
        } catch (AccountAssenteException e) {
            return new ResponseEntity("account inesistente",HttpStatus.BAD_REQUEST);
        } catch (CarrelloInesistenteException e) {
            return new ResponseEntity("carrello inesistente",HttpStatus.BAD_REQUEST);
        }catch(RuntimeException er){
            return new ResponseEntity("Errore ",HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/aggiornaprezzi")
    public ResponseEntity aggiornaPrezzi(@RequestParam(value="username") String username){
       try{
           List<ProdottoNelCarrelloDTO> result = carrelloService.aggiornaPrezzi(username);
           return new ResponseEntity(result,HttpStatus.OK);
       } catch (AccountAssenteException e) {
         return new ResponseEntity("account inesistente",HttpStatus.BAD_REQUEST);
       } catch (CarrelloInesistenteException e) {
           return new ResponseEntity("carrello inesistente",HttpStatus.BAD_REQUEST);
       }catch(RuntimeException er){
           return new ResponseEntity("Errore ",HttpStatus.BAD_REQUEST);
       }
    }

    @PostMapping("/add")
    public ResponseEntity aggiungiAlCarrello(@RequestBody ProdottoNelCarrelloDTO pncDTO){
        try{
           ProdottoNelCarrelloDTO result= carrelloService.aggiungiAlCarrello(pncDTO);
           return new ResponseEntity(result,HttpStatus.OK);
        } catch (ProdottoAssenteException e) {
            return new ResponseEntity("Prodotto assente",HttpStatus.BAD_REQUEST);
        } catch (QtaNonDisponibileException e) {
            return new ResponseEntity("quantità non disponibile",HttpStatus.BAD_REQUEST);
        } catch (CarrelloInesistenteException e) {
            return new ResponseEntity("carrello inesistente",HttpStatus.BAD_REQUEST);
        } catch (QtaInvalidaException e) {
            return new ResponseEntity("Quantità non valida",HttpStatus.BAD_REQUEST);
        }catch(RuntimeException er){
            System.out.println(er.getMessage());
            return new ResponseEntity("Errore ",HttpStatus.BAD_REQUEST);
        }
    }

    @DeleteMapping("/delete") //rivedere
    public ResponseEntity rimuoviDalCarrello(@RequestParam(value = "username") String username,@RequestParam(value="id") Integer id){
      try {
          float result = carrelloService.rimuoviDalCarrello(username,id);
          return new ResponseEntity(result,HttpStatus.OK);
      } catch (ProdottoAssenteException e) {
          return new ResponseEntity("Prodotto assente",HttpStatus.BAD_REQUEST);
      } catch (CarrelloInesistenteException e) {
          return new ResponseEntity("Carrello inesistente",HttpStatus.BAD_REQUEST);
      } catch (AccountAssenteException e) {
          return new ResponseEntity("Carrello inesistente",HttpStatus.BAD_REQUEST);
      }catch(RuntimeException er){
          return new ResponseEntity("Errore ",HttpStatus.BAD_REQUEST);
      }
    }

    @PutMapping("/aggiorna")//rivedere
    public ResponseEntity aggiornaQta(@RequestParam(value="id")Integer id,@RequestParam(value = "qta") Integer qta){
     try{
         ProdottoNelCarrelloDTO result = carrelloService.aggiornaQuantita(id,qta);
         return new ResponseEntity(result,HttpStatus.OK);
     } catch (ProdottoAssenteException e) {
         return new ResponseEntity("Prodotto assente",HttpStatus.BAD_REQUEST);
     } catch (QtaInvalidaException e) {
         return new ResponseEntity("quantità non valida",HttpStatus.BAD_REQUEST);
     }catch(RuntimeException er){
         return new ResponseEntity("Errore ",HttpStatus.BAD_REQUEST);
     }
    }

    @DeleteMapping("/svuotacarrello")
    public ResponseEntity svuotaCarrello(@RequestParam(value="username") String username){
        try{
            List<ProdottoNelCarrelloDTO> result=carrelloService.svuotaCarrello(username);
            return new ResponseEntity(result,HttpStatus.OK);
        } catch (AccountAssenteException e) {
            return new ResponseEntity("Account assente",HttpStatus.BAD_REQUEST);
        } catch (CarrelloInesistenteException e) {
            return new ResponseEntity("Carrello assente",HttpStatus.BAD_REQUEST);
        }catch(RuntimeException er){
            return new ResponseEntity("Errore ",HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("/acquista")
    public ResponseEntity acquista(@RequestParam(value="username") String username){
      try{
          float result =carrelloService.acquista(username);
          return new ResponseEntity(result,HttpStatus.OK);
      } catch (PrezzoVariatoException e) {
          return new ResponseEntity("Prezzo variato",HttpStatus.BAD_REQUEST);
      } catch (QtaNonDisponibileException e) {
          return new ResponseEntity("quantità non disponibile",HttpStatus.BAD_REQUEST);
      } catch (AccountAssenteException e) {
          return new ResponseEntity("account assente",HttpStatus.BAD_REQUEST);
      } catch (CarrelloInesistenteException e) {
          return new ResponseEntity("Carrello inesistente",HttpStatus.BAD_REQUEST);
      }catch(RuntimeException er){
          return new ResponseEntity("Errore ",HttpStatus.BAD_REQUEST);
      }
    }

    @GetMapping("/price")
    public ResponseEntity getPrezzo(@RequestParam(value="username") String username){
        try{
            Float result =  carrelloService.getPrezzo(username);
            return new ResponseEntity(result,HttpStatus.OK);
        } catch (AccountAssenteException e) {
            return new ResponseEntity("account inesistente",HttpStatus.BAD_REQUEST);
        }catch(RuntimeException er){
            return new ResponseEntity("Errore ",HttpStatus.BAD_REQUEST);
        }
    }








}
