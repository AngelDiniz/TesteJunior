package com.picpaysimplificado.picpaysimplificado.services;

import com.picpaysimplificado.picpaysimplificado.domain.transaction.Transaction;
import com.picpaysimplificado.picpaysimplificado.domain.user.User;
import com.picpaysimplificado.picpaysimplificado.dtos.TransactionDTO;
import com.picpaysimplificado.picpaysimplificado.repositories.TransactionRepository;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Map;


@Service
public class TransactionService {
    @Autowired
    private UserService userService;

    private TransactionRepository repository;

    @Autowired
    private RestTemplate restTemplate;


    public void createTransaction(TransactionDTO transaction) throws Exception {
        User sender = this.userService.findUserById(transaction.senderID());
        User reciver = this.userService.findUserById((transaction.reciverId()));

        userService.validateTransaction(sender, transaction.value());

        boolean isAuthorazid = this.authorizeTransaction(sender, transaction.value());
        if (isAuthorazid){
            throw new Exception("Transação não autorizado");
        }

        Transaction newtransaction = new Transaction();
        newtransaction.setAmount(transaction.value());
        newtransaction.setSender(sender);
        newtransaction.setReceiver(reciver);
        newtransaction.setTimestamp(LocalDateTime.now());

        sender.setBalance(sender.getBalance().subtract(transaction.value()));
        reciver.setBalance(reciver.getBalance().add(transaction.value()));

        this.repository.save(newtransaction);
        this.userService.saveUser(sender);
        this.userService.saveUser(reciver);
    }
    public boolean authorizeTransaction(User sender, BigDecimal value){
        ResponseEntity<Map> aurthorizateReponse = restTemplate.getForEntity("https://util.devi.tools/api/v2/authorize", Map.class);

        if (aurthorizateReponse.getStatusCode()== HttpStatus.OK){
            String massage = (String) aurthorizateReponse.getBody().get("massage");
            return "Autorizado".equalsIgnoreCase(massage);
        }else return false;
    }
}
