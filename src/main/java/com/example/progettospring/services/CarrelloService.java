package com.example.progettospring.services;

import com.example.progettospring.dto.ProdottoNelCarrelloDTO;
import com.example.progettospring.entities.*;
import com.example.progettospring.repositories.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;
import support.*;

import javax.persistence.EntityManager;
import javax.persistence.LockModeType;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

@Service
public class CarrelloService {

    @Autowired
    private CarrelloRepository carrelloRep;

    @Autowired
    private AcquistoRepository acquistoRep;

    @Autowired
    private ProdottoRepository prodottoRep;

    @Autowired
    private EntityManager entityManager;

    @Autowired
    private UserRepository userRep;

    @Autowired
    private ProdottoNelCarrelloRepository prodNelCarrRep;

    @Transactional(readOnly = false,isolation = Isolation.READ_COMMITTED)
    public ProdottoNelCarrelloDTO aggiungiAlCarrello(ProdottoNelCarrelloDTO pNC) throws CarrelloInesistenteException, QtaNonDisponibileException, ProdottoAssenteException, QtaInvalidaException {
        User user=userRep.findByUsername(pNC.getUsername());
        if(!(carrelloRep.existsById(user.getCarrello().getId()))) throw new CarrelloInesistenteException();
        Carrello carr=user.getCarrello();
        Prodotto prod=prodottoRep.findByNome(pNC.getNome());
        if((prod.getQta()-pNC.getQuantitaRichiesta())<0) throw new QtaNonDisponibileException();
        if(prodNelCarrRep.existsByCarrelloAndProdotto(carr,prod)){
            ProdottoNelCarrello p=prodNelCarrRep.findAllByCarrelloAndProdotto(carr,prod);
            int nuovaQta= pNC.getQuantitaRichiesta()+p.getQuantita();
            if(prod.getQta()-nuovaQta < 0) throw new QtaNonDisponibileException();
            return aggiornaQuantita(p.getId(),nuovaQta);
        }
        else{

            ProdottoNelCarrello nuovo=new ProdottoNelCarrello();
            nuovo.setCarrello(carr);
            nuovo.setProdotto(prod);
            nuovo.setQuantita(pNC.getQuantitaRichiesta());
            nuovo.setPrezzo(prod.getPrezzo());
            ProdottoNelCarrello result=prodNelCarrRep.save(nuovo);
            float prezzo=carr.getPrezzoTotale()+pNC.getQuantitaRichiesta()*prod.getPrezzo();
            carrelloRep.updatePrice(carr.getId(),prezzo);
            carr.getProdottiNelCarrello().add(result);
            carrelloRep.save(carr);
            return new ProdottoNelCarrelloDTO(result);
        }
    }

    @Transactional(readOnly = false,isolation = Isolation.READ_COMMITTED)
    public ProdottoNelCarrelloDTO aggiornaQuantita(int id,int qta) throws ProdottoAssenteException, QtaInvalidaException {
        if (!prodNelCarrRep.existsById(id)) throw new ProdottoAssenteException();
        if (qta<=0) throw new QtaInvalidaException();
        Optional<ProdottoNelCarrello> opt=prodNelCarrRep.findById(id);
        ProdottoNelCarrello pNC=opt.get();
        Optional<Carrello> opt2=carrelloRep.findById(pNC.getCarrello().getId());
        Carrello carr=opt2.get();
        int deltaQuantity=qta- pNC.getQuantita();
        prodNelCarrRep.updateQuantita(id, qta);
        float nuovoPrezzoCarrello=carr.getPrezzoTotale()+deltaQuantity*pNC.getPrezzo();
        carrelloRep.updatePrice(pNC.getCarrello().getId(),nuovoPrezzoCarrello);
        return new ProdottoNelCarrelloDTO(prodNelCarrRep.findById(id).get());
    }


    @Transactional(readOnly = false,isolation = Isolation.READ_COMMITTED)
    public float rimuoviDalCarrello(String username,Integer idProdNelCarr) throws ProdottoAssenteException, CarrelloInesistenteException, AccountAssenteException {
        if(!(userRep.existsByUsername(username))) throw new AccountAssenteException();
        Carrello carr = userRep.findByUsername(username).getCarrello();
       // if(!(prodNelCarrRep.existsById(idProdNelCarr))) throw new ProdottoAssenteException();
        ProdottoNelCarrello prod = null;
        List<ProdottoNelCarrello> list = carr.getProdottiNelCarrello();
        for(ProdottoNelCarrello pr:list){
            if(pr.getId() == idProdNelCarr){
                prod = pr;
                prodNelCarrRep.deleteById(pr.getId());
            }
        }
        /*ProdottoNelCarrello prod=prodNelCarrRep.findById(idProdNelCarr).get();
        if(!carrelloRep.existsById(prod.getCarrello().getId())) throw new CarrelloInesistenteException();
        Carrello carr = carrelloRep.findById(prod.getCarrello().getId()).get();
        prodNelCarrRep.deleteById(idProdNelCarr);*/
        float newPrezzo=carr.getPrezzoTotale()-prod.getProdotto().getPrezzo()*prod.getQuantita();
        carr.getProdottiNelCarrello().remove(prod);
        carrelloRep.updatePrice(carr.getId(),newPrezzo);
        return carrelloRep.findById(carr.getId()).get().getPrezzoTotale();
    }

