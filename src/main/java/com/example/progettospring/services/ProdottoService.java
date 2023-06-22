package com.example.progettospring.services;

import com.example.progettospring.dto.ProdottoDTO;
import com.example.progettospring.entities.Carrello;
import com.example.progettospring.entities.Prodotto;
import com.example.progettospring.entities.ProdottoNelCarrello;
import com.example.progettospring.entities.User;
import com.example.progettospring.repositories.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;
import support.ProdottoAssenteException;
import support.ProdottoEsistenteException;

import javax.persistence.EntityManager;
import javax.persistence.LockModeType;
import java.util.List;
import java.util.Optional;

@Service
public class ProdottoService {

    @Autowired
    private ProdottoRepository prodottoRep;


    @Autowired
    private CarrelloRepository carrelloRep;

    @Autowired
    private ProdottoNelCarrelloRepository prodInCartRep;

    @Autowired
    private UserRepository userRep;

    @Autowired
    private AcquistoRepository acquistoRep;

    @Autowired
    private EntityManager entityManager;



    @Transactional(readOnly = false,isolation = Isolation.READ_COMMITTED)
    public Prodotto aggiungiProdotto(ProdottoDTO p) throws  ProdottoAssenteException {

        if(prodottoRep.existsByNome(p.getNome())) {

            this.aggiornaQta(p.getNome(),p.getQta());
            this.aggiornaPrezzo(p.getNome(),p.getPrezzo());

            return prodottoRep.findByNome(p.getNome());
        }
        Prodotto prod=new Prodotto();
        prod.setByDto(p);
        prodottoRep.save(prod);
        return prodottoRep.findByNome(p.getNome());
    }

    @Transactional(readOnly = false,isolation = Isolation.READ_COMMITTED)
    public ProdottoDTO aggiornaQta(String nome,int qta) throws ProdottoAssenteException {
        if(!(prodottoRep.existsByNome(nome))) throw new ProdottoAssenteException();
        if(qta<=0) throw new RuntimeException("quantità richiesta > 0");
        Prodotto prod = prodottoRep.findByNome(nome);
        prodottoRep.updateQuantity(prod.getNome(),qta);
        return new ProdottoDTO(prod);
    }

    @Transactional(readOnly = false,isolation = Isolation.READ_COMMITTED)
    public ProdottoDTO aggiornaPrezzo(String nome,float prezzo) throws ProdottoAssenteException {
        if(!(prodottoRep.existsByNome(nome))) throw new ProdottoAssenteException();
        if(prezzo<=0) throw new RuntimeException("il prezzo deve essere > 0");
        Prodotto prod = prodottoRep.findByNome(nome);
        prodottoRep.updatePrice(prod.getNome(),prezzo);
        return new ProdottoDTO(prod);
    }


    @Transactional(readOnly = true,isolation = Isolation.READ_COMMITTED)
    public List<Prodotto> ricercaAvanzata(String nome,Integer qta, float prezzo){
        return prodottoRep.advancedSearch(nome,qta,prezzo);
    }

    @Transactional(readOnly = true,isolation = Isolation.READ_COMMITTED)
    public List<Prodotto> mostraProdotti(){
        return prodottoRep.findAll();
    }

    @Transactional(readOnly = true,isolation = Isolation.READ_COMMITTED)
    public List<Prodotto> mostraProdottiByContaining(String nome){
        return prodottoRep.findByNomeContaining(nome);
    }

    @Transactional(readOnly = true,isolation = Isolation.READ_COMMITTED)
    public List<Prodotto> mostraProdottiByCategoria(String categoria){
        return prodottoRep.findByCategoria(categoria);
    }


    @Transactional(readOnly = false,isolation = Isolation.READ_COMMITTED)
    public ProdottoDTO deleteProdotto(String nome) throws ProdottoAssenteException {
        if(!(prodottoRep.existsByNome(nome))) throw new ProdottoAssenteException();
        Prodotto prod=prodottoRep.findByNome(nome);
        ProdottoDTO result=new ProdottoDTO(prod);
        prodottoRep.deleteById(prod.getId());
        return result;
    }

    @Transactional(readOnly = true,isolation = Isolation.READ_COMMITTED)
    public Prodotto trovaProdotto(String nome) throws ProdottoAssenteException {
        if(!prodottoRep.existsByNome(nome)) throw new ProdottoAssenteException();
        return prodottoRep.findByNome(nome);
    }

    @Transactional(readOnly = true,isolation = Isolation.READ_COMMITTED)
    public List<Prodotto> mostraProdotti(int pageNumber, int pageSize, String sortBy) throws ProdottoAssenteException {
        Pageable paging = PageRequest.of(pageNumber, pageSize, Sort.by(sortBy));

        Page<Prodotto> pagedResult = prodottoRep.findAll(paging);
        if ( pagedResult.hasContent() ) {
            return pagedResult.getContent();
        }
        else {
            throw new ProdottoAssenteException();
        }
    }

    @Transactional(readOnly = true,isolation = Isolation.READ_COMMITTED)
    public Prodotto trovaProdotto(Integer id) throws ProdottoAssenteException {
        if(!prodottoRep.existsById(id)){
            throw new ProdottoAssenteException();
        }
        Optional<Prodotto> prod=prodottoRep.findById(id);
        Prodotto pr= prod.get();
        return pr;
    }


    @Transactional(readOnly = true,isolation = Isolation.READ_COMMITTED)
    public List<Prodotto> mostraPerPrezzoSup(float prezzo){
        if(prezzo<=0)throw new RuntimeException("Il prezzo deve essere > 0");
        return prodottoRep.findByPrezzoGreaterThan(prezzo);
    }

    @Transactional(readOnly = true,isolation = Isolation.READ_COMMITTED)
    public List<Prodotto> mostraPerPrezzoInf(float prezzo){
        if(prezzo<=0)throw new RuntimeException("Il prezzo deve essere > 0");
        return prodottoRep.findByPrezzoLessThan(prezzo);
    }

    @Transactional(readOnly = true,isolation = Isolation.READ_COMMITTED)
    public List<Prodotto> mostraPerPrezzoBetween(float prezzo1,float prezzo2){
        if(prezzo1<=0)throw new RuntimeException("Il prezzo deve essere > 0");
        if(prezzo2<=0)throw new RuntimeException("Il prezzo deve essere > 0");
        if(prezzo1>prezzo2)throw new RuntimeException("Il range di prezzo è incompatibile");
        return prodottoRep.findByPrezzoBetween(prezzo1,prezzo2);
    }

}
