package com.example.progettospring.controllers;

import com.example.progettospring.dto.ProdottoDTO;
import com.example.progettospring.entities.Prodotto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.example.progettospring.services.ProdottoService;
import support.ProdottoAssenteException;
import support.ProdottoEsistenteException;
import support.ResponseMessage;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/prodotti")
public class ProdottoController {

    @Autowired
    private ProdottoService prodottoService;

    @PostMapping("/add")
    public ResponseEntity aggiungiProdotto(@RequestBody ProdottoDTO prod) {
        try {
            if (prod.getNome() == null || prod.getPrezzo() == 0 || prod.getQta() == 0)
                return new ResponseEntity("INVALID_GAME", HttpStatus.BAD_REQUEST);
            Prodotto result = prodottoService.aggiungiProdotto(prod);
            return new ResponseEntity(result, HttpStatus.OK);
        }  catch (RuntimeException er) {
            return new ResponseEntity("Errore", HttpStatus.BAD_REQUEST);
        } catch (ProdottoAssenteException e) {
            return new ResponseEntity("Prodotto assente", HttpStatus.BAD_REQUEST);
        }
    }

    @PutMapping("/aggiornaquantita")
    public ResponseEntity aggiornaQuantita(@RequestParam(value = "nome") String nome, @RequestParam(value = "qta") Integer qta) throws ProdottoAssenteException {
        try {
            ProdottoDTO result = prodottoService.aggiornaQta(nome, qta);
            return new ResponseEntity(result, HttpStatus.OK);
        } catch (ProdottoAssenteException e) {
            return new ResponseEntity("Prodotto assente", HttpStatus.BAD_REQUEST);
        } catch (RuntimeException er) {
            return new ResponseEntity("Errore", HttpStatus.BAD_REQUEST);
        }
    }

    @PutMapping("/aggiornaprezzo")
    public ResponseEntity aggiornaPrezzo(@RequestParam(value = "nome") String nome, @RequestParam(value = "prezzo") float prezzo) {
        try {
            ProdottoDTO result = prodottoService.aggiornaPrezzo(nome, prezzo);
            return new ResponseEntity(result, HttpStatus.OK);
        } catch (ProdottoAssenteException e) {
            return new ResponseEntity("Prodotto assente", HttpStatus.BAD_REQUEST);
        } catch (RuntimeException er) {
            return new ResponseEntity("Errore", HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/paginate")
    public ResponseEntity paginazione(@RequestParam(value = "pageNumber", defaultValue = "1") int pageNumber, @RequestParam(value = "pageSize", defaultValue = "10") int pageSize, @RequestParam(value = "sortBy", defaultValue = "codice", required = false) String sortBy) {
        try {
            List<Prodotto> result = prodottoService.mostraProdotti(pageNumber, pageSize, sortBy);

            if (result.size() <= 0) {
                return new ResponseEntity("Nessun risultato!", HttpStatus.OK);
            }
            return new ResponseEntity(result, HttpStatus.OK);
        } catch (ProdottoAssenteException e) {
            return new ResponseEntity("Prodotti assenti", HttpStatus.OK);
        }catch (RuntimeException er){
            return new ResponseEntity("Errore ",HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/ricercaavanzata")
    public ResponseEntity ricercaAvanzata(@RequestParam(value="nome")String nome,@RequestParam(value="qta")int qta,@RequestParam(value="prezzo")float prezzo){
        try{
         List<Prodotto> result=prodottoService.ricercaAvanzata(nome,qta,prezzo);
         return new ResponseEntity(result,HttpStatus.OK);
        } catch (RuntimeException e) {
            return new ResponseEntity("Errore",HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/show/all")
    public ResponseEntity mostra(){
       try{ List<Prodotto> result = prodottoService.mostraProdotti();
        return new ResponseEntity(result,HttpStatus.OK);
       }catch(RuntimeException er){
           return new ResponseEntity("Errore ",HttpStatus.BAD_REQUEST);
       }
    }


    @GetMapping("/show/bynamecontaining")
    public ResponseEntity mostraByNameContaining(@RequestParam(value="nome")String nome){
        try{
            List<Prodotto> result = prodottoService.mostraProdottiByContaining(nome);
            return new ResponseEntity(result,HttpStatus.OK);
        } catch (RuntimeException e) {
            return new ResponseEntity("Errore",HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/show/bycategory")
    public ResponseEntity mostraByCategoria(@RequestParam(value="categoria") String categoria){
        try{
            List<Prodotto> result=prodottoService.mostraProdottiByCategoria(categoria);
            return new ResponseEntity(result,HttpStatus.OK);
        } catch (RuntimeException e) {
            return new ResponseEntity("Errore",HttpStatus.BAD_REQUEST);
        }
    }


    @GetMapping("/show/product")
    public ResponseEntity mostraPerNome(@RequestParam(value="nome")String nome){
        try{
            Prodotto result = prodottoService.trovaProdotto(nome);
            return new ResponseEntity(result,HttpStatus.OK);
        } catch (RuntimeException e) {
            return new ResponseEntity("Errore",HttpStatus.BAD_REQUEST);
        } catch (ProdottoAssenteException e) {
            return new ResponseEntity("prodotto assente",HttpStatus.OK);
        }
    }


    @GetMapping("/show/bypriceinf")
    public ResponseEntity mostraPerPrezzoInf(@RequestParam (value="prezzo")float prezzo){
        try{
            List<Prodotto> result = prodottoService.mostraPerPrezzoInf(prezzo);
            return new ResponseEntity(result,HttpStatus.OK);
        } catch (RuntimeException e) {
            return new ResponseEntity("Errore",HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/show/bypricesup")
    public ResponseEntity mostraPerPrezzoSup(@RequestParam (value="prezzo")float prezzo){
        try{
            List<Prodotto> result = prodottoService.mostraPerPrezzoSup(prezzo);
            return new ResponseEntity(result,HttpStatus.OK);
        } catch (RuntimeException e) {
            return new ResponseEntity("Errore",HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/show/bypricebetween")
    public ResponseEntity mostraPerPrezzoBetween(@RequestParam (value="prezzoinf")float prezzoinf,@RequestParam(value="prezzosup")float prezzosup){
        try{
            List<Prodotto> result = prodottoService.mostraPerPrezzoBetween(prezzoinf,prezzosup);
            return new ResponseEntity(result,HttpStatus.OK);
        } catch (RuntimeException e) {
            return new ResponseEntity("Errore",HttpStatus.BAD_REQUEST);
        }
    }

    @DeleteMapping("/delete")
    public ResponseEntity deleteProdotto(@RequestParam(value="nome") String nome){
        try{
          ProdottoDTO result=  prodottoService.deleteProdotto(nome);
          return new ResponseEntity(result,HttpStatus.OK);
        } catch (ProdottoAssenteException e) {
            return new ResponseEntity("prodotto già assente",HttpStatus.BAD_REQUEST);
        }catch(RuntimeException er){
            System.out.println(er.getMessage());
            return new ResponseEntity("Errore",HttpStatus.OK);
        }
    }

    @GetMapping("/show/productById")
    public ResponseEntity mostraPerId(@RequestParam(value="id")Integer id){
        try{
            Prodotto result = prodottoService.trovaProdotto(id);
            return new ResponseEntity(result,HttpStatus.OK);
        } catch (RuntimeException e) {
            return new ResponseEntity("Errore",HttpStatus.BAD_REQUEST);
        } catch (ProdottoAssenteException e) {
            return new ResponseEntity("prodotto assente",HttpStatus.OK);
        }
    }









}




