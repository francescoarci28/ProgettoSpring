package com.example.progettospring.services;

import com.example.progettospring.dto.AcquistoDTO;
import com.example.progettospring.dto.ProdottoDTO;
import com.example.progettospring.entities.*;
import com.example.progettospring.repositories.*;
import org.springframework.transaction.annotation.Isolation;
import support.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.util.*;

@Service
public class AcquistoService {

    @Autowired
    private ProdottoNelCarrelloRepository prodottoNelCarrelloRepository;
    @Autowired
    private AcquistoRepository acquistoRep;
    @Autowired
    private ProdottoRepository prodottoRep;
    @Autowired
    private CarrelloRepository carrelloRep;
    @Autowired
    private EntityManager entityManager;
    @Autowired
    private UserRepository userRep;

    @Transactional(readOnly = true,isolation= Isolation.READ_COMMITTED)
    public List<AcquistoDTO> getAllPurchasesByUser(String username) throws AccountAssenteException {
        if (!userRep.existsByUsername(username)) throw new AccountAssenteException();
        User user=userRep.findByUsername(username);
        List<AcquistoDTO> l=new LinkedList<>();
        for(Acquisto purchase: acquistoRep.findAllByBuyer(user)){
            AcquistoDTO purchaseDTO=new AcquistoDTO(purchase);
            l.add(purchaseDTO);
        }
        return l;
    }//getAllBookingsByUser

   /* @Transactional(readOnly = false,isolation = Isolation.READ_COMMITTED)
    public float effettuaAcquisto(ProdottoDTO prod,int qta,String username) throws ProdottoAssenteException, QtaNonDisponibileException, AccountAssenteException {
        if(!prodottoRep.existsByNome(prod.getNome())) throw new ProdottoAssenteException();
        if(!userRep.existsByUsername(username)) throw new AccountAssenteException();
        User user = userRep.findByUsername(username);
        Prodotto pr = prodottoRep.findByNome(prod.getNome());
        if(pr.getQta()-qta < 0) throw new QtaNonDisponibileException();
        float prezzo = pr.getPrezzo()*qta;
        Acquisto acq= new Acquisto();
        acq.setPrezzo(acq.getPrezzo()*qta);
        acq.setBuyer(user);
        acq.setProdotto(pr);
        acquistoRep.save(acq);
        prodottoRep.updateQuantity(pr.getNome(),pr.getQta()-qta);
        return prezzo;
    }
    */


}
