package tn.isetkebili.Account_service.service;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tn.isetkebili.Account_service.entity.Account;
import tn.isetkebili.Account_service.entity.AccountStatus;
import tn.isetkebili.Account_service.repository.AccountRepository;
import java.math.BigDecimal;
import java.util.List;
@Service
public class AccountService {
    private final AccountRepository repository;
    public AccountService(AccountRepository repository) {
        this.repository = repository;
    }
    public Account create(Account account) {
        if (account.getBalance() == null
                || account.getBalance()
                .compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException(
                    "Le solde doit être positif ou nul");
        }
        account.setStatus(AccountStatus.ACTIVE);
        return repository.save(account);
    }
    public List<Account> findAll() {
        return repository.findAll();
    }
    public Account findById(Long id) {
        return repository.findById(id)
                .orElseThrow(() ->
                        new IllegalArgumentException(
                                "Compte introuvable : " + id));
    }
    public List<Account> findByCustomerId(Long customerId) {
        return repository.findByCustomerId(customerId);
    }
    public Account updateStatus(Long id, AccountStatus status) {
        Account account = findById(id);
        account.setStatus(status);
        return repository.save(account);
    }
    @Transactional
    public void debit(Long id, BigDecimal amount) {
        Account account = findById(id);
        if (account.getStatus() != AccountStatus.ACTIVE) {
            throw new IllegalStateException(
                    "Le compte n'est pas actif");
        }
        if (amount == null
                || amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException(
                    "Le montant doit être strictement positif");
        }
        if (account.getBalance().compareTo(amount) < 0) {
            throw new IllegalStateException(
                    "Solde insuffisant");
        }
        account.setBalance(account.getBalance().subtract(amount));
        repository.save(account);
    }
    @Transactional
    public void credit(Long id, BigDecimal amount) {
        Account account = findById(id);
        if (account.getStatus() != AccountStatus.ACTIVE) {
            throw new IllegalStateException("Le compte n'est pas actif");
        }
        if (amount == null
                || amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException(
                    "Le montant doit être strictement positif");
        }
        account.setBalance(account.getBalance().add(amount));
        repository.save(account);
    }
}