    @Transactional(readOnly = false,isolation = Isolation.READ_COMMITTED)
    public List<ProdottoNelCarrelloDTO> svuotaCarrello(String username) throws CarrelloInesistenteException, AccountAssenteException {
        if(!userRep.existsByUsername(username)) throw new AccountAssenteException();
        User user=userRep.findByUsername(username);
        if (!carrelloRep.existsByUser(user)) throw new CarrelloInesistenteException();
        Carrello carr=carrelloRep.findByUser(user);
        prodNelCarrRep.deleteAllByCarrello(carr);
        carrelloRep.updatePrice(carr.getId(),0);
        return new LinkedList<>();
    }//emptyCart


    @Transactional( readOnly = true,isolation=Isolation.READ_COMMITTED)
    public List<ProdottoNelCarrelloDTO> getCarr(Integer id) throws AccountAssenteException, CarrelloInesistenteException {
        if (!userRep.existsById(id)) throw new AccountAssenteException();
        Optional<User> u=userRep.findById(id);
        User user=u.get();
        if(!carrelloRep.existsByUser(user)) throw new CarrelloInesistenteException();
        Carrello carr=carrelloRep.findByUser(user);
        LinkedList<ProdottoNelCarrelloDTO> pNCDTO=new LinkedList<>();
        for (ProdottoNelCarrello p: prodNelCarrRep.findAllByCarrello(carr))
           pNCDTO.addLast(new ProdottoNelCarrelloDTO(p));
        return pNCDTO;
    }


    @Transactional(readOnly = false,isolation=Isolation.READ_COMMITTED)
    public List<ProdottoNelCarrelloDTO> aggiornaPrezzi(String username) throws CarrelloInesistenteException, AccountAssenteException {
        if (!userRep.existsByUsername(username) ) throw new AccountAssenteException();
        User user=userRep.findByUsername(username);
        if(!carrelloRep.existsByUser(user)) throw new CarrelloInesistenteException();
        Carrello carr=carrelloRep.findByUser(user);
        LinkedList<ProdottoNelCarrelloDTO> list= new LinkedList<>();
        for (ProdottoNelCarrello pnc: prodNelCarrRep.findAllByCarrello(carr)) {
            if (pnc.getPrezzo() != pnc.getProdotto().getPrezzo())
                prodNelCarrRep.updatePrezzo(pnc.getId(), pnc.getProdotto().getPrezzo());
            list.addLast(new ProdottoNelCarrelloDTO());
        }
        return list;
    }

    @Transactional(readOnly = false,isolation=Isolation.READ_COMMITTED)
    public float getPrezzo(String nome) throws AccountAssenteException {
        User user=userRep.findByUsername(nome);
        if(!carrelloRep.existsByUser(user)) throw new RuntimeException();
        Carrello carr=carrelloRep.findByUser(user);
        return carr.getPrezzoTotale();
    }




    //restituisco la spesa totale
    @Transactional(readOnly = false,isolation = Isolation.READ_COMMITTED)
    public float acquista(String username) throws AccountAssenteException, CarrelloInesistenteException, QtaNonDisponibileException, PrezzoVariatoException {
        if (!userRep.existsByUsername(username)) throw new AccountAssenteException();
        User user=userRep.findByUsername(username);
        if (!carrelloRep.existsByUser(user)) throw new CarrelloInesistenteException();
        Carrello carr=carrelloRep.findByUser(user);
        if (carr.getProdottiNelCarrello()==null || carr.getProdottiNelCarrello().size()==0) throw new RuntimeException("carrello vuoto");
        for (ProdottoNelCarrello pnc: carr.getProdottiNelCarrello()) {
            if(pnc.getQuantita()>pnc.getProdotto().getQta()) throw new QtaNonDisponibileException();
            if (pnc.getPrezzo() != pnc.getProdotto().getPrezzo()) throw new PrezzoVariatoException();
        }
        float prezzoTotale=0.0f;
        for(ProdottoNelCarrello p:carr.getProdottiNelCarrello()){
            Prodotto pr=p.getProdotto();
            int nuovaQta=pr.getQta()-p.getQuantita();
            float spesa=p.getPrezzo()*p.getQuantita();
            prezzoTotale+=spesa;
            prodottoRep.updateQuantity(pr.getNome(),nuovaQta);
            Acquisto acq=new Acquisto();
            acq.setBuyer(user);
            acq.setPrezzo(spesa);
            acq.setQuantita(p.getQuantita());
           acq.setProdotto(pr);
           acquistoRep.save(acq);
        }
        carrelloRep.updatePrice(carr.getId(),0);
        prodNelCarrRep.deleteAllByCarrello(carr);
        return prezzoTotale;
    }
}
