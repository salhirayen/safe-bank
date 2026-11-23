package tn.isetkebili.transaction.controller; // J'ai corrigé le nom du package (tout en minuscules)

import org.springframework.web.bind.annotation.*;
import tn.isetkebili.transaction.entity.Transaction;
import tn.isetkebili.transaction.service.TransactionService;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("/api/transactions")
public class TransactionController {

    private final TransactionService service;

    public TransactionController(TransactionService service) {
        this.service = service;
    }

    // Un petit record pour recevoir la requête JSON
    public record TransferRequest(Long sourceAccountId, Long destinationAccountId, BigDecimal amount) {}

    @PostMapping("/transfer")
    public Transaction transfer(@RequestBody TransferRequest request) {
        return service.transfer(request.sourceAccountId(), request.destinationAccountId(), request.amount());
    }

    @GetMapping
    public List<Transaction> findAll() {
        return service.findAll();
    }

    @GetMapping("/{id}")
    public Transaction findById(@PathVariable Long id) {
        return service.findById(id);
    }

    @GetMapping("/account/{accountId}")
    public List<Transaction> findByAccount(@PathVariable Long accountId) {
        return service.findByAccountId(accountId);
    }
}